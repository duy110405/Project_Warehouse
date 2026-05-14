import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, Tag, Space, Popconfirm, ConfigProvider, theme, message } from 'antd';
import { Plus, Edit, Trash2, Search } from 'lucide-react';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/category'; 

//  COMPONENT BẢNG DỮ LIỆU (CategoryTable)
const CategoryTable = ({ categories, isLoading, onEdit, onDelete }) => {
  const columns = [
    {
      title: 'Mã danh mục',
      dataIndex: 'code',
      key: 'code',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Tên danh mục',
      dataIndex: 'name',
      key: 'name',
      render: (text) => <span className="text-slate-200 font-medium">{text}</span>,
    },
    {
      title: 'Mô tả',
      dataIndex: 'description',
      key: 'description',
      render: (text) => <span className="text-slate-400">{text}</span>,
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag color={status === 'Active' ? 'success' : 'error'} className="bg-transparent border border-current">
          {status === 'Active' ? 'Hoạt động' : 'Tạm dừng'}
        </Tag>
      ),
    },
    {
      title: 'Hành động',
      key: 'action',
      align: 'right',
      render: (_, record) => (
        <Space size="middle">
          <button 
            onClick={() => onEdit(record)}
            className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors"
          >
            <Edit size={18} />
          </button>
          
          <Popconfirm
            title="Xóa danh mục"
            description={`Bạn có chắc chắn muốn xóa "${record.name}" không?`}
            onConfirm={() => onDelete(record.id)}
            okText="Xóa"
            cancelText="Hủy"
            okButtonProps={{ danger: true }}
          >
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
      <Table 
        columns={columns} 
        dataSource={categories} 
        loading={isLoading}
        rowKey="id"
        pagination={{ pageSize: 5 }} 
        className="custom-dark-table"
      />
    </div>
  );
};

// 2. COMPONENT MODAL THÊM/SỬA (CategoryModal)

const CategoryModal = ({ isOpen, onClose, form, onSubmit, isEditing }) => (
  <Modal
    title={<span className="text-lg">{isEditing ? 'Sửa thông tin danh mục' : 'Tạo danh mục mới'}</span>}
    open={isOpen}
    onCancel={onClose}
    footer={null} 
    className="dark-modal"
  >
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      <Form.Item 
        name="code" 
        label={<span className="text-slate-300">Mã danh mục</span>}
        rules={[{ required: true, message: 'Vui lòng nhập mã danh mục!' }]}
      >
        <Input placeholder="VD: ELEC, FOOD..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <Form.Item 
        name="name" 
        label={<span className="text-slate-300">Tên danh mục</span>}
        rules={[{ required: true, message: 'Vui lòng nhập tên danh mục!' }]}
      >
        <Input placeholder="VD: Điện tử" className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <Form.Item 
        name="description" 
        label={<span className="text-slate-300">Mô tả</span>}
      >
        <Input.TextArea rows={3} placeholder="Mô tả chi tiết..." className="bg-[#1E293B] border-slate-700 text-white" />
      </Form.Item>

      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">
          Hủy
        </Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Tạo danh mục'}
        </Button>
      </div>
    </Form>
  </Modal>
);


export default function Category() {
  const [form] = Form.useForm();
  
  // State quản lý dữ liệu
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  // State quản lý Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null); 

  // LẤY DỮ LIỆU TỪ API
  const fetchCategories = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL);
      setCategories(response.data.data);
    
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

  // QUẢN LÝ MODAL
  const handleOpenModal = (category = null) => {
    if (category) {
      setEditingId(category.id);
      form.setFieldsValue(category); 
    } else {
      setEditingId(null);
      form.resetFields(); 
      form.setFieldsValue({ status: 'Active' }); 
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    form.resetFields();
  };

  // THÊM / SỬA DỮ LIỆU
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

  // XÓA DỮ LIỆU
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

  // BỘ LỌC TÌM KIẾM
  const filteredCategories = categories.filter(cat => 
    cat.name?.toLowerCase().includes(searchTerm.toLowerCase()) || 
    cat.code?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <ConfigProvider
      theme={{
        algorithm: theme.darkAlgorithm,
        token: {
          colorBgContainer: '#0F172A', 
          colorBorderSecondary: '#1E293B', 
          colorText: '#cbd5e1', 
        },
      }}
    >
      <div className="flex flex-col h-full">
        
        {/* HEADER CỦA TRANG */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý danh mục</h2>
            <p className="text-slate-400 text-base mt-1">Quản lý các danh mục sản phẩm kho</p>
          </div>
          <button 
            onClick={() => handleOpenModal()}
            className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium"
          >
            <Plus size={18} />
            Tạo danh mục
          </button>
        </div>

        {/* TOOLBAR (Tìm kiếm) */}
        <div className="bg-[#0F172A] border border-slate-800 p-4 rounded-t-2xl flex justify-between items-center">
          <div className="relative w-96">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input 
              type="text" 
              placeholder="Tìm kiếm danh mục theo tên hoặc mã..." 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-[#1E293B] border border-slate-700 text-white text-sm rounded-lg pl-10 pr-4 py-2.5 focus:outline-none focus:border-blue-500 transition-colors"
            />
          </div>
        </div>

        {/* COMPONENT BẢNG DỮ LIỆU */}
        <CategoryTable 
          categories={filteredCategories} 
          isLoading={isLoading} 
          onEdit={handleOpenModal} 
          onDelete={handleDelete} 
        />

        {/* COMPONENT MODAL THÊM/SỬA */}
        <CategoryModal 
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