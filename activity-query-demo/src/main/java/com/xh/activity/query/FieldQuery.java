package com.xh.activity.query;

import com.xh.activity.query.util.GroupUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.identity.Group;

import java.util.List;

public class FieldQuery {

    public static void main(String[] args) {
        System.out.println("start query multiple field ...");
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<Group> groupList = identityService.createGroupQuery()
                // 需要同时满足 两个条件才能查询出来
                .groupName("GroupName_2")
                .groupType("GroupType_1")
                .list();
        for (Group group : groupList) {
            System.out.println(GroupUtil.getGroupInfo(group));
        }
        engine.close();
        System.out.println("end query multiple field ...");
        System.exit(0);
    }

}
