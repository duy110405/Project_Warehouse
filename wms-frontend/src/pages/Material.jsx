import { useState, useEffect } from 'react';
// ĐÃ FIX: Import thêm Image
import { Table, Button, Modal, Form, Input, Tag, Space, Popconfirm, ConfigProvider, theme, message, Image ,Select} from 'antd';
import { Plus, Edit, Trash2, Search, MapPin, ChevronRight, ChevronDown} from 'lucide-react';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/material'; 
const API_ZONE_URL = 'http://localhost:8080/api/zone';


// 1. COMPONENT BẢNG DỮ LIỆU
const MaterialTable = ({ materials, isLoading, onEdit, onDelete }) => {
  const columns = [
    {
      title: 'Ảnh',
      dataIndex: 'materialImage', // ĐÃ FIX: Khớp với API Backend
      key: 'materialImage',
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
      title: 'Mã nguyên liệu',
      dataIndex: 'materialId', 
      key: 'materialId',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Tên nguyên liệu',
      dataIndex: 'materialName', 
      key: 'materialName',
      render: (text) => <span className="text-slate-200 font-medium">{text}</span>,
    },
    {
      title: 'Đơn vị',
      dataIndex: 'unit',
      key: 'unit',
      render: (text) => <span className="text-slate-400">{text || '---'}</span>,
    },
    {
      title: 'Giá nhập',
      dataIndex: 'price',
      key: 'price',
      render: (price) => <span className="text-slate-400">{(price || 0).toLocaleString()} VND</span>,
    },
    {
      title: 'Tồn kho',
      dataIndex: 'quantity',
      key: 'quantity',
      render: (quantity) => {
        const qty = quantity || 0;
        return <span className={`font-medium ${qty > 0 ? 'text-emerald-400' : 'text-red-400'}`}>{qty}</span>;
      },
    },
    {
      title: 'Khu vực',
      dataIndex: 'zoneName', 
      key: 'zoneName',
      render: (text) => <Tag className="bg-slate-800 border-slate-600 text-slate-300">{text || 'Chưa xếp'}</Tag>,
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
            title="Xóa nguyên liệu"
            description={`Bạn có chắc muốn xóa "${record.materialName}"?`}
            onConfirm={() => onDelete(record.materialId)}
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
        dataSource={materials} 
        loading={isLoading}
        rowKey="materialId" 
        pagination={{ pageSize: 5 }} 
        className="custom-dark-table"
      />
    </div>
  );
}

// ==========================================
// 2. COMPONENT MODAL THÊM / SỬA
// ==========================================
const MaterialModal = ({ isOpen, onClose, form, onSubmit, isEditing , zones }) => (
  <Modal
    title={<span className="text-lg">{isEditing ? 'Sửa vật tư' : 'Thêm vật tư mới'}</span>}
    open={isOpen}
    onCancel={onClose}
    footer={null} 
    className="dark-modal"
  >
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      {isEditing && (
        <Form.Item name="materialId" label={<span className="text-slate-300">Mã nguyên liệu</span>}>
          <Input className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled />
        </Form.Item>
      )}

      <Form.Item 
        name="materialName" 
        label={<span className="text-slate-300">Tên nguyên liệu</span>}
        rules={[{ required: true, message: 'Vui lòng nhập tên vật tư!' }]}
      >
        <Input placeholder="VD: RAM 16GB DDR4" className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="price" label={<span className="text-slate-300">Giá nhập</span>} rules={[{ required: true }]}>
          <Input type="number" placeholder="15000000" className="bg-[#1E293B] border-slate-700 text-white py-2" />
        </Form.Item>

        <Form.Item name="unit" label={<span className="text-slate-300">Đơn vị tính</span>} rules={[{ required: true }]}>
          <Input placeholder="VD: Cái, Hộp, Kg..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
        </Form.Item>
      </div>

      <Form.Item 
        name="zoneId" 
        label={<span className="text-slate-300">Khu vực lưu trữ (Zone)</span>} 
        rules={[{ required: true, message: 'Vui lòng chọn khu vực!' }]}
      >
        <Select placeholder="Chọn khu vực..." className="h-[40px] custom-dark-select">
          {zones?.map(z => (
            <Select.Option key={z.zoneId} value={z.zoneId}>{z.zoneName}</Select.Option>
          ))}
        </Select>
      </Form.Item>
      
      {/* ĐÃ BỎ TRƯỜNG NHẬP SỐ LƯỢNG (QUANTITY) ĐỂ BE TỰ ĐỘNG LÀ 0 */}

      <Form.Item name="materialImage" label={<span className="text-slate-300">Link ảnh</span>}>
        <Input placeholder="https://domain.com/image.jpg" className="bg-[#1E293B] border-slate-700 text-white py-2" />
      </Form.Item>

      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">
          Hủy
        </Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Tạo vật tư'}
        </Button>
      </div>
    </Form>
  </Modal>
);

// Component MenuItem dùng chung
const MenuItem = ({ icon, label, onClick }) => (
  <button onClick={onClick} className="w-full flex items-center gap-3 px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-lg transition-all">
    {icon}
    <span className="text-sm font-medium">{label}</span>
  </button>
);

// ==========================================
// 3. COMPONENT CHÍNH (Material)
// ==========================================
export default function Material() {
  const [form] = Form.useForm();
  
  const [materials, setMaterials] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);

  const [zones, setZones] = useState([]); 
  const [selectedZoneId, setSelectedZoneId] = useState(null); 
  const [selectedZoneName, setSelectedZoneName] = useState('Tất cả khu vực');
  const [openZones, setOpenZones] = useState(false);

  // Lấy danh sách Zone
  const fetchZones = async () => {
    try {
      const response = await axios.get(API_ZONE_URL);
      setZones(response.data.data || []);
    } catch (error) {
      console.error("Lỗi lấy danh sách Zone:", error);
    }
  };

  // Lấy dữ liệu nguyên liệu
  const fetchMaterials = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL, {
        params: { search: searchTerm || null, zoneId: selectedZoneId || null }
      });
      setMaterials(response.data.data || []);
    } catch (error) {
      message.error("Lỗi khi tải dữ liệu nguyên liệu!",error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => { 
         // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchZones(); }, []);
  useEffect(() => {
         // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchMaterials(); }, [searchTerm, selectedZoneId]);

  // Lưu dữ liệu 
  const handleSubmit = async (values) => {
    try {
      // Ép kiểu giá về số trước khi gửi
      const payload = { ...values, price: Number(values.price) };
      
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, payload);
        message.success('Cập nhật nguyên liệu thành công!');
      } else {
        // Ảnh mặc định
        payload.materialImage = payload.materialImage || 'https://placehold.co/100x100/1E293B/FFF?text=New';
        await axios.post(API_URL, payload);
        message.success('Tạo nguyên liệu mới thành công!');
      }
      handleCloseModal();
      fetchMaterials(); 
    } catch (error) {
      console.error("Lỗi khi lưu:", error);
      message.error("Có lỗi xảy ra khi lưu dữ liệu!");
    }
  };

  // Xóa dữ liệu 
  const handleDelete = async (id) => {
    try {
       await axios.delete(`${API_URL}/${id}`);
       message.success('Đã xóa nguyên liệu này!');
       fetchMaterials(); 
    } catch (error) {
       message.error("Không thể xóa nguyên liệu này!",error);
    }
  };

  // Quản lý Modal
  const handleOpenModal = (material = null) => {
    if (material) {
      setEditingId(material.materialId); 
      form.setFieldsValue(material);
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
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý nguyên liệu</h2>
            <p className="text-slate-400 text-base mt-1">Quản lý toàn bộ nguyên liệu kho</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Thêm nguyên liệu
          </button>
        </div>

        {/* THANH CÔNG CỤ */}
        <div className="flex items-center gap-4 mb-6">
          <div className=" border-slate-800 flex flex-1 items-center">
            <div className="relative w-96">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
              <input 
                type="text" 
                placeholder="Tìm kiếm theo tên hoặc mã..." 
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-[#1E293B] border border-slate-700 text-white text-sm rounded-lg pl-10 pr-4 py-2.5 focus:outline-none focus:border-blue-500 transition-colors"
              />
            </div>
          </div>

          <div className="w-64 h-11 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
            <button onClick={() => setOpenZones(!openZones)} className="w-full h-full flex items-center justify-between px-4 text-slate-300">
              <div className="flex items-center gap-2">
                <MapPin size={16} className="text-emerald-400" />
                <span className="text-sm font-medium truncate">{selectedZoneName}</span>
              </div>
              {openZones ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>

            {openZones && (
              <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20 max-h-60 overflow-y-auto">
                <MenuItem icon={<MapPin size={18} />} label="Tất cả khu vực" onClick={() => { setSelectedZoneId(null); setSelectedZoneName('Tất cả khu vực'); setOpenZones(false); }} />
                {zones.map((z) => (
                  <MenuItem key={z.zoneId} icon={<MapPin size={18} />} label={z.zoneName} onClick={() => { setSelectedZoneId(z.zoneId); setSelectedZoneName(z.zoneName); setOpenZones(false); }} />
                ))}
              </div>
            )}
          </div>
        </div>

        {/* BẢNG DỮ LIỆU */}
        <MaterialTable materials={materials} isLoading={isLoading} onEdit={handleOpenModal} onDelete={handleDelete} zones={zones} />
 
        {/* MODAL */}
        <MaterialModal isOpen={isModalOpen} onClose={handleCloseModal} form={form} onSubmit={handleSubmit} isEditing={!!editingId} zones={zones} />
      </div>
    </ConfigProvider>
  );
}