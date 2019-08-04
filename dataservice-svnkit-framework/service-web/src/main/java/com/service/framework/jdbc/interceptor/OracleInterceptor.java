package com.service.framework.jdbc.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({
        @Signature(
                // 只能是: StatementHandler | ParameterHandler | ResultSetHandler | Executor 类或者子类
                type = StatementHandler.class,
                // StatementHandler (prepare, parameterize, batch, update, query)
                // ResultSetHandler (handleResultSets, handleOutputParameters)
                // ParameterHandler (getParameterObject, setParameters)
                // Executor (update, query, flushStatements, commit, rollback,getTransaction, close, isClosed)
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
public class OracleInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = (StatementHandler) invocation.getTarget();
        // 获取指定对象的元信息
        MetaObject metaObject = MetaObject.forObject(
                handler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory()
        );
        metaObject.setValue("delegate.boundSql.sql", handler.getBoundSql().getSql().replace("`", ""));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}