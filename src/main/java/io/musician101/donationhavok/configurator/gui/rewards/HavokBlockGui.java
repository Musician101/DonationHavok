package io.musician101.donationhavok.configurator.gui.rewards;

public class HavokBlockGui {/*extends BaseGui {

    private final int index;
    private JComboBox<IBlockState> blockComboBox;
    private JTable blockStateTable;
    private JFormattedTextField delayTextField;
    private JTree tileEntityTree;
    private JFormattedTextField xTextField;
    private JFormattedTextField yTextField;
    private JFormattedTextField zTextField;

    public HavokBlockGui(HavokBlock block, int index, RewardsGui prevGUI) {
        this.index = index;
        String name = "Havok Block";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, block, prevGUI));
    }

    private JPanel blockStatePanel(HavokBlock block) {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel blockIDPanel = new JPanel(new GridBagLayout());
        blockIDPanel.add(parseJLabel("Block ID:", SwingConstants.LEFT), gbc(0, 0));
        IForgeRegistry<Block> registry = ForgeRegistries.BLOCKS;
        blockComboBox = new JComboBox<>(new SortedComboBoxModel<>(registry.getKeys().stream().map(registry::getValue).filter(Objects::nonNull).map(Block::getDefaultState).collect(Collectors.toList()), new BlockStateIDComparator()));
        blockComboBox.setRenderer(new IBlockStateCellRenderer());
        IBlockState blockState = block.getBlockState();
        blockComboBox.addActionListener(e -> {
            if (blockStateTable != null) {
                ((BlockStatePropertiesTableModel) blockStateTable.getModel()).setBlockState((IBlockState) blockComboBox.getSelectedItem());
            }
        });
        blockComboBox.setSelectedItem(blockState.getBlock().getDefaultState());
        blockIDPanel.add(flowLayoutPanel(blockComboBox), gbc(1, 0));
        panel.add(flowLayoutPanel(blockIDPanel), gbc(0, 0));
        blockStateTable = parseJTable(new BlockStatePropertiesTableModel(blockState), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = blockStateTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    return;
                }

                JPopupMenu jPopupMenu = getPopupMenu(((BlockStatePropertiesTableModel) blockStateTable.getModel()).getBlockState(), row);
                jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        panel.add(new JScrollPane(blockStateTable), gbc(0, 1));
        return panel;
    }

    private JPopupMenu getPopupMenu(IBlockState blockState, int row) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        IProperty<?> key = new ArrayList<>(blockState.getProperties()).get(row);
        if (key instanceof IntegerProperty) {
            IntegerProperty intKey = (IntegerProperty) key;
            intKey.getAllowedValues().forEach(value -> {
                JMenuItem item = new JMenuItem(value.toString());
                item.addActionListener(e -> ((BlockStatePropertiesTableModel) blockStateTable.getModel()).setBlockState(blockState.with(intKey, value)));
                jPopupMenu.add(item);
            });
        }
        else if (key instanceof BooleanProperty) {
            BooleanProperty boolKey = (BooleanProperty) key;
            boolKey.getAllowedValues().forEach(value -> {
                JMenuItem item = new JMenuItem(value.toString());
                item.addActionListener(e -> ((BlockStatePropertiesTableModel) blockStateTable.getModel()).setBlockState(blockState.with(boolKey, value)));
            });
        }
        else if (key instanceof DirectionProperty) {
            parseEnumMenuItems(blockState, (DirectionProperty) key).forEach(jPopupMenu::add);
        }
        else if (key instanceof EnumProperty) {
            if (key == BlockBed.PART) {
                parseEnumMenuItems(blockState, BlockBed.PART).forEach(jPopupMenu::add);
            }
            else if (key == BlockDoor.HINGE) {
                parseEnumMenuItems(blockState, BlockDoor.HINGE).forEach(jPopupMenu::add);
            }
            else if (key == BlockDoor.HALF) {
                parseEnumMenuItems(blockState, BlockDoublePlant.HALF).forEach(jPopupMenu::add);
            }
            else if (key == BlockDoublePlant.HALF) {
                parseEnumMenuItems(blockState, BlockDoublePlant.HALF).forEach(jPopupMenu::add);
            }
            else if (key == BlockPistonExtension.TYPE) {
                parseEnumMenuItems(blockState, BlockPistonExtension.TYPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockPortal.AXIS) {
                parseEnumMenuItems(blockState, BlockPortal.AXIS).forEach(jPopupMenu::add);
            }
            else if (key == BlockRail.SHAPE) {
                parseEnumMenuItems(blockState, BlockRail.SHAPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockRailDetector.SHAPE) {
                parseEnumMenuItems(blockState, BlockRailDetector.SHAPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockRailPowered.SHAPE) {
                parseEnumMenuItems(blockState, BlockRailPowered.SHAPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockRedstoneComparator.MODE) {
                parseEnumMenuItems(blockState, BlockRedstoneComparator.MODE).forEach(jPopupMenu::add);
            }
            else if (key == BlockRedstoneWire.NORTH) {
                parseEnumMenuItems(blockState, BlockRedstoneWire.NORTH).forEach(jPopupMenu::add);
            }
            else if (key == BlockRedstoneWire.EAST) {
                parseEnumMenuItems(blockState, BlockRedstoneWire.EAST).forEach(jPopupMenu::add);
            }
            else if (key == BlockRedstoneWire.SOUTH) {
                parseEnumMenuItems(blockState, BlockRedstoneWire.SOUTH).forEach(jPopupMenu::add);
            }
            else if (key == BlockRedstoneWire.WEST) {
                parseEnumMenuItems(blockState, BlockRedstoneWire.WEST).forEach(jPopupMenu::add);
            }
            else if (key == BlockRotatedPillar.AXIS) {
                parseEnumMenuItems(blockState, BlockRotatedPillar.AXIS).forEach(jPopupMenu::add);
            }
            else if (key == BlockSlab.TYPE) {
                parseEnumMenuItems(blockState, BlockSlab.TYPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockStairs.HALF) {
                parseEnumMenuItems(blockState, BlockStairs.HALF).forEach(jPopupMenu::add);
            }
            else if (key == BlockStairs.SHAPE) {
                parseEnumMenuItems(blockState, BlockStairs.SHAPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockStructure.MODE) {
                parseEnumMenuItems(blockState, BlockStructure.MODE).forEach(jPopupMenu::add);
            }
            else if (key == BlockTrapDoor.HALF) {
                parseEnumMenuItems(blockState, BlockTrapDoor.HALF).forEach(jPopupMenu::add);
            }
        }

        return jPopupMenu;
    }

    private JPanel leftPanel(HavokBlock block, JFrame frame, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(block.getDelay());
        panel.add(delayTextField, gbc(0, 1));
        panel.add(parseJLabel("Offset", SwingConstants.CENTER), gbc(0, 2));
        panel.add(offsetPanel(block), gbc(0, 3));
        JPanel saveButtons = gridBagLayoutPanel();
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.dispose();
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        saveButtons.add(flowLayoutPanel(saveButton), gbc(0, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.dispose());
        cancelButton.setPreferredSize(new Dimension(195, 26));
        saveButtons.add(flowLayoutPanel(cancelButton), gbc(1, 0));
        panel.add(saveButtons, gbc(0, 4));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, HavokBlock block, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(leftPanel(block, frame, prevGUI), gbc(0, 0));
        panel.add(rightPanel(block), gbc(1, 0));
        return panel;
    }

    private JPanel offsetPanel(HavokBlock block) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJLabel("X:", SwingConstants.LEFT)), gbc(0, 0));
        xTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        xTextField.setValue(block.getXOffset());
        Dimension xDim = xTextField.getPreferredSize();
        xDim.width = 100;
        xTextField.setPreferredSize(xDim);
        panel.add(xTextField, gbc(1, 0));
        panel.add(flowLayoutPanel(parseJLabel("Y:", SwingConstants.LEFT)), gbc(2, 0));
        yTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        yTextField.setValue(block.getYOffset());
        Dimension yDim = yTextField.getPreferredSize();
        yDim.width = 100;
        yTextField.setPreferredSize(yDim);
        panel.add(yTextField, gbc(3, 0));
        panel.add(flowLayoutPanel(parseJLabel("Z:", SwingConstants.LEFT)), gbc(4, 0));
        zTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        zTextField.setValue(block.getYOffset());
        Dimension zDim = zTextField.getPreferredSize();
        zDim.width = 100;
        zTextField.setPreferredSize(zDim);
        panel.add(zTextField, gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    private <P extends EnumProperty<T>, T extends Enum<T> & IStringSerializable> List<JMenuItem> parseEnumMenuItems(IBlockState blockState, P property) {
        return property.getAllowedValues().stream().map(value -> {
            JMenuItem jMenuItem = new JMenuItem(value.getName());
            jMenuItem.addActionListener(e -> ((BlockStatePropertiesTableModel) blockStateTable.getModel()).setBlockState(blockState.with(property, value)));
            return jMenuItem;
        }).collect(Collectors.toList());
    }

    private JPanel rightPanel(HavokBlock block) {
        JPanel panel = new JPanel(new GridBagLayout());
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("Block State", blockStatePanel(block));
        JsonPanel jsonPanel = new JsonPanel(block.getNBTTagCompound());
        tileEntityTree = jsonPanel.getTree();
        pane.addTab("Tile Entity", jsonPanel.getScrollPane());
        panel.add(pane, gbc(0, 0));
        return flowLayoutPanel(panel);
    }

    @Override
    protected final void update(RewardsGui prevGUI) {
        JTable blocks = prevGUI.blocksTable;
        HavokBlockTableModel model = (HavokBlockTableModel) blocks.getModel();
        HavokBlock havokBlock = new HavokBlock(Integer.valueOf(delayTextField.getValue().toString()), Integer.valueOf(xTextField.getValue().toString()), Integer.valueOf(yTextField.getValue().toString()), Integer.valueOf(zTextField.getValue().toString()), ((BlockStatePropertiesTableModel) blockStateTable.getModel()).getBlockState(), GSON.fromJson(((HavokMapTreeNode) tileEntityTree.getModel().getRoot()).serialize(), NBTTagCompound.class));
        if (index == -1) {
            model.add(havokBlock);
        }
        else {
            model.replace(index, havokBlock);
        }
    }*/
}
