<template>
  <el-collapse-item :title="title" :name="name" style="background-color:#C3EEFD;">
    <div style="display: flex">
      <el-checkbox-group
          :value="activeTheme"
          v-model="selectedTheme"
          @change="updateActiveTheme"
          :max="1"
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
import {ref, defineProps, defineEmits, onMounted} from "vue";
import * as httpUtil from "@/api/http.js";
import {useRoute} from "vue-router";
import router from "@/router/index.js";
const selectedTheme = ref([]);
const route = useRoute();
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
  searchInput: String
});

onMounted(()=>{
  console.log(route.query.year)
  if(route.query.year !== null && route.query.year !== '' && route.query.year !== undefined) {
    console.log('aha')
    selectedTheme.value = [route.query.year];
  }
})
const emit = defineEmits(["update:activeTheme"]);
const updateActiveTheme = async () => {
  console.log(selectedTheme.value);
  if(selectedTheme.value.length!==0) {
    try {
      const type = selectedTheme.value[0];
      console.log(type);
      if (type === 'article' || type === 'book-chapter' || type === 'report' || type === 'dissertation' || type === 'other') {
        await router.push({path: '/search', query: {page: 1, input: props.searchInput, year: type, type: 'typeselect'}});
      }else{
        // const res = await httpUtil.get('/elasticSearch/works/getWorksByYear',{
        //   page: 1,
        //   title:props.searchInput,
        //   year: type
        // })
        await router.push({path: '/search',query: {page: 1, input: props.searchInput, year: type, type: 'yearselect'}})
        console.log(res);
      }

    } catch (e) {
      console.error(e);
    }
  }
};
</script>
