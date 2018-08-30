<template>
    <div>
        <el-row>
           <span :class="'level lv-'+bug.level">&nbsp;</span><span class="title">{{bug.title}}</span>
        </el-row>
        <el-row>
            <div class="changes">
                <span>
                  发布于 {{bug.updateAtStr}}
                </span>
                <span>
                  Buger: {{bug.nickname}}<span v-show="isUser">[{{bug.user.realname}}]</span>
                </span>
            </div>
        </el-row>
        <el-row>
            <div class="changes">
                标签: <span class="tag" v-for="n in bug.tag">{{n}}</span>
            </div>
        </el-row>
        <el-row>
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
    </div>
</template>

<script>
    export default {
        name: "Bug",
        data() {
            return {
                isUser:false,
                bug: {
                    id:"",
                    title:"",
                    user:{
                        id:"",
                        realname:""
                    }
                }
            }
        },
        mounted: function () {
            if("ADMIN"==this.$cookie.get("role")||"USER"==this.$cookie.get("role")){
                this.isUser=true
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
    .title{
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
    .tag{
        background-color: lightsteelblue;
        margin-left: 10px;
        padding: 2px;
    }
    .level{
        color: white;
        width: 10px;
        padding: 3px;
        margin-left: 2px;
        line-height: 2.5rem;
        -moz-border-radius: 50px;
        -webkit-border-radius: 50px;
        border-radius: 50px;
    }
    .lv-0{
        background-color: #66FF99;
    }
    .lv-10{
        background-color: #CCFF99;
    }
    .lv-20{
        background-color: #FFFF99;
    }
    .lv-30{
        background-color: #FFFF33;
    }
    .lv-40{
        background-color: #FFCC33;
    }
    .lv-50{
        background-color: #FF9400;
    }
    .lv-60{
        background-color: #FF66CC;
    }
    .lv-70{
        background-color: #CC3399;
    }
    .lv-80{
        background-color: #FF3333;
    }
    .lv-90{
        background-color: #CC0000;
    }
    .lv-100{
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
        margin: 20px -10px;
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
</style>