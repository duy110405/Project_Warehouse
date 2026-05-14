import { Tabs ,ConfigProvider , theme} from 'antd';

import Supplier from './Supplier'
import Vendor from './Vendor';

export default function SupplyManager(){
    const tabItems = [
    {
      key: '1',
      label: 'Xưởng gia công',
      children: <Supplier />, // Component chứa giao diện Card của Xưởng
    }, 
     {
      key: '2',
      label: 'Nhà cung cấp Nguyên liệu',
      children: <Vendor />, // Component chứa giao diện Card của Vendor
    },
    // {
    //   key: '3', // Dành cho tương lai mở rộng
    //   label: 'Đơn vị Vận chuyển',
    //   children: <Courier />, 
    // },
  ];

  return (
    <ConfigProvider
      theme={{
        algorithm: theme.darkAlgorithm,
        token: { colorBgContainer: '#0F172A', colorBorderSecondary: '#1E293B' },
      }}
    >
      <div className="flex flex-col h-full max-w-[1400px] mx-auto text-slate-200">
        
        {/* HEADER CHUNG */}
        <div className="mb-6">
          <h2 className="text-2xl font-bold text-white tracking-wide">Quản lý Đối tác</h2>
          <p className="text-slate-400 text-sm mt-1">Quản lý toàn bộ nguồn cung cấp và đơn vị gia công</p>
        </div>

        {/* TABS CHUYỂN ĐỔI */}
        <Tabs 
          defaultActiveKey="1" 
          items={tabItems} 
          className="custom-dark-tabs"
        />

      </div>
    </ConfigProvider>
  );
}
