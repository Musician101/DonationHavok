package io.musician101.donationhavok.gui.rewards;

import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.model.SortedComboBoxModel;
import io.musician101.donationhavok.gui.model.table.HavokItemStackTableModel;
import io.musician101.donationhavok.gui.tree.HavokMapTreeNode;
import io.musician101.donationhavok.havok.HavokItemStack;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

public class HavokItemStackGUI extends BaseGUI<RewardsGUI> {

    private final int index;
    private JComboBox<ResourceLocation> itemComboBox;
    private JFormattedTextField amountTextField;
    private JFormattedTextField damageTextField;
    private JFormattedTextField delayTextField;
    private JTree nbtTree;
    private JsonPanel jsonPanel;

    public HavokItemStackGUI(HavokItemStack itemStack, int index, RewardsGUI prevGUI) {
        this.index = index;
        String name = "Havok Item Stack";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, itemStack, prevGUI));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void update(RewardsGUI prevGUI) {
        JOptionPane.showConfirmDialog(null, "NBT doesn't support booleans. Any booleans will be converted to bytes.", "Alert!", JOptionPane.DEFAULT_OPTION);
        JTable items = prevGUI.itemsTable;
        HavokItemStackTableModel model = (HavokItemStackTableModel) items.getModel();
        ItemStack is = new ItemStack(Item.REGISTRY.getObject((ResourceLocation) itemComboBox.getSelectedItem()), Integer.valueOf(amountTextField.getText()), Integer.valueOf(damageTextField.getText()));
        is.setTagCompound(GSON.fromJson(((HavokMapTreeNode) nbtTree.getModel().getRoot()).serialize(), NBTTagCompound.class));
        HavokItemStack itemStack = new HavokItemStack(Integer.valueOf(delayTextField.getText()), is);
        if (index == -1) {
            model.add(itemStack);
        }
        else {
            model.replace(index, itemStack);
        }

        resizeTable(items);
    }

    private JPanel mainPanel(JFrame frame, HavokItemStack itemStack, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(leftPanel(frame, itemStack, prevGUI), gbc(0, 0));
        panel.add(rightPanel(itemStack), gbc(1, 0));
        return panel;
    }

    private JPanel leftPanel(JFrame frame, HavokItemStack itemStack, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(delayPanel(itemStack), gbc(0, 0));
        panel.add(idPanel(itemStack), gbc(0, 1));
        panel.add(numericalPanel(itemStack), gbc(0, 2));
        panel.add(buttonPanel(frame, prevGUI), gbc(0, 3));
        return flowLayoutPanel(panel);
    }

    private JPanel delayPanel(HavokItemStack itemStack) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(itemStack.getDelay());
        delayTextField.setPreferredSize(new Dimension(100, delayTextField.getPreferredSize().height));
        panel.add(flowLayoutPanel(delayTextField), gbc(1, 0));
        return panel;
    }

    private JPanel idPanel(HavokItemStack itemStack) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("ID:", SwingConstants.LEFT), gbc(0, 0));
        itemComboBox = new JComboBox<>(new SortedComboBoxModel<>(new ArrayList<>(Item.REGISTRY.getKeys()), Comparator.comparing(ResourceLocation::toString)));
        itemComboBox.setSelectedItem(Item.REGISTRY.getNameForObject(itemStack.getItemStack().getItem()));
        panel.add(flowLayoutPanel(itemComboBox), gbc(1, 0));
        return panel;
    }

    private JPanel numericalPanel(HavokItemStack itemStack) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Amount:", SwingConstants.LEFT), gbc(0, 0));
        ItemStack is = itemStack.getItemStack();
        amountTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        amountTextField.setValue(is.getCount());
        Dimension yDim = amountTextField.getPreferredSize();
        yDim.width = 100;
        amountTextField.setPreferredSize(yDim);
        panel.add(flowLayoutPanel(amountTextField), gbc(1, 0));
        panel.add(parseJLabel("Damage:", SwingConstants.LEFT), gbc(2, 0));
        damageTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        damageTextField.setValue(is.getItemDamage());
        Dimension xDim = damageTextField.getPreferredSize();
        xDim.width = 100;
        damageTextField.setPreferredSize(xDim);
        panel.add(flowLayoutPanel(damageTextField), gbc(3, 0));
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

    private JPanel rightPanel(HavokItemStack itemStack) {
        JPanel panel = new JPanel(new GridBagLayout());
        NBTTagCompound nbt = itemStack.getItemStack().getTagCompound();
        jsonPanel = new JsonPanel(nbt == null ? new NBTTagCompound() : nbt);
        nbtTree = jsonPanel.getTree();
        panel.add(parseJLabel("Tag", SwingConstants.CENTER), gbc(0, 0));
        panel.add(jsonPanel.getScrollPane(), gbc(0, 1));
        return flowLayoutPanel(panel);
    }
}
