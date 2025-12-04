package view.gui;

import javax.swing.text.DefaultFormatter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexFormatter extends DefaultFormatter {
    private final Pattern pattern;

    public RegexFormatter(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value == null) {
            return "";
        }
        String text = value.toString();
        Matcher matcher = pattern.matcher(text);


        if (!matcher.matches()) {
            throw new ParseException("Regex does not match.", 0);
        }
        return text;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null || text.isEmpty()) {
            return null;
        }

        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) {
            throw new ParseException("Input does not match regex", 0);
        }

        return text;
    }
}