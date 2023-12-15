/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2023 Grégory Van den Borre
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
package be.yildizgames.common.application.helper.restarter;

import be.yildizgames.common.logging.Logger;

import java.io.File;

/**
 * Handles restarting the application java process.
 *
 * @author Grégory Van den Borre
 */
public class ApplicationRestarter implements Restarter {

    /**
     * Logger for output.
     */
    private final Logger logger = Logger.getLogger(this);

    /**
     * Name of the application JAR file.
     */
    private final String app;

    /**
     * Path to the Java executable.
     */
    private final String javaPath;

    /**
     * Creates a new restarter with the given JAR name.
     *
     * @param jarName the application JAR file name
     */
    public ApplicationRestarter(String jarName) {
        super();
        this.app = jarName;
        this.javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator;
    }

    /**
     * Creates a new restarter with the given JAR name and a provided.
     *
     * @param jarName  the application JAR file name
     * @param javaPath Java implementation to use.
     */
    public ApplicationRestarter(String jarName, String javaPath) {
        super();
        this.app = jarName;
        this.javaPath = javaPath.endsWith(File.separator) ? javaPath : javaPath + File.separator;
    }

    @Override
    public final void restart() {
        restart(0);
    }

    @Override
    public final void restart(long msBeforeRestart) {
        try {
            this.logger.info("Restarting the system.");
            String[] command = {this.javaPath +
                    "java -jar " +
                    this.app};
            Thread.sleep(msBeforeRestart);
            Runtime.getRuntime().exec(command);
            System.exit(0);
        } catch (Exception e) {
            throw new IllegalStateException("The application could not restart", e);
        }
    }

}
