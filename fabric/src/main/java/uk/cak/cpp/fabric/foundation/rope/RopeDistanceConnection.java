package uk.cak.cpp.fabric.foundation.rope;

public class RopeDistanceConnection implements RopeConstraint {
    
    RopeNode from;
    RopeNode to;
    
    float width;
    
    public RopeDistanceConnection(RopeNode from, RopeNode to, float width) {
        this.from = from;
        this.to = to;
        this.width = width;
    }
    
    @Override
    public void iterateConstraint() {
//        Vec3 diff = from.pos.subtract(to.pos);
//
//        if (diff.lengthSqr() > width*width) {
//            float overBoundsDistance = (float) (diff.length() - width) * 0.9f;
//
//            if (!from.isFixed() && !to.isFixed()) {
//                overBoundsDistance /= 2;
//            }
//
//            Vec3 resolution = diff.normalize().scale(overBoundsDistance);
//            if (!from.fixed) {
//                from.addNextPosition(from.pos.subtract(resolution));
//            }
//            if (!to.fixed) {
//                to.addNextPosition(to.pos.add(resolution));
//            }
//        }
    }
    
    public RopeNode getFrom() {
        return from;
    }
    
    public RopeNode getTo() {
        return to;
    }
    
}
