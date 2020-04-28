package io.musician101.donationhavok.configurator.gui.model.table;

public class TwitchCommandsModel {/*extends ListTableModel<Entry<String, TwitchCommandConfig>> {

    public TwitchCommandsModel(List<Entry<String, TwitchCommandConfig>> elements) {
        super(elements, "Name", "Enabled?", "Permissions");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Entry<String, TwitchCommandConfig> command = getObjectAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return "!" + command.getKey();
            case 1:
                return command.getValue().isEnabled() ? "Yes" : "No";
            case 2:
                return StringUtils.join(command.getValue().getPermissions().stream().map(CommandPermission::toString).map(String::toLowerCase).map(StringUtils::capitalize).map(s -> {
                    if (s.equalsIgnoreCase(CommandPermission.PRIME_TURBO.toString())) {
                        return "Prime/Turbo";
                    }

                    return s;
                }).collect(Collectors.toList()), ", ");
            default:
                return null;
        }
    }*/
}
