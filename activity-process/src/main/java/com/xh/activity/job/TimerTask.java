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

// 定时事件
public class TimerTask {

    private static Logger logger = LoggerFactory.getLogger(ServiceTask.class);

    public static void main(String[] args) {
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        logger.info("TimerTask -- get process engine info : " + engine.getName());
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
//        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/job/timer_task.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        logger.info("TimerTask -- this process instance id is : {}", processInstance.getId());

        /**
         * 流程到达这里，数据库定时工作表：ACT_RU_TIME_JOB  -- 定时事件
         * 中就数据
         * 一分钟后数据就消失了
         *
         *
         * 定时任务
         * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
         * <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:tns="http://www.activiti.org/test" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" expressionLanguage="http://www.w3.org/1999/XPath" id="m1548563577425" name="" targetNamespace="http://www.activiti.org/test" typeLanguage="http://www.w3.org/2001/XMLSchema">
         *   <process id="myProcess_1" isClosed="false" isExecutable="true" processType="None">
         *     <startEvent id="_2" name="StartEvent"/>
         *     <endEvent id="_3" name="EndEvent"/>
         *     <userTask activiti:exclusive="true" id="_4" name="UserTask"/>
         *     <intermediateCatchEvent id="_5" name="IntermediateCatchingEvent">
         *       <timerEventDefinition id="_5_ED_1">
         *         <timeDuration><![CDATA[PT1M]]></timeDuration>
         *       </timerEventDefinition>
         *     </intermediateCatchEvent>
         *     <sequenceFlow id="_6" sourceRef="_2" targetRef="_5"/>
         *     <sequenceFlow id="_7" sourceRef="_5" targetRef="_4"/>
         *
         * PT1M --> 一分钟后流程节点 就过了
         *
         * 还需要在 activiti.cfg.xml 中配置 异步执行器
         *      <!-- 启动异步执行器 -->
         *      <property name="asyncExecutorActivate" value="true" />
         */

        engine.close();
        // 退出
        System.exit(0);
    }

}
