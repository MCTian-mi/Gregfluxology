package gregfluxology.mixins.gregtech;

import com.gregtechceu.gtceu.common.CommonInit;
import gregfluxology.util.GFyUtility;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CommonInit.class, remap = false)
public class CommonProxyMixin {

    @Redirect(method = "registerCapabilities",
            at = @At(value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/capabilities/RegisterCapabilitiesEvent;isBlockRegistered(Lnet/neoforged/neoforge/capabilities/BlockCapability;Lnet/minecraft/world/level/block/Block;)Z",
                    ordinal = 0))
    private static boolean skipGTBlocks(RegisterCapabilitiesEvent event, BlockCapability<?, ?> capability, Block block) {
        if (GFyUtility.isGTMachine(block)) {
            return false;
        }
        return event.isBlockRegistered(capability, block);
    }
}
