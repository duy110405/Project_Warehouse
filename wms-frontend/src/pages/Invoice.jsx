import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Select, Tag, Space, Popconfirm, ConfigProvider, theme, message, DatePicker, Drawer, Divider, InputNumber, Input } from 'antd';
import { Plus, Edit, Trash2, Search, Calendar, Eye, FileText, Settings2, CheckCircle, Clock, Users, ChevronDown, ChevronRight, BadgeX, CreditCard, CheckSquare } from 'lucide-react';
import axios from 'axios';
import dayjs from 'dayjs';

// ============================================================================
// CẤU HÌNH API GIAO TIẾP VỚI BACKEND
// ============================================================================
const API_URL = 'http://localhost:8080/api/invoice'; // API Quản lý Hóa đơn
const API_CUSTOMER_URL = 'http://localhost:8080/api/customer'; // API Danh sách Khách hàng
const API_PRODUCT_URL = 'http://localhost:8080/api/product'; // API Danh sách Sản phẩm (Thành phẩm)

const { RangePicker } = DatePicker;

// Component Menu Item dùng chung cho Dropdown lọc Khách hàng
const MenuItem = ({ icon, label, onClick }) => (
  <button onClick={onClick} className="w-full flex items-center gap-3 px-4 py-2 text-slate-400 hover:text-white hover:bg-slate-800/50 rounded-lg transition-all">
    {icon}
    <span className="text-sm font-medium">{label}</span>
  </button>
);

// ============================================================================
// 1. COMPONENT BẢNG HÓA ĐƠN
// ============================================================================
const InvoiceTable = ({ invoices, isLoading, onEdit, onDelete, onView, activeTab }) => {
  const columns = [
    {
      title: 'Mã Hóa Đơn',
      dataIndex: 'invoiceId',
      key: 'invoiceId',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Ngày lập',
      dataIndex: 'invoiceDate',
      key: 'invoiceDate',
      render: (text) => <span className="text-slate-200">{dayjs(text).format('DD/MM/YYYY')}</span>,
    },
    {
      title: 'Khách hàng',
      dataIndex: 'customerName', 
      key: 'customerName',
      render: (text) => <span className="text-slate-300 font-medium">{text}</span>,
    },
    {
      title: 'Tổng tiền',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      // Màu xanh ngọc (emerald) ám chỉ tiền thu vào
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
        let text = 'Chờ thanh toán';
        if (status === 1) { color = 'processing'; text = 'Đã thanh toán (Chờ xuất kho)'; }
        if (status === 2) { color = 'success'; text = 'Hoàn thành (Đã xuất)'; }
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
          <button onClick={() => onView(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors" title="Xem chi tiết">
            <Eye size={18} />
          </button>
          
          {/* Nút Sửa/Xóa chỉ hiện khi Hóa đơn vừa tạo, chưa thanh toán (status = 0) */}
          {activeTab === 0 && (
            <>
              <button onClick={() => onEdit(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
                <Edit size={18} />
              </button>
              <Popconfirm title="Hủy hóa đơn" description={`Bạn có chắc muốn hủy HĐ "${record.invoiceId}"?`} onConfirm={() => onDelete(record.invoiceId)} okText="Đồng ý" cancelText="Hủy" okButtonProps={{ danger: true }}>
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
      <Table columns={columns} dataSource={invoices} loading={isLoading} rowKey="invoiceId" pagination={{ pageSize: 6 }} className="custom-dark-table" />
    </div>
  );
}

// ============================================================================
// 2. COMPONENT MODAL (Tạo/Sửa Hóa Đơn)
// ============================================================================
const InvoiceModal = ({ isOpen, onClose, form, onSubmit, isEditing, customers, products }) => (
  <Modal title={<span className="text-lg">{isEditing ? 'Sửa hóa đơn' : 'Tạo hóa đơn mới'}</span>} open={isOpen} onCancel={onClose} footer={null} className="dark-modal" width={800}>
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      
      <Form.Item name="invoiceId" label={<span className="text-slate-300">Mã Hóa đơn</span>}>
        <Input placeholder="Hệ thống tự động tạo..." className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled />
      </Form.Item>

      <div className="grid grid-cols-2 gap-4">
        {/* Chọn Khách hàng (Customer) thay vì Nhà cung cấp */}
        <Form.Item name="customerId" label={<span className="text-slate-300">Khách hàng</span>} rules={[{ required: true, message: 'Vui lòng chọn Khách hàng' }]}>
          <Select placeholder="Chọn khách hàng..." className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
            {customers.map(c => <Select.Option key={c.customerId || c.id} value={c.customerId || c.id}>{c.customerName || c.name}</Select.Option>)}
          </Select>
        </Form.Item>

        <Form.Item name="invoiceDate" label={<span className="text-slate-300">Ngày lập HĐ</span>} rules={[{ required: true }]}>
          <DatePicker className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" format="DD/MM/YYYY" />
        </Form.Item>
      </div>

      <Divider className="border-slate-700 my-4" />
      <div className="flex items-center gap-2 mb-4">
         <Settings2 size={18} className="text-blue-400"/>
         <h4 className="text-slate-200 font-semibold">Danh sách sản phẩm bán</h4>
      </div>

      <Form.List name="invoiceDetails">
        {(fields, { add, remove }) => (
          <>
            {fields.map(({ key, name, ...restField }) => (
              <div key={key} className="flex gap-3 mb-3 items-start">
                
                <Form.Item {...restField} name={[name, 'productId']} rules={[{ required: true, message: 'Chọn sản phẩm' }]} className="flex-1 mb-0">
                  <Select placeholder="Chọn sản phẩm..." className="h-[40px] custom-dark-select" showSearch optionFilterProp="children"
                  onChange={(selectedValue) => {
                       // 1. Tìm sản phẩm vừa được chọn trong mảng products
                       const selectedProduct = products.find(p => p.productId === selectedValue || p.id === selectedValue);
      
                       if (selectedProduct) {
                       // 2. Lấy toàn bộ mảng dữ liệu hiện tại đang có trên Form
                       const currentDetails = form.getFieldValue('invoiceDetails') || [];
        
                       // 3. Cập nhật lại giá (price) cho đúng cái dòng (name) hiện tại
                       currentDetails[name] = { 
                      ...currentDetails[name], 
                      price: selectedProduct.price // Lấy giá từ object Product nhét vào ô price
                      };
        
                       // 4. Đẩy ngược mảng dữ liệu đã cập nhật lên lại Form
                    form.setFieldsValue({ invoiceDetails: currentDetails });
                    }
                    }}>
                    {products.map(p => <Select.Option key={p.productId || p.id} value={p.productId || p.id}>{p.productName || p.name}</Select.Option>)}
                  </Select>
                </Form.Item>
                
                <Form.Item {...restField} name={[name, 'quantity']} rules={[{ required: true, message: 'Nhập SL' }]} className="w-32 mb-0">
                  <InputNumber placeholder="Số lượng" className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" min={1} />
                </Form.Item>
                
                <Form.Item {...restField} name={[name, 'price']} rules={[{ required: true, message: 'Nhập đơn giá' }]} className="w-48 mb-0">
                  <InputNumber placeholder="Đơn giá bán" className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" min={0} formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} />
                </Form.Item>
                
                <button type="button" onClick={() => remove(name)} className="h-[40px] px-3 text-slate-400 hover:text-red-400 bg-[#1E293B] hover:bg-red-400/10 rounded-lg transition-colors border border-slate-700">
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
            <Button type="dashed" onClick={() => add()} block icon={<Plus size={16}/>} className="bg-transparent border-slate-700 text-blue-400 hover:text-blue-300 hover:border-blue-400 h-[40px] mt-2">
              Thêm sản phẩm
            </Button>
          </>
        )}
      </Form.List>

      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Tạo hóa đơn'}
        </Button>
      </div>
    </Form>
  </Modal>
);

// ============================================================================
// 3. COMPONENT CHÍNH
// ============================================================================
export default function Invoice() {
  const [form] = Form.useForm();
  
  // State quản lý luồng dữ liệu
  const [activeTab, setActiveTab] = useState(0); 
  const [invoices, setInvoices] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  
  // State Filters
  const [searchTerm, setSearchTerm] = useState('');
  const [dateRange, setDateRange] = useState(null);
  const [selectedCustomerId, setSelectedCustomerId] = useState(null);
  const [selectedCustomerName, setSelectedCustomerName] = useState('Tất cả Khách hàng');
  const [openCustomers, setOpenCustomers] = useState(false);

  // Data phụ trợ
  const [customers, setCustomers] = useState([]);
  const [products, setProducts] = useState([]);

  // State Modal & Drawer
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [viewingInvoice, setViewingInvoice] = useState(null);

  // 1. Fetch dữ liệu phụ
  useEffect(() => {
    const fetchSelectData = async () => {
      try {
        const [custRes, prodRes] = await Promise.all([ 
          axios.get(API_CUSTOMER_URL).catch(() => ({ data: { data: [] } })),
          axios.get(API_PRODUCT_URL).catch(() => ({ data: { data: [] } }))
        ]);
        setCustomers(custRes.data?.data || []);
        setProducts(prodRes.data?.data || []);
      } catch (error) { 
        console.error("Lỗi data phụ:", error); 
      }
    };
    fetchSelectData();
  }, []);

  // 2. Fetch danh sách Hóa đơn
  const fetchInvoices = async () => {
    try {
      setIsLoading(true);
      const params = { 
        status: activeTab, 
        search: searchTerm || null,
        customerId: selectedCustomerId || null
      };
      
      if (dateRange && dateRange.length === 2) {
        params.startDate = dateRange[0].format('YYYY-MM-DD');
        params.endDate = dateRange[1].format('YYYY-MM-DD');
      }

      const response = await axios.get(API_URL, { params });
      setInvoices(response.data?.data || []);
    } catch (error) {
      console.log(error);
      message.error("Lỗi khi tải danh sách Hóa đơn!");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
     fetchInvoices(); 
  }, [activeTab, searchTerm, dateRange, selectedCustomerId]);

  // Lưu Hóa đơn
  const handleSubmit = async (values) => {
    try {
      const currentUsername = localStorage.getItem('username');
      const payload = { 
        ...values, 
        invoiceDate: values.invoiceDate.format('YYYY-MM-DD'),
        status: 0 , // Luôn khởi tạo là 0 (Chờ thanh toán)
        createBy: currentUsername 
      };
      
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, payload);
        message.success('Cập nhật hóa đơn thành công!');
      } else {
        await axios.post(API_URL, payload);
        message.success('Tạo hóa đơn mới thành công!');
      }
      setIsModalOpen(false);
      fetchInvoices(); 
    } catch (error) { 
      message.error(error.response?.data?.message || "Lỗi khi lưu hóa đơn!"); 
    }
  };

  // Hủy Hóa đơn
  const handleDelete = async (id) => {
    try {
      await axios.put(`${API_URL}/${id}/cancel`);
      message.success('Đã hủy hóa đơn!');
      fetchInvoices(); 
    } catch (error) {
      console.log(error);
      message.error("Lỗi khi hủy hóa đơn!"); 
    }
  };

  // XÁC NHẬN THANH TOÁN (Chuyển status -> 1, Đẩy lệnh chờ Thủ Kho làm Phiếu Xuất)
  const handleMarkAsPaid = async (id) => {
    try {
      await axios.put(`${API_URL}/${id}/approve`);
      message.success('Hóa đơn đã thanh toán! Đã chuyển yêu cầu sang Thủ kho.');
      setIsDrawerOpen(false);
      fetchInvoices();
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi xác nhận thanh toán!");
    }
  };

  // Mở Form sửa
  const handleOpenModal = (invoice = null) => {
    if (invoice) {
      setEditingId(invoice.invoiceId);
      form.setFieldsValue({ 
        ...invoice, 
        invoiceDate: dayjs(invoice.invoiceDate),
        customerId: invoice.customer?.customerId 
      }); 
    } else {
      setEditingId(null);
      form.resetFields();
      form.setFieldsValue({ invoiceDate: dayjs() });
    }
    setIsModalOpen(true);
  };

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER */}
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý Hóa Đơn Bán Hàng</h2>
            <p className="text-slate-400 text-sm mt-1">Lập hóa đơn, theo dõi thanh toán và đẩy lệnh xuất kho</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Tạo hóa đơn
          </button>
        </div>

        {/* THANH TOGGLE TAB (4 Trạng thái) */}
        <div className="flex bg-[#1E293B] p-1 rounded-xl w-fit border border-slate-700 mb-6 overflow-x-auto">
          <button onClick={() => setActiveTab(0)} className={`flex items-center whitespace-nowrap gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${activeTab === 0 ? 'bg-blue-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'}`}>
            <Clock size={16} /> Mới tạo (Chờ thanh toán)
          </button>
          <button onClick={() => setActiveTab(1)} className={`flex items-center whitespace-nowrap gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${activeTab === 1 ? 'bg-cyan-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'}`}>
            <CreditCard size={16} /> Đã thanh toán (Chờ xuất kho)
          </button>
          <button onClick={() => setActiveTab(2)} className={`flex items-center whitespace-nowrap gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${activeTab === 2 ? 'bg-emerald-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'}`}>
            <CheckCircle size={16} /> Hoàn thành (Đã xuất)
          </button>
          <button onClick={() => setActiveTab(-1)} className={`flex items-center whitespace-nowrap gap-2 px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${activeTab === -1 ? 'bg-rose-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'}`}>
            <BadgeX size={16} /> Đã hủy
          </button>
        </div>

        {/* THANH CÔNG CỤ (Search, Dropdown Customer, Date Filter) */}
        <div className="flex items-center gap-4 mb-6">
          <div className="bg-[#0F172A] border border-slate-800 p-4 rounded-xl flex items-center flex-1">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
              <input 
                type="text" placeholder="Tìm kiếm mã Hóa đơn..." 
                value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-[#1E293B] border border-slate-700 text-white text-sm rounded-lg pl-10 pr-4 py-2.5 focus:outline-none focus:border-blue-500 transition-colors"
              />
            </div>
          </div>

          <div className="w-64 h-14 bg-[#1E293B] border border-slate-700 rounded-xl relative shadow-sm hover:border-slate-500 transition-colors">
            <button onClick={() => setOpenCustomers(!openCustomers)} className="w-full h-full flex items-center justify-between px-4 text-slate-300">
              <div className="flex items-center gap-2">
                <Users size={16} className="text-emerald-400" />
                <span className="text-sm font-medium truncate">{selectedCustomerName}</span>
              </div>
              {openCustomers ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            </button>
            {openCustomers && (
              <div className="absolute top-full right-0 mt-2 w-full bg-[#1E293B] border border-slate-600 rounded-xl p-2 shadow-2xl z-20 max-h-60 overflow-y-auto">
                <MenuItem icon={<Users size={18} />} label="Tất cả Khách hàng" onClick={() => { setSelectedCustomerId(null); setSelectedCustomerName('Tất cả Khách hàng'); setOpenCustomers(false); }} />
                {customers.map(c => (
                  <MenuItem key={c.customerId} icon={<Users size={18} />} label={c.customerName} onClick={() => { setSelectedCustomerId(c.customerId); setSelectedCustomerName(c.customerName); setOpenCustomers(false); }} />
                ))}
              </div>
            )}
          </div>

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
        <InvoiceTable 
          invoices={invoices} 
          isLoading={isLoading} 
          activeTab={activeTab}
          onEdit={handleOpenModal} 
          onDelete={handleDelete} 
          onView={(record) => { setViewingInvoice(record); setIsDrawerOpen(true); }} 
        />
        
        {/* MODAL */}
        <InvoiceModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} form={form} onSubmit={handleSubmit} isEditing={!!editingId} customers={customers} products={products} />

        {/* DRAWER CHI TIẾT */}
        <Drawer
          title={<div className="flex items-center gap-3"><FileText className="text-blue-400" size={24} /><span className="text-white text-lg font-semibold tracking-wide">Chi tiết Hóa Đơn</span></div>}
          placement="right" width={750} onClose={() => setIsDrawerOpen(false)} open={isDrawerOpen}
          styles={{ header: { background: '#0F172A', borderBottom: '1px solid #1E293B' }, body: { background: '#0B1120', padding: '24px' } }}
          closeIcon={<span className="text-slate-400 hover:text-white">✕</span>}
        >
          {viewingInvoice && (
            <div className="flex flex-col h-full">
              <div className="bg-[#0F172A] p-5 rounded-xl border border-slate-800 mb-6">
                <div className="flex justify-between items-start mb-4">
                   <div>
                     <h3 className="text-xl font-bold text-white mb-1">Mã HĐ: {viewingInvoice.invoiceId}</h3>
                     <p className="text-slate-400">Ngày lập: {dayjs(viewingInvoice.invoiceDate).format('DD/MM/YYYY')}</p>
                   </div>
                   <Tag color={viewingInvoice.status === 2 ? 'success' : viewingInvoice.status === 1 ? 'processing' : viewingInvoice.status === -1 ? 'error' : 'default'} className="text-sm px-3 py-1">
                     {viewingInvoice.status === 2 ? 'Hoàn thành' : viewingInvoice.status === 1 ? 'Đã thanh toán' : viewingInvoice.status === -1 ? 'Đã hủy' : 'Chờ thanh toán'}
                   </Tag>
                </div>
                <Divider className="border-slate-700 m-0 mb-4"/>
                <div className="grid grid-cols-2 gap-4 text-sm">
                   <p className="text-slate-400">Khách hàng: <span className="text-white font-medium block">{viewingInvoice.customer?.customerName || '---'}</span></p>
                   <p className="text-slate-400">Tổng thanh toán: <span className="text-emerald-400 font-bold text-base block">{(viewingInvoice.totalAmount || 0).toLocaleString()} VND</span></p>
                </div>
              </div>

              <h4 className="text-lg font-semibold text-white mb-4">Danh sách Sản phẩm</h4>
              <Table 
                columns={[
                  { title: 'Sản phẩm', dataIndex:'productName', key: 'name', render: text => <span className="text-slate-200">{text || '---'}</span> },
                  { title: 'SL', dataIndex: 'quantity', key: 'qty', render: val => <span className="text-blue-400 font-medium">{val}</span> },
                  { title: 'Đơn giá', dataIndex: 'price', key: 'price', render: val => <span className="text-slate-400">{(val || 0).toLocaleString()}</span> },
                  { title: 'Thành tiền', key: 'total', render: (_, record) => <span className="text-white font-medium">{(record.quantity * record.price).toLocaleString()}</span> },
                ]} 
                dataSource={viewingInvoice.invoiceDetails || []} 
                rowKey={(record, index) => record.id || index} pagination={false} size="small" className="custom-dark-table border border-slate-800 rounded-xl overflow-hidden"
              />

              {/* Nút thao tác trong Drawer: Chỉ hiện nút "Xác nhận thanh toán" nếu Hóa đơn đang ở trạng thái 0 (Mới tạo) */}
              {viewingInvoice.status === 0 && (
                <div className="mt-auto pt-6">
                   <Button onClick={() => handleMarkAsPaid(viewingInvoice.invoiceId)} type="primary" block className="bg-blue-600 hover:bg-blue-500 border-none h-[45px] text-base font-medium flex items-center justify-center gap-2">
                     <CheckSquare size={18} /> Xác nhận Đã thanh toán
                   </Button>
                </div>
              )}
              
              {/* Nếu đã thanh toán (status = 1), hiển thị thông báo chờ kho */}
              {viewingInvoice.status === 1 && (
                <div className="mt-auto pt-6">
                   <div className="bg-blue-900/20 border border-blue-800/50 rounded-xl p-4 text-center">
                     <p className="text-blue-400 font-medium">Hóa đơn đã thanh toán. Đang chờ Thủ kho duyệt Phiếu xuất hàng.</p>
                   </div>
                </div>
              )}
            </div>
          )}
        </Drawer>

      </div>
    </ConfigProvider>
  );
}