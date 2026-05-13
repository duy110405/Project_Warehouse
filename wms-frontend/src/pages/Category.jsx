import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Tag, Space, Popconfirm, ConfigProvider, theme } from 'antd';
import { Plus, Edit, Trash2, Search } from 'lucide-react';

export default function Category() {
  const [form] = Form.useForm();
  
  // State quản lý dữ liệu (Sau này thay bằng data lấy từ API axios.get)
  const [categories, setCategories] = useState([
    { id: '1', code: 'ELEC', name: 'Electronics', description: 'Thiết bị điện tử, máy tính, điện thoại', status: 'Active' },
    { id: '2', code: 'APP', name: 'Apparel', description: 'Quần áo, thời trang nam nữ', status: 'Active' },
    { id: '3', code: 'HOME', name: 'Home & Garden', description: 'Đồ gia dụng, nội thất', status: 'Inactive' },
    { id: '4', code: 'FOOD', name: 'Food & Beverage', description: 'Thực phẩm, đồ uống đóng gói', status: 'Active' },
  ]);

  // State quản lý Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null); // Lưu ID của danh mục đang sửa (nếu có)

  // 1. CẤU HÌNH CỘT CHO BẢNG ANT DESIGN
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
          {/* Nút Sửa */}
          <button 
            onClick={() => handleOpenModal(record)}
            className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors"
          >
            <Edit size={18} />
          </button>
          
          {/* Nút Xóa (Có Popconfirm xác nhận tránh xóa nhầm) */}
          <Popconfirm
            title="Xóa danh mục"
            description={`Bạn có chắc chắn muốn xóa "${record.name}" không?`}
            onConfirm={() => handleDelete(record.id)}
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

  // 2. CÁC HÀM XỬ LÝ SỰ KIỆN (CRUD)
  const handleOpenModal = (category = null) => {
    if (category) {
      setEditingId(category.id);
      form.setFieldsValue(category); // Đổ dữ liệu cũ vào Form để sửa
    } else {
      setEditingId(null);
      form.resetFields(); // Làm sạch Form để thêm mới
      form.setFieldsValue({ status: 'Active' }); // Mặc định trạng thái
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    form.resetFields();
  };

  const handleSubmit = (values) => {
    if (editingId) {
      // Logic Cập nhật (Sửa API: axios.put)
      const updatedList = categories.map(cat => 
        cat.id === editingId ? { ...cat, ...values } : cat
      );
      setCategories(updatedList);
    } else {
      // Logic Thêm mới (Gọi API: axios.post)
      const newCategory = {
        id: Date.now().toString(), // Fake ID
        ...values
      };
      setCategories([...categories, newCategory]);
    }
    handleCloseModal();
  };

  const handleDelete = (id) => {
    // Logic Xóa (Gọi API: axios.delete)
    const filteredList = categories.filter(cat => cat.id !== id);
    setCategories(filteredList);
  };

  return (
    // Dùng ConfigProvider để ép AntD sang chế độ Dark Mode chuẩn tone nền
    <ConfigProvider
      theme={{
        algorithm: theme.darkAlgorithm,
        token: {
          colorBgContainer: '#0F172A', // Màu nền bảng
          colorBorderSecondary: '#1E293B', // Màu viền bảng
          colorText: '#cbd5e1', // Màu chữ
          sizeText: 28, // Kích thước chữ mặc định
        },
      }}
    >
      <div className="flex flex-col h-full">
        
        {/* HEADER CỦA TRANG */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Category Management</h2>
            <p className="text-slate-400 text-base mt-1">Manage all warehouse product categories</p>
          </div>
          <button 
            onClick={() => handleOpenModal()}
            className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium"
          >
            <Plus size={18} />
            Create Category
          </button>
        </div>

        {/* TOOLBAR (Tìm kiếm) */}
        <div className="bg-[#0F172A] border border-slate-800 p-4 rounded-t-2xl flex justify-between items-center">
          <div className="relative w-96">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input 
              type="text" 
              placeholder="Search categories by name or code..." 
              className="w-full bg-[#1E293B] border border-slate-700 text-white text-sm rounded-lg pl-10 pr-4 py-2.5 focus:outline-none focus:border-blue-500 transition-colors"
            />
          </div>
        </div>

        {/* BẢNG DỮ LIỆU */}
        <div className="bg-[#0F172A] border-x border-b border-slate-800 rounded-b-2xl overflow-hidden shadow-xl text-base">
          <Table 
            columns={columns} 
            dataSource={categories} 
            rowKey="id"
            pagination={{ pageSize: 5 }} // Phân 5 dòng 1 trang
            className="custom-dark-table"
          />
        </div>

        {/* MODAL THÊM / SỬA */}
        <Modal
          title={<span className="text-lg">{editingId ? 'Edit Category' : 'Create New Category'}</span>}
          open={isModalOpen}
          onCancel={handleCloseModal}
          footer={null} // Ẩn footer mặc định để tự custom nút
          className="dark-modal"
        >
          <Form form={form} layout="vertical" onFinish={handleSubmit} className="mt-6">
            <Form.Item 
              name="code" 
              label={<span className="text-slate-300">Category Code</span>}
              rules={[{ required: true, message: 'Vui lòng nhập mã danh mục!' }]}
            >
              <Input placeholder="e.g., ELEC, FOOD..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
            </Form.Item>

            <Form.Item 
              name="name" 
              label={<span className="text-slate-300">Category Name</span>}
              rules={[{ required: true, message: 'Vui lòng nhập tên danh mục!' }]}
            >
              <Input placeholder="e.g., Electronics" className="bg-[#1E293B] border-slate-700 text-white py-2" />
            </Form.Item>

            <Form.Item 
              name="description" 
              label={<span className="text-slate-300">Description</span>}
            >
              <Input.TextArea rows={3} placeholder="Mô tả chi tiết..." className="bg-[#1E293B] border-slate-700 text-white" />
            </Form.Item>

            <div className="flex justify-end gap-3 mt-8">
              <Button onClick={handleCloseModal} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">
                Cancel
              </Button>
              <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500">
                {editingId ? 'Save Changes' : 'Create Category'}
              </Button>
            </div>
          </Form>
        </Modal>

      </div>
    </ConfigProvider>
  );
}