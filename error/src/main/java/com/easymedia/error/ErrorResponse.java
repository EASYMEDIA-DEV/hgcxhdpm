package com.easymedia.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(title = "오류 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    @Schema(title = "오류 메시지", example = "입력값이 유효하지 않습니다.")
    private String message;
    @Schema(title = "오류 상세 내용")
    private List<FieldError> errors;
    @Schema(title = "오류 코드", example = "I001")
    private String code;


    private ErrorResponse(final String code, final String message) {
        this.code = code;
        this.message = message;
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.errors = errors;
        this.code = code.getCode();
    }

    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }


    private ErrorResponse(ErrorCode code, String message) {
        this.code = code.getCode();
        this.message = StringUtils.defaultIfBlank(message, code.getMessage());
    }

    public static ErrorResponse of(final String code, final String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code, final Set<ConstraintViolation<?>> constraintViolations) {
        return new ErrorResponse(code, FieldError.of(constraintViolations));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }
    @Schema(title = "오류 상세 응답")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        @Schema(title = "오류 상세 필드", example = "cntn")
        private String field;
        @Schema(title = "오류 상세 데이터")
        private String value;
        @Schema(title = "오류 상세 설명", example = "내용은 필수 값입니다.")
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        private static List<FieldError> of(final Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new FieldError(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue() == null ? "" : constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()))
                    .collect(Collectors.toList());
        }
    }
}
