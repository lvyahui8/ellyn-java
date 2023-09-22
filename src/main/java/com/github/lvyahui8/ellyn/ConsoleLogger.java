package com.github.lvyahui8.ellyn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleLogger {

    private static final Logger logger = LoggerFactory.getLogger("default");

    public static void info(String format,Object ... args) {
        logger.info(format,args);
    }
}
