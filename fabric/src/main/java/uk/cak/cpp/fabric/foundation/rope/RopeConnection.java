package uk.cak.cpp.fabric.foundation.rope;

import net.minecraft.world.phys.Vec3;

public class RopeConnection {
    
    RopeNode from;
    RopeNode to;
    
    float width;
    
    public RopeConnection(RopeNode from, RopeNode to, float width) {
        this.from = from;
        this.to = to;
        this.width = width;
    }
    
    public void iterateConstraint() {
        Vec3 diff = from.pos.subtract(to.pos);
        
        if (diff.lengthSqr() > width*width) {
            float overBoundsDistance = (float) (diff.length() - width) * 0.9f;
            
            if (!from.isFixed() && !to.isFixed()) {
                overBoundsDistance /= 2;
            }
            
            Vec3 resolution = diff.normalize().scale(overBoundsDistance);
            if (!from.fixed) {
                from.pos = from.pos.subtract(resolution);
            }
            if (!to.fixed) {
                to.pos = to.pos.add(resolution);
            }
        }
    }
    
    public RopeNode getFrom() {
        return from;
    }
    
    public RopeNode getTo() {
        return to;
    }
    
}
