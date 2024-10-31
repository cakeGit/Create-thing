package uk.cak.cpp.fabric;

import foundry.veil.api.client.render.VeilRenderer;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.fabric.event.FabricVeilPostProcessingEvent;
import foundry.veil.fabric.event.FabricVeilRendererEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import uk.cak.cpp.fabric.system.projector.ProjectorRenderSystem;

public class CreatePlusPlusClient {
    
    public static final ProjectorRenderSystem PROJECTOR_RENDER = new ProjectorRenderSystem();
    
    public static void register() {
        FabricVeilPostProcessingEvent.PRE.register(CreatePlusPlusClient::onVeilPostProcessingRender);
        FabricVeilRendererEvent.EVENT.register(CreatePlusPlusClient::onClientSetup);
        ClientTickEvents.START_CLIENT_TICK.register(CreatePlusPlusClient::onClientTick);
    }
    
    public static void onClientTick(Minecraft minecraft) {
        PROJECTOR_RENDER.tickClient();
    }
    
    public static void onVeilPostProcessingRender(ResourceLocation name, PostPipeline pipeline, PostPipeline.Context context) {
        PROJECTOR_RENDER.onPostProcessing(name, pipeline, context);
    }
    
    public static void onClientSetup(VeilRenderer renderer) {
        PROJECTOR_RENDER.onVeilRenderer();
    }
    
}
