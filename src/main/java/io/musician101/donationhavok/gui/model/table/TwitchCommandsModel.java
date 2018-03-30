package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.twitch.commands.Command;
import io.musician101.donationhavok.handler.twitch.commands.CommandPermission;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class TwitchCommandsModel extends ListTableModel<Command> {

    public TwitchCommandsModel(List<Command> elements) {
        super(elements, "Name", "Enabled?", "Permissions");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Command command = getObjectAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return "!" + command.getCommand();
            case 1:
                return command.isEnabled() ? "Yes" : "No";
            case 2:
                return StringUtils.join(command.getRequiredPermissions().stream().map(CommandPermission::toString).map(String::toLowerCase).map(StringUtils::capitalize).map(s -> {
                    if (s.equalsIgnoreCase(CommandPermission.PRIME_TURBO.toString())) {
                        return "Prime/Turbo";
                    }

                    return s;
                }).collect(Collectors.toList()), ", ");
            default:
                return null;
        }
    }
}
