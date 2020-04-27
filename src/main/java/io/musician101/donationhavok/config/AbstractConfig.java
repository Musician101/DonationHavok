package io.musician101.donationhavok.config;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.Reference;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.Logger;

import static io.musician101.donationhavok.util.json.Keys.GSON;

public abstract class AbstractConfig<C> {

    @Nonnull
    protected abstract File getFile();

    public final void save() throws IOException {
        File file = getFile();
        if (!file.exists()) {
            getDir().mkdirs();
            file.createNewFile();
        }

        try (FileWriter fw = new FileWriter(file)) {
            GSON.toJson(this, fw);
        }
    }

    final C load() {
        Logger logger =  DonationHavok.getInstance().getLogger();
        File file = getFile();
        if (!file.exists()) {
            try {
                save();
            }
            catch (IOException e) {
                logger.error("Failed to create default instance of " + getFile().getName(), e);
            }
        }

        try (FileReader fr = new FileReader(file)) {
            return GSON.<C>fromJson(fr, getClass());
        }
        catch (IOException e) {
            logger.error("Failed to load " + file.getName());
        }

        return (C) this;
    }

    @Nonnull
    File getDir() {
        return new File("config", Reference.MOD_ID);
    }
}
