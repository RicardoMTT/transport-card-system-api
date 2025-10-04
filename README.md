# 🚍 Transport Card Management System

Aplicación completa para la **gestión de tarjetas de transporte** que permite consultar saldos, realizar recargas y registrar usos de las tarjetas del **Metro** y **Corredor**.  

Frontend desarrollado en **Angular** y backend en **Spring Boot (WebFlux + R2DBC + MySQL)**.  

---

## ✨ Características principales

### 📌 Vista Principal
- Visualización de tarjetas disponibles (**Metro: S/ 1.50**, **Corredor: S/ 2.40**).
- Saldos actualizados en tiempo real.
- Indicadores de estado:
  - ✅ Saldo bueno  
  - ⚠️ Saldo medio  
  - ❌ Saldo bajo
- Cálculo automático de viajes disponibles.

### 💳 Recarga de Tarjetas
- Selección visual de la tarjeta.
- Montos rápidos predefinidos (**S/ 10, 20, 30, 50**).
- Input personalizado con validaciones.
- Resumen antes de confirmar la recarga.

### 🚌 Uso de Tarjetas
- Selección de número de pasajeros (1 a 10).
- Cálculo automático: pasajeros × tarifa.
- Validación de saldo suficiente.
- Advertencias de saldo bajo.

---

## 🛠️ Tecnologías utilizadas

### Frontend
- [Angular](https://angular.io/)
- [TypeScript](https://www.typescriptlang.org/)

### Backend
- [Spring Boot](https://spring.io/projects/spring-boot) (WebFlux)
- [R2DBC](https://r2dbc.io/) (conexión reactiva a BD)
- [MySQL](https://www.mysql.com/) (almacenamiento)

### Otros
- Maven
- GitHub (control de versiones)
- Postman 

---

## 🗄️ Modelo de Base de Datos

- **cards** → Información de tarjetas (tipo, saldo, estado).  
- **recharges** → Historial de recargas.  
- **transactions** → Historial de usos/consumos.

---

## 📡 Endpoints principales (API REST)

### Cards
- `GET /api/cards` → Listar tarjetas.
- `GET /api/cards/{id}` → Obtener tarjeta por ID.
- `POST /api/cards` → Crear nueva tarjeta.
- `PUT /api/cards/{id}` → Actualizar tarjeta.
- `DELETE /api/cards/{id}` → Eliminar tarjeta.

### Recharges
- `POST /api/recharges` → Recargar una tarjeta.
- `GET /api/recharges/card/{cardId}` → Listar recargas de una tarjeta.

### Transactions
- `POST /api/transactions` → Registrar uso de tarjeta.
- `GET /api/transactions/card/{cardId}` → Listar usos de una tarjeta.

---

### Tablas
```
CREATE TABLE cards (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  type VARCHAR(20) NOT NULL,
  balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  fare DECIMAL(10,2) NOT NULL,
  status VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE recharges (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  card_id BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
);

CREATE TABLE usages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  card_id BIGINT NOT NULL,
  passengers INT NOT NULL,
  total_fare DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
);

```
## 🚀 Cómo ejecutar el proyecto

### 1. Clonar repositorio
```bash
git clone https://github.com/tu-usuario/transport-card-system.git
cd transport-card-system
