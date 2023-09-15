package com.easymedia.jwt;

import com.easymedia.dto.EmfMap;
import com.easymedia.dto.login.LoginUser;
import com.easymedia.enums.menu.Site;
import com.easymedia.property.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JwtTokenProvider {
    private final JwtProperties _jwtProperties;

    private static final String _TOKEN_TYPE = "type";
    private static final String _MEMBER_SEQ = "seq";
    private static final String _MEMBER_ID = "id";
    private static final String _MEMBER_NAME = "name";
    private static final String _MEMBER_PWD = "pwd";
    private static final String _MEMBER_NATN_CD = "natnCd";
    private static final String _MEMBER_DLSP_CD = "dlspCd";
    private static final String _MEMBER_DLR_CD = "dlrCd";
    private static final String _MEMBER_DLR_CD_LIST = "dlrCdList";
    private static final String _MEMBER_ASGN_TACK_CD = "asgnTaskCd";
    private static final String _MEMBER_AUTH_CD = "authCd";
    private static final String _MEMBER_RSC_USE_YN = "rscUseYn";
    private static final String _MEMBER_MYS_USE_YN = "mysUseYn";
    private static final String _MEMBER_KPI_USE_YN = "kpiUseYn";
    private static final String _AUTHORITIES = "authorities";
    private static final SignatureAlgorithm _SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String _EMPTY_CREDENTIALS = "";

    /**
     * 전달받은 로그인 정보로 TOKEN 생성
     * @param loginUser
     * @return
     */
    public String createToken(EmfMap loginUser) {
        Instant now = new Date().toInstant();
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(getSigningKey()), _SIGNATURE_ALGORITHM)
                .setHeaderParam(_TOKEN_TYPE, _jwtProperties.getTokenType())
                .setExpiration(new Date(System.currentTimeMillis() + _jwtProperties.getExpiration()))
                .setIssuedAt(Date.from(now))
                .claim(_AUTHORITIES, null)
                .claim(_MEMBER_SEQ, loginUser.getString("admSeq"))
                .claim(_MEMBER_ID, loginUser.getString("id"))
                .claim(_MEMBER_NAME, loginUser.getString("name"))
                .claim(_MEMBER_NATN_CD, loginUser.getString("ntnCd"))
                .claim(_MEMBER_DLSP_CD, loginUser.getString("dlspCd"))
                .claim(_MEMBER_DLR_CD, loginUser.getString("dlrCd"))
                .claim(_MEMBER_DLR_CD_LIST, loginUser.getList("dlrCdList"))
                .claim(_MEMBER_ASGN_TACK_CD, loginUser.getString("asgnTaskCd"))
                .claim(_MEMBER_AUTH_CD, loginUser.getString("authCd"))
                .claim(_MEMBER_RSC_USE_YN, loginUser.getString("rscUseYn"))
                .claim(_MEMBER_MYS_USE_YN, loginUser.getString("mysUseYn"))
                .claim(_MEMBER_KPI_USE_YN, loginUser.getString("kpiUseYn"))
                .compact();
        return token;
    }

    /**
     * 비밀번호 변경 임시 저장
     * @param loginUser
     * @return
     */
    public String createTempToken(LoginUser loginUser) {
        Instant now = new Date().toInstant();
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(getSigningKey()), _SIGNATURE_ALGORITHM)
                .setHeaderParam(_TOKEN_TYPE, _jwtProperties.getTokenType())
                .setExpiration(new Date(System.currentTimeMillis() + (60*1000*60)))
                .setIssuedAt(Date.from(now))
                .claim(_AUTHORITIES, null)
                .claim(_MEMBER_SEQ, loginUser.getAdmSeq())
                .claim(_MEMBER_ID, loginUser.getId())
                .claim(_MEMBER_PWD, loginUser.getPassword())
                .compact();
        return token;
    }

    /**
     * 임시 변경 토큰 파싱
     * @param tokenHeader
     * @return
     */
    public UsernamePasswordAuthenticationToken getTempAuthentication(String tokenHeader) {
        if (isBlank(tokenHeader)) return null;
        try
        {
            String token = tokenHeader; //Bearer 삭제

            Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            Claims claims = parsedToken.getBody();
            log.error("claims : {}", parsedToken.getBody().toString());
            if (validateToken(claims.getExpiration()) == false) return null;
            String memberId = claims.get(_MEMBER_ID, String.class);
            if (isNotBlank(memberId)) {
                LoginUser loginUser = LoginUser.builder()
                        .admSeq(claims.get(_MEMBER_SEQ, Integer.class))
                        .id(claims.get(_MEMBER_ID, String.class))
                        .password(claims.get(_MEMBER_PWD, String.class))
                        .authorities(null)
                        .build();
                return new UsernamePasswordAuthenticationToken(loginUser, _EMPTY_CREDENTIALS, loginUser.getAuthorities());
            }

        } catch (ExpiredJwtException ex) {
            throw ex;
//            log.warn(ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn(ex.getMessage());
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }

        return null;
    }

    /**
     * 토큰 쿠키 전달
     * @param response
     * @param token
     * @param site
     */
    public void createCookie(HttpServletResponse response, String token, String site) {
        ResponseCookie cookie = ResponseCookie.from(_jwtProperties.getTokenHeader() + "_" + site, StringUtils.replace(token, " ", "%20"))
                .httpOnly(true)
                .sameSite("Lax")
                .secure(true)
                .maxAge(_jwtProperties.getCookieMaxAge())
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * 쿠키 삭제
     * @param request
     * @param response
     * @param site
     */
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, Site site) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            String name = _jwtProperties.getTokenHeader() + "_" + site.name();
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    ResponseCookie rcookie = ResponseCookie.from(name, "")
                            .httpOnly(true)
                            .sameSite("Lax")
                            .secure(true)
                            .maxAge(0)
                            .path("/")
                            .build();
                    response.addHeader("Set-Cookie", rcookie.toString());
                }
            }
        }
    }

    public String resolveCookie(HttpServletRequest request, Site site) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(_jwtProperties.getTokenHeader() + "_" + site.name())) {
                return StringUtils.replace(cookie.getValue(), "%20", " ");
            }
        }
        return null;
    }

    /**
     * 전달 받은 토큰 LoginUser 변환
     * @param tokenHeader
     * @return
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        if (isBlank(tokenHeader)) return null;
        try {
            String token = tokenHeader.substring(_jwtProperties.getTokenPrefix().length()); //Bearer 삭제
            Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            Claims claims = parsedToken.getBody();
            if (validateToken(claims.getExpiration()) == false) return null;
            String memberId = claims.get(_MEMBER_ID, String.class);
            if (isNotBlank(memberId)) {
                LoginUser loginUser = LoginUser.builder()
                        .admSeq(Integer.parseInt(claims.get(_MEMBER_SEQ, String.class)))
                        .id(claims.get(_MEMBER_ID, String.class))
                        .name(claims.get(_MEMBER_NAME, String.class))
                        .natnCd(claims.get(_MEMBER_NATN_CD, String.class))
                        .dlspCd(claims.get(_MEMBER_DLSP_CD, String.class))
                        .dlrCd(claims.get(_MEMBER_DLR_CD, String.class))
                        .asgnTaskCd(claims.get(_MEMBER_ASGN_TACK_CD, String.class))
                        .dlrCdList(claims.get(_MEMBER_DLR_CD_LIST, List.class))
                        .authCd(claims.get(_MEMBER_AUTH_CD, String.class))
                        .rscUseYn(claims.get(_MEMBER_RSC_USE_YN, String.class))
                        .mysUseYn(claims.get(_MEMBER_MYS_USE_YN, String.class))
                        .kpiUseYn(claims.get(_MEMBER_KPI_USE_YN, String.class))
                        .authorities(null)
                        .build();
                return new UsernamePasswordAuthenticationToken(loginUser, _EMPTY_CREDENTIALS, loginUser.getAuthorities());
            }

        } catch (ExpiredJwtException ex) {
            throw ex;
//            log.warn(ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn(ex.getMessage());
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }

        return null;
    }

    /**
     * 토큰 검증
     * @param expirationDate
     * @return
     */
    public boolean validateToken(Date expirationDate) {
        try
        {
            return expirationDate.after(new Date());
        } catch (ExpiredJwtException ex) {
            log.error("Token Expired");
            return false;
        } catch (JwtException ex) {
            log.error("Token Exception");
            return false;
        } catch (NullPointerException ex) {
            log.error("Token is null");
            return false;
        } catch (Exception ex) {
            log.error("Exception");
            return false;
        }
    }

    /**
     * 리플레시 토큰 생성
     */
    // jwt refresh 토큰 생성
    public String createRefreshToken() {
        Instant now = new Date().toInstant();
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(getSigningKey()), _SIGNATURE_ALGORITHM)
                .setHeaderParam(_TOKEN_TYPE, _jwtProperties.getTokenType())
                .setExpiration(new Date(System.currentTimeMillis() + _jwtProperties.getExpiration()))
                .setIssuedAt(Date.from(now))
                .compact();
        log.error("refreshToken : {}", token);
        return token;
    }

    /**
     * 토큰 갱신
     * @param expirationDate
     * @return
     */
    public boolean isRefreshToken(Date expirationDate) {
        try
        {
            return expirationDate.after(expirationDate) && expirationDate.before(expirationDate);
        } catch (ExpiredJwtException ex) {
            log.error("Token Expired");
            return false;
        } catch (JwtException ex) {
            log.error("Token Exception");
            return false;
        } catch (NullPointerException ex) {
            log.error("Token is null");
            return false;
        } catch (Exception ex) {
            log.error("Exception");
            return false;
        }
    }

    /**
     * JWT 만료 값
     * @return
     */
    public long getExpiration() {
        return _jwtProperties.getExpiration();
    }
    public int getCookieMaxAge() {
        return _jwtProperties.getCookieMaxAge();
    }
    private byte[] getSigningKey() {
        return _jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
    }
}
