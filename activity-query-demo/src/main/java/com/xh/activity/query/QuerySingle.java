package com.xh.activity.query;

import com.xh.activity.query.util.GroupUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.identity.Group;

public class QuerySingle {

    public static void main(String[] args) {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        // 返回两个结果会返回异常
        Group group = identityService.createGroupQuery()
                .groupId("1").singleResult();
        System.out.println(GroupUtil.getGroupInfo(group));
        engine.close();
        System.exit(0);
    }

}
