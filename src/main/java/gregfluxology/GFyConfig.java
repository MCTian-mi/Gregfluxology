package gregfluxology;

import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MODID)
public class GFyConfig {

    @Config.Comment("Ignoring the max input amperage of a CEu cable/wire, burning them during energy overflow. (like in base CEu)")
    public static boolean ignoreCableCapacity = false;
}
