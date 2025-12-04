package view.gui;

import model.item.Product;
import model.item.ProductWithQuantity;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class ProductDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCreate;
    private JButton buttonCancel;
    private JPanel mainPanel;
    private JTextField nameField;
    private JFormattedTextField priceField;
    private JLabel productIdLabel;
    private JFormattedTextField quantityField;
    private JLabel quantityLabel;
    private Product product = null;
    private ProductWithQuantity productWithQuantity = null;
    private final boolean withQuantity;
    private final int idCounter;

    public ProductDialog(Component parentComponent, int idCounter, @Nullable Product template, boolean withQuantity, Integer templateQuantity) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCreate);
        setLocationRelativeTo(parentComponent);

        this.withQuantity = withQuantity;
        this.idCounter = idCounter;

        String title;
        String templateName = null;
        Double templatePrice = null;
        if (template == null) {
            title = "Creating product with ID " + idCounter;
        } else {
            title = "Editing product with ID " + idCounter;
            buttonCreate.setText("Edit");
            templateName = template.getName();
            templatePrice = template.getPrice().doubleValue();
        }

        mainPanel.setBorder(BorderFactory.createTitledBorder(title));

        nameField.setText(templateName);
        FormattedFieldFactory.editPriceField(priceField, templatePrice);
        FormattedFieldFactory.editQuantityField(quantityField, templateQuantity);

        quantityLabel.setVisible(withQuantity);
        quantityField.setVisible(withQuantity);

        buttonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCreate();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setVisible(true);
    }

    private void onCreate() {
        if (nameField.getText().isBlank() || !priceField.isValid() || (withQuantity && !quantityField.isValid())) {
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
