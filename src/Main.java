import view.App;
import view.AppFactory;
import view.console.ConsoleAppFactory;
import view.gui.GuiAppFactory;

import java.util.Arrays;

public class Main {
    // Obs: caso o modo de visualização não seja dado, a GUI é selecionada por padrão.
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
