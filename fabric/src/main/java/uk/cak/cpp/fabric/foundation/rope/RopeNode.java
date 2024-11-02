package uk.cak.cpp.fabric.foundation.rope;

import net.minecraft.world.phys.Vec3;

public class RopeNode {
    
    Vec3 prevPos;
    Vec3 pos;
    
    boolean fixed = false;
    
    public RopeNode(Vec3 pos) {
        this.pos = pos;
        this.prevPos = pos;
    }
    
    public boolean isFixed() {
        return fixed;
    }
    
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
    
    public void simulatePhysics() {
        Vec3 velocity = Vec3.ZERO;
        if (!fixed) {
            velocity = pos.subtract(prevPos);
            
            velocity = velocity.add(0, -0.1, 0);
            velocity = velocity.scale(0.75);
        }
        
        prevPos = pos;
        pos = pos.add(velocity);
    }
    
    public Vec3 getPosition(float partialTicks) {
        return prevPos.lerp(pos, partialTicks);
    }
    
    public void setPosition(Vec3 position) {
        this.pos = position;
    }
    
}
