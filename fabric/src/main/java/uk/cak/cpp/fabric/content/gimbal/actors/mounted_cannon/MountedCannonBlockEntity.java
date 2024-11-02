package uk.cak.cpp.fabric.content.gimbal.actors.mounted_cannon;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uk.cak.cpp.fabric.content.gimbal.actors.foundation.GimbalActorBlockEntity;

import java.util.List;

public class MountedCannonBlockEntity extends GimbalActorBlockEntity {
    
    public MountedCannonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    @Override
    public boolean canHaveFluidInput() {
        return true;
    }
    
    
}
