import { useState , useEffect } from 'react';
import { Plus, Search, Building2, Phone, Mail , Edit , Trash2 } from 'lucide-react';
import { Modal, Form, Input, Button, Popconfirm , ConfigProvider , theme, message , Spin , Empty } from 'antd';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/vendor';


//Component thống kê
const VendorStats = () => (
      <div className="grid grid-cols-4 gap-6 mb-8">
        <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg">
          <h3 className="text-slate-400 text-sm font-medium mb-2">Total Active Vendors</h3>
          <p className="text-3xl font-bold text-white">24</p>
         </div>
         <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg">
          <h3 className="text-slate-400 text-sm font-medium mb-2">Active Purchase Orders</h3>
          <p className="text-3xl font-bold text-white">156</p>
         </div>
         <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg">
          <h3 className="text-slate-400 text-sm font-medium mb-2">Avg. On-Time Rate</h3>
          <p className="text-3xl font-bold text-emerald-400">94.5%</p>
         </div>
         <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg">
          <h3 className="text-slate-400 text-sm font-medium mb-2">Total Spend (MTD)</h3>
          <p className="text-3xl font-bold text-white">2.8B <span className="text-lg text-slate-500 font-normal">VND</span></p>
         </div>
      </div>
   );

   //Component danh sách dạng thẻ
   const VendorGrid =({vendors , isLoading, onEdit, onDelete}) => {
    if(isLoading){
      return (
      <div className="flex justify-center items-center py-20">
        <Spin size="large" />
      </div>
    );
    } 
    if(!vendors  || vendors.length == 0){
          return (
      <div className="bg-[#0F172A] rounded-2xl py-20 border border-slate-800">
        <Empty description={<span className="text-slate-400">Chưa có xưởng gia công nào</span>} />
      </div>
    );
    }

    return (
           <div className="grid grid-cols-2 gap-6 pb-10">
        {vendors.map((vendor) => (
          <div key={vendor.id} className="bg-[#0F172A] border border-slate-800 rounded-2xl p-6 shadow-xl hover:border-slate-600 transition-colors flex flex-col">
            
            {/* Card Header: Icon, Tên, Đánh giá */}
            <div className="flex justify-between items-start mb-6">
              <div className="flex gap-4 items-center">
                <div className="w-12 h-12 rounded-xl bg-blue-900/30 border border-blue-800/50 flex items-center justify-center text-blue-400">
                  <Building2 size={24} />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-white">{vendor.name}</h3>
                  <div className="flex items-center gap-2 mt-1">
                    <span className="text-xs px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                      {vendor.type}
                    </span>
                  </div>
                </div>
              </div>
              {/* Nút Edit hoặc xóa */}
             <div className='gap-3'>
               <button onClick={() => onEdit(vendor)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
               <Edit size={18} />
               </button>
              <Popconfirm title="Xóa sản phẩm" description={`Bạn có chắc chắn muốn xóa "${vendor.name}"?`} onConfirm={() => onDelete(vendor.id)} okText="Xóa" cancelText="Hủy" okButtonProps={{ danger: true }}>
              <button className="p-2 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded-lg transition-colors">
              <Trash2 size={18} />
              </button>
              </Popconfirm>
             </div>               

            </div>

            {/* Card Body: Các chỉ số KPI */}
            <div className="grid grid-cols-3 gap-4 mb-6">
              <div className="bg-[#1E293B] rounded-lg p-3">
                <p className="text-slate-400 text-xs mb-1">Đơn đang chờ</p>
                <p className="text-lg font-semibold text-white">{vendor.activeOrders}</p>
              </div>
              <div className="bg-[#1E293B] rounded-lg p-3">
                <p className="text-slate-400 text-xs mb-1">Tỷ lệ đúng hạn</p>
                <p className={`text-lg font-semibold ${vendor.onTimeRate < '90%' ? 'text-red-400' : 'text-emerald-400'}`}>
                  {vendor.onTimeRate}
                </p>
              </div>
              <div className="bg-[#1E293B] rounded-lg p-3">
                <p className="text-slate-400 text-xs mb-1">Tổng chi tiêu</p>
                <p className="text-lg font-semibold text-white">{vendor.totalSpend}</p>
              </div>
            </div>

            {/* Thông tin liên hệ mini */}
            <div className="flex gap-4 text-sm text-slate-400 mb-6 border-t border-slate-800 pt-4">
              <div className="flex items-center gap-1.5"><Phone size={14} /> {vendor.phone}</div>
              <div className="flex items-center gap-1.5"><Mail size={14} /> {vendor.email}</div>
            </div>

            {/* Card Footer: Các nút thao tác */}
            <div className="mt-auto flex gap-3">
              <button className="flex-1 bg-[#1E293B] hover:bg-slate-700 border border-slate-700 text-white py-2.5 rounded-xl text-sm font-medium transition-colors">
                Xem chi tiết
              </button>
              <button className="flex-1 bg-blue-600 hover:bg-blue-500 text-white py-2.5 rounded-xl text-sm font-medium transition-colors">
                Lên đơn nhập
              </button>
            </div>
          </div>
          ))}
        </div>
    )
   };

   //Component Modal thêm / sửa
   const VendorModal  =  ({ isOpen, onClose, form, onSubmit, isEditing }) => {
    <Modal title={<span className="text-lg">{isEditing ? 'Sửa thông tin nhà cung cấp' : 'Thêm nhà cung cấp mới'}</span>} open={isOpen} onCancel={onClose} footer={null} className="dark-modal" width={650}>
        <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6 ">
          
          <div className="grid grid-cols-2 gap-4">
             <Form.Item name="code" label={<span className="text-slate-300">Mã nhà cung cấp</span>} rules={[{ required: true }]}>
               <Input placeholder="VD: NCC001" className="bg-[#1E293B] border-slate-700 text-white py-2" disabled={isEditing !== null} />
             </Form.Item>

             <Form.Item name="name" label={<span className="text-slate-300">Tên nhà cung cấp</span>} rules={[{ required: true }]}>
               <Input placeholder="VD: Công ty TNHH..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
             </Form.Item>

             <Form.Item name="type" label={<span className="text-slate-300">Loại hàng cung cấp</span>} rules={[{ required: true }]}>
               <Input placeholder="VD: Nguyên vật liệu" className="bg-[#1E293B] border-slate-700 text-white py-2" />
             </Form.Item>

             <Form.Item name="phone" label={<span className="text-slate-300">Số điện thoại</span>} rules={[{ required: true }]}>
               <Input type="text" className="bg-[#1E293B] border-slate-700 text-white py-2" />
             </Form.Item>
          </div>

          {/* Để Email và Địa chỉ chiếm full dòng  */}
          <Form.Item name="email" label={<span className="text-slate-300">Email liên hệ</span>} rules={[{ type: 'email' }]}>
            <Input placeholder="email@domain.com" className="bg-[#1E293B] border-slate-700 text-white py-2" />
          </Form.Item>

          <Form.Item name="address" label={<span className="text-slate-300">Địa chỉ</span>} rules={[{ required: true }]}>
            <Input className="bg-[#1E293B] border-slate-700 text-white py-2" />
          </Form.Item>

          <div className="flex justify-end gap-3 mt-8">
            <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
            <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
              {isEditing ? 'Lưu thay đổi' : 'Thêm nhà cung cấp'}
            </Button>
          </div>
        </Form>
      </Modal>
   }


export default function Vendor() {

    const [form] = Form.useForm();
    const [vendors , setVendors] = useState([]);
    const [isLoading, setIsLoading] = useState(true); // Mặc định true để load API mượt hơn
     const [searchTerm, setSearchTerm] = useState('');

    // State Modal (Thêm/Sửa)
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);

  //Lấy dữ liệu
  const fetchVendors = async () =>{
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL);
      const actualData = response.data.data || []; 
      
      const dataWithFakeStats = actualData.map(item => ({
        ...item,
        key: item.vendorId, 
        activeOrders: Math.floor(Math.random() * 20) + 1, 
        onTimeRate: '98%',
        totalSpend: '450M VND',
      }));
      setVendors(dataWithFakeStats);
    } catch (error) {
      console.error("Lỗi khi tải dữ liệu:", error);
      message.error("Không thể kết nối đến máy chủ!");
    } finally {
      setIsLoading(false);
    }
  };

  // Tự động gọi hàm fetchSuppliers khi component vừa được render lần đầu
       useEffect(() => {
        // eslint-disable-next-line react-hooks/set-state-in-effect
        fetchVendors();
      }, []);
  
  // Lưu dữ liệu
  const handleSubmit = async (values) =>{
    try{
      if(editingId){
        await axios.put(`${API_URL}/${editingId}`, values);
        message.success("cập nhật nhà cung cấp thành công")
      }else{
        await axios.post(API_URL, values);
        message.success('Thêm nhà cung cấp thành công!');
      }
       handleCloseModal();
      fetchVendors(); 
    }catch(error) {
      console.error("Lỗi khi lưu:", error);
      message.error("Có lỗi xảy ra khi lưu dữ liệu!");
    }
  };

  //Xóa dữ liệu
  const handleDelete = async(id) =>{
    try{
      await axios.delete(`${API_URL}/${id}`);
       message.success('Đã xóa xưởng này!');
      fetchVendors(); 
    } catch (error) {
      console.error("Lỗi khi xóa:", error);
      message.error(error.response?.data?.message || "Không thể xóa!");
    }
  };

 // Quản lý modal
  const handleOpenModal = (vendor = null) =>{
    if(vendor){
        setEditingId(vendor.vendorId);
        form.setFieldsValue(vendor);
    }else{
        setEditingId(null);
         form.resetFields();
         form.setFieldsValue({ status: 1 });

    }
     setIsModalOpen(true);
  };

    const handleCloseModal = () => {
    setIsModalOpen(false);
    form.resetFields();
  };

  // BỘ LỌC TÌM KIẾM (Frontend Filtering)
  const filteredVendors = vendors.filter(v => 
    v.vendorName?.toLowerCase().includes(searchTerm.toLowerCase()) || 
    v.type?.toLowerCase().includes(searchTerm.toLowerCase())
  );

    return (
        <ConfigProvider
          theme={{
            algorithm: theme.darkAlgorithm,
            token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' },
          }}
        >
          <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
            
            {/* HEADER */}
            <div className="flex items-center justify-between mb-8">
              <div>
                <h2 className="text-2xl font-bold text-white tracking-wide">Nhà cung cấp</h2>
                <p className="text-slate-400 text-sm mt-1">Quản lý nhà cung cấp nguyên liệu</p>
              </div>
              <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
                <Plus size={18} /> Thêm nhà cung cấp
              </button>
            </div>
    
            {/* CÁC COMPONENT GIAO DIỆN ĐƯỢC NHÚNG VÀO (Truyền props) */}
            
            <VendorStats />
            <div className="flex items-center mb-6">
              <div className="w-[400px] h-11 bg-[#1E293B] border border-slate-700 rounded-xl flex items-center px-4 shadow-sm focus-within:border-blue-500 transition-colors">
                <Search className="text-slate-400 mr-3" size={18} />
                <input 
                  type="text" 
                  placeholder="Tìm theo tên, loại hàng..." 
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full bg-transparent text-white text-sm focus:outline-none placeholder-slate-400" 
                />
              </div>
            </div>
    
            <VendorGrid 
              suppliers={filteredVendors} 
              isLoading={isLoading} 
              onEdit={handleOpenModal} 
              onDelete={handleDelete} 
            />
    
            <VendorModal 
              isOpen={isModalOpen} 
              onClose={handleCloseModal} 
              form={form} 
              onSubmit={handleSubmit} 
              isEditing={!!editingId} 
            />

          </div>
        </ConfigProvider>
  );
  
}