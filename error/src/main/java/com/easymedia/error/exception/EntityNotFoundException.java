package com.easymedia.error.exception;

import com.easymedia.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String title) {
        super(String.format("%s 찾을 수 없습니다.", title), ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String title, String id) {
        super(String.format("%s 찾을 수 없습니다.[%s]", title, id), ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String title, Long id) {
        super(String.format("%s 찾을 수 없습니다.[%s]", title, id), ErrorCode.ENTITY_NOT_FOUND);
    }


}
