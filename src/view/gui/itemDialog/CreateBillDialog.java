package view.gui.itemDialog;

import model.item.Bill;
import model.item.Client;
import model.manager.BillManager;
import model.manager.ClientManager;
import view.gui.util.Fields;
import view.gui.util.GenericDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        Fields.editIdField(clientIdField);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void setupListeners() {

        createButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
        openSearchButton.addActionListener(e -> openSearch());

        clientIdField.addActionListener(e -> onOK());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void openSearch() {
        Client client = GenericDialogs.searchClient(this);
        if (client == null) { return; }
        clientIdField.setText(client.getId() + "");
        clientIdField.requestFocusInWindow();

    }

    private void onOK() {
        if (!Fields.validId(clientIdField)) {
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
