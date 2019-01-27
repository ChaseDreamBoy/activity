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

// 异步任务
public class ServiceTask {

    private static Logger logger = LoggerFactory.getLogger(ServiceTask.class);

    public static void main(String[] args) {
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        logger.info("ServiceTask -- get process engine info : " + engine.getName());
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
//        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/job/service_task.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        logger.info("ServiceTask -- this process instance id is : {}", processInstance.getId());

        /**
         * 这里启动流程后第一个节点是service task任务
         * 该任务设置了异步
         * 所以在  一般任务表：ACT_RU_JOB  中可以看到该数据
         *
         * 注意 service_task 中不要有多余的标签
         *
         * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
         * <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:tns="http://www.activiti.org/test" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" expressionLanguage="http://www.w3.org/1999/XPath" id="m1548561864745" name="" targetNamespace="http://www.activiti.org/test" typeLanguage="http://www.w3.org/2001/XMLSchema">
         *   <process id="myProcess_1" isClosed="false" isExecutable="true" processType="None">
         *     <startEvent id="_2" name="StartEvent"/>
         *     <endEvent id="_3" name="EndEvent"/>
         *     <userTask activiti:exclusive="true" id="user_task_4" name="UserTask"/>
         *     <serviceTask activiti:async="true" activiti:class="com.xh.activity.job.MyJavaDelegate" id="service_task_5" name="ServiceTask">
         *
         *     </serviceTask>
         *     <sequenceFlow id="_6" sourceRef="_2" targetRef="service_task_5"/>
         *     <sequenceFlow id="_7" sourceRef="service_task_5" targetRef="user_task_4"/>
         *
         * 还需要在 activiti.cfg.xml 中配置 异步执行器
         * <!-- 启动异步执行器 -->
         * 		<property name="asyncExecutorActivate" value="true" />
         */

        engine.close();
        // 退出
        System.exit(0);
    }

}
