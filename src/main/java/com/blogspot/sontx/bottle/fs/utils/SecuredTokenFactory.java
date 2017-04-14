package com.blogspot.sontx.bottle.fs.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public final class SecuredTokenFactory {

    public static String generateSecuredToken() {
        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);
        return token;
    }

    private SecuredTokenFactory() {
    }
}
