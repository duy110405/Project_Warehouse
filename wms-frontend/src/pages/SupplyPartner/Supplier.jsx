import { useState, useEffect } from 'react';
import { Plus, Search, Building2, Edit, Trash2 } from 'lucide-react';
import { Modal, Form, Input, Button, Popconfirm, ConfigProvider, theme, message, Spin, Empty } from 'antd';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/supplier';

// CÁC COMPONENT GIAO DIỆN CON (PRESENTATIONAL COMPONENTS)
// Nhiệm vụ: Chỉ nhận dữ liệu  và vẽ UI, KHÔNG chứa logic gọi API


// 1. Component Thống kê
const SupplierStats = () => (
  <div className="grid grid-cols-4 gap-6 mb-8">
    <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg">
      <h3 className="text-slate-400 text-sm font-medium mb-2">Tổng xưởng hoạt động</h3>
      <p className="text-3xl font-bold text-white">24</p>
    </div>
    <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg">
      <h3 className="text-slate-400 text-sm font-medium mb-2">Đơn nhập đang chờ</h3>
      <p className="text-3xl font-bold text-white">156</p>
    </div>
    <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg">
      <h3 className="text-slate-400 text-sm font-medium mb-2">Tỷ lệ đúng hạn</h3>
      <p className="text-3xl font-bold text-emerald-400">94.5%</p>
    </div>
    <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg">
      <h3 className="text-slate-400 text-sm font-medium mb-2">Tổng chi tiêu (Tháng)</h3>
      <p className="text-3xl font-bold text-white">2.8B <span className="text-lg text-slate-500 font-normal">VND</span></p>
    </div>
  </div>
);

// 2. Component Danh sách dạng Thẻ (Grid)
const SupplierGrid = ({ suppliers, isLoading, onEdit, onDelete }) => {
  if (isLoading) {
    return (
      <div className="flex justify-center items-center py-20">
        <Spin size="large" />
      </div>
    );
  }

  if (!suppliers || suppliers.length === 0) {
    return (
      <div className="bg-[#0F172A] rounded-2xl py-20 border border-slate-800">
        <Empty description={<span className="text-slate-400">Chưa có xưởng gia công nào</span>} />
      </div>
    );
  }

  return (
    <div className="grid grid-cols-2 gap-6 pb-10">
      {suppliers.map((supplier) => (
        <div key={supplier.supplierId} className="bg-[#0F172A] border border-slate-800 rounded-2xl p-6 shadow-xl hover:border-slate-600 transition-colors flex flex-col">
          {/* Header Card */}
          <div className="flex justify-between items-start mb-6">
            <div className="flex gap-4 items-center">
              <div className="w-12 h-12 rounded-xl bg-blue-900/30 border border-blue-800/50 flex items-center justify-center text-blue-400">
                <Building2 size={24} />
              </div>
              <div>
                <h3 className="text-lg font-bold text-white">{supplier.supplierName}</h3>
                <div className="flex items-center gap-2 mt-1">
                  <span className="text-xs px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                    {supplier.type}
                  </span>
                </div>
              </div>
            </div>
            {/* Nút Thao tác */}
            <div className='flex gap-3'>
              <button onClick={() => onEdit(supplier)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
                <Edit size={18} />
              </button>
              <Popconfirm title="Xóa xưởng" description="Bạn có chắc chắn muốn xóa?" onConfirm={() => onDelete(supplier.supplierId)} okText="Xóa" cancelText="Hủy" okButtonProps={{ danger: true }}>
                <button className="p-2 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-colors">
                  <Trash2 size={18} />
                </button>
              </Popconfirm>
            </div>               
          </div>

          {/* Body Card */}
          <div className="grid grid-cols-3 gap-4 mb-6">
            <div className="bg-[#1E293B] rounded-lg p-3">
              <p className="text-slate-400 text-xs mb-1">Đơn đang chờ</p>
              <p className="text-lg font-semibold text-white">{supplier.activeOrders}</p>
            </div>
            <div className="bg-[#1E293B] rounded-lg p-3">
              <p className="text-slate-400 text-xs mb-1">Tỷ lệ đúng hạn</p>
              <p className={`text-lg font-semibold ${supplier.onTimeRate < '90%' ? 'text-red-400' : 'text-emerald-400'}`}>
                {supplier.onTimeRate}
              </p>
            </div>
            <div className="bg-[#1E293B] rounded-lg p-3">
              <p className="text-slate-400 text-xs mb-1">Tổng chi tiêu</p>
              <p className="text-lg font-semibold text-white">{supplier.totalSpend}</p>
            </div>
          </div>

          {/* Footer Card */}
          <div className="mt-auto flex gap-3">
            <button className="flex-1 bg-[#1E293B] hover:bg-slate-700 border border-slate-700 text-white py-2.5 rounded-xl text-sm font-medium transition-colors">
              Xem chi tiết
            </button>
            <button className="flex-1 bg-blue-600 hover:bg-blue-500 text-white py-2.5 rounded-xl text-sm font-medium transition-colors">
              Lên đơn nhập
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

// 3. Component Form Modal Thêm/Sửa
const SupplierModal = ({ isOpen, onClose, form, onSubmit, isEditing }) => (
  <Modal 
    title={<span className="text-lg">{isEditing ? 'Sửa thông tin xưởng' : 'Thêm xưởng mới'}</span>} 
    open={isOpen} 
    onCancel={onClose} 
    footer={null} 
    className="dark-modal" 
    width={650}
  >
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      <Form.Item name="supplierName" label={<span className="text-slate-300">Tên xưởng</span>} rules={[{ required: true, message: 'Vui lòng nhập tên xưởng!' }]}>
        <Input placeholder="VD: Công ty TNHH..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>
      <Form.Item name="type" label={<span className="text-slate-300">Loại hàng gia công</span>} rules={[{ required: true, message: 'Vui lòng nhập loại hàng!' }]}>
        <Input placeholder="VD: Nguyên vật liệu" className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>
      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Thêm xưởng'}
        </Button>
      </div>
    </Form>
  </Modal>
);


// COMPONENT CHA (CONTAINER COMPONENT)
// Nhiệm vụ: Chứa Logic API, Quản lý State, và truyền Props xuống cho Con

export default function Supplier() {
  const [form] = Form.useForm();
  
  // State quản lý dữ liệu
  const [suppliers, setSuppliers] = useState([]);
  const [isLoading, setIsLoading] = useState(true); // Mặc định true để load API mượt hơn
  const [searchTerm, setSearchTerm] = useState('');

  // State quản lý UI Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // LẤY DỮ LIỆU
  const fetchSuppliers = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL);
      const actualData = response.data.data || []; 
      
      const dataWithFakeStats = actualData.map(item => ({
        ...item,
        key: item.supplierId, 
        activeOrders: Math.floor(Math.random() * 20) + 1, 
        onTimeRate: '98%',
        totalSpend: '450M VND',
      }));
      setSuppliers(dataWithFakeStats);
    } catch (error) {
      console.error("Lỗi khi tải dữ liệu:", error);
      message.error("Không thể kết nối đến máy chủ!");
    } finally {
      setIsLoading(false);
    }
  };

  // Tự động gọi hàm fetchSuppliers khi component vừa được render lần đầu
     useEffect(() => {
      // eslint-disable-next-line react-hooks/set-state-in-effect
      fetchSuppliers();
    }, []);
    

  // LƯU DỮ LIỆU
  const handleSubmit = async (values) => {
    try {
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, values);
        message.success('Cập nhật xưởng thành công!');
      } else {
        await axios.post(API_URL, values);
        message.success('Thêm xưởng mới thành công!');
      }
      handleCloseModal();
      fetchSuppliers(); 
    } catch (error) {
      console.error("Lỗi khi lưu:", error);
      message.error("Có lỗi xảy ra khi lưu dữ liệu!");
    }
  };

  // XÓA DỮ LIỆU
  const handleDelete = async (id) => {
    try {
      await axios.delete(`${API_URL}/${id}`);
      message.success('Đã xóa xưởng này!');
      fetchSuppliers(); 
    } catch (error) {
      console.error("Lỗi khi xóa:", error);
      message.error(error.response?.data?.message || "Không thể xóa!");
    }
  };

  // QUẢN LÝ MODAL
  const handleOpenModal = (supplier = null) => {
    if (supplier) {
       setEditingId(supplier.supplierId); 
       form.setFieldsValue(supplier);
    } else {
       setEditingId(null);
       form.resetFields();
       form.setFieldsValue({ status: 1 }); 
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    form.resetFields();
  };

  // BỘ LỌC TÌM KIẾM (Frontend Filtering)
  const filteredSuppliers = suppliers.filter(s => 
    s.supplierName?.toLowerCase().includes(searchTerm.toLowerCase()) || 
    s.type?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <ConfigProvider
      theme={{
        algorithm: theme.darkAlgorithm,
        token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' },
      }}
    >
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Xưởng gia công</h2>
            <p className="text-slate-400 text-sm mt-1">Quản lý xưởng gia công thành phẩm</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Thêm xưởng
          </button>
        </div>

        {/* CÁC COMPONENT GIAO DIỆN ĐƯỢC NHÚNG VÀO (Truyền props) */}
        
        <SupplierStats />

        <div className="flex items-center mb-6">
          <div className="w-[400px] h-11 bg-[#1E293B] border border-slate-700 rounded-xl flex items-center px-4 shadow-sm focus-within:border-blue-500 transition-colors">
            <Search className="text-slate-400 mr-3" size={18} />
            <input 
              type="text" 
              placeholder="Tìm xưởng theo tên, loại hàng..." 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-transparent text-white text-sm focus:outline-none placeholder-slate-400" 
            />
          </div>
        </div>

        <SupplierGrid 
          suppliers={filteredSuppliers} 
          isLoading={isLoading} 
          onEdit={handleOpenModal} 
          onDelete={handleDelete} 
        />

        <SupplierModal 
          isOpen={isModalOpen} 
          onClose={handleCloseModal} 
          form={form} 
          onSubmit={handleSubmit} 
          isEditing={!!editingId} 
        />

      </div>
    </ConfigProvider>
  );
}