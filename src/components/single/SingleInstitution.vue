<template>
    <div class="institution-details">
        <div class="institution-header">
        <div class="institution-left">
        <el-avatar :src="avatar" :size="80" />
            <div class="institution-info">
                <h2 class="hover-effect" @click="goToInstitutionInfo">{{ name }}</h2>
                <p><span style="font-weight: lighter;">机构id</span>: <span style="color: grey;">{{ id }}</span></p>
                <p><span style="font-weight: lighter;">评分</span>: <span style="color: grey;">{{ score }}</span></p>
                <p><span style="font-weight: lighter;">主页链接</span>: <span style="color: grey;">{{ homepageUrl }}</span></p>
            </div>
        </div>

        <div class="institution-middle" >
            <!-- <p><strong>总被引量：{{ citedByCount }}</strong></p>
            <p><strong>总发文量：{{ worksCount }}</strong></p> -->
            <p>总被引量：{{ citedByCount }}</p>
            <p>总发文量：{{ worksCount }}</p>
        </div>

        <div class="institution-right" >
            <p>国家{{ country }}</p>
            <p>上次更新时间{{ updateDate }}</p>
            <p>类型{{ type }}</p>
        </div>
    </div>
    <el-divider/>
    </div>
</template>

<script>
import axios from 'axios';
import router from "@/router/index.js";
  export default {
    name: 'SingleInstitution',
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
        score: {
            type: String,
            required: false,
            default:'未知机构'
        },
        homepageUrl: {
            type: String,
            required: false,
            default:'未知机构'
        },
        citedByCount: {
            type: String,
            required: false,
            default:'未知机构'
        },
        worksCount: {
            type: String,
            required: false,
            default:'未知机构'
        },
        country: {
            type: String,
            required: false,
            default:'未知机构'
        },
        updateDate: {
            type: String,
            required: false,
            default:'未知机构'
        },
        type: {
            type: String,
            required: false,
            default:'未知机构'
        },
    },
    methods: {
    goToInstitutionInfo() {
      router.push({
        name: 'institutionInfo',
        path: '/institutionInfo',
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
.institutionInfo-details {
margin-bottom: 0px;
}
.institutionInfo-header {
display: flex;
justify-content: space-between;
align-items: center;
margin:0px;
}
.institutionInfo-left {
margin: 0px;
display: flex;
align-items: center;
}
.institutionInfo-info h2 {
font-size: 20px;
margin-top: 0px;
margin-bottom: 10px;
margin-left:20px;
}
.institutionInfo-info .description {
color: #888;
}
.institutionInfo-info p {
margin: 2px 0;
margin-left:20px;
}
.institutionInfo-middle{
margin: 0px;
}
.institutionInfo-middle p{
margin: 0px;
}
.institutionInfo-right p {
margin: 0px;
color: #333;
}
css
.institutionInfo-right {
flex: 1;
text-align: center;
margin-bottom:0px;
margin-left:20%;
}
.institutionInfo-publications {
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
