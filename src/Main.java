import view.App;
import view.AppFactory;
import view.console.ConsoleAppFactory;
import view.gui.GuiAppFactory;

public class Main {
    public static void main(String[] args) {
//        for (String arg: args) {
//            if (arg.equals("--gui")) {
//                AppFactory.setConcreteFactory(new ConsoleAppFactory());
//            } else if (arg.equals("--terminal")) {
                AppFactory.setConcreteFactory(new GuiAppFactory());
//            }
//        }


        App app = AppFactory.getConcreteFactory().newApp();
        app.run();
    }
}
