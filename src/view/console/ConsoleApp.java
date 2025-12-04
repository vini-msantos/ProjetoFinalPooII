package view.console;

import model.item.AbstractItem;
import model.manager.*;
import org.jetbrains.annotations.Nullable;
import view.App;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleApp implements App {
    private static final int RESULTS_PER_PAGE = 20;

    private BillView view = null;

    ConsoleApp() {}

    @Override
    public void run() {
        Display.displayStartMessage();

        ProductManager.getInstance();
        FeeManager.getInstance();
        ClientManager.getInstance();
        BillManager.getInstance();

        LOOP: while (true) {
            displayBillContext();

            String command = getCommand();
            switch (command) {
                case "exit no save" -> { break LOOP; }
                case "exit" -> {
                    save();
                    break LOOP;
                }
                case "save" -> {
                    save();
                    continue;
                }
                case "help" -> {
                    Display.help();
                    continue;
                }
            };

            boolean result = new Handler(command)
                    .handleEntries(this)
                    .handleSetSortOption()
                    .handleBillContext(this)
                    .handleSearch()
                    .isDone();

            if (!result) {
                Display.invalidAction(command, null);
            }
        }
    }

    public void enterBillContext(int id) {
        view = BillView.fromId(id);
    }

    public void exitBillContext() {
        view = null;
    }

    public void displayBillContext() {
        if (inBillContext()) {
            view.displayBill();
        }
    }

    public BillView getView() {
        return view;
    }

    public static String getCommand() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine().trim();
    }

    public static void save() {
        ProductManager.getInstance().save();
        FeeManager.getInstance().save();
        BillManager.getInstance().save();
        ClientManager.getInstance().save();

        System.out.println("(i) Data saved");
    }

    public static <T extends AbstractItem> Integer getSearchResponse(
            AbstractManager<T> manager,
            @Nullable String prefix
    ) {
        StringBuilder query = new StringBuilder(Objects.requireNonNullElse(prefix, ""));
        List<T> list = manager.searchName(query.toString());

        int page = 0;
        int total = list.size();

        if (total == 0) {
            System.out.println("(i) No entries");
            return null;
        }

        while (true) {
            Display.displayTruncatedList(list, page, RESULTS_PER_PAGE);
            System.out.println("From a total of " + total + " results");

            String command = getCommand();

            switch (command) {
                case "close", "" -> { return null; }
                case "scroll up", "w" -> {
                    page = Math.clamp(page-1, 0, (total-1)/RESULTS_PER_PAGE);
                    continue;
                }
                case "scroll down", "s" -> {
                    page = Math.clamp(page+1, 0, (total-1)/RESULTS_PER_PAGE);
                    continue;
                }
            }

            boolean refresh = new Handler(command)
                    .handleSetSortOption()
                    .handleGetString(query)
                    .isDone();
            if (refresh) {
                list = manager.searchName(query.toString());
                total = list.size();
                continue;
            }

            try {
                return Integer.parseInt(command);
            } catch (NumberFormatException e) {
                System.out.println("(!) '" + command + "' is not a valid ID");
                return null;
            }
        }
    }

    public boolean inBillContext() {
        return view != null;
    }
}
