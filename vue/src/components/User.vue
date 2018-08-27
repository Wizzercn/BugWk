<template>
    <div>
        <el-row>
            <el-button type="primary" @click="openAdd">新建用户</el-button>
        </el-row>
        <el-table
                :data="tableData"
                style="width: 100%">
            <el-table-column
                    prop="loginname"
                    label="用户名"
                    width="200">
            </el-table-column>
            <el-table-column
                    prop="realname"
                    label="姓名"
                    width="200">
            </el-table-column>
            <el-table-column
                    prop="nickname"
                    label="昵称" width="200">
            </el-table-column>
            <el-table-column
                    prop="disabled"
                    label="用户状态">
                <template scope="scope">
                    <el-switch
                            v-model="scope.row.disabled"
                            active-color="#ff4949"
                            inactive-color="#13ce66">
                    </el-switch>
                </template>
            </el-table-column>
            <el-table-column
                    prop="role"
                    label="角色">
            </el-table-column>
            <el-table-column label="操作">
                <template scope="scope">
                    <el-button
                            size="small"
                            type="primary"
                            @click="handleEdit(scope.$index, scope.row)">编辑
                    </el-button>
                    <el-button
                            size="small"
                            type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div align="center">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-size="pagesize"
                    layout="total, prev, pager, next"
                    :total="totalCount">
            </el-pagination>
        </div>
        <el-dialog title="新建用户" :visible.sync="dialogFormVisible" @close="resetForm('form')">
            <el-form :model="form" :rules="rules" ref="form">
                <el-form-item label="用户名" prop="loginname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="form.loginname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="form.nickname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="姓名" prop="realname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="form.realname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="输入密码" prop="loginpass" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input type="password" v-model="form.loginpass" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkpass" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input type="password" v-model="form.checkpass" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="状态" prop="disabled" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-switch
                                v-model="form.disabled"
                                active-color="#ff4949"
                                inactive-color="#13ce66">
                        </el-switch>
                    </el-col>
                </el-form-item>
                <el-form-item label="角色" prop="role" :label-width="formLabelWidth">
                    <el-select v-model="form.role" placeholder="请选择用户角色">
                        <el-option label="管理员" value="ADMIN"></el-option>
                        <el-option label="普通用户" value="USER"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('form')">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="编辑用户" :visible.sync="dialogEditFormVisible" @close="resetEditForm('editForm')">
            <el-form :model="form" :rules="editRules" ref="editForm">
                <el-form-item label="用户名" prop="loginname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="form.loginname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="form.nickname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="姓名" prop="realname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="form.realname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="状态" prop="disabled" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-switch
                                v-model="form.disabled"
                                active-color="#ff4949"
                                inactive-color="#13ce66">
                        </el-switch>
                    </el-col>
                </el-form-item>
                <el-form-item label="角色" prop="role" :label-width="formLabelWidth">
                    <el-select v-model="form.role" placeholder="请选择用户角色">
                        <el-option label="管理员" value="ADMIN"></el-option>
                        <el-option label="普通用户" value="USER"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetEditForm('editForm')">取 消</el-button>
                <el-button type="primary" @click="edit">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>

    export default {
        name: "User",
        data() {
            var validatePass = (rule, value, callback) => {
                if (value !== this.form.loginpass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                dialogFormVisible: false,     //模态框是否显示
                dialogEditFormVisible: false,     //模态框是否显示
                form: {
                    id: '',
                    loginname: '',
                    loginpass: '',
                    checkpass: '',
                    nickname: '',
                    realname: '',
                    disabled: true,
                    role: ''
                },
                rules: {
                    loginname: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    nickname: [
                        {required: true, message: '请输入昵称', trigger: 'blur'}
                    ],
                    realname: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ],
                    loginpass: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 3, max: 8, message: '密码长度必须是3-8位', trigger: 'change'}
                    ],
                    checkpass: [
                        {required: true, message: '请再次输入密码', trigger: 'blur'},
                        {validator: validatePass, trigger: 'change'}
                    ],
                    role: [
                        {required: true, message: '请选择用户角色', trigger: 'blur'}
                    ]
                },
                editRules: {
                    loginname: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    nickname: [
                        {required: true, message: '请输入昵称', trigger: 'blur'}
                    ],
                    realname: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ],
                    role: [
                        {required: true, message: '请选择用户角色', trigger: 'blur'}
                    ]
                },
                formLabelWidth: '120px',
                tableData: [],
                pagesize: 10,
                currentPage: 1,
                start: 1,
                totalCount: 0
            }
        },
        methods: {
            openAdd() {
                this.dialogFormVisible = true
            },
            save() {
                this.$refs["form"].validate((valid) => {
                    if (valid) {
                        this.$http.post(platform_base + '/platform/user/add', this.form, {
                            withCredentials: true
                        }).then((resp) => {
                            return resp.data
                        }).then((d) => {
                            if (d.code == 0) {
                                this.$message({
                                    message: d.msg,
                                    type: 'success'
                                });
                                this.resetForm("form");
                                this.pageData(1, this.pagesize);
                            } else {
                                this.$message({
                                    message: d.msg,
                                    type: 'error'
                                });
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            edit() {
                this.$refs["editForm"].validate((valid) => {
                    if (valid) {
                        this.$http.post(platform_base + '/platform/user/edit', this.form, {
                            withCredentials: true
                        }).then((resp) => {
                            return resp.data
                        }).then((d) => {
                            if (d.code == 0) {
                                this.$message({
                                    message: d.msg,
                                    type: 'success'
                                });
                                this.resetEditForm("editForm");
                                this.pageData(1, this.pagesize);
                            } else {
                                this.$message({
                                    message: d.msg,
                                    type: 'error'
                                });
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.dialogFormVisible = false;
                this.$refs[formName].resetFields();
            },
            resetEditForm(formName) {
                this.dialogEditFormVisible = false;
                this.$refs[formName].resetFields();
            },
            handleEdit: function (index, row) {
                this.$http.post(platform_base + '/platform/user/get', {
                    "id": row.id
                }, {
                    withCredentials: true
                }).then((resp) => {
                    return resp.data
                }).then((d) => {
                    if (d.code == 0) {
                        this.form = d.data;
                        this.dialogEditFormVisible = true;
                    } else {
                        this.$message({
                            message: d.msg,
                            type: 'error'
                        });
                    }
                });
            },
            handleDelete: function (index, row) {
                this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post(platform_base + '/platform/user/del', {
                        "id": row.id
                    }, {
                        withCredentials: true
                    }).then((resp) => {
                        return resp.data
                    }).then((d) => {
                        if (d.code == 0) {
                            this.$message({
                                message: d.msg,
                                type: 'success'
                            });
                            this.pageData(1, this.pagesize);
                        } else {
                            this.$message({
                                message: d.msg,
                                type: 'error'
                            });
                        }
                    });
                }).catch(() => {
                })
            },
            handleCurrentChange: function (val) {
                this.currentPage = val;
                this.pageData(this.currentPage, this.pagesize);
            },
            pageData(page, size) {
                this.$http.post(platform_base + '/platform/user/data', {
                    "page": page,
                    "size": size
                }, {
                    withCredentials: true
                }).then((resp) => {
                    return resp.data
                }).then((d) => {
                    if (d.code == 0) {
                        this.tableData = d.data.list;
                        this.totalCount = d.data.pager.recordCount;
                    } else {
                        this.$message({
                            message: d.msg,
                            type: 'error'
                        });
                    }
                });
            }
        },
        mounted: function () {
            this.pageData(this.currentPage, this.pagesize);
        }
    }
</script>

<style scoped>

</style>