package io.musician101.donationhavok.configurator.gui;

import java.awt.Frame;
import java.util.Arrays;
import java.util.function.Function;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class BaseGui extends Gui {

    protected abstract JPanel mainPanel(JFrame frame);

    public static boolean isFrameActive(String name) {
        return Arrays.stream(Frame.getFrames()).anyMatch(frame -> frame.getTitle().equals(name) && frame.isVisible());
    }

    protected void parseJFrame(String name, Function<JFrame, JPanel> panel) {
        JFrame frame = new JFrame(name);
        //TODO temp disabled. need to set main frame to close entire program only
        //frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(850, 510);
        frame.setLocationRelativeTo(null);
        //TODO temp disabled
        /*frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Would you like to these changes?");
                if (result == 2) {
                    return;
                }

                if (result == 0) {
                    update();
                }

                frame.setVisible(false);
            }
        });*/

        frame.setContentPane(panel.apply(frame));
        frame.pack();
        frame.setVisible(true);
    }

    protected abstract void update();
}
