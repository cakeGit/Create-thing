package uk.cak.cpp.fabric.registry;

import com.tterrag.registrate.util.entry.BlockEntry;
import uk.cak.cpp.fabric.content.gimbal.actors.mounted_cannon.MountedCannonBlock;
import uk.cak.cpp.fabric.content.gimbal.actors.projector.ProjectorBlock;
import uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis.GimbalAxisBlock;

import static uk.cak.cpp.fabric.CreatePlusPlus.REGISTRATE;

public class CppBlocks {
    
    public static final BlockEntry<ProjectorBlock> PROJECTOR = REGISTRATE
        .block("projector", ProjectorBlock::new)
        .simpleItem()
        .register();
    
    public static final BlockEntry<GimbalAxisBlock> GIMBAL_AXIS = REGISTRATE
        .block("gimbal_axis", GimbalAxisBlock::new)
        .properties(p -> p.noOcclusion())
        .blockstate((ctx, prov) -> {
            prov.directionalBlock(ctx.get(), prov.models().getExistingFile(prov.modLoc("block/gimbal/gimbal_base")));
        })
        .item()
        .model((ctx, prov) -> {
            prov.withExistingParent(ctx.getName(), prov.modLoc("block/gimbal/gimbal_base"));
        })
        .build()
        .register();
    public static final BlockEntry<MountedCannonBlock> MOUNTED_CANNON = REGISTRATE
        .block("mounted_cannon", MountedCannonBlock::new)
        .simpleItem()
        .register();;
    
    public static void register() {}
    
}
