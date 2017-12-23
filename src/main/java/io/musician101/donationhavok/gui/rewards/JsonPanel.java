package io.musician101.donationhavok.gui.rewards;

import com.google.gson.JsonObject;
import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.tree.HavokArrayTreeNode;
import io.musician101.donationhavok.gui.tree.HavokBooleanTreeNode;
import io.musician101.donationhavok.gui.tree.HavokBranchingTreeNode;
import io.musician101.donationhavok.gui.tree.HavokMapTreeNode;
import io.musician101.donationhavok.gui.tree.HavokNumberTreeNode;
import io.musician101.donationhavok.gui.tree.HavokStringTreeNode;
import io.musician101.donationhavok.gui.tree.HavokTreeNode;
import io.musician101.donationhavok.gui.tree.HavokValueTreeNode;
import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import net.minecraft.nbt.NBTTagCompound;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

public class JsonPanel extends BaseGUI {

    private final boolean isNBT;
    private JButton booleanButton;
    private JButton byteButton;
    private JButton byteArrayButton;
    private JButton compoundButton;
    private JButton deleteTagButton;
    private JButton doubleButton;
    private JButton editTagButton;
    private JButton floatButton;
    private JButton integerButton;
    private JButton integerArrayButton;
    private JButton listButton;
    private JButton longButton;
    private JButton renameTagButton;
    private JButton shortButton;
    private JButton stringButton;
    private JTree tree;
    private JPanel scrollPane;

    public JsonPanel(NBTTagCompound nbt) {
        this.scrollPane = parseTabbedPane(GSON.toJsonTree(nbt).getAsJsonObject());
        isNBT = true;
    }

    public JsonPanel(JsonObject json) {
        this.scrollPane = parseTabbedPane(json);
        isNBT = false;
    }

    public JPanel getScrollPane() {
        return scrollPane;
    }

    public JTree getTree() {
        return tree;
    }

    private JPanel parseTabbedPane(JsonObject json) {
        JPanel panel = new JPanel(new GridBagLayout());
        tree = new JTree(new HavokMapTreeNode(json));
        tree.setCellRenderer(new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (value instanceof HavokArrayTreeNode) {
                    HavokArrayTreeNode array = (HavokArrayTreeNode) value;
                    if (array.isByteArray()) {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/byte_array.png"), "Byte Array"));
                    }
                    else if (array.isIntArray()) {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/integer_array.png"), "Integer Array"));
                    }
                    else {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/list.png"), "List"));
                    }
                }
                else if (value instanceof HavokBooleanTreeNode) {
                    label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/boolean.png"), "Boolean"));
                }
                else if (value instanceof HavokMapTreeNode) {
                    label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/compound.png"), "Tag Compound"));
                }
                else if (value instanceof HavokNumberTreeNode) {
                    Number number = ((HavokNumberTreeNode) value).getValue();
                    if (number instanceof Byte) {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/byte.png"), "Byte"));
                    }
                    else if (number instanceof Double) {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/double.png"), "Double"));
                    }
                    else if (number instanceof Float) {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/float.png"), "Float Array"));
                    }
                    else if (number instanceof Integer) {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/integer.png"), "Integer"));
                    }
                    else if (number instanceof Long) {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/long.png"), "Long"));
                    }
                    else if (number instanceof Short) {
                        label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/short.png"), "Short"));
                    }
                }
                else if (value instanceof HavokStringTreeNode) {
                    label.setIcon(new ImageIcon(getClass().getClassLoader().getResource("assets/donationhavok/icon/string.png"), "String"));
                }

                return label;
            }
        });
        tree.addTreeSelectionListener(l -> {
            TreePath path = l.getNewLeadSelectionPath();
            if (path == null) {
                return;
            }

            updateButtons(path.getLastPathComponent());
        });
        panel.add(new JScrollPane(tree), gbc(0, 0));
        panel.add(buttons(), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    @Override
    protected void update(BaseGUI prevGUI) {

    }

    private void updateButtons(Object node) {
        if (node != null) {
            newButtons((HavokTreeNode) node);
            if (node.equals(tree.getModel().getRoot())) {
                setButtonEnabled(deleteTagButton, false);
            }
            else {
                setButtonEnabled(deleteTagButton, true);
            }
        }
        else {
            setButtonEnabled(booleanButton, false);
            setButtonEnabled(byteButton, false);
            setButtonEnabled(byteArrayButton, false);
            setButtonEnabled(compoundButton, false);
            setButtonEnabled(doubleButton, false);
            setButtonEnabled(deleteTagButton, false);
            setButtonEnabled(editTagButton, false);
            setButtonEnabled(floatButton, false);
            setButtonEnabled(integerButton, false);
            setButtonEnabled(integerArrayButton, false);
            setButtonEnabled(listButton, false);
            setButtonEnabled(longButton, false);
            setButtonEnabled(renameTagButton, false);
            setButtonEnabled(shortButton, false);
            setButtonEnabled(stringButton, false);
        }
    }

    private void newButtons(HavokTreeNode node) {
        if (node instanceof HavokValueTreeNode) {
            valueButtons();
        }
        else if (node instanceof HavokArrayTreeNode) {
            arrayButtons((HavokArrayTreeNode) node);
        }
        else if (node instanceof HavokMapTreeNode) {
            mapButtons();
        }

        if (node.getKey() == null) {
            setButtonEnabled(renameTagButton, false);
        }
        else {
            setButtonEnabled(renameTagButton, true);
        }
    }

    private void arrayButtons(HavokArrayTreeNode node) {
        if (node.isByteArray()) {
            setButtonEnabled(booleanButton, false);
            setButtonEnabled(byteButton, true);
            setButtonEnabled(byteArrayButton, false);
            setButtonEnabled(compoundButton, false);
            setButtonEnabled(doubleButton, false);
            setButtonEnabled(editTagButton, false);
            setButtonEnabled(floatButton, false);
            setButtonEnabled(integerButton, false);
            setButtonEnabled(integerArrayButton, false);
            setButtonEnabled(listButton, false);
            setButtonEnabled(longButton, false);
            setButtonEnabled(shortButton, false);
            setButtonEnabled(stringButton, false);
        }
        else if (node.isIntArray()) {
            setButtonEnabled(booleanButton, false);
            setButtonEnabled(byteButton, false);
            setButtonEnabled(byteArrayButton, false);
            setButtonEnabled(compoundButton, false);
            setButtonEnabled(doubleButton, false);
            setButtonEnabled(editTagButton, false);
            setButtonEnabled(floatButton, false);
            setButtonEnabled(integerButton, true);
            setButtonEnabled(integerArrayButton, false);
            setButtonEnabled(listButton, false);
            setButtonEnabled(longButton, false);
            setButtonEnabled(shortButton, false);
            setButtonEnabled(stringButton, false);
        }
        else {
            if (isNBT && node.getChildCount() > 0) {
                TreeNode child = node.getChildAt(0);
                if (child instanceof HavokValueTreeNode) {
                    if (child instanceof HavokBooleanTreeNode) {
                        setButtonEnabled(booleanButton, true);
                        setButtonEnabled(byteButton, false);
                        setButtonEnabled(byteArrayButton, false);
                        setButtonEnabled(compoundButton, false);
                        setButtonEnabled(doubleButton, false);
                        setButtonEnabled(floatButton, false);
                        setButtonEnabled(integerButton, false);
                        setButtonEnabled(integerArrayButton, false);
                        setButtonEnabled(listButton, false);
                        setButtonEnabled(longButton, false);
                        setButtonEnabled(shortButton, false);
                        setButtonEnabled(stringButton, false);
                    }
                    else if (child instanceof HavokNumberTreeNode) {
                        Number number = ((HavokNumberTreeNode) child).getValue();
                        if (number instanceof Byte) {
                            setButtonEnabled(booleanButton, false);
                            setButtonEnabled(byteButton, true);
                            setButtonEnabled(byteArrayButton, false);
                            setButtonEnabled(compoundButton, false);
                            setButtonEnabled(doubleButton, false);
                            setButtonEnabled(floatButton, false);
                            setButtonEnabled(integerButton, false);
                            setButtonEnabled(integerArrayButton, false);
                            setButtonEnabled(listButton, false);
                            setButtonEnabled(longButton, false);
                            setButtonEnabled(shortButton, false);
                            setButtonEnabled(stringButton, false);
                        }
                        else if (number instanceof Double) {
                            setButtonEnabled(booleanButton, false);
                            setButtonEnabled(byteButton, false);
                            setButtonEnabled(byteArrayButton, false);
                            setButtonEnabled(compoundButton, false);
                            setButtonEnabled(doubleButton, true);
                            setButtonEnabled(floatButton, false);
                            setButtonEnabled(integerButton, false);
                            setButtonEnabled(integerArrayButton, false);
                            setButtonEnabled(listButton, false);
                            setButtonEnabled(longButton, false);
                            setButtonEnabled(shortButton, false);
                            setButtonEnabled(stringButton, false);
                        }
                        else if (number instanceof Float) {
                            setButtonEnabled(booleanButton, false);
                            setButtonEnabled(byteButton, false);
                            setButtonEnabled(byteArrayButton, false);
                            setButtonEnabled(compoundButton, false);
                            setButtonEnabled(doubleButton, false);
                            setButtonEnabled(floatButton, true);
                            setButtonEnabled(integerButton, false);
                            setButtonEnabled(integerArrayButton, false);
                            setButtonEnabled(listButton, false);
                            setButtonEnabled(longButton, false);
                            setButtonEnabled(shortButton, false);
                            setButtonEnabled(stringButton, false);
                        }
                        else if (number instanceof Integer) {
                            setButtonEnabled(booleanButton, false);
                            setButtonEnabled(byteButton, false);
                            setButtonEnabled(byteArrayButton, false);
                            setButtonEnabled(compoundButton, false);
                            setButtonEnabled(doubleButton, false);
                            setButtonEnabled(floatButton, false);
                            setButtonEnabled(integerButton, true);
                            setButtonEnabled(integerArrayButton, false);
                            setButtonEnabled(listButton, false);
                            setButtonEnabled(longButton, false);
                            setButtonEnabled(shortButton, false);
                            setButtonEnabled(stringButton, false);
                        }
                        else if (number instanceof Long) {
                            setButtonEnabled(booleanButton, false);
                            setButtonEnabled(byteButton, false);
                            setButtonEnabled(byteArrayButton, false);
                            setButtonEnabled(compoundButton, false);
                            setButtonEnabled(doubleButton, false);
                            setButtonEnabled(floatButton, false);
                            setButtonEnabled(integerButton, false);
                            setButtonEnabled(integerArrayButton, false);
                            setButtonEnabled(listButton, false);
                            setButtonEnabled(longButton, true);
                            setButtonEnabled(shortButton, false);
                            setButtonEnabled(stringButton, false);
                        }
                        else if (number instanceof Short) {
                            setButtonEnabled(booleanButton, false);
                            setButtonEnabled(byteButton, false);
                            setButtonEnabled(byteArrayButton, false);
                            setButtonEnabled(compoundButton, false);
                            setButtonEnabled(doubleButton, false);
                            setButtonEnabled(floatButton, false);
                            setButtonEnabled(integerButton, false);
                            setButtonEnabled(integerArrayButton, false);
                            setButtonEnabled(listButton, false);
                            setButtonEnabled(longButton, false);
                            setButtonEnabled(shortButton, true);
                            setButtonEnabled(stringButton, false);
                        }
                    }
                    else if (child instanceof HavokStringTreeNode) {
                        setButtonEnabled(booleanButton, false);
                        setButtonEnabled(byteButton, false);
                        setButtonEnabled(byteArrayButton, false);
                        setButtonEnabled(compoundButton, false);
                        setButtonEnabled(doubleButton, false);
                        setButtonEnabled(floatButton, false);
                        setButtonEnabled(integerButton, false);
                        setButtonEnabled(integerArrayButton, false);
                        setButtonEnabled(listButton, false);
                        setButtonEnabled(longButton, false);
                        setButtonEnabled(shortButton, false);
                        setButtonEnabled(stringButton, true);
                    }
                }
                else if (child instanceof HavokArrayTreeNode) {
                    HavokArrayTreeNode array = (HavokArrayTreeNode) child;
                    if (array.isByteArray()) {
                        setButtonEnabled(booleanButton, false);
                        setButtonEnabled(byteButton, false);
                        setButtonEnabled(byteArrayButton, true);
                        setButtonEnabled(compoundButton, false);
                        setButtonEnabled(doubleButton, false);
                        setButtonEnabled(floatButton, false);
                        setButtonEnabled(integerButton, false);
                        setButtonEnabled(integerArrayButton, false);
                        setButtonEnabled(listButton, false);
                        setButtonEnabled(longButton, false);
                        setButtonEnabled(shortButton, false);
                        setButtonEnabled(stringButton, false);
                    }
                    else if (array.isIntArray()) {
                        setButtonEnabled(booleanButton, false);
                        setButtonEnabled(byteButton, false);
                        setButtonEnabled(byteArrayButton, false);
                        setButtonEnabled(compoundButton, false);
                        setButtonEnabled(doubleButton, false);
                        setButtonEnabled(floatButton, false);
                        setButtonEnabled(integerButton, false);
                        setButtonEnabled(integerArrayButton, true);
                        setButtonEnabled(listButton, false);
                        setButtonEnabled(longButton, false);
                        setButtonEnabled(shortButton, false);
                        setButtonEnabled(stringButton, false);
                    }
                    else {
                        setButtonEnabled(booleanButton, false);
                        setButtonEnabled(byteButton, false);
                        setButtonEnabled(byteArrayButton, false);
                        setButtonEnabled(compoundButton, false);
                        setButtonEnabled(doubleButton, false);
                        setButtonEnabled(floatButton, false);
                        setButtonEnabled(integerButton, false);
                        setButtonEnabled(integerArrayButton, false);
                        setButtonEnabled(listButton, true);
                        setButtonEnabled(longButton, false);
                        setButtonEnabled(shortButton, false);
                        setButtonEnabled(stringButton, false);
                    }
                }
                else if (child instanceof HavokMapTreeNode) {
                    setButtonEnabled(booleanButton, false);
                    setButtonEnabled(booleanButton, false);
                    setButtonEnabled(byteButton, false);
                    setButtonEnabled(byteArrayButton, false);
                    setButtonEnabled(compoundButton, true);
                    setButtonEnabled(doubleButton, false);
                    setButtonEnabled(floatButton, false);
                    setButtonEnabled(integerButton, false);
                    setButtonEnabled(integerArrayButton, false);
                    setButtonEnabled(listButton, false);
                    setButtonEnabled(longButton, false);
                    setButtonEnabled(shortButton, false);
                    setButtonEnabled(stringButton, false);
                }
            }
            else {
                setButtonEnabled(booleanButton, true);
                setButtonEnabled(booleanButton, true);
                setButtonEnabled(byteButton, true);
                setButtonEnabled(byteArrayButton, true);
                setButtonEnabled(compoundButton, true);
                setButtonEnabled(doubleButton, true);
                setButtonEnabled(floatButton, true);
                setButtonEnabled(integerButton, true);
                setButtonEnabled(integerArrayButton, true);
                setButtonEnabled(listButton, true);
                setButtonEnabled(longButton, true);
                setButtonEnabled(shortButton, true);
                setButtonEnabled(stringButton, true);
            }
        }

        setButtonEnabled(editTagButton, false);
    }

    private void mapButtons() {
        setButtonEnabled(booleanButton, true);
        setButtonEnabled(byteButton, true);
        setButtonEnabled(byteArrayButton, true);
        setButtonEnabled(compoundButton, true);
        setButtonEnabled(doubleButton, true);
        setButtonEnabled(editTagButton, false);
        setButtonEnabled(floatButton, true);
        setButtonEnabled(integerButton, true);
        setButtonEnabled(integerArrayButton, true);
        setButtonEnabled(listButton, true);
        setButtonEnabled(longButton, true);
        setButtonEnabled(shortButton, true);
        setButtonEnabled(stringButton, true);
    }

    private void valueButtons() {
        setButtonEnabled(booleanButton, false);
        setButtonEnabled(byteButton, false);
        setButtonEnabled(byteArrayButton, false);
        setButtonEnabled(compoundButton, false);
        setButtonEnabled(doubleButton, false);
        setButtonEnabled(editTagButton, true);
        setButtonEnabled(floatButton, false);
        setButtonEnabled(integerButton, false);
        setButtonEnabled(integerArrayButton, false);
        setButtonEnabled(listButton, false);
        setButtonEnabled(longButton, false);
        setButtonEnabled(shortButton, false);
        setButtonEnabled(stringButton, false);
    }

    private void setButtonEnabled(JButton button, boolean isEnabled) {
        ButtonModel model = button.getModel();
        model.setArmed(!isEnabled);
        model.setEnabled(isEnabled);
    }

    private JPanel buttons() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(editButtons(), gbc(0, 0));
        panel.add(addButtons1(), gbc(0, 1));
        panel.add(addButtons2(), gbc(0, 2));
        updateButtons(null);
        return flowLayoutPanel(panel);
    }

    @SuppressWarnings("ConstantConditions")
    private JPanel editButtons() {
        JPanel panel = new JPanel(new GridBagLayout());
        renameTagButton = parseJButton("Rename Tag", l -> {
            HavokTreeNode node = (HavokTreeNode) tree.getLastSelectedPathComponent();
            if (node.getKey() != null) {
                node.setKey(JOptionPane.showInputDialog("Enter the new name for this tag.", node.getKey()));
                parseChanges(node);
            }
        });
        panel.add(flowLayoutPanel(renameTagButton), gbc(0, 0));
        editTagButton = parseJButton("Edit Tag", l -> {
            HavokTreeNode node = (HavokTreeNode) tree.getLastSelectedPathComponent();
            if (node instanceof HavokValueTreeNode) {
                if (node instanceof HavokBooleanTreeNode) {
                    HavokBooleanTreeNode booleanTreeNode = (HavokBooleanTreeNode) node;
                    booleanTreeNode.setValue((Boolean) inputDialog(booleanTreeNode.getValue(), new Object[]{true, false}));
                }
                else if (node instanceof HavokNumberTreeNode) {
                    HavokNumberTreeNode numberTreeNode = (HavokNumberTreeNode) node;
                    Number number = numberTreeNode.getValue();
                    try {
                        if (number instanceof Byte) {
                            numberTreeNode.setValue(Byte.valueOf(inputDialog(number)));
                        }
                        else if (number instanceof Double) {
                            numberTreeNode.setValue(Double.valueOf(inputDialog(number)));
                        }
                        else if (number instanceof Float) {
                            numberTreeNode.setValue(Float.valueOf(inputDialog(number)));
                        }
                        else if (number instanceof Integer) {
                            numberTreeNode.setValue(Integer.valueOf(inputDialog(number)));
                        }
                        else if (number instanceof Long) {
                            numberTreeNode.setValue(Long.valueOf(inputDialog(number)));
                        }
                        else if (number instanceof Short) {
                            numberTreeNode.setValue(Short.valueOf(inputDialog(number)));
                        }
                    }
                    catch (NumberFormatException e) {
                        JOptionPane.showConfirmDialog(null, e.getMessage(), "Invalid Input!", JOptionPane.DEFAULT_OPTION);
                    }
                }
                else if (node instanceof HavokStringTreeNode) {
                    HavokStringTreeNode stringTreeNode = (HavokStringTreeNode) node;
                    stringTreeNode.setValue(inputDialog(stringTreeNode.getValue()));
                }
            }

            parseChanges(node);
        });
        panel.add(flowLayoutPanel(editTagButton), gbc(1, 0));
        deleteTagButton = parseJButton("Delete Tag", l -> {
            MutableTreeNode node = (MutableTreeNode) tree.getLastSelectedPathComponent();
            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
            updateButtons(null);
        });
        panel.add(flowLayoutPanel(deleteTagButton), gbc(2, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel addButtons1() {
        JPanel panel = new JPanel(new GridBagLayout());
        booleanButton = parseJButton("New Boolean", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            if (node instanceof HavokArrayTreeNode) {
                Boolean b = (Boolean) inputDialog(true, new Object[]{true, false});
                if (b != null) {
                    node.add(new HavokBooleanTreeNode(b));
                }
            }
            else if (node instanceof HavokMapTreeNode) {
                String key = inputDialog("key");
                if (key == null) {
                    return;
                }

                Boolean b = (Boolean) inputDialog(true, new Object[]{true, false});
                if (b == null) {
                    return;
                }

                node.add(new HavokBooleanTreeNode(key, b));
            }

            parseChanges(node);
        });
        panel.add(flowLayoutPanel(booleanButton), gbc(0, 0));
        byteButton = parseJButton("New Byte", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            try {
                if (node instanceof HavokArrayTreeNode) {
                    String result = inputDialog(0);
                    if (result != null) {
                        node.add(new HavokNumberTreeNode(Byte.valueOf(result)));
                    }
                }
                else if (node instanceof HavokMapTreeNode) {
                    String key = inputDialog("key");
                    if (key == null) {
                        return;
                    }

                    String result = inputDialog(0);
                    if (result == null) {
                        return;
                    }

                    node.add(new HavokNumberTreeNode(key, Byte.valueOf(result)));
                }

                parseChanges(node);
            }
            catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, e.getMessage(), "Invalid Input!", JOptionPane.DEFAULT_OPTION);
            }
        });
        panel.add(flowLayoutPanel(byteButton), gbc(1, 0));
        byteArrayButton = parseJButton("New Byte Array", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            if (node instanceof HavokArrayTreeNode) {
                node.add(new HavokArrayTreeNode(true, false));
            }
            else if (node instanceof HavokMapTreeNode) {
                String key = inputDialog("key");
                if (key != null) {
                    node.add(new HavokArrayTreeNode(key, true, false));
                }
            }

            parseChanges(node);
        });
        panel.add(flowLayoutPanel(byteArrayButton), gbc(2, 0));
        compoundButton = parseJButton("New Compound", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            if (node instanceof HavokArrayTreeNode) {
                node.add(new HavokMapTreeNode());
            }
            else if (node instanceof HavokMapTreeNode) {
                String key = inputDialog("key");
                if (key != null) {
                    node.add(new HavokMapTreeNode(key));
                }
            }

            parseChanges(node);
        });
        panel.add(flowLayoutPanel(compoundButton), gbc(3, 0));
        doubleButton = parseJButton("New Double", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            try {
                if (node instanceof HavokArrayTreeNode) {
                    String result = inputDialog(0);
                    if (result != null) {
                        node.add(new HavokNumberTreeNode(Double.valueOf(result)));
                    }
                }
                else if (node instanceof HavokMapTreeNode) {
                    String key = inputDialog("key");
                    if (key == null) {
                        return;
                    }

                    String result = inputDialog(0);
                    if (result == null) {
                        return;
                    }

                    node.add(new HavokNumberTreeNode(key, Double.valueOf(result)));
                }

                parseChanges(node);
            }
            catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, e.getMessage(), "Invalid Input!", JOptionPane.DEFAULT_OPTION);
            }
        });
        panel.add(flowLayoutPanel(doubleButton), gbc(4, 0));
        floatButton = parseJButton("New Float", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            try {
                if (node instanceof HavokArrayTreeNode) {
                    String result = inputDialog(0);
                    if (result != null) {
                        node.add(new HavokNumberTreeNode(Float.valueOf(result)));
                    }
                }
                else if (node instanceof HavokMapTreeNode) {
                    String key = inputDialog("key");
                    if (key == null) {
                        return;
                    }

                    String result = inputDialog(0);
                    if (result == null) {
                        return;
                    }

                    node.add(new HavokNumberTreeNode(key, Float.valueOf(result)));
                }

                parseChanges(node);
            }
            catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, e.getMessage(), "Invalid Input!", JOptionPane.DEFAULT_OPTION);
            }
        });
        panel.add(flowLayoutPanel(floatButton), gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel addButtons2() {
        JPanel panel = new JPanel(new GridBagLayout());
        integerButton = parseJButton("New Integer", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            try {
                if (node instanceof HavokArrayTreeNode) {
                    String result = inputDialog(0);
                    if (result != null) {
                        node.add(new HavokNumberTreeNode(Integer.valueOf(result)));
                    }
                }
                else if (node instanceof HavokMapTreeNode) {
                    String key = inputDialog("key");
                    if (key == null) {
                        return;
                    }

                    String result = inputDialog(0);
                    if (result == null) {
                        return;
                    }

                    node.add(new HavokNumberTreeNode(key, Integer.valueOf(result)));
                }

                parseChanges(node);
            }
            catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, e.getMessage(), "Invalid Input!", JOptionPane.DEFAULT_OPTION);
            }
        });
        panel.add(flowLayoutPanel(integerButton), gbc(0, 0));
        integerArrayButton = parseJButton("New Integer Array", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            if (node instanceof HavokArrayTreeNode) {
                node.add(new HavokArrayTreeNode(false, true));
            }
            else if (node instanceof HavokMapTreeNode) {
                String key = inputDialog("key");
                if (key != null) {
                    node.add(new HavokArrayTreeNode(key, false, true));
                }
            }

            parseChanges(node);
        });
        panel.add(flowLayoutPanel(integerArrayButton), gbc(1, 0));
        listButton = parseJButton("New List", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            if (node instanceof HavokArrayTreeNode) {
                node.add(new HavokArrayTreeNode());
            }
            else if (node instanceof HavokMapTreeNode) {
                String key = inputDialog("key");
                if (key != null) {
                    node.add(new HavokArrayTreeNode(key));
                }
            }

            parseChanges(node);
        });
        panel.add(flowLayoutPanel(listButton), gbc(2, 0));
        longButton = parseJButton("New Long", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            try {
                if (node instanceof HavokArrayTreeNode) {
                    String result = inputDialog(0);
                    if (result != null) {
                        node.add(new HavokNumberTreeNode(Long.valueOf(result)));
                    }
                }
                else if (node instanceof HavokMapTreeNode) {
                    String key = inputDialog("key");
                    if (key == null) {
                        return;
                    }

                    String result = inputDialog(0);
                    if (result == null) {
                        return;
                    }

                    node.add(new HavokNumberTreeNode(key, Long.valueOf(result)));
                }

                parseChanges(node);
            }
            catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, e.getMessage(), "Invalid Input!", JOptionPane.DEFAULT_OPTION);
            }
        });
        panel.add(flowLayoutPanel(longButton), gbc(3, 0));
        shortButton = parseJButton("New Short", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            try {
                if (node instanceof HavokArrayTreeNode) {
                    String result = inputDialog(0);
                    if (result != null) {
                        node.add(new HavokNumberTreeNode(Short.valueOf(result)));
                    }
                }
                else if (node instanceof HavokMapTreeNode) {
                    String key = inputDialog("key");
                    if (key == null) {
                        return;
                    }

                    String result = inputDialog(0);
                    if (result == null) {
                        return;
                    }

                    node.add(new HavokNumberTreeNode(key, Short.valueOf(result)));
                }

                parseChanges(node);
            }
            catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, e.getMessage(), "Invalid Input!", JOptionPane.DEFAULT_OPTION);
            }
        });
        panel.add(flowLayoutPanel(shortButton), gbc(4, 0));
        stringButton = parseJButton("New String", l -> {
            HavokBranchingTreeNode node = (HavokBranchingTreeNode) tree.getLastSelectedPathComponent();
            if (node instanceof HavokArrayTreeNode) {
                String s = inputDialog("value");
                if (s != null) {
                    node.add(new HavokStringTreeNode(s));
                }
            }
            else if (node instanceof HavokMapTreeNode) {
                String key = inputDialog("key");
                if (key == null) {
                    return;
                }

                String s = inputDialog("value");
                if (s == null) {
                    return;
                }

                node.add(new HavokStringTreeNode(key, s));
            }

            parseChanges(node);
        });
        panel.add(flowLayoutPanel(stringButton), gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    private Object inputDialog(Object initialValue, Object[] allowedValues) {
        return JOptionPane.showInputDialog(null, (allowedValues == null ? "Enter a new value:" : "Choose a new value"), "Input", JOptionPane.INFORMATION_MESSAGE, null, allowedValues, initialValue);
    }

    private String inputDialog(Object initialValue) {
        Object result = inputDialog(initialValue, null);
        return result == null ? null : result.toString();
    }

    private void parseChanges(TreeNode node) {
        TreePath[] selectionPaths = tree.getSelectionModel().getSelectionPaths();
        TreePath lastPath = selectionPaths[selectionPaths.length - 1];
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.reload(node);
        model.nodeChanged(node);
        tree.setSelectionPaths(selectionPaths);
        tree.expandPath(lastPath);
        updateButtons(node);
    }
}
