package view.gui.itemDialog;

import model.item.Fee;
import model.manager.FeeManager;
import view.gui.util.FieldFactory;
import view.gui.util.GenericDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

public class AddFeeDialog extends JDialog {
    private JPanel contentPane;
    private JButton createButton;
    private JButton cancelButton;
    private JFormattedTextField idField;
    private JButton openSearchButton;
    private JPanel mainPanel;
    private JFormattedTextField percentageField;
    private JTextField nameField;
    private JFormattedTextField quantityField;
    private Fee fee = null;

    public AddFeeDialog(Component parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(createButton);
        setLocationRelativeTo(parent);

        setTitle("Adding Fee");

        FieldFactory.editIdField(idField);
        FieldFactory.editNameField(nameField, null);
        FieldFactory.editPercentageField(percentageField, null);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void setupListeners() {

        createButton.addActionListener(_ -> onOK());
        cancelButton.addActionListener(_ -> onCancel());
        openSearchButton.addActionListener(_ -> openSearch());

        idField.addActionListener(_ -> {
            nameField.requestFocusInWindow();
            reload();
        });

        nameField.addActionListener(_ -> percentageField.requestFocusInWindow());
        percentageField.addActionListener(_ -> onOK());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(_ -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void reload() {
        if (!FieldFactory.validId(idField)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        FeeManager pm = FeeManager.getInstance();
        Fee fee = pm.get(id);
        if (fee == null) {
            JOptionPane.showMessageDialog(this, "Given ID does not correspond to valid fee.", "No Such ID Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        nameField.setText(fee.getName());
        percentageField.setText(fee.getPercentage().multiply(BigDecimal.valueOf(100)).intValue() + "");
    }


    private void openSearch() {
        Fee fee = GenericDialogs.searchFee(this);
        if (fee == null) { return; }
        idField.setText(fee.getId() + "");
        reload();

        percentageField.requestFocusInWindow();
    }

    private void onOK() {
        if (nameField.getText().isBlank() || !FieldFactory.validId(idField) || !FieldFactory.validPercentage(percentageField)) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        FeeManager fm = FeeManager.getInstance();
        Fee idFee = fm.get(id);
        if (idFee == null) {
            JOptionPane.showMessageDialog(this, "Given ID does not correspond to valid fee.", "No Such ID Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        BigDecimal percentage = BigDecimal.valueOf(Double.parseDouble(percentageField.getText().trim()) / 100);

        fee = new Fee(id, name, percentage);
        dispose();
    }

    private void onCancel() {
        fee = null;
        dispose();
    }

    public Fee getFee() {
        return fee;
    }
}
