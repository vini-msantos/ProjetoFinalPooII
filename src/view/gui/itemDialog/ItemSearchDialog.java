package view.gui.itemDialog;

import model.item.AbstractItem;
import model.manager.AbstractManager;
import model.sorting.Sorter;
import view.gui.ItemViewPanel;

import javax.swing.*;
import java.awt.event.*;

public class ItemSearchDialog<T extends AbstractItem> extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private ItemViewPanel<T> itemView;
    private JButton openManagerButton;
    private T selected;

    public ItemSearchDialog(AbstractManager<T> manager, Sorter<T>[] sorters, Runnable openManager) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setupListeners(openManager);
        itemView.setupItemView(manager, sorters);

        pack();
        setVisible(true);
    }

    private void setupListeners(Runnable openManager) {
        buttonOK.addActionListener(_ -> onSelect());
        buttonCancel.addActionListener(_ -> onCancel());
        openManagerButton.addActionListener(_ -> openManager.run());

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
