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

package soo.swallow.log.box;

import soo.swallow.log.Theme;

/**
 * Created by Joseph.yan
 */
public class DefaultTheme implements Theme {

    private static final char CHAR_HORIZONTAL = '═';
    private static final char CHAR_VERTICAL = '║';
    private static final char CHAR_LT = '╔';
    private static final char CHAR_LB = '╚';
    private static final char CHAR_RT = '╗';
    private static final char CHAR_RB = '╝';
    private static final char CHAR_DIVIDER = '─';


    @Override
    public char getHorizontalChar() {
        return CHAR_HORIZONTAL;
    }

    @Override
    public char getVerticalChar() {
        return CHAR_VERTICAL;
    }

    @Override
    public char getLTChar() {
        return CHAR_LT;
    }

    @Override
    public char getLBChar() {
        return CHAR_LB;
    }

    @Override
    public char getRTChar() {
        return CHAR_RT;
    }

    @Override
    public char getRBChar() {
        return CHAR_RB;
    }

    @Override
    public char getDividerChar() {
        return CHAR_DIVIDER;
    }
}
