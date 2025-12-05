package view.gui.managerView;

import model.item.Bill;
import model.manager.BillManager;
import model.sorting.BillSortBy;
import org.jetbrains.annotations.NotNull;
import view.gui.BillEditor;
import view.gui.itemDialog.CreateBillDialog;

public class BillManagerView extends AbstractManagerView<Bill> {
    public BillManagerView() {
        super(BillManager.getInstance(), "Bill", BillSortBy.values());
    }

    @Override
    protected Bill newItemPopUp(int idCounter) {
        CreateBillDialog dialog = new CreateBillDialog(this);
        return dialog.getBill();
    }

    @Override
    protected Bill editItemPopUp(@NotNull Bill originalItem) {
        new BillEditor(originalItem);
        return originalItem;
    }
}
