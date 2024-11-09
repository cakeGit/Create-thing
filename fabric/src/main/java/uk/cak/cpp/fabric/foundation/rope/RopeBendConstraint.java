package uk.cak.cpp.fabric.foundation.rope;

import net.minecraft.world.phys.Vec3;

public class RopeBendConstraint implements RopeConstraint {
    
    RopeNode from;
    RopeNode middle;
    RopeNode to;
    
    float width;
    
    public RopeBendConstraint(RopeNode from, RopeNode middle, RopeNode to, float width) {
        this.from = from;
        this.middle = middle;
        this.to = to;
        this.width = width;
    }
    
    @Override
    public void iterateConstraint() {
        
        Vec3 fromDifference = from.pos.subtract(middle.pos).scale(-1);
        Vec3 newTo = to.pos.subtract(middle.pos).normalize().lerp(fromDifference.normalize(), 0.4);
        newTo = newTo.add(middle.pos);
        to.addNextPosition(middle.pos.add(newTo.subtract(middle.pos).normalize().scale(width / 2f)));

        Vec3 toDifference = to.pos.subtract(middle.pos).scale(-1);
        Vec3 newFrom = from.pos.subtract(middle.pos).normalize().lerp(toDifference.normalize(), 0.4);
        newFrom = newFrom.add(middle.pos);
        from.addNextPosition(middle.pos.add(newFrom.subtract(middle.pos).normalize().scale(width / 2f)));
        
    }
    
}
