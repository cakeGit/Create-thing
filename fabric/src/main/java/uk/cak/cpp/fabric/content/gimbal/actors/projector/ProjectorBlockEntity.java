package uk.cak.cpp.fabric.content.gimbal.actors.projector;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import uk.cak.cpp.fabric.CreatePlusPlusClient;
import uk.cak.cpp.fabric.content.gimbal.actors.foundation.GimbalActorBlockEntity;
import uk.cak.cpp.fabric.system.projector.ProjectorRenderSystem;
import uk.cak.cpp.fabric.system.projector.ProjectorRenderer;

import java.util.List;
import java.util.UUID;

public class ProjectorBlockEntity extends GimbalActorBlockEntity {
    
    public ProjectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    
    UUID projectorUUID = UUID.randomUUID();
    
    @Override
    public void tick() {
        super.tick();
        ProjectorRenderSystem system = CreatePlusPlusClient.PROJECTOR_RENDER;
        if (level != null && level.isClientSide && CreatePlusPlusClient.PROJECTOR_RENDER.getLevel() == level) {
            system.getOrCreateThen(projectorUUID, this::configure);
        }
    }
    
    @Override
    public void remove() {
        super.remove();
        
        ProjectorRenderSystem system = CreatePlusPlusClient.PROJECTOR_RENDER;
        system.invalidate(projectorUUID);
    }
    
    protected void configure(ProjectorRenderer renderer) {
        Vector3d pos = new Vector3d(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5);
        renderer.setOrigin(pos);
        
        Quaternionf rotation = new Quaternionf().identity();
        rotation = rotation.rotateY(-(float) Math.toRadians(getRotationOfDirection(getBlockState().getValue(DirectionalBlock.FACING))));
        
        if (horizontalGimbal != null) {
            if (horizontalGimbal.getAxis() == Direction.Axis.X) {
                rotation = rotation.rotateX(-(float) Math.toRadians(horizontalGimbal.getAngle()));
            } else if (horizontalGimbal.getAxis() == Direction.Axis.Z) {
                rotation = rotation.rotateZ(-(float) Math.toRadians(horizontalGimbal.getAngle()));
            }
        }
        
        if (verticalGimbal != null) {
            rotation = rotation.rotateY((float) Math.toRadians(verticalGimbal.getAngle()));
        }
        
        renderer.setDirection(rotation);
        renderer.setColor(new Vector3f(0.8f, 0.2f, 0.1f));
//        int angleAnimTicks = AnimationTickHolder.getTicks();
//        angleAnimTicks = angleAnimTicks % 20;
//        renderer.setAngle(1f / angleAnimTicks);
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
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    
    }
    
    @Override
    public boolean canHaveFluidInput() {
        return false;
    }
}
