package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.havok.HavokMessage;
import java.util.List;
import net.minecraft.util.text.ITextComponent;

public class HavokMessageTableModel extends ListTableModel<HavokMessage> {

    public HavokMessageTableModel(List<HavokMessage> elements) {
        super(elements, "Delay", "Broadcast?", "Text");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HavokMessage message = elements.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return message.getDelay();
            case 1:
                return message.broadcastEnabled();
            case 2:
                return ITextComponent.Serializer.componentToJson(message.getMessage());
            default:
                return null;
        }
    }
}
