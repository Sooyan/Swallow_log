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

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import soo.swallow.log.Level;
import soo.swallow.log.Printer;

/**
 * Created by Joseph.yan
 */
public class FilePrinter implements Printer, Runnable {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String NAME_PREFIX = "L";
    private static final Object LOCK = new Object();

    private static FilePrinter instance = null;

    private String pName;
    private File logFile;
    private BlockingQueue<Package> packageQueue = new LinkedBlockingQueue<>();

    private Thread thread;
    private boolean isRunning;

    public static synchronized FilePrinter getInstance(Context context, File workSpace) {
        if (instance == null) {
            if (workSpace == null) {
                throw new NullPointerException("WorkSpace must not be null");
            }
            if (!workSpace.isDirectory()) {
                throw new IllegalArgumentException("Workspace must is directory");
            }
            String logFileName = generateFileName();
            File logFile = new File(workSpace, logFileName);
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String pName = getCurProcessName(context);
            if (TextUtils.isEmpty(pName)) {
                throw new RuntimeException("Can`t fetch current process name");
            }

            instance = new FilePrinter(pName, logFile);
        }
        return instance;
    }

    private static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private static String generateFileName() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(NAME_PREFIX
                + calendar.get(Calendar.YEAR))
                + calendar.get(Calendar.MONTH)
                + calendar.get(Calendar.DAY_OF_MONTH)
                + calendar.get(Calendar.DAY_OF_MONTH);
    }

    private FilePrinter(String pName, File logFile) {
        this.pName = pName;
        this.logFile = logFile;
    }

    private void startIfNecessary() {
        synchronized (LOCK) {
            if (!isRunning && thread == null) {
                thread = new Thread(this);
                thread.start();
            }
        }
    }

    @Override
    public void print(String tag, Level level, String message) {
        Package logPackage = Package.obtain(pName, tag, level, message);
        try {
            packageQueue.put(logPackage);
            startIfNecessary();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        synchronized (LOCK) {
            isRunning = true;
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile, true);
            while (!packageQueue.isEmpty()) {
                Package p = packageQueue.take();

                String content = p.toString();
                Package.recycle(p);

                fileWriter.write(content);
                fileWriter.write(LINE_SEPARATOR);
                fileWriter.flush();

                synchronized (LOCK) {
                    if (!isRunning) {
                        break;
                    }
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            synchronized (LOCK) {
                isRunning = false;
                if (thread != null) {
                    thread = null;
                }
            }
        }
    }

    private static class Package {

        private static final int MAX_SIZE = 20;
        private static LinkedList<Package> pool = new LinkedList<>();

        String nameSpace;
        String tag;
        Level level;
        String message;

        synchronized static Package obtain(String nameSpace, String tag, Level level, String message) {
            Package p = pool.poll();
            if (p == null) {
                p = new Package();
            }

            p.nameSpace = nameSpace;
            p.tag = tag;
            p.level = level;
            p.message = message;

            return p;
        }

        synchronized static void recycle(Package p) {
            int currentSize = pool.size();
            if (currentSize < MAX_SIZE) {
                pool.offer(p);

                p.nameSpace = null;
                p.tag = null;
                p.level = null;
                p.message = null;
            }
        }

        @Override
        public synchronized String toString() {
            return getS()
                    + "/"
                    + nameSpace
                    + "/"
                    + level
                    + "/"
                    + tag
                    + ":"
                    + message;
        }

        private String getS() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return simpleDateFormat.format(new Date());
        }
    }
}
