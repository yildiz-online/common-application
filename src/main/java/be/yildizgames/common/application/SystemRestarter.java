/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
 *
 *  More infos available: https://engine.yildiz-games.be
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *
 */
package be.yildizgames.common.application;

import be.yildizgames.common.logging.Logger;

import java.io.File;

public class SystemRestarter {

    private final Logger logger = Logger.getLogger(this);

    private final String app;

    private final String javaPath;

    public SystemRestarter(String jarName) {
        super();
        this.app = jarName;
        this.javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator;
    }

    public SystemRestarter(String jarName, String javaPath) {
        super();
        this.app = jarName;
        this.javaPath = javaPath.endsWith(File.separator) ? javaPath : javaPath + File.separator;
    }

    public void restartApplication(long msBeforeRestart) {
        try {
            logger.info("Restarting the system.");
            StringBuilder cmd = new StringBuilder();
            cmd
                    .append(javaPath)
                    .append("java ")
                    .append("-jar " + app);
            Thread.sleep(msBeforeRestart);
            Runtime.getRuntime().exec(cmd.toString());
            System.exit(0);
        } catch (Exception e) {
            throw new IllegalStateException("The application could not restart", e);
        }

    }

}
