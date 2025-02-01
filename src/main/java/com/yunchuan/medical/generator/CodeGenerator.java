package com.yunchuan.medical.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * MyBatis-Plus 代码生成器
 */
public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/medical?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String username = "root";
        String password = "123456";
        
        // 设置项目根目录的绝对路径
        String projectPath = "D:/运城市移动医疗咨询平台/后端/demo5";
        
        FastAutoGenerator.create(url, username, password)
            .globalConfig(builder -> {
                builder.author("yunchuan") // 设置作者
                    .outputDir(projectPath + "/src/main/java"); // 指定输出目录
            })
            .packageConfig(builder -> {
                builder.parent("com.yunchuan.medical") // 设置父包名
                    .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper")); // 设置mapperXml生成路径
            })
            .strategyConfig(builder -> {
                builder.addInclude("user", "department", "doctor", "appointment", "rating") // 设置需要生成的表名
                    .controllerBuilder()
                        .enableRestStyle()
                        .enableHyphenStyle()
                        .build()
                    .entityBuilder()
                        .enableLombok()
                        .enableTableFieldAnnotation()
                        .enableFileOverride() // 开启文件覆盖
                        .build()
                    .mapperBuilder()
                        .enableMapperAnnotation()
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .enableFileOverride() // 开启文件覆盖
                        .build()
                    .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        .enableFileOverride() // 开启文件覆盖
                        .build()
                    .controllerBuilder()
                        .enableFileOverride() // 开启文件覆盖
                        .build();
            })
            .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板
            .execute();
    }
} 