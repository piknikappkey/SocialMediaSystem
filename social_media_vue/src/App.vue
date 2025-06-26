<template>
  <div class="left-box">
    <div class="title-main">
      <h1>社交媒体</h1>
    </div>
    <div class="navigation">
      <OptionalBtn
        class="navigation-btn"
        v-for="r in routeArr"
        :key="r.id"
        :returnValue="r.routePath"
        :title="r.title"
        name="nav-btn"
        v-model="navPath"
      />
    </div>
  </div>
  <div class="right-box">
    <RouterView v-model:navpath="navPath"></RouterView>
  </div>
</template>

<script setup lang="ts">
import OptionalBtn from './components/OptionalBtn.vue'
import { RouterView } from 'vue-router'
import useGetUserData from './hooks/useGetUserData'
import { onMounted, onUnmounted, watch } from 'vue'
import router from './router'
import { useNavStory } from './store/navPath'
import { storeToRefs } from 'pinia'

const { navPath } = storeToRefs(useNavStory()) // 导航数据

// 路由数组
const routeArr = [
  { id: 1, title: '首页', routePath: '/home' },
  { id: 2, title: '分享', routePath: '/issue' },
  { id: 3, title: '消息', routePath: '/message' },
  { id: 4, title: '我的', routePath: '/me' },
]
navPath.value = routeArr[0].routePath
router.push(navPath.value)
// 当导航数据改变时进行导航
watch(navPath, (newValue) => {
  router.push(newValue)
})

// 获取用户数据（根据浏览器cookie）
const { initUserData, setupVisibilityListener } = useGetUserData()
initUserData()

// 监听用户是否选中窗口，选中时保存用户数据
onMounted(() => {
  const removeListener = setupVisibilityListener()
  onUnmounted(() => removeListener && removeListener())
})
</script>

<style scoped lang="scss">
.title-main {
  font-family: '华文彩云';
  text-align: center;
  font-weight: 400;
  letter-spacing: 1px;
  font-size: 16px;
  -webkit-text-stroke: 1px $main-color;
  margin: 35px 5px 0px;
}
.left-box {
  float: left;
  width: 100px;
}
.navigation {
  margin-top: 30px;
  text-align: center;
  &-btn {
    margin: 20px 0;
  }
}

.right-box {
  float: left;
  position: relative;
  width: calc(100% - 100px);
  height: 100vh;
  /* border: 2px solid var(--font-black-color-light); */
  box-shadow: 2px 2px 10px 1px rgba(73, 73, 73, 0.3);
  border-radius: 10px;
  overflow: auto;
  background-color: rgba(255, 255, 255, 0.5);
}
</style>
