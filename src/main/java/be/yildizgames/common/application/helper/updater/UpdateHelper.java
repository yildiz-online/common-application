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

import be.yildizgames.common.logging.Logger;
import be.yildizgames.module.http.HttpClientBuilder;
import org.update4j.Archive;
import org.update4j.Configuration;
import org.update4j.FileMetadata;
import org.update4j.UpdateOptions;
import org.update4j.service.UpdateHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Handles checking for and applying updates.
 *
 * @author Grégory Van den Borre
 */
public class UpdateHelper {

    /**
     * A map of last update times by URL.
     */
    private final Map<String, LocalDateTime> lastUpdate = new HashMap<>();

    private final HttpClientBuilder httpClientBuilder;

    /**
     * Creates a new update helper.
     */
    public UpdateHelper() {
        super();
        this.httpClientBuilder = HttpClientBuilder.provide();
    }

    /**
     * Checks for and applies any available updates.
     *
     * @param url         the update URL
     * @param archiveName the archive file name
     * @param delay       minimum delay between checks
     * @param timeout     HTTP timeout
     * @param listener    event listeners
     */
    public final void update(String url, String archiveName, TemporalAmount delay, int timeout, List<UpdateDownloadListener> listener) {
        var now = LocalDateTime.now();
        if (!this.lastUpdate.containsKey(url) || now.isAfter(this.lastUpdate.computeIfAbsent(url, a -> now).plus(delay))) {
            try {
                var config = Configuration.read(this.httpClientBuilder.buildHttpClient(timeout).getReader(url));
                if (config.requiresUpdate()) {
                    var result = config.update(
                            UpdateOptions
                                    .archive(Path.of(archiveName))
                                    .updateHandler(new UpdateHandlerNotifier(listener)));
                    if(result.getException()  != null) {
                        Logger.getLogger(this).error(result.getException());
                    } else {
                        Archive.read(archiveName).install(true);
                    }
                } else {
                    listener.forEach(UpdateDownloadListener::fileUpToDate);
                }
                this.lastUpdate.put(url, now);
            } catch (Exception e) {
                Logger.getLogger(this).error(e);
                listener.forEach(l -> l.downloadFailure(e));
            }
        }
    }

    /**
     * Notifies update listeners of events during the download process.
     */
    private static class UpdateHandlerNotifier implements UpdateHandler {

        /**
         * The list of update listeners.
         */
        private final List<UpdateDownloadListener> listener;

        /**
         * Creates a new notifier with the given listeners.
         *
         * @param listener the list of listeners
         */
        private UpdateHandlerNotifier(final List<UpdateDownloadListener> listener) {
            this.listener = listener;
        }

        @Override
        public final void startDownloads() {
            this.listener.forEach(UpdateDownloadListener::startDownloads);
        }

        @Override
        public final void updateDownloadFileProgress(FileMetadata file, float frac) {
            this.listener.forEach(l -> l.fileUpdated(file.getPath(), (int) (frac * 100)));
        }

        @Override
        public final void doneDownloadFile(FileMetadata file, Path path) {
            this.listener.forEach(l -> l.fileCompletedSuccessfully(file.getPath()));
        }

        @Override
        public final void updateDownloadProgress(float frac) {
            this.listener.forEach(l -> l.downloadUpdated((int) (frac * 100)));
        }

        @Override
        public final void doneDownloads() {
            this.listener.forEach(UpdateDownloadListener::downloadCompletedSuccessfully);
        }

        @Override
        public final void startDownloadFile(FileMetadata file) {
            this.listener.forEach(l -> l.startDownloadFile(file.getPath()));
        }

        @Override
        public final void succeeded() {
            this.listener.forEach(UpdateDownloadListener::completed);
        }
    }
}
