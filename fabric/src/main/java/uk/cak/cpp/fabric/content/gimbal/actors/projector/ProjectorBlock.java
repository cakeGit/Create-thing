package uk.cak.cpp.fabric.content.gimbal.actors.projector;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;
import uk.cak.cpp.fabric.registry.CppBlockEntities;

public class ProjectorBlock extends DirectionalBlock implements IBE<ProjectorBlockEntity> {
    
    public ProjectorBlock(Properties properties) {
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
        return super.getStateForPlacement(context).setValue(DirectionalBlock.FACING, context.getNearestLookingDirection());
    }
    
    @Override
    public Class<ProjectorBlockEntity> getBlockEntityClass() {
        return ProjectorBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends ProjectorBlockEntity> getBlockEntityType() {
        return CppBlockEntities.PROJECTOR.get();
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
    
}