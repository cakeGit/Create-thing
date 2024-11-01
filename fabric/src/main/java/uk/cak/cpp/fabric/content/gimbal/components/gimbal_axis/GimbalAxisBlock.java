package uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;
import uk.cak.cpp.fabric.registry.CppBlockEntities;

public class GimbalAxisBlock extends DirectionalBlock implements IBE<GimbalAxisBlockEntity> {
    
    public GimbalAxisBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DirectionalBlock.FACING);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return super.getStateForPlacement(placeContext).setValue(DirectionalBlock.FACING, placeContext.getClickedFace());
    }
    
    @Override
    public Class<GimbalAxisBlockEntity> getBlockEntityClass() {
        return GimbalAxisBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends GimbalAxisBlockEntity> getBlockEntityType() {
        return CppBlockEntities.GIMBAL_AXIS.get();
    }
    
}
