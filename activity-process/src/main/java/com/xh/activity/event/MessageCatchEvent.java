package com.xh.activity.event;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// 消息 中间 捕获事件

/**
 * 消息  和 信号
 * 消息要发给特定的执行流 才执行  -- 订阅模式
 * 信号一经发送  所有执行流都执行  -- 广播模式
 */
public class MessageCatchEvent {

    private static Logger logger = LoggerFactory.getLogger(MessageCatchEvent.class);

    public static void main(String[] args) {
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        logger.info("MessageCatchEvent -- get process engine info : " + engine.getName());
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
//        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
//        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/simple.bpmn").deploy();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/message_catch_event.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        logger.info("MessageCatchEvent -- this process instance id is : {}", processInstance.getId());

        // 获取子执行流
        List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().list();
        for (Execution execution : executionList) {
            logger.info(" current activity id : {}", execution.getActivityId());
        }

        // 执行流程
        // <signal id="testSignal" name="testSignal"></signal>
        runtimeService.messageEventReceived("testSignal",executionList.get(0).getId());


        logger.info("执行流程 messageEventReceived() -> testSignal");

        executionList = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().list();
        for (Execution execution : executionList) {
            logger.info("MessageCatchEvent -- current activity id : {}", execution.getActivityId());
        }

        engine.close();
        // 退出
        System.exit(0);
    }
}
