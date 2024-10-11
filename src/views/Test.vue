<template>
  <div>
    <div id="maychar"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  mounted() {
    this.getCharts();
  },
  methods: {
    // 使用柱形图，关于其他配置可以去官网查看
    getCharts() {
      const chartBox = this.$echarts.init(document.getElementById("maychar"));
      const option = {
        xAxis: {
          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        },
        yAxis: {},
        series: [
          {
            type: "bar",
            data: [23, 24, 18, 25, 27, 28, 25],
          },
        ],
      };
      chartBox.setOption(option);
      // 根据页面大小自动响应图表大小
      window.addEventListener("resize", function () {
        chartBox.resize();
      });
    },
  },
};
</script>