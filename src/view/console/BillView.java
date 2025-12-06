package view.console;

import model.item.Bill;
import model.manager.BillManager;

/**
 * Classe responsável pela representação textual da tela de edição de comanda.
 * Possuí métodos que permitem a rolagem das listas de produtos e taxas.
 */
class BillView {
    private static final int PRODUCTS_PER_PAGE = 12;
    private static final int FEES_PER_PAGE = 8;
    private final Bill bill;
    private int productPage;
    private int feePage;

    BillView(Bill bill) {
        this.bill = bill;
        this.productPage = 0;
        this.feePage = 0;
    }

    static BillView fromId(int id) {
        Bill view = BillManager.getInstance().get(id);
        if (view == null) {
            System.out.println("ERROR: No bill with such id: " + id);
            return null;
        }
        return new BillView(view);
    }

    void displayBill() {
        System.out.println("[---- Editing " + bill.getClientName() + "'s bill of id " + bill.getId() + " ----]\n");
        System.out.println("[---- Products ----]");
        Display.displayTruncatedList(bill.listProducts(), productPage, PRODUCTS_PER_PAGE);
        System.out.println("[------------------]\n");

        System.out.println("[---- Fees ----]");
        Display.displayTruncatedList(bill.listFees(), feePage, FEES_PER_PAGE);
        System.out.println("[--------------]\n");

        System.out.println("[---- Total of $" + String.format("%.2f", bill.getTotal()) + "        Status: " + bill.getState() + " ----]");
    }

    void scrollProducts(int amount) {
        int total = bill.listProducts().size();
        productPage = Math.clamp(productPage + amount, 0, (total-1)/PRODUCTS_PER_PAGE);
    }

    void scrollFees(int amount) {
        int total = bill.listFees().size();
        feePage = Math.clamp(feePage + amount, 0, (total-1)/FEES_PER_PAGE);
    }

    Bill getBill() {
        return bill;
    }
}
