<template>
  <el-collapse-item :title="title" :name="name" style="background-color:#C3EEFD;">
    <div style="display: flex">
      <el-checkbox-group
          :value="activeTheme"
          @change="updateActiveTheme"
      >
        <el-checkbox
            v-for="theme in themes"
            :key="theme.value"
            :label="theme.label"
            :value="theme.value"
            style="display: block; padding-top: 10px;"
        >
          {{ theme.label }}
        </el-checkbox>
      </el-checkbox-group>
    </div>
  </el-collapse-item>
</template>

<script setup>
import { ref, defineProps, defineEmits } from "vue";

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  themes: {
    type: Array,
    required: true,
  },
  activeTheme: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["update:activeTheme"]);

const updateActiveTheme = (newValue) => {
  emit("update:activeTheme", newValue);
};
</script>
