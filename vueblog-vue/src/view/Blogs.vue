<template>
  <div>
    <!-- 头部 -->
    <Header></Header> 
    <!-- 列表 -->
    <div class="block">
      <el-timeline>
        <el-timeline-item :timestamp="blog.created" placement="top" v-for="(blog,key) in blogs" :key="key">
          <el-card>
            <h4>
              <router-link :to="{name:'BlogDetail',params:{blogid:blog.id}}">{{blog.title}}</router-link>
            </h4>
            <p>{{blog.description}}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <!-- 分页 -->
      <el-pagination class="mpage"
        background
        layout="prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :page-count="pages"
        @current-change="page"
        >
      </el-pagination>
    </div>
  </div>
</template>

<script>
  //导入公共部分Header
  import Header from "../components/Header"
  export default {
    //声明导入的组件
    components:{Header},
    data(){
      return{
        blogs:{},
        currentPage:1, //当前页，默认是第一页
        total:0, //总记录数
        pageSize:5, //默认一页显示5条
        pages:0 //总页数
      }
    },methods:{
      //分页显示
      page(currentPage){  //currentPage是当前页
        const _this = this;
        _this.$axios.get("/blogs?currentPage=" + currentPage) //传入当前页去发送请求到后台查询
        .then(response =>{
          console.log(response);
          //将响应数据中的博客记录、当前页、总记录数、每页显示记录数和总页数，设置给data中的数据，更新组件
          _this.blogs = response.data.data.records;
          _this.currentPage = response.data.data.currentPage;
          _this.total = response.data.data.total;
          _this.pageSize = response.data.data.size;
          _this.pages = response.data.data.pages;
        })
      }
    },
    created(){
      //路由到Blogs组件时，就调用page()分页展示
      this.page(1);
    }
  }
</script>

<style scoped>
  .mpage{
    margin:0 auto;
    text-align: center;
  }
</style>