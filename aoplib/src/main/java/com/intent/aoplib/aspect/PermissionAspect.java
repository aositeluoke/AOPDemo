package com.intent.aoplib.aspect;

import android.content.Context;

import com.intent.aoplib.IPermission;
import com.intent.aoplib.PermissionActivity;
import com.intent.aoplib.annotation.CheckPermission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年03月29日
 * 参考链接：https://github.com/crazyqiang/Aopermission
 * 博客:https://blog.csdn.net/u013700502/article/details/79748829
 * AspectJ配置教程
 * https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx
 *
 * Spring AspectJ切入点语法详解
 * https://www.cnblogs.com/caoyc/p/5629507.html
 *
 * AOP应用场景
 * https://juejin.im/post/5c01533de51d451b80257752#heading-17
 */
@Aspect
public class PermissionAspect {

    private static final String TAG = "PermissionAspect";

    @Pointcut("execution(@com.intent.aoplib.annotation.CheckPermission * *(..))&&@annotation(permissions)")
    public void checkPermission(CheckPermission permissions) {
    }

    @Around("checkPermission(permissions)")
    public Object checkPermissionAround(final ProceedingJoinPoint point, CheckPermission permissions) {
        Context context = (Context) point.getThis();
        PermissionActivity.requestPermission(context, permissions.permissions(), new IPermission() {
            @Override
            public void onSuccess() {
                try {
                    point.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        return null;
    }
}
