## 🏗️ Tổng quan kiến trúc

Đây là một hệ thống microservices phức tạp gồm 9 dịch vụ độc lập, tất cả được xây dựng và chạy đồng thời bằng **Docker Compose**.

Luồng hoạt động chính của hệ thống như sau:

1.  Mọi yêu cầu (request) từ bên ngoài đều phải đi qua **API Gateway (Cổng 8090)**. Đây là người gác cổng, chịu trách nhiệm xác thực **JWT Token** và phân quyền người dùng (vai trò `ADMIN` hay `OPERATOR`).
2.  Sau khi xác thực, Gateway sẽ điều hướng request đến các service nghiệp vụ tương ứng (ví dụ: request về động vật sẽ tới Animal Service).
3.  Các service gửi log và event thông qua một Message Broker trung tâm là **ActiveMQ Artemis**.
4.  Dữ liệu của toàn bộ hệ thống được lưu trữ trong cơ sở dữ liệu **PostgreSQL**.
5.  Một service đặc biệt là **Logging Service (8087)** sẽ lắng nghe và thu thập log (cả API log lẫn system log) từ tất cả các service nghiệp vụ khác (trừ Gateway) để quản lý tập trung.

-----

## 🚀 Chức năng các Service

Hệ thống được chia thành các dịch vụ nhỏ, mỗi dịch vụ đảm nhận một chức năng riêng biệt:

  * **Gateway (8090):** Cổng API chính, lọc request, xác thực JWT và phân quyền.
  * **Auth (8084):** Xử lý mọi thứ liên quan đến người dùng: đăng nhập, đăng ký, tạo và xác thực token JWT.
  * **Animal (8081):** Quản lý thông tin động vật (CRUD, loài, tuổi, giới tính...).
  * **Enclosure (8089):** Quản lý chuồng trại (sức chứa, vị trí, loại chuồng...).
  * **Feeding (8088):** Quản lý lịch ăn, loại thức ăn, và phân công người cho ăn.
  * **Health (8086):** Theo dõi hồ sơ sức khỏe, lịch sử khám bệnh, cân nặng, và chỉ định thú y.
  * **Staff (8085):** Quản lý nhân viên (thú y, người chăm sóc), vai trò và thông tin liên lạc.
  * **Logging (8087):** Như đã nói ở trên, đây là dịch vụ ghi log tập trung cho toàn hệ thống (trừ Gateway).
  * **Report (8080):** Dịch vụ chuyên tạo và xuất các báo cáo, thống kê (ví dụ: thống kê động vật, báo cáo sức khỏe...).

**Lưu ý:** Các service nghiệp vụ cốt lõi (Animal, Enclosure, Feeding, Health, Staff) đều áp dụng kiến trúc **Axon (CQRS)** để tách biệt logic ghi và đọc dữ liệu.

-----

## 📡 Phân quyền API (Endpoints)

Tất cả các API đều phải được gọi qua Gateway tại `http://localhost:8090`.

### 1\. Công khai (Public)

Các API này không cần xác thực.

```http
POST /api/auth/login     # Đăng nhập
POST /api/auth/register  # Đăng ký
GET  /api/auth/verify    # Xác thực token
```

### 2\. Yêu cầu xác thực (OPERATOR & ADMIN)

Các API nghiệp vụ chính, yêu cầu token JWT hợp lệ với vai trò `OPERATOR` hoặc `ADMIN`.

```http
# Quản lý động vật
GET    /api/animals
POST   /api/animals
PUT
...

# Quản lý nhân viên
GET    /api/staff
POST   /api/staff
PUT
...

# Quản lý sức khỏe, chuồng trại, thức ăn...
/api/health
/api/enclosures
/api/feeding
```

### 3\. Chỉ ADMIN

Các API quản trị hệ thống, chỉ vai trò `ADMIN` mới có quyền truy cập.

```http
# Xuất báo cáo excel, pdf
GET /api/reports

# Xem logs api của các service
GET /api/logs/api

# Xem logs system của các service
GET /api/logs/sys
```

-----

## 🛠️ Các kiến thức và kỹ thuật cốt lõi

Đây là những kỹ thuật và khái niệm quan trọng được áp dụng trong dự án này:

  * **Axon Framework (CQRS):**
    Áp dụng mô hình **CQRS** (Tách biệt Trách nhiệm Truy vấn và Lệnh). Logic nghiệp vụ (như "thêm động vật") là **Command**, và logic lấy dữ liệu (như "xem danh sách động vật") là **Query**. Axon giúp quản lý các Command, Event và Query này một cách rõ ràng.

  * **ActiveMQ Artemis (Message Broker):**
    Đây là trung tâm giao tiếp **bất đồng bộ** (event-driven) của hệ thống. Thay vì các service gọi trực tiếp lẫn nhau (gây phụ thuộc và trễ), chúng sẽ giao tiếp qua hàng đợi tin nhắn (Message Queue). Có hai ví dụ rõ nét trong dự án này:

    1.  **Ghi Log:** Khi các service (như `Animal`, `Staff`...) muốn ghi log, chúng không cần gọi API của `Logging Service`. Thay vào đó, chúng chỉ cần gửi lên một tin nhắn chứa nội dung log vào một hàng đợi (queue) trên ActiveMQ. `Logging Service` sẽ lắng nghe hàng đợi đó và xử lý việc lưu log vào database một cách độc lập.
    2.  **Đồng bộ nghiệp vụ:** Khi `Animal Service` xử lý nghiệp vụ thêm một con vật vào chuồng, nó sẽ bắn ra một sự kiện (event) như `AnimalCreatedEvent`. `Enclosure Service` (vốn quản lý số lượng động vật trong chuồng) sẽ lắng nghe sự kiện này và tự động cập nhật lại số lượng, đảm bảo dữ liệu nhất quán mà không cần `Animal Service` phải gọi trực tiếp sang `Enclosure Service`.

  * **Dead Letter Queue (DLQ):**
    Đây là một tính năng của ActiveMQ. Khi một tin nhắn (message) bị xử lý lỗi lặp đi lặp lại (ví dụ: service nhận bị sập), thay vì vứt bỏ hoặc làm tắc nghẽn hàng đợi, ActiveMQ sẽ tự động chuyển tin nhắn lỗi đó sang một hàng đợi riêng gọi là "Hàng đợi chết" (DLQ). Điều này giúp hệ thống tiếp tục chạy và lập trình viên có thể vào DLQ để kiểm tra và xử lý lỗi sau.

  * **Enqueue Policy (Chính sách xếp hàng):**
    Là các quy tắc định nghĩa cách ActiveMQ xử lý khi hàng đợi bị đầy (ví dụ: chặn không cho gửi thêm, hoặc vứt bỏ tin nhắn cũ nhất để nhận tin nhắn mới).

  * **Filter (Servlet Filter):**
    Đây là một bộ lọc chạy *trước khi* request đi vào Spring (DispatcherServlet). Trong dự án này, nó được dùng với một mục đích rất cụ thể: "đọc trộm" và **lưu lại nội dung (body) của request**. Vì body của request chỉ có thể đọc được một lần, Filter sẽ đọc, sao lưu nó lại (vào request attribute) để các thành phần chạy sau (như Interceptor) có thể sử dụng.

  * **HandlerInterceptor (Spring):**
    Đây là một bộ lọc chạy *sau khi* request đã vào Spring nhưng *trước khi* tới Controller. Nó được dùng để thực hiện các tác vụ chung như ghi log. Nó sẽ lấy nội dung body mà **Filter** đã sao lưu trước đó để ghi lại log request một cách đầy đủ.

  * **Dockerfile (Multi-stage build):**
    Đây là kỹ thuật tối ưu hóa Docker image. Thay vì dùng một Dockerfile chứa cả môi trường build (Maven, JDK) và môi trường chạy (JRE), multi-stage build chia làm 2 giai đoạn:

    1.  **Giai đoạn 1:** Dùng image `maven:3.9-jdk-17` để build code Java ra file `.jar`.
    2.  **Giai đoạn 2:** Dùng một image siêu nhẹ (như `eclipse-temurin:17-jre-alpine`) và *chỉ copy* file `.jar` từ giai đoạn 1 vào.
        Kết quả là image cuối cùng (dùng để chạy) có dung lượng cực kỳ nhỏ và an toàn hơn.

  * **Docker Compose:**
    Là công cụ cho phép định nghĩa và chạy toàn bộ kiến trúc microservices (gồm 9 service + PostgreSQL + ActiveMQ) chỉ bằng một file `docker-compose.yaml`.

  * **Apache POI & iTextPDF:**
    Đây là hai thư viện Java được sử dụng trong **Report Service** để tạo và xuất file:

      * **Apache POI:** Dùng để tạo và xuất các báo cáo, thống kê dưới dạng file **Excel** (`.xlsx`).
      * **iTextPDF:** Dùng để tạo và xuất các báo cáo tương tự dưới dạng file **PDF**.

-----

## Getting Started

### Quick Start with Docker Compose

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd AnimalProject
   ```

2. **Build and start all services**
   ```bash
   docker-compose up --build
   ```

   Or run in detached mode:
   ```bash
   docker-compose up -d --build
   ```

3. **Verify all services are running**
   ```bash
   docker-compose ps
   ```

4. **Check service logs**
   ```bash
   # All services
   docker-compose logs -f

   # Specific service
   docker-compose logs -f gateway
   ```

### Stop Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (cleans database)
docker-compose down -v
```