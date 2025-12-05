package view.gui;

import model.item.Bill;
import model.item.Fee;
import model.item.ProductWithQuantity;
import model.manager.ProductManager;
import model.sorting.FeeSortBy;
import model.sorting.ProductSortBy;
import model.sorting.QuantifiableProductSortBy;
import model.sorting.SortingConfig;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BillEditor extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private ItemViewPanel<Fee> feesView;
    private ItemViewPanel<ProductWithQuantity> productsView;
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
    private final Bill bill;

    public BillEditor(@NotNull Bill bill) {
        this.bill = bill;

        billStateSelector.setModel(new DefaultComboBoxModel<>(new String[]{"Unpaid", "Paid", "Cancelled"}));
        billStateSelector.setSelectedItem("Unpaid");

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

        pack();
        setVisible(true);
    }

    private void setupListeners() {
    }
}
