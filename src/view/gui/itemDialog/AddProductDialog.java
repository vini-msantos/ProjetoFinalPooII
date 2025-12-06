package view.gui.itemDialog;

import model.item.Product;
import model.item.ProductWithQuantity;
import model.manager.ProductManager;
import view.gui.util.Fields;
import view.gui.util.GenericDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

public class AddProductDialog extends JDialog {
    private JPanel contentPane;
    private JButton createButton;
    private JButton cancelButton;
    private JFormattedTextField idField;
    private JButton openSearchButton;
    private JPanel mainPanel;
    private JFormattedTextField priceField;
    private JFormattedTextField quantityField;
    private JTextField nameField;
    private ProductWithQuantity productWithQuantity = null;

    public AddProductDialog(Component parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(createButton);
        setLocationRelativeTo(parent);

        setTitle("Adding Product");

        Fields.editIdField(idField);
        Fields.editNameField(nameField, null);
        Fields.editPriceField(priceField, null);
        Fields.editQuantityField(quantityField, null);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void setupListeners() {
        createButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
        openSearchButton.addActionListener(e -> openSearch());

        idField.addActionListener(e -> {
            nameField.requestFocusInWindow();
            reload();
        });
        nameField.addActionListener(e -> priceField.requestFocusInWindow());
        priceField.addActionListener(e -> quantityField.requestFocusInWindow());
        quantityField.addActionListener(e -> onOK());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void reload() {
        if (!Fields.validId(idField)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        ProductManager pm = ProductManager.getInstance();
        Product product = pm.get(id);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Given ID does not correspond to valid product.", "No Such ID Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        nameField.setText(product.getName());
        priceField.setText(product.getPrice().toString());
    }


    private void openSearch() {
        Product product = GenericDialogs.searchProduct(this);
        if (product == null) { return; }
        idField.setText(product.getId() + "");
        reload();

        priceField.requestFocusInWindow();
    }

    private void onOK() {
        if (!Fields.validId(idField) || nameField.getText().isBlank() || !Fields.validPrice(priceField) || !Fields.validQuantity(quantityField)) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        ProductManager pm = ProductManager.getInstance();
        Product product = pm.get(id);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Given ID does not correspond to valid product.", "No Such ID Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity = Integer.parseInt(quantityField.getText().trim());
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceField.getText().trim()));
        String name = nameField.getText().trim();

        productWithQuantity = new ProductWithQuantity(id, name, price, quantity);
        dispose();
    }

    private void onCancel() {
        productWithQuantity = null;
        dispose();
    }

    public ProductWithQuantity getProduct() {
        return productWithQuantity;
    }
}
