package uk.cak.cpp.fabric.content.gimbal.actors.foundation;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import uk.cak.cpp.fabric.content.gimbal.components.fluid_mount.GimbalFluidMountBlockEntity;
import uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis.GimbalAxisBlockEntity;
import uk.cak.cpp.fabric.foundation.rope.RopeNode;
import uk.cak.cpp.fabric.foundation.rope.SimulatedRope;

import javax.annotation.Nullable;
import java.util.List;

public abstract class GimbalActorBlockEntity extends SmartBlockEntity {
    
    boolean isOnGimbal = false;
    @Nullable
    protected GimbalAxisBlockEntity horizontalGimbal = null;
    @Nullable
    protected GimbalAxisBlockEntity verticalGimbal = null;
    
    boolean hasFluidConnection = false;
    @Nullable
    GimbalFluidMountBlockEntity fluidMount = null;
    BlockPos incomingFluidMount = null;
    BlockPos fluidMountPos = null;
    /**
     * Client / renderer only
     */
    @Nullable
    SimulatedRope fluidConnectionVisuals = null;
    
    public GimbalActorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (incomingFluidMount != null && !level.isClientSide) {
            BlockEntity be = level.getBlockEntity(incomingFluidMount);
            
            if (be instanceof GimbalFluidMountBlockEntity fluidMountBe) {
                fluidMount = fluidMountBe;
                hasFluidConnection = true;
                fluidMountPos = incomingFluidMount;
                sendData();
            }
            
            incomingFluidMount = null;
        }
        
        if (fluidMountPos != null && level.isClientSide) {
            BlockEntity be = level.getBlockEntity(fluidMountPos);
            
            if (be instanceof GimbalFluidMountBlockEntity fluidMountBe) {
                fluidMount = fluidMountBe;
                hasFluidConnection = true;
                sendData();
            }
        }
        
        if (fluidConnectionVisuals != null) {
            fluidConnectionVisuals.simulate();
            updateFluidRopeAttachmentPositions(fluidConnectionVisuals);
        }
    }
    
    protected void updateFluidRopeAttachmentPositions(SimulatedRope fluidConnectionVisuals) {
        RopeNode first = fluidConnectionVisuals.getNode(0);
        first.setPosition(getFluidAttachmentPos());
        
        RopeNode last = fluidConnectionVisuals.getLastNode();
        last.setPosition(getFluidMountPos());
    }
    
    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        if (fluidMountPos != null) {
            tag.putInt("fluidMountPosX", fluidMountPos.getX());
            tag.putInt("fluidMountPosY", fluidMountPos.getY());
            tag.putInt("fluidMountPosZ", fluidMountPos.getZ());
        }
    }
    
    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (tag.contains("fluidMountPosX")) {
            fluidMountPos = new BlockPos(
                tag.getInt("fluidMountPosX"),
                tag.getInt("fluidMountPosY"),
                tag.getInt("fluidMountPosZ")
            );
        }
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    public boolean attachToGimbal(GimbalAxisBlockEntity gabe) {
        isOnGimbal = true;
        if (gabe.isHorizontal()) {
            if (horizontalGimbal != null) return false;
            horizontalGimbal = gabe;
        } else {
            if (verticalGimbal != null) return false;
            verticalGimbal = gabe;
        }
        return true;
    }
    
    public void invalidateAttachedGimbal(GimbalAxisBlockEntity gabe) {
        if (gabe == horizontalGimbal) {
            horizontalGimbal = null;
        } else if (gabe == verticalGimbal) {
            verticalGimbal = null;
        }
        if (verticalGimbal == null && horizontalGimbal == null) {
            isOnGimbal = false;
        }
    }
    
    public void buildFluidRope(SimulatedRope fluidAttachmentSimulator) {
        fluidAttachmentSimulator.buildRope(10, getFluidAttachmentPos(), getFluidMountPos(), 2 / 16f);
    }
    
    private Vec3 getFluidAttachmentPos() {
        Vec3 pos = Vec3.atLowerCornerOf(getBlockState().getValue(DirectionalBlock.FACING).getNormal());
        pos = pos.scale(0.5);
        
        if (horizontalGimbal != null) {
            if (horizontalGimbal.getAxis() == Direction.Axis.X) {
                pos = pos.xRot(-(float) Math.toRadians(horizontalGimbal.getAngle()));
            } else if (horizontalGimbal.getAxis() == Direction.Axis.Z) {
                pos = pos.zRot(-(float) Math.toRadians(horizontalGimbal.getAngle()));
            }
        }
        
        if (verticalGimbal != null) {
            pos = pos.yRot(-(float) Math.toRadians(verticalGimbal.getAngle()));
        }
        
        return pos.add(0.5, 0.5, 0.5);
    }
    
    private Vec3 getFluidMountPos() {
        return hasFluidConnection ?
            fluidMount.getBlockPos().getCenter().add(
                Vec3.atLowerCornerOf(fluidMount.getBlockState().getValue(DirectionalBlock.FACING).getOpposite().getNormal())
                    .scale(2/16f)
                    .subtract(Vec3.atLowerCornerOf(getBlockPos()))
            )
            : new Vec3(1, 1, 1);
    }
    
    public void setIncomingFluidMount(BlockPos neighborPos) {
        incomingFluidMount = neighborPos;
    }
    
    public boolean acceptsIncomingFluidMount() {
        return !hasFluidConnection && incomingFluidMount == null && canHaveFluidInput();
    }
    
    public abstract boolean canHaveFluidInput();
    
}
