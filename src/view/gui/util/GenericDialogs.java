package view.gui.util;

import model.item.Bill;
import model.item.Client;
import model.item.Fee;
import model.item.Product;
import model.manager.BillManager;
import model.manager.ClientManager;
import model.manager.FeeManager;
import model.manager.ProductManager;
import model.sorting.BillSortBy;
import model.sorting.ClientSortBy;
import model.sorting.FeeSortBy;
import model.sorting.ProductSortBy;
import view.gui.itemDialog.ItemSearchDialog;
import view.gui.managerView.ClientManagerView;
import view.gui.managerView.FeeManagerView;
import view.gui.managerView.ProductManagerView;

import javax.swing.*;
import java.awt.*;

public class GenericDialogs {
    public static boolean checkUnsavedChanges(Component parent, boolean hasUnsavedChanges) {
        if (!hasUnsavedChanges) { return true; }
        int answer = JOptionPane.showConfirmDialog(parent,
                "You have unsaved changes, close anyways?",
                "Unsaved Changes",
                JOptionPane.YES_NO_OPTION
        );
        return answer == JOptionPane.YES_OPTION;
    }
    
    public static boolean confirmDelete(Component parent, int numberOfElements) {
        String plural = (numberOfElements > 1) ? "s" : "";
        int response = JOptionPane.showConfirmDialog(parent,
                "You are about to delete " + numberOfElements + " item" + plural + ", proceed with operation?",
                "Confirm delete", JOptionPane.YES_NO_OPTION);
        
        return response == JOptionPane.YES_OPTION;
    }
    
    public static Product searchProduct(Component parent) {
        ItemSearchDialog<Product> dialog = new ItemSearchDialog<>(ProductManager.getInstance(), ProductSortBy.values(), ProductManagerView::new);
        return dialog.getSelected();
    }

    public static Fee searchFee(Component parent) {
        ItemSearchDialog<Fee> dialog = new ItemSearchDialog<>(FeeManager.getInstance(), FeeSortBy.values(), FeeManagerView::new);
        return dialog.getSelected();
    }

    public static Client searchClient(Component parent) {
        ItemSearchDialog<Client> dialog = new ItemSearchDialog<>(ClientManager.getInstance(), ClientSortBy.values(), ClientManagerView::new);
        return dialog.getSelected();
    }

    public static Bill searchBill(Component parent) {
        ItemSearchDialog<Bill> dialog = new ItemSearchDialog<>(BillManager.getInstance(), BillSortBy.values(), () -> {});
        return dialog.getSelected();
    }
}
