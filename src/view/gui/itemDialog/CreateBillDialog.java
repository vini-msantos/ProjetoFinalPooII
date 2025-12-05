package view.gui.itemDialog;

import model.item.Bill;
import model.item.Client;
import model.manager.BillManager;
import model.manager.ClientManager;
import view.gui.util.FieldFactory;
import view.gui.util.GenericDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateBillDialog extends JDialog {
    private JPanel contentPane;
    private JButton createButton;
    private JButton cancelButton;
    private JFormattedTextField clientIdField;
    private JButton openSearchButton;
    private JPanel mainPanel;
    private Bill bill = null;

    public CreateBillDialog(Component parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(createButton);
        setLocationRelativeTo(parent);

        setTitle("Creating Bill");
        mainPanel.setBorder(BorderFactory.createTitledBorder("Creating bill of ID " + BillManager.getInstance().getIdCounter()));

        FieldFactory.editIdField(clientIdField);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void setupListeners() {

        createButton.addActionListener(_ -> onOK());
        cancelButton.addActionListener(_ -> onCancel());
        openSearchButton.addActionListener(_ -> openSearch());

        clientIdField.addActionListener(_ -> onOK());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(_ -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void openSearch() {
        Client client = GenericDialogs.searchClient(this);
        if (client == null) { return; }
        clientIdField.setText(client.getId() + "");
        clientIdField.requestFocusInWindow();

    }

    private void onOK() {
        if (!FieldFactory.validId(clientIdField)) {
            JOptionPane.showMessageDialog(this, "Fill out all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(clientIdField.getText().trim());
        ClientManager cm = ClientManager.getInstance();
        if (cm.get(id) == null) {
            JOptionPane.showMessageDialog(this, "Given ID does not correspond to valid client.", "No Such ID Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client client = cm.get(id);
        bill = new Bill(BillManager.getInstance().getIdCounter(), client);

        System.out.println(bill);
        dispose();
    }

    private void onCancel() {
        bill = null;
        dispose();
    }

    public Bill getBill() {
        return bill;
    }
}
