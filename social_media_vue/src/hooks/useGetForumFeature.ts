export default function () {
  function getForumFeature(content: String) {
    const feature = content.split('#')
    if (feature.length <= 1) return ''
    feature.shift()
    return feature.join('#').slice(0, 256)
  }
  return { getForumFeature }
}
