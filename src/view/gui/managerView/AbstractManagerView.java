package view.gui.managerView;

import model.item.AbstractItem;
import model.manager.AbstractManager;
import model.sorting.Sorter;
import model.util.Status;
import view.gui.util.GenericDialogs;
import view.gui.ItemViewPanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public abstract class AbstractManagerView<T extends AbstractItem> extends JFrame {
    private JPanel panel;
    private JButton createItemButton;
    private JButton editItemButton;
    private JButton deleteItemButton;
    private JButton saveButton;
    private ItemViewPanel<T> itemViewPanel;
    private final AbstractManager<T> manager;
    private boolean unsavedChanges = false;
    private final String itemName;

    public AbstractManagerView(AbstractManager<T> manager, String itemName, Sorter<T>[] sorters) {
        super(itemName + " Manager");

        this.manager = manager;
        this.itemName = itemName;

        this.setContentPane(panel);

        createItemButton.setText("Create " + itemName);
        deleteItemButton.setText("Delete " + itemName);
        editItemButton.setText("Edit " + itemName);
        itemViewPanel.setupItemView(manager::searchName, manager::getSortingOption, manager::setSortingOption, sorters);

        setupListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupListeners() {

        deleteItemButton.addActionListener(_ -> {
            List<T> selected = itemViewPanel.getSelectedValuesList();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select one or more " + itemName.toLowerCase() + "s to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (GenericDialogs.confirmDelete(this, selected.size())) {
                for (T item : selected) {
                    manager.remove(item.getId());
                }
                unsavedChanges = true;
                itemViewPanel.reloadList();
            }
        });

        createItemButton.addActionListener(_ -> {
            T newItem = newItemPopUp(manager.getIdCounter());
            if (newItem == null) { return; }
            manager.add(newItem);
            unsavedChanges = true;
            itemViewPanel.reloadList();
        });

        editItemButton.addActionListener(_ -> {
            T selected = itemViewPanel.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a " + itemName.toLowerCase() + " to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            T updated = editItemPopUp(selected);

            if (updated == null) { return; }
            manager.update(updated);
            unsavedChanges = true;
            itemViewPanel.reloadList();
        });

        saveButton.addActionListener(_ -> {
            if (manager.save() == Status.OK ) {
                unsavedChanges = false;
                JOptionPane.showMessageDialog(this, "Changes saved.", "Saving Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Saving was unsuccessful.", "Saving Error", JOptionPane.ERROR_MESSAGE);
            }
        });

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

    protected abstract T editItemPopUp(T originalItem);
}