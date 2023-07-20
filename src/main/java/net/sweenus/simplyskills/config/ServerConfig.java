package net.sweenus.simplyskills.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "server")
public class ServerConfig implements ConfigData {
    @Comment("""
            When enabled, changes all stat nodes in the Simply Skills trees to utilise PlayerEx stats.
            Requires that you delete the Puffish Skills config folder and reboot the server.
            """)
    public boolean playerExCompatibility = false;

}
