package uk.cak.cpp.fabric.content.gimbal.components.mounts.fluid;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import uk.cak.cpp.fabric.registry.CppBlockEntities;

public class GimbalFluidMountBlock extends DirectionalBlock implements IBE<GimbalFluidMountBlockEntity> {
    
    public GimbalFluidMountBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DirectionalBlock.FACING);
    }
    
    @Override
    public Class<GimbalFluidMountBlockEntity> getBlockEntityClass() {
        return GimbalFluidMountBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends GimbalFluidMountBlockEntity> getBlockEntityType() {
        return CppBlockEntities.GIMBAL_FLUID_MOUNT.get();
    }
    
}
