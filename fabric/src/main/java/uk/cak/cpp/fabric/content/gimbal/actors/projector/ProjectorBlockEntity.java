package uk.cak.cpp.fabric.content.gimbal.actors.projector;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import uk.cak.cpp.fabric.CreatePlusPlusClient;
import uk.cak.cpp.fabric.system.projector.ProjectorRenderSystem;
import uk.cak.cpp.fabric.system.projector.ProjectorRenderer;

import java.util.List;
import java.util.UUID;

public class ProjectorBlockEntity extends SmartBlockEntity {
    
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
        Vector3d pos = new Vector3d(getBlockPos().getX() + 1.1, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5);
        renderer.setOrigin(pos);
        renderer.setDirection(new Quaternionf().identity().lookAlong(new Vector3f(1f, 0f, 0f), new Vector3f(0f, 1f, 0f)));
        renderer.setColor(new Vector3f(0.2f, 0.2f, 0.3f));
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    
    }
    
}
