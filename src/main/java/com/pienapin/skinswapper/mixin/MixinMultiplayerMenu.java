package com.pienapin.skinswapper.mixin;

import com.pienapin.skinswapper.config.SkinSwapperConfig;
import com.pienapin.skinswapper.gui.SkinScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerMenu extends Screen {
	public MixinMultiplayerMenu(Text title) {
		super(title);
	}

	@Inject(at = @At("RETURN"), method = "init")
	private void init(CallbackInfo info) {
		//gets button x-coordinate
		int buttonX;
		if(SkinSwapperConfig.modButton == SkinSwapperConfig.ModButton.LEFT){
			buttonX = 25;
		} else if(SkinSwapperConfig.modButton == SkinSwapperConfig.ModButton.RIGHT){
			buttonX = this.width - 125;
		} else {
			buttonX = (this.width/2) - 50;
		}

		//draws change skin button
		this.addDrawableChild(new ButtonWidget(buttonX, 6, 100, 20, Text.translatable("Change Skin"), button ->{
			MinecraftClient.getInstance().setScreen(new SkinScreen(this));
		}));
		this.addSelectableChild(new ButtonWidget(buttonX, 6, 100, 20, Text.translatable("Change Skin"), button ->{
			MinecraftClient.getInstance().setScreen(new SkinScreen(this));
		}));
	}

	//hides multiplayer text which can block the button
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"), method = "render")
	private void render(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color){
		if(!(SkinSwapperConfig.modButton == SkinSwapperConfig.ModButton.CENTER)){
			drawCenteredText(matrices,textRenderer,text,centerX,y,color);
		}
	}
}