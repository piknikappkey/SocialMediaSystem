<template>
  <div class="content">
    <div class="forums">
      <RouterLink
        v-for="forum in forums"
        :to="{
          path: '/home/forumView',
          query: {
            userId: forum.userId,
            forumId: forum.forumId,
            forumTitle: forum.forumTitle,
            forumText: forum.forumText,
            forumFeature: forum.forumFeature,
            forumContentUrl: forum.forumContentUrl,
            createdTime: forum.createdTime,
          },
        }"
        replace
      >
        <ForumBox class="forum_box" :forum="forum" />
      </RouterLink>
    </div>
    <!-- 用于检测是否到达底部 -->
    <div ref="isBottom" class="isBottom">
      <p class="isBottom-tips">我可是有底线的</p>
      <hr class="isBottom-line" width="50%" />
    </div>
    <RouterView></RouterView>
  </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import ForumBox from '@/components/ForumBox.vue'
import useGetForums from '@/hooks/useGetForums'
const { getForums } = useGetForums() // 获取论坛数据函数
const forums = ref([]) // 论坛数据列表
const isBottom = ref(null)
const getSize = 20 // 每次请求获取的论坛数据量
// 观察是否到达页面底部
const updateForums = async function (entries, observer) {
  entries.forEach(async (entry) => {
    if (!entry.isIntersecting) return
    // 如果观察到底部元素
    await getForums(forums.value, forums.value.length, getSize * 2)
  })
}
// 交叉观察者
const bottomObserve = new IntersectionObserver(updateForums, {
  root: null,
  threshold: 0,
})
// 开始观察
onMounted(() => bottomObserve.observe(isBottom.value))
// 结束观察
onBeforeUnmount(() => {
  if (isBottom.value) bottomObserve.unobserve(isBottom.value)
})
</script>

<style scoped lang="scss">
.content {
  position: relative;
  text-align: center;
  padding: 25px 40px 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  @media only screen and (max-width: 768px) {
    padding: 25px 20px 0;
  }
}

.content::-webkit-scrollbar {
  width: 6px;
}

/*定义滚动条轨道 内阴影+圆角*/
.content::-webkit-scrollbar-track {
  border-radius: 10px;
}

/*定义滑块 内阴影+圆角*/
.content::-webkit-scrollbar-thumb {
  border-radius: 10px;
  border: 2px solid $main-color-llight;
}

.forums {
  overflow: auto;
}

.forum_box {
  display: inline-block;
  margin: 10px 15px 0;
  width: calc((100% - 15px * 10) / 5);
  height: 300px;
  overflow: hidden;
  transition: all 0.3s;
  @media only screen and (max-width: 1440px) {
    margin: 10px 10px 0;
    width: calc((100% - 10px * 8) / 4);
    height: 300px;
  }
  @media only screen and (max-width: 1024px) {
    margin: 10px 10px 0;
    width: calc((100% - 10px * 6) / 3);
    height: 300px;
  }
  @media only screen and (max-width: 768px) {
    margin: 10px 5px 0;
    width: calc((100% - 5px * 4) / 2);
    height: 300px;
  }
}

.isBottom {
  position: relative;
  background-color: transparent;
  margin-top: -520px;
  z-index: -1;
  height: 600px;
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
