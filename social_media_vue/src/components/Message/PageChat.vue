<template>
  <div class="chat">
    <div class="chat-left">
      <div class="chat-search">
        <input
          ref="inputSearch"
          class="chat-search-input"
          type="text"
          placeholder="查找好友或用户"
          @input="search"
        />
        <button class="chat-search-btn" @click="search">搜索</button>
      </div>
      <!-- 搜索好友结果 -->
      <SearchUser
        v-for="user in searchUserData"
        :user="user"
        :myUserId="userData.userId"
        :friendData="friendData"
      />
      <!-- 分割线 -->
      <div class="chat-search-fenge" v-if="inputSearch?.value && searchUserData.length > 0">
        <div class="chat-search-fenge-tip">以上为搜索到的用户</div>
      </div>
      <!-- 搜索好友结果 -->
      <ChatList
        :friendData="sea"
        v-for="sea in searchData"
        v-model:chatFriend="chatFriend"
        v-model:chatMessages="chatMessages"
        v-model:loadContent="loadContent"
        radioId="sea"
      />
      <!-- 分割线 -->
      <div class="chat-search-fenge" v-if="inputSearch?.value && searchData.length > 0">
        <div class="chat-search-fenge-tip">以上为搜索到的好友</div>
      </div>
      <!-- 分割线 -->
      <div
        class="chat-search-fenge"
        v-if="inputSearch?.value && searchData.length == 0 && searchUserData.length == 0"
      >
        <div class="chat-search-fenge-tip">没有符合要求的用户</div>
      </div>
      <!-- 好友列表 -->
      <ChatList
        :friendData="fri"
        v-for="fri in friendData"
        v-model:chatFriend="chatFriend"
        v-model:chatMessages="chatMessages"
        v-model:loadContent="loadContent"
        radioId="fri"
      />
    </div>
    <!-- 右侧聊天窗 -->
    <div class="chat-box">
      <div class="chat-main" v-if="chatFriend">
        <!-- 上方标题框 -->
        <div class="chat-main-head">
          <div class="chat-main-head-username">{{ chatFriend.addresseeUser.username }}</div>
          <img class="chat-main-head-other" src="/images/Message/other.png" alt="" />
        </div>
        <!-- 聊天内容框 -->
        <div class="chat-main-messagebox" ref="chatMessageBox">
          <div class="chat-main-messagebox-head" ref="chatMessageBoxHead">
            <span>{{ loadContent }}</span>
            <hr />
          </div>
          <div
            class="chat-main-message"
            v-for="(msg, $index) in chatMessages"
            :key="$index"
            ref="chatMessagesRef"
          >
            <AvatarBox
              class="chat-main-message-avatar"
              :avatar-url="
                msg.senderId === chatFriend.requesterId
                  ? userData.avatarUrl
                  : chatFriend.addresseeUser.avatarUrl
              "
              :style="msg.senderId === chatFriend.requesterId ? `right:10px` : `left:10px`"
            />
            <div
              class="chat-main-message-content"
              v-if="msg.messageType == 'text'"
              :style="msg.senderId === chatFriend.requesterId ? `float:right` : `float:left`"
            >
              {{ msg.content }}
            </div>
            <div
              class="chat-main-message-time"
              :style="msg.senderId === chatFriend.requesterId ? `right:90px` : `left:90px`"
            >
              {{ getTime(msg.createdTime) }}
            </div>
          </div>
        </div>
        <!-- 聊天输入框 -->
        <PageChatInput
          class="chat-main-inputbox"
          :chatFriend="chatFriend"
          v-model:chatMessages="chatMessages"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, ref, watch, watchEffect } from 'vue'
import { service } from '@/utils/request'
import { ElMessage } from 'element-plus'
import AvatarBox from '../AvatarBox.vue'
import useGetTime from '@/hooks/useGetTime'
import { useUserStore } from '@/store/userData'
import { storeToRefs } from 'pinia'
import PageChatInput from './PageChatInput.vue'
import ChatList from './ChatList.vue'
import SearchUser from './SearchUser.vue'
import { useChatStore } from '@/store/chatData'
const chatFriend = ref() // 正在聊天的好友
const chatMessages = ref([]) // 消息列表
const inputSearch = ref(null) // 搜索好友输入框
const searchData = ref([]) // 搜索结果
const searchUserData = ref([]) // 搜索用户结果
const friendData = ref([]) // 好友数据
const { userData } = storeToRefs(useUserStore()) // 用户数据
const { getTime } = useGetTime() // 时间转换函数
const chatMessageBox = ref(null) // 聊天消息盒子
const chatMessagesRef = ref(null) // 聊天消息列表
const chatMessageBoxHead = ref(null) // 用于获取更多聊天消息
const loadContent = ref('正在加载中') // 消息加载提示
let firstMessagesTop = 0 // 用于平滑处理消息刷新机制
let getMessagesSize = 20 // 获取消息数
const { onlineFriends, friendMessages } = storeToRefs(useChatStore()) // 已上线用户数据
// 使用搜索框输入内容或点击搜索按钮
function search() {
  searchData.value = []
  searchUserData.value = []
  // 输入框内容为空则退出
  if (!inputSearch.value.value) return

  // 如果好友列表不为空
  if (friendData.value.length != 0) {
    friendData.value.forEach((f) => {
      if (
        f.addresseeUser.username.search(inputSearch.value.value) != -1 ||
        f.addresseeUser.email.search(inputSearch.value.value) != -1
      ) {
        searchData.value.push(f)
        return
      }
    })
  }
  searchUser()
}

// 搜索用户
function searchUser() {
  service
    .get(
      `/friendSearches?searchValue=${inputSearch.value.value}&start=${searchUserData.value.length}&size=10&userId=${userData.value.userId}`,
    )
    .then((res) => {
      if (res.data.state !== 1) return
      searchUserData.value = res.data.data
    })
    .catch((err) => {
      console.log(err)
    })
}

// 标记上线用户
function friendIsOnline() {
  if (onlineFriends.value.length == 0) {
    friendData.value.map((fri) => {
      fri.isOnline = false
    })
    friendSoft()
    return
  }
  friendData.value.map((fri) => {
    onlineFriends.value.forEach((online) => {
      if (online.friendshipId === fri.friendshipId) {
        fri.isOnline = true
      } else {
        fri.isOnline = false
      }
    })
  })
  friendSoft()
}

// 用户根据活跃时间与是否上线情况排序
function friendSoft() {
  friendData.value.sort((a, b) => {
    if (a.isOnline && !b.isOnline) return -1
    if (!a.isOnline && b.isOnline) return 1
    const aDate = new Date(a.updatedTime)
    const bDate = new Date(b.updatedTime)
    return bDate - aDate
  })
}

// 接收在线好友发出的消息
function getOnlineMessages() {
  if (friendMessages.value.length == 0) return
  friendData.value.map((fri) => {
    friendMessages.value.forEach((msg) => {
      // 找到发送消息的好友
      if (fri.addresseeId != msg.data.senderId) return
      // 判断是否在与这名好友对话
      if (chatFriend.value?.addresseeId != fri.addresseeId) {
        // 否
        fri.notReadMessages += 1
        return
      }
      // 是，发送重置对话信息给服务器，添加消息到消息列表
      service.post(`/notReadMessages?requesterId=${fri.requesterId}&addresseeId=${fri.addresseeId}`)
      chatMessages.value.push(msg.data)
    })
  })
  friendMessages.value.length = 0
}

// 获取好友数据
function getFriendData() {
  service
    .get(`/friends?userId=${userData.value.userId}`)
    .then((res) => {
      if (res.data.state !== 1) return
      // console.log(res.data.data)
      friendData.value = res.data.data
      // 标记上线用户
      watchEffect(friendIsOnline)
      // 观察好友发送过来的消息
      friendMessages.value.length = 0
      watchEffect(getOnlineMessages)
    })
    .catch((err) => {
      ElMessage.error(err)
    })
}
getFriendData()

// 获取消息数据
function getMessages(fri, size) {
  service
    .get(
      `/messages?senderId=${fri.requesterId}&receiverId=${fri.addresseeId}&start=${chatMessages.value.length}&size=${size}`,
    )
    .then((res) => {
      if (res.data.state !== 1) return
      // console.log(res.data.data.reverse())
      chatMessages.value.unshift(...res.data.data.reverse())
      // 处理滚动提示
      if (getMessagesSize > chatMessages.value.length || res.data.data.length == 0) {
        loadContent.value = '已获取全部消息'
      }
      // 处理滚动
      if (chatMessages.value.length > getMessagesSize && res.data.data.length > 0) {
        firstMessagesTop =
          chatMessagesRef.value[chatMessagesRef.value.length - 1].getBoundingClientRect().y
      }
    })
    .catch((err) => {
      ElMessage.error(err)
    })
}

// 设置消息页面滚动到底部
function messageBoxBottom() {
  if (!chatMessageBox.value) return true
  if (!chatMessagesRef.value) return true
  if (
    chatMessagesRef.value.length > 20 ||
    chatMessageBox.value.scrollHeight -
      chatMessageBox.value.scrollTop -
      chatMessageBox.value.clientHeight >
      1000
  )
    return false
  chatMessageBox.value.scrollTop =
    chatMessageBox.value.scrollHeight - chatMessageBox.value.clientHeight
  return true
}

// 调整滚动条以适应更新的页面
function adjust() {
  chatMessageBox.value.scrollTop +=
    chatMessagesRef.value[chatMessagesRef.value.length - 1].getBoundingClientRect().y -
    firstMessagesTop
}

// chatMessages获取到数据时，如果数据量较少则代表获取到了新消息，将页面滚动到底部
// 如果获取的数据量大，则代表用户在浏览旧消息
watch(
  () => chatMessagesRef.value?.length,
  () => {
    if (messageBoxBottom()) return
    adjust()
  },
)

// 当用户滚动到messageBox最顶部时，获取消息
// 观察是否到达页面底部
const updateMessages = function (entries, observer) {
  entries.forEach((entry) => {
    // 如果观察到顶部元素，则获取消息
    if (entry.isIntersecting) getMessages(chatFriend.value, getMessagesSize)
  })
}
// 交叉观察者
const bottomObserve = new IntersectionObserver(updateMessages, {
  root: null,
  rootMargin: '200px 0px', // 提前200px触发
  threshold: 0,
})
// 开始观察
watch(chatFriend, () => {
  if (chatMessageBoxHead.value) {
    bottomObserve.unobserve(chatMessageBoxHead.value)
    bottomObserve.observe(chatMessageBoxHead.value)
    return
  }
  // 如果是第一次点击chatFriend，由于此时chatMessageBoxHead未加载，需要等待其加载完成后再开始观察
  const firstClickChat = watch(chatMessageBoxHead, () => {
    if (chatMessageBoxHead.value) {
      bottomObserve.observe(chatMessageBoxHead.value)
      return
    }
    firstClickChat()
  })
})
// 结束观察
onBeforeUnmount(() => {
  if (chatMessageBoxHead.value) bottomObserve.unobserve(chatMessageBoxHead.value)
})
</script>

<style scoped lang="scss">
@use 'sass:color';
.chat {
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 10px;
  overflow: hidden;
  &-left {
    float: left;
    width: 250px;
    height: 100%;
    padding: 15px 0;
    overflow: auto;
    text-align: center;
    border-right: 1px solid rgb(180, 180, 180);
    &::-webkit-scrollbar {
      display: none;
    }
  }
  &-search {
    &-input {
      color: $main-color;
      caret-color: $main-color-light;
      height: 27px;
      vertical-align: middle;
      border-radius: 5px;
      border: 1px solid color.adjust($main-color-llight, $lightness: -5%);
      padding: 0 2px;
      &:focus {
        outline: 1px solid color.adjust($main-color-light);
      }
    }
    &-btn {
      color: $main-color;
      padding: 2px 4px;
      height: 25px;
      vertical-align: middle;
      margin-left: 5px;
      box-shadow: 2px 2px 5px 1px rgba(160, 160, 160, 0.3);
      border-radius: 5px;
      border: 1px solid color.adjust($main-color-llight, $lightness: -5%);
      background-color: $main-color-lllight;
      &:hover {
        background-color: color.adjust($main-color-lllight, $lightness: -5%);
      }
      &:active {
        background-color: $main-color-lllight;
      }
    }
    &-fenge {
      margin-top: 25px;
      width: 100%;
      height: 0;
      border-bottom: 1px solid rgb(120, 120, 120);
      position: relative;
      &-tip {
        position: absolute;
        left: 50%;
        bottom: 0;
        transform: translate(-50%);
        font-size: 12px;
        color: rgb(82, 82, 82);
      }
    }
  }
  &-box {
    float: left;
    width: calc(100% - 250px);
    height: 100%;
  }
  &-main {
    width: 100%;
    height: 100%;
    position: relative;
    &-head {
      width: calc(100% - 250px);
      text-align: center;
      top: 0;
      position: fixed;
      border-bottom: 1px solid rgb(180, 180, 180);
      height: 40px;
      &-username {
        font-size: 20px;
        line-height: 40px;
        color: $main-color;
      }
      &-other {
        position: absolute;
        right: 8px;
        top: 3px;
        height: 24px;
        opacity: 0.6;
        &:hover {
          opacity: 1;
        }
      }
    }
    &-messagebox {
      padding: 20px 0 0 0;
      margin-top: 40px;
      height: calc(100% - 40px);
      overflow: auto;
      position: relative;
      &::-webkit-scrollbar {
        width: 6px;
      }
      &::-webkit-scrollbar-track {
        border-radius: 10px;
      }
      &::-webkit-scrollbar-thumb {
        border-radius: 10px;
        border: 2px solid $main-color-llight;
      }
      &-head {
        position: absolute;
        text-align: center;
        top: 0;
        height: 200px;
        left: 50%;
        transform: translate(-50%, 0);
        width: 100%;
        & hr {
          border: none;
          border-bottom: 1px dashed rgb(180, 180, 180);
        }
        & span {
          color: rgb(135, 135, 135);
          font-size: 12px;
        }
      }
    }
    &-message {
      width: 100%;
      padding: 20px;
      position: relative;
      overflow-y: auto;
      &-avatar {
        position: absolute;
        height: 32px;
        width: 32px;
      }
      &-content {
        word-wrap: break-word;
        word-break: break-all;
        margin: 0 60px;
      }
      &-time {
        font-size: 12px;
        position: absolute;
        bottom: 0px;
      }
      &:first-child {
        margin-top: 100px;
      }
      &:last-child {
        margin-bottom: 120px;
      }
    }
    &-inputbox {
      position: fixed;
      width: calc(100% - 262px);
      bottom: 6px;
      margin-left: 6px;
    }
  }
}
</style>
