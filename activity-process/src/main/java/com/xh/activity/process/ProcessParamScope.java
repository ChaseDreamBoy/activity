package com.xh.activity.process;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProcessParamScope {

    private static Logger logger = LoggerFactory.getLogger(ProcessParamScope.class);

    public static void main(String[] args) {
// 创建流程引擎
//        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 配置文件 和bean 名称
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
        logger.info("get process engine info : " + engine.getName());
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
//        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/simple.bpmn").deploy();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/process_param_scope.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        // 启动流程  key是 xml中process 的 id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        logger.info("ProcessParamScope startup process success...");
        /**
         * 到达 这一步  可以在数据库表 : ACT_RU_EXECUTION 中看见数据
         * 该表中有一条  主执行流
         * 有多少个分支就有多少 子执行流
         */
        logger.info("this process instance id is : {}", processInstance.getId());

        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task task : taskList) {
            logger.info("the process first step task name is : {}", task.getName());
            // 获取执行流
            Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
            if ("task1".equals(task.getName())) {
                runtimeService.setVariableLocal(execution.getId(), "task1param", "task1value");
            } else if ("task2".equals(task.getName())) {
                runtimeService.setVariable(execution.getId(), "task2param", "task2value");
            }
        }

        //两个执行流时输出参数
        for (Task task : taskList) {
            Execution exe = runtimeService.createExecutionQuery()
                    .executionId(task.getExecutionId()).singleResult();
            if ("task1".equals(task.getName())) {
                logger.info("使用getVariableLocal方法获取task1参数: {}", runtimeService.getVariableLocal(exe.getId(), "task1param"));
                logger.info("使用getVariable方法获取task1参数: {}", runtimeService.getVariable(exe.getId(), "task1param"));
            } else if ("task2".equals(task.getName())) {
                logger.info("使用getVariableLocal方法获取task2参数: {}", runtimeService.getVariableLocal(exe.getId(), "task2param"));
                logger.info("使用getVariable方法获取task2参数: {}", runtimeService.getVariable(exe.getId(), "task2param"));
            }
        }
        //完成任务
        for (Task task : taskList) {
            taskService.complete(task.getId());
        }
        logger.info("========  完成流程分支后     ========");
        //重新查找流程任务
        taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Task task : taskList) {
            logger.info("当前流程节点：" + task.getName());
            Execution exe = runtimeService.createExecutionQuery()
                    .executionId(task.getExecutionId()).singleResult();
            logger.info("task1参数：" + runtimeService.getVariable(exe.getId(), "task1param"));
            logger.info("task2参数：" + runtimeService.getVariable(exe.getId(), "task2param"));
        }

        engine.close();
        // 退出
        System.exit(0);
    }

}
