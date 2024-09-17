<template>



  <div class="common-layout">
    <el-container>
      <el-aside width="250px">
        <UserAside></UserAside>
      </el-aside>

      <el-main>
        <el-card style="max-width: 700px">
          <div class="card-header">
            <span>基本信息</span>
          </div>
          <br>
          <p class="card-item">aa</p>
          <el-divider />
          <p class="card-item">aa</p>
        </el-card>
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

.card-item{
  padding: 0 15px;
}

</style>