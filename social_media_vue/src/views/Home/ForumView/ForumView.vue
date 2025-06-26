<template>
  <div ref="forumView" class="forum-view" @click="closeView"></div>
  <div ref="viewBox" class="view-box">
    <div class="view-box-left">
      <MediaBox :files="files" />
    </div>
    <div class="view-box-right">
      <div class="view-box-right-user">
        <AvatarBox class="view-box-right-user-avatar" :avatarUrl="userData?.avatarUrl && 'none'" />
        <h3 class="view-box-right-user-name">{{ userData?.username }}</h3>
      </div>
      <div class="view-box-right-title">{{ query.forumTitle }}</div>
      <div class="view-box-right-content">{{ query.forumText }}</div>
      <div class="view-box-right-time">{{ query.createdTime }}</div>
      <hr class="view-box-right-line" />
      <Comment :forumId="query.forumId" />
    </div>
  </div>
  <div ref="viewClose" class="view-close-box" @click="closeView">
    <div class="view-close-box-back"></div>
    <div class="view-close-box-button">&times;</div>
  </div>
</template>

<script setup lang="ts">
import router from '@/router'
import { service, baseUrl } from '@/utils/request'
import { onMounted, ref, toRefs } from 'vue'
import { useRoute } from 'vue-router'
import MediaBox from '@/components/MediaBox.vue'
import { ElMessage } from 'element-plus'
import AvatarBox from '@/components/AvatarBox.vue'
import useGetTime from '@/hooks/useGetTime'
import Comment from '@/components/Home/Comment.vue'
// 点击链接显示详情页面
const forumView = ref()
const viewBox = ref()
const viewClose = ref()
const { query } = toRefs(useRoute()) // forum数据
const files = ref([]) // 文件数据
const userData = ref() // 用户数据

// 返回动态的时间
const { getTime } = useGetTime()
query.value.createdTime = getTime(new Date(query.value.createdTime))

// 获取用户数据
service
  .get(`/users?userId=${query.value.userId}`)
  .then((res) => {
    if (res.data.state == 0) {
      ElMessage.error(res.data.content)
      return
    }
    userData.value = res.data.data
  })
  .catch((err) => {
    console.log(err)
    ElMessage.error('错误！ ' + err)
  })

// 判断文件类型
function audioType(fileName: String) {
  const last = fileName.split('.')
  const videoType = ['mp4', 'mov', 'm4v']
  return videoType.indexOf(last[last.length - 1]) === -1 ? 'image' : 'video'
}

// 获取文件具体路径
service
  .get(`/paths?path=${query.value.forumContentUrl}`)
  .then((res) => {
    if (res.data.state !== 1) return
    files.value = res.data.data.map((file, i) => {
      return {
        id: i,
        name: `${baseUrl}/${query.value.forumContentUrl}/${file}`,
        type: audioType(`${baseUrl}/${query.value.forumContentUrl}/${file}`),
      }
    })
  })
  .catch((err) => console.log(err))

// 挂载后显示
onMounted(() =>
  setTimeout(() => {
    forumView.value.classList.add('forum-view-show')
    viewBox.value.classList.add('view-box-show')
    viewClose.value.classList.add('view-close-box-show')
  }, 10),
)

// 关闭页面
function closeView() {
  forumView.value.classList.remove('forum-view-show')
  viewBox.value.classList.remove('view-box-show')
  viewClose.value.classList.remove('view-close-box-show')
  setTimeout(() => {
    router.replace('/home')
  }, 300)
}
</script>

<style scoped lang="scss">
.forum-view {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100vw;
  height: 100vh;
  //   display: none;
  opacity: 0;
  z-index: -1;
  transition: all 0.3s;
  &-show {
    // display: block;
    opacity: 1;
    background-color: rgba(0, 0, 0, 0.25);
    filter: blur(25px);
    z-index: 10;
  }
}

.view-box {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) scale(10%);
  opacity: 0.2;
  z-index: 11;
  max-width: 1400px;
  width: 80vw;
  height: 90vh;
  background-color: aliceblue;
  border-radius: 20px 2px;
  box-shadow: 3px 3px 10px 10px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  transition: all 0.3s;
  &-show {
    transform: translate(-50%, -50%) scale(1);
    opacity: 1;
  }
  &-left {
    float: left;
    width: 70%;
    height: 100%;
    background-color: rgb(239, 239, 239);
  }
  &-right {
    float: left;
    width: 30%;
    height: 100%;
    position: relative;
    background-color: white;
    padding: 0 20px;
    overflow: auto;
    text-align: left;
    &::-webkit-scrollbar {
      display: none;
    }
    &-user {
      margin-top: 20px;
      width: 100%;
      text-align: left;
      padding-bottom: 4px;
      &-avatar {
        display: inline-block;
        height: 40px;
        width: 40px;
      }
      &-name {
        display: inline-block;
        margin-left: 10px;
        font-size: 16px;
        line-height: 40px;
        color: $font-black-color-light;
      }
    }
    &-title {
      margin-top: 20px;
      font-size: 18px;
      font-weight: 600;
    }
    &-content {
      margin-top: 10px;
      font-size: 16px;
      font-weight: 400;
    }
    &-time {
      margin-top: 10px;
      color: $font-black-color-light;
    }
    &-line {
      margin-top: 10px;
      border: none;
      border-top: 1px solid rgba(0, 0, 0, 0.126);
    }
  }
}

.view-close-box {
  position: fixed;
  top: 20px;
  left: 20px;
  height: 40px;
  width: 40px;
  border-radius: 50%;
  z-index: 10;
  overflow: hidden;
  opacity: 0;
  transition: all 0.3s;
  cursor: pointer;
  &:hover &-button {
    color: rgb(255, 255, 255);
  }
  &-show {
    opacity: 1;
  }
  &-button {
    user-select: none;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 20px;
    line-height: 20px;
    font-weight: 400;
    color: rgb(228, 228, 228);
  }
  &-back {
    position: absolute;
    top: 0;
    left: 0;
    height: 40px;
    width: 40px;
    border-radius: 50%;
    background-color: rgba(0, 0, 0, 0.5);
    filter: blur(15px);
  }
}
</style>
