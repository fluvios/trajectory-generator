package com.utils;

import java.util.Arrays;

public class ThreadUtils {
 
    public static Thread[] getAllThreads() {
        ThreadGroup rootThreadGroup = getRootThreadGroup();
        Thread[] threads = new Thread[32];
        int count = rootThreadGroup.enumerate(threads);
        while (count == threads.length) {
            threads = new Thread[threads.length * 2];
            count = rootThreadGroup.enumerate(threads);
        }
        return Arrays.copyOf(threads, count);
    }
     
    private static ThreadGroup getRootThreadGroup() {
        ThreadGroup candidate = Thread.currentThread().getThreadGroup();
        while (candidate.getParent() != null) {
            candidate = candidate.getParent();
        }
        return candidate;
    }
     
}
