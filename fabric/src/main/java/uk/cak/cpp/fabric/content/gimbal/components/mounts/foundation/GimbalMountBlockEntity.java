package uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class GimbalMountBlockEntity extends SmartBlockEntity {
    
    public GimbalMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    public Vec3 getMountWorldPos() {
        Vec3 position = new Vec3(0.5, 0.5, 0.5);
        position = position.relative(getBlockState().getValue(DirectionalBlock.FACING).getOpposite(), 2/16f);
        return position;
    }
    
    public GimbalActorConnectionType getMountType() {
        return GimbalActorConnectionType.FLUID;
    }
    
    public Vec3 getMountPivotPos() {
        Vec3 position = new Vec3(0.5, 0.5, 0.5);
        position = position.relative(getBlockState().getValue(DirectionalBlock.FACING).getOpposite(), 4/16f);
        return position;
    }
    
}
