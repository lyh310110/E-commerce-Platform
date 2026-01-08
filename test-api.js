// 测试localStorage中的userInfo数据
console.log('localStorage userInfo:', localStorage.getItem('userInfo'));

// 测试API调用
const testApi = async () => {
  try {
    // 测试获取可领取优惠券（不需要userId的API）
    const response = await fetch('http://localhost:3000/api/coupon/available');
    console.log('API Response Status:', response.status);
    console.log('API Response:', await response.json());
  } catch (error) {
    console.error('API Test Error:', error);
  }
};

testApi();
