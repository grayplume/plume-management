<script>
import * as echarts from 'echarts';

export default {
  name: "Home",
  data() {
    return {}
  },
  mounted() {  // 页面加载完成后触发
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option;

    option = {
      title: {
        text: "各季度会员数量",
        subtext: "趋势图",
        left: "center"
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'line'
        },
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'bar'
        }
      ]
    };
    // 饼图
    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);
    var pieOption = {
      title: {
        text: '各季度会员数量',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          data: [
            // {value: 1048, name: 'Search Engine'},
            // {value: 735, name: 'Direct'},
            // {value: 580, name: 'Email'},
            // {value: 484, name: 'Union Ads'},
            // {value: 300, name: 'Video Ads'}
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    this.request.get("/echarts/members").then(res => {
      if (res.code === "200") {
        option.series[0].data = res.data
        option.series[1].data = res.data

        pieOption.series[0].data = [
          {name: "第一季度", value: res.data[0]},
          {name: "第二季度", value: res.data[1]},
          {name: "第三季度", value: res.data[2]},
          {name: "第四季度", value: res.data[3]},
        ]
      } else {
        this.$message.error("获取数据失败")
      }
      option && myChart.setOption(option);
      pieOption && pieChart.setOption(pieOption);
    })

  }
}
</script>

<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px">
      <el-col span="6">
        <el-card style="color: #0679ff">
          <div><i class="el-icon-user-solid"></i>用户总数</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col span="6">
        <el-card style="color: #56b294">
          <div><i class="el-icon-money"></i>销售总量</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">
            $ 100000
          </div>
        </el-card>
      </el-col>
      <el-col span="6">
        <el-card style="color: #16ce00">
          <div><i class="el-icon-coin"></i>收益总额</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">
            $ 100000
          </div>
        </el-card>
      </el-col>
      <el-col span="6">
        <el-card style="color: #ff7101">
          <div><i class="el-icon-s-shop"></i>门店总数</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px;height: 400px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width: 500px;height: 400px"></div>
      </el-col>
    </el-row>
  </div>

</template>

<style scoped>

</style>