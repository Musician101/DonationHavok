package io.musician101.donationhavok.configurator.serialization;

import io.musician101.donationhavok.configurator.Configurator;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

import static io.musician101.donationhavok.configurator.serialization.Json.GSON;

public abstract class AbstractConfig<C> {

    @Nonnull
    protected abstract File getFile();

    public final void save() throws IOException {
        File file = getFile();
        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileWriter fw = new FileWriter(file)) {
            GSON.toJson(this, fw);
        }
    }


    public final C load() {
        File file = getFile();
        Logger logger = Configurator.LOGGER;
        if (!file.exists()) {
            try {
                save();
            }
            catch (IOException e) {
                logger.severe("Failed to create default instance of " + getFile().getName());
                e.printStackTrace();
            }
        }

        try (FileReader fr = new FileReader(file)) {
            return GSON.<C>fromJson(fr, getClass());
        }
        catch (IOException e) {
            logger.severe("Failed to load " + file.getName());
        }

        return (C) this;
    }
}
