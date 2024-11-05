package uk.cak.cpp.fabric.registry;

import com.jozufozu.flywheel.core.PartialModel;
import uk.cak.cpp.fabric.CreatePlusPlus;

public class CppPartialModels {
    
    public static final PartialModel
        GIMBAL_SECONDARY_ARM = block("gimbal/gimbal_secondary_arm"),
        GIMBAL_PRIMARY_ARM = block("gimbal/gimbal_primary_arm"),
        GIMBAL_FLUID_PIPE_SECTION = block("gimbal/gimbal_fluid_pipe_section");
    
    private static PartialModel block(String path) {
        return new PartialModel(CreatePlusPlus.asResource("block/" + path));
    }
    
    public static void register() {}
    
}
