package com.xh.activity.job;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// service task (bpmn中) 需要配置这个类
// service task 中的 activiti:class="com.xh.activity.job.MyJavaDelegate"
// 异步执行需要  activiti:async="true"
public class MyJavaDelegate implements JavaDelegate {

    private Logger logger = LoggerFactory.getLogger(MyJavaDelegate.class);

    public void execute(DelegateExecution delegateExecution) {
        logger.info("MyJavaDelegate -- 这是处理类 -- execute name : {}", delegateExecution.getEventName());
    }
}
