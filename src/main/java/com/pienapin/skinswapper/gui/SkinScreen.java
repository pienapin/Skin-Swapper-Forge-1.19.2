package com.pienapin.skinswapper.gui;

import com.pienapin.skinswapper.changeskin.SkinChange;
import com.pienapin.skinswapper.config.SkinSwapperConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.io.File;

public class SkinScreen extends Screen {
    final File folder;
    private final Screen parent;
    private static SkinListWidget skinList;
    public String error;

    public SkinScreen(Screen scr) {
        super(Text.translatable("Change Skin"));
        this.parent = scr;
        this.folder = new File("skins");
    }

    @Override
    protected void init() {

        //lists skins
        skinList = new SkinListWidget(this.client, this.width/2, this.height, 36, this.height - 52, 36, this);
        skinList.setLeftPos(0);
        this.addSelectableChild(skinList);

        //adds skins from "skins" folder
        addSkins(folder);

        //back button
        this.addDrawableChild(new ButtonWidget((this.width - (this.width/4)) + 2, this.height - 24, 100, 20, Text.translatable("Back"), button -> {
            MinecraftClient.getInstance().setScreen(parent);
        }));

        //change skin button
        this.addDrawableChild(new ButtonWidget(((this.width - (this.width/4)) - 100 - 2), this.height - 24, 100, 20, Text.translatable("Change Skin"), button -> {
            this.client.setScreen(new ConfirmScreen(this::changeSkin, Text.literal(I18n.translate("Are you sure")), Text.literal(I18n.translate("change skin to") + " '" + skinList.getFocused().fname + "'"), Text.translatable("Yes"), Text.translatable("Cancel")));
        }) {
            @Override //sets button to be active only if a skin is selected
            public void render(MatrixStack matrices, int var1, int var2, float var3) {
                visible = true;
                active = (skinList.getFocused() != null);
                super.render(matrices, var1, var2, var3);
            }
        });

        //open skin folder
        this.addDrawableChild(new ButtonWidget(this.width/4 + 2, this.height - 24, 100, 20, Text.translatable("Open Folder"), button -> Util.getOperatingSystem().open(new File("skins"))));

        //delete skin button
        this.addDrawableChild(new ButtonWidget(this.width/4 - 100 - 2, this.height - 24, 100, 20, Text.translatable("Delete Skin"), button ->
        {
            this.client.setScreen(new ConfirmScreen(this::removeEntry, Text.literal(I18n.translate("Are you sure to remove")), Text.literal("'" + skinList.getFocused().fname + "' " + I18n.translate("skin?")), Text.translatable("selectWorld.delete"), Text.translatable("gui.cancel")));
        }) {
            @Override //sets button to be active only if a skin is selected
            public void render(MatrixStack matrices, int var1, int var2, float var3) {
                visible = true;
                active = (skinList.getFocused() != null);
                super.render(matrices, var1, var2, var3);
            }
        });

        //classic select button
        this.addDrawableChild(new ButtonWidget(this.width/4 + 2, this.height - 48, 100, 20, Text.translatable("Classic"), button -> skinList.getFocused().toggleSkinType()) {
            @Override //sets button to be active only if a skin is selected and slim
            public void render(MatrixStack matrices, int var1, int var2, float var3) {
                visible = true;
                active = (skinList.getFocused() != null && skinList.getFocused().skinType.equals(SkinType.SLIM));
                super.render(matrices, var1, var2, var3);
            }
        });

        //slim select button
        this.addDrawableChild(new ButtonWidget(this.width/4 - 100 - 2, this.height - 48, 100, 20, Text.translatable("Slim"), button -> skinList.getFocused().toggleSkinType()) {
            @Override //sets button to be active only if a skin is selected and classic
            public void render(MatrixStack matrices, int var1, int var2, float var3) {
                visible = true;
                active = (skinList.getFocused() != null && skinList.getFocused().skinType.equals(SkinType.CLASSIC));
                super.render(matrices, var1, var2, var3);
            }
        });

        //download button
        if (SkinSwapperConfig.showDownloadScreen) {
            this.addDrawableChild(new ButtonWidget(this.width/4 + 2, 8, 100, 20, Text.translatable("Download Skin"), button -> MinecraftClient.getInstance().setScreen(new DownloadScreen(this))));
        }

        //refresh button
        this.addDrawableChild(new ButtonWidget(this.width/4 - 100 - 2, 8, 100, 20, Text.translatable("selectServer.refresh"), button -> {
            addSkins(folder);
            skinList.setFocused(null);
            error = "";
        }));
    }

    public void render(MatrixStack matrices,  int mouseX, int mouseY, float delta)
    {
        this.renderBackground(matrices);
        skinList.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);

        //draws skin preview
        if(skinList.getFocused() != null) {
            //draws 2d skin
            if (SkinSwapperConfig.displayType == SkinSwapperConfig.DisplayType.LEGACY) {
                try {
                    SkinUtils.drawSkin(this.width - skinList.getRowWidth() / 2 - 16, this.height / 2 - 64, matrices, skinList.getFocused());
                } catch (Exception ignored) {}
            }
            //draws 3d skin
            else {
                if (skinList.getFocused() != null) {
                    try {
                        SkinUtils.drawPlayer(this.width - (this.width / 4), this.height - 36, 92, mouseX, mouseY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //draws error messages
        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        drawCenteredText(matrices, font, error, this.width - this.width/4, 12, 0xFFFFFF);
    }

    public void addSkins(final File folder) {
        NativeImage nativeImage;
        boolean oldSkin;

        skinList.children().clear();
        for (final File fileEntry : folder.listFiles()) {
            nativeImage = SkinUtils.toNativeImage(fileEntry);

            if(nativeImage != null){

                //checks dimensions of file
                if(nativeImage.getWidth() == 64 && nativeImage.getHeight() == 32){
                    oldSkin = true;
                } else if(nativeImage.getWidth() == 64 && nativeImage.getHeight() == 64){
                    oldSkin = false;
                } else {
                    continue;
                }
                skinList.children().add(new SkinEntry(fileEntry.getName().substring(0, fileEntry.getName().length()-4),fileEntry,skinList,nativeImage,oldSkin));
            }
        }
    }

    private void removeEntry(boolean confirmedAction) {
        if(confirmedAction) {
            skinList.getFocused().deleteSkin();
            skinList.children().remove(skinList.getFocused());
            skinList.setSelected(null);
        }
        this.client.setScreen(this);
    }

    public static SkinEntry getSelected(){
        return skinList.getFocused();
    }

    private void changeSkin(boolean confirmedAction) {
        if(confirmedAction) {
            if(SkinChange.changeSkin(skinList.getFocused().skin_file, skinList.getFocused().skinType, this)) {
                skinList.setSelected(null);
                MinecraftClient.getInstance().setScreen(parent);
            }
            else {
                this.client.setScreen(this);
            }
        }
        else {
            this.client.setScreen(this);
        }
    }
}