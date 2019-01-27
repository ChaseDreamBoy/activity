package com.xh.activity.process;

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

public class ExecuteProcess {

    private static Logger logger = LoggerFactory.getLogger(ExecuteProcess.class);

    public static void main(String[] args) {
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        logger.info("ExecuteProcess -- get process engine info : " + engine.getName());
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
//        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
//        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/simple.bpmn").deploy();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/receive_task.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        logger.info("ExecuteProcess -- this process instance id is : {}", processInstance.getId());

        // 获取子执行流
        List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().list();
        for (Execution execution : executionList){
            logger.info("current activity id : {}", execution.getActivityId());
        }

        // 执行 流程
        for (Execution execution : executionList){
            // taskService.complete(task.getId());
            // 流程task 是receive task 的时候，执行流程用trigger
            runtimeService.trigger(execution.getId());
        }

        logger.info("execute process finish...");

        // 执行流程后 再次查看 activity id
        executionList = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().list();
        for (Execution execution : executionList){
            logger.info("current activity id : {}", execution.getActivityId());
        }

        engine.close();
        // 退出
        System.exit(0);
    }
}
