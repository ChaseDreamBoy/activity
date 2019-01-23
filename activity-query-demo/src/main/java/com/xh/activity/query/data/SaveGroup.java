package com.xh.activity.query.data;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.identity.Group;

public class SaveGroup {

    public static void main(String[] args) {
        System.out.println("start query activity ...");
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        // 保存多条数据
        for (int i = 1; i < 14; i++) {
            // 传入id
            Group group = identityService.newGroup(String.valueOf(i));
            // 设置group的名称
            group.setName("GroupName_" + i);
            group.setType("GroupType_" + i);
            // ACT_ID_GROUP 表中就有该数据了
            identityService.saveGroup(group);
        }
        engine.close();
        System.exit(0);
    }

}
