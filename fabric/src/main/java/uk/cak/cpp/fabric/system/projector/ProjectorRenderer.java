package uk.cak.cpp.fabric.system.projector;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11C.glDeleteTextures;

public class ProjectorRenderer {
    
    boolean invalid = false;
    
    ProjectorTexture textureInstance = new ProjectorTexture();
    Vector3f color;
    Vector3d origin;
    Matrix4f viewTransform = new Matrix4f().identity().setPerspective((float) (Math.PI / 2f), 1f, 0.1F, 64f);
    Quaternionf direction;
    
    public ProjectorRenderer(Vector3f color, Vector3d origin, Quaternionf direction) {
        this.color = color;
        this.origin = origin;
        this.direction = direction;
    }
    
    public ProjectorRenderer() {
        this(new Vector3f(1f, 0f, 1f), new Vector3d(0, 0, 0), new Quaternionf());
    }
    
    public void free() {
        invalid = true;
        glDeleteTextures(textureInstance.getId());
    }
    
    public ProjectorTexture getTextureInstance() {
        return textureInstance;
    }
    
    public Vector3d getOrigin() {
        return origin;
    }
    
    public Matrix4f getViewTransform() {
        return viewTransform;
    }
    
    public Quaternionf getDirection() {
        return direction;
    }
    
    public void setColor(Vector3f color) {
        this.color = color;
    }
    
    public void setOrigin(Vector3d origin) {
        this.origin = origin;
    }
    
    public void setDirection(Quaternionf direction) {
        this.direction = direction;
    }
    
}
