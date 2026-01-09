<template>
  <div class="order-management-container">
    <h1>我的订单</h1>
    
    <!-- 订单列表 -->
    <div class="orders-list">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部订单" name="all">
          <el-table :data="filteredOrders" style="width: 100%" stripe>
            <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
            <el-table-column prop="totalAmount" label="订单金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.totalAmount ? scope.row.totalAmount.toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="120">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="350">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
                <el-button v-if="scope.row.status === 1" type="primary" size="small" @click="handlePayOrder(scope.row.id)">支付</el-button>
                <el-button v-if="scope.row.status === 1" type="danger" size="small" @click="handleCancelOrder(scope.row.id)">取消</el-button>
                <el-button v-if="scope.row.status === 3" type="success" size="small" @click="handleConfirmReceive(scope.row.id)">确认收货</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="filteredOrders.length === 0" description="暂无订单" />
        </el-tab-pane>
        <el-tab-pane label="待付款" name="pending_payment">
          <el-table :data="filteredOrders" style="width: 100%" stripe>
            <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
            <el-table-column prop="totalAmount" label="订单金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.totalAmount ? scope.row.totalAmount.toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="120">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="350">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
                <el-button type="primary" size="small" @click="handlePayOrder(scope.row.id)">支付</el-button>
                <el-button type="danger" size="small" @click="handleCancelOrder(scope.row.id)">取消</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="filteredOrders.length === 0" description="暂无待付款订单" />
        </el-tab-pane>
        <el-tab-pane label="待发货" name="pending_shipment">
          <el-table :data="filteredOrders" style="width: 100%" stripe>
            <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
            <el-table-column prop="totalAmount" label="订单金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.totalAmount ? scope.row.totalAmount.toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="120">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="350">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="filteredOrders.length === 0" description="暂无待发货订单" />
        </el-tab-pane>
        <el-tab-pane label="待收货" name="pending_receive">
          <el-table :data="filteredOrders" style="width: 100%" stripe>
            <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
            <el-table-column prop="totalAmount" label="订单金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.totalAmount ? scope.row.totalAmount.toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="120">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="350">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
                <el-button type="success" size="small" @click="handleConfirmReceive(scope.row.id)">确认收货</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="filteredOrders.length === 0" description="暂无待收货订单" />
        </el-tab-pane>
        <el-tab-pane label="已完成" name="completed">
          <el-table :data="filteredOrders" style="width: 100%" stripe>
            <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
            <el-table-column prop="totalAmount" label="订单金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.totalAmount ? scope.row.totalAmount.toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="120">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="350">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="filteredOrders.length === 0" description="暂无已完成订单" />
        </el-tab-pane>
        <el-tab-pane label="已取消" name="cancelled">
          <el-table :data="filteredOrders" style="width: 100%" stripe>
            <el-table-column prop="orderNo" label="订单号" width="180"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
            <el-table-column prop="totalAmount" label="订单金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.totalAmount ? scope.row.totalAmount.toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="120">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="350">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="filteredOrders.length === 0" description="暂无已取消订单" />
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <!-- 购物车区域 -->
    <div class="cart-section">
      <h2>我的购物车</h2>
      <el-table :data="cartItems" stripe style="width: 100%">
        <el-table-column prop="productName" label="商品名称" width="200"></el-table-column>
        <el-table-column prop="num" label="数量" width="100"></el-table-column>
        <el-table-column prop="price" label="单价" width="100">
          <template #default="scope">
            ¥{{ scope.row.price ? scope.row.price.toFixed(2) : '0.00' }}
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120">
          <template #default="scope">
            ¥{{ scope.row.price && scope.row.num ? (scope.row.price * scope.row.num).toFixed(2) : '0.00' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button type="primary" size="small" @click="addToOrder(scope.row)">立刻下单</el-button>
            <el-button type="danger" size="small" @click="handleRemoveFromCart(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="cartItems.length === 0" description="购物车为空" />
    </div>
    
    <!-- 订单详情对话框 -->
    <el-dialog v-model="dialogVisible" title="订单详情" width="70%">
      <div v-if="orderDetail" class="order-detail">
        <div class="detail-header">
          <h3>订单号：{{ orderDetail.orderNo }}</h3>
          <el-tag :type="getStatusType(orderDetail.status)">
            {{ getStatusText(orderDetail.status) }}
          </el-tag>
        </div>
        <div class="detail-info">
          <p>创建时间：{{ orderDetail.createTime }}</p>
          <p>总金额：¥{{ orderDetail.totalAmount ? orderDetail.totalAmount.toFixed(2) : '0.00' }}</p>
          <p v-if="orderDetail.payTime">支付时间：{{ orderDetail.payTime }}</p>
          <p v-if="orderDetail.shipTime">发货时间：{{ orderDetail.shipTime }}</p>
          <p v-if="orderDetail.receiveTime">收货时间：{{ orderDetail.receiveTime }}</p>
        </div>
        <div class="detail-goods">
          <h4>商品信息</h4>
          <el-table :data="orderDetail.orderItems" style="width: 100%">
            <el-table-column prop="productName" label="商品名称" width="200"></el-table-column>
            <el-table-column prop="price" label="单价" width="100">
              <template #default="scope">
                ¥{{ scope.row.price ? scope.row.price.toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
            <el-table-column label="小计" width="120">
              <template #default="scope">
                ¥{{ scope.row.price && scope.row.quantity ? (scope.row.price * scope.row.quantity).toFixed(2) : '0.00' }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button v-if="orderDetail && orderDetail.status === 1" type="primary" @click="handlePayOrder(orderDetail.id)">支付</el-button>
          <el-button v-if="orderDetail && orderDetail.status === 1" type="danger" @click="handleCancelOrder(orderDetail.id)">取消</el-button>
          <el-button v-if="orderDetail && orderDetail.status === 3" type="success" @click="handleConfirmReceive(orderDetail.id)">确认收货</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getOrderList,
  getOrderDetail,
  getOrderDetailWithItems,
  payOrder,
  cancelOrder,
  confirmOrder,
  getCartList,
  removeFromCart,
  createOrder
} from '@/api'

// 状态管理
const activeTab = ref('all')
const orders = ref([])
const filteredOrders = ref([])
const dialogVisible = ref(false)
const orderDetail = ref(null)
const userInfo = ref(null)
const cartItems = ref([])

// 生命周期钩子
onMounted(() => {
  fetchUserInfo()
})

// 获取用户信息
const fetchUserInfo = () => {
  try {
    const userStr = localStorage.getItem('userInfo')
    if (userStr) {
      userInfo.value = JSON.parse(userStr)
      fetchOrders()
      fetchCartList()
    } else {
      // 如果没有用户信息，手动模拟一个用户ID用于测试
      console.log('没有从localStorage获取到用户信息，使用模拟数据')
      userInfo.value = {
        id: 2, // 假设这是一个买家用户ID
        username: 'buyer001',
        nickname: '测试买家'
      }
      fetchOrders()
      fetchCartList()
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
    console.error('获取用户信息失败:', error)
  }
}

// 获取订单列表
const fetchOrders = async () => {
  try {
    if (!userInfo.value) return
    
    // 使用getOrderList接口，传递用户ID参数
    const response = await getOrderList({ userId: userInfo.value.id })
    orders.value = response.data
    filteredOrders.value = orders.value
  } catch (error) {
    ElMessage.error('获取订单列表失败')
    console.error('获取订单列表失败:', error)
  }
}

// 订单状态转换
const getStatusText = (status) => {
  const statusMap = {
    1: '待付款',
    2: '待发货',
    3: '待收货',
    4: '已完成',
    5: '已取消'
  }
  return statusMap[status] || status
}

// 订单状态标签类型
const getStatusType = (status) => {
  const typeMap = {
    1: 'warning',
    2: 'info',
    3: 'success',
    4: 'success',
    5: 'danger'
  }
  return typeMap[status] || 'default'
}

// 标签页切换
const handleTabChange = (tabName) => {
  activeTab.value = tabName
  if (tabName === 'all') {
    filteredOrders.value = orders.value
  } else {
    // 定义标签页名称与状态码的映射
    const statusMap = {
      'pending_payment': 1,
      'pending_shipment': 2,
      'pending_receive': 3,
      'completed': 4,
      'cancelled': 5
    }
    const status = statusMap[tabName]
    filteredOrders.value = status ? orders.value.filter(order => order.status === status) : []
  }
}

// 查看订单详情
const viewOrderDetail = async (orderId) => {
  try {
    const response = await getOrderDetailWithItems(orderId)
    orderDetail.value = response.data
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取订单详情失败')
    console.error('获取订单详情失败:', error)
  }
}

// 处理支付操作
const handlePayOrder = async (orderId) => {
  try {
    const numericOrderId = Number(orderId)
    await payOrder(numericOrderId)
    ElMessage.success('支付成功')
    fetchOrders()
    if (orderDetail.value && orderDetail.value.id === numericOrderId) {
      orderDetail.value.status = 2 // 待发货
    }
  } catch (error) {
    ElMessage.error('支付失败')
    console.error('支付失败:', error)
  }
}

// 处理取消订单操作
const handleCancelOrder = async (orderId) => {
  try {
    if (!userInfo.value) return
    
    const numericOrderId = Number(orderId)
    await cancelOrder(numericOrderId, userInfo.value.id)
    ElMessage.success('取消订单成功')
    fetchOrders()
    if (orderDetail.value && orderDetail.value.id === numericOrderId) {
      orderDetail.value.status = 5 // 已取消
    }
  } catch (error) {
    ElMessage.error('取消订单失败')
    console.error('取消订单失败:', error)
  }
}

// 处理确认收货操作
const handleConfirmReceive = async (orderId) => {
  try {
    if (!userInfo.value) return
    
    const numericOrderId = Number(orderId)
    await confirmOrder(numericOrderId, userInfo.value.id)
    ElMessage.success('确认收货成功')
    fetchOrders()
    if (orderDetail.value && orderDetail.value.id === numericOrderId) {
      orderDetail.value.status = 4 // 已完成
    }
  } catch (error) {
    ElMessage.error('确认收货失败')
    console.error('确认收货失败:', error)
  }
}

// 获取购物车列表
const fetchCartList = async () => {
  try {
    if (!userInfo.value) return
    const response = await getCartList(userInfo.value.id)
    cartItems.value = (response.data || []).map(item => ({
      ...item,
      id: String(item.id) // 将id转换为字符串以避免精度丢失
    }))
  } catch (error) {
    ElMessage.error('获取购物车数据失败')
    console.error('获取购物车数据失败:', error)
  }
}

// 从购物车删除商品
const handleRemoveFromCart = async (cartItem) => {
  try {
    console.log('准备删除购物车商品，cartItem:', cartItem)
    console.log('cartItem.id:', cartItem.id)
    console.log('cartItem.id类型:', typeof cartItem.id)
    const response = await removeFromCart(cartItem.id)
    console.log('删除商品响应:', response)
    ElMessage.success('删除商品成功')
    fetchCartList()
  } catch (error) {
    console.error('删除商品失败详情:', error)
    console.error('删除商品失败，error.message:', error.message)
    console.error('删除商品失败，error.response:', error.response)
    ElMessage.error('删除商品失败')
    console.error('删除商品失败:', error)
  }
}

// 添加到订单
const addToOrder = async (cartItem) => {
  try {
    if (!userInfo.value) return
    const totalPrice = cartItem.price * cartItem.num
    const orderData = {
      order: {
        userId: userInfo.value.id,
        shopId: 1, // 默认店铺ID，需根据实际业务调整
        totalAmount: totalPrice,
        payAmount: totalPrice,
        receiverName: '默认收货人', // 需替换为实际收货信息
        receiverPhone: '13800138000',
        receiverAddress: '默认收货地址'
      },
      orderItems: [{
        productId: cartItem.productId,
        productName: cartItem.productName,
        quantity: cartItem.num, // 订单系统使用quantity字段
        price: cartItem.price,
        totalPrice: totalPrice
      }]
    }
    await createOrder(orderData)
    ElMessage.success('添加到订单成功')
    await removeFromCart(cartItem.id)
    fetchOrders()
    fetchCartList()
  } catch (error) {
    ElMessage.error('添加到订单失败')
    console.error('添加到订单失败:', error)
  }
}
</script>

<style scoped>
.order-management-container {
  padding: 20px;
}

h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.orders-list {
  margin-bottom: 30px;
}

.order-detail {
  padding: 20px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.detail-header h3 {
  margin: 0;
}

.detail-info {
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.detail-info p {
  margin: 5px 0;
}

.detail-goods {
  margin-top: 20px;
}

.cart-section {
  margin-top: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.cart-section h2 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #333;
}
</style>