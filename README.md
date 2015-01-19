## JobMonage
使用spring4+quartz2实现动态任务调用

###主要功能

* 暂停
* 恢复
* 删除
* 修改执行时间
* 立即运行一次

###trigger各状态说明

* `None`：Trigger已经完成，且不会在执行，或者找不到该触发器，或者Trigger已经被删除
* `NORMAL`:正常状态
* `PAUSED`：暂停状态
* `COMPLETE`：触发器完成，但是任务可能还正在执行中
* `BLOCKED`：线程阻塞状态
* `ERROR`：出现错误
