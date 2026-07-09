import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Select, Tag, Space, Popconfirm, ConfigProvider, theme, message, DatePicker, Drawer, Divider, InputNumber, Input } from 'antd';
import { Plus, Edit, Trash2, Search, Calendar, Eye, FileText, Settings2, CheckCircle, Clock, Factory, ChevronDown, ChevronRight, BadgeX } from 'lucide-react';
import axios from 'axios';
import dayjs from 'dayjs';

// ============================================================================
// CẤU HÌNH API
// ============================================================================
const API_URL = 'http://localhost:8080/api/outbound-material'; 
const API_SUPPLIER_URL = 'http://localhost:8080/api/supplier'; 
const API_MATERIAL_URL = 'http://localhost:8080/api/material'; 
const API_ZONE_URL = 'http://localhost:8080/api/zone'; 

const { RangePicker } = DatePicker;

// Component Menu Item dùng chung cho Dropdown
const MenuItem = ({ icon, label, onClick }) => (
  <button onClick={onClick} className="w-full flex items-center gap-3 px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-lg transition-all">
    {icon}
    <span className="text-sm font-medium">{label}</span>
  </button>
);

// ============================================================================
// 1. COMPONENT BẢNG PHIẾU XUẤT
// ============================================================================
const OutboundTable = ({ outboundReceipts, isLoading, onEdit, onDelete, onView, activeTab , pagination, onChange }) => {
  const columns = [
    {
      title: 'Mã phiếu xuất',
      dataIndex: 'materialIssueId', 
      key: 'materialIssueId',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Ngày xuất',
      dataIndex: 'materialIssueDate', 
      key: 'materialIssueDate',
      render: (text) => <span className="text-slate-200">{dayjs(text).format('DD/MM/YYYY')}</span>,
    },
    {
      title: 'Nơi nhận (Phân xưởng)',
      dataIndex: 'supplierName', 
      key: 'supplierName',
      render: (text) => <span className="text-slate-300 font-medium">{text}</span>,
    },
    {
      title: 'Tổng tiền',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => <span className="text-rose-400 font-medium">{(amount || 0).toLocaleString()} VND</span>,
    },
    {
      title: 'Người tạo',
      dataIndex: 'createBy',
      key: 'createBy',
      render: (text) => <span className="text-slate-300 font-medium">{text}</span>,
    },
    {
      title: 'Trạng thái', 
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        let color = 'default';
        let text = 'Chờ xuất (Nháp)';
        if (status === 1) { color = 'success'; text = 'Đã xuất kho'; }
        if (status === -1) { color = 'error'; text = 'Đã hủy'; }
        return <Tag color={color} className="bg-transparent border border-current">{text}</Tag>;
      },
    },
    {
      title: 'Hành động',
      key: 'action',
      align: 'right',
      render: (_, record) => (
        <Space size="small">
          <button onClick={() => onView(record)} className="p-2 text-slate-400 hover:text-emerald-400 hover:bg-emerald-400/10 rounded-lg transition-colors" title="Xem chi tiết">
            <Eye size={18} />
          </button>
          
          {/* Nút Sửa/Xóa chỉ hiện ở tab Nháp (status = 0) */}
          {activeTab === 0 && (
            <>
              <button onClick={() => onEdit(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
                <Edit size={18} />
              </button>
              <Popconfirm title="Hủy phiếu xuất" description={`Bạn có chắc muốn hủy phiếu "${record.materialIssueId}"?`} onConfirm={() => onDelete(record.materialIssueId)} okText="Đồng ý" cancelText="Hủy" okButtonProps={{ danger: true }}>
                <button className="p-2 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-colors">
                  <Trash2 size={18} />
                </button>
              </Popconfirm>
            </>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div className="bg-[#0F172A] border-x border-b border-slate-800 rounded-b-2xl shadow-xl text-base">
      <Table columns={columns} dataSource={outboundReceipts} loading={isLoading} rowKey="materialIssueId" pagination={pagination} onChange={onChange} className="custom-dark-table" />
    </div>
  );
}

// ============================================================================
// 2. COMPONENT MODAL (Tạo/Sửa Phiếu Xuất)
// ============================================================================
const OutboundModal = ({ isOpen, onClose, form, onSubmit, isEditing, suppliers, materials, zones }) => (
  <Modal title={<span className="text-lg">{isEditing ? 'Sửa phiếu xuất' : 'Tạo phiếu xuất mới'}</span>} open={isOpen} onCancel={onClose} footer={null} className="dark-modal" width={800}>
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      
      <Form.Item name="materialIssueId" label={<span className="text-slate-300">Mã phiếu</span>}>
        <Input placeholder="Tự động tạo..." className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled />
      </Form.Item>

      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="supplierId" label={<span className="text-slate-300">Phân xưởng / Nơi nhận</span>} rules={[{ required: true, message: 'Vui lòng chọn nơi nhận' }]}>
          <Select placeholder="Chọn nơi nhận..." className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
            {suppliers.map(s => (
              <Select.Option key={s.supplierId || s.id} value={s.supplierId || s.id}>
                {s.supplierName || s.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item name="materialIssueDate" label={<span className="text-slate-300">Ngày xuất kho</span>} rules={[{ required: true }]}>
          <DatePicker className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" format="DD/MM/YYYY" />
        </Form.Item>
      </div>

      <Divider className="border-slate-700 my-4" />
      <div className="flex items-center gap-2 mb-4">
        <Settings2 size={18} className="text-blue-400"/>
        <h4 className="text-slate-200 font-semibold">Danh sách nguyên liệu xuất</h4>
      </div>

      <Form.List name="materialIssueDetails">
        {(fields, { add, remove }) => (
          <>
            {fields.map(({ key, name, ...restField }) => (
              <div key={key} className="flex gap-3 mb-3 items-start">
                {/* Nguyên liệu cần lấy */}
                <Form.Item {...restField} name={[name, 'materialId']} rules={[{ required: true, message: 'Chọn nguyên liệu' }]} className="flex-1 mb-0">
                  <Select placeholder="Chọn nguyên liệu..." className="h-[40px] custom-dark-select" showSearch optionFilterProp="children"
                  onChange={(selectedValue) => {
                       // 1. Tìm sản phẩm vừa được chọn trong mảng products
                       const selectedMaterial = materials.find(m => m.materialId === selectedValue || m.id === selectedValue);     
                       if (selectedMaterial) {
                       // 2. Lấy toàn bộ mảng dữ liệu hiện tại đang có trên Form
                       const currentDetails = form.getFieldValue('materialIssueDetails') || [];       
                       // 3. Cập nhật lại giá (price) cho đúng cái dòng (name) hiện tại
                       currentDetails[name] = { 
                      ...currentDetails[name], 
                      price: selectedMaterial.price // Lấy giá từ object Product nhét vào ô price
                      };
                       // 4. Đẩy ngược mảng dữ liệu đã cập nhật lên lại Form
                    form.setFieldsValue({ invoiceDetails: currentDetails });
                    }
                    }}>
                    {materials.map(m => <Select.Option key={m.materialId || m.id} value={m.materialId || m.id}>{m.materialName || m.name}</Select.Option>)}
                  </Select>
                </Form.Item>
                
                {/* Khu vực LẤY hàng ra (Zone) */}
                <Form.Item {...restField} name={[name, 'zoneId']} className="w-56 mb-0">
                  <Select placeholder="Lấy từ khu vực" className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
                    {zones?.map(z => <Select.Option key={z.zoneId || z.id} value={z.zoneId || z.id}>{z.zoneName || z.name}</Select.Option>)}
                  </Select>
                </Form.Item>

                {/* Số lượng cần xuất */}
                <Form.Item {...restField} name={[name, 'quantity']} rules={[{ required: true, message: 'Nhập SL' }]} className="w-28 mb-0">
                  <InputNumber placeholder="SL" className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" min={1} />
                </Form.Item>

                {/* Đơn giá */}
                <Form.Item {...restField} name={[name, 'price']} rules={[{ required: true, message: 'Nhập giá' }]} className="w-36 mb-0">
                  <InputNumber placeholder="Đơn giá" className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" min={0} formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} />
                </Form.Item>

                <button type="button" onClick={() => remove(name)} className="h-[40px] px-3 text-slate-400 hover:text-red-400 bg-[#1E293B] hover:bg-red-400/10 rounded-lg transition-colors border border-slate-700">
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
            <Button type="dashed" onClick={() => add()} block icon={<Plus size={16}/>} className="bg-transparent border-slate-700 text-blue-400 hover:text-blue-300 hover:border-blue-400 h-[40px] mt-2">
              Thêm dòng hàng cần xuất
            </Button>
          </>
        )}
      </Form.List>

      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Tạo phiếu nháp'}
        </Button>
      </div>
    </Form>
  </Modal>
);

// ============================================================================
// 3. COMPONENT CHÍNH
// ============================================================================
export default function OutboundMaterial() {
  const [form] = Form.useForm();
  
  // State quản lý luồng dữ liệu
  const [activeTab, setActiveTab] = useState(0); 
  const [outboundReceipts, setOutboundReceipts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  
  // State Filters
  const [searchTerm, setSearchTerm] = useState('');
  const [dateRange, setDateRange] = useState(null);
  

  const [selectedSupId, setSelectedSupId] = useState(null);
  const [selectedSupName, setSelectedSupName] = useState('Tất cả phân xưởng');
  const [openSups, setOpenSups] = useState(false);

  // Data phụ trợ
  const [suppliers, setSuppliers] = useState([]); 
  const [materials, setMaterials] = useState([]);
  const [zones, setZones] = useState([]);

  // State Modal & Drawer
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [viewingReceipt, setViewingReceipt] = useState(null);

   //state quản lý phân trang
  const [pagination, setPagination] = useState({
    current: 1, // AntD đếm trang từ 1
    pageSize: 6, // Số dòng trên 1 trang
    total: 0,   // Tổng số bản ghi (Backend trả về)
  });
  

  // 1. Fetch dữ liệu phụ (Chạy 1 lần)
  useEffect(() => {
    const fetchSelectData = async () => {   
      try {
        const [supRes, mateRes, zoneRes] = await Promise.all([ 
          axios.get(API_SUPPLIER_URL), 
          axios.get(API_MATERIAL_URL), 
          axios.get(API_ZONE_URL) 
        ]);
        setSuppliers(supRes.data?.data || []);
        setMaterials(mateRes.data?.data || []);
        setZones(zoneRes.data?.data || []);
      } catch (error) { 
        console.error("Lỗi data phụ:", error); 
      }
    };
    fetchSelectData();
  }, []);

  // 2. Fetch danh sách phiếu xuất
  const fetchReceipts = async (page = 1, size = 6) => {
    try {
      setIsLoading(true);
      const params = { 
        status: activeTab, 
        search: searchTerm || null,
        supplierId: selectedSupId || null,
        page: page - 1,
        size: size
      };
      
      if (dateRange && dateRange.length === 2) {
        params.startDate = dateRange[0].format('YYYY-MM-DD');
        params.endDate = dateRange[1].format('YYYY-MM-DD');
      }

      const response = await axios.get(API_URL, { params });
      const pageData = response.data?.data ;
      setOutboundReceipts(pageData.content);
       
      setPagination({
        ...pagination,
        current: page,
        total: pageData.totalElements, 
      });

    } catch (error) {
      console.log(error);
      message.error("Lỗi khi tải danh sách phiếu xuất!");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
     fetchReceipts(); 
  }, [activeTab, searchTerm, dateRange, selectedSupId]);


   //Hàm bắt sự kiện khi user click chuyển trang trên UI
  const handleTableChange = (newPagination) => {
    fetchReceipts(newPagination.current, newPagination.pageSize);
  };

  // Lưu hoặc Cập nhật phiếu
  const handleSubmit = async (values) => {
    try {
      const currentUsername = localStorage.getItem('username');
      const payload = { 
        ...values, 
        materialIssueDate: values.materialIssueDate.format('YYYY-MM-DD'),
        status: 0, 
        createBy: currentUsername 
      };
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, payload);
        message.success('Cập nhật phiếu xuất thành công!');
      } else {
        await axios.post(API_URL, payload);
        message.success('Tạo phiếu xuất mới thành công!');
      }
      setIsModalOpen(false);
      fetchReceipts(); 
    } catch (error) { 
      message.error(error.response?.data?.message || "Lỗi khi lưu phiếu!"); 
    }
  };


  // Hủy phiếu
  const handleDelete = async (id) => {
    try {
      await axios.put(`${API_URL}/${id}/cancel`);
      message.success('Đã hủy phiếu xuất kho!');
      fetchReceipts(); 
    } catch (error) {
      console.log(error);
      message.error("Lỗi khi hủy phiếu!"); 
    }
  };

  // Duyệt phiếu xuất kho
  const handleApprove = async (id) => {
    try {
      await axios.put(`${API_URL}/${id}/approve`);
      message.success('Đã xuất kho thành công!');
      setIsDrawerOpen(false);
      fetchReceipts();
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi xuất kho! Vui lòng kiểm tra tồn kho.");
    }
  };

  // Mở Form sửa/thêm
  const handleOpenModal = (receipt = null) => {
    if (receipt) {
      setEditingId(receipt.materialIssueId); 
      form.setFieldsValue({ 
        ...receipt, 
        materialIssueDate: dayjs(receipt.materialIssueDate), 
        supplierId: receipt.supplier?.supplierId // Map đúng vào supplierId
      }); 
    } else {
      setEditingId(null);
      form.resetFields();
      form.setFieldsValue({ materialIssueDate: dayjs() });
    }
    setIsModalOpen(true);
  };

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER */}
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý xuất kho nguyên liệu</h2>
            <p className="text-slate-400 text-sm mt-1">Tạo chứng từ và cấp phát nguyên liệu cho phân xưởng</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Tạo phiếu xuất
          </button>
        </div>

        {/* THANH TOGGLE TAB */}
        <div className="flex bg-[#1E293B] p-1 rounded-xl w-fit border border-slate-700 mb-6">
          <button onClick={() => setActiveTab(0)} className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${activeTab === 0 ? 'bg-blue-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'}`}>
            <Clock size={16} /> Chờ xuất (Nháp)
          </button>
          <button onClick={() => setActiveTab(1)} className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${activeTab === 1 ? 'bg-emerald-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'}`}>
            <CheckCircle size={16} /> Đã xuất kho
          </button>
          <button onClick={() => setActiveTab(-1)} className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${activeTab === -1 ? 'bg-rose-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'}`}>
            <BadgeX size={16} /> Đã hủy
          </button>
        </div>

        {/* THANH CÔNG CỤ FILTER */}
        <div className="flex items-center gap-4 mb-6">
          <div className="bg-[#0F172A] border border-slate-800 p-4 rounded-xl flex items-center flex-1">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
              <input type="text" placeholder="Tìm kiếm mã phiếu xuất..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} className="w-full bg-[#1E293B] border border-slate-700 text-white text-sm rounded-lg pl-10 pr-4 py-2.5 focus:outline-none focus:border-blue-500 transition-colors" />
            </div>
          </div>

          {/* Lọc theo Nơi nhận (vẫn dùng biến Supplier ở dưới) */}
          <div className="w-64 h-14 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
            <button onClick={() => setOpenSups(!openSups)} className="w-full h-full flex items-center justify-between px-4 text-slate-300">
              <div className="flex items-center gap-2">
                <Factory size={16} className="text-emerald-400" /> 
                <span className="text-sm font-medium truncate">{selectedSupName}</span>
              </div>
              {openSups ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openSups && (
              <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20 max-h-60 overflow-y-auto">
                <MenuItem icon={<Factory size={18} />} label="Tất cả phân xưởng" onClick={() => { setSelectedSupId(null); setSelectedSupName('Tất cả phân xưởng'); setOpenSups(false); }} />
                {suppliers.map(s => (
                  <MenuItem key={s.supplierId} icon={<Factory size={18} />} label={s.supplierName} onClick={() => { setSelectedSupId(s.supplierId); setSelectedSupName(s.supplierName); setOpenSups(false); }} />
                ))}
              </div>
            )}
          </div>

          <div className="bg-[#1E293B] border border-slate-700 rounded-xl px-2 h-14 flex items-center shadow-sm w-72">
             <Calendar size={18} className="text-blue-400 mx-2" />
             <RangePicker className="bg-transparent border-none shadow-none hover:bg-transparent focus:bg-transparent custom-dark-rangepicker w-full" format="DD/MM/YYYY" placeholder={['Từ ngày', 'Đến ngày']} onChange={(dates) => setDateRange(dates)} />
          </div>
        </div>

        {/* BẢNG DỮ LIỆU */}
        <OutboundTable 
        outboundReceipts={outboundReceipts}
         isLoading={isLoading} 
         activeTab={activeTab} 
         onEdit={handleOpenModal} 
         onDelete={handleDelete} 
         pagination={pagination}
          onChange={handleTableChange}
         onView={(record) => { setViewingReceipt(record); setIsDrawerOpen(true);

        }} />
        
        {/* MODAL */}
        <OutboundModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} form={form} onSubmit={handleSubmit} isEditing={!!editingId} suppliers={suppliers} materials={materials} zones={zones} />

        {/* DRAWER CHI TIẾT */}
        <Drawer
          title={<div className="flex items-center gap-3"><FileText className="text-blue-400" size={24} /><span className="text-white text-lg font-semibold tracking-wide">Chi tiết Phiếu Xuất</span></div>}
          placement="right" width={750} onClose={() => setIsDrawerOpen(false)} open={isDrawerOpen}
          styles={{ header: { background: '#0F172A', borderBottom: '1px solid #1E293B' }, body: { background: '#0B1120', padding: '24px' } }}
          closeIcon={<span className="text-slate-400 hover:text-white">✕</span>}
        >
          {viewingReceipt && (
            <div className="flex flex-col h-full">
              <div className="bg-[#0F172A] p-5 rounded-xl border border-slate-800 mb-6">
                <div className="flex justify-between items-start mb-4">
                   <div>
                     <h3 className="text-xl font-bold text-white mb-1">Mã: {viewingReceipt.materialIssueId}</h3>
                     <p className="text-slate-400">Ngày xuất: {dayjs(viewingReceipt.materialIssueDate).format('DD/MM/YYYY')}</p>
                   </div>
                   <Tag color={viewingReceipt.status === 1 ? 'success' : viewingReceipt.status === -1 ? 'error' : 'default'} className="text-sm px-3 py-1">
                     {viewingReceipt.status === 1 ? 'Đã xuất' : viewingReceipt.status === -1 ? 'Đã hủy' : 'Bản nháp'}
                   </Tag>
                </div>
                <Divider className="border-slate-700 m-0 mb-4"/>
                <div className="grid grid-cols-2 gap-4 text-sm">
                   <p className="text-slate-400">Nơi nhận: <span className="text-white font-medium block">{viewingReceipt.supplier?.supplierName || '---'}</span></p>
                   <p className="text-slate-400">Tổng giá trị xuất: <span className="text-rose-400 font-bold text-base block">{(viewingReceipt.totalAmount || 0).toLocaleString()} VND</span></p>
                </div>
              </div>

              <h4 className="text-lg font-semibold text-white mb-4">Danh sách xuất kho</h4>
              <Table 
                columns={[
                  { title: 'Tên nguyên liệu', dataIndex:'materialName', key: 'name', render: text => <span className="text-slate-200">{text || '---'}</span> },
                  { title: 'Lấy từ khu vực', dataIndex:'zoneName', key: 'zone', render: text => <Tag className="bg-slate-800 border-slate-600 text-slate-300">{text || 'Chưa chọn'}</Tag> },
                  { title: 'SL xuất', dataIndex: 'quantity', key: 'qty', render: val => <span className="text-rose-400 font-medium">{val}</span> },
                  { title: 'Đơn giá', dataIndex: 'price', key: 'price', render: val => <span className="text-slate-400">{(val || 0).toLocaleString()}</span> },
                  { title: 'Thành tiền', key: 'total', render: (_, record) => <span className="text-white font-medium">{(record.quantity * record.price).toLocaleString()}</span> },
                ]} 
                dataSource={viewingReceipt.materialIssueDetails || []} 
                rowKey={(record, index) => record.id || index} pagination={false} size="small" className="custom-dark-table border border-slate-800 rounded-xl overflow-hidden"
              />

              {viewingReceipt.status === 0 && (
                <div className="mt-auto pt-6">
                   <Button onClick={() => handleApprove(viewingReceipt.materialIssueId)} type="primary" block className="bg-blue-600 hover:bg-blue-500 border-none h-[45px] text-base font-medium">
                     Xác nhận xuất kho
                   </Button>
                </div>
              )}
            </div>
          )}
        </Drawer>
      </div>
    </ConfigProvider>
  );
}