package com.xh.activity.process;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleProcess {

    private static Logger logger = LoggerFactory.getLogger(SimpleProcess.class);

    public static void main(String[] args) {
        // 创建流程引擎
//        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 配置文件 和bean 名称
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
//        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
//        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/simple.bpmn").deploy();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/multi.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        // 启动流程  key是 xml中process 的 id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());

        /**
         * 到达 这一步  可以在数据库表 : ACT_RU_EXECUTION 中看见数据
         * 该表中有一条  主执行流
         * 有多少个分支就有多少 子执行流
         */
        logger.info("this process instance id is : {}", processInstance.getId());

        engine.close();
        // 退出
        System.exit(0);
    }

}
