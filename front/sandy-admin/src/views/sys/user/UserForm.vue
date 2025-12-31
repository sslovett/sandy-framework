<template>
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="500px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item label="用户名" prop="userName">
        <el-input
          v-model="formData.userName"
          placeholder="请输入用户名"
          :disabled="formType === 'update'"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="formData.nickName" placeholder="请输入昵称" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="formData.password"
          type="password"
          :placeholder="formType === 'create' ? '请输入密码' : '留空则不修改密码'"
          show-password
        />
      </el-form-item>
      <el-form-item label="角色" prop="roleIdList">
        <el-select
          v-model="formData.roleIdList"
          multiple
          placeholder="请选择角色"
          style="width: 100%"
        >
          <el-option
            v-for="role in roleList"
            :key="role.id"
            :label="role.name"
            :value="role.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="formData.sex">
          <el-radio label="0">男</el-radio>
          <el-radio label="1">女</el-radio>
          <el-radio label="2">未知</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="1">正常</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="formLoading" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import * as UserApi from '@/api/sys/user'
import * as RoleApi from '@/api/sys/role'
import { encryptPassword } from '@/utils/encrypt'

const emit = defineEmits(['success'])

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref<'create' | 'update'>('create')
const formRef = ref()
const roleList = ref<RoleApi.SysRoleVO[]>([])

const formData = ref<UserApi.SysUserVO>({
  id: undefined,
  userName: '',
  nickName: '',
  password: '',
  email: '',
  phone: '',
  sex: '0',
  status: 1,
  roleIdList: []
})

const formRules = reactive({
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [
    {
      required: true,
      validator: (rule: any, value: string, callback: Function) => {
        if (formType.value === 'create' && !value) {
          callback(new Error('请输入密码'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  roleIdList: [
    {
      required: true,
      type: 'array',
      message: '请选择角色',
      trigger: 'change'
    }
  ]
})

// 获取角色列表
const getRoleList = async () => {
  try {
    const res = await RoleApi.getRoleList()
    // 只显示正常状态的角色
    roleList.value = (res || []).filter((role: RoleApi.SysRoleVO) => role.status === '1')
  } catch (error) {
    console.error('获取角色列表失败:', error)
  }
}

// 重置表单
const resetForm = () => {
  formData.value = {
    id: undefined,
    userName: '',
    nickName: '',
    password: '',
    email: '',
    phone: '',
    sex: '0',
    status: 1,
    roleIdList: []
  }
  formRef.value?.resetFields()
}

// 打开弹窗
const open = async (type: 'create' | 'update', id?: number) => {
  dialogVisible.value = true
  formType.value = type
  dialogTitle.value = type === 'create' ? '新增管理员' : '编辑管理员'
  resetForm()

  // 获取角色列表
  await getRoleList()

  if (type === 'update' && id) {
    formLoading.value = true
    try {
      const res = await UserApi.getUserInfo(id)
      formData.value = {
        ...res,
        password: '', // 编辑时不显示密码
        roleIdList: res.roleIdList || []
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
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
    // 如果有密码，进行 AES 加密
    if (data.password) {
      data.password = encryptPassword(data.password)
    }
    if (formType.value === 'create') {
      await UserApi.createUser(data)
      ElMessage.success('新增成功')
    } else {
      await UserApi.updateUser(data)
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
