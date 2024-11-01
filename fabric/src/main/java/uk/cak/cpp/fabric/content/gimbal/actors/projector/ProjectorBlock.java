package uk.cak.cpp.fabric.content.gimbal.actors.projector;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import uk.cak.cpp.fabric.registry.CppBlockEntities;

public class ProjectorBlock extends HorizontalDirectionalBlock implements IBE<ProjectorBlockEntity> {
    
    public ProjectorBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public Class<ProjectorBlockEntity> getBlockEntityClass() {
        return ProjectorBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends ProjectorBlockEntity> getBlockEntityType() {
        return CppBlockEntities.PROJECTOR.get();
    }
    
}