package io.musician101.donationhavok.config;

import java.io.IOException;
import javax.annotation.Nonnull;

public class Config {

    @Nonnull
    private GeneralConfig generalConfig;
    @Nonnull
    private RewardsConfig rewardsConfig;
    @Nonnull
    private StreamlabsConfig streamlabsConfig;
    @Nonnull
    private TwitchConfig twitchConfig;

    public Config() {
        this.generalConfig = new GeneralConfig().load();
        this.rewardsConfig = new RewardsConfig().load();
        this.streamlabsConfig = new StreamlabsConfig().load();
        this.twitchConfig = new TwitchConfig().load();
    }

    @Nonnull
    public GeneralConfig getGeneralConfig() {
        return generalConfig;
    }

    @Nonnull
    public RewardsConfig getRewardsConfig() {
        return rewardsConfig;
    }

    @Nonnull
    public StreamlabsConfig getStreamlabsConfig() {
        return streamlabsConfig;
    }

    @Nonnull
    public TwitchConfig getTwitchConfig() {
        return twitchConfig;
    }

    public void setGeneralConfig(@Nonnull GeneralConfig generalConfig) throws IOException {
        generalConfig.save();
        this.generalConfig = generalConfig;
    }

    public void setRewardsConfig(@Nonnull RewardsConfig rewardsConfig) throws IOException {
        rewardsConfig.save();
        this.rewardsConfig = rewardsConfig;
    }

    public void setStreamlabsConfig(@Nonnull StreamlabsConfig streamlabsConfig) throws IOException {
        streamlabsConfig.save();
        this.streamlabsConfig = streamlabsConfig;
    }

    public void setTwitchConfig(@Nonnull TwitchConfig twitchConfig) throws IOException {
        twitchConfig.save();
        this.twitchConfig = twitchConfig;
    }
}
