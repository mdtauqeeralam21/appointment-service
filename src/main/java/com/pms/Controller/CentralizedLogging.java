package com.pms.Controller;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Aspect
@Component
@Slf4j
public class CentralizedLogging {

	
	private static final String POINTCUT = "within(com.pms.Controller.*)";

	@Around(POINTCUT)
	public Object logArroundExec(ProceedingJoinPoint pjp) throws Throwable {
		log.info("----->before {}", constructLogMsg(pjp));
		var proceed = pjp.proceed();
		log.info("----->after {}, result: {}", constructLogMsg(pjp), proceed.toString());
		return proceed;
	}

	@AfterThrowing(pointcut = POINTCUT, throwing = "e")
	public void logAfterException(JoinPoint jp, Exception e) {
		log.error("----->Exception during: {} with ex: {}", constructLogMsg(jp), e.toString());
	}

	private String constructLogMsg(JoinPoint jp) {
		var args = Arrays.asList(jp.getArgs()).stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
		Method method = ((MethodSignature) jp.getSignature()).getMethod();
		var sb = new StringBuilder("@");
		sb.append(method.getName());
		sb.append(":");
		sb.append(args);
		return sb.toString();
	}
}


