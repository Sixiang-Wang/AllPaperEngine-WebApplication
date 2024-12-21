<template>



  <div class="common-layout">
    <el-container style="height: 100%;">
      <el-aside width="400px">
        <UserAside></UserAside>
      </el-aside>

      <el-main style="flex-grow: 1;">
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>

import UserAside from "@/components/UserAside.vue";
</script>

<style scoped>
.el-menu-vertical-demo{
  margin-left: 0;
  margin-right: 0;
}

.card-header{
  font-size: large;
  border-bottom: none;
}



</style>