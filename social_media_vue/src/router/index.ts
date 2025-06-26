import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home/Home.vue'
import ForumView from '@/views/Home/ForumView/ForumView.vue'
import Hot from '@/views/Hot/Hot.vue'
import Issue from '@/views/Issue/Issue.vue'
import Message from '@/views/Message/Message.vue'
import Me from '@/views/Me/Me.vue'
import Production from '@/views/Me/MeMain/Production.vue'
import Love from '@/views/Me/MeMain/Love.vue'
import Collect from '@/views/Me/MeMain/Collect.vue'
import Set from '@/views/Me/MeMain/Set.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      // 首页
      path: '/home',
      name: 'home',
      component: Home,
      children: [
        {
          path: '/home/forumView',
          name: 'forumView',
          component: ForumView,
          props(route) {
            return route.query
          },
        },
      ],
    },
    {
      // 热门
      path: '/hot',
      name: 'hot',
      component: Hot,
    },
    {
      // 发布
      path: '/issue',
      name: 'issue',
      component: Issue,
    },
    {
      // 消息
      path: '/message',
      name: 'message',
      component: Message,
    },
    {
      // 我
      path: '/me',
      name: 'me',
      component: Me,
      children: [
        {
          path: '/me/production',
          name: 'production',
          component: Production,
        },
        {
          path: '/me/love',
          name: 'love',
          component: Love,
        },
        {
          path: '/me/collect',
          name: 'collect',
          component: Collect,
        },
        {
          path: '/me/set',
          name: 'set',
          component: Set,
        },
        {
          path: '/me/',
          component: Love,
        },
      ],
    },
    {
      // 默认打开
      path: '/',
      component: Home,
    },
  ],
})

export default router
