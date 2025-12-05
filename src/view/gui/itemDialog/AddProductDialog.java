package view.gui.itemDialog;

import model.item.Bill;
import model.item.Client;
import model.item.Product;
import model.item.ProductWithQuantity;
import model.manager.BillManager;
import model.manager.ClientManager;
import model.manager.ProductManager;
import view.gui.util.FieldFactory;
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
    private JFormattedTextField IdField;
    private JButton openSearchButton;
    private JPanel mainPanel;
    private JFormattedTextField priceField;
    private JFormattedTextField quantityField;
    private ProductWithQuantity productWithQuantity = null;

    public AddProductDialog(Component parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(createButton);
        setLocationRelativeTo(parent);

        setTitle("Adding Product");

        FieldFactory.editIdField(IdField);
        FieldFactory.editPriceField(priceField, null);
        FieldFactory.editQuantityField(quantityField, 1);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void setupListeners() {

        createButton.addActionListener(_ -> onOK());
        cancelButton.addActionListener(_ -> onCancel());
        openSearchButton.addActionListener(_ -> openSearch());

        quantityField.addActionListener(_ -> onOK());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(_ -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void reload() {
        if (!FieldFactory.validId(IdField)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(IdField.getText().trim());
        ProductManager pm = ProductManager.getInstance();
        Product product = pm.get(id);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Given ID does not correspond to valid client.", "No Such ID Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        priceField.setText(product.getPrice().toString());
    }


    private void openSearch() {
        Product product = GenericDialogs.searchProduct(this);
        if (product == null) { return; }
        IdField.setText(product.getId() + "");
        reload();

        priceField.requestFocusInWindow();
    }

    private void onOK() {
        if (!FieldFactory.validId(IdField) || !FieldFactory.validPrice(priceField) || !FieldFactory.validQuantity(quantityField)) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(IdField.getText().trim());
        ProductManager pm = ProductManager.getInstance();
        Product product = pm.get(id);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Given ID does not correspond to valid client.", "No Such ID Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity = Integer.parseInt(quantityField.getText().trim());
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceField.getText().trim()));

        productWithQuantity = new ProductWithQuantity(id, product.getName(), price, quantity);
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
