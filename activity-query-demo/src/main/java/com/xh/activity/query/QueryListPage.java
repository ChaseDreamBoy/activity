package com.xh.activity.query;

import com.xh.activity.query.util.GroupUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.identity.Group;

import java.util.List;

public class QueryListPage {

    public static void main(String[] args) {
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        // start page start with 0  --   page size
        List<Group> groupList = identityService.createGroupQuery().listPage(0,4);
        for (Group group : groupList) {
            System.out.println(GroupUtil.getGroupInfo(group));
        }
        // get count
        long count = identityService.createGroupQuery().count();
        System.out.println("count = " + count);
        engine.close();
        System.exit(0);
    }

}
