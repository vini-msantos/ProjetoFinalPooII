package view.console;

import model.item.Bill;
import model.manager.BillManager;

import java.math.RoundingMode;

public class BillView {
    private static final int PRODUCTS_PER_PAGE = 12;
    private static final int FEES_PER_PAGE = 8;
    private final Bill bill;
    private int productPage;
    private int feePage;

    public BillView(Bill bill) {
        this.bill = bill;
        this.productPage = 0;
        this.feePage = 0;
    }

    public static BillView fromId(int id) {
        Bill view = BillManager.getInstance().get(id);
        if (view == null) {
            System.out.println("ERROR: No bill with such id: " + id);
            return null;
        }
        return new BillView(view);
    }

    public void displayBill() {
        System.out.println("[---- Editing " + bill.getClientName() + "'s bill of id " + bill.getId() + " ----]\n");
        System.out.println("[---- Products ----]");
        Display.displayTruncatedList(bill.listProducts(), productPage, PRODUCTS_PER_PAGE);
        System.out.println("[------------------]\n");

        System.out.println("[---- Fees ----]");
        Display.displayTruncatedList(bill.listFees(), feePage, FEES_PER_PAGE);
        System.out.println("[--------------]\n");

        System.out.println("[---- Total of $" + bill.getTotal().setScale(2, RoundingMode.HALF_UP) + "        Status: " + bill.getState() + " ----]");
    }

    public void scrollProducts(int amount) {
        int total = bill.listProducts().size();
        productPage = Math.clamp(productPage + amount, 0, (total-1)/PRODUCTS_PER_PAGE);
    }

    public void scrollFees(int amount) {
        int total = bill.listFees().size();
        feePage = Math.clamp(feePage + amount, 0, (total-1)/FEES_PER_PAGE);
    }

    public Bill getBill() {
        return bill;
    }
}
