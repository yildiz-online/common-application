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

import be.yildizgames.common.application.helper.cli.Banner;
import be.yildizgames.common.application.helper.cli.BannerLine;
import be.yildizgames.common.application.helper.logging.LoggerPropertiesConsoleFile;
import be.yildizgames.common.application.helper.splashscreen.EmptySplashScreen;
import be.yildizgames.common.application.helper.splashscreen.SplashScreen;
import be.yildizgames.common.application.helper.splashscreen.SplashScreenProvider;
import be.yildizgames.common.configuration.ConfigurationNotFoundAdditionalBehavior;
import be.yildizgames.common.configuration.ConfigurationNotFoundDefault;
import be.yildizgames.common.configuration.ConfigurationRetriever;
import be.yildizgames.common.configuration.ConfigurationRetrieverFactory;
import be.yildizgames.common.configuration.parameter.ApplicationArgs;
import be.yildizgames.common.git.GitProperties;
import be.yildizgames.common.git.GitPropertiesProvider;
import be.yildizgames.common.logging.LogEngine;
import be.yildizgames.common.logging.LogEngineProvider;
import be.yildizgames.common.logging.Logger;
import be.yildizgames.common.logging.LoggerPropertiesConfiguration;
import be.yildizgames.module.http.HttpRequest;
import org.update4j.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * This class will configure any new application (logger,...)
 *
 * @author Grégory Van den Borre
 */
public class Application {

    private final String applicationName;

    private String updateUrl;

    private Properties properties = new Properties();

    private boolean started;

    private Banner banner;

    private SplashScreenProvider splashScreenProvider;

    private SplashScreen splashScreen;

    /**
     * Use the static function start instead.
     */
    private Application(String applicationName) {
        super();
        this.applicationName = Objects.requireNonNull(applicationName);
        this.banner = new Banner(applicationName);
        this.splashScreenProvider = EmptySplashScreen::new;
        if(applicationName.isEmpty()) {
            throw new IllegalArgumentException("Application name cannot be empty");
        }
    }

    public static Application prepare(String applicationName) {
        return new Application(applicationName);
    }

    public final Application withConfiguration(String[] args, Properties defaultConfig, ConfigurationNotFoundAdditionalBehavior behavior) {
        ConfigurationRetriever configurationRetriever = ConfigurationRetrieverFactory
                .fromFile(ConfigurationNotFoundDefault.fromDefault(Stream.of(new LoggerPropertiesConsoleFile(applicationName), defaultConfig)
                        .collect(Properties::new, Map::putAll, Map::putAll), behavior));
        this.properties = configurationRetriever.retrieveFromArgs(ApplicationArgs.of(args));
        return this;
    }

    public final Application withBanner(Banner banner) {
        this.banner = Objects.requireNonNull(banner);
        return this;
    }

    public final Application withSplashScreen(SplashScreenProvider splashScreen) {
        this.splashScreenProvider = Objects.requireNonNull(splashScreen);
        return this;
    }

    public final Application addBannerLine(BannerLine line) {
        this.banner.addLine(line);
        return this;
    }

    public final Application withConfiguration(String[] args, Properties defaultConfig) {
        return this.withConfiguration(args, defaultConfig, () -> {});
    }

    public final Application withUpdate(String url) {
        this.updateUrl = url;
        return this;
    }

    public final Application start() {
        if(this.started) {
            return this;
        }
        try {
            init();
            this.started = true;
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public final Application start(Starter starter) {
        if(this.started) {
            return this;
        }
        try {
            init();
            starter.setApplication(this);
            starter.startLoggedErrors();
            this.started = true;
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public final void applicationStarted() {
        this.splashScreen.close();
    }

    public final Properties getConfiguration() {
        return this.properties;
    }

    private void init() throws IOException {
        LogEngine logEngine = LogEngineProvider.getLoggerProvider().getLogEngine();
        logEngine.configureFromProperties(LoggerPropertiesConfiguration.fromProperties(this.properties));
        System.Logger logger = System.getLogger(Application.class.getName());
        this.banner.display();
        logger.log(System.Logger.Level.INFO, "Starting {0} (PID:{1}).", this.applicationName, ProcessHandle.current().pid());
        GitProperties git = GitPropertiesProvider.getGitProperties();
        logger.log(System.Logger.Level.INFO, "Commit: {0}", git.getCommitId());
        logger.log(System.Logger.Level.INFO, "Built at {0}", git.getBuildTime());
        this.splashScreen = this.splashScreenProvider.buildSplashScreen();
        this.splashScreen.setName(this.applicationName);
        this.splashScreen.display();
        Optional.ofNullable(this.updateUrl).ifPresent(this::update);
    }

    private void update(String url) {
        try {
            Configuration.read(
                    new HttpRequest().getReader(url))
                    .update();
        } catch (Exception e) {
            Logger.getLogger(Application.class).error(e);
        }
    }
}
