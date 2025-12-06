package view.console;

import model.item.AbstractItem;
import model.manager.*;
import model.sorting.SortingConfig;
import view.App;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleApp implements App {
    private static final int RESULTS_PER_PAGE = 20;

    private BillView view = null;

    ConsoleApp() {}

    /**
     * Loop principal de execução, primeiro ele exibe a visão da comanda (caso tenha uma
     * aberta), e em seguida é recolhido a entrada do usuário, e então o comando é interpretado.
     * O loop só é quebrado caso o usuário escolha sair.
     */
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
            }

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

    void enterBillContext(int id) {
        view = BillView.fromId(id);
    }

    void exitBillContext() {
        view = null;
    }

    void displayBillContext() {
        if (inBillContext()) {
            view.displayBill();
        }
    }

    BillView getView() {
        return view;
    }

    static String getCommand() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine().trim();
    }

    static void save() {
        ProductManager.getInstance().save();
        FeeManager.getInstance().save();
        BillManager.getInstance().save();
        ClientManager.getInstance().save();
        SortingConfig.save();

        System.out.println("(i) Data saved");
    }

    /**
     * Abre uma tela de busca de item, mostrando algumas opções por vez e
     * esperando uma resposta do usuário (respostas especificadas na tela de help).
     * Sua função é permitir que no lugar de um ID específico, o usuário pode usar
     * a busca para selecionar o item que será usado no comando.
     *
     * @param manager Fonte da busca dos itens.
     * @param prefix Usado para filtrar todos os itens que começam com ele.
     * @param <T> O tipo do item buscado
     * @return O ID do item selecionado pela busca,
     * pode retornar null caso o usuário não escolha nenhum item.
     */
    static <T extends AbstractItem> Integer getSearchResponse(
            AbstractManager<T> manager,
            String prefix
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

    boolean inBillContext() {
        return view != null;
    }
}
