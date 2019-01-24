package com.xh.task.param;


import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * 流程 初始化 参数
 * <process id="vacationProcess" name="vacation">
 * <!-- 定义了名称，默认值为 Crazyit -->
 * <dataObject id="personName" name="personName" itemSubjectRef="xsd:string">
 * <extensionElements>
 * <activiti:value>Crazyit</activiti:value>
 * </extensionElements>
 * </dataObject>
 * <!-- 定义了年龄，默认值为20 -->
 * <dataObject id="personAge" name="personAge" itemSubjectRef="xsd:int">
 * <extensionElements>
 * <activiti:value>20</activiti:value>
 * </extensionElements>
 * </dataObject>
 * <startEvent id="startevent1" name="Start"></startEvent>
 * <userTask id="usertask1" name="Write Vacation"></userTask>
 * <endEvent id="endevent1" name="End"></endEvent>
 * <sequenceFlow id="flow1" name="" sourceRef="startevent1"
 * targetRef="usertask1"></sequenceFlow>
 * <sequenceFlow id="flow2" name="" sourceRef="usertask1"
 * targetRef="endevent1"></sequenceFlow>
 * </process>
 */
public class TaskProcessInitParam {

    public static void main(String[] args) {
        // 创建流程引擎
//        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 配置文件 和bean 名称
        ProcessEngine engine =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();

        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();

        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();

        // 获取流程任务组件
        TaskService taskService = engine.getTaskService();
        // 部署流程文件
        // act_re_deployment  act_get_bytearray
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/process_init_param.bpmn").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        System.out.println(processDefinition.getId());
        // 启动流程  key是 xml中process 的 id  -- 到达第一个节点
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());

        // 查询第一个任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        String personName = taskService.getVariable(task.getId(), "personName", String.class);
        System.out.println("personName : " + personName);

        engine.close();
        // 退出
        System.exit(0);
    }

}
