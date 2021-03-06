package com.xh.activity.deploy;

import org.activiti.bpmn.model.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;

public class DeployBpmn {

    public static void main(String[] args) {
        // 创建流程引擎
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        // 得到流程存储服务对象
        RepositoryService repositoryService = engine.getRepositoryService();
        // 创建DeploymentBuilder实例
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addBpmnModel("MyCodeProcess", createProcessModel())
                .name("MyCodeDeploy").deploy();
    }

    private static BpmnModel createProcessModel() {
        // 创建BPMN模型对象
        BpmnModel model = new BpmnModel();
        org.activiti.bpmn.model.Process process = new org.activiti.bpmn.model.Process();
        model.addProcess(process);
        process.setId("myProcess");
        process.setName("My Process");
        // 开始事件
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        process.addFlowElement(startEvent);
        // 用户任务
        UserTask userTask = new UserTask();
        userTask.setName("User Task");
        userTask.setId("userTask");
        process.addFlowElement(userTask);
        // 结束事件
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        process.addFlowElement(endEvent);
        // 添加流程顺序
        process.addFlowElement(new SequenceFlow("startEvent", "userTask"));
        process.addFlowElement(new SequenceFlow("userTask", "endEvent"));
        return model;
    }

}
