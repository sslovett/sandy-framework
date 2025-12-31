<template>
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="600px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item label="角色名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio label="1">正常</el-radio>
          <el-radio label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="菜单权限" prop="menuIdList">
        <div class="menu-tree-container">
          <div class="menu-tree-toolbar">
            <el-checkbox v-model="menuExpand" @change="handleExpandChange">展开/折叠</el-checkbox>
            <el-checkbox v-model="menuCheckAll" @change="handleCheckAllChange">全选/全不选</el-checkbox>
          </div>
          <el-tree
            ref="menuTreeRef"
            :data="menuOptions"
            :props="{ label: 'menuName', children: 'list' }"
            :default-expand-all="menuExpand"
            show-checkbox
            node-key="id"
            :default-checked-keys="formData.menuIdList"
            :check-strictly="false"
            class="menu-tree"
          />
        </div>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="formLoading" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import * as RoleApi from '@/api/sys/role'
import * as MenuApi from '@/api/sys/menu'

const emit = defineEmits(['success'])

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref<'create' | 'update'>('create')
const formRef = ref()
const menuTreeRef = ref()
const menuOptions = ref<MenuApi.SysMenuVO[]>([])
const menuExpand = ref(true)
const menuCheckAll = ref(false)

const formData = ref<RoleApi.SysRoleVO>({
  id: undefined,
  name: '',
  status: '1',
  remark: '',
  menuIdList: []
})

const formRules = reactive({
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
})

// 重置表单
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: '',
    status: '1',
    remark: '',
    menuIdList: []
  }
  menuExpand.value = true
  menuCheckAll.value = false
  formRef.value?.resetFields()
}

// 将扁平数组转换为树形结构
const buildMenuTree = (menus: MenuApi.SysMenuVO[]): any[] => {
  const menuMap = new Map<number, any>()
  const result: any[] = []

  // 先将所有菜单放入 map
  menus.forEach((menu) => {
    menuMap.set(menu.id!, { ...menu, list: [] })
  })

  // 构建树形结构
  menus.forEach((menu) => {
    const node = menuMap.get(menu.id!)
    if (menu.parentId === 0) {
      result.push(node)
    } else {
      const parent = menuMap.get(menu.parentId)
      if (parent) {
        parent.list.push(node)
      }
    }
  })

  // 处理空的 list
  const processChildren = (nodes: any[]): any[] => {
    return nodes.map((node) => {
      if (node.list && node.list.length > 0) {
        node.list = processChildren(node.list)
      } else {
        node.list = undefined
      }
      return node
    })
  }

  return processChildren(result)
}

// 获取菜单树
const getMenuTree = async () => {
  try {
    const res = await MenuApi.getMenuTable()
    // 将扁平数组转换为树形结构
    menuOptions.value = buildMenuTree(res || [])
  } catch (error) {
    console.error('获取菜单树失败:', error)
  }
}

// 展开/折叠
const handleExpandChange = (val: boolean) => {
  const nodes = menuTreeRef.value?.store?.nodesMap
  if (nodes) {
    for (const key in nodes) {
      nodes[key].expanded = val
    }
  }
}

// 全选/全不选
const handleCheckAllChange = (val: boolean) => {
  if (val) {
    menuTreeRef.value?.setCheckedNodes(getAllMenuNodes(menuOptions.value))
  } else {
    menuTreeRef.value?.setCheckedKeys([])
  }
}

// 获取所有菜单节点
const getAllMenuNodes = (menus: any[]): any[] => {
  const nodes: any[] = []
  const traverse = (items: any[]) => {
    for (const item of items) {
      nodes.push(item)
      if (item.list && item.list.length > 0) {
        traverse(item.list)
      }
    }
  }
  traverse(menus)
  return nodes
}

// 获取叶子节点ID（用于设置选中状态）
const getLeafMenuIds = (menuIds: number[], menus: any[]): number[] => {
  const allMenuMap = new Map<number, any>()
  const collectMenus = (items: any[]) => {
    for (const item of items) {
      allMenuMap.set(item.id, item)
      if (item.list && item.list.length > 0) {
        collectMenus(item.list)
      }
    }
  }
  collectMenus(menus)

  // 只返回叶子节点的ID
  return menuIds.filter((id) => {
    const menu = allMenuMap.get(id)
    return menu && (!menu.list || menu.list.length === 0)
  })
}

// 打开弹窗
const open = async (type: 'create' | 'update', id?: number) => {
  dialogVisible.value = true
  formType.value = type
  dialogTitle.value = type === 'create' ? '新增角色' : '编辑角色'
  resetForm()

  // 获取菜单树
  await getMenuTree()

  // 如果是编辑，获取角色详情
  if (type === 'update' && id) {
    formLoading.value = true
    try {
      const res = await RoleApi.getRoleInfo(id)
      formData.value = { ...res }
      // 设置选中的菜单（只设置叶子节点，父节点会自动半选/全选）
      nextTick(() => {
        if (res.menuIdList && menuTreeRef.value) {
          const leafIds = getLeafMenuIds(res.menuIdList, menuOptions.value)
          menuTreeRef.value.setCheckedKeys(leafIds)
        }
      })
    } catch (error) {
      console.error('获取角色信息失败:', error)
    } finally {
      formLoading.value = false
    }
  }
}

// 提交表单
const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  formLoading.value = true
  try {
    const data = { ...formData.value }
    // 获取选中的菜单ID（包括半选中的父节点）
    if (menuTreeRef.value) {
      const checkedKeys = menuTreeRef.value.getCheckedKeys()
      const halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys()
      data.menuIdList = [...checkedKeys, ...halfCheckedKeys]
    }
    if (formType.value === 'create') {
      await RoleApi.createRole(data)
      ElMessage.success('新增成功')
    } else {
      await RoleApi.updateRole(data)
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    emit('success')
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    formLoading.value = false
  }
}

defineExpose({ open })
</script>

<style scoped>
.menu-tree-container {
  width: 100%;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
}

.menu-tree-toolbar {
  padding: 10px;
  border-bottom: 1px solid var(--el-border-color);
  background-color: var(--el-fill-color-light);
}

.menu-tree-toolbar :deep(.el-checkbox) {
  margin-right: 20px;
}

.menu-tree-toolbar :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: var(--el-color-primary);
  border-color: var(--el-color-primary);
}

.menu-tree-toolbar :deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: var(--el-color-primary);
}

.menu-tree {
  padding: 10px;
  max-height: 300px;
  overflow-y: auto;
}
</style>
