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
    Quaternionf direction;
    float angle;
    
    Vector3d lastRenderedOrigin;
    Quaternionf lastRenderedDirection;
    Matrix4f lastViewTransform;
    
    public ProjectorRenderer(Vector3f color, Vector3d origin, Quaternionf direction, float angle) {
        this.color = color;
        this.origin = origin;
        this.direction = direction;
        this.angle = angle;
        lastRenderedOrigin = origin;
        lastRenderedDirection = direction;
        lastViewTransform = new Matrix4f().identity().setPerspective((float) (Math.PI * angle / 2), 1f, 0.1F, 64f);
    }
    
    public ProjectorRenderer() {
        this(new Vector3f(1f, 0f, 1f), new Vector3d(0, 0, 0), new Quaternionf(), 1f);
    }
    
    public void free() {
        invalid = true;
        glDeleteTextures(textureInstance.getId());
    }
    
    public ProjectorTexture getTextureInstance() {
        return textureInstance;
    }
    
    public Matrix4f buildViewTransform() {
        return new Matrix4f().identity().setPerspective((float) (Math.PI * angle / 2), 1f, 0.1F, 64f);
    }
    
    public Vector3d getOrigin() {
        return origin;
    }
    
    public void setColor(Vector3f color) {
        this.color = color;
    }
    
    public void setOrigin(Vector3d origin) {
        this.origin = origin;
    }
    
    public void setPropertiesAsLastRendered() {
        this.lastRenderedOrigin = origin;
        this.lastRenderedDirection = direction;
        this.lastViewTransform = buildViewTransform();
    }
    
    public void setDirection(Quaternionf direction) {
        this.direction = direction;
    }
    
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    public Vector3f getColor() {
        return color;
    }
    
}
