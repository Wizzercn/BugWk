<template>
    <div>
        <el-row v-show="isAdmin" >
            <el-button type="primary" @click="addBug">添加新Bug</el-button>
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
        name: "Lol",
        data() {
            return {
                isAdmin:false,
                tableData: []
            }
        },
        methods: {
            addBug:function(){
                this.$router.push("/lol/add")
            }
        },
        mounted: function () {
            console.log(this.$cookie.get("role"))
            if("ADMIN"==this.$cookie.get("role")){
                this.isAdmin=true
            }
        }
    }
</script>

<style scoped>

</style>