package uk.cak.cpp.fabric.mixin.client;

import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.cak.cpp.fabric.CreatePlusPlusClient;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    
    @Shadow public boolean noRender;
    
    @Shadow @Nullable public ClientLevel level;
    
    @Inject(method = "runTick", at = @At("HEAD"))
    public void inject_render(boolean bl, CallbackInfo ci) {
        if (noRender || !(bl && level != null))
            return;
        CreatePlusPlusClient.PROJECTOR_RENDER.preRenderLevel(AnimationTickHolder.getPartialTicks());
    }

}
