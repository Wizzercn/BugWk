<template>
    <div>
        <el-row >
            <el-button type="primary" @click="goBack">返回</el-button>
        </el-row>
        <el-table
                :data="tableData"
                style="width: 100%">
            <el-table-column
                    prop="date"
                    label="日期"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="姓名"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="address"
                    label="地址">
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    export default {
        name: "Add",
        data() {
            return {
                isAdmin:false,
                tableData: []
            }
        },
        methods: {
            goBack:function(){
                this.$router.push("/lol")
            }
        },
        mounted: function () {
            if("ADMIN"==this.$cookie.get("role")){
                this.isAdmin=true
            }
        }
    }
</script>

<style scoped>

</style>