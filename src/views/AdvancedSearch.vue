<script setup>
import {onMounted, ref, watch} from "vue";
import router from "@/router/index.js";
import {Plus,Minus} from '@element-plus/icons-vue'
import cookieUtil from "@/utils/cookie.js"

//高级检索部分
const searchRows = ref([
  { searchInput: '', searchType: '1', logicOperator: ''},
  { searchInput: '', searchType: '2', logicOperator: ''},
  { searchInput: '', searchType: '3', logicOperator: ''}
]);

const minusSearchBox = (index) => {
  if (searchRows.value.length > 1) {
    searchRows.value.splice(index, 1);
  }
};

const addSearchBox = () => {
  if (searchRows.value.length < 10) { // 最多可保持十行
    searchRows.value.push({ searchInput: '', searchType: '1', logicOperator: ''});
  } else {
    console.log('最多只能添加10行搜索条件。');
  }
};

// 监听搜索行变化
watch(searchRows, (newVal, oldVal) => {
  console.log('Search rows changed:', newVal);
});

const checked1 = ref(true)
const checked2 = ref(false)
const checked3 = ref(false)
const checked4 = ref(false)
const checked5 = ref(false)
const checked6 = ref(false)

const timeRange = ref(null);

const shortcuts = [
  {
    text: 'Last week',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: 'Last month',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: 'Last 3 months',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]

// 方法：重置条件
const resetConditions = () => {
  searchRows.value = [
    { searchInput: '', searchType: '1', logicOperator: '' },
    { searchInput: '', searchType: '2', logicOperator: ''},
    { searchInput: '', searchType: '3', logicOperator: ''}
  ];
  checked1.value = false;
  checked2.value = false;
  checked3.value = false;
  checked4.value = false;
  checked5.value = false;
  checked6.value = false;
  timeRange.value = null; // 重置日期选择器
};
/**
 *
 * 数据传输前处理
 */
const dataProcess = () => {
  const len = searchRows.value.length;

  for(let tmp of )
}

const advancedSearch = () => {
  console.log(111);
  dataProcess();
  router.push({path: "/search", query: {input: searchRows.value[0].searchInput, page: 1}});
}
</script>

<template >
  <div class = "head">
    高级检索
  </div>
  <div style="display: flex; justify-content: space-between; margin: 0%; height: 700px;">
    <div :style="{ width: '60%',marginLeft:'9%',boxShadow: '0 0 8px rgba(0, 0, 0, 0.3)', height: '70%' }">
      <div style="margin-top: 50px;">
        <el-row :gutter="10" v-for="(row, index) in searchRows" :key="index" style="margin: 20px;">

          <el-col :span="index === 0 ? 0 : 3">
            <el-select v-if="index !== 0" v-model="row.logicOperator" placeholder="AND">
              <el-option label="AND" value="AND" />
              <el-option label="OR" value="OR" />
              <el-option label="NOT" value="NOT" />
            </el-select>
          </el-col>
          <el-col :span="index === 0 ? 3 : 0"/>
          <el-col :span="17">
            <el-input v-model="row.searchInput" :style="{width:'100%'}" placeholder="请输入搜索内容">
              <template #prepend>
                <el-select v-model="row.searchType" style="width: 115px" placeholder="主题">
                  <el-option label="主题" value="1" />
                  <el-option label="篇名" value="2" />
<!--                  <el-option label="关键词" value="3" />-->
                  <el-option label="作者" value="4" />
                  <el-option label="第一作者" value="5" />
                  <el-option label="作者单位" value="6" />
                  <el-option label="摘要" value="7" />
                  <el-option label="DOI" value="8" />
                  <el-option label="参考文献" value="9" />
                  <el-option label="文献来源" value="10" />
                </el-select>
              </template>
            </el-input>
          </el-col>
          <el-col :span="index === 0 ? 0 : 2" v-if="index !== 0">
            <el-button type="text" :icon="Minus" class="plus-minus-button" @click="minusSearchBox(index)"  />
          </el-col>
          <el-col :span="2" v-if="index === searchRows.length - 1">
            <el-button type="text" :icon="Plus"  class="plus-minus-button" @click="addSearchBox"  />
          </el-col>
        </el-row>
        <el-row style="margin: 20px;margin-left: 9%;">
          <el-checkbox v-model="checked1" label="A类期刊会议" size="large" />
          <el-checkbox v-model="checked2" label="网络首发" size="large" />
          <el-checkbox v-model="checked3" label="免费" size="large" />
          <el-checkbox v-model="checked4" label="基金文献" size="large" />
          <el-checkbox v-model="checked5" label="中英文扩展" size="large" />
          <el-checkbox v-model="checked6" label="同义词扩展" size="large" />
        </el-row>
        <el-row  type="flex" justify="center">
          <div class="block">
            <span style="font-size: 14px;margin-right: 20px;margin-left: 20px;">发表时间范围:</span>
            <el-date-picker
                v-model="timeRange"
                type="daterange"
                unlink-panels
                range-separator="To"
                start-placeholder="Start date"
                end-placeholder="End date"
                :shortcuts="shortcuts"
                :size="size"
            />
          </div>
        </el-row>
        <hr style="width:90%;height:0.5px;background-color: grey;margin-top: 20px;">
        <el-row type="flex" justify="center" style="margin-top:20px">
          <el-button class="reset-button" @click="resetConditions">重置条件</el-button>
          <el-button class="search-button" @click="advancedSearch">检索</el-button>
        </el-row>
      </div>
    </div>

    <div :style="{ height: '520px', width: '20%',marginRight:'9%',boxShadow: '0 0 8px rgba(0, 0, 0, 0.3)' }">
      <p class="hStyle">高级检索使用方法：</p>
      <hr class="line">
      <p class="pStyle">高级检索支持使用运算符*、+、-、''、""、()进行同一检索项内多个检索词的组合运算，检索框内输入的内容不得超过120个字符。<br>
        输入运算符*(与)、+(或)、-(非)时，前后要空一个字节，优先级需用英文半角括号确定。<br>
        若检索词本身含空格或*、+、-、()、/、%、=等特殊符号，进行多词组合运算时，为避免歧义，须将检索词用英文半角单引号或英文半角双引号引起来。<br>
        <br>
        例如：<br>
        （1）篇名检索项后输入：神经网络 * 自然语言，可以检索到篇名包含“神经网络”及“自然语言”的文献。<br>
        （2）主题检索项后输入：(锻造 + 自由锻) * 裂纹，可以检索到主题为“锻造”或“自由锻”，且有关“裂纹”的文献。<br>
        （3）如果需检索篇名包含“digital library”和“information service”的文献，在篇名检索项后输入：'digital library' * 'information service'。<br>
        （4）如果需检索篇名包含“2+3”和“人才培养”的文献，在篇名检索项后输入：'2+3' * 人才培养。<br>
      </p>
    </div>
  </div>
</template>

<style scoped>
@import "@/css/advancedSearch.css";
</style>
