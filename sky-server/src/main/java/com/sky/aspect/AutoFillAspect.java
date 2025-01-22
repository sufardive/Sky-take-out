package com.sky.aspect;

import com.sky.annotion.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..))")
//    you just tool,dont need have (why)
    public void autoFillpointCut() {}
//    class only exis one
    @Before("autoFillpointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("Auto fill point start");
//        1. 获取当前被拦截的方法上的数据库类型
//        1. signature 方法前面对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        2. 获得方法上的注解对象
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
//        3. 获得数据库操作类型
        OperationType operationType = autoFill.value();
//        2. 获取当前被拦截方法的参数（获得实体对象）
//        1. 连接点参数
        Object[] args = joinPoint.getArgs();
//        2. 防止为空，进行参数判断
        if (args == null || args.length == 0) {
            return;
        }
//        3. 用object收东西
        Object entity = args[0];
//        3. 准备赋值的数据
        LocalTime now = LocalTime.now();
        Long currentid = BaseContext.getCurrentId();
//        4. 根据当前 不同的操作类型为对应的属性通过反射赋值
        if (operationType == OperationType.INSERT) {
            try {
//                make method by myself
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentid);
                setUpdateUser.invoke(entity,currentid);
            } catch (Exception e) {
                throw new RuntimeException(e);}
        } else if (operationType == OperationType.UPDATE) {
            try {

            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        3. 通过反射为对象属性赋值

//       ```java
//        setCreateTime.invoke(xx.class)
//       ```
    }}}