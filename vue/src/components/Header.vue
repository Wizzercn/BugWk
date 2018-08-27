<template>
    <div>
        <el-menu ref="menu" :default-active="$route.name" :router="true" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="lol" :route="{name:'lol'}">问题中心</el-menu-item>
            <el-submenu index="2" v-show="adminVisible">
                <template slot="title">系统管理</template>
                <el-menu-item index="user" :route="{name:'user'}">
                    用户管理
                </el-menu-item>
                <el-menu-item index="tag">标签管理</el-menu-item>
                <el-menu-item index="project">项目管理</el-menu-item>
            </el-submenu>
            <el-submenu index="3" v-show="userVisible">
                <template slot="title">用户中心</template>
                <el-menu-item index="#">
                    <a @click="editUser">修改资料</a>
                </el-menu-item>
                <el-menu-item index="#">
                    <a @click="logout">退出登录</a>
                </el-menu-item>
            </el-submenu>
            <el-menu-item index="#" v-show="!userVisible">
                <el-button @click="openLogin">登录</el-button>
            </el-menu-item>
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
    </div>
</template>

<script>
    export default {
        name: "Header",
        data() {
            return {
                adminVisible: false,
                userVisible: false,
                dialogFormVisible: false,     //模态框是否显示
                loginForm: {
                    loginname: '',
                    loginpass: ''
                },
                rules: {
                    loginname: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    loginpass: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ]
                },
                formLabelWidth: '120px',
            };
        },
        methods: {
            loadRole(role) {
                if (role == "ADMIN") {
                    this.adminVisible = true;
                    this.userVisible = true;
                }else if (role == "USER") {
                    this.userVisible = true;
                }else {
                    this.adminVisible = false;
                    this.userVisible = false;
                }
            },
            editUser(){

            },
            logout(){
                this.$cookie.delete("role");
                this.loadRole("NONE");
                this.$router.push("/lol")
            },
            openLogin() {
                this.dialogFormVisible = true
            },
            resetForm(formName) {
                this.dialogFormVisible = false;
                this.$refs[formName].resetFields();
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
                                this.$cookie.set("role",d.data.role);
                                this.$cookie.set("loginname",d.data.loginname);
                                this.$cookie.set("nickname",d.data.nickname);
                                this.$router.push("/user")
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
            }
        },
        created() {
            var role=this.$cookie.get("role");
            this.loadRole(role);
        }
    }
</script>

<style scoped>

</style>