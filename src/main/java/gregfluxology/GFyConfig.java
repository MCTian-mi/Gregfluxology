package gregfluxology;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Gregfluxology.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GFyConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static final ForgeConfigSpec SPEC = BUILDER.build();
    private static final ForgeConfigSpec.BooleanValue IGNORE_CABLE_CAPACITY = BUILDER
            .comment(
                    "Ignoring the max input amperage of a CEu cable/wire, burning them during energy overflow. (like in base CEu)")
            .define("ignoreCableCapacity", false);
    public static boolean ignoreCableCapacity;

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        ignoreCableCapacity = IGNORE_CABLE_CAPACITY.get();
    }
}
