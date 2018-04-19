# 教学教务管理平台v3.0_9_19 变更列表

## 2016-10-20 
>> @micarol
1. 增加考试预约展示列表

>> @hyf
1. 管理平台论文教师增删改查
2. 管理平台学员学籍查看、审核
3. 班主任平台学员学籍录入、提交资料

>> @lizhijian 
1. 修改上传控件 
2. 新增为学员个人分配、修改指导老师功能和修改答辩方式功能

## 2017-08-01 八一搞了点什么事情呢？
>> @hyf
1. 因为之前两套平台要写两套代码，并且还是重复的代码，为了提高工作效率，班主任项目重构了一遍；
2. JPA配置多数据源，引入了管理后台的entity、service、biz模块；
3. 由于组件在Spring容器中会冲突，班主任的组件name全部加了前缀bzr；
4. 以后写代码都统一往后台写，尽量别写在班主任上，有时间的可以将班主任的方法慢慢往后台挪；
5. 为了区分后台和班主任的entity、service、biz，在班主任中这些模块的类全都标记为过时类@Deprecated。

## 2017-08-10 独立打包各web模块
>> @zhoujianming
1. 管理后台：mvn clean install -f pom-com.gzedu.xlims.web.xml -Pdevelop
2. 班主任平台：mvn clean install -f pom-com.ouchgzee.headTeacher.web.xml -Pdevelop
3. 学习空间：mvn clean install -f pom-com.study.ouchgzee.web.xml -Pdevelop

## 2017-10-16 新增release_20171016分枝
>> @zhoujianming
1. 新增release_20171016分枝，不允许直接提交，需要发起合并申请  

## 2018-01-12 优化日志
>> @hyf
1. 优化日志切面
2. 学习空间接口可以使用@SysLog注解记录日志
