export default function () {
  function getTime(date: Date) {
    const now = new Date()
    const targetDate = new Date(date)

    // 标准化日期（去除时间部分）
    const normalizeDate = (date: Date) =>
      new Date(date.getFullYear(), date.getMonth(), date.getDate())
    const nowDate = normalizeDate(now)
    const targetNormalized = normalizeDate(targetDate)

    // 1. 年份不同
    if (targetDate.getFullYear() !== now.getFullYear()) {
      const year = targetDate.getFullYear()
      const month = (targetDate.getMonth() + 1).toString().padStart(2, '0')
      const day = targetDate.getDate().toString().padStart(2, '0')
      return `${year}-${month}-${day}` //
    }

    // 2. 月份不同
    if (targetDate.getMonth() !== now.getMonth()) {
      const month = (targetDate.getMonth() + 1).toString().padStart(2, '0')
      const day = targetDate.getDate().toString().padStart(2, '0')
      return `${month}-${day}` //
    }

    // 计算天数差
    const timeDiff = nowDate.getTime() - targetNormalized.getTime()
    const daysDiff = Math.floor(timeDiff / (1000 * 3600 * 24)) //

    // 3. 超过3天
    if (daysDiff > 3) {
      const month = (targetDate.getMonth() + 1).toString().padStart(2, '0')
      const day = targetDate.getDate().toString().padStart(2, '0')
      return `${month}-${day}`
    }

    // 4-6. 三天内
    if (daysDiff > 0) {
      return `${daysDiff}天前` // 根据差值返回对应文案
    }

    // 7. 当天
    const hours = targetDate.getHours().toString().padStart(2, '0')
    const minutes = targetDate.getMinutes().toString().padStart(2, '0')
    return `${hours}:${minutes}` //
  }
  return { getTime }
}
