package gregfluxology.mixins.theoneprobe;

import com.llamalad7.mixinextras.sugar.Local;
import gregfluxology.cap.FEToEUProvider;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import net.minecraftforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DefaultProbeInfoProvider.class, remap = false)
public abstract class DefaultProbeInfoProviderMixin {

    @Shadow
    protected abstract void addRFInfo(IProbeInfo probeInfo, ProbeConfig config, long energy, long maxEnergy);

    @Redirect(method = "showRF",
            at = @At(
                    value = "INVOKE",
                    target = "Lmcjty/theoneprobe/apiimpl/providers/DefaultProbeInfoProvider;addRFInfo(Lmcjty/theoneprobe/api/IProbeInfo;Lmcjty/theoneprobe/apiimpl/ProbeConfig;JJ)V",
                    ordinal = 3
            ))
    private void redirectAddRFInfo(DefaultProbeInfoProvider instance, IProbeInfo probeInfo, ProbeConfig config, long energy, long maxEnergy, @Local IEnergyStorage handler) {
        if (!(handler instanceof FEToEUProvider.FEEnergyWrapper)) {
            addRFInfo(probeInfo, config, energy, maxEnergy);
        }
    }
}
