<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item label="上级菜单" prop="parentId">
        <el-tree-select
          v-model="formData.parentId"
          :data="menuOptions"
          :props="{ label: 'menuName', value: 'id', children: 'list' }"
          placeholder="选择上级菜单"
          check-strictly
          clearable
          filterable
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="菜单类型" prop="type">
        <el-radio-group v-model="formData.type">
          <el-radio-button :value="0">目录</el-radio-button>
          <el-radio-button :value="1">菜单</el-radio-button>
          <el-radio-button :value="2">按钮</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="formData.menuName" placeholder="请输入菜单名称" clearable />
      </el-form-item>
      <el-form-item v-if="formData.type !== 2" label="菜单图标">
        <IconSelect v-model="formData.icon" clearable />
      </el-form-item>
      <el-form-item v-if="formData.type !== 2" label="路由地址" prop="path">
        <el-input v-model="formData.path" placeholder="请输入路由地址，如：sys/user" clearable />
      </el-form-item>
      <el-form-item v-if="formData.type === 1" label="组件路径" prop="component">
        <el-input v-model="formData.component" placeholder="请输入组件路径（可选）" clearable />
      </el-form-item>
      <el-form-item label="权限标识" prop="perms">
        <el-input v-model="formData.perms" placeholder="请输入权限标识，如：sys:user:list" clearable />
      </el-form-item>
      <el-form-item label="排序" prop="orderNum">
        <el-input-number v-model="formData.orderNum" :min="0" :max="999" controls-position="right" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio value="1">正常</el-radio>
          <el-radio value="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :disabled="formLoading" @click="submitForm">确定</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as MenuApi from '@/api/sys/menu'

defineOptions({ name: 'SysMenuForm' })

const message = useMessage()
const emit = defineEmits(['success'])

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref<'create' | 'update'>('create')
const formRef = ref()
const menuOptions = ref<MenuApi.SysMenuVO[]>([])

const formData = ref<MenuApi.SysMenuVO>({
  id: undefined,
  parentId: 0,
  menuName: '',
  path: '',
  component: '',
  type: 1,
  status: '1',
  perms: '',
  icon: '',
  orderNum: 0,
  remark: ''
})

const formRules = reactive({
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择菜单类型', trigger: 'change' }]
})

// 重置表单
const resetForm = () => {
  formData.value = {
    id: undefined,
    parentId: 0,
    menuName: '',
    path: '',
    component: '',
    type: 1,
    status: '1',
    perms: '',
    icon: '',
    orderNum: 0,
    remark: ''
  }
  formRef.value?.resetFields()
}

// 获取菜单选项（用于选择上级菜单）
const getMenuOptions = async () => {
  try {
    const res = await MenuApi.getMenuList()
    // 添加顶级菜单选项
    menuOptions.value = [
      {
        id: 0,
        menuName: '顶级菜单',
        parentId: -1,
        type: 0,
        status: '1',
        orderNum: 0,
        list: res || []
      }
    ]
  } catch (error) {
    console.error('获取菜单选项失败:', error)
  }
}

// 打开弹窗
const open = async (type: 'create' | 'update', id?: number, parentId?: number) => {
  dialogVisible.value = true
  formType.value = type
  dialogTitle.value = type === 'create' ? '新增菜单' : '编辑菜单'
  resetForm()

  // 获取菜单选项
  await getMenuOptions()

  // 如果是新增且指定了父菜单
  if (type === 'create' && parentId !== undefined) {
    formData.value.parentId = parentId
  }

  // 如果是编辑，获取菜单详情
  if (type === 'update' && id) {
    formLoading.value = true
    try {
      const res = await MenuApi.getMenuInfo(id)
      formData.value = res
    } catch (error) {
      console.error('获取菜单信息失败:', error)
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
    if (formType.value === 'create') {
      await MenuApi.createMenu(data)
      message.success('新增成功')
    } else {
      await MenuApi.updateMenu(data)
      message.success('修改成功')
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
