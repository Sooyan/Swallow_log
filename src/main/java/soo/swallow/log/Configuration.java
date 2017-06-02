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

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import soo.swallow.log.box.DefaultLoggable;
import soo.swallow.log.box.DefaultTheme;
import soo.swallow.log.box.FilePrinter;
import soo.swallow.log.box.LogcatPrinter;

/**
 * Created by Joseph.yan
 */
public class Configuration {

    Context context;
    String name;
    boolean enable = true;
    boolean isPrintHeader = true;
    boolean isPrintTrace = true;
    String defaultTag;
    Level defaultLevel;
    int traceMaxSize = 3;

    File workSpace;

    Theme theme;
    Printer filePrinter;
    Printer logcatPrinter;

    Loggable loggable;

    Map<Class<?>, LineFactory> lineFactoryMap = new HashMap<>();

    private Configuration(Context context) {
        this.context = context;
    }

    public static class Builder {

        private static final String DEFAULT_NAME = "SwallowLog";
        private static final String DEFAULT_TAG = "--->";

        private Configuration configuration;

        public Builder(Context context) {
            configuration = new Configuration(context);
        }

        public Builder setName(String name) {
            configuration.name = name;
            return this;
        }

        public Builder setEnable(boolean enable) {
            configuration.enable = enable;
            return this;
        }

        public Builder setWorkSpace(File workSpace) {
            configuration.workSpace = workSpace;
            return this;
        }

        public Builder setPrintHeaderEnable(boolean enable) {
            configuration.isPrintHeader = enable;
            return this;
        }

        public Builder setPrintTradeEnable(boolean enable) {
            configuration.isPrintTrace = enable;
            return this;
        }

        public Builder setTheme(Theme theme) {
            configuration.theme = theme;
            return this;
        }

        public Builder setDefaultTag(String tag) {
            configuration.defaultTag = tag;
            return this;
        }

        public Builder setDefaultLevel(Level level) {
            configuration.defaultLevel = level;
            return this;
        }

        public Builder setLoggable(Loggable loggable) {
            configuration.loggable = loggable;
            return this;
        }

        public Builder setTraceMaxSize(int maxSize) {
            configuration.traceMaxSize = Math.max(1, maxSize);
            return this;
        }

        public <T> Builder addLineFactory(Class<T> tClass, LineFactory<T> lineFactory) {
            configuration.lineFactoryMap.put(tClass, lineFactory);
            return this;
        }

        public Configuration build() {
            notNull(configuration.context, "Context is null");
            if (TextUtils.isEmpty(configuration.name)) {
                configuration.name = DEFAULT_NAME;
            }
            if (TextUtils.isEmpty(configuration.defaultTag)) {
                configuration.defaultTag = DEFAULT_TAG;
            }
            if (configuration.defaultLevel == null) {
                configuration.defaultLevel = Level.DEBUG;
            }
            if (configuration.theme == null) {
                configuration.theme = new DefaultTheme();
            }
            if (configuration.loggable == null) {
                configuration.loggable = new DefaultLoggable();
            }
            if (configuration.logcatPrinter == null) {
                configuration.logcatPrinter = new LogcatPrinter();
            }
            if (configuration.filePrinter == null) {
                if (configuration.workSpace == null) {
                    configuration.workSpace = getLogDirectory(configuration.context, configuration.name);
                    if (configuration.workSpace == null) {
                        configuration.workSpace = configuration.context.getDir(configuration.name,
                                Context.MODE_APPEND);
                    }
                }
                if (configuration.workSpace != null) {
                    configuration.filePrinter = FilePrinter.getInstance(configuration.context,
                            configuration.workSpace);
                }
            }

            return configuration;
        }

        static void notNull(Object obj, String message) {
            if (obj == null) {
                throw new NullPointerException(message);
            }
        }

        public static File getLogDirectory(Context context, String dirName) {
            File appLogDir = null;
            String externalStorageState;
            try {
                externalStorageState = Environment.getExternalStorageState();
            } catch (NullPointerException | IncompatibleClassChangeError e) {
                externalStorageState = "";
            }
            if (android.os.Environment.MEDIA_MOUNTED.equals(externalStorageState)
                    && hasExternalStoragePermission(context)) {
                appLogDir = getExternalLogDir(context, dirName);
            }
            if (appLogDir == null) {
                appLogDir = context.getDir(dirName, Context.MODE_APPEND);
            }
            return appLogDir;
        }

        private static File getExternalLogDir(Context context, String dirName) {
            File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
            File appLogDir = new File(new File(dataDir, context.getPackageName()), dirName);
            if (!appLogDir.exists()) {
                if (!appLogDir.mkdirs()) {
                    return null;
                }
                try {
                    new File(appLogDir, ".nomedia").createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return appLogDir;
        }

        private static boolean hasExternalStoragePermission(Context context) {
            int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
            return perm == PackageManager.PERMISSION_GRANTED;
        }
    }
}
