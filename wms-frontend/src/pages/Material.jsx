import { useState } from 'react';
import { Table, Button, Modal, Form, Input, Tag, Space, Popconfirm, ConfigProvider, theme  } from 'antd';
import { Plus, Edit, Trash2, Search ,MapPin , ChevronRight , ChevronDown} from 'lucide-react';

const MenuItem = ({ icon, label, onClick }) => (
  <button 
    onClick={onClick}
    className="w-full flex items-center gap-3 px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-lg transition-all"
  >
    {icon}
    <span className="text-sm font-medium">{label}</span>
  </button>
);

export default function Material() {
  const [form] = Form.useForm();
  
  // State quản lý dữ liệu (Sau này thay bằng data lấy từ API axios.get)
  const [materials, setMaterials] = useState([
    { id: '1', code: 'NL01', name: 'Màn hình 15.6 inch 144Hz', unit: 'cái', price: 3500000 , quantity: 100, zoneName: 'Zone A', image: 'https://placehold.co/100x100/1E293B/FFF?text=N5'},
    { id: '2', code: 'NL02', name: 'RAM 16GB DDR4 3200MHz', unit: 'cái', price: 1500000 , quantity: 200, zoneName: 'Zone B', image: 'https://placehold.co/100x100/1E293B/FFF?text=N5'},
    { id: '3', code: 'NL03', name: 'Ổ cứng SSD 1TB NVMe', unit: 'cái', price: 2500000 , quantity: 150, zoneName: 'Zone C', image: 'https://placehold.co/100x100/1E293B/FFF?text=N5'},
  ]);

  // State quản lý Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null); // Lưu ID của danh mục đang sửa (nếu có)
   const [openZones, setOpenZones] = useState(false);
    const [selectedFilter, setSelectedFilter] = useState({zone: 'All Zones' });

  // 1. CẤU HÌNH CỘT CHO BẢNG ANT DESIGN
  const columns = [
    {
      title: 'Mã nguyên liệu',
      dataIndex: 'code',
      key: 'code',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Tên nguyên liệu',
      dataIndex: 'name',
      key: 'name',
      render: (text) => <span className="text-slate-200 font-medium">{text}</span>,
    },
    {
      title: 'Đơn vị',
      dataIndex: 'unit',
      key: 'unit',
      render: (text) => <span className="text-slate-400">{text}</span>,
    },
    {
      title: 'Giá',
      dataIndex: 'price',
      key: 'price',
      render: (price) => <span className="text-slate-400">{price.toLocaleString()} VND</span>,
    },
    {
      title: 'Tồn kho',
      dataIndex: 'quantity',
      key: 'quantity',
      render: (quantity) => <span className={`font-medium ${quantity > 0 ? 'text-emerald-400' : 'text-red-400'}`}>{quantity}</span>,
    },
    {
      title: 'Khu vực',
      dataIndex: 'zoneName',
      key: 'zoneName',
      render: (text) => <Tag className="bg-slate-800 border-slate-600 text-slate-300">{text}</Tag>,
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
      const updatedList = materials.map(mat => 
        mat.id === editingId ? { ...mat, ...values } : mat
      );
      setMaterials(updatedList);
    } else {
      // Logic Thêm mới (Gọi API: axios.post)
      const newMaterial = {
        id: Date.now().toString(), // Fake ID
        ...values
      };
      setMaterials([...materials, newMaterial]);
    }
    handleCloseModal();
  };

  const handleDelete = (id) => {
    // Logic Xóa (Gọi API: axios.delete)
    const filteredList = materials.filter(mat => mat.id !== id);
    setMaterials(filteredList);
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
            <h2 className="text-2xl font-bold text-white tracking-wide">Material Management</h2>
            <p className="text-slate-400 text-base mt-1">Manage all warehouse materials</p>
          </div>
          <button 
            onClick={() => handleOpenModal()}
            className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium"
          >
            <Plus size={18} />
            Create Material
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
          {/* Lọc Khu Vực (Zone)*/}
          <div className="w-56 h-11 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
              <button
                onClick={() => { setOpenZones(!openZones); }}
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
                  <MenuItem icon={<MapPin size={18} />} label="All Zones" onClick={() => { setSelectedFilter({...selectedFilter, zone: 'All Zones'}); setOpenZones(false); }} />
                  <MenuItem icon={<MapPin size={18} />} label="Zone A - Tech" onClick={() => { setSelectedFilter({...selectedFilter, zone: 'Zone A - Tech'}); setOpenZones(false); }} />
                  <MenuItem icon={<MapPin size={18} />} label="Zone B - Clothes" onClick={() => { setSelectedFilter({...selectedFilter, zone: 'Zone B - Clothes'}); setOpenZones(false); }} />
                </div>
              )}
          </div>
        </div>


        {/* BẢNG DỮ LIỆU */}
        <div className="bg-[#0F172A] border-x border-b border-slate-800 rounded-b-2xl overflow-hidden shadow-xl text-base">
          <Table 
            columns={columns} 
            dataSource={materials} 
            rowKey="id"
            pagination={{ pageSize: 5 }} // Phân 5 dòng 1 trang
            className="custom-dark-table"
          />
        </div>

        {/* MODAL THÊM / SỬA */}
        <Modal
          title={<span className="text-lg">{editingId ? 'Edit Material' : 'Create New Material'}</span>}
          open={isModalOpen}
          onCancel={handleCloseModal}
          footer={null} // Ẩn footer mặc định để tự custom nút
          className="dark-modal"
        >
          <Form form={form} layout="vertical" onFinish={handleSubmit} className="mt-6">

             <Form.Item name="code" label={<span className="text-slate-300">Mã sản phẩm (MaH)</span>} rules={[{ required: true }]}>
                 <Input placeholder="VD: H001" className="bg-[#1E293B] border-slate-700 text-white py-2" disabled={editingId !== null} />
               </Form.Item>

            <Form.Item 
              name="name" 
              label={<span className="text-slate-300">Material Name</span>}
              rules={[{ required: true, message: 'Vui lòng nhập tên vật tư!' }]}
            >
              <Input placeholder="e.g., Electronics" className="bg-[#1E293B] border-slate-700 text-white py-2" />
            </Form.Item>


            <Form.Item name="price" label={<span className="text-slate-300">Giá bán (Gia)</span>} rules={[{ required: true }]}>
                 <Input type="number" placeholder="15000000" className="bg-[#1E293B] border-slate-700 text-white py-2" />
             </Form.Item>

            <Form.Item name="quantity" label={<span className="text-slate-300">Số lượng (SoLg)</span>} rules={[{ required: true }]}>
                 <Input type="number" placeholder="10" className="bg-[#1E293B] border-slate-700 text-white py-2" />
            </Form.Item>
        

            <div className="flex justify-end gap-3 mt-8">
              <Button onClick={handleCloseModal} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">
                Cancel
              </Button>
              <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500">
                {editingId ? 'Save Changes' : 'Create Material'}
              </Button>
            </div>
          </Form>
        </Modal>

      </div>
    </ConfigProvider>
  );
}