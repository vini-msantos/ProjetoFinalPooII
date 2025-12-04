package view.gui;

import view.App;
import view.AppFactory;

public class GuiAppFactory extends AppFactory {

    @Override
    public App newApp() {
        return new GuiApp();
    }
}
