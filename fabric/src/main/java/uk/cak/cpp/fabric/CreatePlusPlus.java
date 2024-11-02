package uk.cak.cpp.fabric;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import uk.cak.cpp.ExampleMod;
import uk.cak.cpp.fabric.foundation.network.CppPackets;
import uk.cak.cpp.fabric.registry.*;

public class CreatePlusPlus implements ModInitializer {
    
    public static final String ID = "create_plus_plus";
    
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);
    
    @Override
    public void onInitialize() {
        ExampleMod.init();
        CppBlocks.register();
        CppBlockEntities.register();
        CppCreativeModeTabs.register();
        CppPartialModels.register();
        CppParticleEmitters.register();
        CppPackets.registerPackets();
        CppPackets.getChannel().initServerListener();
        REGISTRATE.register();
    }
    
    public static ResourceLocation asResource(String loc) {
        return new ResourceLocation(ID, loc);
    }
    
}
