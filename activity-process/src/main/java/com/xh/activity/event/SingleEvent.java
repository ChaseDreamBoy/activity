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

/**
 * 流程 : 开始 结束 中间 事件
 * 信号  消息
 *
 * single event
 * 信号捕获事件
 * catching
 * 流程到了捕获事件后，流程就会停在这里，等待外部向他发信号，就一直处于捕获状态，当外界向它发送信号，她才往下流转。
 * <p>
 * 抛出事件
 * throwing
 * 流程到了抛出事件后，流程不会等待，直接抛出，对外抛出信息。
 * <p>
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" expressionLanguage="http://www.w3.org/1999/XPath" id="m1548518894007" name="" targetNamespace="http://www.activiti.org/testm1548518894007" typeLanguage="http://www.w3.org/2001/XMLSchema">
 * <signal id="testSignal" name="testSignal"></signal>
 * <process id="myProcess_1" isClosed="false" isExecutable="true" processType="None">
 * <startEvent id="_2" name="StartEvent"/>
 * <intermediateCatchEvent id="catching_3" name="IntermediateCatchingEvent">
 * <signalEventDefinition signalRef="testSignal"></signalEventDefinition>
 * </intermediateCatchEvent>
 * <userTask activiti:exclusive="true" id="usertask_4" name="UserTask"/>
 * <endEvent id="_5" name="EndEvent"/>
 * <sequenceFlow id="_6" sourceRef="_2" targetRef="catching_3"/>
 * <p>
 * 信号
 * <signal id="testSignal" name="testSignal"></signal>
 * 引用信号
 * signalEventDefinition signalRef="testSignal"></signalEventDefinition>
 */
public class SingleEvent {

    private static Logger logger = LoggerFactory.getLogger(SingleEvent.class);

    public static void main(String[] args) {
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        logger.info("SingleEvent -- get process engine info : " + engine.getName());
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
//        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
//        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/simple.bpmn").deploy();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/single_event.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        logger.info("SingleEvent -- this process instance id is : {}", processInstance.getId());

        // 获取子执行流
        List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().list();
        for (Execution execution : executionList) {
            logger.info("current activity id : {}", execution.getActivityId());
        }

        // 执行流程
        // <signal id="testSignal" name="testSignal"></signal>
        runtimeService.signalEventReceived("testSignal");


        logger.info("执行流程 testSignal");

        executionList = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).onlyChildExecutions().list();
        for (Execution execution : executionList) {
            logger.info(" current activity id : {}", execution.getActivityId());
        }

        engine.close();
        // 退出
        System.exit(0);
    }
}
