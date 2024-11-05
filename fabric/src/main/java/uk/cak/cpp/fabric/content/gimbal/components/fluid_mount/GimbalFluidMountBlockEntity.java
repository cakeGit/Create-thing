package uk.cak.cpp.fabric.content.gimbal.components.fluid_mount;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalMountBlockEntity;

import java.util.List;

public class GimbalFluidMountBlockEntity extends GimbalMountBlockEntity {
    
    public GimbalFluidMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    @Override
    public Vec3 getMountWorldPos() {
        Vec3 position = new Vec3(0.5, 0.5, 0.5);
        position = position.relative(getBlockState().getValue(DirectionalBlock.FACING), 0.5);
        return position;
    }
    
}
