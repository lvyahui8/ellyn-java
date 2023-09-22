package com.github.lvyahui8.ellyn.plugin;

import java.util.concurrent.atomic.AtomicInteger;

public class Program {

    public static final Program instance = new Program();

    /**
     * 加载全量函数文件
     * @return 全量函数
     */
    static Method[] loadMethods() {
        //

        return null;
    }

    private Program() {
        methods = loadMethods();
    }
    private final Method[] methods;
}
