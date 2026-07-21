<template>
  <a-row :wrap="false">
    <a-col flex="200px">
      <RouterLink to="/">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.png" alt="logo" />
          <div class="title">异世界云图库</div>
        </div>
      </RouterLink>
    </a-col>
    <a-col flex="auto">
      <a-menu
        v-model:selectedKeys="current"
        mode="horizontal"
        :items="items"
        @click="doMenuClick"
      />
    </a-col>
    <a-col flex="120px">
      <div class="user-login-status">
        <div v-if="loginUserStore.loginUser.id">
          <a-dropdown>
            <ASpace>
              <a-avatar :src="loginUserStore.loginUser.userAvatar" />
              {{ loginUserStore.loginUser.userName ?? '无名' }}
            </ASpace>
            <template #overlay>
              <a-menu>
                <a-menu-item @click="doLogout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>

        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </div>
    </a-col>
  </a-row>
</template>
<script lang="ts" setup>
import { h, ref } from 'vue'
import { HomeOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons-vue'
import { MenuProps, message } from 'ant-design-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
//选中当前菜单
const current = ref<string[]>(['mail'])
//监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  current.value = [to.path]
})


import { computed, h, ref } from 'vue'
import checkAccess from '@/access/checkAccess'
import ACCESS_ENUM from '@/access/accessEnum'

const menus = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/admin/userManage',
    icon: () => h(SettingOutlined),
    label: '用户管理',
    title: '用户管理',
  },
]

const items = computed(() => {
  return menus.filter((menu) => {
    const route = router.resolve(menu.key).matched.at(-1)

    // 菜单配置了不存在的路由，不显示
    if (!route) {
      return false
    }

    // 路由主动要求从菜单隐藏
    if (route.meta.hideInMenu) {
      return false
    }

    const needAccess =
      (route.meta.access as string | undefined) ??
      ACCESS_ENUM.NOT_LOGIN

    return checkAccess(loginUserStore.loginUser, needAccess)
  })
})
//点击菜单操作
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  })
}

import { healthUsingGet } from '@/api/mainController'
import { userLogoutUsingPost } from '@/api/userController'
import { useLoginUserStore } from '@/stores/user'

const loginUserStore = useLoginUserStore()

healthUsingGet().then((res) => {
  console.log(res)
})

// 用户注销
const doLogout = async () => {
  const res = await userLogoutUsingPost()
  console.log(res)
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
</script>
<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo {
  height: 48px;
}
</style>
