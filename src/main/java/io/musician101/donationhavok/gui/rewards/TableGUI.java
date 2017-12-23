package io.musician101.donationhavok.gui.rewards;

import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.model.table.ListTableModel;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("unchecked")
public class TableGUI<M extends ListTableModel<T>, T> extends BaseGUI<RewardsGUI> {

    private JPanel panel;
    private JTable table;

    public TableGUI(M model, T defaultObject, BiConsumer<T, Integer> openGUI) {
        panel = mainPanel(model, defaultObject, openGUI);
    }

    public JPanel getPanel() {
        return panel;
    }

    public JTable getTable() {
        return table;
    }

    @Override
    protected final void update(RewardsGUI prevGUI) {

    }

    private JPanel mainPanel(M model, T defaultObject, BiConsumer<T, Integer> openGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        table = parseJTable(model, parseMouseAdapter((Class<T>) defaultObject.getClass(), openGUI).apply(table));
        panel.add(new JScrollPane(table), gbc(0, 0));
        panel.add(flowLayoutPanel(buttonPanel((Class<M>) model.getClass(), defaultObject, openGUI)), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    private Function<JTable, MouseAdapter> parseMouseAdapter(Class<T> objectType, BiConsumer<T, Integer> openGUI) {
        return table -> new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                getSelectedObject(table, objectType).filter(command -> e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2).ifPresent(t -> openGUI.accept(t, table.getSelectedRow()));
            }
        };
    }

    private Optional<T> getSelectedObject(JTable table, Class<T> objectType) {
        int row = table.getSelectedRow();
        return row == -1 ? Optional.empty() : Optional.of(objectType.cast(((ListTableModel) table.getModel()).getObjectAt(row)));
    }

    private JPanel buttonPanel(Class<M> tableModelClass, T defaultObject, BiConsumer<T, Integer> openGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJButton("New", e -> openGUI.accept(defaultObject, -1))), gbc(0, 0));
        panel.add(flowLayoutPanel(parseJButton("Copy", e -> getSelectedObject(table, (Class<T>) defaultObject.getClass()).ifPresent(object -> tableModelClass.cast(table.getModel()).add(object)))), gbc(1, 0));
        panel.add(flowLayoutPanel(parseJButton("Edit", e -> getSelectedObject(table, (Class<T>) defaultObject.getClass()).ifPresent(t -> openGUI.accept(t, table.getSelectedRow())))), gbc(2, 0));
        panel.add(flowLayoutPanel(parseJButton("Delete", e -> {
            M model = tableModelClass.cast(table.getModel());
            Arrays.stream(table.getSelectedRows()).forEach(model::remove);
        })), gbc(3, 0));
        return panel;
    }
}
