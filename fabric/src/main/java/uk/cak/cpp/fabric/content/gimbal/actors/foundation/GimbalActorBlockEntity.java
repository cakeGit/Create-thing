package uk.cak.cpp.fabric.content.gimbal.actors.foundation;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uk.cak.cpp.fabric.content.gimbal.components.gimbal_axis.GimbalAxisBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public abstract class GimbalActorBlockEntity extends SmartBlockEntity {
    
    boolean isOnGimbal = false;
    @Nullable
    GimbalAxisBlockEntity horizontalGimbal = null;
    @Nullable
    GimbalAxisBlockEntity verticalGimbal = null;
    
    boolean hasFluidConnection = false;
    @Nullable
    GimbalAxisBlockEntity fluidMount = null;
    
    public GimbalActorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
    }
    
    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
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
    
}
