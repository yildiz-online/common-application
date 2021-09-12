/*
 * MIT License
 *
 * Copyright (c) 2019 Grégory Van den Borre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.yildizgames.common.application.helper.updater;

import be.yildizgames.common.logging.Logger;
import be.yildizgames.module.http.HttpRequest;
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
 * @author Grégory Van den Borre
 */
public class UpdateHelper {

    private final Map<String, LocalDateTime> lastUpdate = new HashMap<>();

    public UpdateHelper() {
        super();
    }

    public void update(String url, String archiveName, TemporalAmount delay, List<UpdateDownloadListener> listener) {
        var now = LocalDateTime.now();
        if (!this.lastUpdate.containsKey(url) || now.isAfter(this.lastUpdate.computeIfAbsent(url, a -> now).plus(delay))) {
            try {
                var config = Configuration.read(new HttpRequest().getReader(url));
                if (config.requiresUpdate()) {
                    config.update(
                            UpdateOptions
                                    .archive(Path.of(archiveName))
                                    .updateHandler(new UpdateHandlerNotifier(listener)));
                    Archive.read(archiveName).install(true);
                }
                this.lastUpdate.put(url, now);
            } catch (IOException e) {
                Logger.getLogger(this).error(e);
            }
        }
    }

    private static class UpdateHandlerNotifier implements UpdateHandler {

        private final List<UpdateDownloadListener> listener;

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
