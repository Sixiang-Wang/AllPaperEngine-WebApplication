import { createStore } from 'vuex'

export default createStore({
    state: {
    },
    getters: {
        GET_TOKEN:state => {
            return sessionStorage.getItem("token")
        }
    },
    mutations: {
        SET_TOKEN:(state,token)=>{
            sessionStorage.setItem("token",token);
        }
    },
    actions: {
    },
    modules: {
    }
})
