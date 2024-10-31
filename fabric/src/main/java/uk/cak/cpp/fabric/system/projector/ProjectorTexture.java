package uk.cak.cpp.fabric.system.projector;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import foundry.veil.api.client.render.framebuffer.AdvancedFbo;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30C.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;

public class ProjectorTexture extends AbstractTexture {
    
    private int width;
    private int height;
    
    public ProjectorTexture() {
        this.setFilter(false, true);
        this.width = -1;
        this.height = -1;
    }
    
    @Override
    public void load(@NotNull ResourceManager resourceManager) {
    
    }
    
    public void copyDepthFrom(AdvancedFbo fbo) {
        int id = this.getId();
        int width = fbo.getWidth();
        int height = fbo.getHeight();
        
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            TextureUtil.prepareImage(NativeImage.InternalGlFormat.RED, id, 1, width, height);
        }
        
        RenderSystem.bindTexture(id);
        fbo.bindRead();
        glCopyTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, 0, 0, width, height, 0);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        RenderSystem.bindTexture(0);
    }
    
}
