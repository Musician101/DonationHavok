package io.musician101.donationhavok.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public abstract class BaseGUI<G extends BaseGUI> {

    protected abstract void update(G prevGUI);

    protected final JFrame parseJFrame(String name, G prevGUI, Function<JFrame, JPanel> panel) {
        JFrame frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(850, 510);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Would you like to these changes?");
                if (result == 2) {
                    return;
                }

                if (result == 0) {
                    update(prevGUI);
                }

                frame.setVisible(false);
            }
        });

        frame.setContentPane(panel.apply(frame));
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    protected final JButton parseJButton(String name, ActionListener l) {
        JButton button = new JButton(name);
        button.addActionListener(l);
        return button;
    }

    protected final JTable parseJTable(TableModel model, MouseAdapter mouseAdapter) {
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.addMouseListener(mouseAdapter);
        model.addTableModelListener(l -> {
            resizeTable(table);
        });
        resizeTable(table);
        return table;
    }

    private void resizeTable(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getCellRenderer(row, column);
                renderer.setHorizontalAlignment(SwingConstants.CENTER);
                Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(row, column), false, false, row, column);
                width = Math.max(width + table.getIntercellSpacing().width + 5, comp.getPreferredSize().width);
            }

            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            tableColumn.setPreferredWidth(Math.max(width + 5, tableColumn.getPreferredWidth()));
        }
    }

    protected final GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.BOTH;
        return gbc;
    }

    protected final JPanel flowLayoutPanel(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(component);
        return panel;
    }

    protected final JPanel gridBagLayoutPanel() {
        return new JPanel(new GridBagLayout());
    }

    protected final JLabel parseJLabel(String text, int horizontalAlignment) {
        JLabel jLabel = new JLabel(text);
        jLabel.setHorizontalAlignment(horizontalAlignment);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        return jLabel;
    }

    public static boolean isFrameActive(String name) {
        return Arrays.stream(Frame.getFrames()).anyMatch(frame -> frame.getTitle().equals(name) && frame.isVisible());
    }
}
