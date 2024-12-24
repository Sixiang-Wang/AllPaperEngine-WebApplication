<script setup>
import router from "@/router/index.js";
import cookieUtil from "@/utils/cookie.js"
import http from "@/api/http.js";
import httpUtil from "@/api/http.js";
import {useTokenStore, useUserStore} from "@/store/store.js";
import {Search} from "@element-plus/icons-vue";
import {computed, onMounted, ref, watch} from "vue";
import {useRoute} from 'vue-router';
import { useUserIdStore} from "@/store/store.js";
import {ElMessage} from "element-plus";

  const totalLength = ref(0);
  let searchResults = ref([]);
  let suggestions = ref([]);
  let showAutoComplete = ref(false);
  const route = useRoute();
  let hoveredIndex = ref(null);
  const currentPage = ref(Number(route.query.page) || 1); // 确保 currentPage 从 URL 获取 

  const userStore = useUserStore();
  const tokenStore = useTokenStore()
  const userIdStore = useUserIdStore()
  const searchType = ref('1');
  const searchInput = ref("");
  const fullText = "llPaper Engine";
  const displayedText = ref("A"); // 动态显示的文字
  const fullText2 = "ower Your Academic Journey";
  const displayedText2 = ref("P"); // 动态显示的文字
  const search = async () => {
    if(searchInput.value === ''){
      ElMessage.warning("请先输入搜索内容！");
      return;
    }
    switch (searchType.value) {
    case '1'://按标题查找
      //为方便测试，这里保留搜索所有结果的接口
      console.log("begin search")
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        console.log("search in openalex/get/page");
        searchResults.value = res.data.works;
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
      }else {
        // const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
        //   word: searchInput.value,
        //   page: currentPage.value
        // })
        const res = await httpUtil.get('/elasticSearch/works/getByTitleByPage', {
          title: searchInput.value,
          page: currentPage.value
        })
        searchResults.value = res.data.works || [];
        console.log(searchResults.value);
        totalLength.value = searchResults.value.length;

        for(let result of searchResults.value){
          if(result.highlightFields.hasOwnProperty('title')){
            result.content.title = result.highlightFields.title[0];
          }
          if(result.highlightFields.hasOwnProperty('abstract')){
            const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
            if(startIndex!=-1){
              result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
            }else{
              result.content.abstractText = result.highlightFields.abstract[0];
            }
          }
          // if(result.highlightFields.hasOwnProperty('keywordsText')){
          //   result.content.abstractText = result.highlightFields.abstract[0];
          // }
        }
      }
      router.push({path: "/search", query: {input: searchInput.value, page: 1,type: searchType.value,searchResult:searchResults}});
      break;
    case '2'://查找篇名
      console.log(2)
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        // console.log(res);
        searchResults.value = res.data.works;
        // console.log(searchResults.value);
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
        // console.log(totalLength.value);
      }else {
        // const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
        //   word: searchInput.value,
        //   page: currentPage.value
        // })
        const res = await httpUtil.get('/elasticSearch/works/getByTitleByPage', {
          title: searchInput.value,
          page: currentPage.value
        })
        searchResults.value = res.data.works || [];
        console.log(searchResults.value);
        totalLength.value = searchResults.value.length;

        for(let result of searchResults.value){
          if(result.highlightFields.hasOwnProperty('title')){
            result.content.title = result.highlightFields.title[0];
          }
          if(result.highlightFields.hasOwnProperty('abstract')){
            const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
              result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
            }else{
              result.content.abstractText = result.highlightFields.abstract[0];
            }
          }
          // if(result.highlightFields.hasOwnProperty('keywordsText')){
          //   result.content.abstractText = result.highlightFields.abstract[0];
          // }
      }
      router.push({path: "/search", query: {input: searchInput.value, page: 1,type: searchType.value,searchResult:searchResults}});
      break;

    case '3'://查找关键词
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        // console.log(res);
        searchResults.value = res.data.works;
        // console.log(searchResults.value);
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
        // console.log(totalLength.value);
      }else {
        // const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
        //   word: searchInput.value,
        //   page: currentPage.value
        // })
        const res = await httpUtil.get('/elasticSearch/works/getByTitleByPage', {
          title: searchInput.value,
          page: currentPage.value
        })
        searchResults.value = res.data.works || [];
        console.log(searchResults.value);
        totalLength.value = searchResults.value.length;

        for(let result of searchResults.value){
          if(result.highlightFields.hasOwnProperty('title')){
            result.content.title = result.highlightFields.title[0];
          }
          if(result.highlightFields.hasOwnProperty('abstract')){
            const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
            if(startIndex!=-1){
              result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
            }else{
              result.content.abstractText = result.highlightFields.abstract[0];
            }
          }
          // if(result.highlightFields.hasOwnProperty('keywordsText')){
          //   result.content.abstractText = result.highlightFields.abstract[0];
          // }
        }
        router.push({path: "/search", query: {input: searchInput.value, page: 1,type: searchType.value,searchResult:searchResults}});
      }
      break;
    case '4'://查找摘要
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        // console.log(res);
        searchResults.value = res.data.works;
        // console.log(searchResults.value);
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
        // console.log(totalLength.value);
      }else {
        // const res = await httpUtil.get('/elasticSearch/works/getByTitleOrAbstractOrKeywords', {
        //   word: searchInput.value,
        //   page: currentPage.value
        // })

        searchResults.value = res.data.works || [];
        console.log(searchResults.value);
        totalLength.value = searchResults.value.length;

        for(let result of searchResults.value){
          if(result.highlightFields.hasOwnProperty('title')){
            result.content.title = result.highlightFields.title[0];
          }
          if(result.highlightFields.hasOwnProperty('abstract')){
            const startIndex = result.highlightFields.abstract[0].indexOf("<span style='color:red'>");
            if(startIndex!=-1){
              result.content.abstractText = result.highlightFields.abstract[0].substring(startIndex);
            }else{
              result.content.abstractText = result.highlightFields.abstract[0];
            }
          }
          // if(result.highlightFields.hasOwnProperty('keywordsText')){
          //   result.content.abstractText = result.highlightFields.abstract[0];
          // }
        }
      }
      //searchResults = res;


      router.push({path: "/search", query: {input: searchInput.value, page: 1,type: searchType.value,searchResult:searchResults}});
      break;

      case '5'://查找学者
      if(searchInput.value===null||searchInput.value === ''){
        const res = await httpUtil.get('/openalex/get/page',{
          page: currentPage.value
        })
        console.log("search in openalex/get/page");
        searchResults.value = res.data.works;
        const res2 = await httpUtil.get('/openalex/get/length');
        totalLength.value = res2.data.leng;
      }
      // }else {
      //   const res = await httpUtil.get('/author/getAuthorIdByAuthorName', { 
      //     name: searchInput.value,
      //     page: currentPage.value
      //   })
      //   searchResults.value = res.data.works || [];
      //   console.log(searchResults.value);
      //   totalLength.value = searchResults.value.length;
      // }
      router.push({path: "/search", query: {input: searchInput.value, page: 1,type: searchType.value,searchResult:searchResults}});
      break;
  }
  router.push({path: "/search", query: {input: searchInput.value, page: 1,type: searchType.value,searchResult:searchResults}});
}
  const advancedSearch = () => {
    router.push({ path: "/advancedSearch" });
  }

  onMounted(() => {
    let index = 0;
    let index2 = 0;
    const interval = setInterval(() => {
      if (index < fullText.length) {
        displayedText.value += fullText[index];
        index++;
      } else {
        clearInterval(interval); // 打字完成后停止
      }
    }, 100); // 设置打字速度，150ms显示一个字符
    const interval2 = setInterval(() => {
      if (index2 < fullText2.length) {
        displayedText2.value += fullText2[index2];
        index2++;
      } else {
        clearInterval(interval2); // 打字完成后停止
      }
    }, 60); // 设置打字速度，150ms显示一个字符
  });



const handleInputChange = async () => {
  console.log(searchInput.value)
  if (searchInput.value == '') {
    showAutoComplete = false;
  } else {
    showAutoComplete = true;
  }

  if (searchInput.value.length > 0) {
    let res = null;
    switch (searchType.value){
      case '1':
        //主题
        res = await httpUtil.get('/elasticSearch/works/autoCompleteTitleWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const titleSuggest1 = res.data.suggestions.suggest.title_suggest;
        if(titleSuggest1 && titleSuggest1.length>0){
          console.log(titleSuggest1);
          suggestions  = titleSuggest1[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }
        break;
      case '2':
        //篇名
        res = await httpUtil.get('/elasticSearch/works/autoCompleteTitleWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const titleSuggest = res.data.suggestions.suggest.title_suggest;
        if(titleSuggest && titleSuggest.length>0){
          console.log(titleSuggest);
          suggestions  = titleSuggest[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }


        break;
      case '3':
        //关键词
        res = await httpUtil.get('/elasticSearch/works/autoCompleteKeywordsWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const keywordstextSuggest = res.data.suggestions.suggest.keywordstextSuggest;
        if(keywordstextSuggest && keywordstextSuggest.length>0){
          console.log(keywordstextSuggest);
          suggestions  = keywordstextSuggest[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }
        break;
      case '4':
        //摘要
        res = await httpUtil.get('/elasticSearch/works/autoCompleteAbstractWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const abstractSuggest = res.data.suggestions.suggest.abstractSuggest;
        if(abstractSuggest && abstractSuggest.length>0){
          console.log(abstractSuggest);
          suggestions  = abstractSuggest[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }
        break;
      case '5':
        //学者 
        //以下在有接口之前是假的
        res = await httpUtil.get('/elasticSearch/works/autoCompleteScholarWithCompletionSuggester', {
          searchContent: searchInput.value 
        });
        const scholarSuggest = res.data.suggestions.suggest.scholarSuggest;
        if(scholarSuggest && scholarSuggest.length>0){
          console.log(scholarSuggest);
          suggestions  = scholarSuggest[0].options.map(option => option.text);
          showAutoComplete = true;
        }else{
          showAutoComplete = false;
        }
        break;
        
    }
    
  } else {
    autocompleteSuggestions = [];
    showAutoComplete = false;
  }
};

const selectSuggestion = (suggestion) => {
  searchInput.value = suggestion;
  showAutoComplete = false;
  // console.log(showAutoComplete)
  // search();
};

const hoverSuggestion = (index) => {
  hoveredIndex.value = index;
};

const leaveSuggestion = (index) => {
  index = -1;
};


</script>

<template>
<div class="input">
  <div style="margin-bottom: 5%;margin-top: -35px">
    <h2 class="title" style="   user-select: none;" >{{displayedText}}</h2>
    <h2 class="title" style="font-size: 45px;margin-bottom: -15px; user-select: none;">
      {{displayedText2}}</h2>
  </div>
  <div style="background-color: transparent !important;">
  <el-input v-model="searchInput" class="search-input" placeholder="请输入搜索内容" @keyup.enter="search" @input="handleInputChange">
    <template #prepend>
      <el-select v-model="searchType" style="width: 115px">
        <el-option label="主题" value="1"/>
        <el-option label="篇名" value="2"/>
        <el-option label="关键词" value="3"/>
        <el-option label="摘要" value="4"/>
        <el-option label="学者" value="5"/>
      </el-select>




    </template>

  </el-input>
  <el-button :icon="Search" @click="search" class="search-button"  />
  <el-button @click="advancedSearch" class="advanced-search-button" type="text">高级检索 ></el-button>
  </div>
  <div v-if="showAutoComplete" class="autocomplete-container">
    <div 
      v-for="(suggestion, index) in suggestions" 
        :key="index" 
        class="autocomplete-item" 
        @click="selectSuggestion(suggestion)"
        @mouseenter="hoverSuggestion(index)"
        @mouseleave="leaveSuggestion(index)"
        :class="{'hovered': hoveredIndex === index}"
    >
    <!-- 显示序号和 text -->
    {{ `${index + 1}. ${suggestion}` }}
    </div>
  </div>


</div>
</template>

<style scoped>
  @import "@/css/main.css";
  :deep(.el-input-group__append) {
    background-color: transparent !important;
    color: rgba(255, 255, 255, 0.7);
  }
  :deep(.el-input__wrapper){
    background-color:rgba(0,0,0,0) !important;
    --el-input-focus-border-color:#FFFFFF;
  }
  :deep(.el-input-group__prepend){
    background-color:rgba(0,0,0,0);
  }
  :deep(.el-input__inner) {
    background-color: rgba(0, 0, 0, 0) !important;
    color: #fff;
  }
  :deep(.el-input__inner::placeholder) {
    color: rgba(255, 255, 255, 0.7) !important;
    cursor: pointer;
  }
  :deep(.el-select__selected-item) {
    color: rgba(255, 255, 255, 0.7) !important;
    cursor: pointer;
  }
/*不要碰这里的代码！别放到.css中*/
  :deep(.el-select__wrapper){
    height: 40px;
    --el-input-focus-border-color: #FFFFFF;
  }

  .autocomplete-item {
  
}

/* 悬停样式 */
.autocomplete-item.hovered {
  background-color: rgba(245, 245, 245, 0.5); /* 设置悬停时的背景为半透明灰色 */
}

.autocomplete-container {

  cursor: pointer; 
  color: white; /* 设置字体颜色为白色 */
  background-color: transparent; /* 设置背景为透明 */
  position: absolute; 
  z-index: 1000; 
  width: 44%;
  border: 1px solid #ccc; 
  border-top: none;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); 
  max-height: 200px; 
  overflow-y: auto; 
}


.autocomplete-item {
  padding: 8px 10px; 
  cursor: pointer; 
}




.search-input {
  position: relative; 
}

/* 添加一些过渡效果，使悬停和点击更加平滑 */
.autocomplete-item {
  transition: background-color 0.2s ease;
}
</style>