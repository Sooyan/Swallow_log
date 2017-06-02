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

import java.util.Arrays;

import soo.swallow.log.box.LineFactoryProxy;

/**
 * Created by Joseph.yan
 */
class Logger {

    private boolean enable;
    private String defaultTag;
    private Level defaultLevel;
    private boolean isPrintHeader;
    private boolean isPrintTrace;
    private int traceMaxSize;

    private Printer filePrinter;
    private Printer logCatPrinter;
    private ThemeSpec themeSpec;
    private Loggable loggable;
    private LineFactoryProxy lineFactoryProxy;

    Logger(Configuration configuration) {
        this.enable = configuration.enable;
        this.defaultTag = configuration.defaultTag;
        this.defaultLevel = configuration.defaultLevel;
        this.isPrintHeader = configuration.isPrintHeader;
        this.isPrintTrace = configuration.isPrintTrace;
        this.traceMaxSize = configuration.traceMaxSize;

        this.filePrinter = configuration.filePrinter;
        this.logCatPrinter = configuration.logcatPrinter;
        this.themeSpec = new ThemeSpec(configuration.theme);
        this.loggable = configuration.loggable;
        this.lineFactoryProxy = new LineFactoryProxy(configuration.lineFactoryMap);
    }

    void release() {
//TODO
    }

    public void log(Level level, String tag, Object object, int flag) {
        if (!enable) {
            return;
        }
        Thread thread = Thread.currentThread();
        StackTraceElement[] trace = splitStackTrace(thread.getStackTrace(), traceMaxSize);
        Class<?> invokeCls = trace[0].getClass();
        level = level == null ? defaultLevel : level;
        tag = tag == null ? defaultTag : tag;

        String[] headerLines = getHeaderLines(thread);
        String[] traceLines = getTraceLines(trace);
        String[] contentLines = getContentLines(object);

        if (loggable.forClass(invokeCls)
                && loggable.forThread(thread)
                && loggable.forTag(tag)
                && loggable.forLevel(level)) {
            printLines(tag, level, headerLines, traceLines, contentLines, flag);
        }
    }

    private void printLines(String tag, Level level,
                            String[] headerLines,
                            String[] traceLines,
                            String[] contentLines, int flag) {
        int headerMaxLength = findMaxLength(headerLines);
        int traceMaxLength = findMaxLength(traceLines);
        int contentMaxLength = findMaxLength(contentLines);

        int maxLength = Math.max(headerMaxLength, Math.max(traceMaxLength, contentMaxLength));

//        print top boundary
        printContent(tag, level, themeSpec.getTopBoundary(maxLength), flag);
//        print header(about thread info)
        if (isPrintHeader) {
            printLinesWithMaxLength(tag, level, headerLines, maxLength, flag);
//            print divider
            printContent(tag, level, themeSpec.getDividerString(maxLength), flag);
        }
//        print trace(about thread stacktrace)
        if (isPrintTrace) {
            printLinesWithMaxLength(tag, level, traceLines, maxLength, flag);
//            print divider
            printContent(tag, level, themeSpec.getDividerString(maxLength), flag);
        }
//        print content(your object)
        printLinesWithMaxLength(tag, level, contentLines, maxLength, flag);
//        print bottom boundary
        printContent(tag, level, themeSpec.getBottomBoundary(maxLength), flag);
    }

    private void printLinesWithMaxLength(String tag, Level level,
                                         String[] lines, int maxLength, int flag) {
        if (lines != null) {
            for (String line : lines) {
                String tempLine = themeSpec.formatMessage(line, maxLength);
                printContent(tag, level, tempLine, flag);
            }
        }
    }

    private void printContent(String tag, Level level, String content, int flag) {
        switch (flag) {
            case Log.FLAG_FILE:
                if (filePrinter != null) {
                    filePrinter.print(tag, level, content);
                }
                break;
            case Log.FLAG_LOGCAT:
                if (logCatPrinter != null) {
                    logCatPrinter.print(tag, level, content);
                }
                break;
            case Log.FLAG_BOTH:
                printContent(tag, level, content, Log.FLAG_LOGCAT);
                printContent(tag, level, content, Log.FLAG_FILE);
                break;
            default:
                printContent(tag, level, content, Log.FLAG_LOGCAT);
                break;
        }
    }

    private String[] getHeaderLines(Thread thread) {
        return new String[]{"Thread:"
                + thread.getName()
                + "("
                + thread.getId()
                + ")"};
    }

    private String[] getTraceLines(StackTraceElement[] stackTraceElements) {
        String[] result = null;
        if (stackTraceElements != null) {
            int length = stackTraceElements.length;
            result = new String[length];
            String indent = "";
            for (int i = 0; i < length; i++) {
                StackTraceElement element = stackTraceElements[i];
                result[i] = indent
                            + getSimpleClassName(element.getClassName())
                            + "."
                            + element.getMethodName()
                            + " ("
                            + element.getFileName()
                            + ":"
                            + element.getLineNumber()
                            + ")";
                indent += "   ";
            }
        }
        return result;
    }

    private String[] getContentLines(Object object) {
        return lineFactoryProxy.getLines(object);
    }

    private static int findMaxLength(String[] lines) {
        String maxLine = null;
        if (lines != null) {
            for (String line : lines) {
                maxLine = (maxLine == null || maxLine.length() < line.length()) ? line : maxLine;
            }
        }
        return maxLine == null ? 0 : maxLine.length();
    }

    private static StackTraceElement[] splitStackTrace(StackTraceElement[] stackTraceElements, int traceMaxSize) {
        if (stackTraceElements != null) {
            int length = stackTraceElements.length;
            for (int i = 2; i < length; i++) {
                StackTraceElement stackTraceElement = stackTraceElements[i];
                String className = stackTraceElement.getClassName();
                if (!className.equals(Log.class.getName())
                        && !className.equals(Logger.class.getName())) {
                    int offset = Math.max(1, Math.min(length - i, traceMaxSize));
                    return Arrays.copyOfRange(stackTraceElements, i, i + offset);
                }
            }
        }
        return null;
    }

    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }
}
