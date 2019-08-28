package be.yildizgames.common.application;

public abstract class Starter {

    private Application application;

    void setApplication(Application application) {
        this.application = application;
    }

    public abstract void start();
}
