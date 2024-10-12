// Copyright (C) 2018 DBot

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
// of the Software, and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all copies
// or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
// INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
// PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
// FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
// DEALINGS IN THE SOFTWARE.

package gregfluxology;

import gregfluxology.cap.FEToEUProvider;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.common.pipelike.cable.tile.TileEntityCable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MODID, name = Tags.MODNAME, version = Tags.VERSION, dependencies = "after:gregtech;")
public class Gregfluxology {

    public static Logger logger;
    public ResourceLocation resourceLocation;

    @SidedProxy(modId = Tags.MODID, clientSide = Tags.MODID + ".ClientProxy", serverSide = Tags.MODID + ".CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        resourceLocation = new ResourceLocation(Tags.MODID, "fecapability");
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void attachTileCapability(AttachCapabilitiesEvent<TileEntity> event) {
        TileEntity tileEntity = event.getObject();
        if (tileEntity instanceof IGregTechTileEntity || tileEntity instanceof TileEntityCable) {
            event.addCapability(resourceLocation, new FEToEUProvider(tileEntity));
        }
    }

//	@SubscribeEvent
//	public void attachItemCapability(AttachCapabilitiesEvent<ItemStack> event) {
//		event.addCapability(resourceLocation, new EnergyProviderItem(event.getObject()));
//	}
}
