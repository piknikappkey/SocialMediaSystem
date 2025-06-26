<template>
  <section class="fileinput-box">
    <el-upload
      list-type="picture-card"
      v-model:file-list="fileList"
      :auto-upload="false"
      multiple
      :class="{ 'hide-upload-container': isLimitReached }"
      @change="handleFileChange"
      :accept="fileType"
      :limit="fileLen"
      :on-exceed="fileToMany"
      :before-upload="beforeUpload"
    >
      <!-- 上传按钮区域 -->
      <template #trigger v-if="!isLimitReached">
        <div class="input-box">
          <el-icon class="input-box-icon"><Plus /></el-icon>
          <div class="input-box-title">{{ title }}</div>
        </div>
      </template>

      <!-- 自定义文件展示 -->
      <template #file="{ file }">
        <div class="custom-preview">
          <img class="el-upload-list__item-thumbnail" :src="file.url" :alt="file.name" />

          <!-- 仅保留删除按钮 -->
          <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-delete" @click.stop="handleRemove(file)">
              <el-icon><Delete /></el-icon>
            </span>
          </span>
        </div>
      </template>
    </el-upload>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Delete, Plus } from '@element-plus/icons-vue'
import { ElMessage, type UploadFile, type UploadProps } from 'element-plus'
import { defineProps } from 'vue'
// 限制文件类型与数量
const { fileType, fileLen, title, modelValue, fileMaxSize } = defineProps([
  'fileType',
  'fileLen',
  'title',
  'modelValue',
  'fileMaxSize',
])

// 文件列表
const fileList = computed<UploadFile[]>({
  get: () => modelValue,
  set: (val) => emit('update:modelValue', val),
})
const emit = defineEmits(['update:modelValue'])

// 文件达到上限时隐藏
const isLimitReached = computed(() => (fileList.value.length >= fileLen ? true : false))

const handleFileChange = (file: UploadFile, files: UploadFile[]) => {
  fileList.value = [...files] // 保持响应式更新
}

// 删除操作
const handleRemove = (file: UploadFile) => {
  const index = fileList.value.findIndex((f) => f.uid === file.uid)
  if (index > -1) {
    fileList.value.splice(index, 1)
  }
  // 触发组件内部更新
  fileList.value = [...fileList.value]
}
// 一次传入过多文件错误提示
const fileToMany = () => {
  ElMessage.error(
    `传入的文件数量超过最大(${fileLen})限制，您还可以传入${fileLen - fileList.value.length}个文件`,
  )
}
// 上传文件前验证文件大小
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  if (file.size / 1024 / 1024 > fileMaxSize) {
    ElMessage.error(`传入的文件大小超过最大(${fileMaxSize})限制`)
    return false
  }
  return true
}
</script>

<style scoped lang="scss">
/* 隐藏原生操作容器 */
:deep(.el-upload) {
  display: block; /* 保持多文件布局 */
}

/* 调整整个上传区域布局 */
:deep(.el-upload-list--picture-card) {
  /* display: flex; */
  flex-wrap: wrap;
  gap: 8px; /* 添加卡片间距 */
}

/* 上传按钮容器 */
:deep(.el-upload--picture-card) {
  width: 148px;
  height: 148px;
  border: 1px dashed #d9d9d9;
  transition: all 0.2s;

  /* 完美居中方案 */
  display: flex !important;
  justify-content: center;
  align-items: center;

  /* 悬停效果 */
  &:hover {
    border-color: $main-color;
    .input-box {
      color: $main-color;
    }
  }
}

/* 图标动态效果 */
:deep(.el-icon.plus-icon) {
  transition: color 0.2s;
}

.custom-preview {
  position: relative;
  width: 100%;
  height: 100%;
}

.el-upload-list__item {
  margin: 0 !important;
}

.el-upload-list__item-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.input-box {
  color: #606060;
  text-align: center;
}

.input-box-icon {
  font-size: 25px;
}

/* 隐藏上传按钮容器 */
:deep(.hide-upload-container .el-upload-list--picture-card .el-upload) {
  display: none !important;
}
</style>
