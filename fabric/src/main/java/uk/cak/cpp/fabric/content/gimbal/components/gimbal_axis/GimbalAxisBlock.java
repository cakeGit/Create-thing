package uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import uk.cak.cpp.fabric.registry.CppBlockEntities;
import uk.cak.cpp.fabric.registry.CppBlocks;
import uk.cak.cpp.fabric.registry.CppParticleEmitters;

public class GimbalAxisBlock extends DirectionalKineticBlock implements IBE<GimbalAxisBlockEntity> {
    
    public GimbalAxisBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return super.getStateForPlacement(placeContext).setValue(DirectionalKineticBlock.FACING, placeContext.getClickedFace());
    }
    
    @Override
    public Class<GimbalAxisBlockEntity> getBlockEntityClass() {
        return GimbalAxisBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends GimbalAxisBlockEntity> getBlockEntityType() {
        return CppBlockEntities.GIMBAL_AXIS.get();
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(DirectionalKineticBlock.FACING).getAxis();
    }
    
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(DirectionalKineticBlock.FACING);
    }
    
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        IBE.onRemove(pState, pLevel, pPos, pNewState);
    }
    
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        BlockEntity be = level.getBlockEntity(pos);
        
        if (!(be instanceof GimbalAxisBlockEntity gabe)) return;
        
        if (level.getBlockState(neighborPos).is(AllBlocks.FLUID_PIPE.get()) && gabe.attached && gabe.attachedTo.acceptsIncomingFluidMount()) {
            Direction mountDirection = Direction.UP;
            for (Direction direction : Iterate.directions) {
                BlockPos checkPos = neighborPos.relative(direction);
                if (level.getBlockState(checkPos).is(AllBlocks.FLUID_PIPE.get())) {
                    mountDirection = direction;
                    break;
                }
            }
            level.setBlockAndUpdate(
                neighborPos,
                CppBlocks.GIMBAL_FLUID_MOUNT
                    .getDefaultState()
                    .setValue(DirectionalBlock.FACING, mountDirection.getOpposite())
            );
            CppParticleEmitters.PIPE_MOUNTED.emitToClients((ServerLevel) level, Vec3.atCenterOf(neighborPos).add(0, 0.4, 0), 4);
            gabe.attachedTo.setIncomingFluidMount(neighborPos);
        }
    }
    
}
