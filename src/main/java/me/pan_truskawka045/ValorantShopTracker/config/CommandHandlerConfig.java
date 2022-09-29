package me.pan_truskawka045.ValorantShopTracker.config;

import me.pan_truskawka045.AnnotationCore.annotation.configuration.Configuration;
import me.pan_truskawka045.AnnotationCore.command.CommandConfig;
import me.pan_truskawka045.AnnotationCore.configuration.AbstractConfiguration;

@Configuration(CommandConfig.class)
public class CommandHandlerConfig extends AbstractConfiguration<CommandConfig> {

    @Override
    public void configure(CommandConfig commandConfig) {
        commandConfig.setPrefix("??");
    }
}
