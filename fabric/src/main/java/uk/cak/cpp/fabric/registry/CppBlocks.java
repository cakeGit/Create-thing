package uk.cak.cpp.fabric.registry;

import com.tterrag.registrate.util.entry.BlockEntry;
import uk.cak.cpp.fabric.content.gimbal.actors.projector.ProjectorBlock;

import static uk.cak.cpp.fabric.CreatePlusPlus.REGISTRATE;

public class CppBlocks {
    
    public static final BlockEntry<ProjectorBlock> PROJECTOR = REGISTRATE
        .block("projector", ProjectorBlock::new)
        .simpleItem()
        .register();
    
    public static void register() {}
    
}
