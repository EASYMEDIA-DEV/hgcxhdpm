package com.easymedia.api.aop;

import com.easymedia.dto.EmfMap;
import com.easymedia.enums.log.SystemLogType;
import com.easymedia.service.COFSysLogService;
import com.easymedia.service.EgovUserDetailsHelper;
import com.easymedia.utility.EgovStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * <pre>
 * API 로그 관련 처리
 * </pre>
 *
 * @ClassName		    : ApiServiceMethodAspect.java
 * @Description		: API 로그 관련 처리
 * @author 박주석
 * @since 2023.01.20
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since		  author	            description
 *    ==========    ==========    ==============================
 *    2023.01.20	  박주석	             최초 생
 * </pre>
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiServiceMethodAspect {

    //서비스 로그
    private final COFSysLogService cOFSysLogService;
    //서비스 로그 ID
    private final EgovIdGnrService serviceLogIdGnrService;

    /**
     * 로그 읽기
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value= "execution(* com.easymedia..*Impl.select*(..))")
    public Object logRead(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        try
        {
            stopWatch.start();
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            setSystemLog(stopWatch, joinPoint, SystemLogType.READ);
        }
    }

    /**
     * 로그 쓰기
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value= "execution(* com.easymedia..*Impl.insert*(..))")
    public Object logWrite(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            setSystemLog(stopWatch, joinPoint, SystemLogType.WRITE);
        }
    }

    /**
     * 로그 수정
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value= "execution(* com.easymedia..*Impl.update*(..))")
    public Object logModify(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            setSystemLog(stopWatch, joinPoint, SystemLogType.MODIFY);
        }
    }

    /**
     * 로그 삭제
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value= "execution(* com.easymedia..*Impl.delete*(..))")
    public Object logDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            setSystemLog(stopWatch, joinPoint, SystemLogType.DELETE);
        }
    }

    /**
     * 로그인
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value= "execution(* com.easymedia..*Impl.loginMember*(..))")
    public Object loginMember(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            setSystemLog(stopWatch, joinPoint, SystemLogType.LI);
        }
    }

    /**
     * 로그아웃
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value= "execution(* com.easymedia..*Impl.logoutMember*(..))")
    public Object logoutMember(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throw e;
        } finally {
            setSystemLog(stopWatch, joinPoint, SystemLogType.LO);
        }
    }

    /**
     * 스탑워치 종료
     * @param stopWatch
     * @param joinPoint
     * @param logType
     */
    private void setSystemLog(StopWatch stopWatch, ProceedingJoinPoint joinPoint, SystemLogType logType) throws Throwable {
        stopWatch.stop();
        String srvcNm = joinPoint.getTarget().getClass().getName();
        String fncNm = joinPoint.getSignature().getName();
        String trgtMenuNm = String.valueOf(RequestContextHolder.getRequestAttributes().getAttribute("pageIndicator", RequestAttributes.SCOPE_SESSION));
        String prcsTime = Long.toString(stopWatch.getTotalTimeMillis());
        String reqnId = "";
        String reqnIp = "";
        String prcsCd = logType.getCode();

        /* Authenticated */
        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

        if (isAuthenticated.booleanValue())
        {
            EmfMap lgnMap = (EmfMap)EgovUserDetailsHelper.getAuthenticatedUser();

            reqnId = lgnMap.getString("id");
            reqnIp = lgnMap.getString("loginIp");
        }

        EmfMap sysLogMap = new EmfMap();

        sysLogMap.put("srvcNm", srvcNm);
        sysLogMap.put("fncNm", fncNm);
        sysLogMap.put("trgtMenuNm", trgtMenuNm);
        sysLogMap.put("prcsCd", prcsCd);
        sysLogMap.put("prcsTime", prcsTime);
        sysLogMap.put("reqnId", reqnId);
        sysLogMap.put("reqnIp", reqnIp);

        if (prcsCd.equals("S"))
        {
            cOFSysLogService.logInsertSysLog(sysLogMap);
        }
        else
        {
            if (!"".equals(EgovStringUtil.nullConvert(trgtMenuNm)))
            {
                cOFSysLogService.logInsertSysLog(sysLogMap);
            }
        }
        cOFSysLogService.logInsertSysLog(sysLogMap);
    }

}
