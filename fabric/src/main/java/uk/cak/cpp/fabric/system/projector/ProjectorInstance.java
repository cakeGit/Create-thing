package uk.cak.cpp.fabric.system.projector;

import net.minecraft.world.phys.Vec3;

public class ProjectorInstance {
    
    int ticksToDie;
    
    ProjectorRenderer renderInfo;
    
    public ProjectorInstance(ProjectorRenderer renderer) {
        this.renderInfo = renderer;
    }
    
    public boolean shouldRender(Vec3 camPos, double renderDistSquared) {
        return renderInfo.origin.distanceSquared((float) camPos.x, (float) camPos.y, (float) camPos.z) < renderDistSquared;
    }
    
    public boolean tickShouldFree(boolean invalid) {
        if (invalid) {
            ticksToDie++;
            return ticksToDie > 100;
        } else {
            ticksToDie = 0;
            return false;
        }
    }
    
    public void free() {
        renderInfo.free();
    }
    
    public ProjectorRenderer getRenderInfo() {
        return renderInfo;
    }
    
}
