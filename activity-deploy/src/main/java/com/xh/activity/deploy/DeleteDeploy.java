package com.xh.activity.deploy;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

/**
 * 删除部署数据
 * 1. 不管是否指定级联，都会删除部署相关的身份数据、流程定义数据、流程资源与部署数据。
 * 2. 如果设置为级联删除，则会将运行的流程实例、流程任务以及流程实例的历史数据删除。
 * 3. 如果不级联删除，但是存在运行时数据，例如还有流程实例，就会删除失败。
 */
public class DeleteDeploy {

    public static void main(String[] args) {
        // 创建流程引擎
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        // 得到流程存储服务对象
        RepositoryService repositoryService = engine.getRepositoryService();
        // 部署一份流程文件与相应的流程图文件
        Deployment dep = repositoryService.createDeployment()
                .addClasspathResource("bpmn/delete.bpmn")
                .deploy();
        // 查询流程定义
        ProcessDefinition def = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId()).singleResult();
        // 开始流程
        engine.getRuntimeService().startProcessInstanceById(def.getId());
        try {
            // 删除部署数据失败，此时将会抛出异常，由于cascade为false
            repositoryService.deleteDeployment(dep.getId());
        } catch (Exception e) {
            System.out.println("删除失败，流程开始，没有设置cascade为true");
        }
        System.out.println("============分隔线");
        // 成功删除部署数据
        repositoryService.deleteDeployment(dep.getId(), true);
    }

}
