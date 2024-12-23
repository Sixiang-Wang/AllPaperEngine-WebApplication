<template>
  <el-container>
    <el-header style="margin-bottom: 20px;">
      <h1>热点领域分析</h1>
      <p>以下是技术领域中的一些关键技术与概念的词云，词语的大小代表其相关性或热度。</p>
    </el-header>
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px" style="margin-left: 50px; margin-top: 20px;">
        <el-collapse v-model="activeFilter" accordion>
          <el-collapse-item title="Domain" name="1">
            <el-select v-model="selectedDomain" placeholder="请选择领域" @change="onDomainChange">
              <el-option v-for="domain in domainOptions" :key="domain" :label="domain" :value="domain" />
            </el-select>
          </el-collapse-item>
          <el-collapse-item title="Field" name="2" v-if="selectedDomain">
            <el-select v-model="selectedField" placeholder="请选择领域下的学科" @change="onFieldChange">
              <el-option v-for="field in fieldOptions" :key="field" :label="field" :value="field" />
            </el-select>
          </el-collapse-item>
          <el-collapse-item title="Subfield" name="3" v-if="selectedField">
            <el-select v-model="selectedSubfield" placeholder="请选择子领域">
              <el-option v-for="subfield in subfieldOptions" :key="subfield" :label="subfield" :value="subfield" />
            </el-select>
          </el-collapse-item>
        </el-collapse>
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

<script setup>
import { ref, onMounted, nextTick } from "vue";
import * as d3 from 'd3';
import cloud from 'd3-cloud';
import Aside from "@/components/HotspotAside.vue";
import { Chart, registerables } from 'chart.js';
import * as XLSX from 'xlsx';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import axios from "axios";

Chart.register(...registerables);

const apiUrl = "http://116.204.112.5:1145/hotspot/getTopicsWorksCount";

const fetchedWordCloudData = ref([]);

const fetchWordCloudData = async () => {
  try {
    const response = await httpUtil.get('/hotspot/getTopicsWorksCount', {
      params: {
        domain: selectedDomain.value,
        field: selectedField.value,
        subfield: selectedSubfield.value
      }
    });

    fetchedWordCloudData.value = response.data.map(item => ({
      text: item.topicName,
      size: item.worksCount
    }));

    wordCloudData.value = fetchedWordCloudData.value;

    nextTick(() => {
      renderWordCloud();
    });
  } catch (error) {
    console.error("获取词云数据失败:", error);
  }
};

onMounted(() => {
  fetchWordCloudData();
});

// 示例词云数据
const wordCloudData = ref([
  { text: "AI", size: 2},
  { text: "Machine Learning", size: 3},
  { text: "Economics", size: 5},
  { text: "Psychology", size: 4},
  { text: "Biology", size: 6},
  { text: "Medicine", size: 7},
  { text: "Vue", size: 10},
]);

const domainOptions = ref(['Life Sciences', 'Physical Sciences', 'Social Sciences', 'Health Sciences']);
const fieldOptionsMap = ref({
  'Life Sciences': ['Neuroscience', 'Immunology and Microbiology', 'Agricultural and Biological Sciences', 'Biochemistry, Genetics and Molecular Biology', 'Pharmacology, Toxicology and Pharmaceutics'],
  'Physical Sciences': ['Engineering', 'Materials Science', 'Chemistry', 'Energy', 'Physics and Astronomy', 'Earth and Planetary Sciences', 'Computer Science', 'Mathematics', 'Environmental Science', 'Chemical Engineering'],
  'Social Sciences': ['Social Sciences', 'Decision Sciences', 'Psychology', 'Economics, Econometrics and Finance', 'Business, Management and Accounting', 'Arts and Humanities'],
  'Health Sciences': ['Medicine', 'Nursing', 'Health Professions', 'Dentistry', 'Veterinary', ],
});

const subfieldOptionsMap = ref({
  Neuroscience: ['Neurology', 'Behavioral Neuroscience', 'Sensory Systems', 'Cognitive Neuroscience', 'Biological Psychiatry', 'Endocrine and Autonomic Systems', 'Cellular and Molecular Neuroscience', 'Developmental Neuroscience'],
  'Immunology and Microbiology': ['Applied Microbiology and Biotechnology', 'Parasitology', 'Immunology', 'Microbiology', 'Virology'],
  'Agricultural and Biological Sciences': ['Aquatic Science', 'Insect Science', 'Plant Science', 'Soil Science', 'Food Science', 'Horticulture', 'Ecology, Evolution', 'Animal Science and Zoology', 'General Agricultural and Biological Sciences', 'Forestry', 'Agronomy and Crop Science'],
  'Biochemistry, Genetics and Molecular Biology': ['Biotechnology', 'Molecular Biology', 'Structural Biology', 'Clinical Biochemistry', 'Biochemistry', 'Biophysics', 'Cell Biology', 'Aging', 'Cancer Research', 'Genetics', 'Physiology', 'Molecular Medicine', 'Developmental Biology', 'Endocrinology', ],
  'Pharmacology, Toxicology and Pharmaceutics': ['Toxicology', 'Pharmaceutical Science'],
});

// 用于绑定到 DOM 元素的 ref
const wordCloudRef = ref(null);
const showChart = ref(''); // 默认不显示任何图表

const filteredWordCloudData = ref(wordCloudData.value); // 默认显示全部数据

// 选择状态
const selectedDomain = ref('');
const selectedField = ref('');
const selectedSubfield = ref('');

// 动态更新字段
const fieldOptions = ref([]);
const subfieldOptions = ref([]);

// 当选择了一个领域（Domain）时，更新字段（Field）
const onDomainChange = (domain) => {
  selectedField.value = '';
  selectedSubfield.value = '';
  fieldOptions.value = fieldOptionsMap.value[domain] || [];
  subfieldOptions.value = [];
};

// 当选择了一个学科（Field）时，更新子领域（Subfield）
const onFieldChange = (field) => {
  selectedSubfield.value = '';
  subfieldOptions.value = subfieldOptionsMap.value[field] || [];
};

// 图表数据
const barChartData = ref({
  labels: filteredWordCloudData.value.map(item => item.text),
  datasets: [{
    label: '技术热度',
    data: filteredWordCloudData.value.map(item => item.size),
    backgroundColor: 'rgba(54, 162, 235, 0.2)',
    borderColor: 'rgba(54, 162, 235, 1)',
    borderWidth: 1
  }]
});


const lineChartData = ref({
  labels: filteredWordCloudData.value.map(item => item.text),
  datasets: [{
    label: '技术热度趋势',
    data: filteredWordCloudData.value.map(item => item.size),
    fill: false,
    borderColor: 'rgba(75, 192, 192, 1)',
    tension: 0.1
  }]
});

const pieChartData = ref({
  labels: filteredWordCloudData.value.map(item => item.text),
  datasets: [{
    data: filteredWordCloudData.value.map(item => item.size),
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

const updateSelectedFilter = (category) => {
  // 筛选出符合当前选择的学科的数据
  if (category === "All") {
    filteredWordCloudData.value = wordCloudData.value;
  }
  else {
    filteredWordCloudData.value = wordCloudData.value.filter(
      (item) => item.label === category
    );
  }

  updateChartData();

  // 重新渲染词云
  nextTick(() => { 
    renderWordCloud();
  });
};

const updateChartData = () => {
  barChartData.value.labels = filteredWordCloudData.value.map(item => item.text);
  barChartData.value.datasets[0].data = filteredWordCloudData.value.map(item => item.size);

  lineChartData.value.labels = filteredWordCloudData.value.map(item => item.text);
  lineChartData.value.datasets[0].data = filteredWordCloudData.value.map(item => item.size);

  pieChartData.value.labels = filteredWordCloudData.value.map(item => item.text);
  pieChartData.value.datasets[0].data = filteredWordCloudData.value.map(item => item.size);
};

// 渲染词云
const renderWordCloud = () => {
  const layout = cloud()
    .size([window.innerWidth * 0.6, window.innerHeight * 0.6])
    .words(filteredWordCloudData.value.map(word => ({ text: word.text, size: word.size })))
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
  fetchWordCloudData();
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
  return filteredWordCloudData.value.map(item => ({
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
