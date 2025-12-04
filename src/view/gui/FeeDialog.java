package view.gui;

import model.item.Fee;
import org.jetbrains.annotations.Nullable;

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

    public FeeDialog(Component parentComponent, int idCounter, @Nullable Fee template) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCreate);
        setLocationRelativeTo(parentComponent);

        this.idCounter = idCounter;

        String title;
        String templateName = null;
        Double templatePercentage = null;
        if (template == null) {
            title = "Creating fee with ID " + idCounter;
        } else {
            title = "Editing fee with ID " + idCounter;
            buttonCreate.setText("Edit");
            templateName = template.getName();
            templatePercentage = template.getPercentage().doubleValue();
        }

        mainPanel.setBorder(BorderFactory.createTitledBorder(title));

        FormattedFieldFactory.editNameField(nameField, templateName);
        FormattedFieldFactory.editPercentageField(percentageField, templatePercentage);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void setupListeners() {
        buttonCreate.addActionListener(_ -> onCreate());
        percentageField.addActionListener(_ -> onCreate());
        buttonCancel.addActionListener(_ -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(_ -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCreate() {
        if (nameField.getText().isBlank() || !percentageField.isValid()) {
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
