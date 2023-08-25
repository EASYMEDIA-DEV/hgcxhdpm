package com.easymedia.dto.login;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private static final String _EMPTY_PASSWORD = "";

    @Getter
    private int admSeq;

    @Getter
    private String memberId;

    @Getter
    private String memberEmail;

    @Getter
    private String memberPassword;

    @Getter
    private String memberName;

    @Getter
    private long companyId;

    @Getter
    private String companyName;

    @Getter
    private long groupId;

    @Getter
    private String tel;

    @Getter
    private boolean isApi;
    @Getter
    private String companyCd;

    @Getter String loginIp;


    private List<GrantedAuthority> authorities;

    /**
     * 비밀번호변경일시
     */
    private LocalDateTime passwordChangeDateTime;

    /**
     * 비밀번호연장일시
     */
    private LocalDateTime passwordExtendDateTime;

    @Getter
    private LocalDateTime lastLoginDateTime;

    /**
     * 임시비밀번호여부
     */
    private boolean isTemporaryPassword;

    /**
     * 로그인실패횟수
     */
    private Integer loginFailCount;

    /**
     * 잠금여부
     */
    private boolean isLock;

    /**
     * 사용여부
     */
    private boolean useYn;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return memberPassword;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLock;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return useYn;
    }

}
