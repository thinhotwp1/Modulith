# 📦 Warehouse Modulith Demo

## 1️⃣ Mục tiêu của dự án
- Trình bày cách tổ chức code dạng **Modulith**: chia ứng dụng thành các module độc lập về logic.
- Các module **giao tiếp thông qua sự kiện (domain events)** thay vì gọi trực tiếp service của nhau.
- Giữ **low coupling** (ít phụ thuộc) để dễ dàng scale hoặc tách microservices sau này.

---

## 2️⃣ Cấu trúc dự án

```markdown
com.example.warehouse
│
├── application              # API layer (Controllers)
│   ├── OrderController       # REST API cho tạo đơn hàng
│   └── ReceivingController   # REST API cho nhập hàng vào kho
│
├── common.events             # Định nghĩa Domain Events giao tiếp giữa các service, khi chuyển thành microservices có thể dùng Message Queue
│   ├── OrderMessageEvent
│   ├── ReceivedMessageEvent
│   └── InventoryShippingMessageEvent
│
├── inventory                 # Module quản lý tồn kho
│   └── InventoryService
│
├── order                     # Module xử lý đơn hàng
│   └── OrderService
│
├── receiving                 # Module xử lý nhập hàng
│   └── ReceivingService
│
├── shipping                  # Module xử lý vận chuyển
│   └── ShippingService
│
└── SpringModulithApplication # Entry point

````

---

## 3️⃣ Chính sách khi dùng Spring Modulith (Architectural Policies)

### **1. Module boundaries**
- Mỗi module phải thể hiện rõ **API công khai** và phần logic **internal**.
- Không được gọi trực tiếp service của module khác, trừ khi thông qua API contract rõ ràng.
- Ưu tiên sử dụng **event-driven communication**.

### **2. Event-driven Communication**
- Giao tiếp giữa các module qua **Spring Application Events** (`ApplicationEventPublisher`).
- Các event là **immutable** (record/class final), đặt trong package `common.events`.
- Tên event nên thể hiện hành động nghiệp vụ (`OrderPlacedEvent`, `InventoryReservedEvent`).

### **3. Không vi phạm dependency**
- Một module không được phụ thuộc vòng tròn với module khác.
- Controller chỉ gọi service trong **module của chính nó**.
- Nếu cần dữ liệu từ module khác → publish event hoặc query thông qua API của module đó (nếu được phép).

### **4. Chuẩn bị cho Microservices**
- Không dùng trực tiếp `@Autowired` hoặc gọi service cross-module.
- Đảm bảo module có thể **chạy độc lập** khi cần tách ra service riêng.
- Event nội bộ có thể thay thế bằng Kafka/RabbitMQ khi scale-out.

---

## 4️⃣ Luồng xử lý nghiệp vụ

### **1. Nhập hàng (Receiving)**
1. Client gửi request `POST /api/receiving?sku=SKU123&qty=10`.
2. `ReceivingService` nhận hàng → publish `ReceivedMessageEvent`.
3. `InventoryService` lắng nghe event → cập nhật tồn kho.

### **2. Đặt hàng (Order)**
1. Client gửi request `POST /api/order?sku=SKU123&qty=2`.
2. `OrderService` publish `OrderMessageEvent`.
3. `InventoryService` lắng nghe → kiểm tra tồn kho:
   - Nếu đủ hàng → giảm tồn kho → publish `InventoryShippingMessageEvent`.
   - Nếu không đủ hàng → log cảnh báo, không gửi shipping.
4. `ShippingService` lắng nghe `InventoryShippingMessageEvent` → thực hiện vận chuyển.

---

## 5️⃣ Ưu điểm của kiến trúc Modulith trong dự án này
- **Clear boundaries**: Mỗi module có trách nhiệm riêng.
- **Event-driven**: Dễ dàng thay đổi hoặc thêm bước xử lý mà không sửa module khác.
- **Microservice-ready**: Khi tách ra service riêng, chỉ cần thay event nội bộ bằng message broker.

---

## 6️⃣ Yêu cầu hệ thống
- Java 17+
- Spring Boot 3.x
- Spring Modulith
- Maven/Gradle

---

## 7️⃣ Chạy ứng dụng
```bash
./mvnw spring-boot:run
````

---

## 8️⃣ API mẫu

### Đặt hàng:

```bash
curl -X POST "http://localhost:8080/api/order?sku=SKU123&qty=2"
```

### Nhập hàng:

```bash
curl -X POST "http://localhost:8080/api/receiving?sku=SKU123&qty=10"
```

---

## 9️⃣ Hướng phát triển tiếp theo

* Thêm **event tracing** bằng Spring Modulith Observability.
* Thay ApplicationEvent bằng Kafka/RabbitMQ để chạy phân tán.
* Bổ sung Saga pattern để xử lý luồng phức tạp hơn.

