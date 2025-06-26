<template>
  <div v-if="userData.logginStatus" class="islog">
    <div class="file-box">
      <!-- <IssueType v-model:model-value="inputType" /> -->
      <div class="file-box-type">
        <IssueTitle title="选择分享类型" />
        <OptionalBtn
          class="file-box-type-btn"
          v-for="inType in inputTypes"
          :key="inType.id"
          :returnValue="inType.routePath"
          :title="inType.title"
          :first="inType.id === 1"
          name="fileType"
          v-model="inputType"
        />
      </div>
      <IssueFileInput
        class="file-box-input"
        :inputType="inputType"
        :fileSet="fileSet"
        v-model:cover="forumCover"
        v-model:images="forumImages"
        v-model:videos="forumVideos"
      />
    </div>
    <IssueContent
      class="text-box"
      v-model:title="forum.forumTitle"
      v-model:content="forum.forumText"
    />
    <el-button :loading="load" class="submit-btn" @click="submitDown">提交</el-button>
  </div>
  <IssueTipsBox v-if="userData.logginStatus" :fileSet="fileSet" />

  <Button v-if="!userData.logginStatus" class="log-tips" title="请先登录" @click="logTipsDown" />
</template>

<script setup lang="ts">
import { useUserStore } from '@/store/userData'
import { storeToRefs } from 'pinia'
import IssueTipsBox from '@/components/Issue/IssueTipsBox.vue'
import Button from '@/components/Button.vue'
import OptionalBtn from '@/components/OptionalBtn.vue'
import IssueFileInput from '@/components/Issue/IssueFileInput.vue'
import IssueContent from '@/components/Issue/IssueContent.vue'
import IssueTitle from '@/components/Issue/IssueTitle.vue'
import { reactive, ref } from 'vue'
import { useLogStore } from '@/store/log'
import useIssueVerify from '@/hooks/useIssueVerify'
import useGetForumFeature from '@/hooks/useGetForumFeature'
import { service } from '@/utils/request'
import { ElMessage, type UploadFile } from 'element-plus'
import type { Forum } from '@/types/forum'
const { userData } = storeToRefs(useUserStore()) // 用户数据
const { logData } = storeToRefs(useLogStore()) // 登录数据
const { issueVerify } = useIssueVerify() // 验证forum
const { getForumFeature } = useGetForumFeature()
const inputTypes = [
  { id: 1, title: '图片', routePath: 'image' },
  { id: 2, title: '视频', routePath: 'video' },
]
// 设置文件限制
const fileSet = {
  // 可上传文件数量
  imgLen: 20,
  vidLen: 4,
  // 文件大小限制(mb)
  imgSize: 32,
  vidSize: 512,
}

// 论坛数据对象
const forum = <Forum>reactive({
  // 论坛文件类型
  forumTitle: '', // 标题
  forumText: '', // 文字内容
  forumFeature: '', // 喜好
  forumContentUrl: `${Date.now()}`, // 文件夹位置
})
const inputType = ref('image')
const forumCover = ref([])
const forumImages = ref([])
const forumVideos = ref([])

// 点击“请先登录”按钮
const emit = defineEmits(['update:navpath'])
function logTipsDown() {
  // 跳转到“我的”
  emit('update:navpath', '/me')
  logData.value.showLog = true // 打开登录界面
}

// 是否为上传状态
const load = ref(false)
// 点击“提交”按钮
async function submitDown() {
  if (
    !issueVerify(
      forum.forumTitle,
      inputType.value,
      forumCover.value,
      forumImages.value,
      forumVideos.value,
    )
  )
    return // 论坛数据未填写完成则不发送

  // 填写完成进入加载状态
  load.value = true

  // 根据内容写入喜好
  forum.forumFeature = getForumFeature(forum.forumText)

  // 写入用户id
  forum.userId = userData.value.userId

  // 发送forum数据
  await service
    .post(`/forums`, forum)
    .then((res) => console.log(res))
    .catch((err) => ElMessage.error(`表单数据填写失败，请确认你的网络是否正确！${err}`))

  // 整理并发送文件函数
  async function loadFile(file: UploadFile, fileName: String) {
    if (file.raw) {
      // 创建唯一文件名
      fileName = fileName + '.' + file.name.split('.').slice(-1)[0]
      // 构建要发送的文件及相关数据
      const fileData = {
        userId: userData.value.userId,
        file: file.raw,
        name: fileName,
        path: forum.forumContentUrl,
        type: 'forum',
      }
      service
        .post('/files', fileData, {
          headers: {
            'Content-Type': 'multipart/form-data; boundary=WebAppBoundary',
          },
        })
        .then((res) => console.log(res))
        .catch((err) => ElMessage.error(`文件上传失败，请确认你的网络是否正确！${err}`))
    }
  }

  // 统一发送文件组
  async function loadFiles(files: UploadFile[], fileName: any = undefined) {
    const loadFiles: any = []
    files.forEach((file) => loadFiles.push(loadFile(file, `${fileName ?? file.uid}`)))
    await Promise.all(loadFiles)
  }

  // 发送cover
  loadFiles(forumCover.value, 'cover')
  // 发送图片
  loadFiles(forumImages.value)
  // 发送视频
  await loadFiles(forumVideos.value)
  load.value = false
  ElMessage.success('上传成功！')
  forumCover.value = []
  forumImages.value = []
  forumVideos.value = []
  forum.forumTitle = ''
  forum.forumText = ''
  forum.forumFeature = ''
  forum.forumContentUrl = `${Date.now()}`
}
</script>

<style scoped lang="scss">
.log-tips {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 30px;
  font-weight: 400;
  color: $main-color;
}
.islog {
  padding: 55px 25px 100px;
  width: 860px;
  margin: 0 auto;
}
.file-box {
  overflow: hidden;
  &-type {
    margin-bottom: 25px;
    &-btn {
      display: inline-block;
      margin: 0px 15px 0px 25px;
    }
  }
  &-input {
    margin-bottom: 25px;
  }
}
.text-box {
  margin-bottom: 25px;
}
.submit-btn {
  padding: 5px 15px;
  border-radius: 5px;
  background-color: #f1e9ff;
  border: 1px solid #dbc7fd;
  color: #955dfd;
  &:hover {
    background-color: #ddcbff;
    border: 1px solid #bf9dff;
  }
  &:active {
    background-color: #f1e9ff;
    border: 1px solid #dbc7fd;
  }
}
</style>
