package com.cmu.blockchain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for setting up loggers.
 */
public class LoggerUtil {

    /**
     * Provides a logger for the specified class.
     *
     * @param clazz The class for which the logger is created
     * @return Logger instance for the given class
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}