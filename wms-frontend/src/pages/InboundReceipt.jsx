import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Select, Tag, Space, Popconfirm, ConfigProvider, theme, message, DatePicker, Drawer, Divider, InputNumber, Input } from 'antd';
import { Plus, Edit, Trash2, Search, Calendar, Eye, FileText, Settings2, CheckCircle, Clock, Truck, ChevronDown, ChevronRight } from 'lucide-react';
import axios from 'axios';
import dayjs from 'dayjs';

const API_URL = 'http://localhost:8080/api/inbound'; 
const API_SUPPLIER_URL = 'http://localhost:8080/api/supplier';
const API_PRODUCT_URL = 'http://localhost:8080/api/product'; 
const API_ZONE_URL =  'http://localhost:8080/api/zone';

const { RangePicker } = DatePicker;

// Component Menu Item dùng chung cho Dropdown lọc Xưởng
const MenuItem = ({ icon, label, onClick }) => (
  <button onClick={onClick} className="w-full flex items-center gap-3 px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-lg transition-all">
    {icon}
    <span className="text-sm font-medium">{label}</span>
  </button>
);


// ============================================================================
// 1. COMPONENT BẢNG PHIẾU NHẬP
// ============================================================================
const ReceiptTable = ({ receipts, isLoading, onEdit, onDelete, onView, activeTab }) => {
  const columns = [
    {
      title: 'Mã phiếu',
      dataIndex: 'receiptId',
      key: 'receiptId',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Ngày nhập',
      dataIndex: 'receiptDate',
      key: 'receiptDate',
      render: (text) => <span className="text-slate-200">{dayjs(text).format('DD/MM/YYYY')}</span>,
    },
    {
      title: 'Xưởng cung cấp',
      dataIndex:'supplierName',
      key: 'supplierName',
      render: (text) => <span className="text-slate-300 font-medium">{text}</span>,
    },
    {
      title: 'Tổng tiền',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => <span className="text-emerald-400 font-medium">{(amount || 0).toLocaleString()} VND</span>,
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
        let text = 'Chưa duyệt';
        if (status === 1) { color = 'success'; text = 'Đã duyệt'; }
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
          
          {/* Chỉ hiển thị nút Sửa/Xóa khi ở Tab Chưa duyệt (status = 0) */}
          {activeTab === 0 && (
            <>
              <button onClick={() => onEdit(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
                <Edit size={18} />
              </button>
              <Popconfirm title="Hủy phiếu nhập" description={`Bạn có chắc muốn hủy phiếu "${record.receiptId}"?`} onConfirm={() => onDelete(record.receiptId)} okText="Đồng ý" cancelText="Hủy" okButtonProps={{ danger: true }}>
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
    <div className="bg-[#0F172A] border-x border-b border-slate-800 rounded-b-2xl overflow-hidden shadow-xl text-base">
      <Table columns={columns} dataSource={receipts} loading={isLoading} rowKey="receiptId" pagination={{ pageSize: 6 }} className="custom-dark-table" />
    </div>
  );
}

// ============================================================================
// 2. COMPONENT MODAL (Tạo/Sửa Phiếu Nhập)
// ============================================================================
const ReceiptModal = ({ isOpen, onClose, form, onSubmit, isEditing, suppliers, products ,zones }) => (
  <Modal title={<span className="text-lg">{isEditing ? 'Sửa phiếu nhập' : 'Tạo phiếu nhập mới'}</span>} open={isOpen} onCancel={onClose} footer={null} className="dark-modal" width={800}>
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      
        <Form.Item name="receiptId" label={<span className="text-slate-300">Mã phiếu</span>}>
          <Input placeholder="Tự động tạo..." className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled />
        </Form.Item>

      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="supplierId" label={<span className="text-slate-300">Nhà cung cấp / Xưởng</span>} rules={[{ required: true, message: 'Vui lòng chọn NCC' }]}>
          <Select placeholder="Chọn nhà cung cấp" className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
            {suppliers.map(s => <Select.Option key={s.supplierId || s.id} value={s.supplierId || s.id}>{s.supplierName || s.name}</Select.Option>)}
          </Select>
        </Form.Item>

        <Form.Item name="receiptDate" label={<span className="text-slate-300">Ngày nhập</span>} rules={[{ required: true }]}>
          <DatePicker className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" format="DD/MM/YYYY" />
        </Form.Item>
      </div>

      <Divider className="border-slate-700 my-4" />
      <div className="flex items-center gap-2 mb-4">
         <Settings2 size={18} className="text-blue-400"/>
         <h4 className="text-slate-200 font-semibold">Danh sách hàng nhập</h4>
      </div>

      <Form.List name="receiptDetails">
        {(fields, { add, remove }) => (
          <>
            {fields.map(({ key, name, ...restField }) => (
              <div key={key} className="flex gap-3 mb-3 items-start">
                <Form.Item {...restField} name={[name, 'productId']} rules={[{ required: true, message: 'Chọn hàng' }]} className="flex-1 mb-0">
                  <Select placeholder="Chọn mặt hàng..." className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
                    {products.map(p => <Select.Option key={p.productId || p.id} value={p.productId || p.id}>{p.productName || p.name}</Select.Option>)}
                  </Select>
                </Form.Item>
                <Form.Item {...restField} name={[name, 'zoneId']} className="w-56 mb-0">
                  <Select placeholder="Khu vực cất" className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
                    {zones?.map(z => <Select.Option key={z.zoneId || z.id} value={z.zoneId || z.id}>{z.zoneName || z.name}</Select.Option>)}
                  </Select>
                </Form.Item>
                <Form.Item {...restField} name={[name, 'quantity']} rules={[{ required: true, message: 'Nhập SL' }]} className="w-28 mb-0">
                  <InputNumber placeholder="SL" className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" min={1} />
                </Form.Item>
                <Form.Item {...restField} name={[name, 'price']} rules={[{ required: true, message: 'Nhập giá' }]} className="w-36 mb-0">
                  <InputNumber placeholder="Đơn giá" className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" min={0} formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} />
                </Form.Item>
                <button type="button" onClick={() => remove(name)} className="h-[40px] px-3 text-slate-400 hover:text-red-400 bg-[#1E293B] hover:bg-red-400/10 rounded-lg transition-colors border border-slate-700">
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
            <Button type="dashed" onClick={() => add()} block icon={<Plus size={16}/>} className="bg-transparent border-slate-700 text-blue-400 hover:text-blue-300 hover:border-blue-400 h-[40px] mt-2">
              Thêm dòng hàng
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
export default function InboundReceipt() {
  const [form] = Form.useForm();
  
  // State quản lý luồng dữ liệu
  const [activeTab, setActiveTab] = useState(0); // 0 = Chưa duyệt, 1 = Đã duyệt
  const [receipts, setReceipts] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  
  // State Filters
  const [searchTerm, setSearchTerm] = useState('');
  const [dateRange, setDateRange] = useState(null);
  const [selectedSupplierId, setSelectedSupplierId] = useState(null);
  const [selectedSupplierName, setSelectedSupplierName] = useState('Tất cả Xưởng (NCC)');
  const [openSuppliers, setOpenSuppliers] = useState(false);


  // Data phụ trợ
  const [suppliers, setSuppliers] = useState([]);
  const [products, setProducts] = useState([]);
 const [zones, setZones] = useState([]);

  // State Modal & Drawer
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [viewingReceipt, setViewingReceipt] = useState(null);

  // 1. Fetch dữ liệu phụ (Chạy 1 lần)
  useEffect(() => {
    const fetchSelectData = async () => {
      try {
        const [supRes, prodRes , zoneRes] = await Promise.all([ axios.get(API_SUPPLIER_URL), axios.get(API_PRODUCT_URL) , axios.get(API_ZONE_URL) ]);
        setSuppliers(supRes.data?.data || []);
        setProducts(prodRes.data?.data || []);
        setZones(zoneRes.data?.data || []);
      } catch (error) { console.error("Lỗi data phụ:", error); }
    };
    fetchSelectData();
  }, []);

  // 2. Fetch danh sách phiếu (Cập nhật khi Tab hoặc Filter thay đổi)
  const fetchReceipts = async () => {
    try {
      setIsLoading(true);
      const params = { 
        status: activeTab, // Filter theo tab (0 hoặc 1)
        search: searchTerm || null,
        supplierId: selectedSupplierId || null
      };
      
      // Định dạng ngày nếu có chọn
      if (dateRange && dateRange.length === 2) {
        params.startDate = dateRange[0].format('YYYY-MM-DD');
        params.endDate = dateRange[1].format('YYYY-MM-DD');
      }

      const response = await axios.get(API_URL, { params });
      setReceipts(response.data?.data || []);
    } catch (error) {
      message.error("Lỗi khi tải danh sách phiếu nhập!",error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
     fetchReceipts(); }, [activeTab, searchTerm, dateRange, selectedSupplierId]);

  // Các hàm CRUD
  const handleSubmit = async (values) => {
    try {
      const currentUsername = localStorage.getItem('username')
      const payload = { 
        ...values, 
        receiptDate: values.receiptDate.format('YYYY-MM-DD'),
        status: 0 ,// Phiếu mới luôn là Nháp (0)
        createBy: currentUsername 
      };
      
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, payload);
        message.success('Cập nhật phiếu thành công!');
      } else {
        await axios.post(API_URL, payload);
        message.success('Tạo phiếu mới thành công!');
      }
      setIsModalOpen(false);
      fetchReceipts(); 
    } catch (error) { message.error("Lỗi khi lưu phiếu!" , error); }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`${API_URL}/${id}`);
      message.success('Đã hủy phiếu nhập!');
      fetchReceipts(); 
    } catch (error) { message.error("Lỗi khi hủy phiếu!",error); }
  };

  const handleApprove = async (id) => {
    try {
      // GỌi API duyệt phiếu (Phụ thuộc vào backend bạn định tuyến là gì, ví dụ: /api/inbounds/{id}/approve)
      await axios.put(`${API_URL}/${id}/approve`);
      message.success('Duyệt phiếu nhập kho thành công!');
      setIsDrawerOpen(false);
      fetchReceipts();
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi duyệt phiếu!");
    }
  };

  // Mở Form sửa
  const handleOpenModal = (receipt = null) => {
    if (receipt) {
      setEditingId(receipt.receiptId);
      // Backend trả về `supplier` là object, cần map lại để select hiện đúng ID
      form.setFieldsValue({ 
        ...receipt, 
        receiptDate: dayjs(receipt.receiptDate),
        supplierId: receipt.supplier?.supplierId 
      }); 
    } else {
      setEditingId(null);
      form.resetFields();
      form.setFieldsValue({ receiptDate: dayjs() });
    }
    setIsModalOpen(true);
  };

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER */}
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý nhập kho</h2>
            <p className="text-slate-400 text-sm mt-1">Xử lý chứng từ và duyệt phiếu nhập nguyên liệu/sản phẩm</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Tạo phiếu nhập
          </button>
        </div>

        {/* THANH TOGGLE TAB (CHƯA DUYỆT - ĐÃ DUYỆT) */}
        <div className="flex bg-[#1E293B] p-1 rounded-xl w-fit border border-slate-700 mb-6">
          <button 
            onClick={() => setActiveTab(0)}
            className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${
              activeTab === 0 ? 'bg-blue-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            <Clock size={16} /> Chờ duyệt (Nháp)
          </button>
          <button 
            onClick={() => setActiveTab(1)}
            className={`flex items-center gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${
              activeTab === 1 ? 'bg-emerald-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            <CheckCircle size={16} /> Lịch sử (Đã duyệt)
          </button>
        </div>

        {/* THANH CÔNG CỤ (Search, Dropdown Xưởng, Date Filter) */}
        <div className="flex items-center gap-4 mb-6">
          {/* Ô Tìm kiếm */}
          <div className="bg-[#0F172A] border border-slate-800 p-4 rounded-xl flex items-center flex-1">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
              <input 
                type="text" placeholder="Tìm kiếm theo mã phiếu..." 
                value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-[#1E293B] border border-slate-700 text-white text-sm rounded-lg pl-10 pr-4 py-2.5 focus:outline-none focus:border-blue-500 transition-colors"
              />
            </div>
          </div>

          {/* Lọc theo Xưởng (Nhà cung cấp) */}
          <div className="w-64 h-14 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
            <button onClick={() => setOpenSuppliers(!openSuppliers)} className="w-full h-full flex items-center justify-between px-4 text-slate-300">
              <div className="flex items-center gap-2">
                <Truck size={16} className="text-emerald-400" />
                <span className="text-sm font-medium truncate">{selectedSupplierName}</span>
              </div>
              {openSuppliers ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openSuppliers && (
              <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20 max-h-60 overflow-y-auto">
                <MenuItem icon={<Truck size={18} />} label="Tất cả Xưởng (NCC)" onClick={() => { setSelectedSupplierId(null); setSelectedSupplierName('Tất cả Xưởng (NCC)'); setOpenSuppliers(false); }} />
                {suppliers.map(s => (
                  <MenuItem key={s.supplierId} icon={<Truck size={18} />} label={s.supplierName} onClick={() => { setSelectedSupplierId(s.supplierId); setSelectedSupplierName(s.supplierName); setOpenSuppliers(false); }} />
                ))}
              </div>
            )}
          </div>

          {/* Lọc Ngày Tháng */}
          <div className="bg-[#1E293B] border border-slate-700 rounded-xl px-2 h-14 flex items-center shadow-sm w-72">
             <Calendar size={18} className="text-blue-400 mx-2" />
             <RangePicker 
               className="bg-transparent border-none shadow-none hover:bg-transparent focus:bg-transparent custom-dark-rangepicker w-full" 
               format="DD/MM/YYYY"
               placeholder={['Từ ngày', 'Đến ngày']}
               onChange={(dates) => setDateRange(dates)}
             />
          </div>
        </div>

        {/* BẢNG DỮ LIỆU */}
        <ReceiptTable 
          receipts={receipts} 
          isLoading={isLoading} 
          activeTab={activeTab} // Truyền activeTab xuống Bảng để ẩn/hiện nút Edit
          onEdit={handleOpenModal} 
          onDelete={handleDelete} 
          onView={(record) => { setViewingReceipt(record); setIsDrawerOpen(true); }} 
        />
        
        {/* MODAL */}
        <ReceiptModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} form={form} onSubmit={handleSubmit} isEditing={!!editingId} suppliers={suppliers} products={products} zones={zones} />

        {/* DRAWER CHI TIẾT PHIẾU NHẬP */}
        <Drawer
          title={<div className="flex items-center gap-3"><FileText className="text-blue-400" size={24} /><span className="text-white text-lg font-semibold tracking-wide">Chi tiết Phiếu Nhập</span></div>}
          placement="right" width={750} onClose={() => setIsDrawerOpen(false)} open={isDrawerOpen}
          styles={{ header: { background: '#0F172A', borderBottom: '1px solid #1E293B' }, body: { background: '#0B1120', padding: '24px' } }}
          closeIcon={<span className="text-slate-400 hover:text-white">✕</span>}
        >
          {viewingReceipt && (
            <div className="flex flex-col h-full">
              <div className="bg-[#0F172A] p-5 rounded-xl border border-slate-800 mb-6">
                <div className="flex justify-between items-start mb-4">
                   <div>
                     <h3 className="text-xl font-bold text-white mb-1">Mã: {viewingReceipt.receiptId}</h3>
                     <p className="text-slate-400">Ngày nhập: {dayjs(viewingReceipt.receiptDate).format('DD/MM/YYYY')}</p>
                   </div>
                   <Tag color={viewingReceipt.status === 1 ? 'success' : 'default'} className="text-sm px-3 py-1">
                     {viewingReceipt.status === 1 ? 'Đã duyệt' : 'Bản nháp'}
                   </Tag>
                </div>
                <Divider className="border-slate-700 m-0 mb-4"/>
                <div className="grid grid-cols-2 gap-4 text-sm">
                   <p className="text-slate-400">Xưởng cung cấp: <span className="text-white font-medium block">{viewingReceipt.supplier?.supplierName || '---'}</span></p>
                   <p className="text-slate-400">Tổng giá trị: <span className="text-emerald-400 font-bold text-base block">{(viewingReceipt.totalAmount || 0).toLocaleString()} VND</span></p>
                </div>
              </div>

              <h4 className="text-lg font-semibold text-white mb-4">Danh sách hàng hóa</h4>
              <Table 
                columns={[
                  { title: 'Tên hàng', dataIndex:'productName', key: 'name', render: text => <span className="text-slate-200">{text || '---'}</span> },
                  { title: 'Khu vực cất', dataIndex:'zoneName', key: 'zone', render: text => <Tag className="bg-slate-800 border-slate-600 text-slate-300">{text || 'Chưa chọn'}</Tag> },
                  { title: 'SL', dataIndex: 'quantity', key: 'qty', render: val => <span className="text-emerald-400 font-medium">{val}</span> },
                  { title: 'Đơn giá', dataIndex: 'price', key: 'price', render: val => <span className="text-slate-400">{(val || 0).toLocaleString()}</span> },
                  { title: 'Thành tiền', key: 'total', render: (_, record) => <span className="text-white font-medium">{(record.quantity * record.price).toLocaleString()}</span> },
                ]} 
                dataSource={viewingReceipt.receiptDetails || []} 
                rowKey={(record, index) => record.id || index} pagination={false} size="small" className="custom-dark-table border border-slate-800 rounded-xl overflow-hidden"
              />

              {/* Nếu phiếu đang là Nháp, hiện nút Duyệt phiếu ở đáy Drawer */}
              {viewingReceipt.status === 0 && (
                <div className="mt-auto pt-6">
                   <Button 
                     onClick={() => handleApprove(viewingReceipt.receiptId)}
                     type="primary" block className="bg-emerald-600 hover:bg-emerald-500 border-none h-[45px] text-base font-medium"
                   >
                     Duyệt phiếu nhập kho
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