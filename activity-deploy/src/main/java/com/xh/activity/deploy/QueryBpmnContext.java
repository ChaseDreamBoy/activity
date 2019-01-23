package com.xh.activity.deploy;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;

import java.io.IOException;
import java.io.InputStream;

public class QueryBpmnContext {

    public static void main(String[] args) throws IOException {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        //得到流程存储服务对象
        RepositoryService repositoryService = engine.getRepositoryService();
        //创建DeploymentBuilder实例
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("bpmn/first.bpmn");
        //执行部署（写入到数据库中）
        // 在数据库表 ACT_RE_DEPLOY 和 ACT_GE_BYTEARRAY 中会有数据
        Deployment deployment = deploymentBuilder.deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 数据查询
        InputStream inputStream = repositoryService.getProcessModel(processDefinition.getId());
        int count = inputStream.available();
        byte[] contents = new byte[count];
        inputStream.read(contents);
        String result = new String(contents);
        System.out.println("result : \n" + result);
        engine.close();
        System.exit(0);
    }

}
