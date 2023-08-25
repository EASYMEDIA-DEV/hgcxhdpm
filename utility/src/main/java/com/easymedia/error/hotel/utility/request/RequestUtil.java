package com.easymedia.error.hotel.utility.request;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtil {
    static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


    public static Optional<UserDetails> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        Object principalObj = authentication.getPrincipal();
        if (!(principalObj instanceof UserDetails)) {
            return Optional.empty();
        }
        return Optional.of((UserDetails) principalObj);
    }

    public static String getUserId() {
        return getUserId(null);
    }

    public static String getUserId(String defaultUserId) {
        Optional<UserDetails> user = getUser();
        return user.isPresent() ? (user.get().getUsername() != null ? user.get().getUsername()  : defaultUserId) : defaultUserId;
    }

    public static String getRequestIp() {
        HttpServletRequest request;
        try {
            request = getRequest();
        } catch (Exception e) {
            return "0.0.0.0";
        }
        List<String> ipHeaders = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");
        String requestIp = "";

        for (String ipHeader : ipHeaders) {
            if (!isUnknown(requestIp)) {
                break;
            }
            requestIp = removeCRLF(request.getHeader(ipHeader));
        }

        if (isUnknown(requestIp)) {
            requestIp = removeCRLF(request.getRemoteAddr());
        }

        if (StringUtils.isBlank(requestIp)) {
            return "0.0.0.0";
        }

        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(requestIp);
        if (!matcher.find()) {
            return "0.0.0.0";
        }

        return requestIp;
    }

    @SneakyThrows
    public static String getServerIp() {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Request 정보 조회
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    private static boolean isUnknown(String requestIp) {
        return StringUtils.isBlank(requestIp) || StringUtils.equalsIgnoreCase(requestIp, "unknown");
    }

    private static String removeCRLF(String str) {
        return StringUtils.replaceEach(str, new String[]{"\r", "\n"}, new String[]{"", ""});
    }
}
