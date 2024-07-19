## 基于Springboot3+Mybatis-plus的动态任务调度执行器
简单的本地小型定时任务调度处理，支持cron、定时、间隔周期、周期定时调度任务的demo。

### 版本-version

|name|version|

|spring-boot-starter-web|3.2.0|
|mybatis-plus-spring-boot3-starter|3.5.7|

### 使用-using
详见TaskController控制层

增加任务并立即调度(Add time task and schedule immediately)
```
request path: /task/add
request body:{
    "className":"org.nott.service.TestService",
    "beanName":"testService", // className和beanName任传其一
    "executeMethod":"test",
    "executeTime":"2024-07-18 15:03:00",
    "executeMode":1,
    "scheduleNow":true
}
```

