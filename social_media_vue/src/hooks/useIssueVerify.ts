import type { Forum } from '@/types/forum'
import { ElMessage, type UploadFile } from 'element-plus'

export default function () {
  function issueVerify(
    title: String,
    inputType: String,
    forumCover: UploadFile[],
    forumImages: UploadFile[],
    forumVideos: UploadFile[],
  ) {
    // 检查标题
    if (!title) {
      ElMessage.error('请输入标题！')
      return false
    }
    // 检查是否有封面
    if (forumCover.length == 0) {
      ElMessage.error('请选择封面！')
      return false
    }
    // 检查是否有图片/视频
    if (inputType === 'image' && forumImages.length == 0) {
      ElMessage.error('请选择图片！')
      return false
    }
    if (inputType === 'video' && forumVideos.length == 0) {
      ElMessage.error('请选择视频！')
      return false
    }
    return true
  }
  return { issueVerify }
}
