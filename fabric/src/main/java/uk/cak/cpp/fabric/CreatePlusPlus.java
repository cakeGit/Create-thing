package uk.cak.cpp.fabric;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import uk.cak.cpp.ExampleMod;
import net.fabricmc.api.ModInitializer;
import uk.cak.cpp.fabric.registry.CppBlockEntities;
import uk.cak.cpp.fabric.registry.CppBlocks;
import uk.cak.cpp.fabric.registry.CppCreativeModeTabs;

public class CreatePlusPlus implements ModInitializer {
    
    public static final String ID = "create_plus_plus";
    
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);
    
    @Override
    public void onInitialize() {
        ExampleMod.init();
        CppBlocks.register();
        CppBlockEntities.register();
        CppCreativeModeTabs.register();
        REGISTRATE.register();
    }
    
    public static ResourceLocation asResource(String loc) {
        return new ResourceLocation(ID, loc);
    }
    
}
