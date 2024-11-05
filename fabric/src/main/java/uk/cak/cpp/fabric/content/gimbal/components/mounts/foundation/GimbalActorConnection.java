package uk.cak.cpp.fabric.content.gimbal.components.mounts.foundation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import uk.cak.cpp.fabric.content.gimbal.actors.foundation.GimbalActorBlockEntity;
import uk.cak.cpp.fabric.foundation.rope.SimulatedRope;

import javax.annotation.Nullable;

public class GimbalActorConnection {

    @Nullable
    GimbalMountBlockEntity target = null;
    BlockPos offset;
    
    SimulatedRope connectionVisuals = null;
    
    int ticksBeforeExpire = 1;
    
    public GimbalActorConnection(GimbalMountBlockEntity target, GimbalActorBlockEntity owner) {
        this.target = target;
        offset = target.getBlockPos().subtract(owner.getBlockPos());
    }
    
    public GimbalActorConnection(CompoundTag tag) {
        this.offset = new BlockPos(tag.getInt("xOffset"), tag.getInt("yOffset"), tag.getInt("zOffset"));
    }
    
    /**
     * @return If the connection should be removed
     * */
    public boolean tickState(GimbalActorBlockEntity owner, Level level) {
        if (target.isRemoved()) {
            return true;
        }
        
        if (target == null) {
            BlockEntity candidate = level.getBlockEntity(owner.getBlockPos().offset(offset));
            
            if (candidate instanceof GimbalMountBlockEntity gmbe) {
                target = gmbe;
            }
        }
        
        if (target == null) {
            if (ticksBeforeExpire == 0) {
                return true;
            }
            ticksBeforeExpire --;
        }
        return false;
    }
    
    public void read(CompoundTag tag) {
        this.offset = new BlockPos(tag.getInt("xOffset"), tag.getInt("yOffset"), tag.getInt("zOffset"));
    }
    
    public SimulatedRope getConnectionVisuals() {
        return connectionVisuals;
    }
    
    public void setConnectionVisuals(SimulatedRope connectionVisuals) {
        this.connectionVisuals = connectionVisuals;
    }
    
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("xOffset", offset.getX());
        tag.putInt("yOffset", offset.getY());
        tag.putInt("zOffset", offset.getZ());
        return tag;
    }
    
    public BlockPos getOffsetOfTag(CompoundTag tag) {
        return new BlockPos(tag.getInt("xOffset"), tag.getInt("yOffset"), tag.getInt("zOffset"));
    }
    
    public BlockPos getOffset() {
        return offset;
    }
    
    public GimbalMountBlockEntity getMountBlockEntity() {
        return target;
    }
    
}
