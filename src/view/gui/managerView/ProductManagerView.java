package view.gui.managerView;

import model.item.Product;
import model.manager.ProductManager;
import model.sorting.ProductSortBy;
import org.jetbrains.annotations.NotNull;
import view.gui.ProductDialog;

import java.util.List;

public class ProductManagerView extends AbstractManagerView<Product> {
    public ProductManagerView() {
        super(ProductManager.getInstance(), List.of(ProductSortBy.values()), "Product Manager");
    }

    @Override
    protected Product newItemPopUp(int idCounter) {
        ProductDialog dialog = new ProductDialog(this, getManager().getIdCounter(), null, false, null);
        return dialog.getProduct();
    }

    @Override
    protected Product editItemPopIp(@NotNull Product originalItem) {
        ProductDialog dialog = new ProductDialog(this, originalItem.getId(), originalItem, false, null);
        return dialog.getProduct();
    }
}
