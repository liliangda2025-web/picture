<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { useLoginUserStore } from '@/stores/user'

//获取用户信息
const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()
</script>

<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style>
#app {
}
</style>
