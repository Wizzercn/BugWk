<template>
    <div>
        <el-row v-show="isAdmin">
            <el-button type="primary" @click="addBug" size="small">添加新Bug</el-button>
        </el-row>
        <el-table
                :data="tableData"
                style="width: 100%" size="small">
            <el-table-column
                    prop="nickname"
                    label="Buger"
                    width="110">
            </el-table-column>
            <el-table-column
                    prop="title"
                    label="标题">
                <template scope="scope">
                    <span :class="'level lv-'+scope.row.level">&nbsp;</span> <a class="bug_title"
                                                                                :href="'/lol/bug/'+scope.row.id"
                                                                                >{{scope.row.title}}</a>
                </template>
            </el-table-column>
            <el-table-column
                    prop="updateAtStr"
                    label="录入时间"
                    width="180">
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
    </div>
</template>

<script>
    export default {
        name: "Lol",
        data() {
            return {
                isAdmin: false,
                tableData: [],
                pagesize: 10,
                currentPage: 1,
                start: 1,
                totalCount: 0
            }
        },
        methods: {
            addBug: function () {
                this.$router.push("/lol/add")
            },
            handleCurrentChange: function (val) {
                this.currentPage = val;
                this.pageData(this.currentPage, this.pagesize);
            },
            pageData(page, size) {
                this.$http.post(platform_base + '/platform/bug/data', {
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
            if ("ADMIN" == this.$cookie.get("role")) {
                this.isAdmin = true
            }
            this.pageData(this.currentPage, this.pagesize);
        }
    }
</script>

<style scoped>
    .bug_title {
        color: #778087;
        max-width: 70%;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
        vertical-align: bottom;
        font-size: 16px;
        line-height: 30px;
    }

    a {
        text-decoration-line: none;
    }

    /deep/ a:hover {
        cursor: pointer;
        text-decoration: underline;
    }

    .level {
        color: white;
        width: 10px;
        padding: 3px;
        margin-left: 2px;
        margin-right: 2px;
        line-height: 2rem;
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
</style>