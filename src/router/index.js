import { createRouter, createWebHistory } from "vue-router";
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            redirect: '/main',
        },
        {
            path: '/user',
            name: 'user',
            component: () => import('@/views/User.vue'),
            redirect: '/user/info', // 添加重定向规则
            children: [
                {
                    path: '/user/info',
                    name: 'userInfo',
                    component: () => import('@/views/user/UserInfo.vue')
                },
                {
                    path: '/user/security',
                    name: 'security',
                    component: () => import('@/views/user/UserSecurity.vue')
                },
                {
                    path: '/user/paper',
                    name: 'paper',
                    component: () => import('@/views/user/PaperManager.vue')
                },
                {
                    path: '/user/statistics',
                    name: 'statistics',
                    component: () => import('@/views/user/UserStatistics.vue')
                }
            ]
        },
        {
            path: '/main',
            name: 'main',
            component: () => import('@/views/Main.vue')
        },
        {
            path: '/search',
            name: 'search',
            component: () => import('@/views/SearchResult.vue')
        },
        {
          path: '/login',
          name: 'login',
          component: ()=> import('@/views/Login.vue')
        },
        {
            path: '/test',
            name: 'test',
            component: () => import('@/views/Test.vue')
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'NotFound',
            component: () => import('@/views/404.vue')
        }
    ]
})
// 导出
export default router