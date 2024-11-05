package uk.cak.cpp.fabric.foundation.rope;

import uk.cak.cpp.fabric.content.gimbal.actors.foundation.GimbalActorBlockEntity;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalActorConnectionType;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalMountBlockEntity;

public class SimulatedRopeTypes {
    
    public static void buildRopeForConnectionType(GimbalActorBlockEntity gabe, GimbalMountBlockEntity mbe, SimulatedRope simulator, GimbalActorConnectionType connectionTypes) {
        switch (connectionTypes) {
            case FLUID -> {
                simulator.buildRope(10, gabe.getAttachmentPosForType(connectionTypes), mbe.getMountWorldPos(), 4 / 16f);
            }
            case ITEM -> {
            
            }
            case SIGNAL -> {
            
            }
        }
        
    }
    
}
