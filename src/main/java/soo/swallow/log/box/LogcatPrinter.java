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

import android.util.Log;

import soo.swallow.log.Level;
import soo.swallow.log.Printer;

/**
 * Created by Joseph.yan
 */
public class LogcatPrinter implements Printer {
    private static final String TAG = "LogcatPrinter--->";

    @Override
    public void print(String tag, Level level, String message, int flag) {
        android.util.Log.println(mapPriority(level), tag, message);
    }

    private static int mapPriority(Level level) {
        switch (level) {
            case VERBOSE: return Log.VERBOSE;
            case DEBUG: return Log.DEBUG;
            case INFO: return Log.INFO;
            case WARN: return Log.WARN;
            case ERROR: return Log.ERROR;
            default: return Log.DEBUG;
        }
    }
}
