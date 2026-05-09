
import { 
  LayoutGrid, Package, ShoppingCart, Users, Truck, 
  HeadphonesIcon, BarChart2, Settings, Archive, ChevronDown 
} from 'lucide-react';

export default function App() {
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
        <nav className="flex-1 px-4 py-2 space-y-2 overflow-y-auto">
          {/* Nút Dashboard đang active */}
          <a href="#" className="flex items-center gap-3 px-4 py-3 bg-blue-600 text-white rounded-xl shadow-[0_0_15px_rgba(37,99,235,0.3)]">
            <LayoutGrid size={20} />
            <span className="font-medium">Dashboard</span>
          </a>

          {/* Các nút menu khác */}
          <MenuItem icon={<Package size={20} />} label="Products" />
          <MenuItem icon={<ShoppingCart size={20} />} label="Orders" />
          <MenuItem icon={<Users size={20} />} label="Customers" />
          <MenuItem icon={<Truck size={20} />} label="Couriers" />
          <MenuItem icon={<HeadphonesIcon size={20} />} label="Customer Service" />
          <MenuItem icon={<BarChart2 size={20} />} label="Management Reports" />
          
          <div className="pt-4 pb-2">
            <p className="px-4 text-xs font-semibold text-slate-500 uppercase tracking-wider">Warehouse</p>
          </div>
          <MenuItem icon={<Settings size={20} />} label="Setup" />
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
           <div className="w-full h-full border-2 border-dashed border-slate-800 rounded-2xl flex items-center justify-center">
              <p className="text-slate-500 text-lg">Khu vực chứa các thẻ Card và Bảng dữ liệu sẽ nằm ở đây</p>
           </div>
        </div>
      </main>

    </div>
  );
}

// Component phụ để vẽ Menu cho gọn code
function MenuItem({ icon, label }) {
  return (
    <a href="#" className="flex items-center gap-3 px-4 py-3 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-xl transition-all duration-200">
      {icon}
      <span className="font-medium">{label}</span>
    </a>
  );
}