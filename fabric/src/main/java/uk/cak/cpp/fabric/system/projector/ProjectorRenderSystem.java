package uk.cak.cpp.fabric.system.projector;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import foundry.veil.api.client.render.VeilLevelPerspectiveRenderer;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.framebuffer.AdvancedFbo;
import foundry.veil.api.client.render.framebuffer.FramebufferManager;
import foundry.veil.api.client.render.framebuffer.VeilFramebuffers;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import uk.cak.cpp.fabric.CreatePlusPlus;

import java.util.*;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11C.GL_ALWAYS;

public class ProjectorRenderSystem {
    
    protected static final ResourceLocation PROJECTOR_RENDERED_DEPTH = CreatePlusPlus.asResource("projector_rendered_depth");
    protected static final ResourceLocation PROJECTOR_RESULTS = CreatePlusPlus.asResource("projector_results");
    protected static final ResourceLocation PROJECTOR_RESULTS_MIX = CreatePlusPlus.asResource("projector_results_mix");
    protected static final ResourceLocation PROJECTOR_FINAL_RESULTS = CreatePlusPlus.asResource("projector_final_results");
    
    HashMap<UUID, ProjectorInstance> projectorInstances = new HashMap<>();
    ClientLevel projectorLevel;
    
    protected int nextIndexToRender = 0;
    
    public void tickClient() {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel currentLevel = mc.level;
        if (projectorLevel == null || currentLevel != projectorLevel) {
            projectorLevel = currentLevel;
            invalidateAll();
        }
        
        Options options = mc.options;
        int renderDist = options.renderDistance().get() << 4;
        int renderDistSquared = renderDist * renderDist;
        
        ArrayList<UUID> toRemove = new ArrayList<>();
        
        for (Map.Entry<UUID, ProjectorInstance> entry : projectorInstances.entrySet()) {
            ProjectorInstance instance = entry.getValue();
            
            if (!mc.noRender && mc.cameraEntity != null) {
                if (instance.tickShouldFree(!instance.shouldRender(mc.cameraEntity.getEyePosition(), renderDistSquared))) {
                    instance.free();
                    toRemove.add(entry.getKey());
                }
            }
        }
        
        for (UUID uuid : toRemove)
            projectorInstances.remove(uuid);
    }
    
    public void onVeilRenderer() {
        VeilRenderSystem.renderer().getPostProcessingManager().add(CreatePlusPlus.asResource("projector"));
    }
    
    public void onPostProcessing(ResourceLocation name, PostPipeline pipeline, PostPipeline.Context context) {
        if (!name.equals(CreatePlusPlus.asResource("projector"))) return;
        
        Minecraft mc = Minecraft.getInstance();
        Options options = mc.options;
        int renderDist = options.renderDistance().get() << 4;
        int renderDistSquared = renderDist * renderDist;
        
        ShaderProgram shader = VeilRenderSystem.setShader(CreatePlusPlus.asResource("projector"));
        FramebufferManager framebufferManager = VeilRenderSystem.renderer().getFramebufferManager();
        AdvancedFbo in = framebufferManager.getFramebuffer(VeilFramebuffers.POST);
        AdvancedFbo out = framebufferManager.getFramebuffer(PROJECTOR_RESULTS);
        AdvancedFbo outMix = framebufferManager.getFramebuffer(PROJECTOR_RESULTS_MIX);
        
        boolean first = true;
        for (ProjectorInstance instance : projectorInstances.values()) {
            if (!instance.shouldRender(mc.cameraEntity.getEyePosition(), renderDistSquared))
                continue;
            
            shader.setup();
            shader.bind();
            shader.applyRenderSystem();
            shader.addRenderSystemTextures();
            setRenderDataIntoProjector(outMix, first, shader, instance);
            context.applySamplers(shader);
            shader.applyShaderSamplers(context, 0);
            shader.setFramebufferSamplers(in);
            out.bind(true);
            context.drawScreenQuad();
            AdvancedFbo.unbind();
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.depthFunc(GL_ALWAYS);
            RenderSystem.depthMask(false);
            
            out.resolveToAdvancedFbo(outMix);
            first = false;
        }
        
        ShaderProgram mixShader = VeilRenderSystem.setShader(CreatePlusPlus.asResource("projector_mix"));
        AdvancedFbo projector_results = framebufferManager.getFramebuffer(PROJECTOR_RESULTS);
        if (mixShader != null) {
            mixShader.addSampler("ProjectorLightSampler", projector_results.getColorTextureAttachment(0).getId());
        }
        ShaderProgram blurShader = VeilRenderSystem.setShader(CreatePlusPlus.asResource("projector_blur"));
        AdvancedFbo projector_final_results = framebufferManager.getFramebuffer(PROJECTOR_FINAL_RESULTS);
        if (blurShader != null) {
            blurShader.addSampler("ProjectorLightSampler", projector_final_results.getColorTextureAttachment(0).getId());
        }
    }
    
    private void setRenderDataIntoProjector(AdvancedFbo outMix, boolean first, ShaderProgram shader, ProjectorInstance instance) {
        shader.setInt("ShouldMix", first ? 0 : 1);
        
        shader.setVector("origin",
            (float) instance.renderInfo.origin.x,
            (float) instance.renderInfo.origin.y,
            (float) instance.renderInfo.origin.z
        );
        shader.setVector("direction", instance.renderInfo.direction.transform(new Vector3f(0, 0, -1)));
        Window window = Minecraft.getInstance().getWindow();
        shader.setFloat("BaseAspect", (float) window.getWidth() / window.getHeight());
        
        Matrix4f transform = new Matrix4f(instance.getRenderInfo().viewTransform).rotate(instance.getRenderInfo().direction);
        shader.setMatrix("DepthMat", transform);
        
        shader.setFloat("ProjectorPlaneNear", 0.1f);
        shader.setFloat("ProjectorPlaneFar", 64f);
        
        shader.setVector("projectorOneTexel", 2 / 1024f, 2 / 1024f);
        
        shader.addSampler("ProjectionDepthSampler", instance.getRenderInfo().getTextureInstance().getId());
        shader.addSampler("ProjectionResults", outMix.getColorTextureAttachment(0).getId());
    }
    
    public void preRenderLevel(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (VeilLevelPerspectiveRenderer.isRenderingPerspective() || mc.noRender || mc.cameraEntity == null) {
            return;
        }
        
        Options options = mc.options;
        int renderDist = options.renderDistance().get() << 4;
        int renderDistSquared = renderDist * renderDist;
        
        List<ProjectorInstance> instances = projectorInstances.values().stream().toList();
        if (instances.isEmpty()) return;
        
        int firstRenderedIndex = -1;
        for (int i = 0; i < 5; i++) {
            nextIndexToRender++;
            nextIndexToRender = nextIndexToRender % instances.size();
            
            if (firstRenderedIndex == -1) {
                firstRenderedIndex = nextIndexToRender;
            } else if (firstRenderedIndex == nextIndexToRender) {
                return;
            }
            
            ProjectorInstance toRender = instances.get(nextIndexToRender);
            if (toRender.shouldRender(mc.cameraEntity.getEyePosition(), renderDistSquared)) {
                renderDepthForInstance(toRender, partialTicks);
            }
        }
    }
    
    protected void renderDepthForInstance(ProjectorInstance instance, float partialTicks) {
        FramebufferManager framebufferManager = VeilRenderSystem.renderer().getFramebufferManager();
        AdvancedFbo out = framebufferManager.getFramebuffer(PROJECTOR_RENDERED_DEPTH);
        
        out.bindDraw(true);
        out.clear();
        AdvancedFbo.unbind();
        
        ProjectorRenderer renderer = instance.renderInfo;
        Matrix4f modelView = new Matrix4f();
        
//        glEnable(GL_CULL_FACE);
//        glCullFace(GL_FRONT);
        VeilLevelPerspectiveRenderer.render(
            out,
            modelView,
            renderer.getViewTransform(),
            renderer.origin,
            renderer.direction,
            64f,
            partialTicks
        );
//        glCullFace(GL_BACK);
        
        renderer.textureInstance.copyDepthFrom(out);
    }
    
    public void getOrCreateThen(UUID uuid, Consumer<ProjectorRenderer> then) {
        if (projectorInstances.containsKey(uuid)) {
            then.accept(projectorInstances.get(uuid).getRenderInfo());
        } else {
            ProjectorRenderer renderer = new ProjectorRenderer();
            then.accept(renderer);
            projectorInstances.put(uuid, new ProjectorInstance(renderer));
        }
    }
    
    protected void invalidateAll() {
        for (ProjectorInstance instance : projectorInstances.values()) {
            instance.free();
        }
        projectorInstances = new HashMap<>();
    }
    
    public void invalidate(UUID uuid) {
        ProjectorInstance instance = projectorInstances.remove(uuid);
        if (instance != null) instance.free();
    }
    
    public ClientLevel getLevel() {
        return projectorLevel;
    }
    
}
