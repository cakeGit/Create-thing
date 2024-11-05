package uk.cak.cpp.fabric.content.gimbal.actors.foundation;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.outliner.LineOutline;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.phys.Vec3;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalActorConnection;
import uk.cak.cpp.fabric.foundation.rope.RopeConnection;
import uk.cak.cpp.fabric.foundation.rope.SimulatedRope;
import uk.cak.cpp.fabric.foundation.rope.SimulatedRopeTypes;
import uk.cak.cpp.fabric.registry.CppPartialModels;

public class GimbalActorBlockEntityRenderer<T extends GimbalActorBlockEntity> extends SmartBlockEntityRenderer<T> {
    
    public GimbalActorBlockEntityRenderer(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected void renderSafe(T blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);
        
        TransformStack stack = TransformStack.cast(ms);
        for (GimbalActorConnection connection : blockEntity.connections.values()) {
            if (connection.getMountBlockEntity() == null) continue;
            if (connection.getConnectionVisuals() == null) {
                SimulatedRope newRope = new SimulatedRope();
                SimulatedRopeTypes.buildRopeForConnectionType(blockEntity, connection.getMountBlockEntity(), newRope, connection.getMountBlockEntity().getMountType());
                connection.setConnectionVisuals(newRope);
            }
        }
        for (GimbalActorConnection mountConnection : blockEntity.connections.values()) {
            if (mountConnection.getConnectionVisuals() == null) continue;
            
            SimulatedRope rope = mountConnection.getConnectionVisuals();
            
            for (RopeConnection connection : rope.getConnections()) {
                Vec3 from = connection.getFrom().getPosition(partialTicks);
                Vec3 to = connection.getTo().getPosition(partialTicks);
                
                Vec3 diff = to.subtract(from);
                double yRot = Math.atan2(diff.x, diff.z);
                double zRot = -Math.atan2(diff.y, Math.sqrt(diff.x * diff.x + diff.z * diff.z));
                
                ms.pushPose();
                
                ms.translate(to.x, to.y, to.z);
                stack.rotateY(Math.toDegrees(yRot) + 90);
                stack.rotateZ(Math.toDegrees(zRot));
                CachedBufferer.partial(CppPartialModels.GIMBAL_FLUID_PIPE_SECTION, blockEntity.getBlockState())
                    .renderInto(ms, buffer.getBuffer(RenderType.solid()));
                
                ms.popPose();
                SuperRenderTypeBuffer superBuffer = SuperRenderTypeBuffer.getInstance();
                LineOutline section = new LineOutline()
                    .set(connection.getFrom().getPosition(partialTicks), connection.getTo().getPosition(partialTicks));
                section
                    .getParams()
                    .lineWidth(2 / 16f);
                section.render(ms, superBuffer, Vec3.ZERO, partialTicks);
            }
            
        }
        
        ms.pushPose();
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
