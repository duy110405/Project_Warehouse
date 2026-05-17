import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, Space, Popconfirm, ConfigProvider, theme, message } from 'antd';
import { Plus, Edit, Trash2, Search } from 'lucide-react';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/category'; 

// ==========================================
// 1. COMPONENT BẢNG DỮ LIỆU
// ==========================================
const CategoryTable = ({ categories, isLoading, onEdit, onDelete }) => {
  const columns = [
    {
      title: 'Mã danh mục',
      dataIndex: 'categoryId',
      key: 'categoryId',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Tên danh mục',
      dataIndex: 'categoryName',
      key: 'categoryName',
      render: (text) => <span className="text-slate-200 font-medium">{text}</span>,
    },
    {
      title: 'Hành động',
      key: 'action',
      align: 'right',
      render: (_, record) => (
        <Space size="middle">
          <button onClick={() => onEdit(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
            <Edit size={18} />
          </button>
          
          <Popconfirm title="Xóa danh mục" description={`Bạn có chắc chắn muốn xóa "${record.categoryName}" không?`} onConfirm={() => onDelete(record.categoryId)} okText="Xóa" cancelText="Hủy" okButtonProps={{ danger: true }}>
            <button className="p-2 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-colors">
              <Trash2 size={18} />
            </button>
          </Popconfirm>
        </Space>
      ),
    },
  ];
  return (
    <div className="bg-[#0F172A] border-x border-b border-slate-800 rounded-b-2xl overflow-hidden shadow-xl text-base">
      <Table columns={columns} dataSource={categories} loading={isLoading} rowKey="categoryId" pagination={{ pageSize: 5 }} className="custom-dark-table" />
    </div>
  );
};

// ==========================================
// 2. COMPONENT MODAL THÊM/SỬA
// ==========================================
const CategoryModal = ({ isOpen, onClose, form, onSubmit, isEditing }) => (
  <Modal
    title={<span className="text-lg">{isEditing ? 'Sửa thông tin danh mục' : 'Tạo danh mục mới'}</span>}
    open={isOpen}
    onCancel={onClose}
    footer={null} 
    className="dark-modal"
    forceRender // <--- RẤT QUAN TRỌNG: Ép render Form để đổ dữ liệu Sửa không bị lỗi hụt
  >
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      {/* Đã sửa name thành categoryId cho khớp Backend */}
      <Form.Item name="categoryId" label={<span className="text-slate-300">Mã danh mục</span>} rules={[{ required: true, message: 'Vui lòng nhập mã danh mục!' }]}>
        {/* Nếu đang sửa thì không cho đổi Mã (ID) */}
        <Input disabled={isEditing} placeholder="VD: ELEC, FOOD..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      {/* Đã sửa name thành categoryName cho khớp Backend */}
      <Form.Item name="categoryName" label={<span className="text-slate-300">Tên danh mục</span>} rules={[{ required: true, message: 'Vui lòng nhập tên danh mục!' }]}>
        <Input placeholder="VD: Điện tử" className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Tạo danh mục'}
        </Button>
      </div>
    </Form>
  </Modal>
);

// ==========================================
// 3. COMPONENT CHÍNH
// ==========================================
export default function Category() {
  const [form] = Form.useForm();
  
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null); 

  const fetchCategories = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL);
      setCategories(response.data?.data || []);
    } catch (error) {
      console.error("Lỗi khi tải dữ liệu:", error);
      message.error("Không thể kết nối đến máy chủ!");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchCategories();
  }, []);

  const handleOpenModal = (category = null) => {
    if (category) {
      setEditingId(category.categoryId);
      form.setFieldsValue(category); 
    } else {
      setEditingId(null);
      form.resetFields(); 
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    form.resetFields();
  };

  const handleSubmit = async (values) => {
    try {
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, values);
        message.success('Cập nhật danh mục thành công!');
      } else {
        await axios.post(API_URL, values);
        message.success('Tạo danh mục mới thành công!');
      }
      handleCloseModal();
      fetchCategories(); 
    } catch (error) {
      console.error("Lỗi khi lưu:", error);
      message.error("Có lỗi xảy ra khi lưu dữ liệu!");
    }
  };

  const handleDelete = async (id) => {
    try {
       await axios.delete(`${API_URL}/${id}`);
      message.success('Đã xóa danh mục này!');
      fetchCategories(); 
    } catch (error) {
      console.error("Lỗi khi xóa:", error);
      message.error("Không thể xóa danh mục này!");
    }
  };

  const filteredCategories = categories.filter(cat => 
    cat.categoryName?.toLowerCase().includes(searchTerm.toLowerCase()) || 
    cat.categoryId?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý danh mục</h2>
            <p className="text-slate-400 text-base mt-1">Quản lý các danh mục sản phẩm kho</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Tạo danh mục
          </button>
        </div>

        <div className="bg-[#0F172A] border border-slate-800 p-4 rounded-t-2xl flex justify-between items-center">
          <div className="relative w-[450px]">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input 
              type="text" placeholder="Tìm kiếm danh mục theo tên hoặc mã..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-[#1E293B] border border-slate-700 text-white text-sm rounded-lg pl-10 pr-4 py-2.5 focus:outline-none focus:border-blue-500 transition-colors"
            />
          </div>
        </div>

        <CategoryTable categories={filteredCategories} isLoading={isLoading} onEdit={handleOpenModal} onDelete={handleDelete} />

        <CategoryModal isOpen={isModalOpen} onClose={handleCloseModal} form={form} onSubmit={handleSubmit} isEditing={!!editingId} />
      </div>
    </ConfigProvider>
  );
}