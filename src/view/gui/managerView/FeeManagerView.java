package view.gui.managerView;

import model.item.Fee;
import model.manager.FeeManager;
import model.sorting.FeeSortBy;
import org.jetbrains.annotations.NotNull;
import view.gui.itemDialog.FeeDialog;

public class FeeManagerView extends AbstractManagerView<Fee> {
    public FeeManagerView() {
        super(FeeManager.getInstance(), "Fee", FeeSortBy.values());
    }

    @Override
    protected Fee newItemPopUp(int idCounter) {
        FeeDialog dialog = new FeeDialog(this, getManager().getIdCounter(), null);
        return dialog.getFee();
    }

    @Override
    protected Fee editItemPopIp(@NotNull Fee originalItem) {
        FeeDialog dialog = new FeeDialog(this, originalItem.getId(), originalItem);
        return dialog.getFee();
    }
}
