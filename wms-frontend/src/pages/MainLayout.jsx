import { useState } from "react";
import { 
  LayoutGrid, Package, Users, Truck, 
  HeadphonesIcon, BarChart2, Settings, Archive, ChevronDown, ChevronRight,
  List, User, BookMinus, ArchiveRestore, PackageOpen, Cog, FileText, LogOut
} from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';


const MenuItem = ({ icon, label, to }) => {
  return (
    <Link to={to} className="flex items-center gap-3 px-4 py-2.5 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-xl transition-all duration-200">
      {icon}
      <span className="font-medium text-sm">{label}</span>
    </Link>
  );
};


export default function MainLayout({ children }) {
  const navigate = useNavigate();

  // Quản lý trạng thái đóng/mở của 4 Dropdown
  const [openInbound, setOpenInbound] = useState(false);
  const [openOutbound, setOpenOutbound] = useState(false);
  const [openSales, setOpenSales] = useState(false);
  const [openWarehouse, setOpenWarehouse] = useState(false);

  // Lấy tên User từ LocalStorage (Nếu không có thì để rỗng)
  const currentUsername = localStorage.getItem('username') || 'Người dùng';

  // Hàm xử lý Đăng xuất
  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    navigate('/login'); // Đá về trang login
  };

  return (
    <div className="flex h-screen bg-[#0B1120] text-white font-sans overflow-hidden">
      
      {/* SIDEBAR BÊN TRÁI */}
      <aside className="w-[260px] min-w-[260px] bg-[#0F172A] border-r border-slate-800 flex flex-col">
        {/* Logo */}
        <div className="p-6 flex items-center gap-3">
          <div className="bg-blue-600 p-2 rounded-lg">
            <Archive size={24} className="text-white" />
          </div>
          <span className="text-xl font-bold tracking-wide">WMS</span>
        </div>

        {/* Menu Items */}
        <nav className="flex-1 px-4 py-2 space-y-2 overflow-y-auto custom-scrollbar pb-6">
          
          {/* NHÓM FLAT MENUS (Không có dropdown)        */}
          <Link to="/" className="flex items-center gap-3 px-4 py-3 bg-blue-600 text-white rounded-xl shadow-[0_0_15px_rgba(37,99,235,0.3)] mb-4">
            <LayoutGrid size={20} />
            <span className="font-medium">Bảng điều khiển</span>
          </Link>

          <MenuItem icon={<List size={20} />} label="Đơn hàng đang xử lý" to="/active-orders" />
          <MenuItem icon={<Package size={20} />} label="Sản phẩm" to="/products" />
          <MenuItem icon={<Cog size={20} />} label="Nguyên liệu" to="/materials" />
          <MenuItem icon={<Truck size={18} />} label="Nguồn cung ứng" to="/supply" />

          <div className="h-px bg-slate-800 my-4 mx-2"></div> {/* Đường kẻ phân cách */}

          {/* NHÓM DROPDOWN MENUS */}
          {/* 1. QUẢN LÝ NHẬP KHO */}
          <div>
            <button
              onClick={() => setOpenInbound(!openInbound)}
              className="w-full flex items-center justify-between px-4 py-2 text-slate-400 hover:text-white transition-colors"
            >
              <span className="text-xs font-semibold uppercase tracking-wider">Quản lý nhập kho</span>
              {openInbound ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openInbound && (
              <div className="mt-1 ml-4 border-l border-slate-700/50 pl-2 space-y-1">
                <MenuItem icon={<ArchiveRestore size={18} />} label="Nhập nguyên liệu" to="/inboundMaterial" />
                <MenuItem icon={<ArchiveRestore size={18} />} label="Nhập thành phẩm" to="/inboundProduct" />
              </div>
            )}
          </div>

          {/* 2. QUẢN LÝ XUẤT KHO */}
          <div className="mt-2">
            <button
              onClick={() => setOpenOutbound(!openOutbound)}
              className="w-full flex items-center justify-between px-4 py-2 text-slate-400 hover:text-white transition-colors"
            >
              <span className="text-xs font-semibold uppercase tracking-wider">Quản lý xuất kho</span>
              {openOutbound ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openOutbound && (
              <div className="mt-1 ml-4 border-l border-slate-700/50 pl-2 space-y-1">
                <MenuItem icon={<PackageOpen size={18} />} label="Xuất sản xuất" to="/outbound/production" />
                <MenuItem icon={<PackageOpen size={18} />} label="Xuất bán hàng" to="/outbound/sales" />
              </div>
            )}
          </div>

          {/* 3. KINH DOANH (Sales) */}
          <div className="mt-2">
            <button
              onClick={() => setOpenSales(!openSales)}
              className="w-full flex items-center justify-between px-4 py-2 text-slate-400 hover:text-white transition-colors"
            >
              <span className="text-xs font-semibold uppercase tracking-wider">Kinh doanh</span>
              {openSales ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openSales && (
              <div className="mt-1 ml-4 border-l border-slate-700/50 pl-2 space-y-1">
                <MenuItem icon={<FileText size={18} />} label="Hóa đơn" to="/invoices" /> 
                <MenuItem icon={<Users size={18} />} label="Khách hàng" to="/customers" />
                <MenuItem icon={<HeadphonesIcon size={18} />} label="CSKH" to="/support" />
              </div>
            )}
          </div>
          
          {/* 4. KHO (System & Setup) */}  
          <div className="mt-2">
            <button
              onClick={() => setOpenWarehouse(!openWarehouse)}
              className="w-full flex items-center justify-between px-4 py-2 text-slate-400 hover:text-white transition-colors"
            >
              <span className="text-xs font-semibold uppercase tracking-wider">Kho (Hệ thống)</span>
              {openWarehouse ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openWarehouse && (
              <div className="mt-1 ml-4 border-l border-slate-700/50 pl-2 space-y-1">
                <MenuItem icon={<Settings size={18} />} label="Thiết lập khu vực" to="/zones" />
                <MenuItem icon={<BookMinus size={18} />} label="Thiết lập danh mục" to="/categories" />
                <MenuItem icon={<BarChart2 size={18} />} label="Báo cáo quản lý" to="/reports" />
                <MenuItem icon={<User size={18} />} label="Người dùng" to="/users" />
              </div>
            )}
          </div>

        </nav>
      </aside>

      {/* PHẦN NỘI DUNG CHÍNH BÊN PHẢI */}
      <main className="flex-1 flex flex-col relative overflow-hidden">
        
        {/* Header */}
        <header className="h-24 px-8 border-b border-slate-800/50 flex items-center justify-between shrink-0">
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
            <div className="flex items-center gap-2 bg-[#1E293B] px-4 py-2 rounded-lg border border-slate-700 cursor-pointer">
              <span className="text-sm font-medium">Today</span>
              <ChevronDown size={16} className="text-slate-400" />
            </div>
            
            {/* Hiển thị tên user đang đăng nhập và Nút Đăng xuất */}
            <div className="flex items-center gap-3 bg-[#0F172A] px-4 py-2 rounded-lg border border-slate-700">
              <div className="w-2 h-2 rounded-full bg-emerald-500 shadow-[0_0_10px_rgba(16,185,129,0.5)]"></div>
              <span className="text-sm font-medium text-slate-300">Chào, {currentUsername}</span>
              
              {/* Nút Đăng xuất nhỏ gọn */}
              <div className="w-px h-4 bg-slate-700 mx-1"></div>
              <button 
                onClick={handleLogout} 
                className="text-slate-400 hover:text-red-400 transition-colors flex items-center gap-1"
                title="Đăng xuất"
              >
                <LogOut size={16} />
              </button>
            </div>
          </div>
        </header>

        {/*Dùng biến children để các trang con hiển thị ở đây */}
        <div className="flex-1 p-8 overflow-y-auto custom-scrollbar">
          {children}
        </div>

      </main>
    </div>
  );
}