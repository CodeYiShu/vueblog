<template>
  <div>
    <Header></Header>
    <div class="mblog">
      <!-- 显示标题 -->
      <h2>{{blog.title}}</h2>
      <!-- 编辑按钮，到BlogEdit组件中编辑，携带当前博客的id -->
      <el-link icon="el-icon-edit" v-if="ownBlog">
        <router-link :to="{name:'BlogEdit',params:{blogid:blog.id}}">编辑</router-link>
      </el-link>
      <!-- 删除按钮，只有博客是当前用户创建时显示，绑定单击事件，触发delbog() -->
      <el-link icon="el-icon-delete" v-if="ownBlog" class="linklist">
        <el-button type="danger" round @click="delblog">删除</el-button>
      </el-link> 
      <el-divider></el-divider>
      <!-- 显示内容 -->
      <div v-html="blog.content" class="markdown-body"></div>
    </div>
  </div>
</template>

<script>
    import Header from "../components/Header"
    //导入markdown-it组件
    import MarkdownIt from "markdown-it"
    //导入github-markdown-css
    import "github-markdown-css"

    export default {
      components:{Header},
      data(){
        return {
          blog:{
            id:"1",
            title:"默认",
            content:"内容"
          },
          ownBlog :false
        }
      },
      methods:{
        delblog(){
          //获取路由的参数blogid，由Blogs组件中点击任意一个博客的标题得到
          const blogid = this.$route.params.blogid;
          const _this = this;
          if(blogid){
              this.$confirm('此操作将永久删除该博客, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                _this.$axios.post("/blogdel/"+blogid,null,{headers: {"Authorization": localStorage.getItem("jwt")}})
                .then(response =>{
                  this.$message({
                    type: 'success',
                    message: response.data.data
                  });
                  _this.$router.push("/blogs")
                })

              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消删除'
                });          
              });
          }
        }
      },
      created(){
        //获取路由的参数blogid，由Blogs组件中点击任意一个博客的标题得到
        const blogid = this.$route.params.blogid;
        const _this = this;
        //发送/blog/{id}到后台，获取此id的博客记录
        this.$axios.get("/blog/" + blogid)
        .then(response => {
          //获取响应数据中的博客记录
          const blog = response.data.data;
          //设置给data中的数据，更新组件
          _this.blog.id = blog.id
          _this.blog.title = blog.title;
          //创建MarkdownIt组件对象
          var md = new MarkdownIt();
          //使用MarkdownIt组件对象去渲染解析blog的内容
          var result = md.render(blog.content);
          //设置给data中的数据，更新组件
          _this.blog.content = result;
          if(blog.userId === JSON.parse(sessionStorage.getItem("userInfo")).id){
            _this.ownBlog = true;
          }
        })
      }
    }
</script>

<style scoped>
  .mblog{
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    width:100%;
    min-height: 700px;
    padding: 20px 15px;
  }
</style>