package view.gui.managerView;

import model.item.AbstractItem;
import model.manager.AbstractManager;
import model.sorting.Sorter;
import model.util.Status;
import view.gui.ItemViewPanel;
import view.gui.util.GenericDialogs;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;


/**
 * Classe que define o comportamento padrão dos gerenciadores de itens.
 * Permite a criação, edição e remoção de registros.
 * @param <T> Tipo do item que vai ser gerenciado.
 */
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

        deleteItemButton.addActionListener(e -> {
            List<T> selected = itemViewPanel.getSelectedValuesList();
            if (selected == null || selected.isEmpty()) {
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

        createItemButton.addActionListener(e -> {
            T newItem = newItemPopUp(manager.getIdCounter());
            if (newItem == null) { return; }
            manager.add(newItem);
            unsavedChanges = true;
            itemViewPanel.reloadList();
        });

        editItemButton.addActionListener(e -> {
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

        saveButton.addActionListener(e -> {
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

    protected AbstractManager<T> getManager() {
        return manager;
    }

    /**
     * Método abstrato que solicita as informações ao usuário para
     * cadastrar um novo item.
     * @param idCounter Valor atual do contador de ID do gerenciador do item.
     * @return Um novo item criado. Pode ser null caso o usuário cancele a operação
     */
    protected abstract T newItemPopUp(int idCounter);


    /**
     * Método abstrato que abre um diálogo para a edição das informações de um item.
     * @param originalItem Item que vai ser modificado.
     * @return Item resultante da edição. Pode ser null caso o usuário cancele a operação.
     */
    protected abstract T editItemPopUp(T originalItem);
}