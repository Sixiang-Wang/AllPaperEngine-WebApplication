<template>
    <div class="author-details">
        <div class="author-header" style="display: flex; justify-content: space-between; align-items: center;">
        <div class="author-left" style="display: flex; align-items: center;">
        <el-avatar :src="avatar" :size="120" />
        <div class="author-info" style="margin-left: 20px;">
            <h2>{{ name }}</h2>
            <p><span style="font-weight: bold;">学者id</span>: <span style="color: grey;">{{ id }}</span></p>
            <p><span style="font-weight: bold;">工作单位</span>: <span style="color: grey;">{{ workPlace }}</span></p>
            <p><span style="font-weight: bold;">领域</span>: <span style="color: grey;">{{ area }}</span></p>
        </div>
    </div>

        <div class="author-right" style="flex: 1; text-align: left; padding-left: 10%;">
            <p><strong>总被引量：{{ citedByCount }}</strong></p>
            <p><strong>总发文量：{{ worksCount }}</strong></p>
            <p><strong>H指数：{{ H_index }}</strong></p>
            <p><strong>第一作者发文量：{{ firstAuthor }}</strong></p>
            <p><strong>高影响力论文发文量：{{ highInflu }}</strong></p>
        </div>
    </div>

    <div class="article">
        <p><strong>代表文章</strong>：{{ publications[0].title }}</p>
    </div>

    <el-divider/>
    </div>
</template>
  
<script>
import axios from 'axios';
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
        required: true
      },
      area: {
        type: String,
        required: true
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
      publications: {
        type: Array,
        required: true,
        default: () => []
      }
    },

    // name: 'AuthorDetails',
    // data() {
    //   return {
    //     id: '114514',
    //     name: '王思翔',
    //     workPlace: '北京航空航天大学',
    //     area: '软件工程，计算机科学与技术',
    //     avatar: '',
    //     citedByCount: 10,
    //     worksCount: 20,
    //     H_index:99,
    //     firstAuthor:5,
    //     highInflu:15,
    //     publications: [
    //         { id: 1, title: 'REVISEVAL IMPROVING LLM-AS-A-JUDGE VIA  RESPONSE-ADAPTED REFERENCES.', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
    //         { id: 2, title: 'Ciallo～(∠・ω< )⌒☆', description: '柚子厨蒸鹅心', date: '2024-03-22', cited: 20 },
    //         { id: 3, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
    //         { id: 4, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
    //         { id: 5, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
    //         { id: 6, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 10 },
    //         { id: 7, title: '我要学猛虎下山', description: '一只鸡煲的自述', date: '2024-01-15', cited: 20 }
    //     ],
    //   };
    // },
    /*
    created() {
      this.fetchAuthorDetails();
      this.fetchAuthorPublications();
    },
    */
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
    },
  };
  </script>
  
<style scoped>
.author-details {
max-width: 100%;
margin: 20px auto;
}
.author-header {
display: flex;
justify-content: space-between;
align-items: center;
}
.author-left {
display: flex;
align-items: center;
}
.author-info h2 {
margin: 0;
font-size: 20px;
}
.author-info .description {
color: #888;
}
.author-right p {
margin: 5px 0;
color: #333;
}
css
.author-right {
flex: 1;
text-align: center;
}
.follow-button {
margin-top: 10px;
}
.author-publications {
margin-top: 20px;
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
margin-top: 20px;
}
.vertical-divider {
height: 80%; 
margin: 20px; 
}
.article{
margin-left: 20px;
color:rgba(34, 0, 255, 0.759)
}
</style>
