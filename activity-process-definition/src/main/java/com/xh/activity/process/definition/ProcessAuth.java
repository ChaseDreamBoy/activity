package com.xh.activity.process.definition;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.List;
import java.util.UUID;

public class ProcessAuth {

    public static void main(String[] args) {

        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        //得到流程存储服务对象
        RepositoryService repositoryService = engine.getRepositoryService();
        IdentityService identityService = engine.getIdentityService();

        String uuid = UUID.randomUUID().toString();
        User user = identityService.newUser(uuid);
        user.setFirstName("username" + uuid);
        identityService.saveUser(user);

        //创建DeploymentBuilder实例
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("bpmn/my_process.bpmn");
        //执行部署（写入到数据库中）
        // 在数据库表 ACT_RE_DEPLOY 和 ACT_GE_BYTEARRAY 中会有数据
        Deployment deployment = deploymentBuilder.deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        repositoryService.addCandidateStarterUser(processDefinition.getId(), user.getId());

        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().startableByUser(user.getId()).list();
        for (ProcessDefinition processDefinitionInfo : processDefinitionList) {
            System.out.println("processDefinition id : " + processDefinitionInfo.getId());
        }

        engine.close();
        System.exit(0);
    }

}
