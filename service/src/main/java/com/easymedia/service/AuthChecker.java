package com.easymedia.service;

import com.easymedia.dto.login.LoginUser;
import com.easymedia.error.ErrorCode;
import com.easymedia.error.exception.BusinessException;
import com.easymedia.error.hotel.utility.request.RequestUtil;


/**
 * <pre>
 * 권한 체크
 * </pre>
 *
 * @ClassName		    : AuthChecker.java
 * @Description		: 권한 체크
 * @author 박주석
 * @since 2023.01.20
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since		  author	            description
 *    ==========    ==========    ==============================
 *    2023.01.20	  박주석	             최초 생성
 * </pre>
 */
public class AuthChecker {
    /**
     * 유저 조회
     * @return
     * @throws BusinessException
     */
    public static LoginUser getLoginUser() throws BusinessException {
        return (LoginUser) RequestUtil.getUser().orElseThrow(() -> new BusinessException("권한이 없습니다.", ErrorCode.ACCESS_DENIED));
    }
}
