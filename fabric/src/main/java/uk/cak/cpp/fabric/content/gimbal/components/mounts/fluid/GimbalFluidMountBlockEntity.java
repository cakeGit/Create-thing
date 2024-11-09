package uk.cak.cpp.fabric.content.gimbal.components.mounts.fluid;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalMountBlockEntity;

import java.util.List;

public class GimbalFluidMountBlockEntity extends GimbalMountBlockEntity {
    
    public GimbalFluidMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
}
