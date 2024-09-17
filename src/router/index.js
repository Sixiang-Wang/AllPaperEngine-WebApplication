import { createRouter, createWebHistory } from "vue-router";
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/user',
            name: 'user',
            component: () => import('@/views/User.vue')
        }
    ]
})
// 导出
export default router