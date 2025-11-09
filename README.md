# Point of Sale (POS) System - DOO_TP_Integrador

A comprehensive Point of Sale system built with JavaFX, implementing multiple design patterns and best practices in object-oriented design. This application provides a complete solution for sales management, inventory control, and customer administration.

## 📋 Description

This POS system is a desktop application developed as an integrative project for Object-Oriented Design (DOO). It features a dual-interface system for cashiers and administrators, complete with sales processing, discount management, inventory control, and reporting capabilities. The project demonstrates the practical implementation of design patterns including Builder, Strategy, State, and Facade patterns.

## ✨ Features

### Cashier Interface
- **Sales Management**: Create and process sales transactions
- **Product Scanning**: Add products to sales by scanning or manual entry
- **Payment Processing**: Support for multiple payment methods (Cash, Debit, Credit)
- **Real-time Calculations**: Automatic calculation of discounts and totals
- **Sale Confirmation**: Review and confirm sales before completion

### Administrator Interface
- **Dashboard**: Overview of system statistics and recent activity
- **Client Management**: Create, read, update, and delete customer information
- **Product Management**: Manage inventory, pricing, and stock levels
- **Discount Management**: Configure percentage and fixed-amount discounts
- **Cashier Management**: Manage cashier accounts and permissions
- **Reports**: Generate sales and inventory reports

### Core Features
- **User Authentication**: Secure login system for cashiers and administrators
- **Database Integration**: SQLite database for data persistence
- **Design Patterns Implementation**:
  - **Builder Pattern**: For constructing complex objects (Clients, Users)
  - **Strategy Pattern**: For payment processing methods
  - **State Pattern**: For managing sale lifecycle states
  - **Facade Pattern**: For simplified system interactions
  - **DAO Pattern**: For database access abstraction

## 🏗️ Architecture

The project follows a layered architecture with clear separation of concerns:

```
src/
├── builder/          # Builder pattern implementations
├── controladores/    # JavaFX controllers
├── dao/              # Data Access Objects
├── dto/              # Data Transfer Objects
├── enums/            # Enumerations (Gender, Discount Types)
├── estado/           # State pattern implementations (Sale states)
├── estrategia/       # Strategy pattern implementations (Payment methods)
├── fachada/          # Facade pattern implementation
├── modelos/          # Domain models
└── servicios/        # Business logic services
```

## 🛠️ Technologies

- **Java 17** - Programming language
- **JavaFX 13** - GUI framework
- **Maven** - Build and dependency management
- **SQLite 3.42** - Database
- **Lombok 1.18.30** - Boilerplate code reduction
- **JUnit 4** - Unit testing

## 📦 Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6+
- JavaFX 13 (included as Maven dependency)

## 🚀 Getting Started

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd DOO_TP_Integrador
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Database Setup

The database is located in the `dbDOOTPIntegrador/` directory. The application uses SQLite with the following structure:

- **Entities**: Person, Client, User, Cashier, Administrator, Product, Sale, Invoice, Discount, Address
- **Relationships**: Properly normalized with foreign key constraints
- **Sample Data**: Pre-populated database available in `dbDOOTPIntegrador/dooTPIntegradorDB.db`

### Default Credentials

The application comes with pre-configured user accounts. Check the database or documentation for login credentials.

## 🧪 Running Tests

Execute the test suite with:

```bash
mvn test
```

Test coverage includes:
- Model unit tests
- User authentication tests
- Sale workflow tests

## 📊 Database Schema

Key tables include:
- `Persona` - Base person information
- `Cliente` - Customer data
- `Usuario` - System users
- `Cajero` - Cashier accounts
- `Administracion` - Administrator accounts
- `Producto` - Product inventory
- `Venta` - Sales transactions
- `DetallesVenta` - Sale line items
- `Factura` - Invoices
- `Descuento` - Discount configurations

## 🎯 Design Patterns Used

1. **Builder Pattern** (`builder/`): Constructs complex objects like Clients and Users step by step
2. **Strategy Pattern** (`estrategia/`): Implements different payment methods (Cash, Debit, Credit)
3. **State Pattern** (`estado/`): Manages sale lifecycle (Created, Adding, PaymentEntered, Paid, Completed, Rejected)
4. **Facade Pattern** (`fachada/`): Provides simplified interface for sales operations
5. **DAO Pattern** (`dao/`): Abstracts database operations
6. **DTO Pattern** (`dto/`): Transfers data between layers

## 📝 Project Structure

- `/src/main/java` - Source code
- `/src/main/resources` - FXML views and resources
- `/src/test/java` - Unit tests
- `/dbDOOTPIntegrador` - Database files
- `/target` - Compiled classes and build artifacts

## 🔧 Configuration

The project uses Maven profiles and NetBeans configuration:
- JDK Platform: JDK 17
- Compiler: Java 17 with preview features enabled
- JavaFX Plugin: Version 0.0.4

## 🤝 Contributing

This is an academic project. If you'd like to contribute:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is an academic assignment for the Object-Oriented Design course.

## 👥 Authors

Universidad Blas Pascal - Object-Oriented Design Course

## 🙏 Acknowledgments

- Universidad Blas Pascal faculty and staff
- JavaFX community for excellent documentation
- SQLite for providing a robust embedded database solution

---

**Note**: This is an educational project demonstrating object-oriented design principles and design patterns in a real-world application context.
