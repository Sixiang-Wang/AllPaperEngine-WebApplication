<script setup>
import { defineEmits } from "vue";
import {ArrowLeft} from "@element-plus/icons-vue";
import {ref} from "vue";
import defaultAvatar from "@/assets/image/user.gif";
const props = defineProps({
  userName: String,
  date: String,
  index: String
})
const avatar = ref({
  defaultAvatar: defaultAvatar,
  url : defaultAvatar
})
const emit = defineEmits();
const goBack = () => {
  emit("go-back");
}
</script>

<template>
  <div style="display:flex; flex-direction: column;">
  <div style="display: flex; flex-direction: row; align-items: center;" @click="goBack">
    <el-icon><arrow-left/></el-icon>
    <span>返回</span>
  </div>
  <div style="display: flex; flex-direction: column;">
    <div style="display:flex; flex-direction: row; align-items: center; margin-top: 3%">
      <el-avatar :src="avatar.url"/>
      <span style="margin-left: 4%; font-size: 18px">{{ props.userName }}</span>
    </div>
    <div style="margin-left: 14%">
      <span class="certain-message-date">{{ props.date }}</span>
    </div>
    <div style="margin-top: 3%">
      <span>{{ props.index }}</span>
    </div>
  </div>
  </div>
</template>

<style scoped>
.certain-message-date {
  font-size: 12px;
  color: #7a7a7a;
}
</style>