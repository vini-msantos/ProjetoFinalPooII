package view.gui;

import model.item.AbstractItem;
import model.sorting.Sorter;
import model.sorting.SortingConfig;
import model.sorting.SortingOption;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Classe que serve para exibir uma lista de itens, permite a seleção de
 * elementos, pesquisa, e escolha da opção de ordenamento.
 * @param <T> Tipo do item a ser mostrado.
 */
public class ItemViewPanel<T extends AbstractItem> extends JPanel {
    private JList<T> itemList;
    private JCheckBox reversedCheckBox;
    private JComboBox<Sorter<T>> sortOptionBox;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel mainPanel;
    private Function<String, List<T>> searcher;
    private final DefaultListModel<T> listModel;

    public ItemViewPanel() {
        listModel = new DefaultListModel<>();
        itemList.setModel(listModel);
    }

    /**
     * Método que prepara a lista, fornecendo-a com as funções necessárias
     * e com as opções de ordenamento disponíveis.
     *
     * @param searcher Função para a busca dos elementos com base num prefixo.
     * @param getSort Função para pegar a opção de ordenamento atual.
     * @param setSort Função para alterar a opção de ordenamento.
     * @param sortingOptions Lista com as opções de ordenamento.
     */
    public void setupItemView(
            Function<String, List<T>> searcher,
            Supplier<SortingOption<T>> getSort,
            Consumer<SortingOption<T>> setSort,
            Sorter<T>[] sortingOptions
    ) {
        sortOptionBox.setModel(new DefaultComboBoxModel<>(sortingOptions));
        sortOptionBox.setSelectedItem(getSort.get().sorter());
        reversedCheckBox.setSelected(getSort.get().reversed());

        this.searcher = searcher;

        reloadList();
        setupListeners(setSort);
    }

    /**
     * Recarrega a lista. Necessário caso tenha ocorrido alguma alteração
     * em seu conteúdo ou caso o parâmetro de busca tenha mudado.
     */
    public void reloadList() {
        List<T> newList = searcher.apply(searchField.getText().trim());
        listModel.clear();
        listModel.addAll(newList);
    }

    public List<T> getSelectedValuesList() {
        return itemList.getSelectedValuesList();
    }

    public T getSelectedValue() {
        return itemList.getSelectedValue();
    }

    private void setupListeners(Consumer<SortingOption<T>> setSort) {
        ActionListener changeSortOptionListener = e -> {
            Sorter<T> sortOption = (Sorter<T>) sortOptionBox.getSelectedItem();
            boolean reversed = reversedCheckBox.isSelected();
            if (sortOption == null) { return; }

            setSort.accept(new SortingOption<>(sortOption, reversed));
            reloadList();
            SortingConfig.save();
        };

        sortOptionBox.addActionListener(changeSortOptionListener);
        reversedCheckBox.addActionListener(changeSortOptionListener);

        searchButton.addActionListener(e -> reloadList());
        searchField.addActionListener(e -> reloadList());
    }
}
