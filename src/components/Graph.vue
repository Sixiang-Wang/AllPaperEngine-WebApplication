<template>
  <div id="graph" style="width: 100%; height: 250px"/>
</template>

<script setup>
import * as echarts from 'echarts';
import {onMounted} from "vue";
import * as httpUtil from "@/api/http.js";
import * as cookieUtil from "@/utils/cookie.js";



onMounted(async()=>{
  const chart = echarts.init(document.getElementById('graph'));
  console.log(localStorage.getItem("ifAuthentication"));
  const option = {
    tooltip: {
      trigger: 'item', // 触发方式，设置为 'item' 以响应节点悬浮
      formatter: function (params) {
        // 判断是否为节点
        if (params.dataType === 'node') {
          return `名称: ${params.data.name}<br>类型: ${params.data.type}`;
        }
        // 判断是否为边
        if (params.dataType === 'edge') {
          return `Source: ${params.data.source}<br>Target: ${params.data.target}`;
        }
        return '';
      },
    },    layout: 'force',
    grid:{ // 让图表占满容器
      top:"0px",
      left:"0px",
      right:"0px",
      bottom:"0px"
    },
    series: [
      {
        type: 'graph',
        layout: 'force',
        force: {
          repulsion: 200, // 调整节点间距
          edgeLength: [50, 100],
        },
        data: [
          { name: 'Granger, 1969', symbolSize: 30 },
          { name: 'Sims, 1972', symbolSize: 20 },
        ],
        links: [
          { source: 'Granger, 1969', target: 'Sims, 1972' },
        ],
        roam: true,
        label: {
          show: true,
          position: 'inside',
        },
        lineStyle: {
          color: 'source',
        },
      },
    ],
  };
  const res = await httpUtil.get('/net/get',{},{
    Authorization: cookieUtil.getCookie("token"),
  })
  console.log(res.data);
  option.series[0].data = res.data.data;
  option.series[0].links = res.data.links;
  option.series[0].data.forEach(item=>{
    switch (item.type){
      case 'SELF':
        item.itemStyle = {
          color: '#1F578F'
        }
        break;
      case 'INSTITUTION_RELATED':
        item.itemStyle = {
          color: '#1F578F'
        }
        break;
      case 'WORK_RELATED':
        item.itemStyle = {
          color: '#3299b1'
        }
        break;
    }

  })
  option.series[0].links.forEach(item=>{
    item.lineStyle = {
      normal: {
        curveness: 0.05
      }
    };
    // item.force =
  })
  console.log(option.series[0]);
  setTimeout(()=>{
    chart.setOption(option);
  },100);
})
</script>