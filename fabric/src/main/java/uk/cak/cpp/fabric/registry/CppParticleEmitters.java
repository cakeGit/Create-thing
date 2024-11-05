package uk.cak.cpp.fabric.registry;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import uk.cak.cpp.fabric.foundation.ParticleEmitter;

import java.util.HashMap;

public class CppParticleEmitters {
    
    //Must be kept the same on client and server
    public static final HashMap<Integer, ParticleEmitter> INSTANCES_BY_HASH = new HashMap<>();
    
    public static final ParticleEmitter
        CONNECTION_MOUNTED = registerInstance(new ParticleEmitter(ParticleTypes.CLOUD)
        .setEmitFromCenterStrength(new Vec3(0.1f, 0.1f, 0.1f))
        .setRandomVelocityStrength(0.05f)
        .setVolume(new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)));
    
    private static ParticleEmitter registerInstance(ParticleEmitter particleEmitter) {
        INSTANCES_BY_HASH.put(particleEmitter.hashCode(), particleEmitter);
        return particleEmitter;
    }
    
    public static void register() {}
    
}
