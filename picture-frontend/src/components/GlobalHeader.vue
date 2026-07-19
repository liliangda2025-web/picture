<template>
  <a-row :wrap="false">
    <a-col flex="200px">
      <RouterLink to="/">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.png" alt="logo" />
          <div class="title">绝区零云图库</div>
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
          {{ loginUserStore.loginUser.userName ?? '无名' }}
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
import { MenuProps } from 'ant-design-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
//选中当前菜单
const current = ref<string[]>(['mail'])
//监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  current.value = [to.path]
})
const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/about',
    label: '关于',
    title: '关于',
  },
  {
    key: 'others',
    label: h('a', { href: 'https://zzz.mihoyo.com/main/', target: '_blank' }, '绝区零'),
    title: '绝区零',
  },
])
//点击菜单操作
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  })
}

import { healthUsingGet } from '@/api/mainController'
import { useLoginUserStore } from '@/stores/user'

const loginUserStore = useLoginUserStore()

healthUsingGet().then((res) => {
  console.log(res)
})
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
