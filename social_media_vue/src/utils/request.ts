import axios from 'axios'
import { ref } from 'vue'
import { useChatStore } from '@/store/chatData'
import { storeToRefs } from 'pinia'

const baseUrl = 'http://10.80.13.39:8080'
const wsUrl = 'ws://' + baseUrl.split('//')[1]

const service = axios.create({
  baseURL: baseUrl,
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 获取/更新cookie
service.get('/set_cookie')

// WebSocket
let ws = ref(null)
// 初始化
function WebSocketInit(userId: Number) {
  WebSocketOnClose(userId)
  ws.value = new WebSocket(`${wsUrl}/chat/${userId}`)
  ws.value.onopen = () => {
    console.log('WebSocket连接已建立')
  }
  // 接收消息
  const { onlineFriends, friendMessages } = storeToRefs(useChatStore())
  ws.value.onmessage = (event) => {
    const msg = JSON.parse(event.data)
    switch (msg?.type) {
      case 'online': // 好友新上线消息
        onlineFriends.value.push(msg.data)
        console.log('好友上线', msg.data)
        console.log(onlineFriends.value)
        break
      case 'onlineFriends': // 已上线的好友消息
        onlineFriends.value = msg.data
        console.log('已上线好友', msg.data)
        break
      case 'message': // 通讯消息
        console.log(msg)
        friendMessages.value.push(msg)
        console.log(friendMessages.value)
        break
      case 'outline': // 下线消息
        onlineFriends.value = onlineFriends.value.filter(
          (item) => item.addresseeId != msg.data.addresseeId,
        )
        console.log('好友下线', msg.data)
        console.log(onlineFriends.value)
        break
      default:
        console.log('error msg: ' + msg)
        break
    }
  }

  ws.value.onerror = (error) => {
    console.error('WebSocket错误:', error)
  }
}
// 发送消息
function sendMessages(msg) {
  if (ws.value && ws.value.readyState === WebSocket.OPEN) {
    ws.value.send(JSON.stringify(msg)) // 发送 JSON 格式数据
    return
  }
  console.warn('WebSocket 未连接，消息发送失败')
}

// 断开链接
function WebSocketOnClose(userId: Number) {
  if (ws.value?.readyState === WebSocket.OPEN) {
    ws.value.close(1000, 'Component unmounted')
    console.log('关闭WebSocket链接')
  }
}

export { service, baseUrl, WebSocketInit, WebSocketOnClose, ws, sendMessages }
