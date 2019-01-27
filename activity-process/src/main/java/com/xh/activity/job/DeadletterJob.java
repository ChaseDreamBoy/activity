package com.xh.activity.job;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 无法 执行的job   -- ACT_RU_DEADLETTER_JOB
public class DeadletterJob {

    private static Logger logger = LoggerFactory.getLogger(DeadletterJob.class);

    public static void main(String[] args) {
        // 创建流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 得到流程存储服务实例
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 管理服务组件
        ManagementService managementService = engine.getManagementService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/job/error_task.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        logger.info("ServiceTask -- this process instance id is : {}", processInstance.getId());
        // 设置重试次数
        Job job = managementService.createJobQuery().singleResult();
        managementService.setJobRetries(job.getId(), 1);
        // 重新执行该工作，抛出异常
        try {
            managementService.executeJob(job.getId());
        } catch (Exception e) {
            e.printStackTrace();

        }
        // 查询无法执行工作表
        long deadCount = managementService.createDeadLetterJobQuery().count();
        logger.info("无法执行的工作，数据量：" + deadCount);
    }

}
