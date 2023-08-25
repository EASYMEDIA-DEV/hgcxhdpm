package com.easymedia.api.error;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Hidden
@Getter
@ToString
@Schema(title = "오류 필드")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorFieldDto {
    @Schema(title = "필드명")
    private String name;
    @Schema(title = "필드구분")
    private String type;
}
