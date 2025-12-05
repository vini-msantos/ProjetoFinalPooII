package view.gui;

import model.manager.BillManager;
import model.manager.ClientManager;
import model.manager.FeeManager;
import model.manager.ProductManager;
import model.sorting.SortingConfig;
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
    private JButton saveButton;

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
        saveButton.addActionListener(_ -> {
            ClientManager.getInstance().save();
            FeeManager.getInstance().save();
            BillManager.getInstance().save();
            ProductManager.getInstance().save();
            SortingConfig.save();
            JOptionPane.showMessageDialog(this, "All changes saved.", "Saved.", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
