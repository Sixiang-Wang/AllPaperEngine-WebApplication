<template>
    <el-menu
        :default-active="$route.path"
        active-text-color="#000000"
        background-color="#ffffff"
        class="el-menu-vertical-demo"
        text-color="#000000"
        @open="handleOpen"
        @close="handleClose"
        router
        style="width:250px;border-right: none;box-shadow: none"
    >
      <el-menu-item index="/user/info">
        <el-icon class="aside-icon"><User /></el-icon>
        <span class="fontStyle">个人信息</span>
      </el-menu-item>
      <el-menu-item index="/user/security">
        <el-icon class="aside-icon"><Lock /></el-icon>
        <span class="fontStyle">安全隐私</span>
      </el-menu-item>
      <el-menu-item index="/user/academicClaim" v-if="ifAuthentication">
        <el-icon class="aside-icon"><document /></el-icon>
        <span class="fontStyle">成果管理</span>
      </el-menu-item>
      <el-menu-item index="/user/personalInfo" v-if="ifAuthentication">
        <el-icon class="aside-icon"><data-line/></el-icon>
        <span class="fontStyle">个人门户</span>
      </el-menu-item>
      <el-menu-item index="/user/favorite">
        <el-icon class="aside-icon"><star/></el-icon>
        <span class="fontStyle">个人收藏</span>
      </el-menu-item>
      <el-menu-item index="/user/history">
        <el-icon class="aside-icon"><clock/></el-icon>
        <span class="fontStyle">浏览历史</span>
      </el-menu-item>
    </el-menu>
</template>


<script setup>
import {Clock, DataLine, Document, Lock, Setting, Star, User} from '@element-plus/icons-vue'
import {onMounted, ref} from "vue";
import * as httpUtil from "@/api/http.js";
import cookieUtil from "@/utils/cookie.js";

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
const ifAuthentication = ref(true);
onMounted(async()=>{
  try{
    const res = await httpUtil.get('/user/ifScholar',{},{
      Authorization: cookieUtil.getCookie("token")
    });
    if(res.data.judge === 1){
      ifAuthentication.value = false;
    }else{
      ifAuthentication.value = true;
    }
  }catch (e){
    console.error(e);
  }
})
</script>


<style scoped>
.fontStyle {
  font-size: medium;
  display: flex;
  margin-right: 20px;
}

.aside-icon {
  width: 2em;
}

.el-menu-item {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50px; /* 调整菜单栏目的高度 */
  line-height: 50px; /* 确保文字垂直居中 */
  padding: 0 20px; /* 调整左右内边距，使内容更居中 */
  border-radius: 0 26px 26px 0;
}

.el-menu-item:hover {
  font-size: medium;
}

.el-menu-item.is-active{
  background-color: #C3EEFD;
}

</style>