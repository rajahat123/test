# E-Commerce Frontend Application

This is an Angular 15 application that integrates with the E-Commerce microservices backend.

## Prerequisites

- Node.js 16.x or higher
- npm 8.x or higher

## Installation

```bash
npm install
```

## Development Server

Run `ng serve` or `npm start` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Features

- User Management (Registration, Login, Profile)
- Product Catalog with Categories and Reviews
- Shopping Cart
- Order Management
- Payment Processing
- Inventory Management

## Backend Services

The application connects to the following microservices through API Gateway (port 8080):

- User Service (port 8081)
- Product Service (port 8082)
- Order Service (port 8083)
- Payment Service (port 8084)
- Inventory Service (port 8085)

## Configuration

Update the API base URL in `src/environments/environment.ts` to point to your backend services.
