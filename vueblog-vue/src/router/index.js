import Vue from 'vue'
import Router from 'vue-router'
//导入所有组件
import Login from '../view/Login.vue'
import BlogDetail from '../view/BlogDetail.vue'
import BlogEdit from '../view/BlogEdit.vue'
import Blogs from '../view/Blogs.vue'
import Register from "../view/Register.vue"
//声明Router模块
Vue.use(Router)

export default new Router({
  mode:'history', //指定为history模式
  //定义路由
  routes: [
    //访问根路径时，重定向到组件Blogs
    {
      path: '/',
      name: 'Index',
      redirect: { name: "Blogs" }
    },
    //Blogs组件的路由路径是/blogs
    {
      path: '/blogs',
      name: 'Blogs',
      component: Blogs
    },
    //Login组件的路由路径是/login
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    //BlogEdit组件的路由路径是/blog/add
    {
      path: '/blog/add',
      name: 'BlogAdd',
      component: BlogEdit,
      meta:{
        requireAuth:true
      }
    },
    //BlogDetail组件的路由路径是/blog/:blogid，绑定一个参数博客id
    {
      path: '/blog/:blogid',
      name: 'BlogDetail',
      component: BlogDetail
    },
    //BlogEdit组件的路由路径是/blog/:blogid/edit，绑定一个参数博客id
    {
      path: '/blog/:blogid/edit',
      name: 'BlogEdit',
      component: BlogEdit,
      meta:{
        requireAuth:true
      }
    },
    //Register组件的路由路径是/register
    {
      path: '/register',
      name: 'Register',
      component: Register
    }
  ]
})
