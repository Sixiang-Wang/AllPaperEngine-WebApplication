<script setup>

import PortalAside from "@/components/PersonalPortal/PortalAside.vue";
import PortalHeader from "@/components/PersonalPortal/PortalHeader.vue";
</script>

<template>
  <div class="common-layout">
  <el-container>`
    <el-aside>
      <PortalAside/>
    </el-aside>

    <el-main>
      <router-view/>
    </el-main>
  </el-container>
  </div>
</template>

<style scoped>

</style>