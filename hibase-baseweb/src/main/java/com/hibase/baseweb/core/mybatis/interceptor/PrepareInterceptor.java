package com.hibase.baseweb.core.mybatis.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Stopwatch;
import com.hibase.baseweb.annotation.mybatis.DataPerm;
import com.hibase.baseweb.cache.DataPermCache;
import com.hibase.baseweb.model.User;
import com.hibase.baseweb.utils.PermissionUtils;
import com.hibase.baseweb.utils.UserUtlis;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 数据权限mybatis拦截器
 *
 * @author chenfeng
 * @date 2019/05/26
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
@Slf4j
public class PrepareInterceptor implements Interceptor {

    @Setter
    private DataPermCache dataPermCache;

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (log.isInfoEnabled()) {
            log.info("进入 PrepareInterceptor 拦截器...");
        }

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
        } else {
            //6 个参数时
            boundSql = (BoundSql) args[5];
        }


        if(UserUtlis.getLoginUserInfo() == null){

            // 获取不到用户信息，直接返回
            return invocation.proceed();
        }

        // 过滤所属平台sql
        // String sql = permissionSql(boundSql.getSql(), buildPlatformDataPermSql());
        String sql = boundSql.getSql();

        DataPerm dataPerm = PermissionUtils.getPermissionByDelegate(ms);
        if (null != dataPerm && !dataPerm.enable()) {
            if (log.isInfoEnabled()) {
                log.info("当前数据权限配置已关闭");
            }
            return invocation.proceed();
        }

        if (log.isInfoEnabled()) {

            if (CollUtil.isEmpty(UserUtlis.getLoginUserInfo().getDataPerm())) {

                log.info("数据权限处理未配置过滤sql，跳出数据权限过滤");

                return invocation.proceed();
            }

            log.info("数据权限处理已开启");
        }

        // 获取当前用户的权限sql
        String dataPermSql = buildDataPermSql();

        ReflectUtil.setFieldValue(boundSql, "sql", permissionSql(sql, dataPermSql));

        if (args.length == 4) {

            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(boundSql));

            args[0] = newMs;
        } else {

            args[5] = boundSql;
        }

        return invocation.proceed();
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 权限sql包装
     */
    protected String permissionSql(String sql, String dataPermSql) {

        StringBuilder sbSql = new StringBuilder();

        // 根据where关键字将sql分成两部分
        String[] splitSql = sql.split("where|WHERE");

        int index = 0;

        for (String tempSql : splitSql) {

            sbSql.append(tempSql);

            if (index < splitSql.length - 1) {

                sbSql.append(" where ");
                sbSql.append(dataPermSql);
            }

            index++;
        }

        return sbSql.toString();
    }

    /**
     * 获取当前用户的权限sql
     *
     * @return
     */
    private String buildDataPermSql() {

        StringBuilder resultSql = new StringBuilder();

        // 获取数据权限sql
        User user = UserUtlis.getLoginUserInfo(true);

        if (CollUtil.isNotEmpty(user.getDataPerm())) {

            // 替换内置表达式
            Map<String, Object> replaceMap = this.getReplaceMap(user);

            resultSql.append(" (");

            int dataPermSqlIndex = 0;

            for (String dataPermSql : user.getDataPerm()) {

                int index = 0;

                String[] splitSql = dataPermSql.split("\\|\\|");

                for (String sql : splitSql) {

                    resultSql.append(" (");

                    sql = StrSubstitutor.replace(sql, replaceMap, "#{", "}");

                    resultSql.append(sql);

                    resultSql.append(") ");

                    if (index < splitSql.length - 1) {

                        resultSql.append(" or ");
                    }

                    index++;
                }

                if (dataPermSqlIndex < user.getDataPerm().size() - 1) {

                    resultSql.append(" or ");
                }

                dataPermSqlIndex ++;
            }

            resultSql.append(") and");
        }

        return resultSql.toString();
    }

    /**
     * 生成所属平台过滤sql
     *
     * @return
     */
    private String buildPlatformDataPermSql() {

        User user = UserUtlis.getLoginUserInfo(true);

        StringBuilder builder = new StringBuilder();

        builder.append("platform_id = ");
        builder.append(user.getPlatformId());
        builder.append(" and ");

        return builder.toString();
    }

    /**
     * 替换内置表达式
     */
    private Map<String, Object> getReplaceMap(User user) {

        Map<String, Object> map = new HashMap<>(10);

        // 当前用户id
        map.put("currentUserId", StrUtil.isNotBlank(user.getLoginName()) ? user.getLoginName() : "null");

        // 当前用户所属组织
        map.put("currentOrgId", StrUtil.isNotBlank(user.getOrgId()) ? user.getOrgId() : "null");

        // 可以查看当前用户的下级组织
        // List<String> orgIds = dataPermCache.getOrgInfo(user.getOrgId());

        // 获取指定组织id
        List<String> assignOrgExps = user.getDataPerm().stream().filter(t -> t.contains("queryAssignOrgExp")).collect(Collectors.toList());

        assignOrgExps.stream().parallel().forEach(
                t -> {
                    String assignOrgId = t.replace("#{queryAssignOrgExp(", "").replace(")}", "");

                    List<String> assignOrgIds = dataPermCache.getOrgInfo(assignOrgId);

                    // 生成查看当前用户的下级组织表达式
                    StringBuilder queryAssignOrgExp = buildOrgExpress(assignOrgIds);

                    map.put(t.replace("#{", "").replace("}", ""), queryAssignOrgExp.toString());
                }
        );

        if (StrUtil.isNotBlank(user.getOrgFullId())) {

            String[] fullIdArr = user.getOrgFullId().split("/");

            if (ArrayUtil.isNotEmpty(fullIdArr) && fullIdArr.length >= 2) {

                // 生成查看当前用户的可以查看第二层级组织表达式(区域)
                List<String> orgTwoIds = dataPermCache.getOrgInfo(fullIdArr[1]);

                StringBuilder queryLeverExp = buildOrgExpress(orgTwoIds);

                map.put("queryLeverExp", queryLeverExp.toString());
            }

            if (ArrayUtil.isNotEmpty(fullIdArr) && fullIdArr.length >= 3) {

                // 生成查看当前用户的可以查看第三层级组织表达式(区域)
                List<String> orgThreeIds = dataPermCache.getOrgInfo(fullIdArr[2]);

                StringBuilder queryThreeLeverExp = buildOrgExpress(orgThreeIds);

                map.put("queryThreeLeverExp", queryThreeLeverExp.toString());
            }
        }

        // 当前所属平台
        map.put("currentPlatformId", StrUtil.isNotBlank(user.getPlatformId()) ? user.getPlatformId() : "null");

        if (log.isInfoEnabled()) {

            if (StrUtil.isBlank(user.getOrgId())) {

                log.info("当前登录的用户{}组织为空，数据权限校验设置当前用户组织为null", user.getLoginName());
            }
        }

        return map;
    }

    /**
     * 生成表达式
     */
    private StringBuilder buildOrgExpress(List<String> orgIds) {

        StringBuilder queryLeverExp = new StringBuilder();

        int index = 0;

        if (CollUtil.isNotEmpty(orgIds)) {

            queryLeverExp.append(" (");
            for (String orgId : orgIds) {

                queryLeverExp.append("mds_org_id=");
                queryLeverExp.append("'");
                queryLeverExp.append(orgId);
                queryLeverExp.append("'");

                if (index < orgIds.size() - 1) {

                    queryLeverExp.append(" or ");
                }

                index++;
            }

            queryLeverExp.append(") ");
        }

        return queryLeverExp;
    }

    public static void main(String[] args) {

        String sql = "select * from mds_data_filter where id= '61039556501213184' or be_active = 0 and be_deleted=0";

        Stopwatch stopwatch = Stopwatch.createStarted();
        try {

            Statement stmt = CCJSqlParserUtil.parse(sql);

            Select select = (Select) stmt;
            SelectBody selectBody = select.getSelectBody();

            if(selectBody instanceof PlainSelect){

                PlainSelect plainSelect = (PlainSelect) selectBody;

                FromItem fromItem = plainSelect.getFromItem();

                Expression expression = plainSelect.getWhere();

                EqualsTo leftExpree = new EqualsTo();
                leftExpree.setLeftExpression(new Column("filter_name"));
                leftExpree.setRightExpression(new StringValue("aaa"));

                AndExpression customerExpree = new AndExpression(leftExpree, expression);

                plainSelect.setWhere(customerExpree);

//                if (fromItem != null) {
//
//                    Alias alias = fromItem.getAlias();
//
//                    if (alias != null && StrUtil.isNotBlank(alias.getName())) {
//
//
//                    }
//                }

                System.out.println(selectBody);
            }



//            List<SelectBody> selectBodys =  operationList.getSelects();
//
//            if (MyUtils.isNotEmpty(selectBodys)) {
//
//                selectBodys.stream().forEach(d -> {
//
//                    PlainSelect plainSelect = (PlainSelect) d;
//
//                    FromItem fromItem = plainSelect.getFromItem();
//
//                    if (fromItem != null) {
//
//                        Alias alias = fromItem.getAlias();
//
//                        if (StrUtil.isNotBlank(alias.getName())) {
//
//
//                        }
//                    }
//                });
//            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        Long consumeTime = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        log.info("耗时：" + consumeTime + "(毫秒).");
//        String ab = "50267856687341568/50267857685585920/50267864690073600";
//
//        int index = ab.indexOf("/");
//        if (index > 0) {
//
//            System.out.println(ab.substring(0, index));
//        } else {
//
//            System.out.println(ab);
//        }
    }

    public static class BoundSqlSqlSource implements SqlSource {

        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
