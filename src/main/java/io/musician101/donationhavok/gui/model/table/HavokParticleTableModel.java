package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.havok.HavokParticle;
import java.util.List;

public class HavokParticleTableModel extends ListTableModel<HavokParticle> {

    public HavokParticleTableModel(List<HavokParticle> elements) {
        super(elements, "Delay", "Name", "X Offset", "Y Offset", "Z Offset", "X Velocity", "Y Velocity", "Z Velocity");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HavokParticle particle = elements.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return particle.getDelay();
            case 1:
                return particle.getXOffset();
            case 2:
                return particle.getYOffset();
            case 3:
                return particle.getZOffset();
            case 4:
                return particle.getXVelocity();
            case 5:
                return particle.getYVelocity();
            case 6:
                return particle.getZVelocity();
            case 7:
                return particle.getParticle().getParticleName();
            default:
                return null;
        }
    }
}
