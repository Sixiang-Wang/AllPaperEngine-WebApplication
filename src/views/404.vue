<!-- eslint-disable vue/multi-word-component-names -->
<script setup>

</script>
<template>
  <div class="py-18 center-div">
    <el-result
        icon="warning"
        title="404提示"
        sub-title="你找的页面走丢了~"
    >
      <template #extra>
        <el-button type="primary" size="default" @click="$router.push('/main')">返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<style scoped>
@import "@/css/404.css";
</style>