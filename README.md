# 📦 Hệ Thống Quản Lý Kho Thông Minh (Warehouse Management System - WMS)

![React](https://img.shields.io/badge/React-18.x-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![SQL Server](https://img.shields.io/badge/SQL_Server-2022-CC2927?style=for-the-badge&logo=microsoft-sql-server&logoColor=white)
![Ant Design](https://img.shields.io/badge/Ant_Design-5.x-0170FE?style=for-the-badge&logo=ant-design&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.x-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)

## 📸 Giao diện ứng dụng

*(Ghi chú cho Dev: Thay thế đường dẫn `./images/...` bên dưới bằng ảnh chụp màn hình thực tế của bạn)*

### 1. Bảng Điều Khiển Tổng Quan (Dashboard)
> Giao diện Dark Mode hiện đại, cung cấp số liệu thống kê thời gian thực về tình trạng kho bãi, nhập/xuất và hoạt động của đối tác.
![Màn hình Dashboard](./images/dashboard.png) 

### 2. Quản Lý Hóa Đơn & Tự Động Hóa Phiếu Xuất
> Tính năng Auto-Fill thông minh tự động trích xuất sản phẩm từ hóa đơn bán hàng vào phiếu xuất kho, loại bỏ việc nhập liệu thủ công.
![Màn hình Hóa Đơn & Phiếu Xuất](./images/invoice_outbound.png)

### 3. Cấu Trúc Kho Bãi (Zone Layout)
> Quản lý trực quan theo từng phân khu, hiển thị thanh tiến trình sức chứa (Capacity) và cảnh báo khi tải trọng vượt mức.
![Màn hình Khu Vực Kho](./images/zone_layout.png)

---
**Warehouse Management System (WMS)** là một giải pháp quản trị kho bãi công nghiệp khép kín. Điểm cốt lõi của hệ thống nằm ở khả năng **đồng bộ hóa dữ liệu tuyệt đối** giữa bộ phận Bán hàng (Sales) và bộ phận Vận hành Kho (Operations), đảm bảo tính toàn vẹn thông tin và tối ưu hóa luồng chu chuyển hàng hóa.

---

## ✨ Tính năng nổi bật
- **Quản lý Vòng Đời Chứng Từ Chặt Chẽ:** Kiểm soát luồng trạng thái từ Hóa đơn bán hàng (`Mới tạo` -> `Đã thanh toán` -> `Hoàn thành`) đến Phiếu xuất kho (`Nháp` -> `Đã xuất` -> `Hủy`), đảm bảo không có bước sóng nào bị bỏ lỡ.
- **Tự Động Hóa Kế Toán Kho (Auto-Fill Logic):** Khi tạo Phiếu xuất, chỉ cần chọn Hóa đơn tham chiếu, hệ thống tự động đổ toàn bộ danh sách sản phẩm, số lượng và đơn giá; thủ kho chỉ cần chỉ định Vị trí (Zone) xuất hàng.
- **Fail-Safe & Ràng Buộc Kép:** - Khóa chặt mọi giao dịch nếu phát hiện âm kho. 
  - Tự động cộng/trừ tải trọng khu vực kho (`currentLoad`) khi duyệt phiếu nhập/xuất. 
  - Xử lý thông minh việc gỡ liên kết giúp Hóa đơn không bị treo khi hủy Phiếu xuất.
- **Quản Trị Danh Mục & Đối Tác:** Quản lý tập trung Khách hàng, Nhà cung cấp, Nguyên liệu (Vật tư) và Thành phẩm thương mại. Cung cấp bộ lọc tìm kiếm thông minh kết hợp truy vấn Server-Side.

---

## 🛠 Công nghệ sử dụng
### 1. Frontend
- **Framework:** ReactJS (Build tools: Vite)
- **UI Library:** Ant Design (AntD) kết hợp Tailwind CSS
- **Giao tiếp HTTP:** Axios
- **Icons:** Lucide React

### 2. Backend
- **Framework:** Java Spring Boot
- **ORM:** Spring Data JPA / Hibernate
- **Mapper:** MapStruct & Lombok
- **Database:** Microsoft SQL Server

---

## 🚀 Hướng dẫn cài đặt và chạy dự án (Localhost)

### Yêu cầu môi trường (Prerequisites)
- [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) trở lên.
- [Node.js](https://nodejs.org/) v18+ trở lên.
- Microsoft SQL Server 2019+.

### Bước 1: Khởi tạo Cơ sở dữ liệu (Database)
1. Mở SQL Server Management Studio (SSMS).
2. Tạo một database mới với tên: `WarehouseDB`.
3. *(Lựa chọn)* Nếu có file script SQL, hãy import vào. Nếu không, cấu hình Spring Boot JPA (`update` mode) sẽ tự động tạo cấu trúc bảng khi chạy lần đầu.

### Bước 2: Chạy Backend (Spring Boot)
1. Mở thư mục `backend` bằng IDE (IntelliJ IDEA / Eclipse / VS Code).
2. Kiểm tra file `application.properties` (hoặc `application.yml`) để đảm bảo thông tin kết nối SQL Server (username/password) khớp với máy của bạn.
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=WarehouseDB;encrypt=true;trustServerCertificate=true
   spring.datasource.username=YOUR_SQL_USERNAME
   spring.datasource.password=YOUR_SQL_PASSWORD
3. Chạy file khởi động `VD : WarehouseApplication.java`.
4. Backend sẽ khởi chạy tại cổng: `http://localhost:8080`.

### Bước 3: Chạy Frontend (ReactJS)
1. Mở terminal, trỏ đường dẫn vào thư mục `frontend`.
2. Cài đặt các gói thư viện phụ thuộc:
   ```bash
   npm install
3.Khởi chạy ứng dụng: npm run dev
4.Frontend sẽ chạy tại cổng: http://localhost:5173. Mở đường dẫn này trên trình duyệt (Chrome/Edge) để sử dụng.

## Tài khoản demo
| Vai trò | Tài khoản (Username) | Mật khẩu (Password) | Chức năng chính |
|---|---|---|---|
| Quản trị viên | ADMIN | 123456 | Toàn quyền cấu hình, quản lý kho, xem báo cáo thống kê |
| Quản lý kho tổng | quanlytong | 123456 | quyền giống admin nhưng không được quản lý tài khoản người dùng |
| Quản lý kho thành phẩm | quanlythanhpham | 123456 | Quản lý kho thành phẩm  |
| Quản lý kho nguyên liệu | quanlynguyenlieu | 123456 | Quản lý kho nguyên liệu |

## đội ngũ phát triển 
1.Trần Khánh Duy
