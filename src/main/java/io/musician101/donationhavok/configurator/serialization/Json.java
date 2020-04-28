package io.musician101.donationhavok.configurator.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(GeneralConfig.class, new GeneralConfig.Serializer()).create();


}
