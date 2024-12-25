package gregfluxology.mixins.gregtech;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.forge.ForgeCommonEventListener;
import gregfluxology.cap.FEToEUProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ForgeCommonEventListener.class, remap = false)
public class ForgeCommonEventListenerMixin {

    @Inject(method = "attachCapabilities", at = @At("HEAD"), cancellable = true)
    private static void attachTileCapability(AttachCapabilitiesEvent<BlockEntity> event, CallbackInfo ci) {
        if (event.getObject() instanceof IMachineBlockEntity) {
            event.addCapability(GTCEu.id("eu_capability"), new FEToEUProvider(event.getObject()));
            ci.cancel();
        }
    }
}
