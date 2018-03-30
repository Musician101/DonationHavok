package io.musician101.donationhavok.gui.rewards;

import com.google.gson.JsonObject;
import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.model.table.HavokMessageTableModel;
import io.musician101.donationhavok.gui.tree.HavokMapTreeNode;
import io.musician101.donationhavok.handler.havok.HavokMessage;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import net.minecraft.util.text.ITextComponent;

import static io.musician101.donationhavok.util.json.JsonKeyProcessor.GSON;

public class HavokMessageGUI extends BaseGUI<RewardsGUI> {

    private final int index;
    private JCheckBox broadcastCheckBox;
    private JFormattedTextField delayTextField;
    private JTree messageTree;

    public HavokMessageGUI(HavokMessage message, int index, RewardsGUI prevGUI) {
        this.index = index;
        String name = "Havok Message";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, message, prevGUI));
    }

    private JPanel buttons(JFrame frame, RewardsGUI prevGUI) {
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
        return panel;
    }

    private JPanel delayPanel(HavokMessage message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(message.getDelay());
        delayTextField.setPreferredSize(new Dimension(100, delayTextField.getPreferredSize().height));
        panel.add(flowLayoutPanel(delayTextField), gbc(1, 0));
        broadcastCheckBox = new JCheckBox("Broadcast Message?", message.broadcastEnabled());
        panel.add(flowLayoutPanel(broadcastCheckBox), gbc(2, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel jsonPanel(HavokMessage message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Message", SwingConstants.CENTER), gbc(0, 0));
        JsonPanel jsonPanel = new JsonPanel(GSON.fromJson(ITextComponent.Serializer.componentToJson(message.getMessage()), JsonObject.class));
        messageTree = jsonPanel.getTree();
        panel.add(jsonPanel.getScrollPane(), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, HavokMessage message, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(delayPanel(message), gbc(0, 0));
        panel.add(jsonPanel(message), gbc(0, 1));
        panel.add(buttons(frame, prevGUI), gbc(0, 2));
        return panel;
    }

    @Override
    protected void update(RewardsGUI prevGUI) {
        JTable messagesTable = prevGUI.messagesTable;
        HavokMessageTableModel model = (HavokMessageTableModel) messagesTable.getModel();
        HavokMessage havokMessage = new HavokMessage(Integer.valueOf(delayTextField.getValue().toString()), broadcastCheckBox.isSelected(), ITextComponent.Serializer.jsonToComponent(((HavokMapTreeNode) messageTree.getModel().getRoot()).serialize().toString()));
        if (index == -1) {
            model.add(havokMessage);
        }
        else {
            model.replace(index, havokMessage);
        }
    }
}
