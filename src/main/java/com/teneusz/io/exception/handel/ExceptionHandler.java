package com.teneusz.io.exception.handel;

import org.apache.log4j.Logger;

/**
 * Created by Teneusz on 05.12.2016.
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static Logger LOG = Logger.getLogger(ExceptionHandler.class);
    public void uncaughtException(Thread t, Throwable e) {
        LOG.debug(e.getMessage());
    }
}
