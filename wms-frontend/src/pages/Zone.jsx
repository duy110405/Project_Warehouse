import { useState, useEffect } from 'react';
import { Modal, Form, Input, InputNumber, Select, ConfigProvider, theme, message, Spin, Empty, Popconfirm } from 'antd';
import { Plus, Trash2, Edit, MapPin, Box, Layers, ArchiveRestore } from 'lucide-react';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/zone'; 


// 1. COMPONENT THỐNG KÊ (StatCard)

const StatCard = ({ title, value, subtitle }) => (
  <div className="bg-[#0F172A] border border-slate-800 p-5 rounded-xl flex flex-col justify-center">
    <p className="text-slate-400 text-sm font-medium mb-2">{title}</p>
    <h3 className="text-white text-3xl font-bold mb-1">{value}</h3>
    {subtitle && <p className="text-slate-500 text-xs mt-2">{subtitle}</p>}
  </div>
);


// 2. COMPONENT DANH SÁCH THẺ (ZoneGrid)
const ZoneGrid = ({ zones, isLoading, onEdit, onDelete, activeTab }) => {
  if (isLoading) {
    return (
      <div className="flex justify-center items-center py-20">
        <Spin size="large" />
      </div>
    );
  }

  if (!zones || zones.length === 0) {
    return (
      <div className="bg-[#0F172A] rounded-2xl py-20 border border-slate-800">
        <Empty description={<span className="text-slate-400">Chưa có khu vực nào</span>} />
      </div>
    );
  }

  return (
    <div className="grid grid-cols-2 gap-6 pb-10 mt-6">
      {zones.map((zone) => {
        // Tránh lỗi chia cho 0 nếu capacity = 0
        const percent = zone.capacity > 0 ? Math.round(((zone.currentLoad || 0) / zone.capacity) * 100) : 0;
  
        let statusColor = 'bg-emerald-500'; // Mặc định xanh
        if (percent > 70) statusColor = 'bg-yellow-500'; // Vàng nếu hơi đầy
        if (percent > 90) statusColor = 'bg-red-500'; // Đỏ nếu sắp tràn kho

        return (
          <div key={zone.zoneId || zone.id} className="bg-[#151E32] border border-slate-700 hover:border-slate-600 transition-colors rounded-xl p-5">
            {/* Card Header */}
            <div className="flex justify-between items-start mb-5">
              <div className="flex items-center gap-4">
                <div className={`w-12 h-12 rounded-xl flex items-center justify-center text-lg font-bold ${
                  activeTab === 1 ? 'bg-blue-500/20 text-blue-400' : 'bg-emerald-500/20 text-emerald-400'
                }`}>
                  {/* Lấy chữ cái đầu tiên của tên Khu vực */}
                  {zone.zoneName ? zone.zoneName.charAt(0).toUpperCase() : 'Z'}
                </div>
                <div>
                  <h4 className="text-white font-semibold text-lg">{zone.zoneName}</h4>
                  <p className="text-slate-400 text-sm">Sức chứa: {zone.capacity?.toLocaleString()} đơn vị</p>
                </div>
              </div>
              
              {/* Nút sửa & xóa */}
              <div className="flex gap-2">
                <button 
                  onClick={() => onEdit(zone)}
                  className="p-2 text-slate-400 hover:text-blue-400 bg-[#1E293B] hover:bg-blue-400/10 rounded-lg transition-colors border border-slate-700 hover:border-blue-500/50"
                >
                  <Edit size={16} />
                </button>
                <Popconfirm
                  title="Xóa khu vực"
                  description={`Bạn có chắc muốn xóa "${zone.zoneName}"?`}
                  onConfirm={() => onDelete(zone.zoneId || zone.id)}
                  okText="Xóa"
                  cancelText="Hủy"
                  okButtonProps={{ danger: true }}
                >
                  <button className="p-2 text-slate-400 hover:text-red-400 bg-[#1E293B] hover:bg-red-400/10 rounded-lg transition-colors border border-slate-700 hover:border-red-500/50">
                    <Trash2 size={16} />
                  </button>
                </Popconfirm>
              </div>
            </div>

            {/* Card Body */}
            <div className="grid grid-cols-3 gap-4">
              <div className="bg-[#0F172A] rounded-lg p-4 border border-slate-800">
                <div className="flex items-center gap-2 mb-2">
                  <ArchiveRestore size={14} className="text-blue-400" />
                  <span className="text-slate-400 text-xs font-medium uppercase tracking-wider">Đang chứa</span>
                </div>
                <span className="text-white text-xl font-bold">{(zone.currentLoad || 0).toLocaleString()}</span>
              </div>

              <div className="bg-[#0F172A] rounded-lg p-4 border border-slate-800">
                <div className="flex items-center gap-2 mb-2">
                  <Box size={14} className="text-emerald-400" />
                  <span className="text-slate-400 text-xs font-medium uppercase tracking-wider">Còn trống</span>
                </div>
                <span className="text-white text-xl font-bold">{(zone.capacity - (zone.currentLoad || 0)).toLocaleString()}</span>
              </div>

              <div className="bg-[#0F172A] rounded-lg p-4 border border-slate-800 flex flex-col justify-center">
                <div className="flex justify-between items-center mb-2">
                   <span className="text-slate-400 text-xs font-medium uppercase tracking-wider">Lấp đầy</span>
                   <span className="text-white text-sm font-bold">{percent}%</span>
                </div>
                <div className="w-full h-1.5 bg-slate-700 rounded-full overflow-hidden">
                  <div className={`h-full ${statusColor} rounded-full transition-all duration-500`} style={{ width: `${percent}%` }}></div>
                </div>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
};

// 3. COMPONENT MODAL (ZoneModal)
const ZoneModal = ({ isOpen, onClose, form, onSubmit, isEditing }) => (
  <Modal
    title={<span className="text-lg">{isEditing ? 'Sửa thông tin khu vực' : 'Thêm khu vực mới'}</span>}
    open={isOpen}
    onCancel={onClose}
    footer={null}
    className="dark-modal"
  >
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      <Form.Item 
        name="zoneName" // Khớp với DB của bạn
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
        {/* Ant Design Select mặc định bị sáng, bạn cần custom CSS hoặc chấp nhận popup nó màu sáng */}
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
          className="w-full bg-[#1E293B] border-slate-700 text-white" 
          formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
          parser={(value) => value?.replace(/\$\s?|(,*)/g, '')}
          min={1} 
        />
      </Form.Item>

      <div className="flex justify-end gap-3 mt-8">
        <button type="button" onClick={onClose} className="px-4 py-2 rounded-lg text-slate-300 hover:text-white hover:bg-slate-800 transition-colors border border-slate-700">
          Hủy
        </button>
        <button type="submit" className="px-4 py-2 rounded-lg bg-blue-600 hover:bg-blue-500 text-white font-medium transition-colors">
          {isEditing ? 'Lưu thay đổi' : 'Tạo khu vực'}
        </button>
      </div>
    </Form>
  </Modal>
);


export default function Zone() {
  const [form] = Form.useForm();
  
  // STATE QUẢN LÝ DỮ LIỆU
  const [activeTab, setActiveTab] = useState(2); // 1 = Nguyên liệu, 2 = Thành phẩm
  const [zones, setZones] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  // STATE QUẢN LÝ MODAL
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // LẤY DỮ LIỆU TỪ API
  const fetchZones = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL);
      setZones(response.data.data || []);
    } catch (error) {
      console.error("Lỗi lấy dữ liệu:", error);
      message.error("Không thể kết nối đến máy chủ!");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
        // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchZones();
  }, []);

  // LƯU DỮ LIỆU (Thêm/Sửa)
  const handleSubmit = async (values) => {
    try {
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, values);
        message.success('Cập nhật khu vực thành công!');
      } else {
        await axios.post(API_URL, values);
        message.success('Tạo khu vực mới thành công!');
      }
      setIsModalOpen(false);
      fetchZones(); 
    } catch (error) {
      console.error("Lỗi lưu dữ liệu:", error);
      message.error(error.response?.data?.message || "Có lỗi xảy ra khi lưu!");
    }
  };

  // XÓA DỮ LIỆU
  const handleDeleteZone = async (id) => {
    try {
      await axios.delete(`${API_URL}/${id}`);
      message.success('Đã xóa khu vực!');
      fetchZones();
    } catch (error) {
      console.error("Lỗi khi xóa:", error);
      message.error("Không thể xóa khu vực này!");
    }
  };

  // QUẢN LÝ MODAL
  const handleOpenModal = (zone = null) => {
    if (zone) {
      setEditingId(zone.zoneId || zone.id);
      form.setFieldsValue(zone);
    } else {
      setEditingId(null);
      form.resetFields();
      form.setFieldsValue({ zoneType: activeTab }); // Mặc định type theo tab hiện tại
    }
    setIsModalOpen(true);
  };

  // LỌC DỮ LIỆU THEO TAB (Filter ở Frontend)
  const filteredZones = zones.filter(zone => zone.zoneType === activeTab);

  // TÍNH TOÁN SỐ LIỆU THỐNG KÊ
  const totalZones = filteredZones.length;
  const totalCapacity = filteredZones.reduce((sum, z) => sum + (z.capacity || 0), 0);
  const totalLoad = filteredZones.reduce((sum, z) => sum + (z.currentLoad || 0), 0);
  const utilization = totalCapacity === 0 ? 0 : Math.round((totalLoad / totalCapacity) * 100);

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER & NÚT THÊM */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Thiết lập kho</h2>
            <p className="text-slate-400 text-sm mt-1">Cấu hình khu vực, sức chứa và vị trí lưu trữ</p>
          </div>
          <button 
            onClick={() => handleOpenModal()}
            className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium"
          >
            <Plus size={18} />
            Thêm khu vực
          </button>
        </div>

        {/* TOP STATS */}
        <div className="grid grid-cols-4 gap-6 mb-8">
          <StatCard title="Tổng số khu vực" value={totalZones} subtitle={activeTab === 1 ? "Khu vực Nguyên liệu" : "Khu vực Thành phẩm"} />
          <StatCard title="Tổng sức chứa" value={totalCapacity.toLocaleString()} subtitle="Đơn vị" />
          <StatCard title="Lượng tồn" value={totalLoad.toLocaleString()} subtitle="Đơn vị" />
          <StatCard title="Tỉ lệ sử dụng" value={`${utilization}%`} subtitle="Tỉ lệ lấp đầy kho" />
        </div>

        {/* VÙNG CHỨA DANH SÁCH ZONE */}
        <div className="bg-[#0F172A] border border-slate-800 rounded-2xl p-6">
          <div className="flex items-center gap-2 mb-6">
            <MapPin className="text-blue-500" size={20} />
            <h3 className="text-lg font-semibold text-white">Cấu hình khu vực</h3>
          </div>

          {/* THANH TOGGLE CHUYỂN ĐỔI (NGUYÊN LIỆU - THÀNH PHẨM) */}
          <div className="flex bg-[#1E293B] p-1 rounded-xl w-fit border border-slate-700">
            <button 
              onClick={() => setActiveTab(1)}
              className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${
                activeTab === 1 ? 'bg-blue-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Layers size={16} /> Khu Nguyên Liệu
            </button>
            <button 
              onClick={() => setActiveTab(2)}
              className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${
                activeTab === 2 ? 'bg-emerald-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Box size={16} /> Khu Thành Phẩm
            </button>
          </div>

          {/* HIỂN THỊ LƯỚI KHU VỰC (GỌI COMPONENT CON) */}
          <ZoneGrid 
            zones={filteredZones} 
            isLoading={isLoading}
            onEdit={handleOpenModal}
            onDelete={handleDeleteZone}
            activeTab={activeTab} // Truyền activeTab để UI tự tô màu xanh/emerald
          />
        </div>

        {/* GỌI COMPONENT MODAL */}
        <ZoneModal 
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          form={form}
          onSubmit={handleSubmit}
          isEditing={!!editingId}
        />     
      </div>
    </ConfigProvider>
  );
}