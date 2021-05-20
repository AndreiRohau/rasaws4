package com.me.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Util {

    public static Byte[] inputStreamToByteArray(InputStream content) {
        return boxIntoByteArray(inputStreamToBytes(content));
    }

    public static byte[] inputStreamToBytes(InputStream content) {
        try {
            byte[] bytes;
            bytes = new byte[content.available()];
            content.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Byte[] boxIntoByteArray(final byte[] bytes) {
        Byte[] byteArray = new Byte[bytes.length];
        Arrays.setAll(byteArray, n -> bytes[n]);
        return byteArray;
    }
}
