package be.yildizgames.common.application;

import java.util.Properties;

public abstract class Starter {

    private Application application;

    void setApplication(Application application) {
        this.application = application;
    }

    public abstract void start();

    protected final Properties getApplicationProperties() {
        return this.application.getConfiguration();
    }
}
