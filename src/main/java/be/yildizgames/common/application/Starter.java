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

import be.yildizgames.common.logging.Logger;

import java.util.Objects;
import java.util.Properties;

/**
 * Base class for application starters.
 *
 * @author Grégory Van den Borre
 */
public abstract class Starter {

    /**
     * The application instance.
     */
    private Application application;

    /**
     * Creates a new starter.
     */
    protected Starter() {
        super();
    }

    /**
     * Sets the application instance.
     *
     * @param application the application
     */
    final void setApplication(Application application) {
        this.application = Objects.requireNonNull(application);
    }

    /**
     * Gets the application configuration properties.
     *
     * @return the properties
     */
    protected final Properties getApplicationProperties() {
        return this.application.getConfiguration();
    }

    /**
     * Starts the application and logs any errors.
     */
    void startLoggedErrors() {
        try {
            this.start();
        } catch (Exception e) {
            Logger.getLogger(this).error(e);
            System.exit(-1);
        }
    }

    /**
     * Starts the application.
     */
    public abstract void start();

}
