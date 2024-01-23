/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2023-2024 Grégory Van den Borre
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

package be.yildizgames.common.application.helper.cli;

/**
 * Utility class for measuring and displaying elapsed time between operations.
 *
 * @author Grégory Van den Borre
 */
public class PerformanceChecker {

    /**
     * The timestamp of when this instance was created or the last measurement was taken.
     */
    private long now = System.currentTimeMillis();

    /**
     * Creates a new PerformanceChecker instance.
     */
    public PerformanceChecker() {
        super();
    }

    /**
     * Displays the elapsed time since construction or the last call in milliseconds.
     *
     * @param label label to display along with the elapsed time.
     */
    public final void displayTimeElapsed(final String label) {
        var l = System.currentTimeMillis();
        if (label == null || label.isEmpty()) {
            Terminal.println("Time elapsed " + (l - this.now) + "ms.");
        } else {
            Terminal.println("Time elapsed for " + label + " " + (l - this.now) + "ms.");
        }
        this.now = l;
    }
}
