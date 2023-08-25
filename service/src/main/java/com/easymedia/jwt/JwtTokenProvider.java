package com.easymedia.jwt;

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
    private static final String _MEMBER_ID = "memberId";
    private static final String _MEMBER_EMAIL = "memberEmail";
    private static final String _MEMBER_NAME = "memberName";
    private static final String _AUTHORITIES = "authorities";
    private static final String _LAST_LOGIN_DATE_TIME = "lastLoginDateTime";
    private static final SignatureAlgorithm _SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String _EMPTY_CREDENTIALS = "";

    public String createToken(String memberId, String memberEmail, String memberName, String lastLoginDateTime) {
        Instant now = new Date().toInstant();
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(getSigningKey()), _SIGNATURE_ALGORITHM)
                .setHeaderParam(_TOKEN_TYPE, _jwtProperties.getTokenType())
                .setExpiration(new Date(System.currentTimeMillis() + _jwtProperties.getExpiration()))
                .setIssuedAt(Date.from(now))
                .claim(_MEMBER_ID, memberId)
                .claim(_MEMBER_EMAIL, memberEmail)
                .claim(_MEMBER_NAME, memberName)
                .claim(_AUTHORITIES, null)
                .claim(_LAST_LOGIN_DATE_TIME, lastLoginDateTime)
                .compact();
        return token;
    }

    public void createCookie(HttpServletResponse response, String token, Site site) {
        ResponseCookie cookie = ResponseCookie.from(_jwtProperties.getTokenHeader() + "_" + site.name(), StringUtils.replace(token, " ", "%20"))
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
            String memberEmail = claims.get(_MEMBER_EMAIL, String.class);
            String memberName = claims.get(_MEMBER_NAME, String.class);
            String lastLoginDateTime = claims.get(_LAST_LOGIN_DATE_TIME, String.class);
            if (isNotBlank(memberEmail)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                LocalDateTime localDateTime = LocalDateTime.parse(lastLoginDateTime, formatter);
                LoginUser loginUser = LoginUser.builder()
                        .memberId(memberId)
                        .memberEmail(memberEmail)
                        .memberName(memberName)
                        .authorities(null)
                        .lastLoginDateTime(localDateTime)
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
