package com.xh.activity.deploy;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class DeployZip {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("start add deploy zip activity ...");
        // 启动流程引擎  自定义activiti.cfg.xml文件路径  和引擎bean的名称
        ProcessEngine engine =
                ProcessEngineConfiguration
                        .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration")
                        .buildProcessEngine();
        //得到流程存储服务对象
        RepositoryService repositoryService = engine.getRepositoryService();
        //创建DeploymentBuilder实例
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        //获取zip文件的输入流
        InputStream is =
                DeployZip.class.getClassLoader()
                        .getResourceAsStream("test.zip");
        //读取zip文件，创建ZipInputStream对象
        ZipInputStream zi = new ZipInputStream(is);
        //添加Zip压缩包资源
        deploymentBuilder.addZipInputStream(zi);
        //执行部署（写入到数据库中）
        // 在数据库表 ACT_RE_DEPLOY 和 ACT_GE_BYTEARRAY 中会有数据
        deploymentBuilder.deploy();
        engine.close();
        System.exit(0);
    }

}
