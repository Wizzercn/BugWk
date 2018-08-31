<template>
    <div>
        <el-row>
            <div class="block">
                <span class="demonstration">标题</span>
                <el-input type="text" v-model="bugForm.title" auto-complete="off"></el-input>
            </div>
        </el-row>
        <el-row>
            <div class="block">
                <span class="demonstration">标签</span>
                <el-select
                        class="span_n"
                        v-model="bugForm.tag"
                        multiple
                        filterable
                        allow-create
                        default-first-option
                        remote
                        reserve-keyword
                        :remote-method="remoteMethod"
                        :loading="loading"
                        placeholder="请选择标签">
                    <el-option
                            v-for="item in tags"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </el-row>
        <el-row>
            <div class="block">
                <span class="demonstration">作者</span>
                <el-select
                        class="span_n"
                        v-model="bugForm.userId"
                        filterable
                        default-first-option
                        placeholder="请选择用户">
                    <el-option
                            v-for="item in users"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </div>
        </el-row>
        <el-row>
            <div class="block">
                <span class="demonstration">Bug严重度</span>
                <el-slider
                        v-model="bugForm.lv"
                        :step="10"
                        show-stops>
                </el-slider>
            </div>
        </el-row>
        <el-row>
            <textarea id="topicContent" v-model="bugForm.note" class='editor' name='content' rows='50'
                      placeholder='使用Markdown语法,下方有粘贴代码和日志的按钮,会有代码高亮'></textarea>
        </el-row>
        <el-row>
            <el-button type="primary" @click="topicSubmit">{{topicButtonTip}}</el-button>
            <el-button type="info" @click="topicAddCode">插入代码或日志片段</el-button>
        </el-row>
        <el-dialog title="贴代码或日志" :visible.sync="addFormVisible" @close="resetForm">
            <el-form :model="bugForm" ref="bugForm">
                <el-row>
                    <textarea id="tempNote" v-model="tempNote" class='editor' name='content' rows='20'></textarea>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="resetForm">取 消</el-button>
                <el-button type="primary" @click="addIt">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Add",
        data() {
            return {
                isAdmin: false,
                addFormVisible: false,
                loading: false,
                tableData: [],
                bugForm: {
                    title: "",
                    note: "",
                    tag: "",
                    lv: 0,
                    loginname:"",
                    userId:""
                },
                tags: [],
                users: [],
                tempNote: "",
                topicButtonTip: "提交"
            }
        },
        methods: {
            goBack: function () {
                this.$router.push("/lol")
            },
            resetForm: function () {
                this.addFormVisible = false;
                this.$refs["bugForm"].resetFields();
            },
            topicSubmit: function () {
                this.bugForm.loginname=this.$cookie.get("loginname")
                this.bugForm.title = this.bugForm.title.trim();
                if (this.bugForm.title.length < 5) {
                    this.$message({
                        message: '标题起码5个字',
                        type: 'warning'
                    });
                    return;
                }
                if (this.bugForm.title.length > 100) {
                    this.$message({
                        message: '标题最多100个字符',
                        type: 'warning'
                    });
                    return;
                }
                this.bugForm.title = this.bugForm.title.replace(" 【", "[").replace("】", "]");
                this.bugForm.note = this.bugForm.note.trim();
                if (this.bugForm.note.length < 10) {
                    this.$message({
                        message: '内容起码10个字',
                        type: 'warning'
                    });
                    return;
                }
                this.topicButtonTip = "正在提交...";
                this.$http.post(platform_base + '/platform/bug/add', this.bugForm).then((resp) => {
                    return resp.data
                }).then((d) => {
                    if (d.code == 0) {
                        this.$message({
                            message: d.msg,
                            type: 'success'
                        });
                        this.$router.push("/lol")
                    } else {
                        this.$message({
                            message: d.msg,
                            type: 'error'
                        });
                    }
                });
            },
            topicAddCode: function () {
                this.addFormVisible = true
            },
            addIt: function () {
                this.bugForm.note += "\r\n```\r\n" + this.tempNote + "\r\n```\r\n"
                this.tempNote = ""
                this.resetForm()
            },
            remoteMethod(query) {
                if (query !== '') {
                    this.loading = true;
                    this.$http.post(platform_base + '/platform/bug/tag', {name: query}).then((resp) => {
                        return resp.data
                    }).then((d) => {
                        if (d.code == 0) {
                            this.tags = d.data.map(item => {
                                return {value: item.name, label: item.name};
                            });
                        }
                        this.loading = false;
                    });
                } else {
                    this.tags = [];
                }
            }
        },
        mounted: function () {
            if ("ADMIN" == this.$cookie.get("role")) {
                this.isAdmin = true
                this.$http.post(platform_base + '/platform/bug/user', {name: ""}).then((resp) => {
                    return resp.data
                }).then((d) => {
                    if (d.code == 0) {
                        this.users = d.data.map(item => {
                            return {value: item.id, label: item.realname};
                        });
                    }
                });
            }
            this.$http.post(platform_base + '/platform/bug/tag', {name: ""}).then((resp) => {
                return resp.data
            }).then((d) => {
                if (d.code == 0) {
                    this.tags = d.data.map(item => {
                        return {value: item.name, label: item.name};
                    });
                }
            });
        }
    }
</script>

<style scoped>
    .demonstration {
        margin-top: 5px;
    }
    .span_n{
        width: 100% !important;
    }
    textarea.editor {
        width: 98%;
        margin-top: 5px;
        line-height: 2em;
        height: 220px;
        resize: vertical;
        font-size: 15px;
        padding: 0.5em;
        border: 1px solid #e1e4e8;
    }
</style>