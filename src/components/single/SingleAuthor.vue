<template>
    <div class="author-details">
        <div class="author-header">
        <div class="author-left">
          <el-avatar :src="avatar" :size="80" />
            <div class="author-info">
              <h2 class="hover-effect" @click="goToAuthorInfo">{{ name }}</h2>
              <p><span style="font-weight: lighter;">学者id</span>: <span style="color: grey;">{{ id }}</span></p>
              <p><span style="font-weight: lighter;">工作单位</span>: <span style="color: grey;">{{ workPlace }}</span></p>
              <p><span style="font-weight: lighter;">领域</span>: <span style="color: grey;">{{ area }}</span></p>
            </div>
        </div>

        <div class="author-middle" >
            <!-- <p><strong>总被引量：{{ citedByCount }}</strong></p>
            <p><strong>总发文量：{{ worksCount }}</strong></p> -->
            <p>总被引量：{{ citedByCount }}</p>
            <p>总发文量：{{ worksCount }}</p>
        </div>

        <div class="author-right" >
            <p>H指数：{{ H_index }}</p>
            <p>第一作者发文量：{{ firstAuthor }}</p>
            <p>高影响力论文发文量：{{ highInflu }}</p>
        </div>
    </div>

    <!-- <div class="article">
        <p><strong>代表文章</strong>：{{ publications[0].title }}</p>
    </div> -->

    <el-divider/>
  </div>
</template>
  
<script>
import axios from 'axios';
import router from "@/router/index.js";
  export default {
    name: 'SingleAuthor',
    props: {
      id: {
        type: String,
        required: true,
        default:'胡春明'
      },
      name: {
        type: String,
        required: true
      },
      workPlace: {
        type: String,
        required: true,
        default:'未知机构'
      },
      area: {
        type: String,
        required: true,
        default:'未知领域'
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
      firstAuthor: {
        type: Number,
        required: true
      },
      highInflu: {
        type: Number,
        required: true
      },
    },
    methods: {
      followAuthor() {
        console.log(`Following author: ${this.name}`);
      },
      viewPublication(id) {
        console.log(`Viewing publication with ID: ${id}`);
      },
      fetchAuthorDetails() {
        axios.get(`/api/author/${this.id}`) // 改后端接口
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
      fetchAuthorPublications() {
        axios.get(`/api/author/${this.id}/publications`) // 改后端接口
          .then(response => {
            this.publications = response.data;
          })
          .catch(error => {
            console.error('获取作者作品信息错误', error);
          });
      },
    goToAuthorInfo() {
      router.push({
        name: 'authorInfo',
        path: '/authorInfo',
        query: {
          id: this.id,
          name: this.name,
          workPlace: this.workPlace,
          area: this.area,
          avatar: this.avatar,
          citedByCount: this.citedByCount,
          worksCount: this.worksCount,
          H_index: this.H_index,
          firstAuthor: this.firstAuthor,
          highInflu: this.highInflu
        }
      });
    }
  }
};
</script>
  
<style scoped>
.author-details {
margin-bottom: 0px;
}
.author-header {
display: flex;
justify-content: space-between;
align-items: center;
margin:0px;
}
.author-left {
margin: 0px;
display: flex;
align-items: center;
}
.author-info h2 {
font-size: 20px;
margin-top: 0px;
margin-bottom: 10px;
margin-left:20px;
}
.author-info .description {
color: #888;
}
.author-info p {
margin: 2px 0;
margin-left:20px;
}
.author-middle{
margin: 0px;
}
.author-middle p{
margin: 0px;
}
.author-right p {
margin: 0px;
color: #333;
}
css
.author-right {
flex: 1;
text-align: center;
margin-bottom:0px;
margin-left:20%;
}
.author-publications {
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
.author-timeline {
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
.hover-effect {
  text-decoration: none;
  transition: color 0.3s ease, text-decoration 0.3s ease;
}

.hover-effect:hover {
  color: rgba(34, 0, 255, 0.759);
  text-decoration: underline;
}
</style>
