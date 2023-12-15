/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2020-2023 Grégory Van den Borre
 *  More infos available: https://engine.yildiz-games.be
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright
 *  notice and this permission notice shall be included in all copies or substantial portions of the  Software.
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package be.yildizgames.common.application.helper.logging;

import be.yildizgames.common.logging.LoggerPropertiesConfiguration;

import java.util.Properties;

/**
 * Provides a set of default logging properties for console output and a file.
 *
 * @author Grégory Van den Borre
 */
public class LoggerPropertiesConsoleFile extends Properties {

    /**
     * Creates a new properties object with default logging values.
     *
     * @param applicationName the name of the application to include in the log file.
     */
    public LoggerPropertiesConsoleFile(String applicationName) {
        super();
        this.setProperty(LoggerPropertiesConfiguration.LOGGER_LEVEL_KEY, "INFO");
        this.setProperty(LoggerPropertiesConfiguration.LOGGER_OUTPUT_KEY, "CONSOLE,FILE");
        this.setProperty(LoggerPropertiesConfiguration.LOGGER_PATTERN_KEY, "%d{yyyy/MM/dd HH:mm:ss} | %level | %logger | %msg%n");
        this.setProperty(LoggerPropertiesConfiguration.LOGGER_TCP_HOST_KEY, "localhost");
        this.setProperty(LoggerPropertiesConfiguration.LOGGER_TCP_PORT_KEY, "60000");
        this.setProperty(LoggerPropertiesConfiguration.LOGGER_FILE_OUTPUT_KEY, "logs/" + applicationName + ".log");
        this.setProperty(LoggerPropertiesConfiguration.LOGGER_CONFIGURATION_FILE_KEY, "logback.xml");
        this.setProperty(LoggerPropertiesConfiguration.LOGGER_DISABLED_KEY, "org.hsqldb.persist.Logger,hsqldb.db,jdk.internal.httpclient.debug,jdk.event.security,javafx.scene.focus,com.sun.webkit.perf.WCFontPerfLogger.TOTALTIME");
    }
}
