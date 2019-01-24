package com.xh.activity.process.definition;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;

// 部署 流程
public class DeployMyProcess {

    public static void main(String[] args)  {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        //得到流程存储服务对象
        RepositoryService repositoryService = engine.getRepositoryService();
        //创建DeploymentBuilder实例
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("bpmn/my_process.bpmn");
        //执行部署（写入到数据库中）
        // 在数据库表 ACT_RE_DEPLOY 和 ACT_GE_BYTEARRAY 中会有数据
        deploymentBuilder.deploy();
        engine.close();

        System.exit(0);
    }

}
