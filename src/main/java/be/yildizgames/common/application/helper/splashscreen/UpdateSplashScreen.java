/*
 *
 *  * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  *
 *  *  Copyright (c) 2022 Grégory Van den Borre
 *  *
 *  *  More infos available: https://engine.yildiz-games.be
 *  *
 *  *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  *  documentation files (the "Software"), to deal in the Software without restriction, including without
 *  *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  *  of the Software, and to permit persons to whom the Software is furnished to do so,
 *  *  subject to the following conditions:
 *  *
 *  *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  *  portions of the Software.
 *  *
 *  *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *  *
 *
 */

package be.yildizgames.common.application.helper.splashscreen;

import be.yildizgames.common.application.helper.updater.UpdateDownloadListener;

import java.nio.file.Path;

/**
 * Base splash screen implementation that supports updating progress.
 *
 * @author Grégory Van den Borre
 */
public abstract class UpdateSplashScreen implements SplashScreen, UpdateDownloadListener {

    /**
     * Creates a new update splash screen.
     */
    protected UpdateSplashScreen() {
        super();
    }

    @Override
    public final void fileUpdated(Path file, int percent) {
        UpdateDownloadListener.super.fileUpdated(file, percent);
    }

    @Override
    public final void downloadUpdated(int percent) {
        this.setProgress(percent);
    }

    @Override
    public final void completed() {
        this.close();
    }

    @Override
    public final void startDownloadFile(Path file) {
        this.setCurrentLoading(file.getFileName().toString());
    }
}
