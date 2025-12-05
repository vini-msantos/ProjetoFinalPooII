package view.gui.itemDialog;

import model.item.Product;
import model.item.ProductWithQuantity;
import view.gui.util.FieldFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

public class ProductDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCreate;
    private JButton buttonCancel;
    private JPanel mainPanel;
    private JTextField nameField;
    private JFormattedTextField priceField;
    private JFormattedTextField quantityField;
    private JLabel quantityLabel;
    private Product product = null;
    private ProductWithQuantity productWithQuantity = null;
    private final boolean withQuantity;
    private final int idCounter;

    public ProductDialog(Component parentComponent, int idCounter, Product template, boolean withQuantity, Integer templateQuantity) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCreate);
        setLocationRelativeTo(parentComponent);

        this.withQuantity = withQuantity;
        this.idCounter = idCounter;

        String templateName = null;
        Double templatePrice = null;
        if (template != null) {
            templateName = template.getName();
            templatePrice = template.getPrice().doubleValue();
            buttonCreate.setText("Edit");
        }

        String title = (template == null) ? "Creating" : "Editing";
        mainPanel.setBorder(BorderFactory.createTitledBorder(title + " product of ID " + idCounter));
        setTitle(title + " Product");

        FieldFactory.editNameField(nameField, templateName);
        FieldFactory.editPriceField(priceField, templatePrice);
        FieldFactory.editQuantityField(quantityField, templateQuantity);

        quantityLabel.setVisible(withQuantity);
        quantityField.setVisible(withQuantity);

        setupListeners();

        pack();
        setVisible(true);
    }

    private void onCreate() {
        if (
                nameField.getText().isBlank()
                || !FieldFactory.validPrice(priceField)
                || (withQuantity && !FieldFactory.validQuantity(quantityField))
        ) {
            JOptionPane.showMessageDialog(this, "Fill out all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceField.getText().trim()));

        if (withQuantity) {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            productWithQuantity = new ProductWithQuantity(idCounter, name, price, quantity);
        } else {
            product = new Product(idCounter, name, price);
        }

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

        if (withQuantity) {
            quantityField.addActionListener(e -> onCreate());
        } else {
            priceField.addActionListener(e -> onCreate());
        }

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        product = null;
        productWithQuantity = null;
        dispose();
    }

    public Product getProduct() {
        return product;
    }

    public ProductWithQuantity getProductWithQuantity() {
        return productWithQuantity;
    }

}
