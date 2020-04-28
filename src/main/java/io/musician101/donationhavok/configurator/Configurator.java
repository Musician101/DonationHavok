package io.musician101.donationhavok.configurator;

import io.musician101.donationhavok.configurator.gui.ConfigGui;
import io.musician101.donationhavok.configurator.serialization.GeneralConfig;
import java.util.logging.Logger;

public class Configurator {

    public static final Logger LOGGER = Logger.getLogger("DonationHavok Configurator");

    public static void main(String... args) {
        GeneralConfig generalConfig = new GeneralConfig().load();
        new ConfigGui(generalConfig);
    }
}
