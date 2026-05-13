import { useState } from 'react';
import { Modal, Form, Input, InputNumber, Select, ConfigProvider, theme } from 'antd';
import { Plus, Trash2 , Edit, MapPin, Box, Layers, ArchiveRestore } from 'lucide-react';

// Component StatCard tĩnh - khai báo ngoài để tránh re-create mỗi lần render
const StatCard = ({ title, value, subtitle }) => (
  <div className="bg-[#0F172A] border border-slate-800 p-5 rounded-xl flex flex-col justify-center">
    <p className="text-slate-400 text-sm font-medium mb-2">{title}</p>
    <h3 className="text-white text-3xl font-bold mb-1">{value}</h3>
    {subtitle && <p className="text-slate-500 text-xs mt-2">{subtitle}</p>}
  </div>
);

export default function Zone() {
  const [form] = Form.useForm();
  
  // 1. STATE QUẢN LÝ DỮ LIỆU
  const [activeTab, setActiveTab] = useState(1); // 1 = Nguyên liệu, 2 = Thành phẩm
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // Mock Data (Dựa theo backend bạn đã thiết kế)
  const [zones, setZones] = useState([
    { id: 'Z001', name: 'Khu A - Hóa chất', capacity: 15000, currentLoad: 12000, zoneType: 1 },
    { id: 'Z002', name: 'Khu B - Vải vóc', capacity: 20000, currentLoad: 5000, zoneType: 1 },
    { id: 'Z003', name: 'Khu C - Đồ điện tử', capacity: 10000, currentLoad: 9500, zoneType: 2 },
    { id: 'Z004', name: 'Khu D - Gia dụng', capacity: 25000, currentLoad: 10000, zoneType: 2 },
  ]);

  // Lọc danh sách theo Tab đang chọn
  const filteredZones = zones.filter(zone => zone.zoneType === activeTab);

  // Tính toán số liệu cho 4 ô Header (Dựa trên tab đang mở)
  const totalZones = filteredZones.length;
  const totalCapacity = filteredZones.reduce((sum, z) => sum + z.capacity, 0);
  const totalLoad = filteredZones.reduce((sum, z) => sum + z.currentLoad, 0);
  const utilization = totalCapacity === 0 ? 0 : Math.round((totalLoad / totalCapacity) * 100);

  // 2. HÀM XỬ LÝ (CRUD)
  const handleOpenModal = (zone = null) => {
    if (zone) {
      setEditingId(zone.id);
      form.setFieldsValue(zone);
    } else {
      setEditingId(null);
      form.resetFields();
      form.setFieldsValue({ zoneType: activeTab }); // Mặc định loại zone theo tab đang mở
    }
    setIsModalOpen(true);
  };

  const handleDeleteZone = (id) => {
     // Logic Xóa (Gọi API: axios.delete)
    const filteredList = zones.filter(zone => zone.id !== id);
    setZones(filteredList);
  }

  const handleSubmit = (values) => {
    if (editingId) {
      const updatedList = zones.map(z => z.id === editingId ? { ...z, ...values } : z);
      setZones(updatedList);
    } else {
      const newZone = {
        id: `Z00${zones.length + 1}`, // Fake ID
        currentLoad: 0, // Mới tạo thì trống
        ...values
      };
      setZones([...zones, newZone]);
    }
    setIsModalOpen(false);
  };

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm }}>
      <div className="flex flex-col h-full max-w-9xl mx-auto">
        
        {/* HEADER & NÚT THÊM */}
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Thiết lập kho</h2>
            <p className="text-slate-400 text-sm mt-1">Cấu hình khu vực, sức chứa và vị trí lưu trữ</p>
          </div>
          <button 
            onClick={() => handleOpenModal()}
            className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium"
          >
            <Plus size={18} />
            Add Zone
          </button>
        </div>

        {/* TOP STATS */}
        <div className="grid grid-cols-4 gap-4 mb-8">
          <StatCard title="Tổng số khu vực" value={totalZones} subtitle={activeTab === 1 ? "Khu vực Nguyên liệu" : "Khu vực Thành phẩm"} />
          <StatCard title="Tổng sức chứa" value={totalCapacity.toLocaleString()} subtitle="Đơn vị" />
          <StatCard title="Lượng tồn" value={totalLoad.toLocaleString()} subtitle="Đơn vị" />
          <StatCard title="Tỉ lệ sử dụng" value={`${utilization}%`} subtitle="Tỉ lệ lấp đầy" />
        </div>

        {/* VÙNG CHỨA DANH SÁCH ZONE */}
        <div className="bg-[#0F172A] border border-slate-800 rounded-2xl p-6">
          
          <div className="flex items-center gap-2 mb-6">
            <MapPin className="text-blue-500" size={20} />
            <h3 className="text-lg font-semibold text-white">Cấu hình khu vực</h3>
          </div>

          {/* THANH TOGGLE CHUYỂN ĐỔI (NGUYÊN LIỆU - THÀNH PHẨM) */}
          <div className="flex bg-[#1E293B] p-1 rounded-xl w-fit border border-slate-700 mb-6">
            <button 
              onClick={() => setActiveTab(1)}
              className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${
                activeTab === 1 ? 'bg-blue-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Layers size={16} />
              Khu Nguyên Liệu
            </button>
            <button 
              onClick={() => setActiveTab(2)}
              className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${
                activeTab === 2 ? 'bg-emerald-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Box size={16} />
              Khu Thành Phẩm
            </button>
          </div>

          {/* DANH SÁCH CARD KHU VỰC */}
          <div className="space-y-4">
            {filteredZones.map((zone, index) => {
              const percent = Math.round((zone.currentLoad / zone.capacity) * 100);
              let statusColor = 'bg-green-500'; // Mặc định xanh
              if (percent > 70) statusColor = 'bg-yellow-500'; // Vàng nếu hơi đầy
              if (percent > 90) statusColor = 'bg-red-500'; // Đỏ nếu sắp tràn kho

              return (
                <div key={zone.id} className="bg-[#151E32] border border-slate-700 hover:border-slate-600 transition-colors rounded-xl p-5">
                  {/* Card Header */}
                  <div className="flex justify-between items-start mb-5">
                    <div className="flex items-center gap-4">
                      {/* Biểu tượng chữ cái cái đầu tiên (Ví dụ: Z001 -> Z) */}
                      <div className={`w-12 h-12 rounded-xl flex items-center justify-center text-lg font-bold ${
                        activeTab === 1 ? 'bg-blue-500/20 text-blue-400' : 'bg-emerald-500/20 text-emerald-400'
                      }`}>
                        {zone.id.charAt(0)}{index + 1}
                      </div>
                      <div>
                        <h4 className="text-white font-semibold text-lg">{zone.name}</h4>
                        <p className="text-slate-400 text-sm">Sức chứa tối đa: {zone.capacity.toLocaleString()} đơn vị</p>
                      </div>
                    </div>
                    {/* Nút sửa */}
                    <div className="flex gap-1">
                    <button 
                      onClick={() => handleOpenModal(zone)}
                      className="p-2 text-slate-400 hover:text-white bg-[#1E293B] hover:bg-slate-700 rounded-lg transition-colors border border-slate-700"
                      >
                      <Edit size={16} />
                    </button>
                     {/* Nút xóa */}
                    <button 
                      onClick={() => handleDeleteZone(zone.id)}
                      className="p-2 text-slate-400 hover:text-white bg-red-500/20 hover:bg-red-500/20 rounded-lg transition-colors border border-slate-700"
                    >
                      <Trash2 size={16} />
                    </button>
                    </div>
                  </div>

                  {/* Card Body (3 cục thông tin dưới) */}
                  <div className="grid grid-cols-3 gap-4">
                    {/* Đang chứa */}
                    <div className="bg-[#0F172A] rounded-lg p-4 border border-slate-800">
                      <div className="flex items-center gap-2 mb-2">
                        <ArchiveRestore size={14} className="text-blue-400" />
                        <span className="text-slate-400 text-xs font-medium uppercase tracking-wider">Đang chứa</span>
                      </div>
                      <span className="text-white text-xl font-bold">{zone.currentLoad.toLocaleString()}</span>
                    </div>

                    {/* Còn trống */}
                    <div className="bg-[#0F172A] rounded-lg p-4 border border-slate-800">
                      <div className="flex items-center gap-2 mb-2">
                        <Box size={14} className="text-emerald-400" />
                        <span className="text-slate-400 text-xs font-medium uppercase tracking-wider">Còn trống</span>
                      </div>
                      <span className="text-white text-xl font-bold">{(zone.capacity - zone.currentLoad).toLocaleString()}</span>
                    </div>

                    {/* Mức độ lấp đầy (Có thanh progress bar) */}
                    <div className="bg-[#0F172A] rounded-lg p-4 border border-slate-800 flex flex-col justify-center">
                      <div className="flex justify-between items-center mb-2">
                         <span className="text-slate-400 text-xs font-medium uppercase tracking-wider">Tỉ lệ lấp đầy</span>
                         <span className="text-white text-sm font-bold">{percent}%</span>
                      </div>
                      {/* Progress Bar tự chế bằng Tailwind */}
                      <div className="w-full h-1.5 bg-slate-700 rounded-full overflow-hidden">
                        <div className={`h-full ${statusColor} rounded-full`} style={{ width: `${percent}%` }}></div>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
            
            {/* Hiển thị khi không có data */}
            {filteredZones.length === 0 && (
              <div className="text-center py-10 border-2 border-dashed border-slate-700 rounded-xl">
                <p className="text-slate-400">Chưa có khu vực nào thuộc loại này.</p>
              </div>
            )}
          </div>
        </div>

        {/* MODAL THÊM / SỬA ZONE */}
        <Modal
          title={<span className="text-lg">{editingId ? 'Sửa thông tin khu vực' : 'Thêm khu vực mới'}</span>}
          open={isModalOpen}
          onCancel={() => setIsModalOpen(false)}
          footer={null}
          className="dark-modal"
        >
          <Form form={form} layout="vertical" onFinish={handleSubmit} className="mt-6">
            <Form.Item 
              name="name" 
              label={<span className="text-slate-300">Tên khu vực</span>}
              rules={[{ required: true, message: 'Vui lòng nhập tên khu vực!' }]}
            >
              <Input placeholder="VD: Khu A - Hóa chất" className="bg-[#1E293B] border-slate-700 text-white py-2" />
            </Form.Item>

            <Form.Item 
              name="zoneType" 
              label={<span className="text-slate-300">Loại khu vực (Dành cho)</span>}
              rules={[{ required: true, message: 'Vui lòng chọn loại khu vực!' }]}
            >
              <Select className="h-10">
                <Select.Option value={1}>Kho Nguyên Liệu</Select.Option>
                <Select.Option value={2}>Kho Thành Phẩm</Select.Option>
              </Select>
            </Form.Item>

            <Form.Item 
              name="capacity" 
              label={<span className="text-slate-300">Sức chứa tối đa (Capacity)</span>}
              rules={[{ required: true, message: 'Vui lòng nhập sức chứa!' }]}
            >
              <InputNumber 
                className="w-full bg-[#1E293B] border-slate-700" 
                formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                parser={(value) => value?.replace(/\$\s?|(,*)/g, '')}
                min={1} 
              />
            </Form.Item>

            <div className="flex justify-end gap-3 mt-8">
              <button type="button" onClick={() => setIsModalOpen(false)} className="px-4 py-2 rounded-lg text-slate-300 hover:text-white hover:bg-slate-800 transition-colors">
                Hủy
              </button>
              <button type="submit" className="px-4 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium transition-colors">
                {editingId ? 'Lưu thay đổi' : 'Tạo khu vực'}
              </button>
            </div>
          </Form>
        </Modal>

      </div>
    </ConfigProvider>
  );
}