package view.gui.managerView;

import model.item.AbstractItem;
import model.manager.AbstractManager;
import model.sorting.Sorter;
import model.sorting.SortingOption;
import model.util.Status;
import view.gui.GenericDialogs;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public abstract class AbstractManagerView<T extends AbstractItem> extends JFrame {
    private final AbstractManager<T> manager;


    private JPanel panel;
    private JList<T> itemList;
    private JButton createItemButton;
    private JButton editItemButton;
    private JButton deleteItemButton;
    private JScrollPane listScrollPane;
    private JPanel ButtonPane;
    private JComboBox<Object> sortOptionBox;
    private JCheckBox reversedCheckBox;
    private JButton saveButton;
    private JPanel listPanel;

    private final DefaultListModel<T> listModel;
    private boolean unsavedChanges = false;

    public AbstractManagerView(AbstractManager<T> manager, List<Sorter<T>> sortingOptions, String windowTitle) {
        super(windowTitle);

        this.manager = manager;
        sortOptionBox.setModel(new DefaultComboBoxModel<>(sortingOptions.toArray()));
        sortOptionBox.setSelectedItem(manager.getSortingOption().getSorter());

        listModel = new DefaultListModel<>();
        itemList.setModel(listModel);

        this.setContentPane(panel);

        reloadList();
        setupListeners();

        pack();
        setVisible(true);
    }

    private void reloadList() {
        List<T> newList = manager.list();
        listModel.clear();
        listModel.addAll(newList);
    }

    private void setupListeners() {

        deleteItemButton.addActionListener(_ -> {
            List<T> selected = itemList.getSelectedValuesList();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select items to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "You are about to delete " + selected.size() + " item(s), proceed with operation?",
                    "Confirm delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (T item : selected) {
                    manager.remove(item.getId());
                }
                unsavedChanges = true;
                reloadList();
            }
        });

        createItemButton.addActionListener(_ -> {
            T newItem = newItemPopUp(manager.getIdCounter());
            if (newItem == null) { return; }
            manager.add(newItem);
            unsavedChanges = true;
            reloadList();
        });

        editItemButton.addActionListener(_ -> {
            T selected = itemList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select an item to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            T updated = editItemPopIp(selected);

            if (updated == null) { return; }
            manager.update(updated);
            unsavedChanges = true;
            reloadList();
        });

        saveButton.addActionListener(_ -> {
            if (manager.save() == Status.OK ) {
                unsavedChanges = false;
                JOptionPane.showMessageDialog(this, "Changes saved.", "Saving Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Saving was unsuccessful.", "Saving Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        ActionListener changeSortOptionListener = _ -> {
            Sorter<T> sortOption = (Sorter<T>) sortOptionBox.getSelectedItem();
            boolean reversed = reversedCheckBox.isSelected();
            if (sortOption == null) { return; }

            manager.setSortingOption(new SortingOption<>(sortOption, reversed));
            reloadList();
        };

        sortOptionBox.addActionListener(changeSortOptionListener);
        reversedCheckBox.addActionListener(changeSortOptionListener);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (GenericDialogs.checkUnsavedChanges(panel, unsavedChanges)) {
                    dispose();
                }
            }
        });
    }

    public AbstractManager<T> getManager() {
        return manager;
    }

    protected abstract T newItemPopUp(int idCounter);

    protected abstract T editItemPopIp(T originalItem);
}