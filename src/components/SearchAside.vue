<template>
  <div class="search-aside">
    <el-collapse v-model="activeName">
      <ThemeCollapseItem
          v-for="(themeGroup, index) in themeGroups"
          :key="index"
          :title="themeGroup.title"
          :name="themeGroup.name"
          :themes="themeGroup.themes"
          :activeTheme="activeThemes[index]"
      />
    </el-collapse>
  </div>
</template>

<script setup>
import { ref } from "vue";
import ThemeCollapseItem from "@/components/ThemeCollapseItem.vue";

const activeName = ref(["1"]);
const activeThemes = ref([[], [], [], [], []]); // Stores the selection state of each collapse item

const themeGroups = ref([
  {
    title: "主题 1",
    name: "1",
    themes: [
      { label: "性能研究", value: "1" },
      { label: "复合材料", value: "2" },
      { label: "光催化", value: "3" },
    ],
  },
  {
    title: "主题 2",
    name: "2",
    themes: [
      { label: "纳米材料", value: "4" },
      { label: "生物材料", value: "5" },
      { label: "高分子材料", value: "6" },
    ],
  },
  {
    title: "主题 3",
    name: "3",
    themes: [
      { label: "人工智能", value: "7" },
      { label: "机器学习", value: "8" },
      { label: "深度学习", value: "9" },
    ],
  },
  {
    title: "主题 4",
    name: "4",
    themes: [
      { label: "大数据", value: "10" },
      { label: "云计算", value: "11" },
      { label: "物联网", value: "12" },
    ],
  },
  {
    title: "主题 5",
    name: "5",
    themes: [
      { label: "区块链", value: "13" },
      { label: "量子计算", value: "14" },
      { label: "边缘计算", value: "15" },
    ],
  },
]);

</script>

<style scoped>
@import "@/css/search.css";

.search-aside {
  padding: 20px;
}
</style>