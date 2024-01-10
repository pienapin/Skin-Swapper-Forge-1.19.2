package com.pienapin.skinswapper.mixin.compatibility.forge;

import com.pienapin.skinswapper.gui.SkinScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class)
public class MixinPlayerEntity {

    @Inject(at = @At("HEAD"), method = "getDisplayName", cancellable = true)
    public void isPartVisible(CallbackInfoReturnable<Text> cir){
        if(MinecraftClient.getInstance().currentScreen instanceof SkinScreen) {
            cir.setReturnValue(Text.literal(""));
        }
    }
}
