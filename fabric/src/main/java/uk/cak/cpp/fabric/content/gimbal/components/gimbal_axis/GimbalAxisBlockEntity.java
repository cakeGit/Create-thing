package uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uk.cak.cpp.fabric.content.gimbal.actors.foundation.GimbalActorBlockEntity;

public class GimbalAxisBlockEntity extends KineticBlockEntity {
    
    public GimbalAxisBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    boolean isPrimary;
    boolean attached;
    
    Direction.Axis thisAxis;
    GimbalActorBlockEntity attachedTo;
    
    float axisRotation = 0;
    float lastAxisRotation = 0;
    
    @Override
    public void tick() {
        super.tick();
        thisAxis = getBlockState().getValue(DirectionalKineticBlock.FACING).getAxis();
        lastAxisRotation = axisRotation;
        axisRotation = (axisRotation + getDegreesPerTick(getSpeed()));
        if (!attached) {
            tryAttach();
        }
    }
    
    private float getDegreesPerTick(float speed) {
        float tickPerMin = 20 * 60;
        int degreesPerRev = 360;
        return degreesPerRev * speed / tickPerMin;
    }
    
    //TODO change to a on place check instead of lazytick
    public void tryAttach() {
        if (level == null) return;
        BlockPos checkPos = getBlockPos().relative(getBlockState().getValue(DirectionalKineticBlock.FACING).getOpposite());
        BlockEntity candidate = level.getBlockEntity(checkPos);
        if (candidate instanceof GimbalActorBlockEntity gabe) {
            if (gabe.attachToGimbal(this)) {
                attachedTo = gabe;
                attached = true;
            }
        }
    }
    
    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        isPrimary = tag.getBoolean("isPrimary");
    }
    
    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putBoolean("isPrimary", true);
    }
    
    public double getAngle(float pt) {
        return lastAxisRotation * (1 - pt) + axisRotation * pt;
    }
    
    public float getAngle() {
        return axisRotation;
    }
    
    public Direction getDirection() {
        return getBlockState().getValue(DirectionalKineticBlock.FACING);
    }
    
    @Override
    public void remove() {
        super.remove();
        if (attachedTo != null)
            attachedTo.invalidateAttachedGimbal(this);
    }
    
    public Direction.Axis getAxis() {
        return getDirection().getAxis();
    }
    
    public boolean isHorizontal() {
        return getAxis() != Direction.Axis.Y;
    }
    
}
