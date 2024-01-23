/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2018-2024 Grégory Van den Borre
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

import be.yildizgames.common.application.helper.cli.Banner;
import be.yildizgames.common.application.helper.cli.BannerLine;
import be.yildizgames.common.application.helper.logging.LoggerPropertiesConsoleFile;
import be.yildizgames.common.application.helper.splashscreen.EmptySplashScreen;
import be.yildizgames.common.application.helper.splashscreen.SplashScreenProvider;
import be.yildizgames.common.application.helper.splashscreen.UpdateSplashScreen;
import be.yildizgames.common.application.helper.updater.UpdateHelper;
import be.yildizgames.common.configuration.ConfigurationNotFoundAdditionalBehavior;
import be.yildizgames.common.configuration.ConfigurationNotFoundDefault;
import be.yildizgames.common.configuration.ConfigurationRetriever;
import be.yildizgames.common.configuration.ConfigurationRetrieverFactory;
import be.yildizgames.common.configuration.parameter.ApplicationArgs;
import be.yildizgames.common.git.GitProperties;
import be.yildizgames.common.git.GitPropertiesProvider;
import be.yildizgames.common.logging.LogEngineProvider;
import be.yildizgames.common.logging.LoggerPropertiesConfiguration;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Main application class.
 * This class is responsible for:
 * Configuring the application (logging, etc)
 * Displaying a banner on startup
 * Managing application updates
 *
 * @author Grégory Van den Borre
 */
public class Application {

    /**
     * Name of the application, never null.
     */
    private final String applicationName;

    /**
     * Url to update, null value will prevent to try to update.
     */
    private String updateUrl;

    /**
     * Timeout for the update connection, -1 means no timeout.
     */
    private int updateTimeOut = -1;

    /**
     * Properties to store the application configuration.
     */
    private Properties properties = new Properties();

    /**
     * Flag to check if application is started or not.
     */
    private boolean started;

    /**
     * Banner to display when the application starts, never null.
     */
    private Banner banner;

    /**
     * Build the splashscreen, never null.
     */
    private SplashScreenProvider splashScreenProvider = p -> new EmptySplashScreen();

    /**
     * Splashscreen, built in init function.
     */
    private UpdateSplashScreen splashScreen;

    /**
     * Constructor, private to force using the static function start instead.
     *
     * @param applicationName Name of the application.
     */
    private Application(final String applicationName) {
        super();
        shutThirdPartyLogger();
        this.applicationName = Objects.requireNonNull(applicationName);
        this.banner = new Banner(applicationName);
        if (applicationName.isEmpty()) {
            throw new IllegalArgumentException("Application name cannot be empty");
        }
    }

    /**
     * Prevent to display the 3rd parties loggers.
     */
    private static void shutThirdPartyLogger() {
        var databaseLogger = java.util.logging.Logger.getLogger("hsqldb.db");
        databaseLogger.setUseParentHandlers(false);
        databaseLogger.setLevel(java.util.logging.Level.WARNING);
    }

    /**
     * Prepare to start the application.
     *
     * @param applicationName Application name.
     * @return The application.
     */
    public static Application prepare(String applicationName) {
        return new Application(applicationName);
    }

    /**
     * Add configuration to the application.
     *
     * @param args          Arguments to override the configuration.
     * @param defaultConfig Default configuration to use for values not provided.
     * @param behavior      Behavior to run if not configuration is provided.
     * @return The application.
     */
    public final Application withConfiguration(String[] args, Properties defaultConfig, ConfigurationNotFoundAdditionalBehavior behavior) {
        ConfigurationRetriever configurationRetriever = ConfigurationRetrieverFactory
                .fromFile(ConfigurationNotFoundDefault.fromDefault(Stream.of(new LoggerPropertiesConsoleFile(applicationName), defaultConfig)
                        .collect(Properties::new, Map::putAll, Map::putAll), behavior));
        this.properties = configurationRetriever.retrieveFromArgs(ApplicationArgs.of(args));
        return this;
    }

    /**
     * To set a custom banner, the banner will be displayed in the console interface when starting the application.
     *
     * @param banner Custom banner to set.
     * @return The application.
     */
    public final Application withBanner(Banner banner) {
        this.banner = Objects.requireNonNull(banner);
        return this;
    }

    /**
     * Set a splash screen to display when starting the application.
     *
     * @param splashScreen Splash screen to display.
     * @return The application.
     */
    public final Application withSplashScreen(SplashScreenProvider splashScreen) {
        this.splashScreenProvider = Objects.requireNonNull(splashScreen);
        return this;
    }

    /**
     * Add a line to the banner.
     *
     * @param line Line to add.
     * @return The application.
     */
    public final Application addBannerLine(BannerLine line) {
        this.banner.addLine(line);
        return this;
    }

    /**
     * Add configuration to the application.
     *
     * @param args          Arguments to override the configuration.
     * @param defaultConfig Default configuration to use for values not provided.
     * @return The application.
     */
    public final Application withConfiguration(String[] args, Properties defaultConfig) {
        return this.withConfiguration(args, defaultConfig, () -> {
        });
    }

    /**
     * Provide the update mechanism.
     *
     * @param url Url to call to get the update manifest.
     * @return The application.
     */
    public final Application withUpdate(String url) {
        this.updateUrl = Objects.requireNonNull(url);
        return this;
    }

    /**
     * Provide the update mechanism.
     *
     * @param url     Url to call to get the update manifest.
     * @param timeout Timeout for the update connection.
     * @return The application.
     */
    public final Application withUpdate(String url, int timeout) {
        this.updateUrl = Objects.requireNonNull(url);
        this.updateTimeOut = timeout;
        return this;
    }

    /**
     * Initialize and start the application.
     *
     * @return The application.
     */
    public final Application start() {
        if (this.started) {
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

    /**
     * Initialize and start the application by using a starter.
     * Provide support to log all uncaught exceptions.
     *
     * @param starter Starting application.
     * @return The application.
     */
    public final Application start(final Starter starter) {
        if (this.started) {
            return this;
        }
        try {
            init();
            starter.setApplication(this);
            starter.startLoggedErrors();
            this.started = true;
            this.applicationStarted();
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * To call once the application is started, this will close the splashscreen.
     */
    public final void applicationStarted() {
        this.splashScreen.close();
    }

    /**
     * Get the application configuration.
     * @return The configuration properties.
     */
    public final Properties getConfiguration() {
        return this.properties;
    }

    /**
     * Initialize the application before starting.
     * @throws IOException If something wrong occurs.
     */
    private void init() throws IOException {
        var logEngine = LogEngineProvider.getLoggerProvider().getLogEngine();
        logEngine.configureFromProperties(LoggerPropertiesConfiguration.fromProperties(this.properties));
        System.Logger logger = System.getLogger(Application.class.getName());
        this.banner.display();
        logger.log(System.Logger.Level.INFO, "Starting {0} (PID:{1}).", this.applicationName, ProcessHandle.current().pid());
        GitProperties git = GitPropertiesProvider.getGitProperties();
        logger.log(System.Logger.Level.INFO, "Commit: {0}", git.getCommitId());
        logger.log(System.Logger.Level.INFO, "Built at {0}", git.getBuildTime());
        this.splashScreen = this.splashScreenProvider.buildSplashScreen(this.properties);
        this.splashScreen.setName(this.applicationName);
        this.splashScreen.display();
        Optional.ofNullable(this.updateUrl).ifPresent(this::update);
    }

    /**
     * Update the application.
     * @param url Url to call to get the update manifest.
     */
    private void update(String url) {
        new UpdateHelper().update(url, "temp", Duration.ofMinutes(5), this.updateTimeOut, List.of(this.splashScreen));
    }
}
