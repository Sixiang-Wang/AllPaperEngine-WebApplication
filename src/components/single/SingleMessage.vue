<script setup>
import { ref } from "vue";
import defaultAvatar from "@/assets/image/user.gif";

const avatar = ref({
  defaultAvatar: defaultAvatar,
  url: defaultAvatar
});

const props = defineProps({
  userName: String,
  date: String,
  messageIndex: String,
  isRead: 1
});
</script>

<template>
  <div class="message-item" >
  <div class="single-message-first-line" style="width: 85%">
    <div class="message-icon-container" >
      <div class="avatar-container">
        <el-avatar :src="avatar.url" style="width: 40px; height: 40px; flex-shrink: 0;"></el-avatar>
        <!-- 红色原点 -->
        <div class="sss" v-if="props.isRead === 0 "></div>
      </div>
    </div>
    <div style="display: flex; flex-direction: column; margin-left: 2%; flex-grow: 1; width: 100%">
      <div style="margin-left: 2%; display: flex; flex-direction: row; align-items: center;">
        <span style="color: #0f3357; font-size: 16px; font-weight: normal;">{{ props.userName }}</span>
        <span style="color: gray; font-size: 12px; font-weight: lighter; margin-left: auto;">{{ props.date }}</span>
      </div>
      <el-text class="message-text" truncated>
       {{ props.messageIndex }}
      </el-text>
    </div>
  </div>
  <el-divider/>
  </div>
</template>

<style scoped>
.single-message-first-line {
  display: flex;
  flex-direction: row;
  margin-left: 1%;
}

.message-icon-container {
  position: relative; /* 相对定位父元素 */
}

.avatar-container {
  position: relative; /* 使红点可以基于头像定位 */
}

.sss {
  position: absolute;
  top: 0;
  right: 0;
  width: 10px;
  height: 10px;
  background-color: red; /* 红色背景 */
  border-radius: 50%; /* 圆形 */
  border: 2px solid white; /* 可选：让红点与头像之间有间隔 */
}

.message-text {
  font-weight: normal;
  font-size: 15px;
  margin-top: 2%;
}
.message-item {
  padding-top: 2%;
  background-color: #fff;
  transition: background-color 0.3s ease;
}
.message-item:hover{
  padding-top: 2%;
  background-color: lightgray;
}
.el-divider--horizontal{
  margin-bottom: 0;
}
</style>

