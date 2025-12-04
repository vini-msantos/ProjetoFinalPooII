package view.gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FormattedFieldFactory {
    public static void editPriceField(JFormattedTextField field, @Nullable Double initialValue) {
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.getDefault());

        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(2);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.01);
        formatter.setOverwriteMode(true);

        field.setFormatterFactory(new DefaultFormatterFactory(formatter));
        field.setColumns(10);

        if (initialValue != null) { field.setValue(initialValue); }
    }

    public static void editQuantityField(JFormattedTextField field, @Nullable Integer initialValue) {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);

        formatter.setValueClass(Integer.class);

        formatter.setMinimum(1);
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);

        field.setFormatterFactory(new DefaultFormatterFactory(formatter));
        field.setColumns(10);

        if (initialValue != null) { field.setValue(initialValue); }
    }
}
