package com.xh.task.param;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.util.List;

public class GetTaskAttachment {

    public static void main(String[] args) throws Exception {
        // 创建流程引擎
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();

        // 获取运行服务组件
        RuntimeService runtimeService = engine.getRuntimeService();

        // 获取任务服务组件
        TaskService taskService = engine.getTaskService();
        // 流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 部署流程描述文件
        Deployment dep = repositoryService.createDeployment()
                .addClasspathResource("bpmn/first.bpmn").deploy();
        // 查找流程定义
        System.out.println();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId()).singleResult();
        // 启动流程
        ProcessInstance pi = runtimeService
                .startProcessInstanceById(pd.getId());
        System.out.println();
        // 查找任务
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId())
                .singleResult();
        // 设置任务附件
        Attachment att1 = taskService.createAttachment("web url", task.getId(), pi.getId(), "Attachement1",
                "163 web page", "http://www.163.com");
        // 创建图片输入流
        InputStream is =
                GetTaskAttachment.class.getClassLoader()
                        .getResourceAsStream("result.png");
        // 设置输入流为任务附件
        Attachment att2 = taskService.createAttachment("web url", task.getId(), pi.getId(), "Attachement2",
                "Image InputStream", is);

        // 根据流程实例ID查询附件
        List<Attachment> attas1 = taskService.getProcessInstanceAttachments(pi.getId());
        System.out.println("流程附件数量：" + attas1.size());
        // 根据任务ID查询附件
        List<Attachment> attas2 = taskService.getTaskAttachments(task.getId());
        System.out.println("任务附件数量：" + attas2.size());
        // 根据附件ID查询附件
        Attachment attResult = taskService.getAttachment(att1.getId());
        System.out.println("附件1名称：" + attResult.getName());
        // 根据附件ID查询附件内容
        InputStream stream1 = taskService.getAttachmentContent(att1.getId());
        // 附件一 只提供了 url  没有提供url  所以返回null
        System.out.println("附件1的输入流：" + stream1);
        InputStream stream2 = taskService.getAttachmentContent(att2.getId());
        System.out.println("附件2的输入流：" + stream2);
    }

}
