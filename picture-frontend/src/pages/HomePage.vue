<template>
  <!-- 搜索框 -->
  <div class="search-bar">
    <a-input-search
      placeholder="从海量图片中搜索"
      v-model:value="searchParams.searchText"
      enter-button="搜索"
      size="large"
      @search="doSearch"
    />
  </div>
  <!-- 分类 + 标签 -->
  <a-tabs v-model:activeKey="selectedCategory" @change="doSearch">
    <a-tab-pane key="all" tab="全部" />
    <a-tab-pane v-for="category in categoryList" :key="category" :tab="category" />
  </a-tabs>
  <div class="tag-bar">
    <span style="margin-right: 8px">标签：</span>
    <a-space :size="[0, 8]" wrap>
      <a-checkable-tag
        v-for="(tag, index) in tagList"
        :key="tag"
        v-model:checked="selectedTagList[index]"
        @change="doSearch"
      >
        {{ tag }}
      </a-checkable-tag>
    </a-space>
  </div>

  <!-- 图片列表 -->
  <a-list
    :grid="{ gutter: 16, xs: 1, sm: 2, md: 3, lg: 4, xl: 5, xxl: 6 }"
    :data-source="dataList"
    :pagination="pagination"
    :loading="loading"
  >
    <template #renderItem="{ item: picture }">
      <a-list-item style="padding: 0">
        <!-- 单张图片 -->
        <a-card hoverable @click="doClickPicture(picture)">
          <template #cover>
            <img style="height: 180px; object-fit: cover" :alt="picture.name" :src="picture.url" />
          </template>
          <a-card-meta :title="picture.name">
            <template #description>
              <a-flex>
                <a-tag color="green">
                  {{ picture.category ?? '默认' }}
                </a-tag>
                <a-tag v-for="tag in picture.tags" :key="tag">
                  {{ tag }}
                </a-tag>
              </a-flex>
            </template>
          </a-card-meta>
        </a-card>
      </a-list-item>
    </template>
  </a-list>
</template>
<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import {
  listPictureVoByPageUsingPost,
  listPictureTagCategoryUsingGet,
} from '@/api/pictureController'
import { useRouter } from 'vue-router'
// 数据
const dataList = ref([])
const total = ref(0)
const loading = ref(true)

// 搜索条件
const searchParams = reactive<API.PictureQueryRequest>({
  current: 1,
  pageSize: 12,
  sortField: 'createTime',
  sortOrder: 'descend',
})

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.current ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    // 切换页号时，会修改搜索参数并获取数据
    onChange: (page, pageSize) => {
      searchParams.current = page
      searchParams.pageSize = pageSize
      fetchData()
    },
  }
})

const fetchData = async () => {
  loading.value = true
  // 转换搜索参数
  const params = {
    ...searchParams,
    tags: [],
  }
  if (selectedCategory.value !== 'all') {
    params.category = selectedCategory.value
  }
  selectedTagList.value.forEach((useTag, index) => {
    if (useTag) {
      params.tags.push(tagList.value[index])
    }
  })
  const res = await listPictureVoByPageUsingPost(params)
  if (res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
  loading.value = false
}

const doSearch = () => {
  // 重置搜索条件
  searchParams.current = 1
  fetchData()
}

const categoryList = ref<string[]>([])
const selectedCategory = ref<string>('all')
const tagList = ref<string[]>([])
const selectedTagList = ref<string[]>([])

// 获取标签和分类选项
const getTagCategoryOptions = async () => {
  const res = await listPictureTagCategoryUsingGet()
  if (res.data.code === 0 && res.data.data) {
    // 转换成下拉选项组件接受的格式
    categoryList.value = res.data.data.categoryList ?? []
    tagList.value = res.data.data.tagList ?? []
  } else {
    message.error('加载分类标签失败，' + res.data.message)
  }
}

const router = useRouter()
// 跳转至图片详情
const doClickPicture = (picture) => {
  router.push({
    path: `/picture/${picture.id}`,
  })
}

// 页面加载时请求一次
onMounted(() => {
  getTagCategoryOptions()
  fetchData()
})
</script>
<style scoped>
.search-bar,
.tag-bar,
.search-bar + :deep(.ant-tabs),
.tag-bar + :deep(.ant-list) {
  width: min(100%, 1440px);
  margin-right: auto;
  margin-left: auto;
}

.search-bar {
  margin-top: 8px;
  margin-bottom: 24px;
}

.search-bar :deep(.ant-input-affix-wrapper),
.search-bar :deep(.ant-input-search-button) {
  height: 44px;
}

.search-bar :deep(.ant-input-affix-wrapper) {
  padding-inline: 16px;
  border-radius: 10px 0 0 10px;
}

.search-bar :deep(.ant-input-search-button) {
  padding-inline: 24px;
  border-radius: 0 10px 10px 0 !important;
}

.search-bar + :deep(.ant-tabs) {
  margin-bottom: 18px;
}

:deep(.ant-tabs-nav) {
  margin-bottom: 0;
}

:deep(.ant-tabs-tab) {
  padding: 10px 4px 14px;
}

.tag-bar {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  min-height: 54px;
  margin-bottom: 30px;
  padding: 14px 18px;
  color: #475569;
  background: #f8fafc;
  border: 1px solid #eef2f6;
  border-radius: 12px;
}

.tag-bar > span {
  flex: none;
  padding-top: 2px;
  font-weight: 500;
}

.tag-bar :deep(.ant-tag-checkable) {
  margin-inline-end: 4px;
  padding: 2px 10px;
  border-radius: 999px;
}

.tag-bar + :deep(.ant-list) {
  padding-bottom: 48px;
}

:deep(.ant-list-grid .ant-list-item) {
  height: 100%;
}

:deep(.ant-card) {
  height: 100%;
  overflow: hidden;
  border-color: #e8edf3;
  border-radius: 14px;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

:deep(.ant-card:hover) {
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgb(15 23 42 / 10%);
}

:deep(.ant-card-cover img) {
  width: 100%;
  height: 210px !important;
  object-fit: cover;
  transition: transform 0.25s ease;
}

:deep(.ant-card:hover .ant-card-cover img) {
  transform: scale(1.02);
}

:deep(.ant-card-body) {
  padding: 18px;
}

:deep(.ant-card-meta-title) {
  margin-bottom: 12px !important;
  font-size: 16px;
  line-height: 1.5;
}

:deep(.ant-card-meta-description .ant-flex) {
  flex-wrap: wrap;
  gap: 6px;
}

:deep(.ant-card-meta-description .ant-tag) {
  margin-inline-end: 0;
}

:deep(.ant-list-pagination) {
  margin-top: 32px;
}

@media (min-width: 1200px) {
  .tag-bar + :deep(.ant-list .ant-row > div) {
    width: 25% !important;
    max-width: 25% !important;
  }
}

@media (max-width: 767px) {
  .search-bar {
    margin-top: 0;
    margin-bottom: 18px;
  }

  .tag-bar {
    gap: 8px;
    margin-bottom: 22px;
    padding: 12px;
  }

  :deep(.ant-card-cover img) {
    height: 200px !important;
  }
}
</style>
