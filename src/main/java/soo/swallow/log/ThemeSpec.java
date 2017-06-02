/*
 *
 *  * Copyright 2015 Soo [154014022@qq.com | sootracker@gmail.com]
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package soo.swallow.log;

import android.text.TextUtils;

/**
 * Created by Joseph.yan
 */
class ThemeSpec {

    private final char ltChar;
    private final char lbChar;
    private final char rtChar;
    private final char rbChar;
    private final char horizontalChar;
    private final char verticalChar;
    private final char dividerChar;

    ThemeSpec(Theme theme) {
        this.ltChar = theme.getLTChar();
        this.lbChar = theme.getLBChar();
        this.rtChar = theme.getRTChar();
        this.rbChar = theme.getRBChar();
        this.horizontalChar = theme.getHorizontalChar();
        this.verticalChar = theme.getVerticalChar();
        this.dividerChar = theme.getDividerChar();
    }

    public String getHorizontalString() {
        return String.valueOf(horizontalChar);
    }

    public String getVerticalString() {
        return String.valueOf(verticalChar);
    }

    String getTopBoundary(int size) {
        StringBuilder sb = new StringBuilder();
        sb.append(ltChar);
        for (int i = 0; i < size; i++) {
            sb.append(horizontalChar);
        }
        sb.append(rtChar);
        return sb.toString();
    }

    String getBottomBoundary(int size) {
        StringBuilder sb = new StringBuilder();
        sb.append(lbChar);
        for (int i = 0; i < size; i++) {
            sb.append(horizontalChar);
        }
        sb.append(rbChar);
        return sb.toString();
    }

    String getDividerString(int size) {
        StringBuilder sb = new StringBuilder();
        sb.append(verticalChar);
        for (int i = 0; i < size; i++) {
            sb.append(dividerChar);
        }
        sb.append(verticalChar);
        return sb.toString();
    }

    String formatMessage(String message, int maxSize) {
        StringBuilder sb = new StringBuilder();
        sb.append(verticalChar);
        if (TextUtils.isEmpty(message)) {
            for (int i = 0; i < maxSize; i++) {
                sb.append(" ");
            }
        } else {
            int length = message.length();
            int tempSize = maxSize - length;
            if (tempSize < 0) {
                throw new IllegalArgumentException("Message length out of bounds (" + maxSize + "0)");
            }
            sb.append(message);
            for (int i = 0; i < tempSize; i++) {
                sb.append(" ");
            }
        }
        sb.append(verticalChar);

        return sb.toString();
    }
}
