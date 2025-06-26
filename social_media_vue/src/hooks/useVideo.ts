import type { Ref } from 'vue'

export default function () {
  function videoInit(mediaBox, i, duration) {
    // 视频初始化
    mediaBox[i].querySelector('.video').addEventListener('loadedmetadata', () => {
      duration = mediaBox[i].querySelector('.video').duration
    })
    // 移动端优化
    mediaBox[i].querySelector('.video').addEventListener(
      'touchmove',
      (e) => {
        e.preventDefault()
      },
      { passive: false },
    )
  }
  function togglePlay(mediaBox, currentIndex, isPlaying) {
    isPlaying = !mediaBox[currentIndex].querySelector('.video').paused
    isPlaying
      ? mediaBox[currentIndex].querySelector('.video').pause()
      : mediaBox[currentIndex].querySelector('.video').play()
  }
  return { videoInit, togglePlay }
}
