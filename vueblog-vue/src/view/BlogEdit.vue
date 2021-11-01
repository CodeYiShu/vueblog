<template>
  <div>
    <!-- 头部 -->
    <Header></Header>
    <!-- 表单 -->
    <div class="m-content">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="ruleForm.title"></el-input>
        </el-form-item>
        <el-form-item label="摘要" prop="description">
          <el-input type="textarea" v-model="ruleForm.description"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <!-- 引入mavon-editor编辑器 -->
          <mavon-editor v-model="ruleForm.content"></mavon-editor>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Header from "../components/Header";
export default {
  components: { Header },
  data() {
      return {
        ruleForm: {
          id:'',
          title: '',  //标题
          description: '',  //摘要
          content:''  //内容
        },
        rules: {
          title: [
            { required: true, message: '请输入题目', trigger: 'blur' },
            { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '请输入摘要', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '请输入内容',trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {  //表单验证通过
            const _this = this;
            //发送/blog/edit请求到后台
            this.$axios.post("/blog/edit",this.ruleForm,{ 
              //由于需要认证才可以访问/blog/edit，所以需要请求头需要携带jwt
              //注意要从localStorage获取
              headers:{"Authorization":localStorage.getItem("jwt")}
            }).then(response =>{
              //弹框提示
              _this.$alert('操作成功', '提示', {
                confirmButtonText: '确定',
                callback: action => {  //弹完框之后的操作
                  //路由到主页
                  _this.$router.push("/blogs")
                }
              });
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    },
    created(){
      //从路由路径中获取参数blogid（需要使用route而不是router）
      const blogid = this.$route.params.blogid;
      //如果获取到博客id
      if(blogid){
        const _this = this;
        //发送/blog/博客id，到后台获取到此id的博客记录
        this.$axios.get("/blog/" + blogid)
        .then(response =>{
          //控制器方法返回的博客记录
          const blog = response.data.data;
          //将返回的博客id、标题、概述和内容设置到ruleForm的数据中
          _this.ruleForm.id = blog.id;
          _this.ruleForm.title = blog.title;
          _this.ruleForm.description = blog.description;
          _this.ruleForm.content = blog.content;
        })
      }

    }
}
</script>

<style scoped>
  .m-content{
    margin:0 auto;
    text-align: center;
  }
</style>