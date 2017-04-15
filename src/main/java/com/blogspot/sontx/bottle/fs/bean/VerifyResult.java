package com.blogspot.sontx.bottle.fs.bean;

import lombok.Data;

@Data
public class VerifyResult {
    private String userId;

    @Override
    public String toString() {
        return userId;
    }
}
