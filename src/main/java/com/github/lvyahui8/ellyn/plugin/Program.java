package com.github.lvyahui8.ellyn.plugin;

import java.util.concurrent.atomic.AtomicInteger;

public class Program {

    public static final Program instance = new Program();

    static Method[] loadMethods() {
        return null;
    }

    private Program() {
        methods = loadMethods();
    }

    public AtomicInteger methodId = new AtomicInteger(0);

    public final Method[] methods;
}
