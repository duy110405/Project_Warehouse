import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, Tag, Space, Popconfirm, ConfigProvider, theme, Image, Select, Drawer, Divider, message } from 'antd';
import { Plus, Edit, Trash2, Search, ChevronDown, ChevronRight, Package, MapPin, Eye, Settings2 } from 'lucide-react';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/product'; 
const API_CATEGORY_URL = 'http://localhost:8080/api/category';
const API_ZONE_URL = 'http://localhost:8080/api/zone'; 
const API_MATERIAL_URL = 'http://localhost:8080/api/material';

// Component Menu Item dùng chung cho Dropdown
const MenuItem = ({ icon, label, onClick }) => (
  <button onClick={onClick} className="w-full flex items-center gap-3 px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-lg transition-all">
    {icon}
    <span className="text-sm font-medium">{label}</span>
  </button>
);

// CỘT BẢNG NGUYÊN LIỆU (Bảng con nằm trong Drawer)
const materialColumns = [
  { title: 'Mã NL', dataIndex: 'materialId', key: 'materialId', render: (text) => <span className="text-slate-400">{text}</span> }, 
  { title: 'Tên nguyên liệu', dataIndex: 'materialName', key: 'materialName', render: (text) => <span className="text-slate-200">{text}</span> }, 
  { title: 'Định mức cần dùng', dataIndex: 'requiredQuantity', key: 'requiredQuantity', align: 'center', render: (qty) => <span className="font-semibold text-emerald-400">{qty}</span> },
  { title: 'Đơn vị tính', dataIndex: 'unit', key: 'unit', align: 'center', render: (text) => <Tag color="default" className="bg-slate-800 border-slate-600">{text}</Tag> },
];

// 1. COMPONENT BẢNG DỮ LIỆU SẢN PHẨM
const ProductTable = ({ products, isLoading, onEdit, onDelete, onRecipe }) => {
  const columns = [
    {
      title: 'Ảnh',
      dataIndex: 'productImage', 
      key: 'productImage',
      width: 80,
      render: (img) => (
        <Image src={img} width={44} height={44} className="rounded-lg object-cover border border-slate-700" fallback="https://placehold.co/100x100/1E293B/FFF?text=No+Img" />
      )
    },
    {
      title: 'Mã SP',
      dataIndex: 'productId', 
      key: 'productId',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Tên sản phẩm',
      dataIndex: 'productName', 
      key: 'productName',
      render: (text) => <span className="text-slate-200 font-medium">{text}</span>,
    }, 
    {
      title: 'Danh mục', 
      dataIndex: 'categoryName',
      key: 'categoryName',
      render: (text) => <span className="text-slate-400">{text || '---'}</span>,
    },
    {
      title: 'Khu vực (Zone)',
      dataIndex: 'zoneName',
      key: 'zoneName',
      render: (text) => <Tag className="bg-slate-800 border-slate-600 text-slate-300">{text || '---'}</Tag>,
    },
    {
      title: 'Tồn kho',
      dataIndex: 'quantity',
      key: 'quantity',
      render: (quantity) => {
        const qty = quantity || 0;
        return <span className={`font-medium ${qty < 15 ? 'text-red-400' : 'text-emerald-400'}`}>{qty}</span>;
      }
    },
    {
      title: 'Giá',
      dataIndex: 'price',
      key: 'price',
      render: (price) => <span className="text-slate-400">{(price || 0).toLocaleString()} VND</span>,
    },
    {
      title: 'Hành động',
      key: 'action',
      align: 'right',
      render: (_, record) => (
        <Space size="small">
          <button onClick={() => onRecipe(record)} className="p-2 text-slate-400 hover:text-emerald-400 hover:bg-emerald-400/10 rounded-lg transition-colors" title="Xem định mức">
            <Eye size={18} />
          </button>
          <button onClick={() => onEdit(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
            <Edit size={18} />
          </button>
          {/* ĐÃ FIX: Truyền record.productId vào hàm Delete */}
          <Popconfirm title="Xóa sản phẩm" description={`Bạn có chắc muốn xóa "${record.productName}"?`} onConfirm={() => onDelete(record.productId)} okText="Xóa" cancelText="Hủy" okButtonProps={{ danger: true }}>
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
        dataSource={products} 
        loading={isLoading}
        rowKey="productId" // ĐÃ FIX: Khóa chính là productId, giúp xóa ngay lỗi Warning đỏ ở Console
        pagination={{ pageSize: 5 }} 
        className="custom-dark-table"
      />
    </div>
  );
};


// 2. COMPONENT MODAL THÊM/SỬA
const ProductModal = ({ isOpen, onClose, form, onSubmit, isEditing, categories, zones, materialsList }) => (
  <Modal title={<span className="text-lg">{isEditing ? 'Sửa thông tin sản phẩm' : 'Thêm sản phẩm mới'}</span>} open={isOpen} onCancel={onClose} footer={null} className="dark-modal" width={700}>
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      
      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="productId" label={<span className="text-slate-300">Mã sản phẩm</span>}>
          <Input placeholder="Tự động tạo..." className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled />
        </Form.Item>
        <Form.Item name="productName" label={<span className="text-slate-300">Tên sản phẩm</span>} rules={[{ required: true }]}>
          <Input placeholder="VD: Laptop Gaming..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
        </Form.Item>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="price" label={<span className="text-slate-300">Giá bán</span>} rules={[{ required: true }]}>
          <Input type="number" placeholder="15000000" className="bg-[#1E293B] border-slate-700 text-white py-2" />
        </Form.Item>
        <Form.Item name="quantity" label={<span className="text-slate-300">Số lượng tồn kho</span>}>
          <Input type="number" className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled placeholder="Tự động (Mặc định 0)" />
        </Form.Item>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="categoryId" label={<span className="text-slate-300">Danh mục sản phẩm</span>} rules={[{ required: true }]}>
          <Select placeholder="Chọn danh mục" className="h-[40px] custom-dark-select">
             {categories.map(c => <Select.Option key={c.categoryId} value={c.categoryId}>{c.categoryName}</Select.Option>)}
          </Select>
        </Form.Item>

        <Form.Item name="zoneId" label={<span className="text-slate-300">Khu vực lưu trữ (Zone)</span>} rules={[{ required: true }]}>
          <Select placeholder="Chọn khu vực" className="h-[40px] custom-dark-select">
             {zones.map(z => <Select.Option key={z.zoneId} value={z.zoneId}>{z.zoneName}</Select.Option>)}
          </Select>
        </Form.Item>
      </div>

      <Form.Item name="productImage" label={<span className="text-slate-300">Link ảnh</span>}>
        <Input placeholder="https://domain.com/image.jpg" className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <Divider className="border-slate-700 my-4" />
      <div className="flex items-center gap-2 mb-4">
         <Settings2 size={18} className="text-blue-400"/>
         <h4 className="text-slate-200 font-semibold">Công thức nguyên liệu (BOM)</h4>
      </div>

      <Form.List name="materials">
        {(fields, { add, remove }) => (
          <>
            {fields.map(({ key, name, ...restField }) => (
              <div key={key} className="flex gap-3 mb-3 items-start">
                <Form.Item {...restField} name={[name, 'id']} rules={[{ required: true, message: 'Thiếu' }]} className="flex-1 mb-0">
                  <Select placeholder="Chọn nguyên liệu..." className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
                    {materialsList.map(m => (
                      <Select.Option key={m.materialId} value={m.materialId}>
                        {m.materialName} ({m.materialId})
                      </Select.Option>
                    ))}
                  </Select>
                </Form.Item>

                <Form.Item {...restField} name={[name, 'quantity']} rules={[{ required: true, message: 'Thiếu' }]} className="w-32 mb-0">
                  <Input type="number" placeholder="Định mức" className="bg-[#1E293B] border-slate-700 text-white h-[40px]" min={0.1} step={0.1} />
                </Form.Item>

                {/*
                <Form.Item {...restField} name={[name, 'unit']} rules={[{ required: true, message: 'Thiếu' }]} className="w-32 mb-0">
                  <Input type="text" placeholder="Đơn vị tính" className="bg-[#1E293B] border-slate-700 text-white h-[40px]" min={0.1} step={0.1} />
                </Form.Item>
                  */}

                <button type="button" onClick={() => remove(name)} className="h-[40px] px-3 text-slate-400 hover:text-red-400 bg-[#1E293B] hover:bg-red-400/10 rounded-lg transition-colors border border-slate-700">
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
            
            <Button type="dashed" onClick={() => add()} block icon={<Plus size={16}/>} className="bg-transparent border-slate-700 text-blue-400 hover:text-blue-300 hover:border-blue-400 h-[40px] mt-2">
              Thêm nguyên liệu
            </Button>
          </>
        )}
      </Form.List>

      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Tạo sản phẩm'}
        </Button>
      </div>
    </Form>
  </Modal>
);

// 3. COMPONENT CHÍNH
export default function Product() {
  const [form] = Form.useForm();

  const [products, setProducts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [materialsList, setMaterialsList] = useState([]);

  const [zones, setZones] = useState([]); 
  const [categories, setCategories] = useState([]); 
  
  const [selectedZoneId, setSelectedZoneId] = useState(null); 
  const [selectedZoneName, setSelectedZoneName] = useState('Tất cả khu vực');
  const [openZones, setOpenZones] = useState(false);

  const [selectedCategoryId, setSelectedCategoryId] = useState(null); 
  const [selectedCategoryName, setSelectedCategoryName] = useState('Tất cả danh mục');
  const [openCategories, setOpenCategories] = useState(false);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [viewingProduct, setViewingProduct] = useState(null);

  useEffect(() => {
    const fetchSelectData = async () => {
      try {
        const [zoneRes, catRes, matRes] = await Promise.all([
          axios.get(API_ZONE_URL),
          axios.get(API_CATEGORY_URL),
          axios.get(API_MATERIAL_URL) 
        ]);
        setZones(zoneRes.data?.data || []);
        setCategories(catRes.data?.data || []);
        setMaterialsList(matRes.data?.data || []); 
      } catch (error) {
        console.error("Lỗi lấy dữ liệu phụ trợ:", error);
      }
    };
    fetchSelectData();
  }, []);

  const fetchProducts = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL, {
        params: { search: searchTerm || null, categoryId: selectedCategoryId || null, zoneId: selectedZoneId || null }
      });
      setProducts(response.data?.data || []);
    } catch (error) {
      console.error(error);
      message.error("Lỗi khi tải dữ liệu sản phẩm!");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => { 
    // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchProducts(); }, [searchTerm, selectedCategoryId, selectedZoneId]);

  // HÀM LƯU DỮ LIỆU
  const handleSubmit = async (values) => {
    try {
      // ĐÃ FIX: Chuyển đổi dữ liệu của Form (materials) sang chuẩn của Backend (materialIds)
      const payload = { 
        ...values, 
        price: Number(values.price),
        quantity: values.quantity ? Number(values.quantity) : 0,
        productImage: values.productImage || 'https://placehold.co/100x100/1E293B/FFF?text=New',
        materialIds: values.materials?.map(m => ({
            materialId: m.id,
            requiredQuantity: Number(m.quantity)
        })) || []
      };
      delete payload.materials; // Xóa mảng cũ đi cho gọn

      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, payload);
        message.success('Cập nhật thành công!');
      } else {
        await axios.post(API_URL, payload);
        message.success('Tạo sản phẩm mới thành công!');
      }
      handleCloseModal();
      fetchProducts();
    } catch (error) {
      console.error(error);
      message.error("Có lỗi xảy ra khi lưu!");
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`${API_URL}/${id}`);
      message.success('Đã xóa sản phẩm!');
      fetchProducts();
    } catch (error) {
      console.error(error);
      message.error("Không thể xóa sản phẩm này!");
    }
  };

  // QUẢN LÝ UI
  const handleOpenDrawer = (product) => { setViewingProduct(product); setIsDrawerOpen(true); };
  const handleCloseDrawer = () => { setIsDrawerOpen(false); setTimeout(() => setViewingProduct(null), 300); };

  const handleOpenModal = (product = null) => {
    if (product) {
      setEditingId(product.productId); 
      
      // Map dữ liệu Material từ Backend trả về ngược lại vào Form
      const formData = {
          ...product,
          materials: product.materials?.map(m => ({
              id: m.materialId,
              quantity: m.requiredQuantity,
              unit: m.unit
          })) || []
      };
      form.setFieldsValue(formData);
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

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto">
        {/* Header */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Sản phẩm</h2>
            <p className="text-slate-400 text-sm mt-1">Quản lý danh mục sản phẩm và định mức nguyên liệu (BOM)</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Tạo sản phẩm
          </button>
        </div>

        {/* THANH CÔNG CỤ: TÌM KIẾM & BỘ LỌC */}
        <div className="flex items-center gap-4 mb-6">
          <div className="flex-1 h-12 bg-[#1E293B] border border-slate-700 rounded-xl flex items-center px-4 shadow-sm focus-within:border-blue-500 transition-colors">
            <Search className="text-slate-400 mr-3" size={18} />
            <input 
              type="text" placeholder="Tìm kiếm theo tên hoặc mã..." 
              value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-transparent text-white text-sm focus:outline-none placeholder-slate-400"
            />
          </div>

          {/* Lọc Danh Mục */}
          <div className="w-56 h-12 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
            <button onClick={() => { setOpenCategories(!openCategories); setOpenZones(false); }} className="w-full h-full flex items-center justify-between px-4 text-slate-300">
              <div className="flex items-center gap-2">
                 <Package size={16} className="text-blue-400" />
                 <span className="text-sm font-medium truncate">{selectedCategoryName}</span>
              </div>
              {openCategories ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openCategories && (
              <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20 max-h-60 overflow-y-auto">
                <MenuItem icon={<Package size={18} />} label="Tất cả danh mục" onClick={() => { setSelectedCategoryId(null); setSelectedCategoryName('Tất cả danh mục'); setOpenCategories(false); }} />
                {categories.map(c => (
                  <MenuItem key={c.categoryId} icon={<Package size={18} />} label={c.categoryName} onClick={() => { setSelectedCategoryId(c.categoryId); setSelectedCategoryName(c.categoryName); setOpenCategories(false); }} />
                ))}
              </div>
            )}
          </div>

          {/* Lọc Khu Vực */}
          <div className="w-56 h-12 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
            <button onClick={() => { setOpenZones(!openZones); setOpenCategories(false); }} className="w-full h-full flex items-center justify-between px-4 text-slate-300">
              <div className="flex items-center gap-2">
                 <MapPin size={16} className="text-emerald-400" />
                 <span className="text-sm font-medium truncate">{selectedZoneName}</span>
              </div>
              {openZones ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openZones && (
              <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20 max-h-60 overflow-y-auto">
                <MenuItem icon={<MapPin size={18} />} label="Tất cả khu vực" onClick={() => { setSelectedZoneId(null); setSelectedZoneName('Tất cả khu vực'); setOpenZones(false); }} />
                {zones.map(z => (
                  <MenuItem key={z.zoneId} icon={<MapPin size={18} />} label={z.zoneName} onClick={() => { setSelectedZoneId(z.zoneId); setSelectedZoneName(z.zoneName); setOpenZones(false); }} />
                ))}
              </div>
            )}
          </div>
        </div>

        {/* BẢNG & MODAL & DRAWER */}
        <ProductTable products={products} isLoading={isLoading} onEdit={handleOpenModal} onDelete={handleDelete} onRecipe={handleOpenDrawer} />
        
        <ProductModal isOpen={isModalOpen} onClose={handleCloseModal} form={form} onSubmit={handleSubmit} isEditing={!!editingId} categories={categories} zones={zones} materialsList={materialsList} />

        <Drawer
          title={
            <div className="flex items-center gap-3">
              <Settings2 className="text-blue-400" size={24} />
              <span className="text-white text-lg font-semibold tracking-wide">Chi tiết Định mức (BOM)</span>
            </div>
          }
          placement="right" width={650} onClose={handleCloseDrawer} open={isDrawerOpen}
          styles={{ header: { background: '#0F172A', borderBottom: '1px solid #1E293B' }, body: { background: '#0B1120', padding: '24px' }, mask: { backdropFilter: 'blur(4px)' } }}
          closeIcon={<span className="text-slate-400 hover:text-white transition-colors">✕</span>}
        >
          {viewingProduct && (
            <div className="flex flex-col h-full">
              {/* Box 1: Thông tin Sản phẩm */}
              <div className="bg-[#0F172A] p-5 rounded-xl border border-slate-800 flex gap-5 items-center mb-6">
                <Image src={viewingProduct.productImage} width={80} height={80} className="rounded-lg object-cover border border-slate-700" fallback="https://placehold.co/100x100/1E293B/FFF?text=No+Img" />
                <div>
                  <h3 className="text-xl font-bold text-white mb-1">{viewingProduct.productName}</h3>
                  <div className="flex gap-4 text-sm text-slate-400">
                    <p>Mã SP: <span className="text-blue-400 font-medium">{viewingProduct.productId}</span></p>
                    <p>Danh mục: <span className="text-slate-300">{viewingProduct.categoryName || '---'}</span></p>
                  </div>
                </div>
              </div>

              <Divider className="border-slate-800 m-0 mb-6" />
              
              {/* Box 2: Bảng Nguyên Liệu Cấu Thành */}
              <div className="flex-1">
                <div className="flex justify-between items-center mb-4">
                  <h4 className="text-lg font-semibold text-white">Công thức Nguyên liệu</h4>
                  <button onClick={() => { handleCloseDrawer(); handleOpenModal(viewingProduct); }} className="text-sm text-blue-400 hover:text-blue-300 font-medium flex items-center gap-1">
                     <Edit size={14}/> Sửa định mức
                  </button>
                </div>
                
                <Table 
                  columns={materialColumns} 
                  dataSource={viewingProduct.materials || []} 
                  rowKey="materialId"
                  pagination={false} 
                  size="small"
                  className="custom-dark-table border border-slate-800 rounded-xl overflow-hidden"
                  locale={{ emptyText: <span className="text-slate-500">Sản phẩm này chưa có công thức</span> }}
                />
              </div>
            </div>
          )}
        </Drawer>
      </div>
    </ConfigProvider>
  );
}