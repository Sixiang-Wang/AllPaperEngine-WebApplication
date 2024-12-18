<template>
  <div id="graph" style="width: 100%; height: 600px;"></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  mounted() {
    const chart = echarts.init(document.getElementById('graph'));
    console.log(localStorage.getItem("ifAuthentication"));
    const option = {
      tooltip: {},
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

    chart.setOption(option);
  },
};
</script>
