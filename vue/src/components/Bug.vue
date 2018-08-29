<template>
    <div>
        <el-row>
            <span class="title">{{bug.title}}</span>
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
            <div class='inner topic'>

                <div class='topic_content' itemprop="articleBody">
                    <div class="markdown-text">
                        {{bug.note}}
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
                bug: {}
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
</style>