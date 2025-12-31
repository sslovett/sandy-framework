<template>
  <div class="sys-log-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" :inline="true">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="操作" prop="operation">
          <el-input v-model="queryParams.operation" placeholder="请输入操作" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <Icon icon="ep:search" class="mr-5px" /> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <Icon icon="ep:refresh" class="mr-5px" /> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 列表 -->
    <el-card shadow="never" class="mt-10px">
      <template #header>
        <div class="card-header">
          <span>操作日志</span>
        </div>
      </template>

      <el-table v-loading="loading" :data="list" stripe style="width: 100%">
        <el-table-column label="ID" prop="id" width="80" />
        <el-table-column label="用户名" prop="username" width="120" />
        <el-table-column label="操作" prop="operation" min-width="150" show-overflow-tooltip />
        <el-table-column label="请求方法" prop="method" min-width="250" show-overflow-tooltip />
        <el-table-column label="请求参数" prop="params" min-width="200" show-overflow-tooltip />
        <el-table-column label="执行时长" prop="time" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.time > 1000 ? 'danger' : row.time > 500 ? 'warning' : 'success'">
              {{ row.time }}ms
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="IP地址" prop="ip" width="140" />
        <el-table-column label="创建时间" prop="createDate" width="180" />
      </el-table>

      <!-- 分页 -->
      <el-pagination
        class="mt-20px"
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="getList"
        @current-change="getList"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import * as LogApi from '@/api/sys/log'

defineOptions({ name: 'SysLog' })

const loading = ref(false)
const list = ref<LogApi.SysLogVO[]>([])
const total = ref(0)
const queryFormRef = ref()

const queryParams = reactive({
  username: '',
  operation: '',
  current: 1,
  size: 10
})

// 获取列表
const getList = async () => {
  loading.value = true
  try {
    const res = await LogApi.getLogPage(queryParams)
    list.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    console.error('获取日志列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleQuery = () => {
  queryParams.current = 1
  getList()
}

// 重置
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.sys-log-container {
  padding: 20px;
}
.search-card {
  margin-bottom: 10px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.mt-10px {
  margin-top: 10px;
}
.mt-20px {
  margin-top: 20px;
}
.mr-5px {
  margin-right: 5px;
}
</style>
