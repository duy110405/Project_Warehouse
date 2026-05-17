import { useState, useEffect } from 'react';
import { Plus, Search, Shield, Phone, Briefcase, Edit, Trash2, Lock } from 'lucide-react';
import { Table, Modal, Form, Input, Select, Button, Popconfirm, ConfigProvider, theme, message, Tag, Space } from 'antd';
import axios from 'axios';

// ==========================================
// CẤU HÌNH API
// ==========================================
const API_URL = 'http://localhost:8080/api/users'; // API Quản lý người dùng
const API_ROLE_URL = 'http://localhost:8080/api/roles'; // API Lấy danh sách phân quyền (Admin, Thủ kho, Sales...)

// ==========================================
// 1. COMPONENT THỐNG KÊ
// ==========================================
const UserStats = ({ totalUsers }) => (
  <div className="grid grid-cols-4 gap-6 mb-8">
    <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg flex flex-col justify-center">
      <h3 className="text-slate-400 text-sm font-medium mb-2">Tổng số nhân sự</h3>
      <p className="text-4xl font-bold text-blue-400">{totalUsers}</p>
    </div>
    <div className="bg-[#0F172A] p-5 rounded-2xl border border-slate-800 shadow-lg flex flex-col justify-center">
      <h3 className="text-slate-400 text-sm font-medium mb-2">Trạng thái</h3>
      <p className="text-xl font-bold text-emerald-400">Đang hoạt động</p>
    </div>
  </div>
);

// ==========================================
// 2. COMPONENT BẢNG DỮ LIỆU
// ==========================================
const UserTable = ({ users, isLoading, onEdit, onDelete }) => {
  const columns = [
    {
      title: 'Mã NV',
      dataIndex: 'userId',
      key: 'userId',
      render: (text) => <span className="font-semibold text-blue-400">{text}</span>,
    },
    {
      title: 'Họ và tên',
      dataIndex: 'fullName',
      key: 'fullName',
      render: (text) => <span className="text-white font-medium">{text}</span>,
    },
    {
      title: 'Tài khoản',
      dataIndex: 'username',
      key: 'username',
      render: (text) => <span className="text-slate-300">{text}</span>,
    },
    {
      title: 'Số điện thoại',
      dataIndex: 'phoneNumber',
      key: 'phoneNumber',
      render: (text) => <span className="text-slate-400">{text}</span>,
    },
    {
      title: 'Chức vụ',
      dataIndex: 'position',
      key: 'position',
      render: (text) => (
        <span className="flex items-center gap-1.5 text-slate-300">
          <Briefcase size={14} className="text-slate-500" /> {text}
        </span>
      ),
    },
    {
      title: 'Phân quyền',
      dataIndex: 'roleName', // Lấy tên quyền từ Backend trả về (hoặc role.roleName)
      key: 'roleName',
      render: (_, record) => {
        // Fallback đọc tên quyền từ object role nếu Backend trả về nguyên object
        const roleName = record.roleName || record.role?.roleName || 'Chưa cấp quyền';
        // Đổi màu Tag dựa theo tên quyền (VD: Admin thì màu đỏ, Nhân viên thì màu xanh)
        const color = roleName.toLowerCase().includes('admin') ? 'volcano' : 'blue';
        return (
          <Tag color={color} className="bg-transparent border border-current flex items-center w-fit gap-1 px-2 py-0.5">
            <Shield size={12} /> {roleName}
          </Tag>
        );
      },
    },
    {
      title: 'Hành động',
      key: 'action',
      align: 'right',
      render: (_, record) => (
        <Space size="middle">
          <button onClick={() => onEdit(record)} className="p-2 text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 rounded-lg transition-colors">
            <Edit size={18} />
          </button>
          <Popconfirm title="Xóa nhân sự" description={`Bạn có chắc muốn xóa tài khoản "${record.username}"?`} onConfirm={() => onDelete(record.userId)} okText="Xóa" cancelText="Hủy" okButtonProps={{ danger: true }}>
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
      <Table columns={columns} dataSource={users} loading={isLoading} rowKey="userId" pagination={{ pageSize: 6 }} className="custom-dark-table" />
    </div>
  );
};

// ==========================================
// 3. COMPONENT MODAL (Thêm / Sửa)
// ==========================================
const UserModal = ({ isOpen, onClose, form, onSubmit, isEditing, roles }) => (
  <Modal 
    title={<span className="text-lg flex items-center gap-2">{isEditing ? <Edit size={20} className="text-blue-400"/> : <Plus size={20} className="text-blue-400"/>} {isEditing ? 'Cập nhật tài khoản' : 'Tạo tài khoản mới'}</span>} 
    open={isOpen} 
    onCancel={onClose} 
    footer={null} 
    className="dark-modal" 
    width={700}
  >
    <Form form={form} layout="vertical" onFinish={onSubmit} className="mt-6">
      
      <div className="grid grid-cols-2 gap-4">
        <Form.Item name="userId" label={<span className="text-slate-300">Mã nhân sự (IDND)</span>}>
          <Input placeholder="Hệ thống tự tạo..." className="bg-[#1E293B] border-slate-700 text-slate-500 py-2" disabled />
        </Form.Item>

        {/* Dropdown lấy danh sách Quyền từ bảng ROLE */}
        <Form.Item name="roleId" label={<span className="text-slate-300">Phân quyền hệ thống</span>} rules={[{ required: true, message: 'Vui lòng chọn quyền' }]}>
          <Select placeholder="Chọn quyền..." className="h-[40px] custom-dark-select" showSearch optionFilterProp="children">
            {roles.map(r => (
              <Select.Option key={r.roleId || r.id} value={r.roleId || r.id}>
                {r.roleName || r.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </div>

      <div className="bg-[#0B1120] p-4 rounded-xl border border-slate-800 mb-4">
        <h4 className="text-slate-400 text-sm font-semibold mb-4 uppercase tracking-wider">Thông tin cá nhân</h4>
        <div className="grid grid-cols-2 gap-4">
          <Form.Item name="fullName" label={<span className="text-slate-300">Họ và Tên</span>} rules={[{ required: true, message: 'Nhập họ tên' }]}>
            <Input placeholder="VD: Trần Khánh Duy" className="bg-[#1E293B] border-slate-700 text-white py-2" />
          </Form.Item>

          <Form.Item name="phoneNumber" label={<span className="text-slate-300">Số điện thoại</span>} rules={[{ required: true, message: 'Nhập SĐT' }]}>
            <Input prefix={<Phone size={16} className="text-slate-500 mr-2"/>} placeholder="0987..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
          </Form.Item>

          <Form.Item name="position" label={<span className="text-slate-300">Chức vụ</span>} rules={[{ required: true, message: 'Nhập chức vụ' }]} className="col-span-2 mb-0">
            <Input prefix={<Briefcase size={16} className="text-slate-500 mr-2"/>} placeholder="VD: Trưởng phòng, Thủ kho..." className="bg-[#1E293B] border-slate-700 text-white py-2" />
          </Form.Item>
        </div>
      </div>

      <div className="bg-[#0B1120] p-4 rounded-xl border border-slate-800">
        <h4 className="text-slate-400 text-sm font-semibold mb-4 uppercase tracking-wider">Tài khoản đăng nhập</h4>
        <div className="grid grid-cols-2 gap-4">
          <Form.Item name="username" label={<span className="text-slate-300">Tên đăng nhập</span>} rules={[{ required: true, message: 'Nhập tài khoản' }]}>
            <Input placeholder="VD: duytran.admin" className="bg-[#1E293B] border-slate-700 text-white py-2" disabled={isEditing} />
          </Form.Item>

          {/* Mật khẩu: Chỉ bắt buộc khi Tạo mới. Nếu Sửa mà bỏ trống thì BE tự hiểu là giữ nguyên mật khẩu cũ */}
          <Form.Item name="password" label={<span className="text-slate-300">Mật khẩu</span>} rules={[{ required: !isEditing, message: 'Nhập mật khẩu' }]}>
            <Input.Password prefix={<Lock size={16} className="text-slate-500 mr-2"/>} placeholder={isEditing ? 'Bỏ trống để giữ nguyên MK cũ' : 'Nhập mật khẩu...'} className="bg-[#1E293B] border-slate-700 text-white py-2" />
          </Form.Item>
        </div>
      </div>

      <div className="flex justify-end gap-3 mt-8">
        <Button onClick={onClose} className="border-slate-700 text-slate-300 hover:text-white bg-transparent">Hủy</Button>
        <Button type="primary" htmlType="submit" className="bg-blue-600 hover:bg-blue-500 border-none">
          {isEditing ? 'Lưu thay đổi' : 'Tạo tài khoản'}
        </Button>
      </div>
    </Form>
  </Modal>
);

// ==========================================
// 4. COMPONENT CHÍNH
// ==========================================
export default function UserManagement() {
  const [form] = Form.useForm();
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]); // Chứa danh sách quyền
  const [isLoading, setIsLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // 1. Lấy dữ liệu danh sách Quyền (Chạy 1 lần)
  useEffect(() => {
    const fetchRoles = async () => {
      try {
        const response = await axios.get(API_ROLE_URL);
        setRoles(response.data?.data || []);
      } catch (error) {
        console.error("Lỗi tải danh sách quyền:", error);
      }
    };
    fetchRoles();
  }, []);

  // 2. Lấy danh sách Người dùng
  const fetchUsers = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(API_URL);
      setUsers(response.data?.data || []);
    } catch (error) {
      console.error("Lỗi khi tải dữ liệu user:", error);
      message.error("Không thể lấy danh sách nhân sự!");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
     // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchUsers();
  }, []);

  const handleCloseModal = () => {
    setIsModalOpen(false);
    form.resetFields();
  };

  const handleOpenModal = (user = null) => {
    if (user) {
      setEditingId(user.userId);
      // Map dữ liệu vào form (Lưu ý: Không set lại password để tránh lộ hash)
      form.setFieldsValue({
        ...user,
        password: '', // Xóa trắng ô password khi mở form sửa
        roleId: user.role?.roleId || user.roleId // Lấy ID quyền
      });
    } else {
      setEditingId(null);
      form.resetFields();
    }
    setIsModalOpen(true);
  };

  // 3. Lưu dữ liệu (Thêm mới / Cập nhật)
  const handleSubmit = async (values) => {
    try {
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, values);
        message.success("Cập nhật thông tin tài khoản thành công!");
      } else {
        await axios.post(API_URL, values);
        message.success('Tạo tài khoản mới thành công!');
      }
      handleCloseModal();
      fetchUsers(); 
    } catch (error) {
      console.error("Lỗi khi lưu user:", error);
      message.error(error.response?.data?.message || "Có lỗi xảy ra khi lưu!");
    }
  };

  // 4. Xóa người dùng
  const handleDelete = async (id) => {
    try {
      await axios.delete(`${API_URL}/${id}`);
      message.success('Đã khóa/xóa tài khoản này!');
      fetchUsers(); 
    } catch (error) {
      message.error(error.response?.data?.message || "Không thể xóa tài khoản lúc này!");
    }
  };

  // 5. Lọc tìm kiếm Frontend
  const filteredUsers = users.filter(u => 
    u.fullName?.toLowerCase().includes(searchTerm.toLowerCase()) || 
    u.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    u.phoneNumber?.includes(searchTerm)
  );

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý Nhân sự & Phân quyền</h2>
            <p className="text-slate-400 text-sm mt-1">Cấp tài khoản và quyền truy cập hệ thống</p>
          </div>
          <button onClick={() => handleOpenModal()} className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-5 py-2.5 rounded-xl transition-all shadow-[0_0_15px_rgba(37,99,235,0.3)] font-medium">
            <Plus size={18} /> Thêm nhân sự
          </button>
        </div>

        <UserStats totalUsers={users.length} />

        {/* THANH TÌM KIẾM */}
        <div className="flex items-center mb-6">
          <div className="w-[450px] h-11 bg-[#1E293B] border border-slate-700 rounded-xl flex items-center px-4 shadow-sm focus-within:border-blue-500 transition-colors">
            <Search className="text-slate-400 mr-3" size={18} />
            <input 
              type="text" 
              placeholder="Tìm theo tên, tài khoản hoặc số điện thoại..." 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-transparent text-white text-sm focus:outline-none placeholder-slate-400" 
            />
          </div>
        </div>

        {/* BẢNG DANH SÁCH */}
        <UserTable 
          users={filteredUsers} 
          isLoading={isLoading} 
          onEdit={handleOpenModal} 
          onDelete={handleDelete} 
        />

        {/* MODAL */}
        <UserModal 
          isOpen={isModalOpen} 
          onClose={handleCloseModal} 
          form={form} 
          onSubmit={handleSubmit} 
          isEditing={!!editingId}
          roles={roles} 
        />
      </div>
    </ConfigProvider>
  );
}