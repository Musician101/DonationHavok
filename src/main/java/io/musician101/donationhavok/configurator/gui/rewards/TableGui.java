package io.musician101.donationhavok.configurator.gui.rewards;

@SuppressWarnings("unchecked")
public class TableGui{/*<M extends ListTableModel<T>, T> extends BaseGui {

    private final JPanel panel;
    private JTable table;

    public TableGui(M model, T defaultObject, BiConsumer<T, Integer> openGUI) {
        panel = mainPanel(model, defaultObject, openGUI);
    }

    private JPanel buttonPanel(Class<M> tableModelClass, T defaultObject, BiConsumer<T, Integer> openGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJButton("New", e -> openGUI.accept(defaultObject, -1))), gbc(0, 0));
        panel.add(flowLayoutPanel(parseJButton("Copy", e -> getSelectedObject(table, (Class<T>) defaultObject.getClass()).ifPresent(object -> tableModelClass.cast(table.getModel()).add(object)))), gbc(1, 0));
        panel.add(flowLayoutPanel(parseJButton("Edit", e -> getSelectedObject(table, (Class<T>) defaultObject.getClass()).ifPresent(t -> openGUI.accept(t, table.getSelectedRow())))), gbc(2, 0));
        panel.add(flowLayoutPanel(parseJButton("Delete", e -> {
            M model = tableModelClass.cast(table.getModel());
            model.remove(table.getSelectedRow());
        })), gbc(3, 0));
        return panel;
    }

    public JPanel getPanel() {
        return panel;
    }

    private Optional<T> getSelectedObject(@Nonnull JTable table, Class<T> objectType) {
        int row = table.getSelectedRow();
        return row == -1 ? Optional.empty() : Optional.of(objectType.cast(((ListTableModel) table.getModel()).getObjectAt(row)));
    }

    public JTable getTable() {
        return table;
    }

    private JPanel mainPanel(M model, T defaultObject, BiConsumer<T, Integer> openGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        table = parseJTable(model, parseMouseAdapter((Class<T>) defaultObject.getClass(), openGUI));
        panel.add(new JScrollPane(table), gbc(0, 0));
        panel.add(flowLayoutPanel(buttonPanel((Class<M>) model.getClass(), defaultObject, openGUI)), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    private MouseAdapter parseMouseAdapter(Class<T> objectType, BiConsumer<T, Integer> openGUI) {
        return new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                getSelectedObject(TableGui.this.table, objectType).filter(command -> e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2).ifPresent(t -> openGUI.accept(t, table.getSelectedRow()));
            }
        };
    }

    @Override
    protected final void update(RewardsGui prevGUI) {

    }*/
}
