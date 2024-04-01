<template>
  <el-dialog
    v-model="visible"
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      :rules="dataRule"
      label-width="80px"
      @keyup.enter="onSubmit()"
    >
      <el-form-item
        label="用户名"
        prop="userName"
      >
        <el-input
          v-model="dataForm.userName"
          placeholder="登录帐号"
        />
      </el-form-item>
      <el-form-item
        label="密码"
        prop="password"
        :class="{ 'is-required': !dataForm.id }"
      >
        <el-input
          v-model="dataForm.password"
          type="password"
          placeholder="密码"
        />
      </el-form-item>
      <el-form-item
        label="确认密码"
        prop="comfirmPassword"
        :class="{ 'is-required': !dataForm.id }"
      >
        <el-input
          v-model="dataForm.comfirmPassword"
          type="password"
          placeholder="确认密码"
        />
      </el-form-item>
      <el-form-item
        label="昵称"
        prop="nickName"
      >
        <el-input
          v-model="dataForm.nickName"
          placeholder="昵称"
        />
      </el-form-item>
      <el-form-item
        label="邮箱"
        prop="email"
      >
        <el-input
          v-model="dataForm.email"
          placeholder="邮箱"
        />
      </el-form-item>
      <el-form-item
        label="手机号"
        prop="mobile"
      >
        <el-input
          v-model="dataForm.mobile"
          maxlength="11"
          placeholder="手机号"
        />
      </el-form-item>
      <el-form-item
        label="角色"
        prop="roleIdList"
      >
        <el-checkbox-group v-model="dataForm.roleIdList">
          <el-checkbox
            v-for="role in roleList"
            :key="role.id"
            :label="role.id"
          >
            {{ role.name }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item
        label="状态"
        prop="status"
      >
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">
            禁用
          </el-radio>
          <el-radio :label="1">
            正常
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button
          type="primary"
          @click="onSubmit()"
        >确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { isEmail, isMobile } from '@/utils/validate'
import { Debounce } from '@/utils/debounce'
import { encrypt } from '@/utils/crypto'
const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataForm = reactive({
  id: 0,
  userName: '',
  password: '',
  comfirmPassword: '',
  nickName: '',
  email: '',
  mobile: '',
  roleIdList: [],
  status: 1
})

// eslint-disable-next-line no-unused-vars
const validatePassword = (rule, value, callback) => {
  if (!dataForm.id && !/\S/.test(value)) {
    callback(new Error('密码不能为空'))
  } else {
    callback()
  }
}
// eslint-disable-next-line no-unused-vars
const validateComfirmPassword = (rule, value, callback) => {
  if (!dataForm.id && !/\S/.test(value)) {
    dataForm.password = ''
    callback(new Error('确认密码不能为空'))
  } else if (dataForm.password !== value) {
    callback(new Error('确认密码与密码输入不一致'))
  } else {
    callback()
  }
}
// eslint-disable-next-line no-unused-vars
const validateEmail = (rule, value, callback) => {
  if (!isEmail(value)) {
    callback(new Error('邮箱格式错误'))
  } else {
    callback()
  }
}
// eslint-disable-next-line no-unused-vars
const validateMobile = (rule, value, callback) => {
  if (!isMobile(value)) {
    callback(new Error('手机号格式错误'))
  } else {
    callback()
  }
}
const dataRule = {
  userName: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的用户名', trigger: 'blur' }
  ],
  password: [
    { validator: validatePassword, trigger: 'blur' }
  ],
  comfirmPassword: [
    { validator: validateComfirmPassword, trigger: 'blur' }
  ],
  nickName: [
    { required: true, message: '昵称不能为空', trigger: 'blur' },
    { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的昵称', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { validator: validateEmail, trigger: 'blur' }
  ],
  mobile: [
    { required: true, message: '手机号不能为空', trigger: 'blur' },
    { validator: validateMobile, trigger: 'blur' }
  ]
}

const roleList = ref([])
const dataFormRef = ref(null)
const init = (id) => {
  dataForm.id = id || 0
  http({
    url: http.adornUrl('/role/list'),
    method: 'get',
    params: http.adornParams()
  }).then(({ data }) => {
    roleList.value = data
  }).then(() => {
    visible.value = true
    nextTick(() => {
      dataFormRef.value?.resetFields()
    })
  }).then(() => {
    if (dataForm.id) {
      http({
        url: http.adornUrl(`/user/info/${dataForm.id}`),
        method: 'get',
        params: http.adornParams()
      }).then(({ data }) => {
        dataForm.userName = data.userName
        dataForm.email = data.email
        dataForm.mobile = data.phone
        dataForm.roleIdList = data.roleIdList
        dataForm.status = data.status
        dataForm.nickName = data.nickName
      })
    }
  })
}
defineExpose({ init })

/**
 * 表单提交
 */
const onSubmit = Debounce(() => {
  dataFormRef.value?.validate((valid) => {
    if (valid) {
      http({
        url: dataForm.id ? http.adornUrl('/user/update') : http.adornUrl('/user/save'),
        method: 'post',
        data: http.adornData({
          id: dataForm.id || undefined,
          userName: dataForm.userName,
          password: encrypt(dataForm.password),
          nickName: dataForm.nickName,
          email: dataForm.email,
          phone: dataForm.mobile,
          status: dataForm.status,
          roleIdList: dataForm.roleIdList
        })
      }).then(() => {
        ElMessage({
          message: '操作成功',
          type: 'success',
          duration: 1500,
          onClose: () => {
            visible.value = false
            emit('refreshDataList')
          }
        })
      })
    }
  })
})

</script>
