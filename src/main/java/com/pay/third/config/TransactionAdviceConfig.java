package com.pay.third.config;

import com.zaxxer.hikari.HikariDataSource;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * 数据库全局事务管理
 *
 * @author Tang
 * @version 1.0.0
 * @date 2021/3/31 9:32
 */
@Aspect
@Configuration
public class TransactionAdviceConfig {
    /**
     * 设置service包
     */
    private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.pay.third.service..*(..))";

    @Bean("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource hikariDataSource() throws Exception {
        HikariDataSource dataSource = new HikariDataSource();
        return dataSource;
    }

    @Bean("txManager")
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("txAdvice")
    public TransactionInterceptor txAdvice(DataSourceTransactionManager txManager) {
        // 事务
        RuleBasedTransactionAttribute txAttr_REQUIRED = new RuleBasedTransactionAttribute();
        // 抛出异常后执行切点回滚
        txAttr_REQUIRED.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        // PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
        txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 超过60秒回滚
//        txAttr_REQUIRED.setTimeout(60);

        // 只读
        DefaultTransactionAttribute txAttr_REQUIRED_READONLY = new DefaultTransactionAttribute();
        txAttr_REQUIRED_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txAttr_REQUIRED_READONLY.setReadOnly(true);



        // 根据方法名来设置哪些需要使用事务，哪些不需要使用事务
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("add*", txAttr_REQUIRED);
        source.addTransactionalMethod("insert*", txAttr_REQUIRED);
        source.addTransactionalMethod("save*", txAttr_REQUIRED);
        source.addTransactionalMethod("delete*", txAttr_REQUIRED);
        source.addTransactionalMethod("update*", txAttr_REQUIRED);
        source.addTransactionalMethod("exec*", txAttr_REQUIRED);
        source.addTransactionalMethod("set*", txAttr_REQUIRED);
        // 只读操作设置
        source.addTransactionalMethod("get*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("query*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("find*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("list*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("count*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("is*", txAttr_REQUIRED_READONLY);
        return new TransactionInterceptor(txManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor(TransactionInterceptor txAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}
