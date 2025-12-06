package view.gui.itemDialog;

import model.item.Client;
import view.gui.util.Fields;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCreate;
    private JButton buttonCancel;
    private JPanel mainPanel;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private Client client = null;
    private final int idCounter;

    public ClientDialog(Component parentComponent, int idCounter, Client template) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCreate);
        setLocationRelativeTo(parentComponent);

        this.idCounter = idCounter;

        String templateName = null;
        String templatePhoneNumber = null;
        if (template != null) {
            templateName = template.getName();
            templatePhoneNumber = template.getPhoneNumber();
            buttonCreate.setText("Edit");
        }

        String title = (template == null) ? "Creating" : "Editing";
        mainPanel.setBorder(BorderFactory.createTitledBorder(title + " client of ID " + idCounter));
        setTitle(title + " Client");

        Fields.editNameField(nameField, templateName);
        Fields.editNameField(phoneNumberField, templatePhoneNumber);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void onCreate() {
        if (nameField.getText().isBlank() || phoneNumberField.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Fill out all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();

        client = new Client(idCounter, name, phoneNumber);

        dispose();
    }

    private void setupListeners() {
        buttonCreate.addActionListener(e -> onCreate());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        phoneNumberField.addActionListener(e -> onCreate());

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        client = null;
        dispose();
    }

    public Client getClient() {
        return client;
    }
}
