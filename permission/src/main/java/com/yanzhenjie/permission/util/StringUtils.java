/*
 * Copyright 2019 Zhenjie Yan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.permission.util;

/**
 * Created Zhenjie Yan on 2019-10-02.
 */
public class StringUtils {

    private static final String DIGITS_TEXT = "0123456789ABCDEF";
    private static final char[] DIGITS_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String textToHex(String text) {
        StringBuilder builder = new StringBuilder();
        byte[] textBytes = text.getBytes();
        int bit;

        for (int i = 0; i < textBytes.length; i++) {
            bit = (textBytes[i] & 0x0f0) >> 4;
            builder.append(DIGITS_ARRAY[bit]);
            bit = textBytes[i] & 0x0f;
            builder.append(DIGITS_ARRAY[bit]);
        }
        return builder.toString().trim();
    }

    public static String hexToText(String hexText) {
        char[] hexArray = hexText.toCharArray();
        byte[] hexBytes = new byte[hexText.length() / 2];
        for (int i = 0; i < hexBytes.length; i++) {
            int n = DIGITS_TEXT.indexOf(hexArray[2 * i]) * 16;
            n += DIGITS_TEXT.indexOf(hexArray[2 * i + 1]);
            hexBytes[i] = (byte) (n & 0xff);
        }
        return new String(hexBytes);
    }
}
