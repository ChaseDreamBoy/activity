package com.xh.task;

// 一个用户 只有一个 拥有者  -- ACT_RU_TASK 中 OWNER_ 字段
// 一个用户 只有一个 代理人  -- ACT_RU_TASK 中 ASSIGNEE_ 字段
// 一个用户 可以有多个 候选人  -- ACT_RU_IDENTITYLINK
// complete 完成任务
public class TaskApp {

    public static void main(String[] args){
        System.out.println("start activity task ...");
    }

}
