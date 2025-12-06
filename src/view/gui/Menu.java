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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A tela de menu permite o acesso as outras telas da aplicação, também permite o usuário
 * salvar as informações. Ao fechar o menu, a aplicação se encerra.
 */
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
        productManagerButton.addActionListener(e -> new ProductManagerView());
        feeManagerButton.addActionListener(e -> new FeeManagerView());
        clientManagerButton.addActionListener(e -> new ClientManagerView());
        billManagerButton.addActionListener(e -> new BillManagerView());
        saveButton.addActionListener(e -> {
            ClientManager.getInstance().save();
            FeeManager.getInstance().save();
            BillManager.getInstance().save();
            ProductManager.getInstance().save();
            SortingConfig.save();
            JOptionPane.showMessageDialog(this, "All changes saved.", "Saved.", JOptionPane.INFORMATION_MESSAGE);
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        contentPanel,
                        "Closing this window will quit the program.\nIf you have unsaved changes, save before closing\n\nDo you really want to quit?",
                        "Want to Quit?",
                        JOptionPane.YES_NO_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
}
