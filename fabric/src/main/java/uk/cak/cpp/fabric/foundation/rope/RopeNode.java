package uk.cak.cpp.fabric.foundation.rope;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class RopeNode {
    
    Vec3 prevPos;
    Vec3 pos;
    
    List<Vec3> nextPositions = new ArrayList<>();
    
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
            velocity = velocity.scale(0.9);
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
    
    public List<Vec3> getNextPositions() {
        return nextPositions;
    }
    
    public void setNextPositions(List<Vec3> nextPositions) {
        this.nextPositions = nextPositions;
    }
    
    public void addNextPosition(Vec3 nextPosition) {
        this.nextPositions.add(nextPosition);
    }
    public void applyNextPositions() {
        if (nextPositions.isEmpty() || fixed) return;
        Vec3 current = pos;
        
        int count = 1;
        for (Vec3 pos : nextPositions) {
            current = current.add(pos);
            count ++;
        }
        this.pos = current.scale(1f / count);
        setNextPositions(new ArrayList<>());
    }
    
    
}
