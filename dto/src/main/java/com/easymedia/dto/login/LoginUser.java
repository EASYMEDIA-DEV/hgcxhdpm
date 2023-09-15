package com.easymedia.dto.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Getter
    @Schema(title="관리자 순번")
    private int admSeq;
    @Getter
    @Schema(title="회원 ID")
    private String id;
    @Getter
    @Schema(title="이름")
    private String name;
    @Getter
    @Schema(title="비밀번호",description = "임시 비밀번호 변경시만 사용")
    @JsonIgnore
    private String password;
    @Getter
    @Schema(title="국가 코드")
    private String natnCd;
    @Getter
    @Schema(title="대리점 코드")
    private String dlspCd;
    @Getter
    @Schema(title="딜러 코드")
    private String dlrCd;
    @Getter
    @Schema(title="딜러 코드 리스트")
    private List<String> dlrCdList;
    @Getter
    private String loginIp;
    @Getter
    @Schema(title="담당 업무 코드")
    private String asgnTaskCd;
    /** 2023.09.15 **/
    @Getter
    @Schema(title="권한 코드")
    private String authCd;
    @Getter
    @Schema(title="rsc 사용 여부")
    private String rscUseYn;
    @Getter
    @Schema(title="mys 사용 여부")
    private String mysUseYn;
    @Getter
    @Schema(title="kpi 사용 여부")
    private String kpiUseYn;
    /**
     * 임시비밀번호여부
     */
    @Getter
    @Builder.Default
    private boolean isTemporaryPassword = false;

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

    private List<GrantedAuthority> authorities;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
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
