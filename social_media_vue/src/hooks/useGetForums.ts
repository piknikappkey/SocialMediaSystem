import type { Forum } from '@/types/forum'
import { service } from '@/utils/request'
import { ElMessage } from 'element-plus'
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/store/userData'

export default function () {
  const { userData } = storeToRefs(useUserStore()) // 用户数据
  function getForums(forums: Forum[], start: Number, size: Number) {
    service
      .get(`/forums?start=${start}&size=${size}&userId=${userData.value.userId || 0}`)
      .then((res) => {
        if (!res.data.state) {
          // 返回数据失败
          ElMessage.error(res.data.content)
          return
        }
        // 成功则添加入forums列表
        // 修改forum创建时间以方便后续显示
        for (let i = 0; i < res.data.data.length; i++) {
          res.data.data[i].createdTime = new Date(res.data.data[i].createdTime)
        }
        forums.push(...res.data.data)
      })
      .catch((err) => console.log(err))
  }
  return { getForums }
}
