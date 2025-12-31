<template>
  <div class="sys-user-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" :inline="true">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="queryParams.userName" placeholder="请输入用户名" clearable @keyup.enter="handleQuery" />
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
          <span>管理员列表</span>
          <el-button v-hasPermi="['sys:user:save']" type="primary" @click="openForm('create')">
            <Icon icon="ep:plus" class="mr-5px" /> 新增
          </el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="list" stripe style="width: 100%">
        <el-table-column label="ID" prop="id" width="80" />
        <el-table-column label="用户名" prop="userName" min-width="120" />
        <el-table-column label="昵称" prop="nickName" min-width="120" />
        <el-table-column label="邮箱" prop="email" min-width="180" />
        <el-table-column label="手机号" prop="phone" min-width="120" />
        <el-table-column label="性别" prop="sex" width="80">
          <template #default="{ row }">
            {{ getSexLabel(row.sex) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="角色" min-width="150">
          <template #default="{ row }">
            <template v-if="row.roleList && row.roleList.length">
              <el-tag v-for="role in row.roleList" :key="role.id" class="mr-5px" size="small">
                {{ role.name }}
              </el-tag>
            </template>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" min-width="180" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button v-hasPermi="['sys:user:update']" link type="primary" @click="openForm('update', row.id)">编辑</el-button>
            <el-button v-hasPermi="['sys:user:delete']" link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
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

    <!-- 新增/编辑弹窗 -->
    <UserForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as UserApi from '@/api/sys/user'
import UserForm from './UserForm.vue'

defineOptions({ name: 'SysUser' })

const loading = ref(false)
const list = ref<UserApi.SysUserVO[]>([])
const total = ref(0)
const queryFormRef = ref()
const formRef = ref()

const queryParams = reactive({
  userName: '',
  current: 1,
  size: 10
})

// 获取性别标签
const getSexLabel = (sex: string) => {
  const map: Record<string, string> = { '0': '男', '1': '女', '2': '未知' }
  return map[sex] || '未知'
}

// 获取列表
const getList = async () => {
  loading.value = true
  try {
    const res = await UserApi.getUserPage(queryParams)
    list.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    console.error('获取用户列表失败:', error)
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

// 打开表单
const openForm = (type: 'create' | 'update', id?: number) => {
  formRef.value?.open(type, id)
}

// 删除
const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      type: 'warning'
    })
    await UserApi.deleteUser([id])
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    // 用户取消
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.sys-user-container {
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
