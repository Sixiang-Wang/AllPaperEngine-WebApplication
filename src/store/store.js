import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => ({
        username: '', // 初始用户名为空
    }),
    actions: {
        setUsername(name) {
            this.username = name;
        },
    },
});

export const useTokenStore = defineStore('token', {
    state: () => ({
        token: '',
    }),
    actions: {
        setToken(token) {
            this.token = token;
        },
    },
});

export const useUserIdStore = defineStore('userId', {
    state: () => ({
        userId: '',
    }),
    actions: {
        setUserId(userId) {
            this.userId = userId;
        },
    },
});