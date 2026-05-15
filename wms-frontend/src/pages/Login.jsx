// src/pages/Login.jsx
import { useState } from 'react';
import { Form, Input, Button, message, ConfigProvider, theme } from 'antd';
import { Archive, Lock, User } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function Login() {
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const onFinish = async (values) => {
    try {
      setIsLoading(true);
      // Gọi API Login của Backend
      const response = await axios.post('http://localhost:8080/api/auth/login', values);
      
      const { token, username } = response.data.data;

      // 1. Lưu Token và thông tin vào Kho của trình duyệt
      localStorage.setItem('token', token);
      localStorage.setItem('username', username);

      message.success('Đăng nhập thành công!');
      
      // 2. Chuyển hướng vào trang chính
      navigate('/');
    } catch (error) {
      console.error(error);
      message.error(error.response?.data?.message || 'Tài khoản hoặc mật khẩu không chính xác!');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <ConfigProvider theme={{ algorithm: theme.darkAlgorithm, token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B', colorText: '#cbd5e1' } }}>
      <div className="min-h-screen bg-[#0B1120] flex flex-col justify-center items-center p-4 relative overflow-hidden">
        
        {/* Vòng tròn Decor nền */}
        <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-blue-600/20 rounded-full blur-[100px] pointer-events-none"></div>
        <div className="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-emerald-600/10 rounded-full blur-[100px] pointer-events-none"></div>

        <div className="bg-[#0F172A] border border-slate-800 p-8 rounded-2xl shadow-2xl w-full max-w-[420px] z-10">
          <div className="flex flex-col items-center mb-8">
            <div className="bg-blue-600 p-3 rounded-xl mb-4 shadow-[0_0_20px_rgba(37,99,235,0.4)]">
              <Archive size={32} className="text-white" />
            </div>
            <h2 className="text-2xl font-bold text-white tracking-wide">WMS Portal</h2>
            <p className="text-slate-400 text-sm mt-1">Đăng nhập vào hệ thống quản trị kho</p>
          </div>

          <Form layout="vertical" onFinish={onFinish} size="large">
            <Form.Item name="username" rules={[{ required: true, message: 'Vui lòng nhập tên đăng nhập!' }]}>
              <Input prefix={<User size={18} className="text-slate-400 mr-2" />} placeholder="Tên đăng nhập" className="bg-[#1E293B] border-slate-700 text-white" />
            </Form.Item>

            <Form.Item name="password" rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}>
              <Input.Password prefix={<Lock size={18} className="text-slate-400 mr-2" />} placeholder="Mật khẩu" className="bg-[#1E293B] border-slate-700 text-white" />
            </Form.Item>

            <Button type="primary" htmlType="submit" loading={isLoading} block className="bg-blue-600 hover:bg-blue-500 border-none mt-4 font-medium h-[45px]">
              Đăng nhập
            </Button>
          </Form>
        </div>
      </div>
    </ConfigProvider>
  );
}