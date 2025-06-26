<template>
  <div ref="slider" class="silder" @click="jumpTime">
    <hr class="line" />
    <hr ref="dotLine" class="dot-line" />
    <div ref="dot" class="dot" @mousedown="startMove"></div>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
const slider = ref()
const dot = ref()
const dotLine = ref()
let dotX = ref(0)
let dotCanMove = false

// 获取进度以及最大进度
const { useLength, max } = defineProps(['useLength', 'max'])
// 监听useLength值并赋值给dotX
onMounted(() => {
  watch(
    () => useLength,
    () => {
      if (dotCanMove) return
      dotX.value = (useLength / max) * slider.value.clientWidth
    },
  )
})

// 定义事件：当手动改变dotX值时触发，用于设置视频播放时间
const emit = defineEmits(['set-videoTime'])
// 设置视频播放时间
function setVideoTime() {
  if (!slider.value?.offsetWidth) return
  emit('set-videoTime', (dotX.value / slider.value.offsetWidth) * max)
}

// 点击跳转
function jumpTime(e) {
  // 如果点击的元素是点则返回
  if (e.target === dot.value) return
  //  点击位置在点右边或左边
  if ((dotX.value < e.offsetX && e.offsetX < slider.value.offsetWidth) || e.offsetX >= 0) {
    dotX.value = e.offsetX
    setVideoTime()
    return
  }
}

// 点击后移动点
const isMove = function (e) {
  if (!dotCanMove) return
  const mouseX = e.clientX - slider.value.getBoundingClientRect().left
  if (mouseX < 0 || mouseX > slider.value.offsetWidth) return
  dotX.value = mouseX
  return
}

// 松开后取消事件监听
const stopMove = function (e) {
  dotCanMove = false
  setVideoTime()
}

document.addEventListener('mousemove', isMove)
document.addEventListener('mouseup', stopMove)

// 点击点
function startMove(e) {
  // 点击位置太靠右则返回
  if (e.offsetX > dot.value.offsetWidth / 2 && dotX.value + e.offsetX > slider.value.offsetWidth)
    return
  // 太靠左返回
  if (dotX.value - e.offsetX < 0) return
  dotX.value = dotX.value + (e.offsetX - dot.value.offsetWidth / 2)
  dotCanMove = true
}

// 移动点
watch(dotX, () => {
  dot.value.style.left = `${dotX.value}px`
  dotLine.value.style.width = `${dotX.value}px`
})
</script>
<style scoped lang="scss">
.silder {
  position: relative;
  height: 15px;
  cursor: pointer;
  transform: translateY(-60%);
}
.line {
  position: absolute;
  width: 100%;
  left: 50%;
  top: 50%;
  height: 3px;
  background-color: rgba($color: #000000, $alpha: 0.6);
  transform: translate(-50%, -50%);
}
.dot {
  position: absolute;
  left: 0;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: rgba($color: $main-color-light, $alpha: 1);
  transform: translateX(-50%);
  transition: all 0ms;
  user-select: none;
  transition: transform 0.2s;
  &:hover {
    transform: translateX(-50%) scale(130%);
  }
  &:active {
    transform: translateX(-50%) scale(130%);
  }
  &-line {
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    height: 5px;
    background-color: rgba($color: $main-color-light, $alpha: 0.6);
    transition: all 0ms;
  }
}
</style>
