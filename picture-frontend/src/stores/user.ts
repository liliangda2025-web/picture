import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoginUserUsingGet } from '@/api/userController'

export const useLoginUserStore = defineStore('loginUser', () => {
  //定义用户全局状态
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  //获取用户信息
  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet()
    if (res.data.code === 0 && res.data.data){
      loginUser.value = res.data.data
    }
  }

  //更新用户信息
  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser
  }

  //将状态导出给全局使用
  return { loginUser, fetchLoginUser, setLoginUser }
})
