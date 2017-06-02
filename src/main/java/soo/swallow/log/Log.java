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

/**
 * Created by Joseph.yan
 */
public final class Log {

    public static final int FLAG_LOGCAT = 0xf01;
    public static final int FLAG_FILE = 0xf02;
    public static final int FLAG_BOTH = 0xf03;

    private static Logger IMPL;

    public static synchronized void init(Configuration configuration) {
        if (configuration == null) {
            throw new NullPointerException("Configuration must not be null");
        }
        if (IMPL == null) {
            IMPL = new Logger(configuration);
        }
    }

    public static synchronized void release() {
        if (IMPL != null) {
            IMPL.release();
        }
    }

    public static void v(Object obj) {
        v(obj, FLAG_LOGCAT);
    }

    public static void v(Object obj, int flag) {
        v(null, obj, flag);
    }

    public static void v(String tag, Object obj) {
        v(tag, obj, FLAG_LOGCAT);
    }

    public static void v(String tag, Object obj, int flag) {
        log(Level.VERBOSE, tag, obj, flag);
    }

    public static void d(Object obj) {
        d(obj, FLAG_LOGCAT);
    }

    public static void d(Object obj, int flag) {
        d(null, obj, flag);
    }

    public static void d(String tag, Object obj) {
        d(tag, obj, FLAG_LOGCAT);
    }

    public static void d(String tag, Object obj, int flag) {
        log(Level.DEBUG, tag, obj, flag);
    }

    public static void i(Object obj) {
        i(obj, FLAG_LOGCAT);
    }

    public static void i(Object obj, int flag) {
        i(null, obj, flag);
    }

    public static void i(String tag, Object obj) {
        i(tag, obj, FLAG_LOGCAT);
    }

    public static void i(String tag, Object obj, int flag) {
        log(Level.INFO, tag, obj, flag);
    }

    public static void w(Object obj) {
        w(obj, FLAG_LOGCAT);
    }

    public static void w(Object obj, int flag) {
        w(null, obj, flag);
    }

    public static void w(String tag, Object obj) {
        w(tag, obj, FLAG_LOGCAT);
    }

    public static void w(String tag, Object obj, int flag) {
        log(Level.WARN, tag, obj, flag);
    }

    public static void e(Object obj) {
        e(obj, FLAG_LOGCAT);
    }

    public static void e(Object obj, int flag) {
        e(null, obj, flag);
    }

    public static void e(String tag, Object obj) {
        e(tag, obj, FLAG_LOGCAT);
    }

    public static void e(String tag, Object obj, int flag) {
        log(Level.ERROR, tag, obj, flag);
    }

    public static void log(Level Level, String tag, Object obj, int flag) {
        if (IMPL != null) {
            IMPL.log(Level, tag, obj, flag);
        }
    }
}
