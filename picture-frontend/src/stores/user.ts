import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useLoginUserStore = defineStore('loginUser', () => {
  //定义用户全局状态
  const loginUser = ref<any>({
    userName: '未登录',
  })

  //获取用户信息
  async function fetchLoginUser() {
    //todo
    //获取用户信息逻辑
    setTimeout(()=>{
      loginUser.value = {userName: "Darling",id: 1}
    })
  }

  //更新用户信息
  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser
  }

  //将状态导出给全局使用
  return { loginUser, fetchLoginUser, setLoginUser }
})
