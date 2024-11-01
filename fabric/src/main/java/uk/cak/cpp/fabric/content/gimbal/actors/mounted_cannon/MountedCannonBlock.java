package uk.cak.cpp.fabric.content.gimbal.actors.mounted_cannon;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;
import uk.cak.cpp.fabric.content.gimbal.actors.foundation.IGimbalActor;
import uk.cak.cpp.fabric.registry.CppBlockEntities;

public class MountedCannonBlock extends DirectionalBlock implements IGimbalActor, IBE<MountedCannonBlockEntity> {
    
    public MountedCannonBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DirectionalBlock.FACING);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(DirectionalBlock.FACING, context.getClickedFace());
    }
    
    @Override
    public Class<MountedCannonBlockEntity> getBlockEntityClass() {
        return MountedCannonBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends MountedCannonBlockEntity> getBlockEntityType() {
        return CppBlockEntities.MOUNTED_CANNON.get();
    }
    
}
