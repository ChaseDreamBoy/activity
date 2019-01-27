package com.xh.activity.job;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 暂停的工作
public class SuspendTask {

    private static Logger logger = LoggerFactory.getLogger(SuspendTask.class);

    public static void main(String[] args) throws InterruptedException {
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        logger.info("SuspendTask -- get process engine info : " + engine.getName());
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
//        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
        logger.info("");
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/job/timer_task.bpmn").deploy();
        logger.info("");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        logger.info("SuspendTask -- this process instance id is : {}", processInstance.getId());

        Thread.sleep(10 * 1000);
        // 当前 在 ACT_RU_TIME_JOB  表中有值

        /**
         * 结束流程
         *
         * 结束流程后 该流程就变为  暂停的工作
         *
         * 在数据库 暂时工作表：ACT_RU_SUSPENDED_JOB  -- 暂停的工作
         * 中就可以看到值了
         */
        runtimeService.suspendProcessInstanceById(processInstance.getId());

        Thread.sleep(10 * 1000);

        /**
         * 重新启动流程
         *
         * 重新启动流程后
         * ACT_RU_SUSPENDED_JOB 表中数据就没有了
         * ACT_RU_TIME_JOB 表中就有数据了
         */
        runtimeService.activateProcessInstanceById(processInstance.getId());

        engine.close();
        // 退出
        System.exit(0);
    }

}
