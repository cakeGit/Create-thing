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
import uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis.GimbalAxisBlockEntity;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalActorConnection;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalActorConnectionType;
import uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation.GimbalMountBlockEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GimbalActorBlockEntity extends SmartBlockEntity {
    
    boolean isOnGimbal = false;
    @Nullable
    protected GimbalAxisBlockEntity horizontalGimbal = null;
    @Nullable
    protected GimbalAxisBlockEntity verticalGimbal = null;
    
    List<BlockPos> incomingConnectionPositions = new ArrayList<>();
    HashMap<BlockPos, GimbalActorConnection> connections = new HashMap<>();
    
//    boolean hasFluidConnection = false;
//    @Nullable
//    GimbalFluidMountBlockEntity fluidMount = null;
//    BlockPos incomingFluidMount = null;
//    BlockPos fluidMountPos = null;
//    /**
//     * Client / renderer only
//     */
//    @Nullable
//    SimulatedRope fluidConnectionVisuals = null;
    
    public GimbalActorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void tick() {
        super.tick();
        for (BlockPos pos : incomingConnectionPositions) {
            BlockEntity be = level.getBlockEntity(pos);
            
            if (be instanceof GimbalMountBlockEntity gimbalMountBlockEntity) {
                GimbalActorConnection actorConnection = new GimbalActorConnection(gimbalMountBlockEntity, this);
                connections.put(actorConnection.getOffset(), actorConnection);
                sendData();
            }
        }
        incomingConnectionPositions = new ArrayList<>();
        
        List<BlockPos> expired = new ArrayList<>();
        for (Map.Entry<BlockPos, GimbalActorConnection> entry : connections.entrySet()) {
            boolean shouldRemove = entry.getValue().tickState(this, level);
            if (shouldRemove) expired.add(entry.getKey());
        }
        for (BlockPos pos : expired) {
            connections.remove(pos);
        }
        
        if (level.isClientSide) {
            for (GimbalActorConnection connection : connections.values()) {
                connection.updatePositions(this);
            }
        }
    }
    
    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        List<GimbalActorConnection> connectionValues = connections.values().stream().toList();
        tag.putInt("ConnectionsSize", connectionValues.size());
        for (int i = 0; i < connectionValues.size(); i++) {
            tag.put("Connections" + i, connectionValues.get(i).write());
        }
    }
    
    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (tag.contains("ConnectionsSize")) {
            int size = tag.getInt("ConnectionsSize");
            List<GimbalActorConnection> readConnections = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                CompoundTag connectionTag = tag.getCompound("Connections" + i);
                readConnections.add(new GimbalActorConnection(connectionTag));
            }
            
            for (GimbalActorConnection connection : readConnections) {
                if (!connections.containsKey(connection.getOffset())) {
                    connections.put(connection.getOffset(), connection);
                }
            }
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
    
    private Vec3 localToWorld(Vec3 local) {
        Vec3 world = local;
        world.yRot(-(float) Math.toRadians(getRotationOfDirection(getBlockState().getValue(DirectionalBlock.FACING))));
        
        if (horizontalGimbal != null) {
            if (horizontalGimbal.getAxis() == Direction.Axis.X) {
                world = world.xRot(-(float) Math.toRadians(horizontalGimbal.getAngle()));
            } else if (horizontalGimbal.getAxis() == Direction.Axis.Z) {
                world = world.zRot(-(float) Math.toRadians(horizontalGimbal.getAngle()));
            }
        }
        
        if (verticalGimbal != null) {
            world = world.yRot(-(float) Math.toRadians(verticalGimbal.getAngle()));
        }
        
        return world.add(0.5, 0.5, 0.5);
    }
    
    public void acceptIncomingMount(BlockPos mountBlockEntity) {
        incomingConnectionPositions.add(mountBlockEntity);
    }
    
    private float getRotationOfDirection(Direction direction) {
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
    
//    private Vec3 getFluidMountPos() {
//        return hasFluidConnection ?
//            fluidMount.getBlockPos().getCenter().add(
//                Vec3.atLowerCornerOf(fluidMount.getBlockState().getValue(DirectionalBlock.FACING).getOpposite().getNormal())
//                    .scale(2/16f)
//                    .subtract(Vec3.atLowerCornerOf(getBlockPos()))
//            )
//            : new Vec3(1, 1, 1);
//    }
    
//    public void setIncomingFluidMount(BlockPos neighborPos) {
//        incomingFluidMount = neighborPos;
//    }
    
    public boolean acceptsIncomingMount(GimbalActorConnectionType connectionType) {
        return true;
    }
    
    public abstract boolean canHaveFluidInput();
    
    public Vec3 getAttachmentPosForType(GimbalActorConnectionType connectionType) {
        switch (connectionType) {
            case FLUID -> {
                return localToWorld(new Vec3(-0.5, 0, 0));
            }
            case ITEM -> {}
            case SIGNAL -> {}
        }
        return new Vec3(0, 0, 0);
    }
    
}
