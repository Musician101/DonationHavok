package io.musician101.donationhavok.gui.rewards;

import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.model.SortedComboBoxModel;
import io.musician101.donationhavok.gui.model.table.HavokSoundTableModel;
import io.musician101.donationhavok.handler.havok.HavokSound;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import net.minecraft.util.SoundEvent;

public class HavokSoundGUI extends BaseGUI<RewardsGUI> {

    private final int index;
    private JFormattedTextField delayTextField;
    private JFormattedTextField pitchTextField;
    private JComboBox<SoundEvent> soundComboBox;
    private JFormattedTextField volumeTextField;
    private JFormattedTextField xTextField;
    private JFormattedTextField yTextField;
    private JFormattedTextField zTextField;

    public HavokSoundGUI(HavokSound sound, int index, RewardsGUI prevGUI) {
        this.index = index;
        String name = "Havok Sound";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, sound, prevGUI));
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

    private JPanel delayPanel(HavokSound sound) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(sound.getDelay());
        delayTextField.setPreferredSize(new Dimension(100, delayTextField.getPreferredSize().height));
        panel.add(flowLayoutPanel(delayTextField), gbc(1, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, HavokSound sound, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(topPanel(sound), gbc(0, 0));
        panel.add(parseJLabel("Offset", SwingConstants.CENTER), gbc(0, 1));
        panel.add(offsetPanel(sound), gbc(0, 2));
        panel.add(buttons(frame, prevGUI), gbc(0, 3));
        return flowLayoutPanel(panel);
    }

    private JPanel offsetPanel(HavokSound sound) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJLabel("X:", SwingConstants.LEFT)), gbc(0, 0));
        xTextField = new JFormattedTextField(new DecimalFormat());
        xTextField.setValue(sound.getXOffset());
        Dimension xDim = xTextField.getPreferredSize();
        xDim.width = 100;
        xTextField.setPreferredSize(xDim);
        panel.add(xTextField, gbc(1, 0));
        panel.add(flowLayoutPanel(parseJLabel("Y:", SwingConstants.LEFT)), gbc(2, 0));
        yTextField = new JFormattedTextField(new DecimalFormat());
        yTextField.setValue(sound.getYOffset());
        Dimension yDim = yTextField.getPreferredSize();
        yDim.width = 100;
        yTextField.setPreferredSize(yDim);
        panel.add(yTextField, gbc(3, 0));
        panel.add(flowLayoutPanel(parseJLabel("Z:", SwingConstants.LEFT)), gbc(4, 0));
        zTextField = new JFormattedTextField(new DecimalFormat());
        zTextField.setValue(sound.getYOffset());
        Dimension zDim = zTextField.getPreferredSize();
        zDim.width = 100;
        zTextField.setPreferredSize(zDim);
        panel.add(zTextField, gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel pitchPanel(HavokSound sound) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Pitch: ", SwingConstants.CENTER), gbc(0, 0));
        pitchTextField = new JFormattedTextField(new DecimalFormat());
        pitchTextField.setValue(sound.getPitch());
        pitchTextField.setPreferredSize(new Dimension(100, pitchTextField.getPreferredSize().height));
        panel.add(pitchTextField, gbc(1, 0));
        return panel;
    }

    private JPanel soundPanel(HavokSound sound) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Particle: ", SwingConstants.CENTER), gbc(0, 0));
        soundComboBox = new JComboBox<>(new SortedComboBoxModel<>(StreamSupport.stream(SoundEvent.REGISTRY.spliterator(), false).collect(Collectors.toList()), Comparator.comparing(o -> Objects.requireNonNull(o.getRegistryName()).toString())));
        soundComboBox.setSelectedItem(sound.getSoundEvent());
        soundComboBox.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(Objects.requireNonNull(((SoundEvent) value).getRegistryName()).toString());
                return label;
            }
        });
        panel.add(soundComboBox, gbc(1, 0));
        return panel;
    }

    private JPanel topPanel(HavokSound sound) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(delayPanel(sound), gbc(0, 0));
        panel.add(soundPanel(sound), gbc(1, 0));
        panel.add(pitchPanel(sound), gbc(0, 1));
        panel.add(volumePanel(sound), gbc(1, 1));
        return flowLayoutPanel(panel);
    }

    @Override
    protected void update(RewardsGUI prevGUI) {
        JTable soundsTable = prevGUI.soundsTable;
        HavokSoundTableModel model = (HavokSoundTableModel) soundsTable.getModel();
        HavokSound havokSound = new HavokSound(Integer.valueOf(delayTextField.getValue().toString()), Double.valueOf(xTextField.getValue().toString()), Double.valueOf(yTextField.getValue().toString()), Double.valueOf(zTextField.getValue().toString()), Float.valueOf(pitchTextField.getValue().toString()), Float.valueOf(volumeTextField.getValue().toString()), (SoundEvent) soundComboBox.getSelectedItem());
        if (index == -1) {
            model.add(havokSound);
        }
        else {
            model.replace(index, havokSound);
        }
    }

    private JPanel volumePanel(HavokSound sound) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Volume: ", SwingConstants.CENTER), gbc(0, 0));
        volumeTextField = new JFormattedTextField(new DecimalFormat());
        volumeTextField.setValue(sound.getVolume());
        volumeTextField.setPreferredSize(new Dimension(100, volumeTextField.getPreferredSize().height));
        panel.add(volumeTextField, gbc(1, 0));
        return panel;
    }
}
