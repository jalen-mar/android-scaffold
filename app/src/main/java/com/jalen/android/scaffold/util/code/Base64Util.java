package com.jalen.android.scaffold.util.code;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Base64Util {
    private static Base64Util base64 = new Base64Util();

    private final int     BASELENGTH           = 128;
    private final int     LOOKUPLENGTH         = 64;
    private final int     TWENTYFOURBITGROUP   = 24;
    private final int     EIGHTBIT             = 8;
    private final int     SIXTEENBIT           = 16;
    private final int     FOURBYTE             = 4;
    private final int     SIGN                 = -128;
    private final char    PAD                  = '=';
    final private byte[]  base64Alphabet       = new byte[BASELENGTH];
    final private char[]  lookUpBase64Alphabet = new char[LOOKUPLENGTH];

    private Base64Util() {
        for (int i = 0; i < BASELENGTH; ++i) {
            if (i >= '0' && i <= '9') {
                base64Alphabet[i] = (byte) (i - '0' + 52);
            }else if (i >= 'A' && i <= 'Z') {
                base64Alphabet[i] = (byte) (i - 'A');
            } else if (i >= 'a' && i <= 'z'){
                base64Alphabet[i] = (byte) (i - 'a' + 26);
            } else {
                base64Alphabet[i] = -1;
            }

            if (i <= 25) {
                lookUpBase64Alphabet[i] = (char) ('A' + i);
            } else if (i <= 51) {
                lookUpBase64Alphabet[i] = (char) ('a' + i - 26);
            } else  if (i < LOOKUPLENGTH){
                lookUpBase64Alphabet[i] = (char) ('0' + i - 52);
            }
        }
        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;
        lookUpBase64Alphabet[62] = (char) '+';
        lookUpBase64Alphabet[63] = (char) '/';
    }

    public static String encode(String message) {
        return encode(message, Charset.defaultCharset());
    }

    public static String encode(String message, Charset charset) {
        return encode(message.getBytes(charset));
    }

    public static String encode(byte[] message) {
        if (message == null) {
            return null;
        }

        int lengthDataBits = message.length * base64.EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % base64.TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / base64.TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;

        char[] encodedData = new char[numberQuartet * 4];

        byte k, l, b1, b2, b3;

        int encodedIndex = 0;
        int dataIndex = 0;
        for (int i = 0; i < numberTriplets; i++) {
            b1 = message[dataIndex++];
            b2 = message[dataIndex++];
            b3 = message[dataIndex++];

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & base64.SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & base64.SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & base64.SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[b3 & 0x3f];
        }

        if (fewerThan24bits == base64.EIGHTBIT) {
            b1 = message[dataIndex];
            k = (byte) (b1 & 0x03);
            byte val1 = ((b1 & base64.SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = base64.PAD;
            encodedData[encodedIndex++] = base64.PAD;
        } else if (fewerThan24bits == base64.SIXTEENBIT) {
            b1 = message[dataIndex];
            b2 = message[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & base64.SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & base64.SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = base64.lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex++] = base64.PAD;
        }
        return new String(encodedData);
    }


    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        char[] base64Data = encoded.toCharArray();
        int len = base64.removeWhiteSpace(base64Data);

        if (len % base64.FOURBYTE != 0) {
            return null;//should be divisible by four
        }

        int numberQuadruple = (len / base64.FOURBYTE);

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte decodedData[];
        byte b1, b2, b3, b4;
        char d1, d2, d3, d4;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[(numberQuadruple) * 3];

        for (; i < numberQuadruple - 1; i++) {

            if (!base64.isData((d1 = base64Data[dataIndex++])) || !base64.isData((d2 = base64Data[dataIndex++]))
                    || !base64.isData((d3 = base64Data[dataIndex++]))
                    || !base64.isData((d4 = base64Data[dataIndex++]))) {
                return null;
            }//if found "no data" just return null

            b1 = base64.base64Alphabet[d1];
            b2 = base64.base64Alphabet[d2];
            b3 = base64.base64Alphabet[d3];
            b4 = base64.base64Alphabet[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!base64.isData((d1 = base64Data[dataIndex++])) || !base64.isData((d2 = base64Data[dataIndex++]))) {
            return null;//if found "no data" just return null
        }

        b1 = base64.base64Alphabet[d1];
        b2 = base64.base64Alphabet[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!base64.isData((d3)) || !base64.isData((d4))) {//Check if they are PAD characters
            if (base64.isPad(d3) && base64.isPad(d4)) {
                if ((b2 & 0xf) != 0)//last 4 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!base64.isPad(d3) && base64.isPad(d4)) {
                b3 = base64.base64Alphabet[d3];
                if ((b3 & 0x3) != 0)//last 2 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else { //No PAD e.g 3cQl
            b3 = base64.base64Alphabet[d3];
            b4 = base64.base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

        }

        return decodedData;
    }

    private boolean isPad(char octect) {
        return (octect == PAD);
    }

    private boolean isData(char octect) {
        return (octect < BASELENGTH && base64Alphabet[octect] != -1);
    }

    private int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }

    private boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }
}
