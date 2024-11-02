package uk.cak.cpp.fabric.content.gimbal.actors.foundation;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.phys.Vec3;
import uk.cak.cpp.fabric.registry.CppPartialModels;

public class GimbalActorBlockEntityRenderer<T extends GimbalActorBlockEntity> extends SmartBlockEntityRenderer<T> {
    
    public GimbalActorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected void renderSafe(T blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);
        
        ms.pushPose();
        TransformStack stack = TransformStack.cast(ms);
        if (blockEntity.horizontalGimbal != null && blockEntity.verticalGimbal != null) {
            BlockPos secondaryRelative = blockEntity.horizontalGimbal.getBlockPos().subtract(blockEntity.getBlockPos())
                .offset(blockEntity.verticalGimbal.getBlockPos().subtract(blockEntity.getBlockPos()));
            stack.translate(Vec3.atLowerCornerOf(secondaryRelative));
            stack.centre();
            stack.rotateY(getRotationOfDirection(blockEntity.horizontalGimbal.getDirection()));
            stack.unCentre();
            CachedBufferer.partial(CppPartialModels.GIMBAL_SECONDARY_ARM, blockEntity.getBlockState())
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
        ms.popPose();
        
        ms.pushPose();
        if (blockEntity.verticalGimbal != null) {
            stack.centre();
            stack.rotate(blockEntity.verticalGimbal.getDirection(), blockEntity.verticalGimbal != null ? (float) -Math.toRadians(blockEntity.verticalGimbal.getAngle(partialTicks)) : 0);
            stack.unCentre();
        }
        ms.pushPose();
        if (blockEntity.horizontalGimbal != null) {
            stack.centre();
            stack.rotate(blockEntity.horizontalGimbal.getDirection(), blockEntity.horizontalGimbal != null ? (float) -Math.toRadians(blockEntity.horizontalGimbal.getAngle(partialTicks)) : 0);
            stack.unCentre();
        }
        CachedBufferer.block(blockEntity.getBlockState())
            .renderInto(ms, buffer.getBuffer(RenderType.solid()));
        ms.popPose();
        if (blockEntity.verticalGimbal != null && blockEntity.horizontalGimbal != null) {
            ms.pushPose();
            stack.centre();
            stack.rotateY(getRotationOfDirection(blockEntity.getBlockState().getValue(DirectionalBlock.FACING)) + 90);
            stack.unCentre();
            CachedBufferer.partial(CppPartialModels.GIMBAL_PRIMARY_ARM, blockEntity.getBlockState())
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));
            ms.popPose();
        }
        ms.popPose();
    }
    
    private double getRotationOfDirection(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return 0;
            }
            case WEST -> {
                return 90;
            }
            case SOUTH -> {
                return 180;
            }
            case EAST -> {
                return 270;
            }
        }
        return -1;
    }
    
}
