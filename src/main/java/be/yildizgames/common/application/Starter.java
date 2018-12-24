/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2018 Grégory Van den Borre
 *
 *  More infos available: https://www.yildiz-games.be
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

import be.yildizgames.common.git.GitProperties;
import be.yildizgames.common.git.GitPropertiesProvider;
import be.yildizgames.common.logging.LogEngine;
import be.yildizgames.common.logging.LogEngineFactory;
import be.yildizgames.common.logging.LoggerConfiguration;
import be.yildizgames.common.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Starter {

    /**
     * Configure the logging, and display the GIT properties.
     * @param loggerConfiguration Configuration for the logger.
     * @param applicationName Name of the application.
     * @throws IOException
     */
    public static void start(LoggerConfiguration loggerConfiguration, String applicationName) throws IOException {
        LogEngine logEngine = LogEngineFactory.getLogEngine();
        logEngine.configureFromProperties(loggerConfiguration);
        Logger logger = LoggerFactory.getLogger(Starter.class);
        logger.info("Starting {} (PID:{})...", applicationName, Util.getPid());
        GitProperties git = GitPropertiesProvider.getGitProperties();
        logger.info("Version: {}", git.getCommitId());
        logger.info("Built at: {}", git.getBuildTime());
    }

}
