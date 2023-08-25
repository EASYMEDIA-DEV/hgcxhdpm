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
@Schema(title = "오류")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorDto {
    @Schema(title = "구분")
    private String type;
    @Schema(title = "메시지")
    private String message;
    @Schema(title = "에러")
    private List<ApiErrorDto> errors;
    @Schema(title = "필드")
    private List<ApiErrorFieldDto> fields;
}
