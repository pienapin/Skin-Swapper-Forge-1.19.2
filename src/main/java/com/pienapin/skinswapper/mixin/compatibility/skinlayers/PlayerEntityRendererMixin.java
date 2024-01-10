package com.pienapin.skinswapper.mixin.compatibility.skinlayers;

import com.pienapin.skinswapper.gui.SkinScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PlayerEntityRenderer.class, priority = 1100)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Shadow protected abstract void setModelPose(AbstractClientPlayerEntity player);

//    @Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/PlayerEntityRenderer;setModelPose(net/minecraft/client/network/AbstractClientPlayerEntity)"))
    public void setModelPoseRedirect(PlayerEntityRenderer playerEntityRenderer, AbstractClientPlayerEntity player) {
        if((MinecraftClient.getInstance().currentScreen instanceof SkinScreen)){
            PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = (PlayerEntityModel)this.getModel();
            playerEntityModel.setVisible(true);
            playerEntityModel.hat.visible = true;
            playerEntityModel.jacket.visible = true;
            playerEntityModel.leftPants.visible = true;
            playerEntityModel.rightPants.visible = true;
            playerEntityModel.leftSleeve.visible = true;
            playerEntityModel.rightSleeve.visible = true;
            playerEntityModel.sneaking = player.isInSneakingPose();
        }

        else {
            setModelPose(player);
        }
    }
}
