<template>
  <div class="hotspot-aside">
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
const activeThemes = ref([[], [], []]); // Stores the selection state of each collapse item

const themeGroups = ref([
  {
    title: "Hotspot Themes",
    name: "1",
    themes: [
      { label: "Performance Research", value: "1" },
      { label: "Composite Materials", value: "2" },
    ],
  },
  {
    title: "Source Categories",
    name: "2",
    themes: [
      { label: "Photocatalysis", value: "3" },
      { label: "Nanomaterials", value: "4" },
    ],
  },
  {
    title: "Disciplines",
    name: "3",
    themes: [
      { label: "Artificial Intelligence", value: "5" },
      { label: "Machine Learning", value: "6" },
    ],
  },
  {
    title: "Research Levels",
    name: "4",
    themes: [
      { label: "Big Data", value: "7" },
      { label: "Cloud Computing", value: "8" },
    ],
  },
  {
    title: "Years",
    name: "5",
    themes: [
      { label: "2021", value: "9" },
      { label: "2022", value: "10" },
    ],
  },
  {
    title: "Document Types",
    name: "6",
    themes: [
      { label: "Journal Articles", value: "11" },
      { label: "Conference Papers", value: "12" },
    ],
  },
  {
    title: "Document Sources",
    name: "7",
    themes: [
      { label: "IEEE", value: "13" },
      { label: "Springer", value: "14" },
    ],
  },
  {
    title: "Authors",
    name: "8",
    themes: [
      { label: "Author A", value: "15" },
      { label: "Author B", value: "16" },
    ],
  },
]);
</script>

<style scoped>
@import "@/css/search.css";

.hotspot-aside {
  padding: 20px;
}
</style>