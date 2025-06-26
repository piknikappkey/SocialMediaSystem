<template>
  <div class="media">
    <!-- 图片展示区域 -->
     <div ref="media" class="media-box">
      <div ref="mediaBox" class="media-box-content" v-for="file in files"         :key="file.id">
        <el-image
          v-if="file.type === 'image'"
          :src="file.name"
          :initial-index="file.id"
          fit="contain"
          class="media-box-content-image"
        >
          <template #error>
            <div class="error-tip">图片加载失败</div>
          </template>
        </el-image>

        <div class="media-box-content-video" v-if="file.type === 'video'">
          <!-- 视频播放器核心区域 -->
          <video 
            class="video"
            loop
            :src="file.name"
            @click="togglePlay(mediaBox, currentIndex, isPlaying)"
          ></video>
          <!-- 自定义进度条 -->
          <Slider class="video-slider" :max="duration" :useLength="videoTime" @set-videoTime="getVideoTime" />
        </div>
      </div>
     </div>
    
    <!-- 左侧切换按钮 -->
    <el-button
      @click="toLeft"
      class="nav-button left"
      circle
      v-if="files.length > 1"
    >
      <img src="/images/ForumView/left.png" alt="&#8678">
    </el-button>

    <!-- 右侧切换按钮 -->
    <el-button
      @click="toRight"
      class="nav-button right"
      circle
      v-if="files.length > 1"
    >
      <img src="/images/ForumView/right.png" alt="&#8680">
    </el-button>

    <!-- 左上角页面提示 -->
     <div class="tips">
      <span>{{ currentIndex + 1 }} / {{ files.length }}</span>
     </div>
  </div>
</template>
<script setup lang="ts">
import {  ref, defineProps, onUpdated, onBeforeUnmount, watch } from 'vue'
import Slider from './Slider.vue'
import useVideo from '@/hooks/useVideo'
const { files } = defineProps({
  files: { type: Object },
})
const currentIndex = ref(0)
const media = ref()
const mediaBox = ref()
let mediaIsReady = false

// 用于观察视频大小变化以自适应
let lastWidth = 0
const observer = new ResizeObserver(entries => {
    entries.forEach(entry => {
      const { height } = entry.contentRect
      if(lastWidth != media.value.offsetWidth) entry.target.style.height = 'auto'
      if(height > media.value.offsetHeight) {
        entry.target.style.height = '100%'
      }      
      lastWidth = media.value.offsetWidth
    })
  })


// 视频播放相关
const videoTime = ref(0)
const duration = ref(0)
const isPlaying = ref(false)
const { videoInit, togglePlay } = useVideo()

// 设置视频播放时间
function getVideoTime(time) {
  mediaBox.value[currentIndex.value].querySelector('.video').currentTime = time
}

// 进度更新
function updateProgress() {  
  if (files[currentIndex.value].type === 'video') {
    videoTime.value = Math.min(
      mediaBox.value[currentIndex.value].querySelector('.video').currentTime,
      duration.value || Infinity
    )
  }
}
// 获取视频播放总时长
function getDuration() {
  duration.value = this.duration
}

// 为当前播放的video添加进度更新并去除上一个video的进度更新
watch(currentIndex, ( newVal, oldVal) => {  
  if(files[oldVal].type === 'video') { // 移除旧视频的监听
    const oldVideo = mediaBox.value[oldVal].querySelector('video')
    oldVideo.removeEventListener('timeupdate', updateProgress)
    oldVideo.removeEventListener('canplay', getDuration)
  }
  if(files[newVal].type === 'video') { // 为新视频添加监听
    const newVideo = mediaBox.value[newVal].querySelector('video')
    newVideo.addEventListener('timeupdate', updateProgress)
    newVideo.addEventListener('canplay', getDuration)
  }
})

// 页面初始化
// onUpdated()
watch(mediaBox, () => {  
  if(mediaIsReady) return
  if(mediaBox.value.length !== files.length) return
  for(let i = 0; i < mediaBox.value.length; i++) {
    mediaBox.value[i].style.transform = `translate(${i * 100}%, -50%)`
    mediaBox.value[i].style.zIndex = `${100 - i}`
    if(files[i].type === 'video') {
      // 视频自适应
      observer.observe(mediaBox.value[i].querySelector('.media-box-content-video'), {box: 'border-box'})
      // 视频初始化
      videoInit(mediaBox.value, i, duration.value)
    }
  }

  // 如果第一个是视频，则自动播放并添加监听
  if(files[0].type === 'video') {
    togglePlay(mediaBox.value, currentIndex.value, isPlaying.value)
    mediaBox.value[0].querySelector('video').addEventListener('timeupdate', updateProgress)
    mediaBox.value[0].querySelector('video').addEventListener('canplay', getDuration)
  }
  mediaIsReady = true
})


// 控制mediaBox移动
function mediaMove(move) {
  // 暂停视频（如果是）
  if(files[currentIndex.value].type === 'video') 
    togglePlay(mediaBox.value, currentIndex.value, isPlaying.value)
  currentIndex.value = (currentIndex.value + move + mediaBox.value.length) % mediaBox.value.length;
  for(let i = 0; i < mediaBox.value.length; i++) {
    mediaBox.value[i].style.transform = `translate(${(i - currentIndex.value) * 100}%, -50%)`
  }
  // 播放视频（如果是）
  if(files[currentIndex.value].type === 'video') 
    togglePlay(mediaBox.value, currentIndex.value, isPlaying.value)
}
// 向左移动
function toLeft() {
  mediaMove(-1)
}
// 向右移动
function toRight() {
  mediaMove(1)
}

// 取消观察以及事件监听
onBeforeUnmount(() => {
  if(observer) observer.disconnect()
  if(files[currentIndex.value].type === 'video') mediaBox.value[currentIndex.value].querySelector('video').removeEventListener('timeupdate', updateProgress)
})

</script>
<style scoped lang="scss">
.media {
  position: relative;
  height: 100%;
  overflow: hidden;
  &:hover .left {
    left: 10px;
  }
  &:hover .right {
    right: 10px;
  }
  &:hover .tips {
    top: 5px;
  }
  &-box {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    &-content {
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 100%;
      height: 100%;
      transition: transform .3s;
      &-image {
        width: 100%;
        height: 100%;
      }
      &-video {
        position: absolute;
        top: 50%;
        left: 0;
        transform: translateY(-50%);
        width: 100%;
      }
    }
  }
}

.video {
  display: block;
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.nav-button {
  position: absolute;
  top: 50%;
  transition: all .2s;
  background-color: #ffffffb9;
  z-index: 1;
}

.left {
  left: -40px;
}

.right {
  right: -40px;
}

.error-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
  background: #f5f7fa;
}

.tips {
  position: absolute;
  top: -25px;
  right: 5px;
  padding: 1px 8px 2px;
  font-size: 12px;
  border-radius: 15px;
  text-align: center;
  background-color: #d4d4d4b9;
  color: #999;
}
</style>
