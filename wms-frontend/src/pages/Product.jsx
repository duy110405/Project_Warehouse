import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Tag, Space, Popconfirm, ConfigProvider, theme, Image } from 'antd';
import { Plus, Edit, Trash2, Search, ChevronDown, ChevronRight, Package, ArchiveRestore, PackageOpen } from 'lucide-react';

// Khai báo component MenuItem cục bộ cho phần Dropdown danh mục
const MenuItem = ({ icon, label }) => (
  <button className="w-full flex items-center gap-3 px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-lg transition-all">
    {icon}
    <span className="text-sm font-medium">{label}</span>
  </button>
);

export default function Product() {
  const [form] = Form.useForm();
  
  // 1. Thêm trường 'image' vào dữ liệu giả lập
  const [products, setProducts] = useState([
    { id: '1', code: 'H001', name: 'Laptop Nitro 5', quantity: 10, price: 15000000, status: 'Active', image: 'https://placehold.co/100x100/1E293B/FFF?text=N5' },
    { id: '2', code: 'H002', name: 'MacBook Pro', quantity: 20, price: 45000000, status: 'Active', image: 'https://placehold.co/100x100/1E293B/FFF?text=Mac' },
    { id: '3', code: 'H003', name: 'Laptop Asus', quantity: 15, price: 12000000, status: 'Inactive', image: 'https://placehold.co/100x100/1E293B/FFF?text=Asus' },
    { id: '4', code: 'H004', name: 'Laptop Dell', quantity: 25, price: 18000000, status: 'Active', image: 'https://placehold.co/100x100/1E293B/FFF?text=Dell' },
  ]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [openProducts, setOpenProducts] = useState(false);

  // 2. CẤU HÌNH CỘT CHO BẢNG (Bổ sung cột Ảnh lên đầu)
  const columns = [
    {
      title: 'Ảnh',
      dataIndex: 'image',
      key: 'image',
      width: 80,
      render: (img) => (
        <Image 
          src={img} 
          width={44} 
          height={44} 
          className="rounded-lg object-cover border border-slate-700"
          fallback="https://placehold.co/100x100/1E293B/FFF?text=No+Img"
        />
      )
    },
    {
      title: 'Mã SP',
      dataIndex: 'code',
      key: 'code',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Tên sản phẩm',
      dataIndex: 'name',
      key: 'name',
      render: (text) => <span className="text-slate-200 font-medium">{text}</span>,
    }, 
    {
      title: 'Tồn kho',
      dataIndex: 'quantity',
      key: 'quantity',
      render: (quantity) => (
         <span className={`font-medium ${quantity < 15 ? 'text-red-400' : 'text-emerald-400'}`}>
            {quantity}
         </span>
      ),
    },
    {
      title: 'Giá',
      dataIndex: 'price',
      key: 'price',
      render: (price) => <span className="text-slate-400">{price.toLocaleString()} VND</span>,
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
          <button onClick={() => handleOpenModal(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
            <Edit size={18} />
          </button>
          <Popconfirm title="Xóa sản phẩm" description={`Bạn có chắc chắn muốn xóa "${record.name}"?`} onConfirm={() => handleDelete(record.id)} okText="Xóa" cancelText="Hủy" okButtonProps={{ danger: true }}>
            <button className="p-2 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-colors">
              <Trash2 size={18} />
            </button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const handleOpenModal = (product = null) => {
    if (product) {
      setEditingId(product.id);
      form.setFieldsValue(product);
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

  const handleSubmit = (values) => {
    if (editingId) {
      const updatedList = products.map(product => product.id === editingId ? { ...product, ...values } : product);
      setProducts(updatedList);
    } else {
      const newProduct = {
        id: Date.now().toString(),
        image: values.image || 'https://placehold.co/100x100/1E293B/FFF?text=New', // Cấp ảnh mặc định nếu để trống
        ...values
      };
      setProducts([...products, newProduct]);
    }
    handleCloseModal();
  };

  const handleDelete = (id) => {
    const filteredList = products.filter(product => product.id !== id);
    setProducts(filteredList);
  };
  
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
      <div className="flex flex-col h-full max-w-9xl mx-auto">
        {/* Header */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Products</h2>
            <p className="text-slate-400 text-sm mt-1">Manage your product catalog and inventory</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} />
            Create Product
          </button>
        </div>

        {/* THANH CÔNG CỤ: TÌM KIẾM & BỘ LỌC (2 khối riêng biệt) */}
        <div className="flex items-center gap-4 mb-6">
          
          {/* Khối 1: Ô tìm kiếm */}
          <div className="flex-1 h-11 bg-[#1E293B] border border-slate-700 rounded-xl flex items-center px-4 shadow-sm transition-colors focus-within:border-blue-500 focus-within:ring-1 focus-within:ring-blue-500">
            <Search className="text-slate-400 mr-3" size={18} />
            <input 
              type="text" 
              placeholder="Search products by name, code or SKU..." 
              className="w-full bg-transparent text-white text-sm focus:outline-none placeholder-slate-400"
            />
          </div>

          {/* Khối 2: Nút Lọc Danh Mục */}
          <div className="w-64 h-11 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
              <button
                onClick={() => setOpenProducts(!openProducts)}
                className="w-full h-full flex items-center justify-between px-4 text-slate-300 hover:text-white rounded-xl"
              >
                <span className="text-sm font-medium tracking-wide">All Categories</span>
                {openProducts ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
              </button>
              
              {/* Menu xổ xuống */}
              {openProducts && (
                <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20">
                  <MenuItem icon={<Package size={18} />} label="Laptop" />
                  <MenuItem icon={<ArchiveRestore size={18} />} label="Headphones"/> 
                  <MenuItem icon={<PackageOpen size={18} />} label="Keyboard"/> 
                </div>
              )}
          </div>
        </div>

        {/* BẢNG DỮ LIỆU */}
        <div className="bg-[#0F172A] border border-slate-800 rounded-2xl overflow-hidden shadow-xl">
          <Table 
            columns={columns} 
            dataSource={products} // Đã fix thành products
            rowKey="id"
            pagination={{ pageSize: 6 }} 
            className="custom-dark-table"
          />
        </div>

        {/* MODAL THÊM / SỬA */}
        <Modal
          title={<span className="text-lg">{editingId ? 'Edit Product' : 'Create New Product'}</span>}
          open={isModalOpen}
          onCancel={handleCloseModal}
          footer={null}
          className="dark-modal"
        >
          <Form form={form} layout="vertical" onFinish={handleSubmit} className="mt-6">
            <div className="grid grid-cols-2 gap-4">
               <Form.Item name="code" label={<span className="text-slate-300">Product Code</span>} rules={[{ required: true }]}>
                 <Input placeholder="e.g., H001" className="bg-[#1E293B] border-slate-700 text-white" />
               </Form.Item>
               
               <Form.Item name="price" label={<span className="text-slate-300">Price (VND)</span>} rules={[{ required: true }]}>
                 <Input type="number" placeholder="15000000" className="bg-[#1E293B] border-slate-700 text-white" />
               </Form.Item>
            </div>

            <Form.Item name="name" label={<span className="text-slate-300">Product Name</span>} rules={[{ required: true }]}>
              <Input placeholder="e.g., Laptop Gaming..." className="bg-[#1E293B] border-slate-700 text-white" />
            </Form.Item>

            {/* MỚI: Thêm ô nhập Link ảnh */}
            <Form.Item name="image" label={<span className="text-slate-300">Image URL</span>}>
              <Input placeholder="https://domain.com/image.jpg" className="bg-[#1E293B] border-slate-700 text-white" />
            </Form.Item>

            <div className="flex justify-end gap-3 mt-8">
              <Button onClick={handleCloseModal} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">
                Cancel
              </Button>
              <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
                {editingId ? 'Save Changes' : 'Create Product'}
              </Button>
            </div>
          </Form>
        </Modal>
      </div>
    </ConfigProvider>
  );
}