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
    tooltip: {},
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
    item.itemStyle = {
      color: '#1F578F'
    }
  })
  setTimeout(()=>{
    chart.setOption(option);
  },100);
})
</script>