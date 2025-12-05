package view.gui.itemDialog;

import model.item.AbstractItem;
import model.manager.AbstractManager;
import model.sorting.Sorter;
import view.gui.ItemViewPanel;
import view.gui.managerView.AbstractManagerView;

import javax.swing.*;
import java.awt.event.*;
import java.util.function.Supplier;

public class ItemSearchDialog<T extends AbstractItem> extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private ItemViewPanel<T> itemView;
    private T selected;

    public ItemSearchDialog(AbstractManager<T> manager, Sorter<T>[] sorters) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setupListeners();
        itemView.setupItemView(manager::searchName, manager, sorters);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupListeners() {
        buttonOK.addActionListener(_ -> onSelect());
        buttonCancel.addActionListener(_ -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(_ -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onSelect() {
        T selection = itemView.getSelectedValue();
        if (selection == null) {
            JOptionPane.showMessageDialog(this, "Please select an item on the list.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.selected = selection;
        dispose();
    }

    private void onCancel() {
        this.selected = null;
        dispose();
    }

    public T getSelected() {
        return selected;
    }
}
