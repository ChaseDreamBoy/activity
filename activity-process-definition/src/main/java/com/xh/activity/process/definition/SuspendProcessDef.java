package com.xh.activity.process.definition;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

// 流程启用与禁用 -- 启用:activate  禁用:suspend
public class SuspendProcessDef {

    public static void main(String[] args) {
        // 创建流程引擎
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        // 得到流程存储服务实例
        RepositoryService repositoryService = engine.getRepositoryService();
        // 部署流程描述文件
        Deployment dep = repositoryService.createDeployment()
                .addClasspathResource("bpmn/my_process.bpmn")
                .deploy();
        //查询流程定义实体
        ProcessDefinition def = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId()).singleResult();
        System.out.println("id : " + def.getId() + ", key : " + def.getKey());
        // 调用suspendProcessDefinitionById中止流程定义
        repositoryService.suspendProcessDefinitionById(def.getId());
        // 调用activateProcessDefinitionById激活流程定义
        repositoryService.activateProcessDefinitionById(def.getId());
        // 调用suspendProcessDefinitionByKey中止流程定义
        repositoryService.suspendProcessDefinitionByKey(def.getKey());
        // 调用activateProcessDefinitionByKey激活流程定义
        repositoryService.activateProcessDefinitionByKey(def.getKey());
    }

}
