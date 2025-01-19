package gregfluxology;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;
import org.slf4j.Logger;

@Mod(Gregfluxology.MODID)
public class Gregfluxology {

    public static final String MODID = "gregfluxology";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Gregfluxology() {
        var context= ModLoadingContext.get();
        context.registerConfig(ModConfig.Type.COMMON, GFyConfig.SPEC);
    }
}
