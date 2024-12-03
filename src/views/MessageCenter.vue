<script setup>
import SingleMessage from "@/components/SingleMessage.vue";
import {onMounted, ref} from "vue";
import * as httpUtil from "@/api/http.js";
import * as cookieUtil from "@/utils/cookie.js";
import {ElMessage} from "element-plus";
import CertainMessage from "@/components/CertainMessage.vue";

const messages = ref([{
  id: 1,
  userName: "古丈机器人",
  date: "2024-12-02",
  messageIndex: "天胡开局没打过大红地精"
}])
const page = ref(1);
onMounted(async()=>{
  try{
    const res = await httpUtil.get('/message/select',{},{Authorization: cookieUtil.getCookie("token")});
    console.log(res.data);
    messages.value = res.data.messages;

  }catch (e){
    console.error(e);
    ElMessage.error("出现错误；请稍后再试");
  }
})
const currentMessage = ref({});
const goToMessage = async(message)=>{
  page.value = 2;
  try{
    const res1 = await httpUtil.get('/message/selectone',{id: message.id});
    currentMessage.value = res1.data.message;
    const res2 = await httpUtil.get('/message/read', {messageId: message.id},{Authorization: cookieUtil.getCookie("token")});
    console.log(res2);
    message.isRead = 1;
  }catch (e){
    console.error(e);
  }
}
const goBack = ()=>{
  page.value = 1;
  currentMessage.value = {};
}
</script>

<template>
  <div v-if="page === 1">
  <SingleMessage v-for="message in messages"
                 v-if="messages.length > 0"
                 :is-read="message.isRead"
                 :key="message.id"
                 :user-name="message.userName"
                 :date="message.date"
                 :message-index="message.index"
                @click="goToMessage(message)"/>
  <span v-else>莫得消息</span>
  </div>
  <div v-if="page === 2">
    <CertainMessage
        :date="currentMessage.date"
        :index="currentMessage.index"
        :user-name="currentMessage.userName"
        @go-back="goBack"
    />
  </div>
</template>

<style scoped>

</style>