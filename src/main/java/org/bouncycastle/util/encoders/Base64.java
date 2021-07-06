/*
 * Decompiled with CFR 0.150.
 */
package org.bouncycastle.util.encoders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.bouncycastle.util.encoders.Encoder;

public class Base64 {
    private static final Encoder encoder = new Base64Encoder();

    public static String toBase64String(byte[] data) {
        return Base64.toBase64String(data, 0, data.length);
    }

    public static String toBase64String(byte[] data, int off, int length) {
        byte[] encoded = Base64.encode(data, off, length);
        return new String(Base64.asCharArray(encoded));
    }

    public static char[] asCharArray(byte[] bytes) {
        char[] chars = new char[bytes.length];
        for (int i = 0; i != chars.length; ++i) {
            chars[i] = (char)(bytes[i] & 0xFF);
        }
        return chars;
    }

    public static byte[] encode(byte[] data) {
        return Base64.encode(data, 0, data.length);
    }

    public static byte[] encode(byte[] data, int off, int length) {
        int len = (length + 2) / 3 * 4;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
        try {
            encoder.encode(data, off, length, bOut);
        }
        catch (Exception e) {
            throw new RuntimeException("exception encoding base64 string: " + e.getMessage(), e);
        }
        return bOut.toByteArray();
    }

    public static int encode(byte[] data, OutputStream out) throws IOException {
        return encoder.encode(data, 0, data.length, out);
    }

    public static int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
        return encoder.encode(data, off, length, out);
    }

    public static byte[] decode(byte[] data) {
        int len = data.length / 4 * 3;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
        try {
            encoder.decode(data, 0, data.length, bOut);
        }
        catch (Exception e) {
            throw new RuntimeException("unable to decode base64 data: " + e.getMessage(), e);
        }
        return bOut.toByteArray();
    }

    public static byte[] decode(String data) {
        int len = data.length() / 4 * 3;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
        try {
            encoder.decode(data, bOut);
        }
        catch (Exception e) {
            throw new RuntimeException("unable to decode base64 string: " + e.getMessage(), e);
        }
        return bOut.toByteArray();
    }

    public static int decode(String data, OutputStream out) throws IOException {
        return encoder.decode(data, out);
    }
}

