<template>
  <div class="order-management-container">
    <h1>订单管理</h1>
    
    <!-- 订单列表 -->
    <div class="orders-list">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部订单" name="all">
          <el-table :data="filteredOrders" style="width: 100%" stripe v-if="shopInfo">
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
            <el-table-column label="操作" width="300">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
                <el-button v-if="scope.row.status === 2" type="primary" size="small" @click="handleShipOrder(scope.row.id)">发货</el-button>
                <el-button v-if="scope.row.status === 3" type="success" size="small" @click="handleReceivePayment(scope.row.id)">完成</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="店铺不存在" />
        </el-tab-pane>
        <el-tab-pane label="待发货" name="pending_shipment">
          <el-table :data="filteredOrders" style="width: 100%" stripe v-if="shopInfo">
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
            <el-table-column label="操作" width="300">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
                <el-button v-if="scope.row.status === 2" type="primary" size="small" @click="handleShipOrder(scope.row.id)">发货</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="店铺不存在" />
        </el-tab-pane>
        <el-tab-pane label="待收货" name="pending_receive">
          <el-table :data="filteredOrders" style="width: 100%" stripe v-if="shopInfo">
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
            <el-table-column label="操作" width="300">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
                <el-button v-if="scope.row.status === 3" type="success" size="small" @click="handleReceivePayment(scope.row.id)">完成</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="店铺不存在" />
        </el-tab-pane>
        <el-tab-pane label="已完成" name="completed">
          <el-table :data="filteredOrders" style="width: 100%" stripe v-if="shopInfo">
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
            <el-table-column label="操作" width="300">
              <template #default="scope">
                <el-button type="info" size="small" @click="viewOrderDetail(scope.row.id)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="店铺不存在" />
        </el-tab-pane>
      </el-tabs>
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
          <el-button v-if="orderDetail && orderDetail.status === 2" type="primary" @click="handleShipOrder(orderDetail.id)">发货</el-button>
          <el-button v-if="orderDetail && orderDetail.status === 3" type="success" @click="handleReceivePayment(orderDetail.id)">完成</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  getShopInfo,
  getShopOrders,
  getOrderDetail,
  shipOrder,
  receivePayment
} from '@/api'

// 状态管理
const activeTab = ref('all')
const orders = ref([])
const filteredOrders = ref([])
const dialogVisible = ref(false)
const orderDetail = ref(null)
const shopInfo = ref(null)

// 生命周期钩子
onMounted(() => {
  fetchShopInfo()
})

// 获取店铺信息
const fetchShopInfo = async () => {
  try {
    const userStr = localStorage.getItem('userInfo')
    let userInfo
    
    if (userStr) {
      userInfo = JSON.parse(userStr)
    } else {
      // 如果没有用户信息，手动模拟一个用户ID为6的卖家信息用于测试
      console.log('没有从localStorage获取到用户信息，使用模拟数据')
      userInfo = {
        id: 6,
        username: 'newseller001',
        nickname: '新测试卖家'
      }
    }
    
    const response = await getShopInfo(userInfo.id)
    console.log('GetShopInfo response:', response)
    shopInfo.value = response.data
    console.log('ShopInfo:', shopInfo.value)
    if (shopInfo.value) {
      fetchOrders()
    }
  } catch (error) {
    if (error.message && error.message.includes('店铺不存在')) {
      shopInfo.value = null
      ElMessage.warning('您还没有创建店铺，请先创建店铺')
    } else {
      ElMessage.error('获取店铺信息失败')
      console.error('获取店铺信息失败:', error)
    }
  }
}

// 获取订单列表
const fetchOrders = async () => {
  try {
    if (!shopInfo.value) return
    
    const response = await getShopOrders({ shopId: shopInfo.value.id })
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
      'pending_shipment': 2,
      'pending_receive': 3,
      'completed': 4
    }
    const status = statusMap[tabName]
    filteredOrders.value = status ? orders.value.filter(order => order.status === status) : []
  }
}

// 查看订单详情
const viewOrderDetail = async (orderId) => {
  try {
    const response = await getOrderDetail(orderId)
    orderDetail.value = response.data
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取订单详情失败')
    console.error('获取订单详情失败:', error)
  }
}

// 处理发货操作
const handleShipOrder = async (orderId) => {
  try {
    const numericOrderId = Number(orderId)
    await shipOrder(numericOrderId, shopInfo.value.id)
    ElMessage.success('发货成功')
    fetchOrders()
    if (orderDetail.value && orderDetail.value.id === numericOrderId) {
      orderDetail.value.status = 3 // 待收货
    }
  } catch (error) {
    ElMessage.error('发货失败')
    console.error('发货失败:', error)
  }
}

// 处理收款操作
const handleReceivePayment = async (orderId) => {
  try {
    const numericOrderId = Number(orderId)
    await receivePayment(numericOrderId, shopInfo.value.id)
    ElMessage.success('收款成功')
    fetchOrders()
    if (orderDetail.value && orderDetail.value.id === numericOrderId) {
      orderDetail.value.status = 4 // 已完成
    }
  } catch (error) {
    ElMessage.error('收款失败')
    console.error('收款失败:', error)
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
</style>