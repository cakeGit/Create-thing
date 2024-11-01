package uk.cak.cpp.fabric;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CppData implements DataGeneratorEntrypoint {
    
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        CreatePlusPlus.REGISTRATE.setupDatagen(fabricDataGenerator.createPack(), ExistingFileHelper.withResourcesFromArg());
    }
    
}
