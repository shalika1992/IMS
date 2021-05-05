package com.epic.ims.util.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class Base64Algorithm {

    /**
     * @Author shalika_w
     * @CreatedTime 2020-01-08 02:48:22 PM
     * @Version V1.00
     * @MethodName encodeBase64
     * @MethodParams [input]
     * @MethodDescription - base 64 encode
     */
    public String encodeBase64(String input) {
        try {
            byte[] bytesEncoded = Base64.encodeBase64(input.getBytes());
            return new String(bytesEncoded);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @Author shalika_w
     * @CreatedTime 2020-01-08 02:48:40 PM
     * @Version V1.00
     * @MethodName decodeBase64
     * @MethodParams [input]
     * @MethodDescription - base 64 decode
     */
    public String decodeBase64(String input) {
        try {
            byte[] valueDecoded = Base64.decodeBase64(input.getBytes());
            return new String(valueDecoded);
        } catch (Exception e) {
            throw e;
        }
    }
}
