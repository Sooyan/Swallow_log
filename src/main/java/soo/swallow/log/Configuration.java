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

import java.util.HashMap;
import java.util.Map;

import soo.swallow.log.box.DefaultLoggable;
import soo.swallow.log.box.DefaultTheme;
import soo.swallow.log.box.LogcatPrinter;

/**
 * Created by Joseph.yan
 */
public class Configuration {
    private static final String TAG = "Configuration--->";

    private Configuration() {}

    boolean enable = true;
    boolean isPrintHeader = true;
    boolean isPrintTrace = true;
    String defaultTag = "--->";
    Level defaultLevel = Level.DEBUG;
    int traceMaxSize = 3;

    protected Theme theme;
    Printer printer;
    Loggable loggable;

    Map<Class<?>, LineFactory> lineFactoryMap = new HashMap<>();

    public static class Builder {

        private Configuration configuration;

        public Builder() {
            configuration = new Configuration();
        }

        public Builder setEnable(boolean enable) {
            configuration.enable = enable;
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

        public Builder setPrinter(Printer printer) {
            configuration.printer = printer;
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
            if (configuration.theme == null) {
                configuration.theme = new DefaultTheme();
            }
            if (configuration.printer == null) {
                configuration.printer = new LogcatPrinter();
            }
            if (configuration.loggable == null) {
                configuration.loggable = new DefaultLoggable();
            }

            return configuration;
        }
    }
}
