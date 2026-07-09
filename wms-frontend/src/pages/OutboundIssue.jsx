import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Select, Tag, Space, Popconfirm, ConfigProvider, theme, message, DatePicker, Drawer, Divider, InputNumber, Input } from 'antd';
import { Plus, Edit, Trash2, Search, Calendar, Eye, FileText, Settings2, CheckCircle, Clock, BadgeX } from 'lucide-react';
import axios from 'axios';
import dayjs from 'dayjs';

const API_URL = 'http://localhost:8080/api/outbound'; 
const API_PRODUCT_URL = 'http://localhost:8080/api/product'; 
const API_ZONE_URL =  'http://localhost:8080/api/zone';
const API_INVOICE_URL =  'http://localhost:8080/api/invoice';

const { RangePicker } = DatePicker;

// ============================================================================
// 1. COMPONENT BẢNG PHIẾU XUẤT
// ============================================================================
const IssueTable = ({ issues, isLoading, onEdit, onDelete, onView, activeTab , pagination, onChange }) => {
  const columns = [
    {
      title: 'Mã phiếu xuất',
      dataIndex: 'issueId', 
      key: 'issueId',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Ngày xuất',
      dataIndex: 'issueDate',
      key: 'issueDate',
      render: (text) => <span className="text-slate-200">{text ? dayjs(text).format('DD/MM/YYYY') : '---'}</span>,
    },
    {
      title: 'Hóa đơn tham chiếu',
      key: 'invoices',
      // Hiển thị danh sách các Hóa đơn được gắn vào phiếu xuất này
      render: (_, record) => (
        <Space size={[0, 4]} wrap>
          {record.invoices?.length > 0 
            ? record.invoices.map(inv => <Tag color="blue" key={inv.invoiceId} className="border-blue-800 bg-blue-900/30">{inv.invoiceId}</Tag>)
            : <span className="text-slate-500">Xuất lẻ (Không có HĐ)</span>}
        </Space>
      ),
    },
    {
      title: 'Tổng tiền',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => <span className="text-rose-400 font-medium">{(amount || 0).toLocaleString()} VND</span>, // Phiếu xuất dùng màu Rose (Đỏ)
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
          <button onClick={() => onView(record)} className="p-2 text-slate-400 hover:text-rose-400 hover:bg-rose-400/10 rounded-lg transition-colors" title="Xem chi tiết">
            <Eye size={18} />
          </button>
          
          {/* Chỉ hiển thị nút Sửa/Xóa khi ở Tab Nháp (status = 0) */}
          {activeTab === 0 && (
            <>
              <button onClick={() => onEdit(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
                <Edit size={18} />
              </button>
              <Popconfirm title="Hủy phiếu xuất" description={`Bạn có chắc muốn hủy phiếu "${record.issueId}"?`} onConfirm={() => onDelete(record.issueId)} okText="Đồng ý" cancelText="Hủy" okButtonProps={{ danger: true }}>
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
      <Table columns={columns} dataSource={issues} loading={isLoading} rowKey="issueId" pagination={pagination} onChange={onChange} className="custom-dark-table" />
    </div>
  );
}

// ============================================================================
// 2. COMPONENT MODAL (Tạo/Sửa Phiếu Xuất)
// ============================================================================
const IssueModal = ({ isOpen, onClose, form, onSubmit, isEditing, invoices, products, zones }) => (
  <Modal title={<span className="text-lg">{isEditing ? 'Sửa phiếu xuất' : 'Tạo phiếu xuất mới'}</span>} open={isOpen} onCancel={onClose} footer={null} className="dark-modal" width={850}>
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      
      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="issueId" label={<span className="text-slate-300">Mã phiếu</span>}>
          <Input placeholder="Tự động tạo..." className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled />
        </Form.Item>

        <Form.Item name="issueDate" label={<span className="text-slate-300">Ngày xuất</span>} rules={[{ required: true }]}>
          <DatePicker className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" format="DD/MM/YYYY" />
        </Form.Item>
      </div>

      <div className="bg-blue-900/10 border border-blue-800/30 p-4 rounded-xl mb-4">
        {/* ĐÂY LÀ CHỖ CHUẨN ĐỂ ĐẶT Ô CHỌN HÓA ĐƠN NÈ! */}
        <Form.Item name="invoiceIds" label={<span className="text-blue-300 font-medium">Lấy hàng theo Hóa Đơn (Tự động điền)</span>} className="mb-0">
          <Select 
            mode="multiple" // Hỗ trợ gộp nhiều HĐ vào 1 Phiếu xuất
            placeholder="Tìm và chọn các Hóa đơn đã thanh toán..." 
            className="custom-dark-select min-h-[40px]" 
            showSearch 
            optionFilterProp="children"

            onChange={(selectedIds) => {
              // Dùng Object để gom nhóm các sản phẩm trùng lặp
              let groupedDetails = {}; 
              selectedIds.forEach(id => {
                const inv = invoices.find(i => i.invoiceId === id);
                if (inv && inv.invoiceDetails) {
                  inv.invoiceDetails.forEach(item => {
                    const pId = item.product?.productId || item.productId;
                    // KỸ THUẬT CỘNG DỒN: Nếu sản phẩm đã tồn tại trong nhóm, chỉ cần cộng thêm số lượng
                    if (groupedDetails[pId]) {
                      groupedDetails[pId].quantity += item.quantity;
                    } else {
                      // Nếu chưa có, tạo dòng mới
                      groupedDetails[pId] = {
                        productId: pId,
                        quantity: item.quantity,
                        price: item.price,
                        zoneId: null // Vẫn để trống chờ Thủ kho chọn
                      };
                    }
                  });
                }
              });

              // Chuyển Object quay ngược lại thành Mảng (Array) và đẩy lên Form
              form.setFieldsValue({ issueDetails: Object.values(groupedDetails) });
            }}

          >
            {/* Chỉ lấy Hóa đơn status = 1 (Đã thanh toán / Chờ xuất kho) để gợi ý */}
            {/* Ép mảng an toàn trước khi filter */}
           {(Array.isArray(invoices) ? invoices : []).filter(i => i.status === 1).map(i => (
              <Select.Option key={i.invoiceId} value={i.invoiceId}>
                 {i.invoiceId} - Khách: {i.customer?.customerName}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </div>

      <Divider className="border-slate-700 my-4" />
      <div className="flex items-center gap-2 mb-4">
         <Settings2 size={18} className="text-blue-400"/>
         <h4 className="text-slate-200 font-semibold">Chi tiết hàng hóa xuất kho</h4>
      </div>

      {/* Đã sửa name thành issueDetails cho khớp entity OutboundIssue */}
      <Form.List name="issueDetails">
        {(fields, { add, remove }) => (
          <>
            {fields.map(({ key, name, ...restField }) => (
              <div key={key} className="flex gap-3 mb-3 items-start">
                {/* Chọn Sản Phẩm */}
                <Form.Item {...restField} name={[name, 'productId']} rules={[{ required: true, message: 'Chọn hàng' }]} className="flex-1 mb-0">
                  <Select placeholder="Chọn sản phẩm..." className="h-[40px] custom-dark-select" showSearch optionFilterProp="children"
                    // Áp dụng chiêu Auto-fill giá bán nếu Thủ kho xuất hàng tự do (không chọn từ Hóa đơn)
                    onChange={(prodId) => {
                      const selectedProd = products.find(p => p.productId === prodId);
                      if (selectedProd) {
                        const currentDetails = form.getFieldValue('issueDetails') || [];
                        currentDetails[name] = { ...currentDetails[name], price: selectedProd.price };
                        form.setFieldsValue({ issueDetails: currentDetails });
                      }
                    }}
                  >
                    {products.map(p => <Select.Option key={p.productId || p.id} value={p.productId || p.id}>{p.productName || p.name}</Select.Option>)}
                  </Select>
                </Form.Item>
                
                {/* Lấy từ Khu Vực */}
                <Form.Item {...restField} name={[name, 'zoneId']} rules={[{ required: true, message: 'Chọn Zone' }]} className="w-48 mb-0">
                  <Select placeholder="Lấy từ Zone" className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
                    {zones?.map(z => <Select.Option key={z.zoneId || z.id} value={z.zoneId || z.id}>{z.zoneName || z.name}</Select.Option>)}
                  </Select>
                </Form.Item>
                
                <Form.Item {...restField} name={[name, 'quantity']} rules={[{ required: true, message: 'Nhập SL' }]} className="w-24 mb-0">
                  <InputNumber placeholder="SL" className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" min={1} />
                </Form.Item>
                
                <Form.Item {...restField} name={[name, 'price']} rules={[{ required: true, message: 'Nhập giá' }]} className="w-32 mb-0">
                  <InputNumber placeholder="Đơn giá" className="w-full bg-[#1E293B] border-slate-700 text-white h-[40px]" min={0} formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} />
                </Form.Item>
                
                <button type="button" onClick={() => remove(name)} className="h-[40px] px-3 text-slate-400 hover:text-red-400 bg-[#1E293B] hover:bg-red-400/10 rounded-lg transition-colors border border-slate-700">
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
            <Button type="dashed" onClick={() => add()} block icon={<Plus size={16}/>} className="bg-transparent border-slate-700 text-blue-400 hover:text-blue-300 hover:border-blue-400 h-[40px] mt-2">
              Thêm dòng xuất hàng tự do
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
export default function OutboundIssue() {
  const [form] = Form.useForm();
  
  const [activeTab, setActiveTab] = useState(0); // 0 = Nháp, 1 = Đã xuất , -1 = Đã hủy
  const [issues, setIssues] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  
  const [searchTerm, setSearchTerm] = useState('');
  const [dateRange, setDateRange] = useState(null);

  // Data phụ trợ
  const [products, setProducts] = useState([]);
  const [zones, setZones] = useState([]);
  const [invoices, setInvoices] = useState([]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [viewingIssue, setViewingIssue] = useState(null);

  const [pagination, setPagination] = useState({ current: 1, pageSize: 6, total: 0 });

  useEffect(() => {
    const fetchSelectData = async () => {
      try {
        const [prodRes, zoneRes, inRes] = await Promise.all([ 
          axios.get(API_PRODUCT_URL), 
          axios.get(API_ZONE_URL), 
          axios.get(API_INVOICE_URL , { params: { status: 1, page: 0, size: 100 } }) 
        ]);
        setProducts(prodRes.data?.data || []);
        setZones(zoneRes.data?.data || []);
        setInvoices(inRes.data?.data?.content || []);
      } catch (error) { console.error("Lỗi data phụ:", error); }
    };
    fetchSelectData();
  }, []);

  const fetchIssues = async (page = 1, size = 6) => {
    try {
      setIsLoading(true);
      const params = { 
        status: activeTab,
        search: searchTerm || null,
        page: page - 1, // API thường sử dụng chỉ số bắt đầu từ 0
        size: size
      };
      
      if (dateRange && dateRange.length === 2) {
        params.startDate = dateRange[0].format('YYYY-MM-DD');
        params.endDate = dateRange[1].format('YYYY-MM-DD');
      }

      const response = await axios.get(API_URL, { params });
      const pageData = response.data?.data || {};
      setIssues(pageData.content || []);
      
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
     fetchIssues(); 
  }, [activeTab, searchTerm, dateRange]);

  const handleTableChange = (newPagination) => {
    fetchIssues(newPagination.current, newPagination.pageSize);
  }

  const handleSubmit = async (values) => {
    try {
      const currentUsername = localStorage.getItem('username');
      const payload = { 
        ...values, 
        issueDate: values.issueDate.format('YYYY-MM-DD'),
        status: 0 ,
        createBy: currentUsername 
      };
      
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, payload);
        message.success('Cập nhật phiếu thành công!');
      } else {
        await axios.post(API_URL, payload);
        message.success('Tạo phiếu xuất kho thành công!');
      }
      setIsModalOpen(false);
      fetchIssues(); 
    } catch (error) { 
      message.error(error.response?.data?.message || "Lỗi khi lưu phiếu!"); 
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.put(`${API_URL}/${id}/cancel`); 
      message.success('Đã hủy phiếu xuất!');
      fetchIssues(); 
    } catch (error) {
      console.log(error);
      message.error("Lỗi khi hủy phiếu!"); }
  };

  const handleApprove = async (id) => {
    try {
      await axios.put(`${API_URL}/${id}/approve`); 
      message.success('Đã duyệt xuất kho thành công!');
      setIsDrawerOpen(false);
      fetchIssues();
    } catch (error) {
      message.error(error.response?.data?.message || "Lỗi khi duyệt phiếu!");
    }
  };

  const handleOpenModal = (issue = null) => {
    if (issue) {
      setEditingId(issue.issueId);
      form.setFieldsValue({ 
        ...issue, 
        issueDate: dayjs(issue.issueDate),
        // Lấy danh sách ID hóa đơn đổ vào cái Select nhiều HĐ
        invoiceIds: issue.invoices?.map(i => i.invoiceId) || []
      }); 
    } else {
      setEditingId(null);
      form.resetFields();
      form.setFieldsValue({ issueDate: dayjs() });
    }
    setIsModalOpen(true);
  };

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER */}
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý Xuất kho Sản phẩm</h2>
            <p className="text-slate-400 text-sm mt-1">Trừ hàng trong kho dựa theo Hóa đơn</p>
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
              <input type="text" placeholder="Tìm kiếm theo mã phiếu..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full bg-[#1E293B] border border-slate-700 text-white text-sm rounded-lg pl-10 pr-4 py-2.5 focus:outline-none focus:border-blue-500 transition-colors"
              />
            </div>
          </div>

          <div className="bg-[#1E293B] border border-slate-700 rounded-xl px-2 h-14 flex items-center shadow-sm w-72">
             <Calendar size={18} className="text-blue-400 mx-2" />
             <RangePicker 
               className="bg-transparent border-none shadow-none hover:bg-transparent focus:bg-transparent custom-dark-rangepicker w-full" 
               format="DD/MM/YYYY" placeholder={['Từ ngày', 'Đến ngày']} onChange={(dates) => setDateRange(dates)}
             />
          </div>
        </div>

        {/* BẢNG DỮ LIỆU */}
        <IssueTable 
          issues={issues} 
          isLoading={isLoading} 
          activeTab={activeTab} 
          onEdit={handleOpenModal} 
          onDelete={handleDelete} 
          pagination={pagination}
          onChange={handleTableChange}
          onView={(record) => { setViewingIssue(record); setIsDrawerOpen(true); }} 
        />
        
        {/* MODAL */}
        <IssueModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} form={form} onSubmit={handleSubmit} isEditing={!!editingId} invoices={invoices} products={products} zones={zones} />

        {/* DRAWER CHI TIẾT */}
        <Drawer
          title={<div className="flex items-center gap-3"><FileText className="text-blue-400" size={24} /><span className="text-white text-lg font-semibold tracking-wide">Chi tiết Phiếu Xuất</span></div>}
          placement="right" width={750} onClose={() => setIsDrawerOpen(false)} open={isDrawerOpen}
          styles={{ header: { background: '#0F172A', borderBottom: '1px solid #1E293B' }, body: { background: '#0B1120', padding: '24px' } }}
          closeIcon={<span className="text-slate-400 hover:text-white">✕</span>}
        >
          {viewingIssue && (
            <div className="flex flex-col h-full">
              <div className="bg-[#0F172A] p-5 rounded-xl border border-slate-800 mb-6">
                <div className="flex justify-between items-start mb-4">
                   <div>
                     <h3 className="text-xl font-bold text-white mb-1">Mã: {viewingIssue.issueId}</h3>
                     <p className="text-slate-400">Ngày xuất: {dayjs(viewingIssue.issueDate).format('DD/MM/YYYY')}</p>
                   </div>
                   <Tag color={viewingIssue.status === 1 ? 'success' : viewingIssue.status === -1 ? 'error' : 'default'} className="text-sm px-3 py-1">
                     {viewingIssue.status === 1 ? 'Đã xuất' : viewingIssue.status === -1 ? 'Đã hủy' : 'Bản nháp'}
                   </Tag>
                </div>
                <Divider className="border-slate-700 m-0 mb-4"/>
                <div className="grid grid-cols-2 gap-4 text-sm">
                   <p className="text-slate-400">Tham chiếu Hóa đơn: <span className="text-white font-medium block">
                     {viewingIssue.invoices?.map(i => i.invoiceId).join(', ') || 'Xuất lẻ'}
                   </span></p>
                   <p className="text-slate-400">Tổng giá trị: <span className="text-rose-400 font-bold text-base block">{(viewingIssue.totalAmount || 0).toLocaleString()} VND</span></p>
                </div>
              </div>

              <h4 className="text-lg font-semibold text-white mb-4">Danh sách hàng xuất</h4>
              <Table 
                columns={[
                  { title: 'Tên hàng', dataIndex:'productName', key: 'name', render: text => <span className="text-slate-200">{text || '---'}</span> },
                  { title: 'Lấy từ khu vực', dataIndex:'zoneName', key: 'zone', render: text => <Tag className="bg-slate-800 border-slate-600 text-slate-300">{text || 'Chưa phân'}</Tag> },
                  { title: 'SL', dataIndex: 'quantity', key: 'qty', render: val => <span className="text-rose-400 font-medium">{val}</span> },
                  { title: 'Đơn giá', dataIndex: 'price', key: 'price', render: val => <span className="text-slate-400">{(val || 0).toLocaleString()}</span> },
                  { title: 'Thành tiền', key: 'total', render: (_, record) => <span className="text-white font-medium">{(record.quantity * record.price).toLocaleString()}</span> },
                ]} 
                dataSource={viewingIssue.issueDetails || []} 
                rowKey={(record, index) => record.id || index} pagination={false} size="small" className="custom-dark-table border border-slate-800 rounded-xl overflow-hidden"
              />

              {/* Nếu phiếu đang Nháp, hiện nút Duyệt xuất kho */}
              {viewingIssue.status === 0 && (
                <div className="mt-auto pt-6">
                   <Button 
                     onClick={() => handleApprove(viewingIssue.issueId)}
                     type="primary" block className="bg-blue-600 hover:bg-blue-500 border-none h-[45px] text-base font-medium"
                   >
                     Xác nhận xuất kho (Trừ tồn kho)
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