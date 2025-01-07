import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      component: resolve => require(['../pages/Login.vue'], resolve)
      // component: resolve => require(['../components/Home.vue'], resolve)
    },

    {
      path: '/Home',
      component: resolve => require(['../components/Home.vue'], resolve),
      children: [
        {
          path: '/Info',
          component: resolve => require(['../pages/InfoPage.vue'], resolve)
        },
        {
          path: '/User',
          component: resolve => require(['../pages/UserPage.vue'], resolve)
        },
        {
          path: '/Authentication',
          component: resolve => require(['../pages/Authentication.vue'], resolve)
        },
        {
          path: '/Claim',
          component: resolve => require(['../pages/Claim.vue'], resolve)
        },
        {
          path: '/Complaint',
          component: resolve => require(['../pages/ComplaintPage.vue'], resolve)
        }
      ]
    }

  ]
})
// 添加全局导航守卫
router.beforeEach((to, from, next) => {
  const admin = localStorage.getItem('admin') // 检查 localStorage.admin
  if (!admin && to.path !== '/') {
    // 如果 admin 为空且访问的不是登录页
    next('/') // 跳转到登录页面
  } else if (admin && to.path === '/') {
    // 如果 admin 为空且访问的不是登录页
    next('Info') // 跳转到登录页面
  } else {
    next() // 放行
  }
})

export default router
