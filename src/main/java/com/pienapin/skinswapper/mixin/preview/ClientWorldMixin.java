package com.pienapin.skinswapper.mixin.preview;

import com.pienapin.skinswapper.gui.SkinScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class ClientWorldMixin {

    @Inject(at = @At(value = "HEAD"), method = "getSpawnPos", cancellable = true)
    public void getSpawnPos(CallbackInfoReturnable<BlockPos> cir) {
        if(MinecraftClient.getInstance().currentScreen instanceof SkinScreen) {
            cir.setReturnValue(new BlockPos(0, 0, 0));
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "getSpawnAngle", cancellable = true)
    public void getSpawnAngle(CallbackInfoReturnable<Float> cir) {
        if(MinecraftClient.getInstance().currentScreen instanceof SkinScreen) {
            cir.setReturnValue(0F);
        }
    }
}
