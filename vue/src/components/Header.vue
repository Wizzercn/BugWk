<template>
    <div>
        <div style="float:right;position:relative;top: 10px;z-index:1000;">
            <div style="float:right;">
                <el-button v-show="adminVisible"  @click="resetIndex" size="small">重置索引</el-button>
                <el-button v-show="userVisible" @click="editUser" size="small">修改资料</el-button>
                <el-button v-show="userVisible" @click="logout" size="small">退出登录</el-button>
                <el-button v-show="!userVisible" @click="openLogin" size="small">登录</el-button>
            </div>
            <div style="float:right;width: 220px;padding-right: 20px;">
                <el-input size="small" placeholder="请输入内容" v-model="keyword" @keyup.enter.native="doSearch"
                          class="input-with-select">
                    <el-button slot="append" icon="el-icon-search" @click="doSearch"></el-button>
                </el-input>
            </div>
        </div>
        <el-menu ref="menu" :default-active="$route.name" :router="true" class="el-menu-demo" mode="horizontal"
                 @select="handleSelect">
            <el-menu-item index="lol" :route="{name:'lol'}">问题中心</el-menu-item>
            <el-menu-item index="user" v-show="adminVisible" :route="{name:'user'}">用户管理</el-menu-item>
        </el-menu>
        <div class="line"></div>
        <el-dialog title="用户登录" :visible.sync="dialogFormVisible" @close="resetForm('loginForm')">
            <el-form :model="loginForm" :rules="rules" ref="loginForm">
                <el-form-item label="用户名" prop="loginname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="loginForm.loginname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="输入密码" prop="loginpass" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input type="password" v-model="loginForm.loginpass" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('loginForm')">取 消</el-button>
                <el-button type="primary" @click="doLogin">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="修改资料" :visible.sync="dialogInfoFormVisible" @close="resetInfoForm()">
            <el-form :model="infoForm" :rules="infoRules" ref="infoForm">
                <el-form-item label="昵称" prop="nickname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="infoForm.nickname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="姓名" prop="realname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="infoForm.realname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="新密码" prop="loginpass" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input type="password" v-model="infoForm.loginpass" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkpass" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input type="password" v-model="infoForm.checkpass" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetInfoForm()" size="small">取 消</el-button>
                <el-button type="primary" @click="save" size="small">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Header",
        data() {
            var validatePass = (rule, value, callback) => {
                if (value !== this.infoForm.loginpass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                adminVisible: false,
                userVisible: false,
                dialogFormVisible: false,     //模态框是否显示
                dialogInfoFormVisible: false,
                keyword: "",
                loginForm: {
                    loginname: '',
                    loginpass: ''
                },
                infoForm: {
                    loginname: '',
                    nickname: '',
                    realname: ''
                },
                rules: {
                    loginname: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    loginpass: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ]
                },
                infoRules: {
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
                },
                formLabelWidth: '120px',
            };
        },
        methods: {
            loadRole(role) {
                if (role == "ADMIN") {
                    this.adminVisible = true;
                    this.userVisible = true;
                } else if (role == "USER") {
                    this.userVisible = true;
                } else {
                    this.adminVisible = false;
                    this.userVisible = false;
                }
            },
            editUser() {
                this.dialogInfoFormVisible = true
                this.infoForm.loginname = this.$cookie.get("loginname")
                this.infoForm.nickname = this.$cookie.get("nickname")
                this.infoForm.realname = this.$cookie.get("realname")
            },
            logout() {
                this.$cookie.delete("role")
                this.loadRole("NONE")
                window.location.reload()
            },
            openLogin() {
                this.dialogFormVisible = true
            },
            resetForm(formName) {
                this.dialogFormVisible = false
                this.$refs[formName].resetFields()
            },
            resetInfoForm() {
                this.dialogInfoFormVisible = false
                this.$refs["infoForm"].resetFields()
            },
            save() {
                this.$refs["infoForm"].validate((valid) => {
                    if (valid) {
                        this.$http.post(platform_base + '/platform/user/update', this.infoForm).then((resp) => {
                            return resp.data
                        }).then((d) => {
                            if (d.code == 0) {
                                this.$message({
                                    message: d.msg,
                                    type: 'success'
                                });
                                this.$cookie.set("loginname", d.data.loginname);
                                this.$cookie.set("nickname", d.data.nickname);
                                this.$cookie.set("realname", d.data.realname);
                                this.resetInfoForm()
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
            doLogin() {
                this.$refs["loginForm"].validate((valid) => {
                    if (valid) {
                        this.$http.post(platform_base + '/platform/login/doLogin', this.loginForm).then((resp) => {
                            return resp.data
                        }).then((d) => {
                            if (d.code == 0) {
                                this.$message({
                                    message: d.msg,
                                    type: 'success'
                                });
                                this.resetForm("loginForm");
                                this.loadRole(d.data.role);
                                this.$cookie.set("role", d.data.role);
                                this.$cookie.set("loginname", d.data.loginname);
                                this.$cookie.set("nickname", d.data.nickname);
                                this.$cookie.set("realname", d.data.realname);
                                window.location.reload()
                                //this.$router.replace({path: '/lol'})//跳转到首页
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
            handleSelect(key, keyPath) {
                //console.log(key, keyPath);
            },
            resetIndex() {
                this.$confirm('此操作将重建索引库, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post(platform_base + '/platform/bug/reset', {}).then((resp) => {
                        return resp.data
                    }).then((d) => {
                        if (d.code == 0) {
                            this.$message({
                                message: d.msg,
                                type: 'success'
                            });
                        } else {
                            this.$message({
                                message: d.msg,
                                type: 'error'
                            });
                        }
                    });
                }).catch(() => {
                });
            },
            doSearch() {
                if (this.keyword)
                    window.location.href = "/lol/search/" + this.keyword
            }
        },
        created() {
            var role = this.$cookie.get("role");
            this.loadRole(role);
        }
    }
</script>

<style scoped>
    .top {
        width: 100%;
    }

    .search {
        float: right;
        position: absolute;
        margin-top: -50px;
        margin-right: 100px;
    }

    .buttons {
        float: right;
        position: absolute;
        margin-top: -50px;
    }
</style>