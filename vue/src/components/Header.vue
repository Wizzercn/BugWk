<template>
    <div>
        <el-menu :default-active="activeIndex" router="true" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="lol">问题中心</el-menu-item>
            <el-submenu index="2" v-show="roleVisible">
                <template slot="title">系统管理</template>
                <el-menu-item index="user">
                    用户管理
                </el-menu-item>
                <el-menu-item index="2-2">标签管理</el-menu-item>
                <el-menu-item index="2-3">项目管理</el-menu-item>
            </el-submenu>
            <el-menu-item index="#" v-show="!roleVisible">
                <el-button @click="openLogin">登录</el-button>
            </el-menu-item>
        </el-menu>
        <div class="line"></div>
        <el-dialog title="用户登录" :visible.sync="dialogFormVisible" @close="resetForm('loginForm')">
            <el-form :model="loginForm" :rules="rules" ref="loginForm">
                <el-form-item label="用户名" prop="loginname" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input v-model="form.loginname" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="输入密码" prop="loginpass" :label-width="formLabelWidth">
                    <el-col :span="20">
                        <el-input type="password" v-model="form.loginpass" auto-complete="off"></el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm('loginForm')">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Header",
        data() {
            return {
                roleVisible: false,
                dialogFormVisible: false,     //模态框是否显示
                form: {
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
                activeIndex: '1',
            };
        },
        methods: {
            loadRole(role) {
                if (role == "ADMIN") {
                    this.roleVisible = true;
                }
            },
            openLogin() {
                this.dialogFormVisible = true
            },
            resetForm(formName) {
                this.dialogFormVisible = false;
                this.$refs[formName].resetFields();
            },
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            }
        }
    }
</script>

<style scoped>

</style>