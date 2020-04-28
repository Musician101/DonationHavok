package io.musician101.donationhavok.configurator.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public abstract class Gui {

    protected final JPanel flowLayoutPanel(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(component);
        return panel;
    }

    protected final GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.BOTH;
        return gbc;
    }

    protected final JPanel gridBagLayoutPanel() {
        return new JPanel(new GridBagLayout());
    }

    protected final JButton parseJButton(String name, ActionListener l) {
        JButton button = new JButton(name);
        button.addActionListener(l);
        return button;
    }

    protected final JLabel parseJLabel(String text, int horizontalAlignment) {
        JLabel jLabel = new JLabel(text);
        jLabel.setHorizontalAlignment(horizontalAlignment);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        return jLabel;
    }

    protected final JTable parseJTable(TableModel model, MouseAdapter mouseAdapter) {
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.addMouseListener(mouseAdapter);
        model.addTableModelListener(l -> resizeTable(table));
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
}
