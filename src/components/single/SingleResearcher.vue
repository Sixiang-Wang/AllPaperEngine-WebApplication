<template>
  <div class="researcher-details">
    <div class="researcher-header">
      <div class="researcher-left">
        <el-avatar :src="avatar" :size="80" />
        <div class="researcher-info" @click="GoToPersonalPortal" >
          <h2 @mouseover="toggleUnderline" @mouseleave="toggleUnderline">{{ name }}</h2>
          <p><span style="font-weight: lighter;">学者id</span>: <span style="color: grey;">{{ id }}</span></p>
          <p><span style="font-weight: lighter;">工作单位</span>: <span style="color: grey;">{{ workPlace }}</span></p>
          <p><span style="font-weight: lighter;">领域</span>: <span style="color: grey;">{{ area }}</span></p>
        </div>
      </div>

      <div class="researcher-middle" >
        <!-- <p><strong>总被引量：{{ citedByCount }}</strong></p>
        <p><strong>总发文量：{{ worksCount }}</strong></p> -->
        <p>总被引量：{{ citedByCount }}</p>
        <p>总发文量：{{ worksCount }}</p>
      </div>

      <div class="researcher-right" >
        <p>H指数：{{ H_index }}</p>
        <p>第一作者发文量：{{ firstresearcher }}</p>
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
export default {
  name: 'Singleresearcher',
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
    firstresearcher: {
      type: Number,
      required: true
    },
    highInflu: {
      type: Number,
      required: true
    },
    // publications: {
    //   type: Array,
    //   required: true,
    //   default: () => []
    // }
  },

  // name: 'researcherDetails',
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
  //     firstresearcher:5,
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
    this.fetchresearcherDetails();
    this.fetchresearcherPublications();
  },
  */
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
    GoToPersonalPortal() {
      // this.$router.push({ path: '/researcher', query: { id: this.id } });
    },
    toggleUnderline() {
      // 检查是否已经添加了underline类
      if (heading.classList.contains('underline')) {
        // 如果有underline类，则移除它
        heading.classList.remove('underline');
      } else {
        // 如果没有underline类，则添加它
        heading.classList.add('underline');
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
