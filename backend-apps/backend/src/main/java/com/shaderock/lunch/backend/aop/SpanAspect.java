package com.shaderock.lunch.backend.aop;


import brave.Span;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SpanAspect {
  private final Tracer tracer;

  @Around("within(@org.springframework.web.bind.annotation.RestController *)")
  public Object traceController(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String methodName = signature.getMethod().getName();
    String controllerName = joinPoint.getTarget().getClass().getSimpleName();
    Span span = tracer.nextSpan().name(controllerName + "." + methodName).start();
    try (Tracer.SpanInScope ignored = tracer.withSpanInScope(span)) {
      return joinPoint.proceed();
    } finally {
      span.finish();
    }
  }
}
