package view.gui;

import model.item.AbstractItem;
import model.item.Bill;
import model.item.Fee;
import model.item.ProductWithQuantity;
import model.manager.BillManager;
import model.manager.ClientManager;
import model.sorting.FeeSortBy;
import model.sorting.QuantifiableProductSortBy;
import model.sorting.SortingConfig;
import model.util.Status;
import org.jetbrains.annotations.NotNull;
import view.gui.itemDialog.AddProductDialog;
import view.gui.util.GenericDialogs;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.Consumer;

public class BillEditor extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JButton addProductButton;
    private JButton removeProductButton;
    private JButton editProductButton;
    private JButton addFeeButton;
    private JButton editFeeButton;
    private JButton removeFeeButton;
    private JComboBox<String> billStateSelector;
    private JButton saveButton;
    private JPanel productButtonPanel;
    private JPanel feeButtonPanel;
    private JPanel billControlPanel;
    private JLabel billLabel;
    private ItemViewPanel<Fee> feesView;
    private ItemViewPanel<ProductWithQuantity> productsView;
    private JLabel totalLabel;
    private final Bill bill;
    private boolean unsavedChanges = false;

    public BillEditor(@NotNull Bill bill) {
        this.bill = bill;
        setContentPane(mainPanel);

        billStateSelector.setModel(new DefaultComboBoxModel<>(new String[]{"Unpaid", "Paid", "Cancelled"}));
        billStateSelector.setSelectedItem("Unpaid");

        billLabel.setText(bill.getClientName() + "'s bill of ID " + bill.getId());

        productsView.setupItemView(
                bill::searchProducts,
                SortingConfig::getQuantifiableProductSortingOption,
                SortingConfig::setQuantifiableProductSortingOption,
                QuantifiableProductSortBy.values()
        );

        feesView.setupItemView(
                bill::searchFees,
                SortingConfig::getFeeSortingOption,
                SortingConfig::setFeeSortingOption,
                FeeSortBy.values()
        );

        setupListeners();

        reload();

        pack();
        setVisible(true);
    }

    private void setupListeners() {
        removeProductButton.addActionListener(removeButtonListener(
                "product",
                productsView,
                bill::removeProduct
        ));
        removeFeeButton.addActionListener(removeButtonListener(
                "fee",
                feesView,
                bill::removeFee
        ));
        addProductButton.addActionListener(_ -> {
            ProductWithQuantity product = new AddProductDialog(this).getProduct();
            if (product == null) { return; }
            bill.addProduct(product);
            unsavedChanges = true;
        });



        saveButton.addActionListener(_ -> {
            if (BillManager.getInstance().save() == Status.OK ) {
                unsavedChanges = false;
                JOptionPane.showMessageDialog(this, "Changes saved.", "Saving Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Saving was unsuccessful.", "Saving Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            if (GenericDialogs.checkUnsavedChanges(mainPanel, unsavedChanges)) {
                dispose();
            }
            }
        });
    }

    private void reload() {
        productsView.reloadList();
        feesView.reloadList();

        totalLabel.setText("$" + bill.getTotal());
    }

    private <T extends AbstractItem> ActionListener removeButtonListener(String itemName, ItemViewPanel<T> itemView, Consumer<Integer> removeFunction) {
        return _ -> {
            List<T> selected = itemView.getSelectedValuesList();
            if (selected == null || selected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select one or more " + itemName + "s to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (GenericDialogs.confirmDelete(this, selected.size())) {
                for (T item : selected) {
                    removeFunction.accept(item.getId());
                }
                unsavedChanges = true;
                reload();
            }
        };
    }
}
