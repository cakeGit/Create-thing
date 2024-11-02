package uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class GimbalAxisRenderer extends KineticBlockEntityRenderer<GimbalAxisBlockEntity> {
    
    public GimbalAxisRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected SuperByteBuffer getRotatedModel(GimbalAxisBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), state.getValue(DirectionalKineticBlock.FACING));
    }
    
}
