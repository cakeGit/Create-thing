package uk.cak.cpp.fabric.registry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import uk.cak.cpp.fabric.content.gimbal.actors.projector.ProjectorBlockEntity;

import static uk.cak.cpp.fabric.CreatePlusPlus.REGISTRATE;

public class CppBlockEntities {
    
    public static final BlockEntityEntry<ProjectorBlockEntity> PROJECTOR = REGISTRATE
        .blockEntity("projector", ProjectorBlockEntity::new)
        .validBlocks(CppBlocks.PROJECTOR)
        .register();
    
    public static void register() {}
    
}
