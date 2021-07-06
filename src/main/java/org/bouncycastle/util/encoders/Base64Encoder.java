/*
 * Decompiled with CFR 0.150.
 */
package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.util.encoders.Encoder;

public class Base64Encoder
implements Encoder {
    protected final byte[] encodingTable = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    protected byte padding = (byte)61;
    protected final byte[] decodingTable = new byte[128];

    protected void initialiseDecodingTable() {
        int i;
        for (i = 0; i < this.decodingTable.length; ++i) {
            this.decodingTable[i] = -1;
        }
        for (i = 0; i < this.encodingTable.length; ++i) {
            this.decodingTable[this.encodingTable[i]] = (byte)i;
        }
    }

    public Base64Encoder() {
        this.initialiseDecodingTable();
    }

    @Override
    public int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
        int modulus = length % 3;
        int dataLength = length - modulus;
        for (int i = off; i < off + dataLength; i += 3) {
            int a1 = data[i] & 0xFF;
            int a2 = data[i + 1] & 0xFF;
            int a3 = data[i + 2] & 0xFF;
            out.write(this.encodingTable[a1 >>> 2 & 0x3F]);
            out.write(this.encodingTable[(a1 << 4 | a2 >>> 4) & 0x3F]);
            out.write(this.encodingTable[(a2 << 2 | a3 >>> 6) & 0x3F]);
            out.write(this.encodingTable[a3 & 0x3F]);
        }
        switch (modulus) {
            case 0: {
                break;
            }
            case 1: {
                int d1 = data[off + dataLength] & 0xFF;
                int b1 = d1 >>> 2 & 0x3F;
                int b2 = d1 << 4 & 0x3F;
                out.write(this.encodingTable[b1]);
                out.write(this.encodingTable[b2]);
                out.write(this.padding);
                out.write(this.padding);
                break;
            }
            case 2: {
                int d1 = data[off + dataLength] & 0xFF;
                int d2 = data[off + dataLength + 1] & 0xFF;
                int b1 = d1 >>> 2 & 0x3F;
                int b2 = (d1 << 4 | d2 >>> 4) & 0x3F;
                int b3 = d2 << 2 & 0x3F;
                out.write(this.encodingTable[b1]);
                out.write(this.encodingTable[b2]);
                out.write(this.encodingTable[b3]);
                out.write(this.padding);
            }
        }
        return dataLength / 3 * 4 + (modulus == 0 ? 0 : 4);
    }

    private boolean ignore(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    @Override
    public int decode(byte[] data, int off, int length, OutputStream out) throws IOException {
        int end;
        int outLen = 0;
        for (end = off + length; end > off && this.ignore((char)data[end - 1]); --end) {
        }
        int i = off;
        int finish = end - 4;
        i = this.nextI(data, i, finish);
        while (i < finish) {
            byte b4;
            byte b1 = this.decodingTable[data[i++]];
            i = this.nextI(data, i, finish);
            byte b2 = this.decodingTable[data[i++]];
            i = this.nextI(data, i, finish);
            byte b3 = this.decodingTable[data[i++]];
            i = this.nextI(data, i, finish);
            if ((b1 | b2 | b3 | (b4 = this.decodingTable[data[i++]])) < 0) {
                throw new IOException("invalid characters encountered in base64 data");
            }
            out.write(b1 << 2 | b2 >> 4);
            out.write(b2 << 4 | b3 >> 2);
            out.write(b3 << 6 | b4);
            outLen += 3;
            i = this.nextI(data, i, finish);
        }
        return outLen += this.decodeLastBlock(out, (char)data[end - 4], (char)data[end - 3], (char)data[end - 2], (char)data[end - 1]);
    }

    private int nextI(byte[] data, int i, int finish) {
        while (i < finish && this.ignore((char)data[i])) {
            ++i;
        }
        return i;
    }

    @Override
    public int decode(String data, OutputStream out) throws IOException {
        int end;
        int length = 0;
        for (end = data.length(); end > 0 && this.ignore(data.charAt(end - 1)); --end) {
        }
        int i = 0;
        int finish = end - 4;
        i = this.nextI(data, i, finish);
        while (i < finish) {
            byte b4;
            byte b1 = this.decodingTable[data.charAt(i++)];
            i = this.nextI(data, i, finish);
            byte b2 = this.decodingTable[data.charAt(i++)];
            i = this.nextI(data, i, finish);
            byte b3 = this.decodingTable[data.charAt(i++)];
            i = this.nextI(data, i, finish);
            if ((b1 | b2 | b3 | (b4 = this.decodingTable[data.charAt(i++)])) < 0) {
                throw new IOException("invalid characters encountered in base64 data");
            }
            out.write(b1 << 2 | b2 >> 4);
            out.write(b2 << 4 | b3 >> 2);
            out.write(b3 << 6 | b4);
            length += 3;
            i = this.nextI(data, i, finish);
        }
        return length += this.decodeLastBlock(out, data.charAt(end - 4), data.charAt(end - 3), data.charAt(end - 2), data.charAt(end - 1));
    }

    private int decodeLastBlock(OutputStream out, char c1, char c2, char c3, char c4) throws IOException {
        if (c3 == this.padding) {
            byte b1 = this.decodingTable[c1];
            byte b2 = this.decodingTable[c2];
            if ((b1 | b2) < 0) {
                throw new IOException("invalid characters encountered at end of base64 data");
            }
            out.write(b1 << 2 | b2 >> 4);
            return 1;
        }
        if (c4 == this.padding) {
            byte b1 = this.decodingTable[c1];
            byte b2 = this.decodingTable[c2];
            byte b3 = this.decodingTable[c3];
            if ((b1 | b2 | b3) < 0) {
                throw new IOException("invalid characters encountered at end of base64 data");
            }
            out.write(b1 << 2 | b2 >> 4);
            out.write(b2 << 4 | b3 >> 2);
            return 2;
        }
        byte b1 = this.decodingTable[c1];
        byte b2 = this.decodingTable[c2];
        byte b3 = this.decodingTable[c3];
        byte b4 = this.decodingTable[c4];
        if ((b1 | b2 | b3 | b4) < 0) {
            throw new IOException("invalid characters encountered at end of base64 data");
        }
        out.write(b1 << 2 | b2 >> 4);
        out.write(b2 << 4 | b3 >> 2);
        out.write(b3 << 6 | b4);
        return 3;
    }

    private int nextI(String data, int i, int finish) {
        while (i < finish && this.ignore(data.charAt(i))) {
            ++i;
        }
        return i;
    }
}

