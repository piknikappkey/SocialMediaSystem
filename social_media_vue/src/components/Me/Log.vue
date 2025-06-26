<template>
  <section class="section-log" ref="section">
    <div class="log-box">
      <a class="section-log_close" @click="close">&times;</a>
      <form id="form" action="log" method="get" class="form">
        <Title title="登录" />
        <LogInput />
        <LogBtn />
      </form>
    </div>
  </section>
</template>

<script setup lang="ts">
import { useLogStore } from '@/store/log'
import { storeToRefs } from 'pinia'
import { ref, watch, watchEffect } from 'vue'
import LogInput from './LogInput.vue'
import LogBtn from './LogBtn.vue'
import Title from './Title.vue'
let section = ref()
let { logData } = storeToRefs(useLogStore())
const { clearInput } = useLogStore()

function close() {
  // 点击关闭
  logData.value.showLog = false
  clearInput()
}

watchEffect(() => {
  // 监视showLog变化以执行显示/隐藏log
  try {
    if (logData.value.showLog) {
      section.value.classList.add('section-log_display')
    } else {
      section.value.classList.remove('section-log_display')
    }
  } catch {}
})
</script>

<style scoped lang="scss">
.section-log {
  position: fixed;
  top: 50%;
  left: 50%;
  width: 400%;
  height: 400%;
  transition: all 0.3s;
  background-color: rgba(0, 0, 0, 0.85);
  opacity: 0;
  visibility: hidden;
  transform: translate(-50%, -50%) scale(25%);
  z-index: -100;
}

@supports (-webkit-backdrop-filter: blur(3px)) or (backdrop-filter: blur(3px)) {
  .section-log {
    -webkit-backdrop-filter: blur(3px);
    backdrop-filter: blur(3px);
    background-color: rgba(0, 0, 0, 0.4);
  }
}

.section-log_display {
  opacity: 1;
  visibility: visible;
  transform: translate(-50%, -50%);
  z-index: 100;
}

.section-log_close {
  user-select: none;
  position: absolute;
  top: -5px;
  right: 10px;
  transition: all 0.2s;
  font-size: 30px;
}

.section-log_close:link,
.section-log_close:visited {
  color: #777;
  text-decoration: none;
  font-size: 30px;
  /* display: block; */
}

@media only screen and (max-width: 37.5em) {
  .section-log_close:link,
  .section-log_close:visited {
    top: -15px;
    right: 0;
  }
}

.section-log_close:hover {
  color: $secend-color;
}

.log-box {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  background-color: $main-color-llight;
  border: 4px solid $secend-color-llight;
  border-radius: 5px;
  text-align: center;
  padding: 20px 0;
}

@media (max-width: 37.5em) {
  .log-box {
    width: 23%;
  }
}
</style>
