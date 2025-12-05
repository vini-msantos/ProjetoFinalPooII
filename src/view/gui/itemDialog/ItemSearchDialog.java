package view.gui.itemDialog;

import model.item.AbstractItem;
import model.sorting.Sorter;
import model.sorting.SortingOption;
import view.gui.ItemViewPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemSearchDialog<T extends AbstractItem> extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private ItemViewPanel<T> itemView;
    private T selected;

    public ItemSearchDialog(
            Function<String, List<T>> searcher,
            Supplier<SortingOption<T>> getSort,
            Consumer<SortingOption<T>> setSort,
            Sorter<T>[] sorters
    ) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setupListeners();
        itemView.setupItemView(searcher, getSort, setSort, sorters);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupListeners() {
        buttonOK.addActionListener(e -> onSelect());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
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
