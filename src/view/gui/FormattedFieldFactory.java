package view.gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

public class FormattedFieldFactory {
    public static void editNameField(JTextField field, @Nullable String initialValue) {
        field.setHorizontalAlignment(SwingConstants.LEFT);
        if (initialValue != null) { field.setText(initialValue); }
    }
    public static void editPriceField(JFormattedTextField field, @Nullable Double initialValue) {
        String regex = "^\\d+(?:\\.\\d+)?$";
        field.setFormatterFactory(new DefaultFormatterFactory(new RegexFormatter(regex)));
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        if (initialValue != null) { field.setValue(initialValue); }
    }

    public static void editQuantityField(JFormattedTextField field, @Nullable Integer initialValue) {
        String regex = "^\\d+$";
        field.setFormatterFactory(new DefaultFormatterFactory(new RegexFormatter(regex)));
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        if (initialValue != null) { field.setValue(initialValue); }
    }

    public static void editPercentageField(JFormattedTextField field, @Nullable Double initialValue) {
        String regex = "^-?\\d+$";
        field.setFormatterFactory(new DefaultFormatterFactory(new RegexFormatter(regex)));
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        if (initialValue != null) { field.setValue(initialValue * 100); }
    }
}
