package view.gui;

import model.item.AbstractItem;
import model.item.Bill;
import model.item.Fee;
import model.item.ProductWithQuantity;
import model.manager.BillManager;
import model.sorting.FeeSortBy;
import model.sorting.QuantifiableProductSortBy;
import model.sorting.SortingConfig;
import model.util.Status;
import view.gui.itemDialog.AddFeeDialog;
import view.gui.itemDialog.AddProductDialog;
import view.gui.itemDialog.FeeDialog;
import view.gui.itemDialog.ProductDialog;
import view.gui.util.GenericDialogs;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public BillEditor(Bill bill) {
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

        addProductButton.addActionListener(e -> {
            ProductWithQuantity product = new AddProductDialog(this).getProduct();
            if (product == null) { return; }
            bill.addProduct(product);
            reload();
            unsavedChanges = true;
        });
        addFeeButton.addActionListener(e -> {
            Fee fee = new AddFeeDialog(this).getFee();
            if (fee == null) { return; }
            bill.addFee(fee);
            reload();
            unsavedChanges = true;
        });

        editProductButton.addActionListener(editButtonListener(
                "product",
                productsView,
                bill::editProduct,
                p -> new ProductDialog(this, p.getId(), p, true, p.getQuantity()).getProductWithQuantity()
        ));
        editFeeButton.addActionListener(editButtonListener(
                "fee",
                feesView,
                bill::editFee,
                f -> new FeeDialog(this, f.getId(), f).getFee()
        ));

        saveButton.addActionListener(e -> {
            if (BillManager.getInstance().save() == Status.OK ) {
                unsavedChanges = false;
                JOptionPane.showMessageDialog(this, "Changes saved.", "Saving Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Saving was unsuccessful.", "Saving Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        billStateSelector.addActionListener(e -> {
            String state = (String) billStateSelector.getSelectedItem();
            switch (state) {
                case "Paid" -> bill.setAsPaid();
                case "Unpaid" -> bill.setAsUnpaid();
                case "Cancelled" -> bill.setAsCancelled();
                case null, default -> {}
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
        totalLabel.setText(String.format("$%.2f",bill.getTotal().doubleValue()));
    }

    private <T extends AbstractItem> ActionListener editButtonListener(String itemName, ItemViewPanel<T> itemView, Consumer<T> update, Function<T, T> dialog) {
        return e -> {
            T selected = itemView.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a " + itemName + " to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            T updated = dialog.apply(selected);

            if (updated == null) { return; }
            update.accept(updated);
            unsavedChanges = true;
            reload();
        };
    }

    private <T extends AbstractItem> ActionListener removeButtonListener(String itemName, ItemViewPanel<T> itemView, Consumer<Integer> removeFunction) {
        return e -> {
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
