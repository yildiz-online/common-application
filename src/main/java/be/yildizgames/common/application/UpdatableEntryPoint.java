/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2021-2023 Grégory Van den Borre
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

import be.yildizgames.common.application.helper.splashscreen.SplashScreenProvider;
import org.update4j.LaunchContext;
import org.update4j.service.Launcher;

import java.util.Properties;

/**
 * Base class for application entry points that support updating.
 *
 * @author Grégory Van den Borre
 */
public abstract class UpdatableEntryPoint implements Launcher {

    /**
     * Creates a new entry point.
     */
    protected UpdatableEntryPoint() {
        super();
    }

    @Override
    public final void run(LaunchContext launchContext) {
        launch(new String[]{});
    }

    /**
     * Launches the application by initializing common components.
     *
     * @param args the command line arguments
     */
    protected final void launch(String[] args) {
        Application.prepare(getApplicationName())
                .withConfiguration(args, getDefaultConfiguration())
                .withUpdate(getUpdateUrl(), 5)
                .withSplashScreen(getSplashScreen())
                .start(getStarter());
    }

    /**
     * Provide the name of the application.
     *
     * @return The application name.
     */
    protected abstract String getApplicationName();

    /**
     * Provide the url to call to get the update manifest.
     *
     * @return The update manifest.
     */
    protected abstract String getUpdateUrl();

    /**
     * Provide the starter.
     *
     * @return The starter.
     */
    protected abstract Starter getStarter();

    /**
     * Provide the splash screen.
     *
     * @return The splash screen.
     */
    protected abstract SplashScreenProvider getSplashScreen();

    /**
     * Provide the default configuration properties.
     *
     * @return The default configuration properties.
     */
    protected abstract Properties getDefaultConfiguration();
}
