import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Tag, Space, Popconfirm, ConfigProvider, theme, Image , Select , Drawer, Divider } from 'antd';
import { Plus, Edit, Trash2, Search, ChevronDown, ChevronRight, Package , MapPin ,Eye, Settings2 } from 'lucide-react';

// Component Menu Item dùng chung cho Dropdown
const MenuItem = ({ icon, label, onClick }) => (
  <button 
    onClick={onClick}
    className="w-full flex items-center gap-3 px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-lg transition-all"
  >
    {icon}
    <span className="text-sm font-medium">{label}</span>
  </button>
);

export default function Product() {
  const [form] = Form.useForm();
  
  //  dữ liệu giả lập
 const [products, setProducts] = useState([
    { 
      id: 'H001', code: 'H001', name: 'Laptop Nitro 5', quantity: 10, price: 15000000, categoryName: 'Điện tử', zoneName: 'Khu A - Công nghệ', image: 'https://placehold.co/100x100/1E293B/FFF?text=N5',
      materials: [
        { id: 'NL01', name: 'Màn hình 15.6 inch 144Hz', quantity: 1, unit: 'Cái' },
        { id: 'NL02', name: 'Bàn phím LED RGB', quantity: 1, unit: 'Bộ' },
        { id: 'NL03', name: 'RAM 8GB DDR4', quantity: 2, unit: 'Thanh' }
      ]
    },
    { 
      id: 'H002', code: 'H002', name: 'Áo Sơ Mi Nam', quantity: 0, price: 250000, categoryName: 'Thời trang', zoneName: 'Khu B - Thời trang', image: 'https://placehold.co/100x100/1E293B/FFF?text=Ao',
      materials: [
        { id: 'NL04', name: 'Vải Cotton 100%', quantity: 1.5, unit: 'Mét' },
        { id: 'NL05', name: 'Cúc áo nhựa mờ', quantity: 6, unit: 'Cái' },
        { id: 'NL06', name: 'Chỉ may', quantity: 0.1, unit: 'Cuộn' }
      ]
    },
  ]);
// State Modal (Thêm/Sửa)
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  
  // State Drawer (Xem chi tiết & Định mức)
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [viewingProduct, setViewingProduct] = useState(null);

  // State Bộ lọc
  const [openCategories, setOpenCategories] = useState(false);
  const [openZones, setOpenZones] = useState(false);
  const [selectedFilter, setSelectedFilter] = useState({ category: 'Tất cả danh mục', zone: 'Tất cả khu vực' });

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
      title: 'Danh mục', 
      dataIndex: 'categoryName',
      key: 'categoryName',
      render: (text) => <span className="text-slate-400">{text}</span>,
    },
    {
      title: 'Khu vực (Zone)',
      dataIndex: 'zoneName',
      key: 'zoneName',
      render: (text) => <Tag className="bg-slate-800 border-slate-600 text-slate-300">{text}</Tag>,
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
      key: 'status',
      render: (_, record) => (
        <Tag color={record.quantity > 0 ? 'success' : 'error'} className="bg-transparent border border-current">
          {record.quantity > 0 ? 'Còn hàng' : 'Hết hàng'}
        </Tag>
      ),
    },
    {
      title: 'Hành động',
      key: 'action',
      align: 'right',
      render: (_, record) => (
        <Space size="small">
       
          <button onClick={() => handleOpenDrawer(record)} className="p-2 text-slate-400 hover:text-emerald-400 hover:bg-emerald-400/10 rounded-lg transition-colors" title="Xem định mức">
            <Eye size={18} />
          </button>

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

  // CỘT BẢNG NGUYÊN LIỆU (Bảng con nằm trong Drawer)
  const materialColumns = [
    { title: 'Mã NL', dataIndex: 'id', key: 'id', render: (text) => <span className="text-slate-400">{text}</span> },
    { title: 'Tên nguyên liệu', dataIndex: 'name', key: 'name', render: (text) => <span className="text-slate-200">{text}</span> },
    { title: 'Định mức cần dùng', dataIndex: 'quantity', key: 'quantity', align: 'center', render: (qty) => <span className="font-semibold text-emerald-400">{qty}</span> },
    { title: 'Đơn vị tính', dataIndex: 'unit', key: 'unit', align: 'center', render: (text) => <Tag color="default" className="bg-slate-800 border-slate-600">{text}</Tag> },
  ];

  const handleOpenDrawer = (product) => { setViewingProduct(product); setIsDrawerOpen(true); };
  const handleCloseDrawer = () => { setIsDrawerOpen(false); setTimeout(() => setViewingProduct(null), 300); };

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
        categoryName: 'Category Name', // Fake name for now
        zoneName: 'Zone Name', // Fake name for now
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
            <h2 className="text-2xl font-bold text-white tracking-wide">Sản phẩm</h2>
            <p className="text-slate-400 text-sm mt-1">Quản lý danh mục sản phẩm và tồn kho</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} />
            Tạo sản phẩm
          </button>
        </div>

        {/* THANH CÔNG CỤ: TÌM KIẾM & BỘ LỌC (2 khối riêng biệt) */}
        <div className="flex items-center gap-4 mb-6">
          
          {/* Khối 1: Ô tìm kiếm */}
          <div className="flex-1 h-11 bg-[#1E293B] border border-slate-700 rounded-xl flex items-center px-4 shadow-sm transition-colors focus-within:border-blue-500 focus-within:ring-1 focus-within:ring-blue-500">
            <Search className="text-slate-400 mr-3" size={18} />
            <input 
              type="text" 
              placeholder="Tìm kiếm sản phẩm theo tên, mã hoặc SKU..." 
              className="w-full bg-transparent text-white text-sm focus:outline-none placeholder-slate-400"
            />
          </div>

         {/* Lọc Danh Mục */}
          <div className="w-56 h-11 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
              <button
                onClick={() => { setOpenCategories(!openCategories); setOpenZones(false); }}
                className="w-full h-full flex items-center justify-between px-4 text-slate-300 hover:text-white rounded-xl"
              >
                <div className="flex items-center gap-2">
                   <Package size={16} className="text-blue-400" />
                   <span className="text-sm font-medium truncate">{selectedFilter.category}</span>
                </div>
                {openCategories ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
              </button>
              {openCategories && (
                <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20">
                  <MenuItem icon={<Package size={18} />} label="Tất cả danh mục" onClick={() => { setSelectedFilter({...selectedFilter, category: 'Tất cả danh mục'}); setOpenCategories(false); }} />
                  <MenuItem icon={<Package size={18} />} label="Điện tử" onClick={() => { setSelectedFilter({...selectedFilter, category: 'Electronics'}); setOpenCategories(false); }} />
                  <MenuItem icon={<Package size={18} />} label="Thời trang" onClick={() => { setSelectedFilter({...selectedFilter, category: 'Apparel'}); setOpenCategories(false); }} />
                </div>
              )}
          </div>

          {/* Lọc Khu Vực (Zone)*/}
          <div className="w-56 h-11 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
              <button
                onClick={() => { setOpenZones(!openZones); setOpenCategories(false); }}
                className="w-full h-full flex items-center justify-between px-4 text-slate-300 hover:text-white rounded-xl"
              >
                <div className="flex items-center gap-2">
                   <MapPin size={16} className="text-emerald-400" />
                   <span className="text-sm font-medium truncate">{selectedFilter.zone}</span>
                </div>
                {openZones ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
              </button>
              {openZones && (
                <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20">
                  <MenuItem icon={<MapPin size={18} />} label="Tất cả khu vực" onClick={() => { setSelectedFilter({...selectedFilter, zone: 'Tất cả khu vực'}); setOpenZones(false); }} />
                  <MenuItem icon={<MapPin size={18} />} label="Khu A - Công nghệ" onClick={() => { setSelectedFilter({...selectedFilter, zone: 'Zone A - Tech'}); setOpenZones(false); }} />
                  <MenuItem icon={<MapPin size={18} />} label="Khu B - Thời trang" onClick={() => { setSelectedFilter({...selectedFilter, zone: 'Zone B - Clothes'}); setOpenZones(false); }} />
                </div>
              )}
          </div>
        </div>

        {/* BẢNG DỮ LIỆU */}
        <div className="bg-[#0F172A] border border-slate-800 rounded-2xl overflow-hidden shadow-xl">
          <Table columns={columns} dataSource={products} rowKey="id" pagination={{ pageSize: 6 }} className="custom-dark-table" scroll={{ x: 1000 }} />
        </div>
       
        {/* DRAWER: HIỂN THỊ CHI TIẾT SẢN PHẨM VÀ ĐỊNH MỨC NGUYÊN LIỆU (BOM) */}

        <Drawer
          title={
            <div className="flex items-center gap-3">
              <Settings2 className="text-blue-400" size={24} />
              <span className="text-white text-lg font-semibold tracking-wide">Chi tiết Định mức (BOM)</span>
            </div>
          }
          placement="right"
          width={650}
          onClose={handleCloseDrawer}
          open={isDrawerOpen}
          // Custom CSS cho Drawer hòa hợp với Dark Mode
          styles={{
            header: { background: '#0F172A', borderBottom: '1px solid #1E293B' },
            body: { background: '#0B1120', padding: '24px' },
            mask: { backdropFilter: 'blur(4px)' }
          }}
          closeIcon={<span className="text-slate-400 hover:text-white transition-colors">✕</span>}
        >
          {viewingProduct && (
            <div className="flex flex-col h-full">
              
              {/* Box 1: Thông tin Sản phẩm */}
              <div className="bg-[#0F172A] p-5 rounded-xl border border-slate-800 flex gap-5 items-center mb-6">
                <Image src={viewingProduct.image} width={80} height={80} className="rounded-lg object-cover border border-slate-700" fallback="https://placehold.co/100x100/1E293B/FFF?text=No+Img" />
                <div>
                  <h3 className="text-xl font-bold text-white mb-1">{viewingProduct.name}</h3>
                  <div className="flex gap-4 text-sm text-slate-400">
                    <p>Mã SP: <span className="text-blue-400 font-medium">{viewingProduct.code}</span></p>
                    <p>Danh mục: <span className="text-slate-300">{viewingProduct.categoryName}</span></p>
                  </div>
                  <div className="mt-2">
                     <Tag color={viewingProduct.quantity > 0 ? 'success' : 'error'} className="bg-transparent border border-current">
                       Tồn kho: {viewingProduct.quantity}
                     </Tag>
                  </div>
                </div>
              </div>

              <Divider className="border-slate-800 m-0 mb-6" />
              {/* Box 2: Bảng Nguyên Liệu Cấu Thành */}
              <div className="flex-1">
                <div className="flex justify-between items-center mb-4">
                  <h4 className="text-lg font-semibold text-white">Công thức Nguyên liệu</h4>
                  <button className="text-sm text-blue-400 hover:text-blue-300 font-medium flex items-center gap-1">
                     <Edit size={14}/> Sửa định mức
                  </button>
                </div>
                
                <Table 
                  columns={materialColumns} 
                  dataSource={viewingProduct.materials || []} 
                  rowKey="id" 
                  pagination={false} 
                  size="small"
                  className="custom-dark-table border border-slate-800 rounded-xl overflow-hidden"
                  locale={{ emptyText: <span className="text-slate-500">Sản phẩm này chưa có công thức nguyên liệu</span> }}
                />
              </div>
            </div>
          )}
        </Drawer>

        {/* MODAL THÊM / SỬA */}
        <Modal title={<span className="text-lg">{editingId ? 'Sửa thông tin hàng hóa' : 'Thêm hàng hóa mới'}</span>} open={isModalOpen} onCancel={handleCloseModal} footer={null} className="dark-modal" width={650}>
          <Form form={form} layout="vertical" onFinish={handleSubmit} className="mt-6">
            
            <div className="grid grid-cols-2 gap-4">
               <Form.Item name="code" label={<span className="text-slate-300">Mã sản phẩm (MaH)</span>} rules={[{ required: true }]}>
                 <Input placeholder="VD: H001" className="bg-[#1E293B] border-slate-700 text-white py-2" disabled={editingId !== null} />
               </Form.Item>
               
               <Form.Item name="name" label={<span className="text-slate-300">Tên sản phẩm (TenH)</span>} rules={[{ required: true }]}>
                 <Input placeholder="VD: Laptop Gaming..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
               </Form.Item>
            </div>

            <div className="grid grid-cols-2 gap-4">
               <Form.Item name="price" label={<span className="text-slate-300">Giá bán (Gia)</span>} rules={[{ required: true }]}>
                 <Input type="number" placeholder="15000000" className="bg-[#1E293B] border-slate-700 text-white py-2" />
               </Form.Item>

               <Form.Item name="quantity" label={<span className="text-slate-300">Số lượng (SoLg)</span>} rules={[{ required: true }]}>
                 <Input type="number" placeholder="10" className="bg-[#1E293B] border-slate-700 text-white py-2" />
               </Form.Item>
            </div>

            <div className="grid grid-cols-2 gap-4">
                {/* Select Danh mục */}
               <Form.Item name="categoryId" label={<span className="text-slate-300">Danh mục sản phẩm</span>} rules={[{ required: true, message: 'Vui lòng chọn danh mục' }]}>
                  <Select placeholder="Chọn danh mục" className="h-[40px] custom-dark-select">
                      <Select.Option value="C01">Điện tử</Select.Option>
                      <Select.Option value="C02">Thời trang</Select.Option>
                  </Select>
               </Form.Item>

               {/* Select Khu vực */}
               <Form.Item name="zoneId" label={<span className="text-slate-300">Khu vực lưu trữ (Zone)</span>} rules={[{ required: true, message: 'Vui lòng chọn khu vực' }]}>
                  <Select placeholder="Chọn khu vực" className="h-[40px] custom-dark-select">
                      <Select.Option value="Z01">Zone A</Select.Option>
                      <Select.Option value="Z02">Zone B</Select.Option>
                  </Select>
               </Form.Item>
            </div>

            <Form.Item name="image" label={<span className="text-slate-300">Link ảnh (anhHang)</span>}>
              <Input placeholder="https://domain.com/image.jpg" className="bg-[#1E293B] border-slate-700 text-white py-2" />
            </Form.Item>

            <div className="flex justify-end gap-3 mt-8">
              <Button onClick={handleCloseModal} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
              <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
                {editingId ? 'Lưu thay đổi' : 'Tạo sản phẩm'}
              </Button>
            </div>
          </Form>
        </Modal>
      </div>
    </ConfigProvider>
  );
}