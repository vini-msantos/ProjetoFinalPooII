package view.console;

import model.item.*;
import model.manager.*;
import model.sorting.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe responsável por lidar com os comandos do usuário, com uso de uma chain of responsibility.
 * <p> Um Handler é criado e associado ao comando inserido, depois são chamados os métodos que vão responder
 * a esse comando. Cada método primeiramente verifica se o comando já foi respondido e caso já tenha sido
 * ele não executa nada e passa adiante.</p>
 * <p>Caso nenhum método anterior tenha consumido o comando, o atual vai checar se o comando é de
 * sua responsabilidade e responder de acordo</p>
 */
public class Handler {
    private final String command;
    private boolean done = false;

    Handler(String command) {
        this.command = command;
    }


    /**
     * Método que retorna se o comando foi consumido por um dos métodos anteriores.
     * Vale mencionar que ele ter sido consumido não significa que o comando foi
     * válido e executado com sucesso.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Método responsável pela lógica do estado de edição da comanda. Caso
     * nenhuma comanda esteja sendo editada, também permite o usuário a entrar
     * em tal modo.
     */
    Handler handleBillContext(ConsoleApp app) {
        if (done) { return this; }

        if (app.inBillContext()) {
            Bill bill = app.getView().getBill();

            return this
                    .handleExitBillView(app)
                    .handleAddBillFee(bill)
                    .handleAddBillProduct(bill)
                    .handleRemoveBillItem(bill)
                    .handleScrollBillView(app.getView())
                    .handleEditBillFee(bill)
                    .handleEditBillProduct(bill)
                    .handleSetBillState(bill);

        } else {
            return handleEnterBillContext(app);
        }
    }

    /**
     * Método que permite o usuário mudar a opção de ordenamento para os diferentes tipos
     * de itens.
     */
    Handler handleSetSortOption() {
        if (done) { return this; }
        return this
                .handleSetBillSort()
                .handleSetClientSort()
                .handleSetFeeSort()
                .handleSetProductSort();
    }

    /**
     * Método responsável por permitir a criação, edição e remoção de itens
     * de seus respectivos registros (managers).
     */
    Handler handleEntries(ConsoleApp app) {
        if (done) { return this; }
        return this
                .handleCreateBill()
                .handleCreateFee()
                .handleCreateProduct()
                .handleCreateClient()
                .handleEditClientEntry()
                .handleEditFeeEntry()
                .handleEditProductEntry()
                .handleDeleteEntry(app);
    }

    /**
     * Método responsável por permitir o usuário a executar uma busca sem contexto, ou seja, ela
     * não espera que o mesmo selecione um item.
     */
    Handler handleSearch() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("search (?<type>fee|product|bill|client)(?: \"(?<query>(?:\\w| )+)\")?$", command);
        if (groups == null) { return this; }
        done = true;

        String query = groups.get("query");
        AbstractManager<? extends AbstractItem> manager = switch (groups.get("type")) {
            case "fee" -> FeeManager.getInstance();
            case "product" -> ProductManager.getInstance();
            case "bill" -> BillManager.getInstance();
            case "client" -> ClientManager.getInstance();
            case null, default -> null;
        };
        if (manager == null) { return this; }
        ConsoleApp.getSearchResponse(manager, query);

        return this;
    }

    /**
     * Método ajudante que serve para pegar o ID inserido pelo usuário no comando, ou
     * caso o mesmo tenha optado por uma pesquisa, retorna o ID selecionado nela.
     */
    private static <T extends AbstractItem> Integer getIdOrSearch(Map<String, String> groups, AbstractManager<T> manager) {
        if (groups.get("id") != null) {
            return Integer.parseInt(groups.get("id"));
        }
        String query = groups.get("query");
        if (query == null) { query = ""; }
        Integer id = ConsoleApp.getSearchResponse(manager, query);
        if (id == null || manager.get(id) == null) {
            System.out.println("(!) Search response should be a valid ID");
            return null;
        }
        return id;
    }

    private static Map<String, String> matchRegex(String regex, String command) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);

        if (!matcher.matches()) {
            return null;
        }
        Map<String, String> result = new HashMap<>();

        for (String groupName : matcher.namedGroups().keySet()) {
            result.put(groupName, matcher.group(groupName));
        }

        return result;
    }

    private Handler handleCreateProduct() {
        if (done) { return this; }

        Map<String, String> groups = matchRegex("create product \"(?<name>(?:\\w| )+)\" (?<price>\\d+(?:\\.\\d\\d?)?)$", command);
        if (groups == null) { return this; }
        done = true;

        String name = groups.get("name");
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(groups.get("price")));

        if (name.isBlank() || price.doubleValue() <= 0) {
            Display.invalidAction(command, "(!) Product creation command should be as follows:\ncreate product \"<name>\" <price>");
            return this;
        }

        ProductManager pm = ProductManager.getInstance();
        int id = pm.getIdCounter();

        pm.createProduct(name, price);
        System.out.println("(i) Product \"" + name + "\" created with ID " + id);
        return this;
    }

    private Handler handleCreateFee() {
        if (done) { return this; }

        Map<String, String> groups = matchRegex("create fee \"(?<name>(?:\\w| )+)\" (?<percentage>-?\\d+)%$", command);
        if (groups == null) { return this; }
        done = true;

        String name = groups.get("name");
        BigDecimal percentage = BigDecimal.valueOf(Double.parseDouble(groups.get("percentage")) / 100);

        if (name.isBlank()) {
            Display.invalidAction(command, "(!) Fee creation command should be as follows:\ncreate fee \"<name>\" <rate>%");
            return this;
        }

        FeeManager fm = FeeManager.getInstance();
        int id = fm.getIdCounter();

        fm.createFee(name, percentage);
        System.out.println("(i) Fee \"" + name + "\" created with ID " + id);
        return this;
    }

    private Handler handleCreateClient() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("create client \"(?<name>(?:\\w| )+)\"(?:$| (?<phoneNumber>\\(\\d{2}\\)\\d{8,9})$)", command);
        if (groups == null) { return this; }
        done = true;

        String name = groups.get("name");
        String phoneNumber = groups.get("phoneNumber");

        if (name.isBlank()) {
            Display.invalidAction(command, "(!) Client creation command should be as follows:\ncreate client \"<name>\" (<DDD>)<phoneNumber>(optional)");
            return this;
        }

        ClientManager cm = ClientManager.getInstance();
        int id = cm.getIdCounter();

        cm.createClient(name, phoneNumber);
        System.out.println("(i) Client \"" + name + "\" created with ID " + id);
        return this;
    }

    private Handler handleCreateBill() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("create bill (?:(?<id>\\d+)|(?<search>\\?)(?: \"(?<query>(?:\\w| )+)\")?)$", command);
        if (groups == null) { return this; }
        done = true;

        Integer id = getClientIdOrSearch(groups);

        if (id == null) { return this; }

        Client client = ClientManager.getInstance().get(id);
        BillManager bm = BillManager.getInstance();
        System.out.println("(i) Bill of id " + bm.getIdCounter() + " created for client \"" + client.getName() + "\"");
        bm.createBill(client);
        return this;
    }


    private Handler handleScrollBillView(BillView view) {
        if (done) { return this; }
        switch (command) {
            case "pw", "scroll product up" -> {
                done = true;
                view.scrollProducts(-1);
            }
            case "fw", "scroll fee up" -> {
                done = true;
                view.scrollFees(-1);
            }
            case "ps", "scroll product down" -> {
                done = true;
                view.scrollProducts(1);
            }
            case "fs", "scroll fee down" -> {
                done = true;
                view.scrollFees(1);
            }
        }
        return this;
    }

    private Handler handleExitBillView(ConsoleApp app) {
        if (done) { return this; }
        if (command.equals("close bill")) {
            done = true;
            app.exitBillContext();
        }
        return this;
    }

    private Handler handleEnterBillContext(ConsoleApp app) {
        if (done) { return this; }

        Map<String, String> groups = matchRegex("edit bill (?:(?<id>\\d+)|(?<search>\\?)(?: \"(?<query>(?:\\w| )+)\")?)$", command);
        if (groups == null) { return this; }
        done = true;

        Integer id = getBillIdOrSearch(groups);
        if (id == null) { return this; }

        app.enterBillContext(id);
        return this;
    }
    private Handler handleAddBillFee(Bill bill) {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("add fee (?:(?<id>\\d+)|(?<search>\\?)(?: \"(?<query>(?:\\w| )+)\")?)(?:$| (?<percentage>-?\\d+)%$)", command);
        if (groups == null) { return this; }
        done = true;

        String percentage = groups.get("percentage");
        Integer id = getFeeIdOrSearch(groups);

        if (id == null) { return this; }

        BigDecimal parsedPercentage = null;
        if (percentage != null) { parsedPercentage = BigDecimal.valueOf(Double.parseDouble(percentage) / 100); }

        bill.addFee(id, parsedPercentage);
        return this;
    }
    private Handler handleAddBillProduct(Bill bill) {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("add product (?:(?<id>\\d+)|(?<search>\\?)(?: \"(?<query>(?:\\w| )+)\")?)(?:$| (?<quantity>\\d+))(?:$| (?<price>\\d+(?:\\.\\d{1,2})?)$)", command);
        if (groups == null) { return this; }
        done = true;

        String price = groups.get("price");
        String quantity = groups.get("quantity");
        Integer id = getProductIdOrSearch(groups);

        if (id == null) { return this; }

        Integer parsedQuantity = null;
        BigDecimal parsedPrice = null;
        if (price != null) { parsedPrice = BigDecimal.valueOf(Double.parseDouble(price)); }
        if (quantity != null) { parsedQuantity = Integer.parseInt(quantity); }

        bill.addProduct(id, parsedPrice, parsedQuantity);
        return this;
    }

    private static Integer getProductIdOrSearch(Map<String, String> groups) {
        return getIdOrSearch(groups, ProductManager.getInstance());
    }

    private static Integer getFeeIdOrSearch(Map<String, String> groups) {
        return getIdOrSearch(groups, FeeManager.getInstance());
    }

    private static Integer getClientIdOrSearch(Map<String, String> groups) {
        return getIdOrSearch(groups, ClientManager.getInstance());
    }

    private static Integer getBillIdOrSearch(Map<String, String> groups) {
        return getIdOrSearch(groups, BillManager.getInstance());
    }

    private Handler handleRemoveBillItem(Bill bill) {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("remove (?<type>fee|product) (?<id>\\d+)$", command);
        if (groups == null) { return this; }
        done = true;

        int id = Integer.parseInt(groups.get("id"));
        if (groups.get("type").equals("fee")) {
            bill.removeFee(id);
        } else {
            bill.removeProduct(id);
        }
        return this;
    }


    private Handler handleEditProductEntry() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("edit product entry (?:(?<id>\\d+)|(?<search>\\?)(?: \"(?<query>(?:\\w| )+)\")?)(?: --name \"(?<name>(?:\\w| )+)\")?(?: --price (?<price>\\d+(?:\\.\\d{1,2})?))?$", command);
        if (groups == null) { return this; }
        done = true;

        ProductManager pm = ProductManager.getInstance();
        Integer id = getIdOrSearch(groups, pm);

        if (id == null) { return this; }

        Product product = pm.get(id);
        if (groups.get("name") != null) { product.setName(groups.get("name")); }
        if (groups.get("price") != null) {
            product.setPrice(BigDecimal.valueOf(Double.parseDouble(groups.get("price"))));
        }
        System.out.println("(i) Edited product entry of id " + product.getId());

        return this;
    }

    private Handler handleEditFeeEntry() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("edit fee entry (?:(?<id>\\d+)|(?<search>\\?)(?: \"(?<query>(?:\\w| )+)\")?)(?: --name \"(?<name>(?:\\w| )+)\")?(?: --percentage (?<percentage>\\d+)%)?$", command);
        if (groups == null) { return this; }
        done = true;

        FeeManager fm = FeeManager.getInstance();
        Integer id = getFeeIdOrSearch(groups);

        if (id == null) { return this; }

        Fee fee = fm.get(id);
        if (groups.get("name") != null) { fee.setName(groups.get("name")); }
        if (groups.get("percentage") != null) {
            fee.setPercentage(BigDecimal.valueOf(Double.parseDouble(groups.get("percentage"))/100));
        }
        System.out.println("(i) Edited fee entry of id " + fee.getId());

        return this;
    }

    private Handler handleEditClientEntry() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("edit client entry (?:(?<id>\\d+)|(?<search>\\?)(?: \"(?<query>(?:\\w| )+)\")?)(?: --name \"(?<name>(?:\\w| )+)\")?(?: --number (?<phoneNumber>\\(\\d{2}\\)\\d{8,9}))?$", command);
        if (groups == null) { return this; }
        done = true;

        ClientManager cm = ClientManager.getInstance();
        Integer id = getClientIdOrSearch(groups);

        if (id == null) { return this; }

        Client client = cm.get(id);
        if (groups.get("name") != null) { client.setName(groups.get("name")); }
        if (groups.get("phoneNumber") != null) { client.setPhoneNumber(groups.get("phoneNumber")); }
        System.out.println("(i) Edited client entry of id " + client.getId());

        return this;
    }

    private Handler handleDeleteEntry(ConsoleApp app) {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("delete (?<type>fee|product|bill|client) entry (?:(?<id>\\d+)|(?<search>\\?)(?: \"(?<query>(?:\\w| )+)\")?)$", command);
        if (groups == null) { return this; }
        done = true;

        AbstractManager<? extends AbstractItem> manager;
        Integer id;
        String type = groups.get("type");
        switch (type) {
            case "fee" -> {
                manager = FeeManager.getInstance();
                id = getFeeIdOrSearch(groups);
            }
            case "product" -> {
                manager = ProductManager.getInstance();
                id = getProductIdOrSearch(groups);
            }
            case "client" -> {
                manager = ClientManager.getInstance();
                id = getClientIdOrSearch(groups);
            }
            case "bill" -> {
                manager = BillManager.getInstance();
                id = getBillIdOrSearch(groups);
                if (app.inBillContext() && app.getView().getBill().getId() == id) {
                    System.out.println("(!) Please run the command 'close bill' to be able to delete it");
                    return this;
                }
            }
            case null, default -> { return this; }
        }

        if (id != null) {
            manager.remove(id);
            System.out.println("(i) Deleted " + type + " entry of id " + id);
        }
        return this;
    }

    private Handler handleSetBillSort() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("sort bill by (?<field>id|name|total)(?: (?<order>v))?$", command);
        if (groups == null) { return this; }
        done = true;

        boolean reversed = groups.get("order") != null;
        switch (groups.get("field")) {
            case "id" -> SortingConfig.setBillSortingOption(new SortingOption<>(BillSortBy.ID, reversed));
            case "name" -> SortingConfig.setBillSortingOption(new SortingOption<>(BillSortBy.CLIENT_NAME, reversed));
            case "total" -> SortingConfig.setBillSortingOption(new SortingOption<>(BillSortBy.TOTAL, reversed));
        }
        System.out.println("(i) Changed bill sorting option.");
        return this;
    }

    private Handler handleSetProductSort() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("sort product by (?<field>id|name|price)(?: (?<order>v))?$", command);
        if (groups == null) { return this; }
        done = true;

        boolean reversed = groups.get("order") != null;
        switch (groups.get("field")) {
            case "id" -> {
                SortingConfig.setProductSortingOption(new SortingOption<>(ProductSortBy.ID, reversed));
                SortingConfig.setQuantifiableProductSortingOption(new SortingOption<>(QuantifiableProductSortBy.ID, reversed));
            }
            case "name" -> {
                SortingConfig.setProductSortingOption(new SortingOption<>(ProductSortBy.NAME, reversed));
                SortingConfig.setQuantifiableProductSortingOption(new SortingOption<>(QuantifiableProductSortBy.NAME, reversed));
            }
            case "price" -> {
                SortingConfig.setProductSortingOption(new SortingOption<>(ProductSortBy.PRICE, reversed));
                SortingConfig.setQuantifiableProductSortingOption(new SortingOption<>(QuantifiableProductSortBy.PRICE, reversed));
            }
        }
        System.out.println("(i) Changed product sorting option.");
        return this;
    }

    private Handler handleSetFeeSort() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("sort fee by (?<field>id|name|percentage)(?: (?<order>v))?$", command);
        if (groups == null) { return this; }
        done = true;

        boolean reversed = groups.get("order") != null;
        switch (groups.get("field")) {
            case "id" -> SortingConfig.setFeeSortingOption(new SortingOption<>(FeeSortBy.ID, reversed));
            case "name" -> SortingConfig.setFeeSortingOption(new SortingOption<>(FeeSortBy.NAME, reversed));
            case "percentage" -> SortingConfig.setFeeSortingOption(new SortingOption<>(FeeSortBy.PERCENTAGE, reversed));
        }
        System.out.println("(i) Changed fee sorting option.");
        return this;
    }

    private Handler handleSetClientSort() {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("sort client by (?<field>id|name)(?: (?<order>v))?$", command);
        if (groups == null) { return this; }
        done = true;

        boolean reversed = groups.get("order") != null;
        switch (groups.get("field")) {
            case "id" -> SortingConfig.setClientSortingOption(new SortingOption<>(ClientSortBy.ID, reversed));
            case "name" -> SortingConfig.setClientSortingOption(new SortingOption<>(ClientSortBy.NAME, reversed));
        }
        System.out.println("(i) Changed client sorting option.");
        return this;
    }

    private Handler handleEditBillFee(Bill bill) {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("edit fee (?<id>\\d+) (?<percentage>-?\\d+)%$", command);
        if (groups == null) { return this; }
        done = true;

        int id = Integer.parseInt(groups.get("id"));
        Fee fee = bill.getFee(id);
        if (fee == null) {
            System.out.println("(!) No fee with id " + id + " present in the bill");
        } else {
            fee.setPercentage(BigDecimal.valueOf(Double.parseDouble(groups.get("percentage")) / 100));
        }
        return this;
    }

    private Handler handleEditBillProduct(Bill bill) {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("edit product (?<id>\\d+)(?: --quantity (?<quantity>\\d+))?(?: --price (?<price>\\d+(?:\\.\\d{1,2})?))?$", command);
        if (groups == null) { return this; }
        done = true;

        int id = Integer.parseInt(groups.get("id"));
        ProductWithQuantity product = bill.getProduct(id);
        if (product == null) {
            System.out.println("(!) No product with id " + id + " present in the bill");
        } else {
            if (groups.get("price") != null) { product.setPrice(BigDecimal.valueOf(Double.parseDouble(groups.get("price"))));}
            if (groups.get("quantity") != null) { product.setQuantity(Integer.parseInt(groups.get("quantity"))); }
        }
        return this;
    }

    private Handler handleSetBillState(Bill bill) {
        if (done) { return this; }
        switch (command) {
            case "set bill as paid" -> {
                bill.setAsPaid();
                done = true;
            }
            case "set bill as unpaid" -> {
                bill.setAsUnpaid();
                done = true;
            }
            case "set bill as cancelled" -> {
                bill.setAsCancelled();
                done = true;
            }
        }
        return this;
    }

    Handler handleGetString(StringBuilder prefix) {
        if (done) { return this; }
        Map<String, String> groups = matchRegex("\"(?<value>(?:\\w| )*)\"", command);
        if (groups != null) {
            prefix.replace(0, prefix.length(), groups.get("value"));
            done = true;
        }
        return this;
    }
}