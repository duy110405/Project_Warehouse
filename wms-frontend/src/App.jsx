import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import './utils/axiosConfig';

import Login from './pages/Login';
import Product from './pages/Product';
import Dashboard from './pages/Dashboard';
import Category from './pages/Category';
import Zone from './pages/Zone';
import Material from './pages/Material';
import Supply from './pages/SupplyPartner/SupplyManager';
import MainLayout from './pages/MainLayout';
import InboundProduct from './pages/InboundReceipt';



// COMPONENT BẢO VỆ ROUTE (Chặn người lạ)

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  const location = useLocation();
  if (!token) {
    // Nếu chưa đăng nhập, đá về trang Login. Lưu lại trang định vào để lúc login xong chuyển lại
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
  // Có token thì cho vào giao diện chính
  return children;
};
export default function App() {
 return (
    <Routes>
      {/* Route công khai: Trang Đăng nhập */}
      <Route path="/login" element={<Login />} />

      {/* Các Route cần bảo vệ: Bọc bên trong MainLayout (Sidebar + Header) */}
      <Route 
        path="/*" 
        element={
          <ProtectedRoute>
            <MainLayout>
              {/* Nội dung thay đổi dựa trên URL */}
              <Routes>
                <Route path="/" element={<Dashboard />} /> 
                <Route path="/products" element={<Product />} />
                <Route path="/categories" element={<Category />} />
                <Route path="/zones" element={<Zone />} />
                <Route path="/materials" element={<Material />} />
                <Route path="/supply" element={<Supply />} />
                <Route path="/inboundProduct" element={<InboundProduct />} />
              </Routes>
            </MainLayout>
          </ProtectedRoute>
        } 
      />
    </Routes>
  );
}
