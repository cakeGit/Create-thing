package uk.cak.cpp.fabric.registry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import uk.cak.cpp.fabric.content.gimbal.actors.mounted_cannon.MountedCannonBlockEntity;
import uk.cak.cpp.fabric.content.gimbal.actors.projector.ProjectorBlockEntity;
import uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis.GimbalAxisBlockEntity;

import static uk.cak.cpp.fabric.CreatePlusPlus.REGISTRATE;

public class CppBlockEntities {
    
    public static final BlockEntityEntry<ProjectorBlockEntity> PROJECTOR = REGISTRATE
        .blockEntity("projector", ProjectorBlockEntity::new)
        .validBlocks(CppBlocks.PROJECTOR)
        .register();
    
    public static final BlockEntityEntry<GimbalAxisBlockEntity> GIMBAL_AXIS = REGISTRATE
        .blockEntity("gimbal_axis", GimbalAxisBlockEntity::new)
        .validBlocks(CppBlocks.GIMBAL_AXIS)
        .register();
    public static final BlockEntityEntry<MountedCannonBlockEntity> MOUNTED_CANNON = REGISTRATE
        .blockEntity("mounted_cannon", MountedCannonBlockEntity::new)
        .validBlocks(CppBlocks.MOUNTED_CANNON)
        .register();
    
    public static void register() {}
    
}
