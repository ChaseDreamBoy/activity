package com.xh.task.param;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * ACT_HI_ATTACHMENT 附件表 保存附件数据
 * 其对应实体类为:AttachmentEntityImpl
 *
 * 保存后 ACT_GE_BYTEARRAY 表中也会有数据
 */
public class UploadTaskAttachment {


    public static void main(String[] args) throws FileNotFoundException {
        // 创建流程引擎
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        // 获取任务服务组件
        TaskService taskService = engine.getTaskService();
        // 获取运行服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 部署流程描述文件
        Deployment dep = repositoryService.createDeployment()
                .addClasspathResource("bpmn/first.bpmn").deploy();
        // 查找流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .deploymentId(dep.getId()).singleResult();
        // 启动流程
        ProcessInstance pi = runtimeService
                .startProcessInstanceById(pd.getId());
        // 查找任务
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId())
                .singleResult();
        // 设置任务附件 -- url方式
        // params: attachmentType taskId ProcessInstanceId attachmentName attachmentDescription url(附件得人url地址)
        taskService.createAttachment("web url", task.getId(), pi.getId(), "163.com",
                "163 web page", "http://www.163.com");
        // 创建图片输入流
        InputStream is =
                UploadTaskAttachment.class.getClassLoader()
                        .getResourceAsStream("result.png");
        // 设置输入流为任务附件 -- 输入流方式
        taskService.createAttachment("web url", task.getId(), pi.getId(), "163.com",
                "163 web page", is);
        engine.close();
        // 退出
        System.exit(0);
    }

}
