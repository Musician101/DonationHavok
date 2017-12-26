package io.musician101.donationhavok.gui.rewards;

import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.model.SortedComboBoxModel;
import io.musician101.donationhavok.gui.model.table.HavokEntityTableModel;
import io.musician101.donationhavok.gui.tree.HavokMapTreeNode;
import io.musician101.donationhavok.havok.HavokEntity;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultTreeModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;
import static io.musician101.donationhavok.util.json.JsonUtils.ID;

public class HavokEntityGUI extends BaseGUI<RewardsGUI> {

    private final int index;
    private JComboBox<ResourceLocation> entityComboBox;
    private JFormattedTextField delayTextField;
    private JFormattedTextField xTextField;
    private JFormattedTextField yTextField;
    private JFormattedTextField zTextField;
    private JTree nbtTree;

    public HavokEntityGUI(HavokEntity entity, int index, RewardsGUI prevGUI) {
        this.index = index;
        String name = "Havok Entity";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f-> mainPanel(f, entity, prevGUI));
    }

    @Override
    protected void update(RewardsGUI prevGUI) {
        JTable entities = prevGUI.entitiesTable;
        HavokEntityTableModel model = (HavokEntityTableModel) entities.getModel();
        NBTTagCompound nbt = GSON.fromJson(((HavokMapTreeNode) nbtTree.getModel().getRoot()).serialize(), NBTTagCompound.class);
        nbt.setString(ID, entityComboBox.getSelectedItem().toString());
        HavokEntity entity = new HavokEntity(Integer.valueOf(delayTextField.getValue().toString()), Double.valueOf(xTextField.getValue().toString()), Double.valueOf(yTextField.getValue().toString()), Double.valueOf(zTextField.getValue().toString()), nbt);
        if (index == -1) {
            model.add(entity);
        }
        else {
            model.replace(index, entity);
        }
    }

    private JPanel mainPanel(JFrame frame, HavokEntity entity, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(leftPanel(frame, entity, prevGUI), gbc(0, 0));
        panel.add(rightPanel(entity), gbc(1, 0));
        return panel;
    }

    private JPanel leftPanel(JFrame frame, HavokEntity entity, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(entity.getDelay());
        panel.add(delayTextField, gbc(0, 1));
        panel.add(parseJLabel("Offset", SwingConstants.CENTER), gbc(0, 2));
        panel.add(offsetPanel(entity), gbc(0, 3));
        panel.add(parseJLabel("Entity", SwingConstants.LEFT), gbc(0, 4));
        entityComboBox = new JComboBox<>(new SortedComboBoxModel<>(new ArrayList<>(EntityList.getEntityNameList()), Comparator.comparing(ResourceLocation::toString)));
        entityComboBox.setSelectedItem(new ResourceLocation(entity.getNBTTagCompound().getString("id")));
        entityComboBox.addActionListener(l -> {
            Entity e = EntityList.createEntityByIDFromName((ResourceLocation) Objects.requireNonNull(entityComboBox.getSelectedItem()), FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
            e.readFromNBT(GSON.fromJson(((HavokMapTreeNode) nbtTree.getModel().getRoot()).serialize(), NBTTagCompound.class));
            DefaultTreeModel model = (DefaultTreeModel) nbtTree.getModel();
            NBTTagCompound nbt = e.writeToNBT(new NBTTagCompound());
            nbt.removeTag(ID);
            nbt.removeTag("UUID");
            nbt.removeTag("UUIDMost");
            nbt.removeTag("UUIDLeast");
            model.setRoot(new HavokMapTreeNode(GSON.toJson(nbt)));
        });
        panel.add(entityComboBox, gbc(0, 5));
        panel.add(buttonPanel(frame, prevGUI), gbc(0, 6));
        return flowLayoutPanel(panel);
    }

    private JPanel buttonPanel(JFrame frame, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.dispose();
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(saveButton), gbc(0, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.dispose());
        cancelButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(cancelButton), gbc(1, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel offsetPanel(HavokEntity entity) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJLabel("X:", SwingConstants.LEFT)), gbc(0, 0));
        xTextField = new JFormattedTextField(new DecimalFormat());
        xTextField.setValue(entity.getXOffset());
        Dimension xDim = xTextField.getPreferredSize();
        xDim.width = 100;
        xTextField.setPreferredSize(xDim);
        panel.add(xTextField, gbc(1, 0));
        panel.add(flowLayoutPanel(parseJLabel("Y:", SwingConstants.LEFT)), gbc(2, 0));
        yTextField = new JFormattedTextField(new DecimalFormat());
        yTextField.setValue(entity.getYOffset());
        Dimension yDim = yTextField.getPreferredSize();
        yDim.width = 100;
        yTextField.setPreferredSize(yDim);
        panel.add(yTextField, gbc(3, 0));
        panel.add(flowLayoutPanel(parseJLabel("Z:", SwingConstants.LEFT)), gbc(4, 0));
        zTextField = new JFormattedTextField(new DecimalFormat());
        zTextField.setValue(entity.getYOffset());
        Dimension zDim = zTextField.getPreferredSize();
        zDim.width = 100;
        zTextField.setPreferredSize(zDim);
        panel.add(zTextField, gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel rightPanel(HavokEntity entity) {
        JPanel panel = new JPanel(new GridBagLayout());
        NBTTagCompound nbt = entity.getNBTTagCompound().copy();
        nbt.removeTag(ID);
        JsonPanel jsonPanel = new JsonPanel(nbt);
        nbtTree = jsonPanel.getTree();
        panel.add(parseJLabel("NBT_COMPOUND", SwingConstants.CENTER), gbc(0, 0));
        panel.add(jsonPanel.getScrollPane(), gbc(0, 1));
        return flowLayoutPanel(panel);
    }
}
