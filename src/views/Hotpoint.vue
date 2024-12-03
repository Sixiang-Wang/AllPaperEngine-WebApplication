<script setup>
import { ref, onMounted, nextTick } from "vue";
import * as d3 from 'd3';
import cloud from 'd3-cloud';
import Aside from "@/components/HotspotAside.vue";
import { Chart, registerables } from 'chart.js';
import * as XLSX from 'xlsx';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';

Chart.register(...registerables);

// 示例词云数据
const wordCloudData = ref([
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
]);

// 用于绑定到 DOM 元素的 ref
const wordCloudRef = ref(null);
const showChart = ref(''); // 默认不显示任何图表

// 图表数据

const barChartData = ref({
  labels: wordCloudData.value.map(item => item.text),
  datasets: [{
    label: '技术热度',
    data: wordCloudData.value.map(item => item.size),
    backgroundColor: 'rgba(54, 162, 235, 0.2)',
    borderColor: 'rgba(54, 162, 235, 1)',
    borderWidth: 1
  }]
});


const lineChartData = ref({
  labels: wordCloudData.value.map(item => item.text),
  datasets: [{
    label: '技术热度趋势',
    data: wordCloudData.value.map(item => item.size),
    fill: false,
    borderColor: 'rgba(75, 192, 192, 1)',
    tension: 0.1
  }]
});

const pieChartData = ref({
  labels: wordCloudData.value.map(item => item.text),
  datasets: [{
    data: wordCloudData.value.map(item => item.size),
    backgroundColor: generateColors(50),
    hoverBackgroundColor: generateColors(50)
  }]
});

function generateColors(count) {
  const colors = [];
  const hueStep = 360 / count;  // 每个色相间隔
  for (let i = 0; i < count; i++) {
    let j = Math.floor(Math.random() * 85) + 1;
    const hue = j * hueStep;  // 通过色相进行分离
    const color = `hsl(${hue}, 70%, 60%)`; // 高饱和度，适中亮度的颜色
    colors.push(color);
  }
  return colors;
}

// 渲染词云
const renderWordCloud = () => {
  const layout = cloud()
    .size([window.innerWidth * 0.6, window.innerHeight * 0.6])
    .words(wordCloudData.value.map(word => ({ text: word.text, size: word.size })))
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
};

// 渲染柱状图
const renderBarChart = () => {
  const barChartCanvas = document.getElementById('barChartCanvas');
  const barCtx = barChartCanvas.getContext('2d');
  new Chart(barCtx, {
    type: 'bar',
    data: barChartData.value,
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
};

// 渲染折线图
const renderLineChart = () => {
  const lineChartCanvas = document.getElementById('lineChartCanvas');
  const lineCtx = lineChartCanvas.getContext('2d');
  new Chart(lineCtx, {
    type: 'line',
    data: lineChartData.value,
    options: {
      responsive: true
    }
  });
};

// 渲染饼图
const renderPieChart = () => {
  const pieChartCanvas = document.getElementById('pieChartCanvas');
  const pieCtx = pieChartCanvas.getContext('2d');
  new Chart(pieCtx, {
    type: 'pie',
    data: pieChartData.value,
    options: {
      responsive: true
    }
  });
};

// 切换图表类型
const toggleChart = (chartType) => {
  if (showChart.value === chartType) {
    showChart.value = '';
  } else {
    showChart.value = chartType;
    if (chartType === 'wordcloud') {
      nextTick(() => {
        renderWordCloud();
      });
    } else if (chartType === 'bar') {
      nextTick(() => {
        renderBarChart();
      });
    } else if (chartType === 'line') {
      nextTick(() => {
        renderLineChart();
      });
    } else if (chartType === 'pie') {
      nextTick(() => {
        renderPieChart();
      });
    }
  }
};

// 导出为 Excel
const exportToExcel = () => {
  const tableData = getTableData();
  const wb = XLSX.utils.book_new();
  const ws = XLSX.utils.json_to_sheet(tableData);
  XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
  XLSX.writeFile(wb, 'table_data.xlsx');
  console.log('导出为 Excel');
};

// 确保 getTableData 函数是通过 ref 或者直接暴露的
const getTableData = () => {
  return wordCloudData.value.map(item => ({
    技术名词: item.text,
    热度: item.size
  }));
};

const exportToPDF = () => {
  let exportContent;

  // 根据选中的图表类型获取对应的图表元素
  if (showChart.value === 'bar') {
    exportContent = document.getElementById('barChartCanvas');
  } else if (showChart.value === 'line') {
    exportContent = document.getElementById('lineChartCanvas');
  } else if (showChart.value === 'pie') {
    exportContent = document.getElementById('pieChartCanvas');
  } else {
    // 如果没有选择任何图表，则提示用户
    alert("请先选择一个图表类型！");
    return;
  }

  // 使用 html2canvas 将图表渲染为图片
  if (!exportContent) {
    console.error('未找到需要导出的内容');
    return;
  }

  html2canvas(exportContent).then(canvas => {
    const imgData = canvas.toDataURL('image/png');
    const pdf = new jsPDF();

    // 获取图片的属性（如宽度和高度）
    const imgProps = pdf.getImageProperties(imgData);
    const pdfWidth = pdf.internal.pageSize.getWidth();
    const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;

    // 根据PDF尺寸调整图片大小和位置，确保图片居中显示且保持比例
    const margin = 10; // 设置边距为10mm
    const scaledWidth = pdfWidth - 2 * margin; // 计算缩放后的图片宽度
    const scaledHeight = (imgProps.height * scaledWidth) / imgProps.width; // 保持图片比例计算高度
    const xPosition = margin; // 图片在PDF中的x坐标
    const yPosition = (pdf.internal.pageSize.getHeight() - scaledHeight) / 2; // 图片在PDF中居中显示的y坐标

    // 将图片添加到PDF中，并保持比例
    pdf.addImage(imgData, 'PNG', xPosition, yPosition, scaledWidth, scaledHeight);
    pdf.save('chart.pdf');
    console.log('成功导出为 PDF');
  }).catch(error => {
    console.error('导出PDF时发生错误:', error);
  });
};


</script>

<template>
  <el-container>
    <el-header style="margin-bottom: 20px;">
      <h1>热点领域分析</h1>
      <p>以下是技术领域中的一些关键技术与概念的词云，词语的大小代表其相关性或热度。</p>
    </el-header>
    <el-container>
      <el-aside>
        <Aside />
      </el-aside>
      <el-main>
        <!-- 图表展示按钮 -->
        <div class="chart-buttons">
          <el-button @click="toggleChart('wordcloud')">词云</el-button>
          <el-button @click="toggleChart('bar')">柱状图</el-button>
          <el-button @click="toggleChart('line')">折线图</el-button>
          <el-button @click="toggleChart('pie')">饼状图</el-button>
          <el-button @click="exportToExcel()">导出为 Excel</el-button>
          <el-button @click="exportToPDF()">导出为 PDF</el-button>
        </div>

        <!-- 图表展示区域 -->
        <div v-if="showChart === 'wordcloud'" ref="wordCloudRef"></div>
        <div v-if="showChart === 'bar'" class="chart-container">
          <canvas id="barChartCanvas"></canvas>
        </div>
        <div v-if="showChart === 'line'" class="chart-container">
          <canvas id="lineChartCanvas"></canvas>
        </div>
        <div v-if="showChart === 'pie'" class="chart-container">
          <canvas id="pieChartCanvas"></canvas>
        </div>
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

.chart-container {
  margin: 20px;
  width: 80%;
  max-width: 900px;
  height: 450px;
}

.chart-buttons {
  margin-bottom: 20px;
}
</style>
