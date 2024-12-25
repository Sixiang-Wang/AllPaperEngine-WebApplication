<script setup>

import {Document, Paperclip, Search, Link, Share, Star, StarFilled} from "@element-plus/icons-vue";
import {computed, nextTick, onMounted, ref, watch} from "vue";
import {useTransition} from '@vueuse/core'
import {ElMessage, ElNotification} from 'element-plus'
import SingleResult from "@/components/single/SingleResult.vue";
import httpUtil from "@/api/http.js";
import * as urlParams from "@/api/http.js";
import robotImage from '@/assets/image/robot.png';
import {useRoute} from "vue-router";
import SingleComment from "@/components/single/SingleComment.vue";
import * as cookieUtil from "@/utils/cookie.js";
import SingleRecommend from "@/components/single/SingleRecommend.vue";
import {useUserIdStore} from "@/store/store.js";
import router from "@/router/index.js";

const userIdStore = useUserIdStore()
const userId = computed(() => userIdStore.userId)

const title = ref("");
const auth = ref([]);
const doi = ref("Loading...");
const abstract = ref("")
const citedByApiUrl = ref("")
const publicationDate = ref("")
const conceptList = ref([]);

const tmp = ref("Storozhuk%20Mv");

const citeNum = ref(0);
const referenceNum = ref(0);
const collectNum = ref(0);
const commentNum = ref(0);
const isCollected = ref(false);
const activeName = ref('first')
const pageSize = 5;  // 每页显示条目数
const referenceResults = ref([]);
const referencePageResults = ref([]);
const referenceTotalLength = ref(0);
const referenceCurrentPage = ref(1);
const citeResults = ref([]);
const citePageResults = ref([]);
const citeTotalLength = ref(0);
const citeCurrentPage = ref(1);
let workId = ref(0);

const ifGujia = ref(true);

watch(activeName, (newVal) => {
  localStorage.setItem("activeTab", newVal);
});

const updateReferencePageResults = () => {
  const start = (referenceCurrentPage.value - 1) * pageSize;
  const end = start + pageSize;
  referencePageResults.value = referenceResults.value.slice(start, end);
};

const updateCitePageResults = () => {
  const start = (citeCurrentPage.value - 1) * pageSize;
  const end = start + pageSize;
  citePageResults.value = citeResults.value.slice(start, end);
};
const handleReferencePageChange = (newPage) => {
  referenceCurrentPage.value = newPage;
  updateReferencePageResults();  // 更新当前页的数据
};
const handleCitePageChange = (newPage) => {
  citeCurrentPage.value = newPage;
  updateCitePageResults();  // 更新当前页的数据
};


const getWork = async (workId) => {
  try {
    const res = await httpUtil.get('/openalex/work/get', {
      workId: workId,
    });
    console.log(res.data);

    const data = res.data.work;
    citeNum.value = data.citedByCount
    /**
     * title
     */
    title.value = data.title;

    /**
     * Author
     */
    console.log("不嘻嘻")

    console.log(data.workAuthorResultDtos)
    data.workAuthorResultDtos.forEach((authorDto) => {
      if(authorDto.authorResultDto !== null) {
        const authorName = authorDto.authorResultDto.authorName[0];
        const authorId = authorDto.authorResultDto.authorId;

        const citedByCount = authorDto.authorResultDto.citedByCount;
        const worksCount = authorDto.authorResultDto.worksCount;

        auth.value.push({
          name: authorName,
          id: authorId,
          citedByCount: citedByCount,
          worksCount: worksCount
        });
      }
    });
    /**
     * Doi
     */
    if (data.doi) {
      // 直接修改 doi.value
      doi.value = data.doi.replace(/^https?:\/\//, "");
    }

    if (data.abstractText) {
      abstract.value = data.abstractText;
    }

    if (data.citedByApiUrl) {
      citedByApiUrl.value = data.citedByApiUrl;
    }

    conceptList.value = data.worksConceptsList.map(item => item.displayName);

    if (data.publicationDate) {
      publicationDate.value = data.publicationDate;
    }

  } catch (error) {
    console.error("Failed to fetch data:", error);
    // title.value = "AAA Revisited: A Comprehensive Review of Risk Factors, Management, and Hallmarks of Pathogenesis";

  }
};

onMounted(async () => {

  const route = useRoute();
  const savedTab = localStorage.getItem("activeTab");
  if (savedTab) {
    activeName.value = savedTab;
  }

  // 从查询参数中获取 id
  workId = route.query.id;
  console.log(workId);

  await getWork(workId);

  await getReference()
  await getCite()

  await updateCitePageResults();
  ifGujia.value = false;
  await updateReferencePageResults();

  haveFavorite();

      //获取评论
      console.log(workId);
      await httpUtil.get('/user/workFavoriteNum', {
        publicationId: workId
      }).then(res => {
            collectNum.value = res.data.favoriteNum
          }
      )

      const resComments = await httpUtil.get('/comment/get', {
        workId: workId
      })
      comments.value = resComments.data.comments;
      commentNum.value = comments.value.length;

      //获取推荐
      const resCommends = await httpUtil.get('/suggest/works', {
        id: workId
      });
      console.log('获取' + resCommends.data)
      recommends.value = resCommends.data.suggests;
      console.log(recommends.value);

  await httpUtil.post2('user/addHistory', {
    publicationId: workId,
    tstring : getCurrentDateTime(),
    userId: userId.value
  });

    }
)

const padZero = (num) => {
  return num < 10 ? '0' + num : num;
}
const getCurrentDateTime = () =>{
  const now = new Date();
  const year = now.getFullYear();
  const month = padZero(now.getMonth() + 1); // 月份是从0开始的
  const day = padZero(now.getDate());
  const hours = padZero(now.getHours());
  const minutes = padZero(now.getMinutes());
  const seconds = padZero(now.getSeconds());
  // 构建 $date-time 格式的字符串
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

const getReference = async () => {
  try {
    const res = await httpUtil.get('/openalex/work/getWorkItsReference', {
      workId: workId,
    });
    console.log("参考文献");
    console.log(res.data);
    referenceResults.value = res.data.work;
    referenceTotalLength.value = referenceResults.value.length;
    referenceNum.value = referenceTotalLength.value;
  } catch (error) {
    console.error("获取参考文献失败:", error);
    ElMessage.error("获取参考文献失败");
  }
}


let citeNumChange = useTransition(citeNum, {
  duration: 300,
})
let referenceNumChange = useTransition(referenceNum, {
  duration: 300,
})
let collectNumChange = useTransition(collectNum, {
  duration: 300,
})
let commentNumChange = useTransition(commentNum, {
  duration: 300,
})

//统计值在这里

isCollected.value = false
//


const getCite = async () => {
  try {
    const res = await httpUtil.get('/openalex/work/getWorkReferenceIt', {
      workId: workId,
    });
    console.log(res.data);
    citeResults.value = res.data.work;
    citeTotalLength.value = citeResults.value.length;
    //citeNum.value = citeTotalLength.value;
  } catch (error) {
    console.error("获取引用失败:", error);
    ElMessage.error("获取引用失败");
  }
}


const collectContent = computed(() =>
    isCollected.value ? '取消收藏' : '添加到收藏'
);

const toggleSite = () => {
  if (doi.value === 'Loading...') {
    ElMessage.error("抱歉，我们无法访问这篇文章的原文")
    return;
  }

  const urlTmp = ref(`https://www.${doi.value}`)
  window.open(urlTmp.value, "_blank");
}

const togglePdf = async () => {
  if (doi.value === 'Loading...') {
    ElMessage.error("抱歉，我们无法访问这篇文章的原文")
    return;
  }
  let tmp1;
  tmp1 = doi.value.split('doi.org/')[1];

  const urlTmp = ref(`https://www.wellesu.com/${tmp1}`)
  window.open(urlTmp.value, "_blank");
}

const toggleShare = () => {
  const currentUrl = window.location.href;  // 获取当前网址
  navigator.clipboard.writeText(currentUrl)  // 将网址复制到剪切板
  ElMessage({
    message: '网址链接已复制',
    type: 'success',
  })
}
const commentIndex = ref("");

const citeVisible = ref(false);
const citation = ref();
const gb7714 = ref();
const mla = ref();
const apa = ref();
const getRandom = (max) => Math.floor(Math.random() * max) + 1; // 随机生成号码

const toggleCite = () => {
  const authors = auth.value.map((author) => {
    const nameParts = author.name.split(" ");
    const lastName = nameParts.pop(); // 获取姓
    const initials = nameParts.map((n) => n[0]).join("."); // 获取名的首字母
    return `${lastName}, ${initials}.`;
  }).join(", ");

  const publicationYear2 = publicationDate.value || "Unknown Year"; // 使用默认值以防数据为空
  const title2 = title.value || "Unknown Title"; // 使用默认值
  const doi2 = doi.value || ""; // DOI可能为空，确保不会出现undefined或对象
  const journalName = "Unknown Journal";
  const volume = getRandom(35); // 假设卷号
  const issue = getRandom(12); // 假设期号
  const pageFrom = getRandom(50);
  const pageBetween = getRandom(8) + 2;
  const pages = `${pageFrom}-${pageFrom + pageBetween}`; // 假设页码

  // GB/T 7714 格式
  gb7714.value = `${authors}. ${title2}[J]. ${journalName}, ${publicationYear2}, ${volume}(${issue}): ${pages}. DOI:${doi2}`;

  // MLA 格式
  mla.value = `${authors}. "${title2}." ${journalName}, vol. ${volume}, no. ${issue}, ${publicationYear2}, pp. ${pages}. DOI:${doi2}.`;

  // APA 格式
  apa.value = `${authors}. (${publicationYear2}). ${title2}. ${journalName}, ${volume}(${issue}), ${pages}. https://doi.org/${doi2}`;


  citeVisible.value = true;
  console.log(citation)
  console.log(doi.value)
}

const copyGBT = () => {
  navigator.clipboard.writeText(gb7714.value) // 使用 Clipboard API 复制到剪贴板
      .then(() => {
        ElMessage.success("内容已复制到剪贴板");
      })
      .catch((err) => {
        console.error("复制失败", err);
      });
};

const copyMLA = () => {
  navigator.clipboard.writeText(mla.value) // 使用 Clipboard API 复制到剪贴板
      .then(() => {
        ElMessage.success("内容已复制到剪贴板");
      })
      .catch((err) => {
        console.error("复制失败", err);
      });
};

const copyAPA = () => {
  navigator.clipboard.writeText(apa.value) // 使用 Clipboard API 复制到剪贴板
      .then(() => {
        ElMessage.success("内容已复制到剪贴板");
      })
      .catch((err) => {
        console.error("复制失败", err);
      });
};


//collect
const collectVisible = ref(false);
const collectTag = ref([])
const tagInputValue = ref('')
const tagInputVisible = ref(false)

const toggleCollect = async () => {
  tagInputVisible.value = false;
  if (!isCollected.value) {
    if (userId.value == null || !userId.value) {
      ElMessage.warning("请登录后再操作");
      return;
    }
    try {
      const res = await httpUtil.get('/user/viewAllTags', {
        userId: userId.value,
      });
      collectTag.value = (res.data.tags || []).map(item => item.tag);
    } catch (error) {
      console.error("获取标签失败:", error);
      ElMessage.error("获取标签失败");
    }
    collectVisible.value = true;
  }

  if (isCollected.value) {
    await deleteFavorite()
    collectNum.value--;
    isCollected.value = !isCollected.value;
  }
}

const addTag = () => {
  try {
    httpUtil.post2('/user/createNewTag', {
      tag: tagInputValue.value,
      userId: userId.value,
    });
  } catch (error) {
    console.error("新建标签失败:", error);
    ElMessage.error("新建标签失败");
  }
}

const selectedTags = ref([]);
const handleCollectSubmit = async () => {
  try {

    console.log('Request Payload:', {
      publicationId: workId,
      tags: [...selectedTags.value],
      userId: userId.value,
    });


    const res = await httpUtil.post('/user/addUserFavorite', {
      publicationId: workId,
      tags: [...selectedTags.value],
      userId: userId.value
    });
    if (res.data.code === 200) {
      ElMessage.success("收藏成功！");
      isCollected.value = true;
      collectNum.value++;
    } else {
      ElMessage.warning(res.data.message || "收藏失败");
    }
  } catch (error) {
    console.error("收藏失败:", error);
    ElMessage.error("收藏失败");
  } finally {
    collectVisible.value = false;
  }
};

const showInput = () => {
  tagInputVisible.value = true

}

const handleInputConfirm = () => {
  if (tagInputValue.value) {
    addTag()
    collectTag.value.push(tagInputValue.value)
  }
  tagInputVisible.value = false
  tagInputValue.value = ''
}

const haveFavorite = async () => {
  try {
    if(userId.value == null || !userId.value) {
      console.log("获取收藏失败,请先登录");
      ElMessage.error("请先登录！");
      setTimeout(()=>{
        router.push('/login');
      },500);
      return;
    }

    const res = await httpUtil.get('/user/haveFavorite', {
      publicationId: workId,
      userId: userId.value
    })
    isCollected.value = res.data.haveFavorite;
  } catch (e) {
    console.error(e);
  }
}
const deleteFavorite = async () => {
  try {
    const res = await httpUtil.get('/user/deleteUserFavorite', {
      publicationId: workId,
      userId: userId.value
    })
    if (res.data.code === 200) {
      ElMessage.success("取消收藏成功")
    } else {
      ElMessage.error("取消收藏失败")
    }

  } catch (e) {
    console.error(e);
    ElMessage.error("取消收藏失败");
  }
}
const submitComment = async () => {
  try {
    if (localStorage.getItem("userId") === null || !localStorage.getItem("userId")) {
      ElMessage.error('请先登录!')
      return;
    }
    const res = await httpUtil.get('/comment/insert', {
      workId: workId,
      commentIndex: commentIndex.value
    }, {
      Authorization: cookieUtil.getCookie("token")
    })
    console.log(res.data);
    if (res.data.msg === 'insert comment success') {
      ElMessage.success("评论成功！");
      location.reload();
    } else {
      ElMessage.warning("发送失败");
    }
  } catch (e) {
    console.error(e);
    ElMessage.error("发送失败");
  }
}


const comments = ref([]);

/**
 * 推荐部分
 */
const recommends = ref([]);

</script>

<template>
  <div id="app" class="container">
    <div class="main-part">
      <div class="left-part">
        <!-- Header Section -->
        <div class="header">
          <div v-if="ifGujia">
            <el-skeleton :rows="2" animated/>
          </div>
          <h1 v-else v-html="title"></h1>

          <p>
            <span v-for="(author, index) in auth" :key="index">
              <a
                  :href="'http://localhost:2221/#/authorInfo?id=' + author.id + '&name=' + author.name + '&citedByCount=' + author.citedByCount + '&worksCount=' + author.worksCount"
                  target="_blank"
                  class="author-link"
              >
                {{ author.name }}
              </a>
              <span v-if="index < auth.length - 1">, </span>
            </span>
            • Published in Biomedicines {{ publicationDate }}
          </p>

          <p>
            <span v-for="(concept, index) in conceptList.slice(0, 4)" :key="index">
              {{ concept }}
            <span v-if="index < Math.min(conceptList.length, 4) - 1"> • </span>
            </span>
            <span v-if="conceptList.length > 4"> • {{ conceptList.length - 4 }} More</span>
          </p>

          <p>
            <strong>DOI:</strong> {{ doi }}</p>

        </div>

        <!-- TLDR Section -->
        <div class="abstract">
          <strong>Abstract:</strong>
          <br>
          <div v-if="ifGujia">
            <el-skeleton :rows="4" animated/>
          </div>
          <div v-else v-html="abstract"></div>
        </div>
        <div class="option-part">


          <el-button
              class="pdf-button"
              :icon="Document"
              type="danger"
              @click="togglePdf"
          >
            查看PDF
          </el-button>


          <el-tooltip
              effect="dark"
              content="分享">
            <el-button
                :icon="Share"
                class="share-button"
                circle
                @click="toggleShare"
            >
            </el-button>
          </el-tooltip>

          <el-tooltip
              effect="dark"
              content="引用">
            <el-button
                :icon="Paperclip"
                class="cite-button"
                circle
                @click="toggleCite"
            >
            </el-button>
          </el-tooltip>


          <el-tooltip
              effect="dark"
              :content="collectContent">
            <el-button
                :icon="isCollected? StarFilled:Star"
                :class="['collect-button', { 'collect-button-collected': isCollected }]"
                circle
                @click="toggleCollect"
            >
            </el-button>
          </el-tooltip>

          <el-button
              class="site-button"
              :icon="Link"

              @click="toggleSite"
          >
            原文网址
          </el-button>

        </div>

        <el-tabs v-model="activeName" class="down-tabs" type="card">
          <el-tab-pane label="参考文献" name="first">
            <div v-if="referenceResults.length!==0" style="display: flex;">
              <div>
                <SingleResult style="max-width: 100%"
                              v-for="result in referencePageResults" :author="result.paperInformation"
                              :content="result.abstractText"
                              :title="result.title" :cited="result.cited" :id="result.id "
                >
                </SingleResult>
              </div>
            </div>
            <div v-else>
              <el-empty :image=robotImage
                        description="这篇论文没有参考文献"/>
            </div>
            <el-pagination
                v-if="referenceTotalLength > 0"
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :total="referenceTotalLength"
                @current-change="handleReferencePageChange"
                :current-page="referenceCurrentPage"
            />
          </el-tab-pane>
          <el-tab-pane label="引用" name="second">
            <div v-if="ifGujia">
              <el-skeleton :rows="2" animated/>
            </div>
            <div v-else>
              <div v-if="citeResults.length!==0" style="display: flex;">
                <div>
                  <SingleResult style="max-width: 100%"
                                v-for="result in citePageResults" :author="result.paperInformation"
                                :content="result.abstractText"
                                :title="result.title" :cited="result.cited" :id="result.id"
                  ></SingleResult>
                </div>
              </div>
              <div v-else>
                <el-empty :image=robotImage
                          description="这篇论文没有被引用过"/>
              </div>
            </div>


            <el-pagination
                v-if="citeTotalLength > 0"
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :total="citeTotalLength"
                @current-change="handleCitePageChange"
                :current-page="citeCurrentPage"
            />
          </el-tab-pane>
          <el-tab-pane label="评论" name="third">
            <div style="margin-left:3%;margin-bottom: 1%">
              <el-input placeholder="在这留下你的高见~" style="width: 80%; height: 35px;" v-model="commentIndex">
              </el-input>
              <el-button @click="submitComment" style="height: 35px;">发送</el-button>

            </div>
            <div v-if="comments.length > 0">
              <SingleComment v-for="comment in comments" :key="comment.id"
                             :comment-index="comment.commentIndex"
                             :user-name="comment.userName"
                             :comment-id="comment.id"
                             :work-id="workId"
                             :likes="comment.likes"
                             :date="comment.date"
                             :user-id="comment.userId"/>
            </div>
            <div v-else>
              <el-empty :image=robotImage
                        description="哈哈,这篇论文没有评论"/>
            </div>
          </el-tab-pane>
        </el-tabs>

      </div>
      <div class="side">
        <el-card class="stat-part" shadow="always">
          <el-row style="margin-bottom: 15px">
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="被引用次数" :value="citeNumChange" :value-style="{ color: '#132fc1' }"/>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="参考文献数" :value="referenceNumChange" :value-style="{ color: '#45a801' }"/>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="平台收藏量" :value="collectNumChange" :value-style="{ color: '#b10101' }"/>
            </el-col>
            <el-col :span="12" style="padding-left: 12px">
              <el-statistic title="文章评论量" :value="commentNumChange" :value-style="{ color: '#dd8500' }"/>
            </el-col>
          </el-row>

        </el-card>
        <!-- Form Section -->
        <div class="suggested-papers">
          <span>相关论文推荐</span>
          <SingleRecommend v-for="recommend in recommends" :key="recommend.id" :date="recommend.date"
                           :title="recommend.title" :id="recommend.id"/>
        </div>
      </div>
    </div>
  </div>

  <!-- 收藏弹窗 -->
  <el-dialog
      title="添加到收藏"
      v-model="collectVisible"
      center
      width="400px"
  >

    <!-- 标签选择 -->
    <div style="margin-left: 10px;">
      <el-checkbox-group v-model="selectedTags" style="font-size: 16px;  display: flex; flex-direction: column;"
                         class="custom-checkbox-group">
        <el-checkbox
            v-for="tag in collectTag"
            :label="tag"
            :key="tag"
            size="large"
        >
          {{ tag }}
        </el-checkbox>
      </el-checkbox-group>


      <!-- 新增标签按钮 -->
      <el-input
          v-if="tagInputVisible"
          ref="InputRef"
          v-model="tagInputValue"
          size="default"
          @keyup.enter="handleInputConfirm"
          @blur="handleInputConfirm"
          style="margin-top:10px;width: 150px"
      />
      <el-button
          v-else
          size="small"
          @click="showInput"
          style="background: rgba(165, 165, 165, 0.4); color: #5a5a5a;margin-top: 10px; margin-bottom: 10px;"
      >
        + New Tag
      </el-button>

    </div>
    <!-- 提交按钮居中 -->
    <span
        slot="footer"
        class="dialog-footer"
        style="display: flex; justify-content: center;margin-top: 35px"
    >
    <el-button style="padding-left: 30px;padding-right: 30px" type="primary"
               @click="handleCollectSubmit">提交  </el-button>
  </span>
  </el-dialog>

  <!-- 引用弹窗 -->
  <el-dialog
      v-model="citeVisible"
      center
      width="550px"
  >
    <template #header>
      <div class="my-header">
        <div style="margin-left: 25px;font-size: 20px;margin-bottom: -10px;margin-top: 5px">引用文章</div>
      </div>
    </template>

    <div class="citation-container">
      <h4>GB/T 7714</h4>
      <div class="citation-content">
        {{ gb7714 }}
      </div>
    </div>
    <div class="citation-container">
      <h4>MLA</h4>
      <div class="citation-content">
        {{ mla }}
      </div>
    </div>
    <div class="citation-container">
      <h4>APA</h4>
      <div class="citation-content">
        {{ apa }}
      </div>
    </div>
    <!-- 复制按钮居中 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="copyGBT"
                   style="background-color: transparent;color: #0003c5;margin-right: -7px;margin-left: -7px">
          复制GB/T 7714
        </el-button>
        <el-button @click="copyMLA" style="background-color: transparent;color: #0003c5;margin-right: -7px">
          复制MLA
        </el-button>
        <el-button @click="copyAPA" style="background-color: transparent;color: #0003c5;margin-right: -7px">
          复制APA
        </el-button>
        <el-button @click="citeVisible = false" style="background-color: transparent;color: #0003c5;">退出</el-button>
      </div>
    </template>
  </el-dialog>

</template>


<style scoped>

.citation-container {
  display: flex;
  justify-content: space-between; /* 左右对齐 */
  align-items: baseline;
  margin-bottom: 15px;
  margin-left: 20px;
  margin-right: 15px;
}

h4 {
  color: #7a7a7a;
  margin-right: 20px; /* 给标题和内容之间加个间隔 */
}


.citation-content {
  flex-grow: 1; /* 让内容占满剩余空间 */
}

.custom-checkbox-group :deep(.el-checkbox__label) {
  font-size: 15px !important;
}


.el-popper.is-customized {
  /* Set padding to ensure the height is 32px */
  padding: 6px 12px;
  background: linear-gradient(90deg, rgb(159, 229, 151), rgb(204, 229, 129));
}

.el-popper.is-customized .el-popper__arrow::before {
  background: linear-gradient(45deg, #b2e68d, #bce689);
  right: 0;
}

.container {
  padding: 0 20px 20px 0;
  max-width: 1200px;
  margin: auto;
  font-family: Arial, sans-serif;
  line-height: 1.6;
}

.main-part {
  display: flex;
  width: 1200px; /* Ensure the width is exactly 1100px */
  margin: -10px auto; /* Center the content */

}


.left-part {
  flex: 800px; /* Take more space for the main section */
  padding: 20px;
  margin: 0;
}

.option-part {
  flex: 800px;
  display: flex;
  align-items: center;
}

.pdf-button {
  background: #e10000;
}

.pdf-button:hover {
  background: #ec5454;
}

.site-button {
  background: rgba(0, 0, 0, 0);
  color: #3200af;

  text-decoration: underline;
  font-size: 16px;
  margin-left: -1px;
  margin-bottom: -3px;
}

.site-button:hover {
  background: rgba(112, 112, 112, 0);
  color: #008be3;
}


.collect-button {
  color: #7a7a7a;
  background-color: #ffffff;
}

.collect-button-collected {
  color: #fadd21;
  background-color: #ffffff;
}

.author-link {
  text-decoration: none;
  color: #1F578F;
}

.author-link:hover {
  text-decoration: underline;
}

.collect-button:hover {
  color: #fff14e;
  background-color: #ffffff;
}

:deep(.collect-button .el-icon) {
  font-size: 28px;
}

.share-button {
  color: #FFFFFF;
  background-color: #ff5892;
}

.share-button:hover {
  color: #ffe595;
  background-color: #ff7ca9;
}

.cite-button {
  color: #ffffff;
  background: #006eff;
}

.cite-button:hover {
  color: rgb(255, 255, 255);
  background: #338aff;
}

.down-tabs {
  padding-top: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.side {
  flex: 320px; /* Take less space for the sidebar */
  padding: 50px 20px 20px 20px;
}

.stat-part {
  width: 320px;
  margin-bottom: 30px;
}

.header h1 {
  font-size: 24px;
  margin-bottom: 10px;
}

.header p {
  font-size: 14px;
  color: #666;
}

.abstract {
  background-color: #f9f9f9;
  padding: 10px;
  margin: 20px 0;
  border-left: 4px solid #C3EEFD;
}

.reference {
  margin: 20px 0;
}

.suggested-papers {
  padding: 20px 20px 20px 20px;
  border-radius: 5px;
  border: #e1e1e1 1px solid;
}


.textarea {
  margin-bottom: 15px;
  padding: 10px;
  font-size: 14px;
  width: 100%;
  height: 80px;

}

button {
  padding: 10px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.search-button {
  color: rgb(0, 58, 255);
  background-color: rgba(255, 255, 255, 0.5);
  border: 1px solid #656565;
}

.search-button:hover {
  color: rgb(255, 255, 255);
  background-color: rgb(14, 136, 255);
  border: 1px solid rgb(14, 136, 255);

}
</style>
