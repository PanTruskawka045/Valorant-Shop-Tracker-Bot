package me.pan_truskawka045.ValorantShopTracker.config;

import me.pan_truskawka045.AnnotationCore.annotation.configuration.Configuration;
import me.pan_truskawka045.AnnotationCore.configuration.AbstractConfiguration;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

@Configuration(JDABuilder.class)
public class JDAConfig extends AbstractConfiguration<JDABuilder> {

    @Override
    public void configure(JDABuilder jdaBuilder) {
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL).enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
    }
}
