package view.gui;

import model.item.AbstractItem;
import model.manager.AbstractManager;
import model.sorting.Sorter;
import model.sorting.SortingOption;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ItemViewPanel<T extends AbstractItem> extends JPanel {
    private JPanel listPanel;
    private JScrollPane listScrollPane;
    private JList<T> itemList;
    private JCheckBox reversedCheckBox;
    private JComboBox<Sorter<T>> sortOptionBox;
    private JTextField searchField;
    private JPanel mainPanel;
    private JButton searchButton;
    private AbstractManager<T> manager;
    private final DefaultListModel<T> listModel;

    public ItemViewPanel() {
        listModel = new DefaultListModel<>();
        itemList.setModel(listModel);
    }

    public void setupItemView(AbstractManager<T> manager, Sorter<T>[] sortingOptions) {
        sortOptionBox.setModel(new DefaultComboBoxModel<>(sortingOptions));
        sortOptionBox.setSelectedItem(manager.getSortingOption().getSorter());

        this.manager = manager;

        reloadList();
        setupListeners();
    }

    public void reloadList() {
        List<T> newList = manager.searchName(searchField.getText().trim());
        listModel.clear();
        listModel.addAll(newList);
    }

    public JPanel getPanel() {
        return listPanel;
    }

    public void setupListeners() {
        ActionListener changeSortOptionListener = _ -> {
            Sorter<T> sortOption = (Sorter<T>) sortOptionBox.getSelectedItem();
            boolean reversed = reversedCheckBox.isSelected();
            if (sortOption == null) { return; }

            manager.setSortingOption(new SortingOption<>(sortOption, reversed));
            reloadList();
        };

        sortOptionBox.addActionListener(changeSortOptionListener);
        reversedCheckBox.addActionListener(changeSortOptionListener);

        searchButton.addActionListener(_ -> reloadList());
        searchField.addActionListener(_ -> reloadList());
    }

    public List<T> getSelectedValuesList() {
        return itemList.getSelectedValuesList();
    }

    public T getSelectedValue() {
        return itemList.getSelectedValue();
    }
}
