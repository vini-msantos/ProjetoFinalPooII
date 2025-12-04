package view.gui;

import view.App;
import view.gui.managerView.ProductManagerView;

import javax.swing.*;

public class GuiApp implements App {

    GuiApp() {}

    @Override
    public void run() {
        ProductManagerView pmv = new ProductManagerView();
    }

    private JFrame menu() {
        JFrame frame = new JFrame("Menu");
        frame.setSize(400, 500);
        frame.setVisible(true);

        Box box = Box.createVerticalBox();



        return frame;
    }
}
