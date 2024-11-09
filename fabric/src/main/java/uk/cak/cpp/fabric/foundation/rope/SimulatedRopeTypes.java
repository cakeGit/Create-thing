package uk.cak.cpp.fabric.foundation.rope;

import net.minecraft.world.phys.Vec3;
import uk.cak.cpp.fabric.content.gimbal.actors.foundation.GimbalActorBlockEntity;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalActorConnectionType;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalMountBlockEntity;

public class SimulatedRopeTypes {
    
    public static void buildRopeForConnectionType(GimbalActorBlockEntity gabe, GimbalMountBlockEntity mbe, SimulatedRope simulator, GimbalActorConnectionType connectionTypes) {
        Vec3 offset = Vec3.atLowerCornerOf(mbe.getBlockPos().subtract(gabe.getBlockPos()));
        switch (connectionTypes) {
            case FLUID -> {
                simulator.buildRope(15, mbe.getMountWorldPos().add(offset), mbe.getMountPivotPos().add(offset), mbe.getMountWorldPos(), new Vec3(0.5, 0.5, 0.5), 4 / 16f);
            }
            case ITEM -> {
            
            }
            case SIGNAL -> {
            
            }
        }
        
    }
    
}
