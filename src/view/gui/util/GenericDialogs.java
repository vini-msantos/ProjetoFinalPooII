package view.gui.util;

import model.item.Client;
import model.item.Fee;
import model.item.Product;
import model.manager.ClientManager;
import model.manager.FeeManager;
import model.manager.ProductManager;
import model.sorting.ClientSortBy;
import model.sorting.FeeSortBy;
import model.sorting.ProductSortBy;
import model.sorting.SortingConfig;
import view.gui.itemDialog.ItemSearchDialog;

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
        ItemSearchDialog<Product> dialog = new ItemSearchDialog<>(
                ProductManager.getInstance()::searchName,
                SortingConfig::getProductSortingOption,
                SortingConfig::setProductSortingOption,
                ProductSortBy.values()
        );
        return dialog.getSelected();
    }

    public static Fee searchFee(Component parent) {
        ItemSearchDialog<Fee> dialog = new ItemSearchDialog<>(
                FeeManager.getInstance()::searchName,
                SortingConfig::getFeeSortingOption,
                SortingConfig::setFeeSortingOption,
                FeeSortBy.values()
        );
        return dialog.getSelected();
    }

    public static Client searchClient(Component parent) {
        ItemSearchDialog<Client> dialog = new ItemSearchDialog<>(
                ClientManager.getInstance()::searchName,
                SortingConfig::getClientSortingOption,
                SortingConfig::setClientSortingOption,
                ClientSortBy.values()
        );
        return dialog.getSelected();
    }
}
