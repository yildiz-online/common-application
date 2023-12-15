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

import java.nio.file.Path;

/**
 * Handles restarting the application process by using a native launcher.
 *
 * @author Grégory Van den Borre
 */
public class LauncherRestarter implements Restarter {

    /**
     * The executable to use on Windows.
     */
    private final String windowExecutable;

    /**
     * The executable to use on Linux.
     */
    private final String linuxExecutable;

    /**
     * Constructs a Restarter with the executables.
     *
     * @param windowExecutable Name of the Windows executable file.
     * @param linuxExecutable  Name of the Linux executable file.
     */
    public LauncherRestarter(String windowExecutable, String linuxExecutable) {
        super();
        this.windowExecutable = windowExecutable;
        this.linuxExecutable = linuxExecutable;
    }

    @Override
    public final void restart() {
        restart(0);
    }

    @Override
    public final void restart(long msBeforeRestart) {
        String[] command = {Path.of("").toAbsolutePath().resolve(System.getProperty("os.name").equals("Windows") ? this.windowExecutable : this.linuxExecutable).toString()};
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Thread.sleep(msBeforeRestart);
                Runtime.getRuntime().exec(command);
            } catch (Exception e) {
                System.getLogger(LauncherRestarter.class.getName()).log(System.Logger.Level.ERROR, "Cannot restart.", e);
                throw new IllegalArgumentException(e);
            }
        }));
        System.exit(0);
    }
}
