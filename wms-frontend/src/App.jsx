import { useState } from "react";
import { 
  LayoutGrid, Package, ShoppingCart, Users, Truck, 
  HeadphonesIcon, BarChart2, Settings, Archive, ChevronDown, ChevronRight ,
   List, User , BookMinus ,ArchiveRestore ,PackageOpen
} from 'lucide-react';
import { Routes, Route, Link } from 'react-router-dom';

import Product from './pages/Product';
import Dashboard from './pages/Dashboard';
import Category from './pages/Category';
import Zone from './pages/Zone';
import Material from './pages/Material';
import Supply from './pages/SupplyPartner/SupplyManager'

export default function App() {
  const [openWarehouse, setOpenWarehouse] = useState(false);
  const [openOperations, setOpenOperations] = useState(false);
  const [openSales, setOpenSales] = useState(false);
  return (
    <div className="flex h-screen bg-[#0B1120] text-white font-sans overflow-hidden">
      
      {/* SIDEBAR BÊN TRÁI */}
      <aside className="w-[260px] bg-[#0F172A] border-r border-slate-800 flex flex-col">
        {/* Logo */}
        <div className="p-6 flex items-center gap-3">
          <div className="bg-blue-600 p-2 rounded-lg">
            <Archive size={24} className="text-white" />
          </div>
          <span className="text-xl font-bold tracking-wide">WMS</span>
        </div>

        {/* Menu Items */}
       <nav className="flex-1 px-4 py-2 space-y-1 overflow-y-auto custom-scrollbar">
          {/* Menu Dashboard */}
          <Link to="/" className="flex items-center gap-3 px-4 py-3 bg-blue-600 text-white rounded-xl shadow-[0_0_15px_rgba(37,99,235,0.3)]">
            <LayoutGrid size={20} />
            <span className="font-medium">Bảng điều khiển</span>
          </Link>

           {/* Menu Active Orders */}
          <Link to="/" className="flex items-center gap-3 px-4 py-3 bg-blue-600 text-white rounded-xl shadow-[0_0_15px_rgba(37,99,235,0.3)]">
            <List size={20} />
            <span className="font-medium">Đơn hàng đang xử lý</span>
          </Link>

          {/* NHÓM 1: NGHIỆP VỤ KHO (Operations) */}
      <div>
          <button
        onClick={() => setOpenOperations(!openOperations)}
        className="w-full flex items-center justify-between px-4 py-2 text-slate-400 hover:text-white">
        <span className="text-xs font-semibold uppercase tracking-wider">
          Nghiệp vụ
        </span>
        {openOperations ? (
          <ChevronDown size={16} />
        ) : (
          <ChevronRight size={16} />
        )}
      </button>
      {/* DROPDOWN */}
      {openOperations && (
        <div className="mt-1 ml-8 space-y-1">
          <MenuItem icon={<Package size={20} />} label="Sản phẩm" to="/products" />
          <MenuItem icon={<ArchiveRestore size={20} />} label="Nhập kho" to="/materials" /> 
          <MenuItem icon={<PackageOpen size={20} />} label="Xuất kho"/> 
        </div>
        )}
      </div>

       {/* NHÓM 2: KINH DOANH & ĐƠN HÀNG (Sales) */}
      <div>
          <button
        onClick={() => setOpenSales(!openSales)}
        className="w-full flex items-center justify-between px-4 py-2 text-slate-400 hover:text-white">
        <span className="text-xs font-semibold uppercase tracking-wider">
          Kinh doanh
        </span>
        {openSales ? (
          <ChevronDown size={16} />
        ) : (
          <ChevronRight size={16} />
        )}
      </button>
      {/* DROPDOWN */}
      {openSales && (
        <div className="mt-1 ml-8 space-y-1">
          <MenuItem icon={<ShoppingCart size={20} />} label="Hóa đơn" to="/invoices" /> 
          <MenuItem icon={<Users size={20} />} label="Khách hàng" />
          <MenuItem icon={<Truck size={20} />} label="Nguồn cung ứng" to="/Supply" />
          <MenuItem icon={<HeadphonesIcon size={20} />} label="Chăm sóc khách hàng" />
        </div>
        )}
       </div>
        
        {/* NHÓM 3: BÁO CÁO & HỆ THỐNG (System & Setup) */}  
    <div>
      <button
        onClick={() => setOpenWarehouse(!openWarehouse)}
        className="w-full flex items-center justify-between px-4 py-2 text-slate-400 hover:text-white">
        <span className="text-xs font-semibold uppercase tracking-wider">
          Kho
        </span>
        {openWarehouse ? (
          <ChevronDown size={16} />
        ) : (
          <ChevronRight size={16} />
        )}
      </button>
      {/* DROPDOWN */}
      {openWarehouse && (
        <div className="mt-1 ml-8 space-y-1">
          <MenuItem icon={<Settings size={20} />} label="Thiết lập khu vực" to="/zones" />
          <MenuItem icon={<BookMinus size={20} />} label="Thiết lập danh mục" to="/categories" />
          <MenuItem icon={<BarChart2 size={20} />} label="Báo cáo quản lý" />
          <MenuItem icon={<User size={20} />} label="Người dùng" />
        </div>
      )}
    </div>

        </nav>
      </aside>

      {/* PHẦN NỘI DUNG CHÍNH BÊN PHẢI */}
      <main className="flex-1 flex flex-col relative overflow-hidden">
        
        {/* Header */}
        <header className="h-24 px-8 border-b border-slate-800/50 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <div className="p-3 bg-blue-500/10 rounded-xl">
              <Archive size={28} className="text-blue-500" />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-white tracking-wide">Warehouse Command Center</h1>
              <p className="text-slate-400 text-sm mt-1">Dark Store Operations</p>
            </div>
          </div>

          <div className="flex items-center gap-4">
            {/* Dropdown giả */}
            <div className="flex items-center gap-2 bg-[#1E293B] px-4 py-2 rounded-lg border border-slate-700 cursor-pointer">
              <span className="text-sm font-medium">Today</span>
              <ChevronDown size={16} className="text-slate-400" />
            </div>
            {/* System Status */}
            <div className="flex items-center gap-2 bg-[#0F172A] px-4 py-2 rounded-lg border border-slate-700">
              <div className="w-2 h-2 rounded-full bg-emerald-500 shadow-[0_0_10px_rgba(16,185,129,0.5)]"></div>
              <span className="text-sm font-medium text-slate-300">System Active</span>
            </div>
          </div>
        </header>

        {/* Dashboard Content (Chỗ này bài sau mình sẽ làm các ô vuông) */}
       <div className="flex-1 p-8 overflow-y-auto">
           <Routes>
              {/* Nếu gõ localhost:5173/ thì hiện Dashboard */}
              <Route path="/" element={<Dashboard />} /> 
               {/* Nếu gõ localhost:5173/products thì hiện trang Products */}
              <Route path="/products" element={<Product />} />
              {/* Nếu gõ localhost:5173/categories thì hiện trang Danh mục */}
              <Route path="/categories" element={<Category />} />
              {/* Nếu gõ localhost:5173/zones thì hiện trang Setup Zones */}
              <Route path="/zones" element={<Zone />} />
              <Route path="/materials" element={<Material />} />
              <Route path="/supply" element={<Supply />} />
             
           </Routes>
        </div>

      </main>
    </div>
  );
}

// Component phụ để vẽ Menu cho gọn code
function MenuItem({ icon, label, to }) {
  return (
    <Link to={to} className="flex items-center gap-3 px-4 py-3 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-xl transition-all duration-200">
      {icon}
      <span className="font-medium">{label}</span>
    </Link>
  );
}