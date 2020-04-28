package io.musician101.donationhavok.configurator.gui;

import com.google.gson.JsonObject;

//@OnlyIn(Dist.CLIENT)
public abstract class DiscoveriesGui extends BaseGui {

    /*private final boolean isClient;
    public JTable currentDiscoveries;
    public JTable legendaryDiscoveries;

    public DiscoveriesGui(DiscoveryHandler dh, boolean isClient) {
        this.isClient = isClient;
        parseJFrame("Donation Havok Discoveries", f -> mainPanel(f, dh));
    }*/

    public DiscoveriesGui() {

    }

    /*private JPanel mainPanel(JFrame frame, DiscoveryHandler dh) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Current", SwingConstants.CENTER), gbc(0, 0));
        currentDiscoveries = parseJTable(new DiscoveryTableModel(dh.getCurrentDiscoveries()), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = currentDiscoveries.getSelectedRow();
                if (row == -1) {
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    new DiscoveryGui(((DiscoveryTableModel) currentDiscoveries.getModel()).getObjectAt(row), DiscoveriesGui.this, row, false);
                }
            }
        });
        panel.add(flowLayoutPanel(new JScrollPane(currentDiscoveries)), gbc(0, 1));
        panel.add(currentDiscoveriesButtons(), gbc(0, 2));
        panel.add(parseJLabel("Legendary", SwingConstants.CENTER), gbc(1, 0));
        legendaryDiscoveries = parseJTable(new DiscoveryTableModel(dh.getLegendaryDiscoveries()), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = legendaryDiscoveries.getSelectedRow();
                if (row == -1) {
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    new DiscoveryGui(((DiscoveryTableModel) legendaryDiscoveries.getModel()).getObjectAt(row), DiscoveriesGui.this, row, true);
                }
            }
        });
        panel.add(flowLayoutPanel(new JScrollPane(legendaryDiscoveries)), gbc(1, 1));
        panel.add(legendaryDiscoveriesButtons(), gbc(1, 2));
        return flowLayoutPanel(panel);
    }

    private JPanel currentDiscoveriesButtons() {
        JPanel panel = gridBagLayoutPanel();
        panel.add(flowLayoutPanel(parseJButton("New", l -> new DiscoveryGui(new Discovery(5D, "Musician101", "A whole new mod!"), DiscoveriesGui.this, -1, false))), gbc(0, 0));
        panel.add(flowLayoutPanel(parseJButton("Set as Legendary", l -> {
            int row = currentDiscoveries.getSelectedRow();
            if (row == -1) {
                return;
            }

            DiscoveryTableModel current = (DiscoveryTableModel) currentDiscoveries.getModel();
            Discovery discovery = current.getObjectAt(row);
            ((DiscoveryTableModel) legendaryDiscoveries.getModel()).add(discovery);
            current.remove(row);
        })), gbc(1, 0));
        panel.add(flowLayoutPanel(parseJButton("Edit", l -> {
            int row = currentDiscoveries.getSelectedRow();
            if (row == -1) {
                return;
            }

            new DiscoveryGui(((DiscoveryTableModel) currentDiscoveries.getModel()).getObjectAt(row), DiscoveriesGui.this, row, false);
        })), gbc(2, 0));
        panel.add(flowLayoutPanel(parseJButton("Delete", l -> ((DiscoveryTableModel) currentDiscoveries.getModel()).remove(currentDiscoveries.getSelectedRow()))), gbc(3, 0));
        return flowLayoutPanel(panel);
    }

    private JsonObject discovery() {
        JsonObject jsonObject = new JsonObject();
        Keys.CURRENT.serialize(((DiscoveryTableModel) currentDiscoveries.getModel()).getElements(), jsonObject);
        Keys.LEGENDARY.serialize(((DiscoveryTableModel) legendaryDiscoveries.getModel()).getElements(), jsonObject);
        return jsonObject;
    }

    private JPanel legendaryDiscoveriesButtons() {
        JPanel panel = gridBagLayoutPanel();
        panel.add(flowLayoutPanel(parseJButton("New", l -> new DiscoveryGui(new Discovery(5D, "Musician101", "A whole new mod!"), DiscoveriesGui.this, -1, true))), gbc(0, 0));
        panel.add(flowLayoutPanel(parseJButton("Set as Current", l -> {
            int row = legendaryDiscoveries.getSelectedRow();
            if (row == -1) {
                return;
            }

            DiscoveryTableModel legendary = (DiscoveryTableModel) legendaryDiscoveries.getModel();
            Discovery discovery = legendary.getObjectAt(row);
            ((DiscoveryTableModel) currentDiscoveries.getModel()).add(discovery);
            legendary.remove(row);
        })), gbc(1, 0));
        panel.add(flowLayoutPanel(parseJButton("Edit", l -> {
            int row = legendaryDiscoveries.getSelectedRow();
            if (row == -1) {
                return;
            }

            new DiscoveryGui(((DiscoveryTableModel) legendaryDiscoveries.getModel()).getObjectAt(row), DiscoveriesGui.this, row, false);
        })), gbc(2, 0));
        panel.add(flowLayoutPanel(parseJButton("Delete", l -> ((DiscoveryTableModel) legendaryDiscoveries.getModel()).remove(legendaryDiscoveries.getSelectedRow()))), gbc(3, 0));
        return flowLayoutPanel(panel);
    }*/

    @Override
    protected void update() {
        JsonObject jsonObject = new JsonObject();
        //jsonObject.add(Keys.DISCOVERY.getKey(), discovery());
        //DonationHavok.getInstance().getProxy().updateDiscoveries(((DiscoveryTableModel) currentDiscoveries.getModel()).getElements(), ((DiscoveryTableModel) legendaryDiscoveries.getModel()).getElements());
    }
}
