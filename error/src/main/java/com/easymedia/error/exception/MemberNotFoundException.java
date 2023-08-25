package com.easymedia.error.exception;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException() {
        super("사용자를");
    }

    public MemberNotFoundException(String id) {
        super("사용자를", id);
    }

}
