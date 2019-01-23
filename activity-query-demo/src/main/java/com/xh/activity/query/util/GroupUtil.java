package com.xh.activity.query.util;

import org.activiti.engine.identity.Group;

public class GroupUtil {

    public static String getGroupInfo(Group group) {
        return "id = " + group.getId() + ", name = " + group.getName() + ", type = " + group.getType();
    }

}
