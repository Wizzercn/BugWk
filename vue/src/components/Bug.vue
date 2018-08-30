<template>
    <div>
        <el-row>
            <span :class="'level lv-'+bug.level">&nbsp;</span><span class="title">{{bug.title}}</span>
        </el-row>
        <el-row class="before_d">
            <div class="changes">
                <span>
                  发布于 {{bug.updateAtStr}}
                </span>
                <span>
                  Buger: {{bug.nickname}}<span v-show="isUser">[{{bug.user.realname}}]</span>
                </span>
            </div>
        </el-row>
        <el-row class="before_d">
            <div class="changes">
                标签: <span class="tag" v-for="n in bug.tag">{{n}}</span>
            </div>
        </el-row>
        <el-row class="before_d">
            <div class="changes">
                严重度: {{bug.level}}
            </div>
        </el-row>
        <el-row>
            <div class='inner topic'>

                <div class='topic_content' itemprop="articleBody">
                    <div class="markdown-text" v-html="bug.note">
                    </div>
                </div>
            </div>
        </el-row>
        <el-row>
            <div class="panel">
                <div class="header">
                    <span class="col_fade">{{bug.replies.length}} 回复</span>
                </div>
                <div class="cell reply_area reply_item" v-for="(reply,index) in bug.replies">
                    <div class="author_content">
                        <div class="user_info">
                            <a class="dark reply_author">{{reply.nickname}}</a>
                            <a class="reply_time" >{{index+1}}楼•{{reply.createAtStr}}</a>
                        </div>
                        <!-- user_action -->
                    </div>
                    <!-- reply_content -->
                    <div class="reply_content from-wendal">
                        <div class="markdown-text" v-html="reply.noteHtml">
                        </div>
                    </div>
                </div>
            </div>
        </el-row>
        <el-row v-show="isUser">
            <div class='panel' data-intro="回帖回帖回帖!!" data-position="top" id="replyAdd">
                <div class='header'>
                    <span class='col_fade'>添加回复</span>
                </div>
                <div class='inner reply'>
                    <form action='#'>
                       <textarea v-model="replyForm.note" id="topicContent" class='editor' name='content' rows='50'
                                 placeholder='使用Markdown语法,下方有粘贴代码和日志的按钮,会有代码高亮'></textarea>
                    </form>

                    <el-button type="primary" @click="topicSubmit">回复</el-button>
                    <el-button type="info" @click="topicAddCode">插入代码或日志片段</el-button>
                </div>
            </div>
            <el-dialog title="贴代码或日志" :visible.sync="addFormVisible" @close="resetForm">
                <el-form :model="replyForm" ref="replyForm">
                    <el-row>
                        <textarea id="tempNote" v-model="tempNote" class='editor' name='content' rows='20'></textarea>
                    </el-row>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="resetForm">取 消</el-button>
                    <el-button type="primary" @click="addIt">确 定</el-button>
                </div>
            </el-dialog>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "Bug",
        data() {
            return {
                isUser: false,
                addFormVisible:false,
                tempNote:"",
                replyForm:{
                    note:"",
                    bugId:""
                },
                bug: {
                    id: "",
                    title: "",
                    user: {
                        id: "",
                        realname: ""
                    },
                    replies:[

                    ]
                }
            }
        },
        methods: {
            resetForm: function () {
                this.addFormVisible = false;
                this.$refs["replyForm"].resetFields();
            },
            topicSubmit: function () {
                this.replyForm.bugId=this.$route.params.id
                this.replyForm.note = this.replyForm.note.trim();
                if (this.replyForm.note.length < 10) {
                    this.$message({
                        message: '内容起码10个字',
                        type: 'warning'
                    });
                    return;
                }
                this.$http.post(platform_base + '/platform/bug/reply', this.replyForm).then((resp) => {
                    return resp.data
                }).then((d) => {
                    if (d.code == 0) {
                        this.$message({
                            message: d.msg,
                            type: 'success'
                        });
                        setTimeout(function () {
                            window.location.reload()
                        },333)
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
                this.replyForm.note += "\r\n```\r\n" + this.tempNote + "\r\n```\r\n"
                this.tempNote = ""
                this.resetForm()
            },
        },
        mounted: function () {
            if ("ADMIN" == this.$cookie.get("role") || "USER" == this.$cookie.get("role")) {
                this.isUser = true
            }
            this.$http.post(platform_base + '/platform/bug/s', {
                "id": this.$route.params.id
            }, {
                withCredentials: true
            }).then((resp) => {
                return resp.data
            }).then((d) => {
                if (d.code == 0) {
                    this.bug = d.data;
                } else {
                    this.$message({
                        message: d.msg,
                        type: 'error'
                    });
                }
            });
        }
    }
</script>

<style scoped>
    .title {
        font-size: 22px;
        font-weight: bold;
        margin: 8px 0;
        display: inline-block;
        vertical-align: bottom;
        width: 75%;
        line-height: 130%;
    }

    .changes {
        font-size: 12px;
        color: #838383;
        margin-top: 5px;
    }

    .tag {
        background-color: lightsteelblue;
        margin-left: 10px;
        padding: 2px;
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
    .level {
        color: white;
        width: 10px;
        padding: 3px;
        margin-left: 2px;
        margin-right: 2px;
        line-height: 2.5rem;
        -moz-border-radius: 50px;
        -webkit-border-radius: 50px;
        border-radius: 50px;
    }

    .lv-0 {
        background-color: #66FF99;
    }

    .lv-10 {
        background-color: #CCFF99;
    }

    .lv-20 {
        background-color: #FFFF99;
    }

    .lv-30 {
        background-color: #FFFF33;
    }

    .lv-40 {
        background-color: #FFCC33;
    }

    .lv-50 {
        background-color: #FF9400;
    }

    .lv-60 {
        background-color: #FF66CC;
    }

    .lv-70 {
        background-color: #CC3399;
    }

    .lv-80 {
        background-color: #FF3333;
    }

    .lv-90 {
        background-color: #CC0000;
    }

    .lv-100 {
        background-color: #660000;
    }

    /deep/ pre {
        background: #fee9cc;
        border: 1px dashed #ccc;
        line-height: 22px;
    }

    /deep/ code {
        padding: 0;
        border: none;
    }

    /deep/ p code {
        background: none;
        color: hsl(0, 0%, 50%);
        margin: 0 1px;
        padding: 1px 4px;
        border-radius: 1px;
    }

    /deep/ div pre.prettyprint {
        font-size: 14px;
        border-radius: 0px;
        padding: 0 15px;
        border: none;
        border-width: 1px 0px;
        background: #f7f7f7;
    }

    /deep/ form {
        margin-bottom: 0;
    }

    /deep/ textarea {
        margin-bottom: 0;
    }

    /deep/ input, textarea {
        background: hsla(0, 0%, 0%, 0);
    }

    /deep/ .panel .inner {
        line-height: 2em;
        padding: 10px;
        background-color: white;
        border-radius: 0 0 3px 3px;
    }

    .before_d div:before {
        content: "•";
    }

    .panel {
        margin-bottom: 13px;
    }

    .breadcrumb > li {
        text-shadow: none;
    }

    .breadcrumb a {
        color: #80bd01;
    }

    .panel .header.topic_header {
        background-color: white;
    }

    .panel .header {
        padding: 10px 10px;
        background-color: #f6f6f6;
        border-radius: 3px 3px 0 0;
    }

    .panel .header.topic_header {
        background-color: white;
    }

    .panel .moon {
        padding: 10px;
    }

    .panel .moon_tags {
        padding-left: 10px;
    }

    .panel .inner .unstyled li div {
        text-overflow: ellipsis;
    }

    .panel .inner.topic,
    .panel .inner.reply,
    .panel .inner.userinfo,
    .panel .inner.post {
        padding: 10px;
        border-top: 1px solid #e5e5e5;
    }

    .panel .inner {
        line-height: 2em;
        padding: 10px;
        background-color: white;
        border-radius: 0 0 3px 3px;
    }

    .panel .inner a {
        color: #778087;
    }

    .panel .inner > form.form-horizontal {
        margin-top: 40px;
    }

    .panel .inner.no-padding {
        padding: 0;
    }

    .panel .inner li {
        line-height: 2em;
    }

    .panel > .cell:last-child {
        box-shadow: none;
    }

    .cell {
        overflow: hidden;
        position: relative;
        padding: 10px 0px 10px 10px;
        font-size: 14px;
    }

    .panel .cell {
        padding-right: 10px;
        background: white;
        border-top: 1px solid #f0f0f0;
    }

    .panel .cell:nth-child(1) {
        border-top: none;
    }

    .cell .reply_count {
        width: 70px;
        display: inline-block;
        text-align: center;
    }

    .cell .count_of_replies {
        color: #9e78c0;
    }

    .cell .count_seperator {
        margin: 0 -3px;
        font-size: 10px;
    }

    .cell .count_of_visits {
        font-size: 10px;
        color: #b4b4b4;
    }

    .cell .cell {
        padding: 10px 0px 0px 10px;
    }

    .cell:last-child {
        border-bottom: none;
    }

    .cell.message,
    .cell[message_id] {
        padding: 10px;
    }

    .cell.message a,
    .cell[message_id] a {
        max-width: 460px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: inline-block;
        vertical-align: middle;
    }

    .cell.more {
        padding: 10px;
    }
    a.dark:link, a.dark:visited, a.dark:active {
        color: #666;
        text-decoration: none;
    }
    a.dark {
        overflow: hidden;
        text-overflow: ellipsis;
        text-decoration: none;
        color: #666;
    }
    .reply_time {
        font-size: 11px;
    }
    a {
        color: #0088cc;
        text-decoration: none;
    }
    .reply_author {
        font-size: 12px;
        font-weight: bold;
        margin-right: 5px;
    }
</style>