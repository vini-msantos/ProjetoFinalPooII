package view.console;

import view.App;
import view.AppFactory;

public class ConsoleAppFactory extends AppFactory {
    @Override
    public App newApp() {
        return new ConsoleApp();
    }
}
