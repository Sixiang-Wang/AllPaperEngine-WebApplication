import { createRouter, createWebHistory } from "vue-router";
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/user',
            name: 'user',
            component: () => import('@/views/UserInfo.vue'),
            children: [
                {
                    path: '/user/info',
                    name: 'userInfo',
                    component: () => import('@/views/UserInfoCard.vue')
                },
                {
                    path: '/user/security',
                    name: 'security',
                    component: () => import('@/views/UserSecurity.vue')
                },
                {
                    path: '/user/paper',
                    name: 'paper',
                    component: () => import('@/views/PaperManager.vue')
                },
                {
                    path: '/user/statistics',
                    name: 'statistics',
                    component: () => import('@/views/UserStatistics.vue')
                }
            ]
        },
        {
            path: '/home',
            name: 'home',
            component: () => import('@/views/Home.vue')
        }
    ]
})
// 导出
export default router