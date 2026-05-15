
import axios from 'axios';

// Thiết lập tự động đính kèm Token vào mọi request gửi đi
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      // Nếu có token, nhét nó vào Header Authorization
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Bắt lỗi tự động: Nếu Token hết hạn (Lỗi 401 hoặc 403), tự động đá văng ra trang Login
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      window.location.href = '/login'; // Ép tải lại trang về login
    }
    return Promise.reject(error);
  }
);

export default axios;