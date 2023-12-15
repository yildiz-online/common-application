/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2019-2023 Grégory Van den Borre
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
package be.yildizgames.common.application;

import be.yildizgames.common.logging.LoggerPropertiesConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

class ApplicationTest {

    @Nested
    class constructor {

        @Test
        void happyFlow() {
            Application application = Application.prepare("test");
            Assertions.assertNotNull(application);
        }

        @Test
        void nullParameter() {
            Assertions.assertThrows(NullPointerException.class, () -> Application.prepare(null));
        }

        @Test
        void emptyParameter() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> Application.prepare(""));
        }

    }

    @Nested
    class WithConfiguration {

        @Test
        void happyFlow() throws IOException {
            Files.deleteIfExists(Path.of("config/configuration.properties"));
            Properties p = new Properties();
            p.setProperty("t", "1");
            Application application = Application.prepare("test");
            application.withConfiguration(new String[]{}, p);
            Assertions.assertEquals(9, application.getConfiguration().size());
            Assertions.assertEquals("1", application.getConfiguration().get("t"));
            Assertions.assertEquals("logs/test.log", application.getConfiguration().get(LoggerPropertiesConfiguration.LOGGER_FILE_OUTPUT_KEY));
            Assertions.assertEquals("60000", application.getConfiguration().get("logger.tcp.port"));
            Assertions.assertEquals("CONSOLE,FILE", application.getConfiguration().get("logger.output"));
            Assertions.assertEquals("%d{yyyy/MM/dd HH:mm:ss} | %level | %logger | %msg%n", application.getConfiguration().get("logger.pattern"));
            Assertions.assertEquals("localhost", application.getConfiguration().get("logger.tcp.host"));
            Assertions.assertEquals("org.hsqldb.persist.Logger,hsqldb.db,jdk.internal.httpclient.debug,jdk.event.security,javafx.scene.focus,com.sun.webkit.perf.WCFontPerfLogger.TOTALTIME", application.getConfiguration().get("logger.disabled"));
            Assertions.assertEquals("INFO", application.getConfiguration().get("logger.level"));
            Assertions.assertEquals("logback.xml", application.getConfiguration().get("logger.configuration.file"));
            Files.deleteIfExists(Path.of("config/configuration.properties"));
        }

        @Test
        void withOverride() throws IOException {
            Files.deleteIfExists(Path.of("config/configuration.properties"));
            Properties p = new Properties();
            p.setProperty("t", "1");
            p.setProperty(LoggerPropertiesConfiguration.LOGGER_FILE_OUTPUT_KEY, "log.log");
            Application application = Application.prepare("test");
            application.withConfiguration(new String[]{}, p);
            Assertions.assertEquals(9, application.getConfiguration().size());
            Assertions.assertEquals("1", application.getConfiguration().get("t"));
            Assertions.assertEquals("log.log", application.getConfiguration().get(LoggerPropertiesConfiguration.LOGGER_FILE_OUTPUT_KEY));
            Assertions.assertEquals("60000", application.getConfiguration().get("logger.tcp.port"));
            Assertions.assertEquals("CONSOLE,FILE", application.getConfiguration().get("logger.output"));
            Assertions.assertEquals("%d{yyyy/MM/dd HH:mm:ss} | %level | %logger | %msg%n", application.getConfiguration().get("logger.pattern"));
            Assertions.assertEquals("localhost", application.getConfiguration().get("logger.tcp.host"));
            Assertions.assertEquals("org.hsqldb.persist.Logger,hsqldb.db,jdk.internal.httpclient.debug,jdk.event.security,javafx.scene.focus,com.sun.webkit.perf.WCFontPerfLogger.TOTALTIME", application.getConfiguration().get("logger.disabled"));
            Assertions.assertEquals("INFO", application.getConfiguration().get("logger.level"));
            Assertions.assertEquals("logback.xml", application.getConfiguration().get("logger.configuration.file"));
            Files.deleteIfExists(Path.of("config/configuration.properties"));
        }
    }

}
