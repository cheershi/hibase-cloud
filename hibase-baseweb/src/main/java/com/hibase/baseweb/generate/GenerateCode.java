package com.hibase.baseweb.generate;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成代码
 *
 * @author hufeng
 * @date 2019/03/21
 */
public class GenerateCode {

    public static String MYBATIS_BASEDO = "com.hibase.baseweb.core.mybatis.BaseDO";

    public static String BASEPAGE_BASEDO = "com.hibase.baseweb.model.PageBase";

    public static String IMPORT_BASEDO = "com.alibaba.excel.metadata.BaseRowModel";

    public static String MYBATIS_WEBDO = "com.hibase.baseweb.core.web.BaseController";

    public static String MYBATIS_SERVICEIMPL = "com.hibase.baseweb.core.service.BaseServiceImpl";

    public static String MYBATIS_SERVICE = "com.hibase.baseweb.core.service.BaseService";

    public static String USER_DIR = "user.dir";

    public static String ENTITY_PACKAGE_NAME_PREFIX = "entity.";

    public static String ENTITYVO_PACKAGE_NAME_PREFIX = "vo";

    public static String ENTITY_PACKAGE_NAME_POSTFIX = "dt";

    public static String SERVICE_PACKAGE_NAME_PREFIX = "service.";

    public static String SERVICEIMPL_PACKAGE_NAME_PREFIX = "service.";

    public static String SERVICEIMPL_PACKAGE_NAME_POSTFIX = "impl";

    public static String MAPPER_PACKAGE_NAME_POSTFIX = "mapper.";

    public static String WEB_PACKAGE_NAME = "web.";

    public static String XML_PACKAGE_NAME = "/src/main/resources/mapper/";

    public static String RESOURCE_DIR = "/src/main/java";

    public static String TEMPLATES_PATH = "/templates/mapper.xml.ftl";

    public static String CODE_POSTFIX_DO = "DO";

    public static String CODE_POSTFIX_MAPPER = "Mapper";

    public static String DATE_TYPE_SQL_DATE = "java.sql.date";

    public static String DATE_TYPE_JAVA8_LOCALTIME = "java.time.LocalTime";

    public static String SUPER_ENTITY_COLUMNS = "id,create_by,create_time,update_by,update_time,be_deleted";

    public static void generateCode(GenerateProperties properties) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty(USER_DIR);
        gc.setOutputDir(projectPath + RESOURCE_DIR);
        gc.setAuthor(properties.getAuthor());
        gc.setOpen(false);
        gc.setSwagger2(false);
        gc.setServiceName("%s" + ConstVal.SERVICE);
        // gc.setEntityName("%s" + CODE_POSTFIX_DO);

        DateType dateType = DateType.ONLY_DATE;

        if (DATE_TYPE_SQL_DATE.equals(properties.getDateType())) {

            dateType = DateType.SQL_PACK;
        } else if (DATE_TYPE_JAVA8_LOCALTIME.equals(properties.getDateType())) {

            dateType = DateType.TIME_PACK;
        }

        gc.setDateType(dateType);
        gc.setBaseColumnList(true);
        gc.setBaseResultMap(true);
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(properties.getDataSourceUrl());
        dsc.setDriverName(properties.getDriverName());
        dsc.setUsername(properties.getUsername());
        dsc.setPassword(properties.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(properties.getParentPackName());
        // pc.setModuleName(properties.getModuleName());
        pc.setEntity(buildName(ENTITY_PACKAGE_NAME_PREFIX, "", properties.getModuleName()));
        //pc.setEntityVo(buildName(ENTITY_PACKAGE_NAME_PREFIX, ENTITYVO_PACKAGE_NAME_PREFIX, properties.getModuleName()));
        pc.setService(buildName(SERVICE_PACKAGE_NAME_PREFIX, "", properties.getModuleName()));
        pc.setServiceImpl(buildName(SERVICEIMPL_PACKAGE_NAME_PREFIX, SERVICEIMPL_PACKAGE_NAME_POSTFIX, properties.getModuleName()));
        pc.setMapper(buildName(MAPPER_PACKAGE_NAME_POSTFIX, "", properties.getModuleName()));
        pc.setController(buildName(WEB_PACKAGE_NAME, "", properties.getModuleName()));
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = TEMPLATES_PATH;
        // 如果模板引擎是 velocity

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                StringBuilder result = new StringBuilder();
                result.append(projectPath);
                result.append(XML_PACKAGE_NAME);
                if (StrUtil.isNotBlank(properties.getModuleName())) {
                    result.append(properties.getModuleName());
                    result.append("/");
                }
                result.append(tableInfo.getEntityName());
                result.append(CODE_POSTFIX_MAPPER);
                result.append(StringPool.DOT_XML);
                return result.toString();
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass(MYBATIS_BASEDO);
       // strategy.setSuperSearchVOClass(BASEPAGE_BASEDO);
        //strategy.setSuperImportVOClass(IMPORT_BASEDO);
        strategy.setSuperServiceImplClass(MYBATIS_SERVICEIMPL);
        strategy.setSuperServiceClass(MYBATIS_SERVICE);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setSuperControllerClass(MYBATIS_WEBDO);
        strategy.setInclude(properties.getTableName());

        strategy.setSuperEntityColumns(SUPER_ENTITY_COLUMNS.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    private static String buildName(String preFix, String postFix, String moduleName) {

        String result = preFix;
        if (StrUtil.isNotBlank(moduleName)) {

            result = result + moduleName;

        }

        if(StrUtil.isNotBlank(postFix)){

            result = result + ".";
            result = result + postFix;
        }

        return result;
    }
}
