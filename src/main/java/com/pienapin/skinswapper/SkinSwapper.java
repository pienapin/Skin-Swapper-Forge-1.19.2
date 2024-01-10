package com.pienapin.skinswapper;

import com.pienapin.skinswapper.config.MidnightConfig;
import com.pienapin.skinswapper.config.SkinSwapperConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.File;

@Mod("skinswapper")
public class SkinSwapper {


    public SkinSwapper() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::clientSetup);

        MidnightConfig.init("skinswapper", SkinSwapperConfig.class);

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> MidnightConfig.getScreen(null, "skinswapper")));

        File file = new File("skins");
        file.mkdirs();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        File file = new File("skins");
        file.mkdirs();
    }
}