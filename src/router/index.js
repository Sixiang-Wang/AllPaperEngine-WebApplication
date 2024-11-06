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
                    name: 'userpaper',
                    component: () => import('@/views/user/PaperManager.vue')
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
            path: '/register',
            name: 'register',
            component: ()=> import('@/views/Register.vue')
        },
        {
            path: '/test',
            name: 'test',
            component: () => import('@/views/Test.vue')
        },
        {
            path: '/paper',
            name: 'paper',
            component: ()=> import('@/views/Paper/Paper.vue')
        },
        {
            path: '/authorInfo',
            name: 'authorInfo',
            component: ()=> import('@/views/user/AuthorInfo.vue')
        },
        {
            path: '/personalportal',
            name: 'personalportal',
            component: () => import('@/views/personalPortal/PersonalPortal.vue'),
            redirect: '/personalportal/personalInfo',
            children: [
                {
                    path: '/personalportal/personalInfo',
                    name: 'PersonalPortalInfo',
                    component: ()=> import('@/views/personalPortal/PersonalInfo.vue')
                },
                {
                    path: '/personalportal/academicClaim',
                    name: 'AcademicClaim',
                    component: ()=> import('@/views/personalPortal/AcademicClaim.vue')
                },
                {
                    path: '/personalportal/otherService',
                    name: 'OtherService',
                    component: ()=> import('@/views/personalPortal/OtherService.vue')
                },
            ]
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'NotFound',
            component: () => import('@/views/404.vue')
        },
    ]
})
// 导出
export default router