
package com.gyjian.logall.aspect;

import com.google.gson.Gson;
import com.gyjian.logall.Tools.IPHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;


/**
 * @author mike
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private Gson gson;

	private static Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

	@Around("@annotation(sysLog)")
	public Object around(ProceedingJoinPoint joinPoint, com.gyjian.logall.annotation.SysLog sysLog) throws Throwable {
		long beginTime = Calendar.getInstance().getTimeInMillis();
		//执行方法
		Object result = joinPoint.proceed();
		//执行时长(毫秒)
		long time = Calendar.getInstance().getTimeInMillis() - beginTime;
		System.out.println("执行时间(ms): " + time);

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		System.out.println("类名: " + className + "." + methodName);

		//请求的参数
		Object[] args = joinPoint.getArgs();
		String params = gson.toJson(args[0]);
		System.out.println("参数: " + params);

		//设置IP地址
		String requestIP = IPHelper.getIpAddr();
		System.out.println("请求IP: " + requestIP);

		//用户名，从 spring security 里获取
		// String username = SecurityUtils.getSysUser().getUsername();


		return result;
	}

}
