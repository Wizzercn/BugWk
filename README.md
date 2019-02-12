# BugWk
一个基于Bug的分析学习及考核系统

### 项目介绍
#### 主要目的
* 开发一套用于公司内部项目质量把控、辅助开发人员能力提升以及考核的系统
* 为 NutzWk 6.0（微服务+API网关+前后端分离）做前期的预研及技术栈评估
#### 技术体系
* 前端：Vue + Element
* 后端：NutzBoot + Lucene + Redis + MySQL
#### 使用说明
* vue启动命令 `npx vue-cli-service serve` 打包命令 `npx vue-cli-service build`
* vue编译完成后，index.html 中 platform_base 改为空字符串，然后拷贝到 `java/resources/static` 目录下覆盖
* java maven 打包命令 `mvn package nutzboot:shade` 然后通过jar包启动 `java -jar bugwk-1.0.jar `
* 启动时自动建表，登陆帐号 superadmin  密码 1 

#### 开发计划
##### 一期
* Bug及解决方法的录入功能（参考答案及外部链接，是否重复发生）
* Bug评论及回复功能（需登录）
* Bug的标签及人员关联功能（非登录状态只可查看昵称，登录后可查看真实姓名）
* Bug的严重程度标签
* 搜索引擎是必须的，缓存看情况再说
* 周、月、年的Bug数量及严重度、重复率统计（也可按项目统计）
##### 二期
* IP黑白名单功能
* 支持分公司及分部门功能（做成云平台？）
* 皇冠功能（月度大神、月度挖坑小王子？）
* 打赏功能（Bug作者需认领并打赏，作为`月度大神`或其他活动的赏金，Bug严重度越高打赏越高）
* 以上功能纯属设想，能不能实现看未来...

