package view.gui;

import javax.swing.*;
import java.awt.*;

public class GenericDialogs {
    public static boolean checkUnsavedChanges(Component parent, boolean hasUnsavedChanges) {
        if (!hasUnsavedChanges) { return true; }
        int answer = JOptionPane.showConfirmDialog(parent,
                "You have unsaved changes, close anyways?",
                "Unsaved Changes",
                JOptionPane.YES_NO_OPTION
        );
        return answer == JOptionPane.YES_OPTION;
    }
}
