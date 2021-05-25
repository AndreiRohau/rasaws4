package com.me.util;

import com.me.Rasaws4Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;

public class Util {
    private static Logger log = Logger.getLogger(Util.class.getName());

    public static byte[] inputStreamToBytes(InputStream content) {
        log.info("Util#inputStreamToBytes()");
        try {
            byte[] bytes;
            bytes = new byte[content.available()];
            content.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Byte[] boxIntoByteArray(final byte[] bytes) {
        log.info("Util#boxIntoByteArray()");
        Byte[] byteArray = new Byte[bytes.length];
        Arrays.setAll(byteArray, n -> bytes[n]);
        return byteArray;
    }

    public static String currentPublicIpAddress() {
        log.info("Util#currentPublicIpAddress()");
        try (final BufferedReader br =
                     new BufferedReader(
                             new InputStreamReader(
                                     new URL("http://checkip.amazonaws.com/")
                                             .openStream()));) {
            return br.readLine();
        } catch (IOException e) {
            log.warning("Util#currentPublicIpAddress(). Exception during getting public ip. " + e.getMessage());
            throw new RuntimeException("Exception during getting public ip.", e);
        }
    }
}
