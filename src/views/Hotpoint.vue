<template>
  <el-container>
    <el-header style="margin-bottom: 20px;">
      <h1>热点领域分析</h1>
      <p>选择下面的领域、学科、子领域，获取热点分析统计图表。</p>
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
        <div class="chart-buttons" style="margin-top: 20px;">
          <el-button @click="analyzeHotspot()" type="primary" style="width: 100%;">分析该领域热点</el-button>
          <el-button @click="exportToPDF" style="width: 100%; margin-top: 10px; margin-left: 0;" type="primary">导出PDF图表</el-button>
          <el-button @click="exportToExcel" style="width: 100%; margin-top: 10px; margin-left: 0;" type="primary">导出表格</el-button>
        </div>
      </el-aside>

      <el-main>

        <div v-if="showChart.pie" class="table-container" style="margin-bottom: 60px">
          <el-table :data="sortedWordCloudData" style="width: 100%" border>
            <el-table-column label="学科领域" prop="text"></el-table-column>
            <el-table-column label="论文数量" prop="size"></el-table-column>
          </el-table>
        </div>
        <div v-else>
          <el-empty :image=robotImage
                    description="请先选择对应领域"/>
        </div>
        <div class="charts-container">
          <div v-if="showChart.bar" class="chart-item" style="margin-bottom: 60px">
            <canvas id="barChartCanvas"></canvas>
          </div>
          <div v-if="showChart.line" class="chart-item" style="margin-bottom: 60px">
            <canvas id="lineChartCanvas"></canvas>
          </div>
          <div v-if="showChart.pie" class="chart-item-pie" style="margin-bottom: 60px">
            <canvas id="pieChartCanvas"></canvas>
          </div>          
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from "vue";
import * as d3 from 'd3';
import cloud from 'd3-cloud';
import Aside from "@/components/HotspotAside.vue";
import { Chart, registerables } from 'chart.js';
import * as XLSX from 'xlsx';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import httpUtil from "@/api/http.js";
import robotImage from "@/assets/image/robot.png";

Chart.register(...registerables);

const apiUrl = "http://116.204.112.5:1145/hotspot/getTopicsWorksCount";

const fetchedWordCloudData = ref([]);

const fetchWordCloudData = async () => {

  try {
    const response = await httpUtil.get(`/hotspot/getTopicsWorksCount?domainDisplayName=${selectedDomain.value}&fieldDisplayName=${selectedField.value}&subfieldDisplayName=${selectedSubfield.value}`);

    fetchedWordCloudData.value = response.data.topics.map(item => ({
      text: item.topicName,
      size: item.worksCount
    }));

    wordCloudData.value = fetchedWordCloudData.value;

    updateChartData();

    console.log(fetchedWordCloudData.value);
  } catch (error) {
    console.error("获取词云数据失败:", error);
  }
};

// 示例词云数据
const wordCloudData = ref([]);

const domainOptions = ref(['Life Sciences', 'Physical Sciences', 'Social Sciences', 'Health Sciences']);
const fieldOptionsMap = ref({
  'Life Sciences': ['Neuroscience', 'Immunology and Microbiology', 'Agricultural and Biological Sciences', 'Biochemistry, Genetics and Molecular Biology', 'Pharmacology, Toxicology and Pharmaceutics'],
  'Physical Sciences': ['Engineering', 'Materials Science', 'Chemistry', 'Energy', 'Physics and Astronomy', 'Earth and Planetary Sciences', 'Computer Science', 'Mathematics', 'Environmental Science', 'Chemical Engineering'],
  'Social Sciences': ['Social Sciences', 'Decision Sciences', 'Psychology', 'Economics, Econometrics and Finance', 'Business, Management and Accounting', 'Arts and Humanities'],
  'Health Sciences': ['Medicine', 'Nursing', 'Health Professions', 'Dentistry', 'Veterinary', ],
});

const subfieldOptionsMap = ref({
  // Life Sciences
  Neuroscience: ['Neurology', 'Behavioral Neuroscience', 'Sensory Systems', 'Cognitive Neuroscience', 'Biological Psychiatry', 'Endocrine and Autonomic Systems', 'Cellular and Molecular Neuroscience', 'Developmental Neuroscience'],
  'Immunology and Microbiology': ['Applied Microbiology and Biotechnology', 'Parasitology', 'Immunology', 'Microbiology', 'Virology'],
  'Agricultural and Biological Sciences': ['Aquatic Science', 'Insect Science', 'Plant Science', 'Soil Science', 'Food Science', 'Horticulture', 'Ecology, Evolution', 'Animal Science and Zoology', 'General Agricultural and Biological Sciences', 'Forestry', 'Agronomy and Crop Science'],
  'Biochemistry, Genetics and Molecular Biology': ['Biotechnology', 'Molecular Biology', 'Structural Biology', 'Clinical Biochemistry', 'Biochemistry', 'Biophysics', 'Cell Biology', 'Aging', 'Cancer Research', 'Genetics', 'Physiology', 'Molecular Medicine', 'Developmental Biology', 'Endocrinology', ],
  'Pharmacology, Toxicology and Pharmaceutics': ['Toxicology', 'Pharmaceutical Science', 'Pharmacology', 'Drug Discovery'],
  // Physical Sciences
  Engineering: ['Ocean Engineering', 'Architecture', 'Mechanics of Materials', 'Industrial and Manufacturing Engineering', 'General Engineering', 'Safety, Risk, Reliability and Quality', 'Control and Systems Engineering', 'Biomedical Engineering', 'Building and Construction', 'Electrical and Electronic Engineering', 'Civil and Structural Engineering','Media Technology', 'Automotive Engineering', 'Aerospace Engineering', 'Mechanical Engineering', 'Computational Mechanics'],
  'Materials Science': ['Materials Chemistry', 'Electronic, Optical and Magnetic Materials', 'Surfaces, Coatings and Films', 'Metals and Alloys', 'Ceramics and Composites', 'General Materials Science', 'Polymers and Plastics', 'Biomaterials'],
  Chemistry: ['Spectroscopy', 'Analytical Chemistry', 'Electrochemistry', 'Inorganic Chemistry', 'Physical and Theoretical Chemistry', 'Organic Chemistry'],
  Energy: ['Energy Engineering and Power Technology', 'General Energy', 'Fuel Technology', 'Nuclear Energy and Engineering', 'Renewable Energy, Sustainability and the Environment'],
  'Physics and Astronomy': ['Condensed Matter Physics', 'Atomic and Molecular Physics, and Optics', 'Statistical and Nonlinear Physics', 'Acoustics and Ultrasonics', 'Nuclear and High Energy Physics', 'Astronomy and Astrophysics', 'Radiation', 'Instrumentation'],
  'Earth and Planetary Sciences': ['Geology', 'Geochemistry and Petrology', 'Geophysics', 'Atmospheric Science', 'Earth-Surface Processes', 'Oceanography', 'Paleontology'],
  'Computer Science': ['Computational Theory and Mathematics', 'Computer Science Applications', 'Artificial Intelligence', 'Information Systems', 'Computer Vision and Pattern Recognition', 'Hardware and Architecture', 'Software', 'Computer Graphics and Computer-Aided Design', 'Computer Networks and Communications', 'Human-Computer Interaction', 'Signal Processing'],
  Mathematics: ['Modeling and Simulation', 'Computational Mathematics', 'Algebra and Number Theory', 'Mathematical Physics', 'Theoretical Computer Science', 'Numerical Analysis', 'Geometry and Topology', 'Statistics and Probability', 'Applied Mathematics', 'Discrete Mathematics and Combinatorics'],
  'Environmental Science': ['Pollution', 'Health', 'Water Science and Technology', 'Industrial and Manufacturing Engineering', 'Environmental Engineering', 'Management, Monitoring, Policy and Law', 'Ecology', 'Global and Planetary Change', 'Ecological Modeling', 'Nature and Landscape Conservation', 'Environmental Chemistry'],
  'Chemical Engineering': ['Process Chemistry and Technology', 'Bioengineering', 'Chemical Health and Safety', 'Fluid Flow and Transfer Processes', 'Catalysis', 'Filtration and Separation'],
  // Social Sciences
  'Social Sciences': ['Political Science and International Relations', 'Law', 'Demography', 'Safety Research', 'Health', 'Human Factors and Ergonomics', 'Transportation', 'Gender Studies', 'Geography, Planning and Development', 'Library and Information Sciences', 'Anthropology', 'Linguistics and Language', 'General Social Sciences', 'Life-span and Life-course Studies', 'Archeology', 'Cultural Studies', 'Education', 'Development', 'Urban Studies', 'Sociology and Political Science', 'Public Administration', 'Communication'],
  'Decision Sciences': ['General Decision Sciences', 'Management Science and Operations Research', 'Statistics, Probability and Uncertainty', 'Information Systems and Management'],
  Psychology: ['Developmental and Educational Psychology', 'Experimental and Cognitive Psychology', 'General Psychology', 'Clinical Psychology', 'Neuropsychology and Physiological Psychology', 'Applied Psychology', 'Social Psychology'],
  'Economics, Econometrics and Finance': ['General Economics, Econometrics and Finance', 'Finance', 'Economics and Econometrics'],
  'Business, Management and Accounting': ['Organizational Behavior and Human Resource Management', 'Management of Technology and Innovation', 'Industrial relations', 'Management Information Systems', 'Accounting', 'Tourism, Leisure and Hospitality Management', 'Strategy and Management', 'Business and International Management', 'Marketing'],
  'Arts and Humanities': ['Classics', 'History and Philosophy of Science', 'Literature and Literary Theory', 'Conservation', 'Visual Arts and Performing Arts', 'History', 'Religious studies', 'General Arts and Humanities', 'Archeology', 'Music', 'Museology', 'Philosophy', 'Language and Linguistics'],
  // Health Sciences
  Medicine: ['Nephrology', 'Microbiology', 'Oncology', 'Transplantation', 'Otorhinolaryngology', 'Biochemistry', 'Public Health', 'Radiology', 'Epidemiology', 'Urology', 'Obstetrics and Gynecology', 'Orthopedics and Sports Medicine', 'Surgery', 'Hepatology', 'Gastroenterology', 'Genetics', 'Physiology', 'Ophthalmology', 'Pulmonary and Respiratory Medicine', 'Anesthesiology and Pain Medicine', 'Pediatrics, Perinatology and Child Health', 'Complementary and alternative medicine', 'Emergency Medicine', 'Hematology', 'Dermatology', 'Pathology and Forensic Medicine', 'Rheumatology', 'Health Informatics', 'Internal Medicine', 'Psychiatry and Mental health', 'Pharmacology', 'Endocrinology, Diabetes and Metabolism', 'Anatomy', 'Geriatrics and Gerontology', 'Neurology', 'Critical Care and Intensive Care Medicine', 'Family Practice', 'Infectious Diseases', 'Rehabilitation', 'Cardiology and Cardiovascular Medicine', 'Immunology and Allergy', 'Reproductive Medicine'],
  Nursing: ['Issues, ethics and legal aspects', 'Research and Theory', 'Nutrition and Dietetics', 'Leadership and Management'],
  'Health Professions': ['Occupational Therapy', 'Speech and Hearing', 'Complementary and Manual Therapy', 'Radiological and Ultrasound Technology', 'General Health Professions', 'Medical Laboratory Technology', 'Pharmacy', 'Health Information Management', 'Emergency Medical Services', 'Physical Therapy, Sports Therapy and Rehabilitation', 'Medical Terminology'],
  Dentistry: ['Periodontics', 'General Dentistry', 'Oral Surgery', 'Orthodontics'],
  Veterinary: ['Small Animals', 'Equine'],
});

// 用于绑定到 DOM 元素的 ref
const wordCloudRef = ref(null);
const showChart = ref({
  bar: false,
  line: false,
  pie: false,
});

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

const analyzeHotspot = async () => {

  const charts = ['bar', 'line', 'pie'];

  // 先获取数据
  await fetchWordCloudData();  // 等待数据加载完成

  charts.forEach(chart => {
    showChart.value[chart] = !showChart.value[chart];  // 切换图表的显示状态
  });

  nextTick(() => {
    // 如果图表被显示，就渲染它们
    if (showChart.value.bar) renderBarChart();
    if (showChart.value.line) renderLineChart();
    if (showChart.value.pie) renderPieChart();
  });
};

const sortedWordCloudData = computed(() => {
  return [...fetchedWordCloudData.value].sort((a, b) => b.size - a.size);
});

// 图表数据
const barChartData = ref({
  labels: fetchedWordCloudData.value.map(item => item.text),
  datasets: [{
    label: '论文数量',
    data: fetchedWordCloudData.value.map(item => item.size),
    backgroundColor: 'rgba(54, 162, 235, 0.2)',
    borderColor: 'rgba(54, 162, 235, 1)',
    borderWidth: 1
  }]
});


const lineChartData = ref({
  labels: fetchedWordCloudData.value.map(item => item.text),
  datasets: [{
    label: '论文数量',
    data: fetchedWordCloudData.value.map(item => item.size),
    fill: false,
    borderColor: 'rgba(75, 192, 192, 1)',
    tension: 0.1
  }]
});

const pieChartData = ref({
  labels: fetchedWordCloudData.value.map(item => item.text),
  datasets: [{
    data: fetchedWordCloudData.value.map(item => item.size),
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

const updateChartData = () => {
  barChartData.value.labels = fetchedWordCloudData.value.map(item => item.text);
  barChartData.value.datasets[0].data = fetchedWordCloudData.value.map(item => item.size);

  lineChartData.value.labels = fetchedWordCloudData.value.map(item => item.text);
  lineChartData.value.datasets[0].data = fetchedWordCloudData.value.map(item => item.size);

  pieChartData.value.labels = fetchedWordCloudData.value.map(item => item.text);
  pieChartData.value.datasets[0].data = fetchedWordCloudData.value.map(item => item.size);
};

// // 渲染词云
// const renderWordCloud = () => {
//   const layout = cloud()
//     .size([window.innerWidth * 0.6, window.innerHeight * 0.6])
//     .words(wordCloudData.value.map(word => ({ text: word.text, size: word.size })))
//     .font("Impact")
//     .fontSize(d => Math.pow(d.size, 1.2))
//     .rotate(() => Math.floor(Math.random() * 181) - 90)
//     .on("end", drawCloud);

//   layout.start();

//   function drawCloud(words) {
//     d3.select(wordCloudRef.value)
//       .append("svg")
//       .attr("width", layout.size()[0])
//       .attr("height", layout.size()[1])
//       .append("g")
//       .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")")
//       .selectAll("text")
//       .data(words)
//       .enter()
//       .append("text")
//       .style("font-size", d => d.size + "px")
//       .style("font-family", "Impact")
//       .style("fill", "steelblue")
//       .attr("text-anchor", "middle")
//       .attr("transform", d => "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")")
//       .text(d => d.text)
//       .on("mouseover", function () {
//         d3.select(this).style("fill", "orange");
//       })
//       .on("mouseout", function () {
//         d3.select(this).style("fill", "steelblue");
//       })
//       .on("click", function (event, d) {
//         alert("你点击了: " + d.text);
//       });
//   }
// };

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
// const toggleChart = (chartType) => {
//   fetchWordCloudData();
//   if (showChart.value === chartType) {
//     showChart.value = '';
//   } else {
//     showChart.value = chartType;
//       // if (chartType === 'wordcloud') {
//       // nextTick(() => {
//       //   renderWordCloud();
//       // });
//       // } else 
//     if (chartType === 'bar') {
//       nextTick(() => {
//         renderBarChart();
//       });
//     } else if (chartType === 'line') {
//       nextTick(() => {
//         renderLineChart();
//       });
//     } else if (chartType === 'pie') {
//       nextTick(() => {
//         renderPieChart();
//       });
//     }
//   }
// };

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
  return fetchedWordCloudData.value.map(item => ({
    技术名词: item.text,
    热度: item.size
  }));
};

const exportToPDF = () => {
  // 获取所有图表元素
  const chartElements = [
    document.getElementById('barChartCanvas'),  // 条形图
    document.getElementById('lineChartCanvas'), // 折线图
    document.getElementById('pieChartCanvas')   // 饼图
  ];

  // 创建一个新的 jsPDF 实例
  const pdf = new jsPDF();

  // 设置边距
  const margin = 10;
  const pdfWidth = pdf.internal.pageSize.getWidth();
  
  // 遍历所有图表，依次导出每个图表
  chartElements.forEach((chartElement, index) => {
    if (chartElement) {
      html2canvas(chartElement).then(canvas => {
        const imgData = canvas.toDataURL('image/png');
        const imgProps = pdf.getImageProperties(imgData);
        
        // 计算每个图表的高度，保持比例
        const scaledWidth = pdfWidth - 2 * margin; // 缩放后的图片宽度
        const scaledHeight = (imgProps.height * scaledWidth) / imgProps.width; // 保持比例计算高度
        
        // 根据当前页，计算图片的y坐标
        const yPosition = margin + (index === 0 ? 0 : 10); // 给每个图表之间留一些间距

        // 如果不是第一页，添加新的页面
        if (index > 0) {
          pdf.addPage();
        }

        // 将图片添加到 PDF 中
        pdf.addImage(imgData, 'PNG', margin, yPosition, scaledWidth, scaledHeight);

        // 最后保存 PDF
        if (index === chartElements.length - 1) {
          pdf.save('charts.pdf');
          console.log('成功导出为 PDF');
        }
      }).catch(error => {
        console.error('导出 PDF 时发生错误:', error);
      });
    } else {
      console.error(`图表元素 #${index + 1} 未找到`);
    }
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

.chart-item {
  margin: 20px;
  width: 100%;
  max-width: 900px;
  height: 450px;
  margin-left: 10%;
}

.chart-item-pie {
  margin: 20px;
  width: 80%;
  max-width: 900px;
  height: 450px;
  margin-left: 30%;
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

.table-container {
  margin-top: 20px;
  margin-left: 10%;
  max-width: 80%;
}
</style>
