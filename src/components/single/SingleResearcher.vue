<template>
  <div class="researcher-details">
    <div class="researcher-header">
      <div class="researcher-left">
        <el-avatar :src="avatar" :size="80" />
        <div class="researcher-info" @click="GoToPersonalPortal(authorId)" >
          <h2 ref="abc" @mouseover="toggleUnderline" @mouseleave="toggleUnderline">{{ authorName }}</h2>
          <p><span style="font-weight: lighter;">学者id</span>: <span style="color: grey;">{{ authorId }}</span></p>
          <p><span style="font-weight: lighter;">所属机构</span>: <span style="color: grey;">{{ institution }}</span></p>
          <p><span style="font-weight: lighter;">认证邮箱</span>: <span style="color: grey;">{{ mail }}</span></p>
        </div>
      </div>

<!--      <div class="researcher-middle" >-->
<!--        &lt;!&ndash; <p><strong>总被引量：{{ citedByCount }}</strong></p>-->
<!--        <p><strong>总发文量：{{ worksCount }}</strong></p> &ndash;&gt;-->
<!--        <p>总被引量：{{ citedByCount }}</p>-->
<!--        <p>总发文量：{{ worksCount }}</p>-->
<!--      </div>-->

<!--      <div class="researcher-right" >-->
<!--        <p>H指数：{{ H_index }}</p>-->
<!--        <p>第一作者发文量：{{ firstresearcher }}</p>-->
<!--        <p>高影响力论文发文量：{{ highInflu }}</p>-->
<!--      </div>-->
    </div>

    <!-- <div class="article">
        <p><strong>代表文章</strong>：{{ publications[0].title }}</p>
    </div> -->

    <el-divider/>
  </div>
</template>

<script>
import axios from 'axios';
import * as heading from "@fortawesome/vue-fontawesome/src/utils.js";
export default {
  name: 'SingleResearcher',
  props: {
    authorId: {
      type: String,
      required: true,
      default:'胡春明'
    },
    authorName: {
      type: String,
      required: true
    },
    institution: {
      type: String,
      required: true,
      default:'未知机构'
    },
    area: {
      type: String,
      required: true,
      default:'未知领域'
    },
    mail: {
      type: String,
      required: true,
      default:''
    },
    avatar: {
      type: String,
      required: false,
      default: ''
    },
    citedByCount: {
      type: Number,
      required: true
    },
    worksCount: {
      type: Number,
      required: true
    },
    H_index: {
      type: Number,
      required: true
    },
    firstresearcher: {
      type: Number,
      required: true
    },
    highInflu: {
      type: Number,
      required: true
    },

  },

  methods: {
    followresearcher() {
      console.log(`Following researcher: ${this.name}`);
    },
    viewPublication(id) {
      console.log(`Viewing publication with ID: ${id}`);
    },
    fetchresearcherDetails() {
      axios.get(`/api/researcher/${this.id}`) // 改后端接口
          .then(response => {
            const data = response.data;
            this.name = data.name;
            this.description = data.description;
            this.avatar = data.avatar;
            this.citedByCount = data.citedByCount;
            this.worksCount = data.worksCount;
          })
          .catch(error => {
            console.error('获取作者信息错误', error);
          });
    },
    fetchresearcherPublications() {
      axios.get(`/api/researcher/${this.id}/publications`) // 改后端接口
          .then(response => {
            this.publications = response.data;
          })
          .catch(error => {
            console.error('获取作者作品信息错误', error);
          });
    },
    GoToPersonalPortal(authorId) {
      this.$router.push({ path: '/user/personalInfo', query: { id: authorId } });
    },
    toggleUnderline() {
      // 检查是否已经添加了underline类
      if (this.$refs.abc.style.textDecoration === 'underline') {
        // 如果有underline类，则移除它
        this.$refs.abc.style.textDecoration = 'none'
      } else {
        // 如果没有underline类，则添加它
        this.$refs.abc.style.textDecoration = 'underline'
      }
    }
  },
};
</script>

<style scoped>
.researcher-details {
  margin-bottom: 0px;
}
.researcher-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin:0px;
}
.researcher-left {
  margin: 0px;
  display: flex;
  align-items: center;
}
.researcher-info h2 {
  font-size: 20px;
  margin-top: 0px;
  margin-bottom: 10px;
  margin-left:20px;
  cursor: pointer;
}
.researcher-info .description {
  color: #888;
}
.researcher-info p {
  margin: 2px 0;
  margin-left:20px;
}
.researcher-middle{
  margin: 0px;
}
.researcher-middle p{
  margin: 0px;
}
.researcher-right p {
  margin: 0px;
  color: #333;
}
css
.researcher-right {
  flex: 1;
  text-align: center;
  margin-bottom:0px;
  margin-left:20%;
}
.researcher-publications {
  margin-top:0px;
}
.publication-link {
  color: #409eff;
  cursor: pointer;
  text-decoration: underline;
}
.publication-link:hover {
  text-decoration: none;
}
.researcher-timeline {
  margin-top: 0px;
}
.vertical-divider {
  height: 80%;
  margin: 0px;
}
.article{
  margin-left: 20px;
  color:rgba(34, 0, 255, 0.759)
}
.underline {
  text-decoration: underline; /* 添加下划线 */
  color: #2c00a9;
}
</style>
