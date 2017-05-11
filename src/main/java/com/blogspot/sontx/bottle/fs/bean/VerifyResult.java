package com.blogspot.sontx.bottle.fs.bean;


import lombok.Getter;
import lombok.Setter;

public class VerifyResult {
    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMIN = 1;

    @Getter
    @Setter
    private String userId;
    @Getter
    @Setter
    private int role;
}
