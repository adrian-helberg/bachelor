package de.haw.utils;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public interface Logging {
    default Logger getLogger() {
        var logger = Logger.getLogger(getClass().getName());
        logger.setUseParentHandlers(false);
        var handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord record) {
                return String.format(
                        "[%1$tT] [%2$-1s] %3$s %n",
                        new Date(record.getMillis()),
                        record.getLevel(), record.getMessage());
            }
        });
        logger.addHandler(handler);
        return logger;
    }
}
