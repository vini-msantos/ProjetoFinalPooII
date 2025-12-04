package view;

public abstract class AppFactory {
    private static AppFactory concreteFactory;

    public static AppFactory getConcreteFactory() {
        return concreteFactory;
    }

    public static void setConcreteFactory(AppFactory concreteFactory) {
        AppFactory.concreteFactory = concreteFactory;
    }

    public abstract App newApp();
}
