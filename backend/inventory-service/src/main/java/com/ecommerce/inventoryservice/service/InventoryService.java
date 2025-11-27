package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.InventoryDTO;
import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final JdbcTemplate jdbcTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Hardcoded credentials - SECURITY ISSUE
    private static final String DB_PASSWORD = "admin123";
    private static final String API_KEY = "sk-1234567890abcdef";
    private static final String SECRET_TOKEN = "my_secret_token_12345";
    
    @Transactional
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        inventory.setProductId(inventoryDTO.getProductId());
        inventory.setProductSku(inventoryDTO.getProductSku());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setReservedQuantity(inventoryDTO.getReservedQuantity() != null ? inventoryDTO.getReservedQuantity() : 0);
        inventory.setReorderLevel(inventoryDTO.getReorderLevel());
        inventory.setReorderQuantity(inventoryDTO.getReorderQuantity());
        inventory.setWarehouseLocation(inventoryDTO.getWarehouseLocation());
        
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDTO(savedInventory);
    }
    
    public InventoryDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventory not found"));
        return convertToDTO(inventory);
    }
    
    public InventoryDTO getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        return convertToDTO(inventory);
    }
    
    public List<InventoryDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    // Additional SQL Injection with user input - CRITICAL SECURITY ISSUE
    public List<InventoryDTO> findByLocation(String location) {
        String sql = "SELECT * FROM inventory WHERE warehouse_location = '" + location + "'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            InventoryDTO dto = new InventoryDTO();
            dto.setId(rs.getLong("id"));
            dto.setWarehouseLocation(rs.getString("warehouse_location"));
            return dto;
        });
    }
    
    public List<InventoryDTO> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public InventoryDTO updateStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        
        inventory.setQuantity(inventory.getQuantity() + quantity);
        if (quantity > 0) {
            inventory.setLastRestockedAt(LocalDateTime.now());
        }
        
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToDTO(updatedInventory);
    }
    
    @Transactional
    public boolean reserveStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        
        int availableQuantity = inventory.getQuantity() - inventory.getReservedQuantity();
        
        if (availableQuantity < quantity) {
            return false;
        }
        
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
        return true;
    }
    
    @Transactional
    public void releaseStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
    }
    
    @Transactional
    public void deductStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for product"));
        
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryRepository.save(inventory);
    }
    
    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProductId());
        dto.setProductSku(inventory.getProductSku());
        dto.setQuantity(inventory.getQuantity());
        dto.setReservedQuantity(inventory.getReservedQuantity());
        dto.setReorderLevel(inventory.getReorderLevel());
        dto.setReorderQuantity(inventory.getReorderQuantity());
        dto.setWarehouseLocation(inventory.getWarehouseLocation());
        dto.setLastRestockedAt(inventory.getLastRestockedAt() != null ? inventory.getLastRestockedAt().format(formatter) : null);
        dto.setCreatedAt(inventory.getCreatedAt() != null ? inventory.getCreatedAt().format(formatter) : null);
        dto.setUpdatedAt(inventory.getUpdatedAt() != null ? inventory.getUpdatedAt().format(formatter) : null);
        return dto;
    }
    
    // SQL Injection vulnerability - SECURITY ISSUE
    public List<InventoryDTO> searchInventoryUnsafe(String productSku) {
        String query = "SELECT * FROM inventory WHERE product_sku = '" + productSku + "'";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            InventoryDTO dto = new InventoryDTO();
            dto.setId(rs.getLong("id"));
            dto.setProductId(rs.getLong("product_id"));
            dto.setProductSku(rs.getString("product_sku"));
            return dto;
        });
    }
    
    // More SQL Injection patterns
    public int deleteInventoryByQuery(String condition) {
        String deleteQuery = "DELETE FROM inventory WHERE " + condition;
        return jdbcTemplate.update(deleteQuery);
    }
    
    public void updateInventoryDynamic(String field, String value, Long id) {
        String updateSql = "UPDATE inventory SET " + field + " = '" + value + "' WHERE id = " + id;
        jdbcTemplate.execute(updateSql);
    }
    
    // Command Injection vulnerability - SECURITY ISSUE
    public String executeSystemCommand(String filename) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("cat /tmp/" + filename);
            return "Command executed";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
    
    // More Command Injection patterns
    public String backupInventory(String backupPath) throws IOException {
        String cmd = "cp /var/inventory/data.db " + backupPath;
        Runtime.getRuntime().exec(cmd);
        return "Backup created at: " + backupPath;
    }
    
    public String exportInventory(String format) throws IOException {
        String[] command = {"/bin/sh", "-c", "export_tool --format=" + format};
        Runtime.getRuntime().exec(command);
        return "Export completed";
    }
    
    // Path Traversal vulnerability - SECURITY ISSUE
    public String readInventoryFile(String filename) {
        try {
            FileInputStream fis = new FileInputStream("/var/inventory/" + filename);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            return new String(data);
        } catch (IOException e) {
            return "Error reading file";
        }
    }
    
    // Weak cryptography - SECURITY ISSUE
    public String encryptData(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    // Insecure Random - SECURITY ISSUE
    public String generateToken() {
        Random random = new Random();
        return "TOKEN-" + random.nextInt(999999);
    }
    
    // Unvalidated Redirect - SECURITY ISSUE
    public String redirectToUrl(String url) {
        return "redirect:" + url;
    }
    
    // Resource leak - SECURITY ISSUE
    public String queryDatabase(String productId) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/inventory", 
                "root", 
                DB_PASSWORD
            );
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM inventory WHERE product_id = " + productId);
            if (rs.next()) {
                return rs.getString("product_sku");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // XXE (XML External Entity) vulnerability - SECURITY ISSUE
    public void parseXmlInventory(String xmlContent) {
        try {
            javax.xml.parsers.DocumentBuilderFactory factory = 
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(xmlContent.getBytes());
            org.w3c.dom.Document doc = builder.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Information disclosure - SECURITY ISSUE
    public String getSystemInfo() {
        return "Database: jdbc:mysql://prod-db-01.internal:3306/inventory, " +
               "User: admin, Password: " + DB_PASSWORD + ", " +
               "API Key: " + API_KEY + ", " +
               "Secret: " + SECRET_TOKEN;
    }
    
    // Deserialization vulnerability - SECURITY ISSUE
    public Object deserializeData(byte[] data) {
        try {
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(data);
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
