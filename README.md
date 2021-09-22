## 使用 AspectJ 统一操作日志

原理：aspectj 利用了 `spring`框架中`aop` 

##### 添加依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

##### 自定义注解

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}

```

##### 配置切面

```java
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
```

将自定义的注解作为切入点，参数是`ProceedingJoinPoint`和`sysLog`，`ProceedingJoinPoint`用来获取当前执行的方法，`syslog`用来获取注解里面的值。

#### 在需要记录日志的方法上，添加注解`@SysLog(value)`

```java
@SysLog("获取什么信息")
@GetMapping("/test")
public String getByPid(@RequestParam("pid") Long pid){
	return "OK";
}
```

当操作这个方法时，会打印相应的日志。例如 `wget http://localhost:8080/p/test?pid=123`   

后续可以记录到数据库中。

