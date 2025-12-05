package view.gui;

import view.gui.managerView.BillManagerView;
import view.gui.managerView.ClientManagerView;
import view.gui.managerView.FeeManagerView;
import view.gui.managerView.ProductManagerView;

import javax.swing.*;

public class Menu extends JFrame {
    private JButton productManagerButton;
    private JButton feeManagerButton;
    private JButton clientManagerButton;
    private JButton billManagerButton;
    private JPanel contentPanel;

    public Menu() {
        super("Biller v1.0 - Menu");

        setContentPane(contentPanel);

        setupListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupListeners() {
        productManagerButton.addActionListener(_ -> new ProductManagerView());
        feeManagerButton.addActionListener(_ -> new FeeManagerView());
        clientManagerButton.addActionListener(_ -> new ClientManagerView());
        billManagerButton.addActionListener(_ -> new BillManagerView());
    }
}
