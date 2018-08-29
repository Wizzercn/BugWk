<template>
    <div>
        <el-row v-show="isAdmin" >
            <el-button type="primary" @click="addBug">添加新Bug</el-button>
        </el-row>
        <el-table
                :data="tableData"
                style="width: 100%">
            <el-table-column
                    prop="nickname"
                    label="Buger"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="title"
                    label="标题">
                <template scope="scope">
                    <a class="bug_title" :href="'/lol/bug/'+scope.row.id" target="_blank">{{scope.row.title}}</a>
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
                isAdmin:false,
                tableData: [],
                pagesize: 10,
                currentPage: 1,
                start: 1,
                totalCount: 0
            }
        },
        methods: {
            addBug:function(){
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
            if("ADMIN"==this.$cookie.get("role")){
                this.isAdmin=true
            }
            this.pageData(this.currentPage, this.pagesize);
        }
    }
</script>

<style scoped>
    .bug_title{
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
    a:hover{
        cursor: pointer;
        text-decoration: underline;
    }
</style>