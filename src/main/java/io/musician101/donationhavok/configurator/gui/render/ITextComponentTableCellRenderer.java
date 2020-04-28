package io.musician101.donationhavok.configurator.gui.render;

public class ITextComponentTableCellRenderer {/*extends DefaultTableCellRenderer {

    public ITextComponentTableCellRenderer() {
        super();
    }

    private String convertToHTML(JsonObject jsonObject) {
        boolean bold = getBoolean(jsonObject, "bold");
        boolean italic = getBoolean(jsonObject, "italic");
        boolean strikeThrough = getBoolean(jsonObject, "strikethrough");
        boolean underlined = getBoolean(jsonObject, "underlined");
        List<String> extra = new ArrayList<>();
        if (jsonObject.has("extra")) {
            jsonObject.getAsJsonArray("extra").forEach(jsonElement -> extra.add(convertToHTML(jsonElement.getAsJsonObject())));
        }

        String color = jsonObject.has("color") ? jsonObject.get("color").getAsString() : null;
        String colorHex = parseColorHex(color);
        StringBuilder sb = new StringBuilder("<span style=\"background-color:#" + parseBackgroundColor(colorHex) + ";color:" + colorHex + ";\">");
        List<String> endTags = new ArrayList<>();
        endTags.add("</span>");
        if (bold) {
            sb.append("<b>");
            endTags.add(0, "</b>");
        }

        if (italic) {
            sb.append("<i>");
            endTags.add(0, "</i>");
        }

        if (strikeThrough) {
            sb.append("<strike>");
            endTags.add(0, "</strike>");
        }

        if (underlined) {
            sb.append("<u>");
            endTags.add(0, "</u>");
        }

        sb.append(parseText(jsonObject));
        endTags.forEach(sb::append);
        extra.forEach(sb::append);
        return sb.append("</p>").toString();
    }

    private boolean getBoolean(JsonObject jsonObject, String key) {
        return jsonObject.has(key) && jsonObject.get(key).getAsBoolean();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JsonObject text = GSON.fromJson(value.toString(), JsonObject.class);
        label.setText("<html>" + convertToHTML(text) + "</html>");
        String tooltip = parseToolTip(text);
        if (!tooltip.isEmpty()) {
            label.setToolTipText(tooltip);
        }

        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    private String parseBackgroundColor(String colorHex) {
        if (colorHex != null) {
            switch (colorHex) {
                case "55FF55":
                case "55FFFF":
                case "FF5555":
                case "FF55FF":
                case "FFFF55":
                case "FFFFFF":
                    return "000000";
            }
        }

        return "FFFFFF";
    }

    private String parseColorHex(String colorName) {
        if (colorName != null) {
            switch (colorName) {
                case "black":
                    return "000000";
                case "dark_blue":
                    return "0000AA";
                case "dark_green":
                    return "00AA00";
                case "dark_red":
                    return "AA0000";
                case "dark_purple":
                    return "AA00AA";
                case "gold":
                    return "FFAA00";
                case "gray":
                    return "AAAAAA";
                case "dark_gray":
                    return "555555";
                case "blue":
                    return "5555FF";
                case "green":
                    return "55FF55";
                case "aqua":
                    return "55FFFF";
                case "red":
                    return "FF5555";
                case "light_purple":
                    return "FF55FF";
                case "yellow":
                    return "FFFF55";
            }
        }

        return "FFFFFF";
    }

    private String parseEvent(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            JsonObject event = jsonObject.getAsJsonObject(key);
            if (event.has("action") && event.has("value")) {
                JsonElement value = event.get("value");
                return "<li>" + parseText(jsonObject) + "<ul><li>Action: " + event.get("action").getAsString() + "</li><li>Value: " + (value.isJsonObject() ? value.toString() : value.getAsString()) + "</li></ul></li>";
            }
        }

        return "";
    }

    private String parseEvents(JsonObject jsonObject, String key) {
        List<String> list = new ArrayList<>();
        list.add(parseEvent(jsonObject, key));
        if (jsonObject.has("extra")) {
            StreamSupport.stream(jsonObject.getAsJsonArray("extra").spliterator(), false).map(JsonElement::getAsJsonObject).forEach(extra -> list.add(parseEvent(extra, key)));
        }

        list.removeIf(String::isEmpty);
        if (list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("The following text contains click events:<ul>");
        list.forEach(sb::append);
        return sb.append("</ul>").toString();
    }

    private String parseInsertion(JsonObject jsonObject) {
        List<String> list = new ArrayList<>();
        if (jsonObject.has("insertion")) {
            list.add("<li>" + parseText(jsonObject) + "<ul><li>" + jsonObject.get("insertion").getAsString() + "</li></ul></li>");
        }

        if (jsonObject.has("extra")) {
            StreamSupport.stream(jsonObject.getAsJsonArray("extra").spliterator(), false).map(JsonElement::getAsJsonObject).filter(extra -> extra.has("insertion")).forEach(extra -> list.add("<li>" + parseText(jsonObject) + "<ul><li>" + jsonObject.get("insertion").getAsString() + "</li></ul></li>"));
        }

        list.removeIf(String::isEmpty);
        if (list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("The following text contains insertion text:<ul>");
        list.forEach(sb::append);
        return sb.append("</ul>").toString();
    }

    private String parseObfuscatedText(JsonObject jsonObject) {
        List<String> list = new ArrayList<>();
        if (getBoolean(jsonObject, "obfuscated")) {
            list.add("<li>" + parseText(jsonObject) + "</li>");
        }

        if (jsonObject.has("extra")) {
            StreamSupport.stream(jsonObject.getAsJsonArray("extra").spliterator(), false).map(JsonElement::getAsJsonObject).filter(extra -> getBoolean(extra, "obfuscated")).forEach(text -> list.add(parseText(text)));
        }

        list.removeIf(String::isEmpty);
        if (list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("The following text is obfuscated:<ul>");
        list.forEach(sb::append);
        return sb.append("</ul>").toString();
    }

    private String parseText(JsonObject jsonObject) {
        if (jsonObject.has("text")) {
            return jsonObject.get("text").getAsString();
        }
        else if (jsonObject.has("translate")) {
            List<String> with = new ArrayList<>();
            jsonObject.getAsJsonArray("with").forEach(jsonElement -> with.add(jsonElement.getAsString()));
            return TextComponentHelper.createComponentTranslation(null, jsonObject.get("translate").getAsString(), with.toArray()).getString();
        }
        else if (jsonObject.has("score") && jsonObject.getAsJsonObject("score").has("name") && jsonObject.getAsJsonObject("score").has("objective")) {
            JsonObject score = jsonObject.getAsJsonObject("score");
            if (score.has("value") && !score.get("value").getAsString().isEmpty()) {
                return score.get("value").getAsString();
            }
            else {
                return score.get("name").getAsString() + ":" + score.get("objective").getAsString();
            }
        }
        else if (jsonObject.has("selector")) {
            return jsonObject.get("selector").getAsString();
        }
        else if (jsonObject.has("keybind")) {
            return new TextComponentKeybind(jsonObject.get("keybind").getAsString()).getString();
        }

        return "Invalid Json: " + jsonObject.toString();
    }

    private String parseToolTip(JsonObject jsonObject) {
        List<String> list = new ArrayList<>();
        if (jsonObject.has("score")) {
            list.add("Some text is left untranslated because it relies on information that is only available on the server.");
        }

        list.add(parseEvents(jsonObject, "clickEvent"));
        list.add(parseEvents(jsonObject, "hoverEvent"));
        list.add(parseInsertion(jsonObject));
        list.add(parseObfuscatedText(jsonObject));
        list.removeIf(String::isEmpty);
        if (list.isEmpty()) {
            return "";
        }

        return "<html>" + StringUtils.join(list, "<br>") + "</html>";
    }*/
}
