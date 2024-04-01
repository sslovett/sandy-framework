export const tableOption = {
  searchMenuSpan: 6,
  columnBtn: false,
  border: true,
  selection: true,
  index: false,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  menuWidth: 300,
  align: 'center',
  refreshBtn: true,
  searchSize: 'mini',
  addBtn: false,
  editBtn: false,
  delBtn: false,
  viewBtn: false,
  props: {
    label: 'label',
    value: 'value'
  },
  column: [{
    label: '用户名',
    prop: 'userName',
    search: true
  }, {
    label: '昵称',
    prop: 'nickName'
  }, {
    label: '邮箱',
    prop: 'email'
  }, {
    label: '手机号',
    prop: 'phone'
  }, {
    label: '创建时间',
    prop: 'createTime'
  }, {
    label: '状态',
    prop: 'status',
    type: 'select',
    dicData: [
      {
        label: '禁用',
        value: 0
      }, {
        label: '正常',
        value: 1
      }
    ]

  }]
}
