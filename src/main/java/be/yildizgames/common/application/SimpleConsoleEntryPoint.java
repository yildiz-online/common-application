package be.yildizgames.common.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public abstract class SimpleConsoleEntryPoint {

    public void launch(String[] args) {
        Application.prepare(getApplicationName())
                .withConfiguration(args, prepareDefaultConfiguration())
                .start(getStarter());
    }

    private Properties prepareDefaultConfiguration() {
        var config = Path.of("config");
        if (Files.notExists(config)) {
            try {
                Files.createDirectory(config);
            } catch (IOException e) {
                throw new IllegalStateException("Config directory could not be created.", e);
            }
        }
        return getDefaultConfiguration();
    }

    protected abstract Properties getDefaultConfiguration();

    protected abstract String getApplicationName();

    /**
     * Provide the starter.
     *
     * @return The starter.
     */
    protected abstract Starter getStarter();
}
