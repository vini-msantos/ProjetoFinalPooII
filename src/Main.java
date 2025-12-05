import view.App;
import view.AppFactory;
import view.console.ConsoleAppFactory;
import view.gui.GuiAppFactory;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (Arrays.asList(args).contains("--terminal")) {
            AppFactory.setConcreteFactory(new ConsoleAppFactory());
        } else {
            AppFactory.setConcreteFactory(new GuiAppFactory());
        }


        App app = AppFactory.getConcreteFactory().newApp();
        app.run();
    }
}
