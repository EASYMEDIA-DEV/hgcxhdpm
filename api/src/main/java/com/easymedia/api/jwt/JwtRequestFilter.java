package com.easymedia.api.jwt;

import com.easymedia.dto.login.LoginUser;
import com.easymedia.enums.menu.Site;
import com.easymedia.error.ErrorCode;
import com.easymedia.error.ErrorResponse;
import com.easymedia.jwt.JwtTokenProvider;
import com.easymedia.property.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtProperties _jwtProperties;
    private final JwtTokenProvider _jwtTokenProvider;
    private final Site site;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰을 가져온다. 쿠키에 없을경우 헤더에서 가져온다.
        String header = StringUtils.defaultIfBlank(_jwtTokenProvider.resolveCookie(request, site), request.getHeader(_jwtProperties.getTokenHeader() + "_" + site.name()));
        if (isBlank(header) || !header.startsWith(_jwtProperties.getTokenPrefix())) {
            setErrorResponse(response, ErrorCode.ACCESS_DENIED);
            return;
        }
        //getAuthentication -> jwt 토큰 만료일 경우 throw new ExpiredJwtException -> catch 처리
        try
        {
            Authentication authentication = _jwtTokenProvider.getAuthentication(header);

            if (authentication == null) {
                setErrorResponse(response, ErrorCode.ACCESS_DENIED);
                return;
            }
            Object principalObj = authentication.getPrincipal();
            if (!(principalObj instanceof LoginUser)) {
                setErrorResponse(response, ErrorCode.ACCESS_DENIED);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            /*
            //2023.06.05
            //1.jwt 토큰만료시간(Expiration) 보다 쿠기 삭제시간(CookieMaxAge)을 더 짧게 설정해야된다.
            //2.그 차이만큼 토큰재생성주기로 설정
            //3.토큰재생성주기보다 짦으면 기존토큰값으로 보냄 아니면 재생성하여 토큰 리셋
            String token = header.substring(_jwtProperties.getTokenPrefix().length()); //Bearer 삭제
            Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            Claims claims = parsedToken.getBody();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(claims.getExpiration());
            calendar.add(Calendar.SECOND, (-1) * Long.valueOf(_jwtTokenProvider.getExpiration() / 1000).intValue());
            Date newDate = calendar.getTime();
            //_jwtTokenProvider.getAuthentication에서 validateToken을 처리함.
            //토큰은 30분인데...토큰 생성한지 jwt.pro에서 시간 확인 후 갱신처리?
            if (!_jwtTokenProvider.validateToken(newDate)) {
                log.error("token refresh");
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                token = _jwtProperties.getTokenPrefix() + _jwtTokenProvider.createToken(loginUser);
                response.addHeader(_jwtProperties.getTokenHeader() + "_" + site.name(), token);
            }
            _jwtTokenProvider.createCookie(response, token, Site.MNGWSERC.name());
            */
        }  catch (ExpiredJwtException ex) {
                setErrorResponse(response, ErrorCode.EXPIRED_TIME);
                return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Collection<String> excludeUrlPatterns = new LinkedHashSet<>();
        return excludeUrlPatterns.stream().anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));
    }
    private byte[] getSigningKey() {
        return _jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * API 권한 없음 출력
     * @param response
     * @param errorCode
     */
    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
