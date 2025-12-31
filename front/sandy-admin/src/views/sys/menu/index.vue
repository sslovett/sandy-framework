<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item label="菜单名称" prop="menuName">
        <el-input
          v-model="queryParams.menuName"
          class="!w-200px"
          clearable
          placeholder="请输入菜单名称"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" class="!w-200px" clearable placeholder="请选择状态">
          <el-option label="正常" value="1" />
          <el-option label="停用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          重置
        </el-button>
        <el-button v-hasPermi="['sys:menu:save']" plain type="primary" @click="openForm('create')">
          <Icon class="mr-5px" icon="ep:plus" />
          新增
        </el-button>
        <el-button plain type="danger" @click="toggleExpandAll">
          <Icon class="mr-5px" icon="ep:sort" />
          展开/折叠
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="filteredMenuList"
      :default-expand-all="isExpandAll"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      border
      row-key="id"
    >
      <el-table-column label="菜单名称" min-width="180" prop="menuName" show-overflow-tooltip>
        <template #default="{ row }">
          <Icon v-if="row.icon" :icon="row.icon" class="mr-5px" />
          <span>{{ row.menuName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="图标" prop="icon" width="80">
        <template #default="{ row }">
          <Icon v-if="row.icon" :icon="row.icon" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" prop="orderNum" width="70" />
      <el-table-column label="权限标识" min-width="150" prop="perms" show-overflow-tooltip />
      <el-table-column label="路由地址" min-width="120" prop="path" show-overflow-tooltip />
      <el-table-column label="组件路径" min-width="150" prop="component" show-overflow-tooltip />
      <el-table-column align="center" label="类型" prop="type" width="80">
        <template #default="{ row }">
          <el-tag v-if="row.type === 0" type="primary">目录</el-tag>
          <el-tag v-else-if="row.type === 1" type="success">菜单</el-tag>
          <el-tag v-else-if="row.type === 2" type="warning">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" prop="status" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === '1' ? 'success' : 'danger'">
            {{ row.status === '1' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="180">
        <template #default="{ row }">
          <el-button v-hasPermi="['sys:menu:update']" link type="primary" @click="openForm('update', row.id)">修改</el-button>
          <el-button
            v-if="row.type !== 2"
            v-hasPermi="['sys:menu:save']"
            link
            type="primary"
            @click="openForm('create', undefined, row.id)"
          >
            新增
          </el-button>
          <el-button v-hasPermi="['sys:menu:delete']" link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </ContentWrap>

  <!-- 新增/编辑弹窗 -->
  <MenuForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { nextTick } from 'vue'
import * as MenuApi from '@/api/sys/menu'
import MenuForm from './MenuForm.vue'

defineOptions({ name: 'SysMenu' })

const message = useMessage()
const loading = ref(false)
const menuList = ref<MenuApi.SysMenuVO[]>([])
const formRef = ref()
const queryFormRef = ref()
const isExpandAll = ref(true)
const refreshTable = ref(true)

const queryParams = reactive({
  menuName: '',
  status: ''
})

// 过滤后的菜单列表
const filteredMenuList = computed(() => {
  if (!queryParams.menuName && !queryParams.status) {
    return menuList.value
  }
  return filterMenuTree(menuList.value)
})

// 递归过滤菜单树
const filterMenuTree = (menus: any[]): any[] => {
  return menus
    .map((menu) => {
      const children = menu.children ? filterMenuTree(menu.children) : []
      const matchName = !queryParams.menuName || menu.menuName.includes(queryParams.menuName)
      const matchStatus = !queryParams.status || menu.status === queryParams.status
      if (matchName && matchStatus) {
        return { ...menu, children: children.length > 0 ? children : menu.children }
      }
      if (children.length > 0) {
        return { ...menu, children }
      }
      return null
    })
    .filter(Boolean)
}

// 将扁平数组转换为树形结构
const buildMenuTree = (menus: MenuApi.SysMenuVO[]): any[] => {
  const menuMap = new Map<number, any>()
  const result: any[] = []

  // 先将所有菜单放入 map
  menus.forEach((menu) => {
    menuMap.set(menu.id!, { ...menu, children: [] })
  })

  // 构建树形结构
  menus.forEach((menu) => {
    const node = menuMap.get(menu.id!)
    if (menu.parentId === 0) {
      result.push(node)
    } else {
      const parent = menuMap.get(menu.parentId)
      if (parent) {
        parent.children.push(node)
      }
    }
  })

  // 处理 children，如果为空则设为 undefined
  const processChildren = (nodes: any[]): any[] => {
    return nodes.map((node) => {
      if (node.children && node.children.length > 0) {
        node.hasChildren = true
        node.children = processChildren(node.children)
      } else {
        node.hasChildren = false
        node.children = undefined
      }
      return node
    })
  }

  return processChildren(result)
}

// 获取菜单列表
const getList = async () => {
  loading.value = true
  try {
    const res = await MenuApi.getMenuTable()
    menuList.value = buildMenuTree(res || [])
  } catch (error) {
    console.error('获取菜单列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleQuery = () => {
  isExpandAll.value = true
  refreshTable.value = false
  nextTick(() => {
    refreshTable.value = true
  })
}

// 重置
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  queryParams.menuName = ''
  queryParams.status = ''
  handleQuery()
}

// 展开/折叠
const toggleExpandAll = () => {
  refreshTable.value = false
  isExpandAll.value = !isExpandAll.value
  nextTick(() => {
    setTimeout(() => {
      refreshTable.value = true
    }, 50)
  })
}

// 打开表单
const openForm = (type: 'create' | 'update', id?: number, parentId?: number) => {
  formRef.value?.open(type, id, parentId)
}

// 删除
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await MenuApi.deleteMenu(id)
    message.success('删除成功')
    getList()
  } catch {}
}

onMounted(() => {
  getList()
})
</script>
