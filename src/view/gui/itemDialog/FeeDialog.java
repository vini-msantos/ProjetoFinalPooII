package view.gui.itemDialog;

import model.item.Fee;
import view.gui.util.FieldFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

public class FeeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCreate;
    private JButton buttonCancel;
    private JPanel mainPanel;
    private JTextField nameField;
    private JFormattedTextField percentageField;
    private Fee fee = null;
    private final int idCounter;

    public FeeDialog(Component parentComponent, int idCounter, Fee template) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCreate);
        setLocationRelativeTo(parentComponent);

        this.idCounter = idCounter;

        String templateName = null;
        Double templatePercentage = null;
        if (template != null) {
            templateName = template.getName();
            templatePercentage = template.getPercentage().doubleValue();
            buttonCreate.setText("Edit");
        }

        String title = (template == null) ? "Creating" : "Editing";
        mainPanel.setBorder(BorderFactory.createTitledBorder(title + " fee of ID " + idCounter));
        setTitle(title + " Fee");

        FieldFactory.editNameField(nameField, templateName);
        FieldFactory.editPercentageField(percentageField, templatePercentage);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void setupListeners() {
        buttonCreate.addActionListener(e -> onCreate());
        percentageField.addActionListener(e -> onCreate());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCreate() {
        if (nameField.getText().isBlank() || !FieldFactory.validPercentage(percentageField)) {
            JOptionPane.showMessageDialog(this, "Fill out all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        BigDecimal percentage = BigDecimal.valueOf(Double.parseDouble(percentageField.getText().trim()) /100);

        fee = new Fee(idCounter, name, percentage);
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
