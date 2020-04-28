package io.musician101.donationhavok.configurator.gui;

import javax.swing.JOptionPane;

public abstract class AddNonReplaceableBlocksGui extends BaseGui {

    //private JList<IBlockState> configBlocks;
    //private JList<IBlockState> nonConfigBlocks;

    public AddNonReplaceableBlocksGui(OldConfigGui prevGUI) {
        String name = "Add Block(s)";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, f -> mainPanel(f));
    }

    /*private JPanel leftPanel(JFrame frame, OldConfigGui prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Available Blocks", SwingConstants.CENTER), gbc(0, 0));
        IForgeRegistry<Block> registry = ForgeRegistries.BLOCKS;
        List<IBlockState> blockStates = ((SortedListModel<IBlockState>) prevGUI.nonReplaceableBlocks.getModel()).getElements();
        nonConfigBlocks = new JList<>(new SortedListModel<>(registry.getKeys().stream().map(registry::getValue).filter(Objects::nonNull).map(Block::getDefaultState).filter(blockState -> !blockStates.contains(blockState)).collect(Collectors.toList()), new BlockStateIDComparator()));
        nonConfigBlocks.setCellRenderer(new IBlockStateCellRenderer());
        nonConfigBlocks.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    transfer(frame, nonConfigBlocks, configBlocks);
                }
            }
        });
        nonConfigBlocks.setPreferredSize(new Dimension(262, 131));
        panel.add(new JScrollPane(nonConfigBlocks), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, OldConfigGui prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(leftPanel(frame, prevGUI), gbc(0, 0));
        panel.add(middlePanel(frame), gbc(1, 0));
        panel.add(rightPanel(frame, prevGUI), gbc(2, 0));
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.setVisible(false);
        });
        saveButton.setPreferredSize(new Dimension(180, 26));
        panel.add(saveButton, gbc(0, 1));
        JButton cancelButton = parseJButton("Cancel", l -> frame.setVisible(false));
        cancelButton.setPreferredSize(new Dimension(180, 26));
        panel.add(cancelButton, gbc(2, 1));
        return flowLayoutPanel(panel);
    }

    private JPanel middlePanel(JFrame frame) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(flowLayoutPanel(parseJButton("==>", l -> transfer(frame, nonConfigBlocks, configBlocks))), gbc(0, 0));
        panel.add(flowLayoutPanel(parseJButton("<==", l -> transfer(frame, configBlocks, nonConfigBlocks))), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    private JPanel rightPanel(JFrame frame, OldConfigGui prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Configured Blocks", SwingConstants.CENTER), gbc(0, 0));
        configBlocks = new JList<>(new SortedListModel<>(((SortedListModel<IBlockState>) prevGUI.nonReplaceableBlocks.getModel()).getElements(), new BlockStateIDComparator()));
        configBlocks.setPreferredSize(new Dimension(262, 131));
        configBlocks.setCellRenderer(new IBlockStateCellRenderer());
        configBlocks.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    transfer(frame, configBlocks, nonConfigBlocks);
                }
            }
        });

        panel.add(new JScrollPane(configBlocks), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    private void transfer(JFrame frame, JList<IBlockState> from, JList<IBlockState> to) {
        SortedListModel<IBlockState> fromModel = (SortedListModel<IBlockState>) from.getModel();
        SortedListModel<IBlockState> toModel = (SortedListModel<IBlockState>) to.getModel();
        List<IBlockState> blockStates = Arrays.stream(from.getSelectedIndices()).mapToObj(index -> {
            IBlockState blockState = fromModel.getElementAt(index);
            fromModel.remove(index);
            return blockState;
        }).collect(Collectors.toList());
        blockStates.addAll(toModel.getElements());
        from.setModel(new SortedListModel<>(fromModel.getElements(), new BlockStateIDComparator()));
        from.validate();
        to.setModel(new SortedListModel<>(blockStates, new BlockStateIDComparator()));
        to.validate();
        frame.pack();
    }*/

    @Override
    protected final void update() {
        /*SortedListModel<IBlockState> model = (SortedListModel<IBlockState>) configGUI.nonReplaceableBlocks.getModel();
        model.clear();
        model.addAll(((SortedListModel<IBlockState>) configBlocks.getModel()).getElements());*/
    }
}
