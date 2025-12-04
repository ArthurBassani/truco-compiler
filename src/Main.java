
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gui.TrucoGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            TrucoGUI gui = new TrucoGUI();
            gui.setVisible(true);
        });
    }
}

