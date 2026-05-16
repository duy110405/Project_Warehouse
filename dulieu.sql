
USE QLyKhoHang;
USE master;
GO

ALTER DATABASE QLyKhoHang
SET SINGLE_USER
WITH ROLLBACK IMMEDIATE;
GO

DROP DATABASE QLyKhoHang;
GO

 --- Dữ liệu tĩnh -- 
INSERT INTO QUYEN (IDQuyen, TenQuyen)
VALUES 
('ROLE01', 'ADMIN'),
('ROLE02', N'Quản lý kho tổng'),
('ROLE03', N'Quản lý kho thành phẩm'),
('ROLE04', N'Quản lý kho nguyên liệu');
INSERT INTO NGUOIDUNG(IDND, TenND,MatKhau,SDT,ChucVu,TenDN,IDQuyen)
VALUES
('ND001', 'ADMIN','$2a$12$6NXaspouvE33EB4oezYQV.tp4inxiljQL7XUFqoU9tVTSihSg.XR6','0987654321','ADMIN','ADMIN','ROLE01'),
('ND002',N'Trần Văn A','$2a$12$xMsSGF06peDhSqMhQlqhm.WS5tyHMqJoI3AQCQ3HViwHRwkAqQ7.u','0987654321',N'Quản lý kho tổng','quanlytong','ROLE02'),
('ND003',N'Trần Văn B','$2a$12$knSEaJhKsgTsgsIJQ05S5OR9LjZ6o.zzQWiTo3tw84rvv/BG6ZyL.','0987654321',N'Quản lý kho thành phẩm','quanlythanhpham','ROLE03'),
('ND004',N'Trần Văn C','$2a$12$dmIiP8HYngx6kjSsY.g.bOdMByMZSFt8ZuprKj7Fhl/8CB6NCOTyW','0987654321',N'Quản lý kho nguyên liệu','quanlynguyenlieu','ROLE04');

INSERT INTO KHUVUC(MaKhu, SucChua, SoLuongHienTai, TenKhu, LoaiKhu)
VALUES 
('K001', 1000, 0,N'Khu nguyên liệu điện thoại', 1),
('K002', 500 , 0,N'Khu nguyên liệu đồng hồ', 1),
('K003', 1200 , 0,N'Khu nguyên liệu laptop', 1),
('K004', 600 , 0,N'Khu thành phẩm máy tính', 2),
('K005', 600 , 0,N'Khu thành phẩm đồng hồ', 2),
('K006', 600 , 0,N'Khu thành phẩm laptop', 2);

INSERT INTO LOAIHANG(MaLH,TenLH)
VALUES
('LH001',N'Điện thoại'),
('LH002',N'Đồng hồ'),
('LH003',N'Laptop');

INSERT INTO XUONG(MaXuong,TrangThai,TenXuong,LoaiXuong)
VALUES
('X001',1,N'Xưởng điện thoại','Sản xuất'),
('X002',1,N'Xưởng đồng hồ','Sản xuất'),
('X003',1,N'Xưởng laptop','Sản xuất');

INSERT INTO NCC (MaNCC,DiaChi,Email,SoDienThoai,TrangThai,LoaiNCC,TenNCC)
VALUES 
('NCC001',N'Số 1-Khu Vsip',N'congtya@gmail.com','0987654321',1,'RAM - Chip' , N'Công ty A'),
('NCC002',N'Số 10-Khu Vsip',N'congtyb@gmail.com','0987654321',1,'Màn hình - KeyBoard - Chuột' , N'Công ty B'),
('NCC003',N'Số 20-Khu Vsip',N'congtyc@gmail.com','0987654321',1,'Cảm biến - Bo mạch' , N'Công ty C'),
('NCC004',N'Số 30-Khu Vsip',N'congtyd@gmail.com','0987654321',1,'Dây cáp' , N'Công ty D'),
('NCC005',N'Số 40-Khu Vsip',N'congtye@gmail.com','0987654321',1,'Vỏ - Nhựa' , N'Công ty E');

