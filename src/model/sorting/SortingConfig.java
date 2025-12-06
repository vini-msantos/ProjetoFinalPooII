package model.sorting;

import model.item.*;
import model.util.Serializer;
import model.util.Status;

import java.io.Serializable;


/**
 * Classe singleton que armazena as configurações de ordenamento para todas os
 * tipos de itens.
 */
public class SortingConfig implements Serializable {
    private static final String filePath = "sortingConfig.ser";
    private static SortingConfig instance;
    private SortingOption<Product> productSortingOption;
    private SortingOption<ProductWithQuantity> quantifiableProductSortingOption;
    private SortingOption<Fee> feeSortingOption;
    private SortingOption<Client> clientSortingOption;
    private SortingOption<Bill> billSortingOption;

    private SortingConfig() {
        billSortingOption = new SortingOption<>(BillSortBy.ID, true);
        productSortingOption = new SortingOption<>(ProductSortBy.NAME, false);
        quantifiableProductSortingOption = new SortingOption<>(QuantifiableProductSortBy.NAME, false);
        clientSortingOption = new SortingOption<>(ClientSortBy.NAME, false);
        feeSortingOption = new SortingOption<>(FeeSortBy.NAME, false);
    }

    public static SortingConfig getInstance() {
        if (instance == null) {
            instance = load();
        }

        return instance;
    }


    public static SortingOption<Product> getProductSortingOption() {
        return SortingConfig.getInstance().productSortingOption;
    }

    public static void setProductSortingOption(SortingOption<Product> productSortingOption) {
        SortingConfig.getInstance().productSortingOption = productSortingOption;
    }

    public static SortingOption<ProductWithQuantity> getQuantifiableProductSortingOption() {
        return SortingConfig.getInstance().quantifiableProductSortingOption;
    }

    public static void setQuantifiableProductSortingOption(SortingOption<ProductWithQuantity> quantifiableProductSortingOption) {
        SortingConfig.getInstance().quantifiableProductSortingOption = quantifiableProductSortingOption;
    }

    public static SortingOption<Fee> getFeeSortingOption() {
        return SortingConfig.getInstance().feeSortingOption;
    }

    public static void setFeeSortingOption(SortingOption<Fee> feeSortingOption) {
        SortingConfig.getInstance().feeSortingOption = feeSortingOption;
    }

    public static SortingOption<Client> getClientSortingOption() {
        return SortingConfig.getInstance().clientSortingOption;
    }

    public static void setClientSortingOption(SortingOption<Client> clientSortingOption) {
        SortingConfig.getInstance().clientSortingOption = clientSortingOption;
    }

    public static SortingOption<Bill> getBillSortingOption() {
        return SortingConfig.getInstance().billSortingOption;
    }

    public static void setBillSortingOption(SortingOption<Bill> billSortingOption) {
        SortingConfig.getInstance().billSortingOption = billSortingOption;
    }

    public static SortingConfig load() {
        SortingConfig config = Serializer.load(filePath);
        if (config == null) {
            config = new SortingConfig();
        }

        return config;
    }

    public static Status save() {
        return Serializer.save(getInstance(), filePath);
    }
}
