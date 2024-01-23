/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2020-2024 Grégory Van den Borre
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
package be.yildizgames.common.application.helper.splashscreen;

/**
 * Defines a splash screen that can be displayed during application launch.
 *
 * @author Grégory Van den Borre
 */
public interface SplashScreen {

    /**
     * Displays the splash screen.
     */
    void display();

    /**
     * Closes the splash screen.
     */
    void close();

    /**
     * Sets the name to display on the splash screen.
     *
     * @param name the name to display
     */
    void setName(String name);

    /**
     * Sets the loading progress percentage to display.
     *
     * @param percent the progress percentage
     */
    void setProgress(int percent);

    /**
     * Sets the current loading task name to display.
     *
     * @param name the current loading task
     */
    void setCurrentLoading(String name);
}
