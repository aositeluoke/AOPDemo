package com.intent.aoplib.aspect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.intent.aoplib.R;
import com.intent.aoplib.annotation.CheckNet;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年03月28日
 */
@Aspect
public class CheckAspect {
    private static final String TAG = "CheckAspect";

    private static final String CHECK_NET_SIGN = "execution(@com.intent.aoplib.annotation.CheckNet * *(..))&&@annotation(checkNet)";

    @Pointcut(CHECK_NET_SIGN)
    public void checkNetPointcut(CheckNet checkNet) {}


    @Around("checkNetPointcut(checkNet)")
    public Object checkNet(ProceedingJoinPoint joinPoint,CheckNet checkNet) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckNet net = signature.getMethod().getAnnotation(CheckNet.class);

        Context context = null;
        try {
            context = ((Context) joinPoint.getThis());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "checkNet: " + e.getMessage());
            return null;
        }

        if (net != null) {
            if (!isNetworkAvailable(context)) {
                Toast.makeText(context, R.string.check_net, Toast.LENGTH_LONG).show();
                return null;
            }
        }
        return joinPoint.proceed();
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cnn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cnn != null) {
            NetworkInfo[] net = cnn.getAllNetworkInfo();
            if (net != null && net.length > 0) {
                for (NetworkInfo networkInfo : net) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

}
