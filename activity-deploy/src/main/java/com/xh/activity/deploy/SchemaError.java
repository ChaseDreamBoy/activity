package com.xh.activity.deploy;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;

import java.io.FileNotFoundException;

// 验证错误 的 bpmn 文件  部署
public class SchemaError {

    public static void main(String[] args) throws FileNotFoundException {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        //得到流程存储服务对象
        RepositoryService repositoryService = engine.getRepositoryService();
        //创建DeploymentBuilder实例
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        // xml 格式错误的会抛出异常
//        deploymentBuilder.addClasspathResource("bpmn/schema_error.bpmn");
        // 流程业务逻辑有问题 会报错
        deploymentBuilder.addClasspathResource("bpmn/process_error.bpmn");
        // 关闭格式验证 -- 错误的bpmn文件也可以部署
//        deploymentBuilder.disableSchemaValidation();
        //执行部署（写入到数据库中）
        // 在数据库表 ACT_RE_DEPLOY 和 ACT_GE_BYTEARRAY 中会有数据
        deploymentBuilder.deploy();
        engine.close();
        System.exit(0);
    }

}
