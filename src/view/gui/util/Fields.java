package view.gui.util;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * Classe que possui métodos úteis para a inicialização e validação de campos de texto.
 */
public class Fields {
    public static void editNameField(JTextField field, String initialValue) {
        field.setHorizontalAlignment(SwingConstants.LEFT);
        if (initialValue != null) { field.setText(initialValue); }
    }
    public static void editPriceField(JFormattedTextField field, Double initialValue) {
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        if (initialValue != null) { field.setValue(initialValue); }
    }

    public static void editQuantityField(JFormattedTextField field, Integer initialValue) {
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        if (initialValue != null) { field.setValue(initialValue); }
    }

    public static void editPercentageField(JFormattedTextField field, Double initialValue) {
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        if (initialValue != null) { field.setValue(initialValue * 100); }
    }

    public static void editIdField(JFormattedTextField field) {
        field.setHorizontalAlignment(SwingConstants.LEFT);
    }

    public static boolean validId(JFormattedTextField field) {
        return Pattern.compile("^\\d+$").matcher(field.getText()).matches();
    }

    public static boolean validQuantity(JFormattedTextField field) {
        return Pattern.compile("^[1-9]\\d*$").matcher(field.getText()).matches();
    }

    public static boolean validPrice(JFormattedTextField field) {
        return Pattern.compile("^[1-9]\\d*(?:\\.\\d+)?$").matcher(field.getText()).matches();
    }

    public static boolean validPercentage(JFormattedTextField field) {
        return Pattern.compile("^-?[1-9]\\d*$").matcher(field.getText()).matches();
    }

}
