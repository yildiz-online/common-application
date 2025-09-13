/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2021-2024 Grégory Van den Borre
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
package be.yildizgames.common.application.helper.updater;

import java.nio.file.Path;

/**
 * Listener for download update events.
 *
 * @author Grégory Van den Borre
 */
public interface UpdateDownloadListener {

    /**
     * Called when a file is determined to already be up to date.
     */
    default void fileUpToDate() {
    }

    /**
     * Called when the file download percentage is updated.
     *
     * @param file    the file currently downloaded.
     * @param percent the new percentage
     */
    default void fileUpdated(Path file, int percent) {
    }

    /**
     * Called when a file download completes successfully.
     *
     * @param file the completed file
     */
    default void fileCompletedSuccessfully(Path file) {
    }

    /**
     * Called when the entire download completes successfully.
     */
    default void downloadCompletedSuccessfully() {
    }

    /**
     * Called when the overall download percentage is updated.
     *
     * @param percent the new percentage
     */
    default void downloadUpdated(int percent) {
    }

    /**
     * Called when downloads are started.
     */
    default void startDownloads() {
    }

    /**
     * Called when the entire download completes.
     */
    default void completed() {
    }

    /**
     * Called when a file download is started.
     *
     * @param file the file path
     */
    default void startDownloadFile(Path file) {
    }

    default void downloadFailure(Throwable t) {}
}

