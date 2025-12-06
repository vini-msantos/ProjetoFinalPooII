package view.console;

import java.util.List;


/**
 * Classe ajudante com alguns métodos estáticos relacionados à escrita de tela.
 */
abstract class Display {
     static void displayStartMessage() {
        System.out.println("[---- Welcome to Biller v1.0 ----]");
        System.out.println("\nFor a list of all available commands, type help.\n");
    }

     static void invalidAction(String command, String message) {
        System.out.println("(!) '" + command + "' is not a valid command, type help for info on valid actions");
        if (message != null) { System.out.println(message); }
    }

    /**
     * Método que serve para mostrar apenas alguns elementos de uma lista por vez. Especifica caso
     * a lista esteja vazia ou se possuí mais elementos em páginas anteriores ou posteriores.
     *
     * @param list Lista dos itens.
     * @param page Qual a porção dessa lista que será mostrada.
     * @param amount Quantidade de itens mostrados por vez.
     * @param <T> Tipo dos itens.
     */
     static <T> void displayTruncatedList(List<T> list, int page, int amount) {
        if (list.isEmpty()) {
            System.out.println("No entries.");
            return;
        }
        if (page != 0) {
            System.out.println("More...");
        }
        for (int i = page*amount; i < amount*(page+1); i++) {
            if (i >= list.size()) { break; }
            System.out.println(list.get(i).toString());
        }
        if (list.size() > amount*(page+1)) {
            System.out.println("More...");
        }
    }

    static void help() {
        System.out.println("""
                Command types:
                    There are three types of commands, some don't require a bill (general commands) while
                    others need a bill view to work (bill commands), and then there are some actions that
                    are only available when searching for an item (search commands).
                
                    PS: commands are case-sensitive!
                
                Tags:
                    Are going to be used in this documentation to indicate what input type is expected for a
                    given command.
                
                    PS: the decimal place separator is the dot '.'
                
                    <price>       -> Tag that indicates a positive number with up to 2 decimal places.
                    <percentage>  -> Tag that indicates a non-zero whole number (can be negative).
                    <name>        -> Tag that indicates a non-blank string of characters (can have spaces).
                                     Strings should be wrapped in double quotes: "my string".
                    <phoneNumber> -> Tag that indicates a phone number as follows: '(<DDD>)<8 or 9 digits>'.
                    <type>        -> Can be one of the following: 'fee', 'product', 'bill' or 'client'.
                    <quantity>    -> Tag that indicates a positive integer.
                    (opt.)        -> Says that the previous tag is optional.
                    (**|*****|**) -> means it can be any one of the literals inside the parenthesis.
                
                Search:
                    In some actions the id can be replaced by a search (written with a ?), in which available
                    items will be displayed and an id can be inputted.
                    A search clause can contain an optional search query, that will filter all items that start
                    with it.
                
                    Instead of:   'add product 1024 1'
                    One can use:  'add product ? 1'
                    Or even:      'add product ? "Bread" 1'

                    Commands where a search can be used will indicate such with the '<id ?>' tag.
                
                General commands:
                    exit    ---- Saves and closes the program.
                    save    ---- Saves all data.
                    exit no save    ---- Closes the program without saving.
                
                    ---- These commands create entries on the database ----
                    create product "<name>" <price>
                    create fee "<name>" <percentage>%
                    create client "<name>" <phoneNumber> (opt.)
                    create bill <clientId ?>
                    -------------------------------------------------------
                
                    delete <type> <id ?>    ---- Deletes an entry from the database. A bill can only be
                        deleted if it's not currently open.
                
                    ---- These commands edit database entries ----
                    PS: if you do not wish to edit a certain field, you can omit it, but the order of fields
                    must be the same.
                
                    edit product entry --name "<name>" --price <price>
                    edit fee entry --name "<name>" --percentage <percentage>%
                    edit client entry --name "<name>" --number <phoneNumber>
                
                    edit bill <id ?>    ----- Is different from other edit commands because it opens a bill
                                              view, allowing finer control over the bill's data.
                    ----------------------------------------------
                
                    ---- These commands change the sorting option for an item ----
                    P.S: at the end of these commands you can add a 'v' to reverse the sorting order.
                
                    sort bill by (name|id|total)
                    sort fee by (name|id|percentage)
                    sort product by (name|id|price)
                    sort client by (name|id)
                
                    e.g: 'sort fee by id v' will sort id's in descending order.
                    --------------------------------------------------------------
                
                Bill commands:
                    close bill    ---- Closes the current bill view.
                
                    add product <id ?> <quantity> (opt.) <price> (opt.)    ---- Adds a product to the bill
                        if no quantity is given, it defaults to 1, and if no custom price is provided, it
                        fetches the product entry's price.

                    add fee <id ?> <percentage>% (opt.)    ---- Adds a fee to the bill, if no percentage is
                        specified, it gets the default value from the fee's entry.

                    edit product <id> --quantity <quantity> (opt.) --price <price> (opt.)    ---- Edits the
                        product's quantity and/or price. If you do not want to change one of the values, you
                        can omit it in the command, but it needs to be in order. This action only affects
                        the current bill, and not the product's entry.

                    edit fee <id> <percentage>%    ---- Edits the given fee's percentage.

                    remove (fee|product) <id>    ---- Removes an item from the bill.

                    scroll (fee|product) down    ---- Scrolls the bill's view list of the given type down.
                        The aliases 'ps' and 'fs' do the same for products and fees respectively.

                    scroll (fee|product) up    ---- Scrolls the view up, 'pw' can be used for products and
                        'fw' for fees.

                    set bill as (paid|unpaid|cancelled)    ---- Changes the bill's status to the given value.
               
                Search commands:
                    While searching only some actions can be performed, they are:

                    close        ---- Closes the search window
                    scroll up    ---- Scrolls the view up. alias: 'w'.
                    scroll down  ---- Scrolls the view down. alias: 's'.
                
                    You can also input an id by just writing a number, it will get you out of the search
                    and return the given value to the command.
                
                    By inputting a string (value wrapped in quotes: "my string") another search will be
                    run with the given string as the query.
                
                    Lastly, you can also change the sorting option with the same commands previously stated.
                """);
    }
}
