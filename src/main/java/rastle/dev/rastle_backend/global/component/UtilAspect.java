package rastle.dev.rastle_backend.global.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.global.common.annotation.WriteThroughCache;

import java.lang.reflect.Method;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class UtilAspect {

    private final AsyncCacheComponent asyncCacheComponent;

    @Around("@annotation(rastle.dev.rastle_backend.global.common.annotation.GetExecutionTime)")
    public Object getExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        log.info("{} execution time : {}", joinPoint.getSignature().getName(), (System.currentTimeMillis() - startTime) / 1000.0);

        return proceed;
    }

    @Around("@annotation(rastle.dev.rastle_backend.global.common.annotation.WriteThroughCache)")
    public Object writeThroughCache(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("entering writeThroughCache");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        WriteThroughCache writeThroughCache = method.getAnnotation(WriteThroughCache.class);

        Object proceed = joinPoint.proceed(joinPoint.getArgs());

        asyncCacheComponent.writeThroughCache(writeThroughCache, joinPoint.getArgs(), signature.getParameterNames());
        log.info("leaving writeThroughCache");
        return proceed;
    }
}
