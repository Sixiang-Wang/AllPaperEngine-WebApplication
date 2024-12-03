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
                },
                {
                    path: '/user/academicClaim',
                    name: 'AcademicClaim',
                    component: ()=> import('@/views/personalPortal/AcademicClaim.vue')
                },
                {
                    path: '/user/favorite',
                    name: 'favorite',
                    component: () => import('@/views/user/Favorite.vue')
                },

                {
                    path: '/user/history',
                    name: 'history',
                    component: () => import('@/views/user/History.vue')
                },
                {
                    path: '/user/personalInfo',
                    name: 'PersonalPortalInfo',
                    component: ()=> import('@/views/personalPortal/PersonalInfo.vue')
                },
            ]
        },
        {
            path: '/hotpoint',
            name: 'hotpoint',
            component: () => import('@/views/Hotpoint.vue')
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
            path: '/advancedSearch',
            name: 'advancedSearch',
            component: () => import('@/views/AdvancedSearch.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: ()=> import('@/views/Login.vue')
        },
        {
          path: '/scholarIdentify',
          name: 'ScholarIdentify',
          component: () => import('@/views/ScholarIdentify.vue')
        },
        {
            path: '/register',
            name: 'register',
            component: ()=> import('@/views/Register.vue')
        },
        {
          path: '/message',
          name: 'message',
          component: ()=> import('@/views/MessageCenter.vue')
        },
        {
            path: '/test',
            name: 'test',
            component: () => import('@/views/Test.vue')
        },
        {
          path: '/pdf',
          name: 'pdf',
          component: ()=> import('@/views/Pdf.vue')
        },
        {
            path: '/paper',
            name: 'paper',
            component: ()=> import('@/views/Paper/Paper.vue')
        },
        {
          path: '/scholarAppeal',
          name: 'ScholarAppeal',
          component: ()=> import('@/views/ScholarAppeal.vue')
        },
        {
            path: '/authorInfo',
            name: 'authorInfo',
            component: ()=> import('@/views/user/AuthorInfo.vue')
        },
        {
            path: '/institutionInfo',
            name: 'institutionInfo',
            component: ()=> import('@/views/user/InstitutionInfo.vue')
        },
        {
            path: '/personalportal',
            name: 'personalportal',
            component: () => import('@/views/personalPortal/PersonalPortal.vue'),
            redirect: '/personalportal/personalInfo',
            children: [

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