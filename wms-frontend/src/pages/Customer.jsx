import { useState, useEffect } from 'react';
import { Plus, Search, Users, Phone, MapPin, CreditCard, Edit, Trash2 } from 'lucide-react';
import { Modal, Form, Input, Button, Popconfirm, ConfigProvider, theme, message, Spin, Empty } from 'antd';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/customer'; // API cho Khách hàng

// ==========================================
// 1. COMPONENT THỐNG KÊ (Stats)
// ==========================================
// Truyền tham số totalCustomers vào để nó tự động nhảy số theo dữ liệu thật
const CustomerStats = ({ totalCustomers }) => (
  <div className="grid grid-cols-4 gap-6 mb-8">
    <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg flex flex-col justify-center">
      <h3 className="text-slate-400 text-sm font-medium mb-2">Khách hàng đang hoạt động</h3>
      <p className="text-4xl font-bold text-blue-400">{totalCustomers}</p>
    </div>
    {/* Các ô thống kê khác đã được gỡ bỏ theo yêu cầu */}
  </div>
);

// ==========================================
// 2. COMPONENT DANH SÁCH (Grid)
// ==========================================
const CustomerGrid = ({ customers, isLoading, onEdit, onDelete }) => {
  if (isLoading) {
    return (
      <div className="flex justify-center items-center py-20">
        <Spin size="large" />
      </div>
    );
  }
  if (!customers || customers.length === 0) {
    return (
      <div className="bg-[#0F172A] rounded-2xl py-20 border border-slate-800">
        <Empty description={<span className="text-slate-400">Chưa có khách hàng nào</span>} />
      </div>
    );
  }

  return (
    <div className="grid grid-cols-2 gap-6 pb-10">
      {customers.map((customer) => (
        <div key={customer.customerId} className="bg-[#0F172A] border border-slate-800 rounded-2xl p-6 shadow-xl hover:border-slate-600 transition-colors flex flex-col">
          
          {/* Card Header: Icon, Tên */}
          <div className="flex justify-between items-start mb-6">
            <div className="flex gap-4 items-center">
              <div className="w-12 h-12 rounded-xl bg-blue-900/30 border border-blue-800/50 flex items-center justify-center text-blue-400">
                <Users size={24} /> {/* Đổi icon thành Users cho hợp với Khách hàng */}
              </div>
              <div>
                <h3 className="text-lg font-bold text-white">{customer.customerName}</h3>
                <div className="flex items-center gap-2 mt-1">
                  <span className="text-xs px-2 py-0.5 rounded bg-slate-800 text-slate-400 border border-slate-700">
                    Mã KH: {customer.customerId}
                  </span>
                </div>
              </div>
            </div>
            
            {/* Nút Edit hoặc xóa */}
            <div className="flex gap-2">
              <button onClick={() => onEdit(customer)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
                <Edit size={18} />
              </button>
              <Popconfirm 
                title="Xóa khách hàng" 
                description={`Bạn có chắc muốn xóa khách hàng "${customer.customerName}"?`} 
                onConfirm={() => onDelete(customer.customerId)} 
                okText="Xóa" cancelText="Hủy" okButtonProps={{ danger: true }}
              >
                <button className="p-2 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-colors">
                  <Trash2 size={18} />
                </button>
              </Popconfirm>
            </div>
          </div>

          {/* Card Body: Thông tin chi tiết khách hàng */}
          <div className="flex flex-col gap-3 mb-6 bg-[#1E293B] rounded-xl p-4 border border-slate-700">
             <div className="flex items-start gap-3 text-sm text-slate-300">
                <MapPin size={16} className="text-slate-500 mt-0.5" /> 
                <span className="flex-1 leading-relaxed">{customer.address || 'Chưa cập nhật địa chỉ'}</span>
             </div>
             <div className="flex items-center gap-3 text-sm text-slate-300">
                <CreditCard size={16} className="text-slate-500" /> 
                <span>STK: <span className="text-emerald-400 font-medium">{customer.bankAccountNumber || '---'}</span></span>
             </div>
          </div>

          {/* Thông tin liên hệ mini & Card Footer */}
          <div className="mt-auto flex items-center justify-between border-t border-slate-800 pt-4">
            <div className="flex items-center gap-2 text-sm text-slate-400 font-medium">
              <Phone size={16} className="text-blue-400" /> {customer.phoneNumber || '---'}
            </div>
            <button className="bg-blue-600 hover:bg-blue-500 text-white px-5 py-2 rounded-xl text-sm font-medium transition-colors">
              Tạo Hóa Đơn
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

// ==========================================
// 3. COMPONENT MODAL (Thêm / Sửa)
// ==========================================
const CustomerModal = ({ isOpen, onClose, form, onSubmit, isEditing }) => (
  <Modal 
    title={<span className="text-lg">{isEditing ? 'Sửa thông tin khách hàng' : 'Thêm khách hàng mới'}</span>} 
    open={isOpen} 
    onCancel={onClose} 
    footer={null} 
    className="dark-modal" 
    width={600}
  >
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="customerId" label={<span className="text-slate-300">Mã khách hàng</span>}>
          <Input placeholder="Tự động tạo..." className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled />
        </Form.Item>

        <Form.Item name="phoneNumber" label={<span className="text-slate-300">Số điện thoại</span>} rules={[{ required: true, message: 'Vui lòng nhập SĐT' }]}>
          <Input type="text" placeholder="VD: 0987654321" className="bg-[#1E293B] border-slate-700 text-white py-2" />
        </Form.Item>
      </div>

      <Form.Item name="customerName" label={<span className="text-slate-300">Tên khách hàng / Tên Công ty</span>} rules={[{ required: true, message: 'Vui lòng nhập tên khách hàng' }]}>
        <Input placeholder="VD: Nguyễn Văn A..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <Form.Item name="address" label={<span className="text-slate-300">Địa chỉ</span>} rules={[{ required: true, message: 'Vui lòng nhập địa chỉ' }]}>
        <Input placeholder="Số nhà, đường, phường, quận..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <Form.Item name="bankAccountNumber" label={<span className="text-slate-300">Số tài khoản ngân hàng</span>}>
        <Input placeholder="VD: 1903... (Techcombank)" className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Thêm khách hàng'}
        </Button>
      </div>
    </Form>
  </Modal>
);

// ==========================================
// 4. COMPONENT CHÍNH
// ==========================================
export default function Customer() {
  const [form] = Form.useForm();
  const [customers, setCustomers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  // State Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // Lấy dữ liệu
  const fetchCustomers = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL);
      // Đổ thẳng dữ liệu từ Backend vào, không cần fake data nữa
      setCustomers(response.data.data || []);
    } catch (error) {
      console.error("Lỗi khi tải dữ liệu:", error);
      message.error("Không thể kết nối đến máy chủ!");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
      // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchCustomers();
  }, []);

  const handleCloseModal = () => {
    setIsModalOpen(false);
    form.resetFields();
  };

  const handleOpenModal = (customer = null) => {
    if (customer) {
      setEditingId(customer.customerId);
      form.setFieldsValue(customer);
    } else {
      setEditingId(null);
      form.resetFields();
    }
    setIsModalOpen(true);
  };

  // Lưu dữ liệu (Thêm mới hoặc Cập nhật)
  const handleSubmit = async (values) => {
    try {
      console.log("🚀 Dữ liệu React chuẩn bị gửi đi là:", values);
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, values);
        message.success("Cập nhật thông tin khách hàng thành công!");
      } else {
        await axios.post(API_URL, values);
        message.success('Thêm khách hàng mới thành công!');
      }
      handleCloseModal();
      fetchCustomers(); 
    } catch (error) {
      console.error("Lỗi khi lưu:", error);
      message.error("Có lỗi xảy ra khi lưu dữ liệu!");
    }
  };

  // Xóa khách hàng
  const handleDelete = async (id) => {
    try {
      await axios.delete(`${API_URL}/${id}`);
      message.success('Đã xóa khách hàng này!');
      fetchCustomers(); 
    } catch (error) {
      console.error("Lỗi khi xóa:", error);
      message.error(error.response?.data?.message || "Không thể xóa khách hàng này!");
    }
  };

  // Lọc tìm kiếm (Search theo Tên, Mã KH hoặc SĐT)
  const filteredCustomers = customers.filter(c => 
    c.customerName?.toLowerCase().includes(searchTerm.toLowerCase()) || 
    c.customerId?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    c.phoneNumber?.includes(searchTerm)
  );

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý Khách hàng</h2>
            <p className="text-slate-400 text-sm mt-1">Quản lý thông tin đối tác mua hàng</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Thêm khách hàng
          </button>
        </div>

        {/* THỐNG KÊ (Truyền số lượng mảng hiện tại vào) */}
        <CustomerStats totalCustomers={customers.length} />

        {/* THANH TÌM KIẾM */}
        <div className="flex items-center mb-6">
          <div className="w-[450px] h-11 bg-[#1E293B] border border-slate-700 rounded-xl flex items-center px-4 shadow-sm focus-within:border-blue-500 transition-colors">
            <Search className="text-slate-400 mr-3" size={18} />
            <input 
              type="text" 
              placeholder="Tìm theo tên, mã khách hàng, số điện thoại..." 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-transparent text-white text-sm focus:outline-none placeholder-slate-400" 
            />
          </div>
        </div>

        {/* GRID DANH SÁCH */}
        <CustomerGrid 
          customers={filteredCustomers} 
          isLoading={isLoading} 
          onEdit={handleOpenModal} 
          onDelete={handleDelete} 
        />

        {/* MODAL */}
        <CustomerModal 
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