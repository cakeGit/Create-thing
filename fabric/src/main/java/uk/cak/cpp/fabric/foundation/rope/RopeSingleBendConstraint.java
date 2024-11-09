package uk.cak.cpp.fabric.foundation.rope;

import net.minecraft.world.phys.Vec3;

public class RopeSingleBendConstraint implements RopeConstraint {
    
    Vec3 fromPos;
    RopeNode middle;
    RopeNode to;
    
    float width;
    
    public RopeSingleBendConstraint(Vec3 fromPos, RopeNode middle, RopeNode to, float width) {
        this.fromPos = fromPos;
        this.middle = middle;
        this.to = to;
        this.width = width;
    }
    
    @Override
    public void iterateConstraint() {
        
        Vec3 fromDifference = fromPos.subtract(middle.pos).scale(-1);
        Vec3 newTo = to.pos.subtract(middle.pos).normalize().lerp(fromDifference.normalize(), 1);
        newTo = newTo.add(middle.pos);
        to.addNextPosition(middle.pos.add(newTo.subtract(middle.pos).normalize().scale(width / 2f)));

    }
    
}
