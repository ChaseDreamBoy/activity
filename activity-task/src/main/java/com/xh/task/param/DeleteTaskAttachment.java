package com.xh.task.param;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;

import java.io.InputStream;

/**
 * 删除 task 中附件
 * 如果上传附件是以输入流的方式创建的，那么调用接口删除附件并不会真的删除资源表中的数据，
 * 只会删除附件表中的数据和相应的历史数据.
 */
public class DeleteTaskAttachment {

    public static void main(String[] args) throws Exception {
        // 创建流程引擎
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();

        System.out.println();
        // 获取任务服务组件
        TaskService taskService = engine.getTaskService();
        // 获取运行服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        System.out.println("-------------DeleteTaskAttachment--------------");
        // 流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 部署流程描述文件
        Deployment dep = repositoryService.createDeployment()
                .addClasspathResource("bpmn/first.bpmn").deploy();
        System.out.println("-------------DeleteTaskAttachment--------------");
        // 查找流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId()).singleResult();
        // 启动流程
        ProcessInstance pi = runtimeService
                .startProcessInstanceById(pd.getId());
        System.out.println("-------------DeleteTaskAttachment--------------");
        // 查找任务
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId())
                .singleResult();
        // 设置任务附件
        Attachment att1 = taskService.createAttachment("web url", task.getId(), pi.getId(), "Attachement1",
                "163 web page", "http://www.163.com");
        System.out.println("-------------DeleteTaskAttachment--------------");
        // 创建图片输入流
        InputStream is =
                DeleteTaskAttachment.class.getClassLoader()
                        .getResourceAsStream("result.png");
        // 设置输入流为任务附件
        Attachment att2 = taskService.createAttachment("web url", task.getId(), pi.getId(), "Attachement2",
                "Image InputStream", is);
        System.out.println("删除前数量：" + taskService.getTaskAttachments(task.getId()).size());
        // 删除附件
        taskService.deleteAttachment(att2.getId());
        System.out.println("删除后数量：" + taskService.getTaskAttachments(task.getId()).size());
    }

}
