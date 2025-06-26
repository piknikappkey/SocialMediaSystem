<template>
  <div class="comment">
    <div class="comment-title">评论区</div>
    <!-- 主评论 -->
    <div v-if="userData.logginStatus && commentData.length > 0" class="list">
      <div class="list-comment" v-for="com in commentData">
        <AvatarBox class="list-comment-avatar" :avatarUrl="com?.avatarUrl" />
        <div class="list-comment-username">{{ com.userName }}</div>
        <div class="list-comment-content">{{ com.comment }}</div>
        <div>
          <div class="list-comment-time">{{ com.createdTime }}</div>
          <div class="list-comment-like">
            <ClickImg
              :srcs="['/images/ForumView/comment/爱心.png', '/images/ForumView/comment/爱心1.png']"
              class="list-comment-like-img"
            />
            {{ com.likeNum }}
          </div>
          <div class="list-comment-reply" @click="replyClick(com)">回复</div>
        </div>
        <!-- 子评论 -->
        <div
          v-if="com?.child && com.child.length > 0 && com.showChild !== 0"
          v-for="(comChild, index) in com.child.slice(0, com.showChild)"
          :key="index"
          class="list-comment-child"
        >
          <AvatarBox class="list-comment-avatar" :avatarUrl="comChild.avatarUrl" />
          <div class="list-comment-username">
            {{ comChild.userName }}{{ comChild.toUserName && ` => ${comChild.toUserName}` }}
          </div>
          <div class="list-comment-content">{{ comChild.comment }}</div>
          <div class="list-comment-time">{{ comChild.createdTime }}</div>
          <div class="list-comment-like">
            <ClickImg
              :srcs="['/images/ForumView/comment/爱心.png', '/images/ForumView/comment/爱心1.png']"
              class="list-comment-like-img"
            />
            {{ comChild.likeNum }}
          </div>
          <div class="list-comment-reply" @click="replyClick(comChild)">回复</div>
        </div>
        <!-- 显示、收起评论按钮 -->
        <div
          class="child-btn"
          v-if="com.haveChild > 0 && com.child.length < com.haveChild"
          @click="getChildComment(com)"
        >
          显示{{ com.haveChild - (com?.child.length && 0) }}条评论&darr;
        </div>
        <div class="child-btn" v-if="com?.child && com.child.length > 0" @click="hiddenChild(com)">
          收起&uarr;
        </div>
      </div>
      <!-- 用于检测是否到达底部 -->
      <div ref="isBottom" class="isBottom">
        <p class="isBottom-tips">我可是有底线的</p>
        <hr class="isBottom-line" width="50%" />
      </div>
    </div>
    <!-- 发送、回复评论区域 -->
    <div class="comment-box">
      <div v-if="replyObject" class="comment-box-reply">
        发送给 => {{ replyObject.userName }}
        <img class="comment-box-reply-no" @click="replyNoClick" src="/images/no-main.png" alt="" />
      </div>
      <textarea
        ref="textareaRef"
        v-model="content"
        class="comment-box-input"
        rows="1"
        @input="adjustHeight"
        maxlength="2048"
        placeholder="留下你的精彩评论吧"
      ></textarea>
      <Button class="comment-box-btn" title="发送" @click="sendComment" />
    </div>
    <div v-if="userData.logginStatus && commentData.length == 0" class="list-none">空空如也</div>
    <Button
      v-if="!userData.logginStatus"
      class="log-tips"
      title="登录后即可查看评论"
      @click="logClick"
    />
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/store/userData'
import Button from '../Button.vue'
import AvatarBox from '../AvatarBox.vue'
import ClickImg from './ClickImg.vue'
import { storeToRefs } from 'pinia'
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { service } from '@/utils/request'
import { ElMessage } from 'element-plus'
import useGetTime from '@/hooks/useGetTime'
import { useLogStore } from '@/store/log'
import { useNavStory } from '@/store/navPath'
const { logData } = storeToRefs(useLogStore()) // 登录数据
const { navPath } = storeToRefs(useNavStory()) // 导航数据
const { getTime } = useGetTime() // 显示时间用
const { userData } = storeToRefs(useUserStore()) // 用户数据
const content = ref('')
const textareaRef = ref(null)
const replyObject = ref(null)
const { forumId } = defineProps(['forumId'])
const commentData = ref([])

// 点击登录按钮
function logClick() {
  navPath.value = '/me'
  logData.value.showLog = true
}

// 收起评论
function hiddenChild(com) {
  const delIndex = Math.floor(com.child.length / 2)
  const delLength = com.child.length - delIndex
  if (com.child.length > 10) com.child.splice(delIndex, delLength)
  else com.child = []
}
// 回复评论
function replyClick(com) {
  replyObject.value = com
}
// 取消回复评论
function replyNoClick() {
  replyObject.value = null
}
// 发送评论
function sendComment() {
  if (!content.value) return
  const forumComment = {
    commentId: null,
    forumId: forumId,
    fatherId: replyObject.value?.fatherId || replyObject.value?.commentId,
    haveChild: 0,
    userId: userData.value.userId,
    toUserId: replyObject.value?.userId,
    likeNum: 0,
    comment: content.value,
    comImgUrl: null,
  }
  service
    .post('/comments', forumComment)
    .then((res) => {
      if (res.data.state === 0) {
        ElMessage.error(res.data.content)
        return
      }
      content.value = ''
      replyObject.value = null
      adjustHeight()
      // 调整数据
      commentData.value = []
      getFatherComment()
    })
    .catch((err) => ElMessage.error(err))
}

// 获取父评论
const isBottom = ref(null)
function getFatherComment() {
  service
    .get(
      `/comments?forumId=${forumId}&start=${commentData.value.length}&size=${20}&type=Father&userId=${userData.value.userId || 0}`,
    )
    .then((res) => {
      if (res.data.state === 0) {
        ElMessage.error(res.data.content)
        return
      }
      res.data.data.map((com) => {
        com.createdTime = getTime(new Date(com.createdTime))
        if (com.haveChild > 0) {
          // 如果有子评论，则添加一个空数组
          com.child = []
        }
      })
      commentData.value.push(...res.data.data)
    })
    .catch((err) => ElMessage.error(err))
}
// 观察是否到达页面底部
const updateForums = function (entries, observer) {
  entries.forEach((entry) => {
    // 如果观察到底部元素
    if (entry.isIntersecting) getFatherComment()
  })
}
// 交叉观察者
const bottomObserve = new IntersectionObserver(updateForums, {
  root: null,
  rootMargin: '0px 0px 400px 0px', // 提前400px触发
  threshold: 0,
})
// 开始观察
const watchIsBottom = watch(isBottom, () => {
  if (isBottom.value) {
    bottomObserve.observe(isBottom.value)
    watchIsBottom()
  }
})
getFatherComment()
// 结束观察
onBeforeUnmount(() => {
  if (isBottom.value) bottomObserve.unobserve(isBottom.value)
})

// 获取子评论
function getChildComment(com) {
  service
    .get(
      `/comments?forumId=${forumId}&start=${com.child.length}&size=${10}&type=Child&fatherCommentId=${com.commentId}&userId=${userData.value.userId}`,
    )
    .then((res) => {
      if (res.data.state === 0) {
        ElMessage.error(res.data.content)
        return
      }
      res.data.data.map((com) => {
        com.createdTime = getTime(new Date(com.createdTime))
      })
      com.child.push(...res.data.data)
      console.log(com.child)
    })
    .catch((err) => ElMessage.error(err))
}

// 自动调整输入框高度
// 动态调整高度函数
const adjustHeight = () => {
  const textarea = textareaRef.value
  textarea.style.height = 'auto' // 重置高度以获取正确scrollHeight
  textarea.style.height = `${textarea.scrollHeight + 2}px`
}
// 初始设置
onMounted(() => {
  textareaRef.value.style.minHeight = '40px' // 设置最小高度
})
</script>

<style scoped lang="scss">
.comment {
  position: relative;
  text-align: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.126);
  padding: 10px 0;
  margin-bottom: 40px;
  width: 100%;
  &-title {
    color: rgb(40, 40, 40);
    text-align: left;
  }
}
.log-tips {
}

.list {
  &-none {
    color: rgb(40, 40, 40);
    margin: 15px 0;
  }
  &-comment {
    width: 100%;
    text-align: left;
    margin-top: 15px;
    &-avatar {
      width: 35px;
      height: 35px;
      margin-right: 5px;
    }
    &-username {
      font-size: 15px;
      color: rgb(19, 19, 19);
    }
    &-content {
      font-size: 16px;
      margin-left: 12px;
    }
    &-time {
      font-size: 14px;
      color: rgb(35, 35, 35);
      display: inline-block;
    }
    &-like {
      margin-left: 15px;
      display: inline-block;
      font-size: 14px;
      color: rgb(35, 35, 35);
      &-img {
        width: 13px;
        height: 13px;
      }
    }
    &-reply {
      margin-left: 15px;
      display: inline-block;
      font-size: 14px;
      cursor: pointer;
      color: #808080;
      &:hover {
        color: #000;
      }
    }
    &-child {
      margin-top: 15px;
      margin-left: 15px;
      width: calc(100% - 15px);
    }
  }
}
.child-btn {
  margin-left: 5px;
  font-size: 12px;
  line-height: 12px;
  color: rgb(94, 94, 94);
  cursor: pointer;
  display: inline-block;
  &:hover {
    color: rgb(17, 17, 17);
  }
}

.comment-box {
  position: fixed;
  width: calc(30% - 40px);
  bottom: 10px;
  background-color: rgba($color: $main-color-lllight, $alpha: 1);
  border-radius: 5px;
  &-reply {
    padding: 5px 8px;
    color: #4f4f4f;
    font-size: 14px;
    vertical-align: middle;
    &:hover {
      color: #0d0d0d;
    }
    &-no {
      vertical-align: middle;
      display: inline-block;
      cursor: pointer;
      height: 14px;
      width: 14px;
      margin-left: 5px;
      opacity: 0.7;
      &:hover {
        opacity: 1;
      }
    }
  }
  &-input {
    vertical-align: middle;
    width: calc(100% - 70px);
    padding: 8px;
    resize: none; /* 禁用手动调整 */
    // white-space: nowrap; /* 默认不换行 */
    overflow-y: hidden; /* 隐藏垂直滚动条 */
    outline: none;
    max-height: 300px; /* 限制最大高度 */
    border: 1px solid $main-color-llight;
    border-radius: 5px;
    color: $main-color;
    line-height: 18px; /* 需与字体大小匹配 */
    font-size: 18px;
    &:focus,
    &:not(:placeholder-shown) {
      white-space: pre-wrap;
      overflow-y: auto;
      border: 1px solid #bf9dff;
    }
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
  }
  &-btn {
    vertical-align: middle;
    margin: 0 10px;
    color: $main-color;
    padding: 5px 10px;
  }
}
.isBottom {
  position: relative;
  background-color: transparent;
  height: 50px;
  color: rgba($color: #5252527b, $alpha: 1);
  &-tips {
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
  }
  &-line {
    position: absolute;
    width: 100%;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
  }
}
</style>
