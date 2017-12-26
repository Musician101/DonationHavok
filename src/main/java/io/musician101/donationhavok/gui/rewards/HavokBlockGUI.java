package io.musician101.donationhavok.gui.rewards;

import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.model.SortedComboBoxModel;
import io.musician101.donationhavok.gui.model.table.BlockStatePropertiesTableModel;
import io.musician101.donationhavok.gui.model.table.HavokBlockTableModel;
import io.musician101.donationhavok.gui.render.IBlockStateCellRenderer;
import io.musician101.donationhavok.gui.tree.HavokMapTreeNode;
import io.musician101.donationhavok.havok.HavokBlock;
import io.musician101.donationhavok.util.IBlockStateIDComparator;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockPurpurSlab;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

public class HavokBlockGUI extends BaseGUI<RewardsGUI> {

    private final int index;
    private JComboBox<IBlockState> blockComboBox;
    private JFormattedTextField delayTextField;
    private JFormattedTextField xTextField;
    private JFormattedTextField yTextField;
    private JFormattedTextField zTextField;
    private JTable blockStateTable;
    private JTree tileEntityTree;

    public HavokBlockGUI(HavokBlock block, int index, RewardsGUI prevGUI) {
        this.index = index;
        String name = "Havok Block";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, block, prevGUI));
    }

    @Override
    protected final void update(RewardsGUI prevGUI) {
        JTable blocks = prevGUI.blocksTable;
        HavokBlockTableModel model = (HavokBlockTableModel) blocks.getModel();
        HavokBlock havokBlock = new HavokBlock(Integer.valueOf(delayTextField.getValue().toString()), Integer.valueOf(xTextField.getValue().toString()), Integer.valueOf(yTextField.getValue().toString()), Integer.valueOf(zTextField.getValue().toString()), ((BlockStatePropertiesTableModel) blockStateTable.getModel()).getBlockState(), GSON.fromJson(((HavokMapTreeNode) tileEntityTree.getModel().getRoot()).serialize(), NBTTagCompound.class));
        if (index == -1) {
            model.add(havokBlock);
        }
        else {
            model.replace(index, havokBlock);
        }
    }

    private JPanel blockStatePanel(HavokBlock block) {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel blockIDPanel = new JPanel(new GridBagLayout());
        blockIDPanel.add(parseJLabel("Block ID:", SwingConstants.LEFT), gbc(0, 0));
        RegistryNamespacedDefaultedByKey<ResourceLocation, Block> registry = Block.REGISTRY;
        blockComboBox = new JComboBox<>(new SortedComboBoxModel<>(registry.getKeys().stream().map(registry::getObject).map(Block::getDefaultState).collect(Collectors.toList()), new IBlockStateIDComparator()));
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
        IProperty key = new ArrayList<>(blockState.getPropertyKeys()).get(row);
        if (key instanceof PropertyInteger) {
            PropertyInteger intKey = (PropertyInteger) key;
            intKey.getAllowedValues().forEach(value -> {
                JMenuItem item = new JMenuItem(value.toString());
                item.addActionListener(e -> ((BlockStatePropertiesTableModel) blockStateTable.getModel()).setBlockState(blockState.withProperty(intKey, value)));
                jPopupMenu.add(item);
            });
        }
        else if (key instanceof PropertyBool) {
            PropertyBool boolKey = (PropertyBool) key;
            boolKey.getAllowedValues().forEach(value -> {
                JMenuItem item = new JMenuItem(value.toString());
                item.addActionListener(e -> ((BlockStatePropertiesTableModel) blockStateTable.getModel()).setBlockState(blockState.withProperty(boolKey, value)));
            });
        }
        else if (key instanceof PropertyDirection) {
            parseEnumMenuItems(blockState, (PropertyDirection) key).forEach(jPopupMenu::add);
        }
        else if (key instanceof PropertyEnum) {
            if (key == BlockBed.PART) {
                parseEnumMenuItems(blockState, BlockBed.PART).forEach(jPopupMenu::add);
            }
            else if (key == BlockCarpet.COLOR) {
                parseEnumMenuItems(blockState, BlockCarpet.COLOR).forEach(jPopupMenu::add);
            }
            else if (key == BlockColored.COLOR) {
                parseEnumMenuItems(blockState, BlockColored.COLOR).forEach(jPopupMenu::add);
            }
            else if (key == BlockConcretePowder.COLOR) {
                parseEnumMenuItems(blockState, BlockConcretePowder.COLOR).forEach(jPopupMenu::add);
            }
            else if (key == BlockDirt.VARIANT) {
                parseEnumMenuItems(blockState, BlockDirt.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockDoor.HINGE) {
                parseEnumMenuItems(blockState, BlockDoor.HINGE).forEach(jPopupMenu::add);
            }
            else if (key == BlockDoor.HALF) {
                parseEnumMenuItems(blockState, BlockDoublePlant.HALF).forEach(jPopupMenu::add);
            }
            else if (key == BlockDoublePlant.VARIANT) {
                parseEnumMenuItems(blockState, BlockDoublePlant.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockDoublePlant.HALF) {
                parseEnumMenuItems(blockState, BlockDoublePlant.HALF).forEach(jPopupMenu::add);
            }
            else if (key == Blocks.RED_FLOWER.getTypeProperty()) {
                parseEnumMenuItems(blockState, (PropertyEnum<BlockFlower.EnumFlowerType>) Blocks.RED_FLOWER.getTypeProperty()).forEach(jPopupMenu::add);
            }
            else if (key == Blocks.YELLOW_FLOWER.getTypeProperty()) {
                parseEnumMenuItems(blockState, (PropertyEnum<BlockFlower.EnumFlowerType>) Blocks.YELLOW_FLOWER.getTypeProperty()).forEach(jPopupMenu::add);
            }
            else if (key == BlockFlowerPot.CONTENTS) {
                parseEnumMenuItems(blockState, BlockFlowerPot.CONTENTS).forEach(jPopupMenu::add);
            }
            else if (key == BlockHugeMushroom.VARIANT) {
                parseEnumMenuItems(blockState, BlockHugeMushroom.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockLever.FACING) {
                parseEnumMenuItems(blockState, BlockLever.FACING).forEach(jPopupMenu::add);
            }
            else if (key == BlockLog.LOG_AXIS) {
                parseEnumMenuItems(blockState, BlockLog.LOG_AXIS).forEach(jPopupMenu::add);
            }
            else if (key == BlockNewLeaf.VARIANT) {
                parseEnumMenuItems(blockState, BlockNewLeaf.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockNewLog.VARIANT) {
                parseEnumMenuItems(blockState, BlockNewLog.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockOldLog.VARIANT) {
                parseEnumMenuItems(blockState, BlockOldLog.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockOldLeaf.VARIANT) {
                parseEnumMenuItems(blockState, BlockOldLeaf.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockOldLeaf.VARIANT) {
                parseEnumMenuItems(blockState, BlockOldLeaf.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockPistonExtension.TYPE) {
                parseEnumMenuItems(blockState, BlockPistonExtension.TYPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockPlanks.VARIANT) {
                parseEnumMenuItems(blockState, BlockPlanks.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockPortal.AXIS) {
                parseEnumMenuItems(blockState, BlockPortal.AXIS).forEach(jPopupMenu::add);
            }
            else if (key == BlockPrismarine.VARIANT) {
                parseEnumMenuItems(blockState, BlockPrismarine.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockPurpurSlab.VARIANT) {
                parseEnumMenuItems(blockState, BlockPurpurSlab.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockQuartz.VARIANT) {
                parseEnumMenuItems(blockState, BlockQuartz.VARIANT).forEach(jPopupMenu::add);
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
            else if (key == BlockRedSandstone.TYPE) {
                parseEnumMenuItems(blockState, BlockRedSandstone.TYPE).forEach(jPopupMenu::add);
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
            else if (key == BlockSand.VARIANT) {
                parseEnumMenuItems(blockState, BlockSand.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockSandStone.TYPE) {
                parseEnumMenuItems(blockState, BlockSandStone.TYPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockSapling.TYPE) {
                parseEnumMenuItems(blockState, BlockSapling.TYPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockSilverfish.VARIANT) {
                parseEnumMenuItems(blockState, BlockSilverfish.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockSlab.HALF) {
                parseEnumMenuItems(blockState, BlockSlab.HALF).forEach(jPopupMenu::add);
            }
            else if (key == BlockStainedGlass.COLOR) {
                parseEnumMenuItems(blockState, BlockStainedGlass.COLOR).forEach(jPopupMenu::add);
            }
            else if (key == BlockStainedGlassPane.COLOR) {
                parseEnumMenuItems(blockState, BlockStainedGlassPane.COLOR).forEach(jPopupMenu::add);
            }
            else if (key == BlockStairs.HALF) {
                parseEnumMenuItems(blockState, BlockStairs.HALF).forEach(jPopupMenu::add);
            }
            else if (key == BlockStairs.SHAPE) {
                parseEnumMenuItems(blockState, BlockStairs.SHAPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockStone.VARIANT) {
                parseEnumMenuItems(blockState, BlockStone.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockStoneBrick.VARIANT) {
                parseEnumMenuItems(blockState, BlockStoneBrick.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockStoneSlab.VARIANT) {
                parseEnumMenuItems(blockState, BlockStoneSlab.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockStoneSlabNew.VARIANT) {
                parseEnumMenuItems(blockState, BlockStoneSlabNew.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockStructure.MODE) {
                parseEnumMenuItems(blockState, BlockStructure.MODE).forEach(jPopupMenu::add);
            }
            else if (key == BlockTallGrass.TYPE) {
                parseEnumMenuItems(blockState, BlockTallGrass.TYPE).forEach(jPopupMenu::add);
            }
            else if (key == BlockTrapDoor.HALF) {
                parseEnumMenuItems(blockState, BlockTrapDoor.HALF).forEach(jPopupMenu::add);
            }
            else if (key == BlockWall.VARIANT) {
                parseEnumMenuItems(blockState, BlockWall.VARIANT).forEach(jPopupMenu::add);
            }
            else if (key == BlockWoodSlab.VARIANT) {
                parseEnumMenuItems(blockState, BlockStructure.MODE).forEach(jPopupMenu::add);
            }
        }

        return jPopupMenu;
    }

    private <P extends PropertyEnum<T>, T extends Enum<T> & IStringSerializable> List<JMenuItem> parseEnumMenuItems(IBlockState blockState, P property) {
        return property.getAllowedValues().stream().map(value -> {
            JMenuItem jMenuItem = new JMenuItem(value.getName());
            jMenuItem.addActionListener(e -> ((BlockStatePropertiesTableModel) blockStateTable.getModel()).setBlockState(blockState.withProperty(property, value)));
            return jMenuItem;
        }).collect(Collectors.toList());
    }

    private JPanel mainPanel(JFrame frame, HavokBlock block, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(leftPanel(block), gbc(0, 0));
        panel.add(rightPanel(block), gbc(1, 0));
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.dispose();
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(saveButton), gbc(1, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.dispose());
        cancelButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(cancelButton), gbc(1, 1));
        return panel;
    }

    private JPanel leftPanel(HavokBlock block) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(block.getDelay());
        panel.add(delayTextField, gbc(0, 1));
        panel.add(parseJLabel("Offset", SwingConstants.CENTER), gbc(0, 2));
        panel.add(offsetPanel(block), gbc(0, 3));
        return flowLayoutPanel(panel);
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
}
