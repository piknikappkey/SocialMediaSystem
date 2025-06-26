// 前端图片文件存储统一接口
export interface UploadFile {
  id: string
  raw: File
  preview: string
  progress: number
  status: 'pending' | 'uploading' | 'success' | 'error'
}

export type FileUploadHandler = (file: File) => Promise<void>
