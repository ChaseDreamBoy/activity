package com.xh.activity.query;

import com.xh.activity.query.util.GroupUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.identity.Group;

import java.util.List;

public class SortQuery {

    public static void main(String[] args){
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        // sort    order by  desc
//        List<Group> groupList = identityService.createGroupQuery().orderByGroupName().desc().list();
        // 多字段排序
        List<Group> groupList = identityService.createGroupQuery()
                // 多字段排序
                .orderByGroupName().desc()
                .orderByGroupId().asc()
                .list();
        for (Group group : groupList) {
            System.out.println(GroupUtil.getGroupInfo(group));
        }
        engine.close();
        System.exit(0);
    }

}
