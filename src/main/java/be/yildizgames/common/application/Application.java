/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
 *
 *  More infos available: https://engine.yildiz-games.be
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

import be.yildizgames.common.configuration.ConfigurationNotFoundAdditionalBehavior;
import be.yildizgames.common.configuration.ConfigurationNotFoundDefault;
import be.yildizgames.common.configuration.ConfigurationRetriever;
import be.yildizgames.common.configuration.ConfigurationRetrieverFactory;
import be.yildizgames.common.configuration.LoggerPropertiesConfiguration;
import be.yildizgames.common.configuration.parameter.ApplicationArgs;
import be.yildizgames.common.git.GitProperties;
import be.yildizgames.common.git.GitPropertiesProvider;
import be.yildizgames.common.logging.LogEngine;
import be.yildizgames.common.logging.LogEngineProvider;

import java.io.IOException;
import java.util.Properties;

/**
 * This class will configure any new application (logger,...)
 *
 * @author Grégory Van den Borre
 */
public class Application {

    private final String applicationName;

    private Properties properties = new Properties();

    /**
     * Use the static function start instead.
     */
    private Application(String applicationName) {
        super();
        this.applicationName = applicationName;
    }

    public static Application prepare(String applicationName) {
        return new Application(applicationName);
    }

    public Application withConfiguration(String[] args, Properties defaultConfig, ConfigurationNotFoundAdditionalBehavior behavior) {
        ConfigurationRetriever configurationRetriever = ConfigurationRetrieverFactory
                .fromFile(ConfigurationNotFoundDefault.fromDefault(defaultConfig, behavior));
        this.properties = configurationRetriever.retrieveFromArgs(ApplicationArgs.of(args));
        return this;
    }

    public Application withConfiguration(String[] args, Properties defaultConfig) {
        return this.withConfiguration(args, defaultConfig, () -> {});
    }

    public Application start() {
        try {
            LogEngine logEngine = LogEngineProvider.getLoggerProvider().getLogEngine();
            logEngine.configureFromProperties(LoggerPropertiesConfiguration.fromProperties(this.properties));
            System.Logger logger = System.getLogger(Application.class.getName());
            logger.log(System.Logger.Level.INFO, "Starting %s (PID:%s)...", this.applicationName, ProcessHandle.current().pid());
            GitProperties git = GitPropertiesProvider.getGitProperties();
            logger.log(System.Logger.Level.INFO, "Version: %s", git.getCommitId());
            logger.log(System.Logger.Level.INFO, "Built at: %s", git.getBuildTime());
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Properties getProperties() {
        return this.properties;
    }

}
