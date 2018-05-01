package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.havok.HavokStructure;
import java.util.List;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class HavokStructureTableModel extends ListTableModel<HavokStructure> {

    public HavokStructureTableModel(List<HavokStructure> elements) {
        super(elements, "Delay", "X Offset", "Y Offset", "Z Offset", "Name", "Mirror", "Rotation", "Integrity", "Seed");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HavokStructure structure = elements.get(rowIndex);
        PlacementSettings placementSettings = structure.getPlacementSettings();
        switch (columnIndex) {
            case 0:
                return structure.getDelay();
            case 1:
                return structure.getXOffset();
            case 2:
                return structure.getYOffset();
            case 3:
                return structure.getZOffset();
            case 4:
                return structure.getStructureName();
            case 5:
                return placementSettings.getMirror();
            case 6:
                return placementSettings.getRotation();
            case 7:
                return placementSettings.getIntegrity();
            case 8:
                return structure.getPlacementSettingsSeed();
            default:
                return null;
        }
    }
}
