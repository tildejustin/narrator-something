package dev.tildejustin.narrator_backport.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NarratorManager.class)
public abstract class NarratorManagerMixin {
    @Unique
    private final Narrator narrator = Narrator.getNarrator();

    @WrapWithCondition(method = {"addToast", "narrate(ZLjava/lang/String;)V"}, at = @At(value = "INVOKE", target = "owner=/^com\\/mojang\\/text2speech\\/Narrator$/ /say/ desc=/^\\(Ljava\\/lang\\/String;Z\\)V$/"))
    private boolean redirectSay(@Coerce Object instance, String string, boolean interrupt) {
        return false;
    }

    @Inject(method = "narrate(ZLjava/lang/String;)V", at = @At(value = "INVOKE", target = "owner=/^com\\/mojang\\/text2speech\\/Narrator$/ /say/ desc=/^\\(Ljava\\/lang\\/String;Z\\)V$/"))
    private void test(boolean interrupt, String message, CallbackInfo ci) {
        narrator.say(message, interrupt, MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.VOICE) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER));
    }
}
