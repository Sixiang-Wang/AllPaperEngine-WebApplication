<script setup>
import { ref, onMounted } from "vue";
import * as d3 from 'd3';
import cloud from 'd3-cloud';
import Aside from "@/components/HotspotAside.vue";

// 示例词云数据
const words = [
  {text: "AI", size: 2},
  {text: "Machine Learning", size: 3},
  {text: "Vue", size: 10},
  {text: "JavaScript", size: 15},
  {text: "React", size: 20},
  {text: "Python", size: 5},
  {text: "Data Science", size: 7},
  {text: "Web Development", size: 30},
  {text: "Node.js", size: 12},
  {text: "Java", size: 18},
  {text: "CSS", size: 4},
  {text: "HTML", size: 6},
  {text: "D3.js", size: 8},
  {text: "Docker", size: 14},
  {text: "Cloud Computing", size: 25},
  {text: "Big Data", size: 40},
  {text: "Blockchain", size: 60},
  {text: "Cybersecurity", size: 50},
  {text: "API", size: 10},
  {text: "Git", size: 9},
  {text: "GraphQL", size: 13},
  {text: "Kubernetes", size: 22},
  {text: "Software Engineering", size: 30},
  {text: "Frontend", size: 11},
  {text: "DevOps", size: 20},
  {text: "TypeScript", size: 17},
  {text: "Cloud Storage", size: 8},
  {text: "Java EE", size: 6},
  {text: "SQL", size: 9},
  {text: "Graph Databases", size: 25},
  {text: "NoSQL", size: 28},
  {text: "Data Visualization", size: 35},
  {text: "Serverless", size: 18},
  {text: "CI/CD", size: 12},
  {text: "Agile", size: 14},
  {text: "Software Testing", size: 17},
  {text: "Containerization", size: 22},
  {text: "Microservices", size: 40},
  {text: "Automation", size: 19},
  {text: "Network Security", size: 45},
  {text: "Quantum Computing", size: 50},
  {text: "Cloud Infrastructure", size: 38},
  {text: "Blockchain Development", size: 55},
  {text: "Digital Transformation", size: 33},
  {text: "Robotic Process Automation", size: 42}
];

// 用于绑定到 DOM 元素的 ref
const wordCloudRef = ref(null);

// 渲染词云
onMounted(() => {
  const layout = cloud()
      .size([window.innerWidth * 0.8, window.innerHeight * 0.8])
      .words(words)
      .font("Impact")
      .fontSize(d => Math.pow(d.size, 1.2))
      .rotate(() => Math.floor(Math.random() * 181) - 90)
      .on("end", drawCloud);

  layout.start();

  // 绘制词云
  function drawCloud(words) {
    d3.select(wordCloudRef.value)
        .append("svg")
        .attr("width", layout.size()[0])
        .attr("height", layout.size()[1])
        .append("g")
        .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")")
        .selectAll("text")
        .data(words)
        .enter()
        .append("text")
        .style("font-size", d => d.size + "px")
        .style("font-family", "Impact")
        .style("fill", "steelblue")
        .attr("text-anchor", "middle")
        .attr("transform", d => "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")")
        .text(d => d.text)
        .on("mouseover", function (event, d) {
          d3.select(this).style("fill", "orange");
        })
        .on("mouseout", function (event, d) {
          d3.select(this).style("fill", "steelblue");
        })
        .on("click", function (event, d) {
          alert("你点击了: " + d.text); // 可根据需求实现跳转
        });
  }
});
</script>

<template>
  <el-container>
    <el-header>
      <h1>热点领域分析</h1>
      <p>以下是技术领域中的一些关键技术与概念的词云，词语的大小代表其相关性或热度。</p>
    </el-header>
    <el-container>
      <el-aside>
        <Aside/>
      </el-aside>
      <el-main>
        <!-- 用于显示词云的容器 -->
        <div ref="wordCloudRef"></div>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
el-header {
  background-color: #409EFF;
  color: white;
  padding: 20px;
  text-align: center;
}

el-main {
  background-color: #f4f4f4;
  padding: 20px;
}

div {
  margin: 20px;
}
</style>
