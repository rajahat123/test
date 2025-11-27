package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.dto.InventoryDTO;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    // API 51: Create inventory
    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO inventory = inventoryService.createInventory(inventoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }
    
    // API 52: Get inventory by ID
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id) {
        InventoryDTO inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }
    
    // API 53: Get inventory by product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryDTO> getInventoryByProductId(@PathVariable Long productId) {
        InventoryDTO inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventory);
    }
    
    // API 54: Get all inventory
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        List<InventoryDTO> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }
    
    // API 55: Get low stock items
    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryDTO>> getLowStockItems() {
        List<InventoryDTO> inventory = inventoryService.getLowStockItems();
        return ResponseEntity.ok(inventory);
    }
    
    // API 56: Update stock
    @PatchMapping("/product/{productId}/stock")
    public ResponseEntity<InventoryDTO> updateStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        InventoryDTO inventory = inventoryService.updateStock(productId, quantity);
        return ResponseEntity.ok(inventory);
    }
    
    // API 57: Reserve stock
    @PostMapping("/product/{productId}/reserve")
    public ResponseEntity<Boolean> reserveStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        boolean reserved = inventoryService.reserveStock(productId, quantity);
        return ResponseEntity.ok(reserved);
    }
    
    // API 58: Release stock
    @PostMapping("/product/{productId}/release")
    public ResponseEntity<Void> releaseStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        inventoryService.releaseStock(productId, quantity);
        return ResponseEntity.noContent().build();
    }
    
    // API 59: Deduct stock
    @PostMapping("/product/{productId}/deduct")
    public ResponseEntity<Void> deductStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        inventoryService.deductStock(productId, quantity);
        return ResponseEntity.noContent().build();
    }
    
    // VULNERABLE ENDPOINTS - SQL Injection
    @GetMapping("/search")
    public ResponseEntity<List<InventoryDTO>> searchInventory(@RequestParam String sku) {
        List<InventoryDTO> results = inventoryService.searchInventoryUnsafe(sku);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/location")
    public ResponseEntity<List<InventoryDTO>> findByLocation(@RequestParam String location) {
        List<InventoryDTO> results = inventoryService.findByLocation(location);
        return ResponseEntity.ok(results);
    }
    
    @DeleteMapping("/query")
    public ResponseEntity<Integer> deleteByQuery(@RequestParam String condition) {
        int deleted = inventoryService.deleteInventoryByQuery(condition);
        return ResponseEntity.ok(deleted);
    }
    
    // VULNERABLE ENDPOINT - Command Injection
    @GetMapping("/export")
    public ResponseEntity<String> exportData(@RequestParam String format) throws Exception {
        String result = inventoryService.exportInventory(format);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/backup")
    public ResponseEntity<String> backupData(@RequestParam String path) throws Exception {
        String result = inventoryService.backupInventory(path);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/command")
    public ResponseEntity<String> executeCommand(@RequestParam String file) {
        String result = inventoryService.executeSystemCommand(file);
        return ResponseEntity.ok(result);
    }
    
    // VULNERABLE ENDPOINT - Path Traversal
    @GetMapping("/file")
    public ResponseEntity<String> readFile(@RequestParam String filename) {
        String content = inventoryService.readInventoryFile(filename);
        return ResponseEntity.ok(content);
    }
    
    // VULNERABLE ENDPOINT - Information Disclosure
    @GetMapping("/system-info")
    public ResponseEntity<String> getSystemInfo() {
        String info = inventoryService.getSystemInfo();
        return ResponseEntity.ok(info);
    }
    
    // VULNERABLE ENDPOINT - Insecure Random
    @GetMapping("/generate-token")
    public ResponseEntity<String> generateToken() {
        String token = inventoryService.generateToken();
        return ResponseEntity.ok(token);
    }
    
    // VULNERABLE ENDPOINT - Open Redirect
    @GetMapping("/redirect")
    public String redirect(@RequestParam String url) {
        return inventoryService.redirectToUrl(url);
    }
}
