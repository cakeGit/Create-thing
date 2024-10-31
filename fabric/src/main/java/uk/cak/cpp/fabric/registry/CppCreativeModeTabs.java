package uk.cak.cpp.fabric.registry;

import com.simibubi.create.foundation.utility.Components;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import uk.cak.cpp.fabric.CreatePlusPlus;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CppCreativeModeTabs {
    
    public static final TabInfo CREATIVE_TAB = register("tab",
        () -> FabricItemGroup.builder()
            .title(Components.translatable("itemGroup.create_plus_plus.base"))
            .icon(CppBlocks.PROJECTOR::asStack)
            .displayItems(new CppCreativeTabGenerator())
            .build());
    
    public static TabInfo register(String id, Supplier<CreativeModeTab> supplier) {
        ResourceLocation location = CreatePlusPlus.asResource(id);
        
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, location);
        CreativeModeTab tab = supplier.get();
        
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, location, tab);
        return new TabInfo(key, tab);
    }
    
    public static void register() {}
    
    protected static class CppCreativeTabGenerator implements CreativeModeTab.DisplayItemsGenerator {
        
        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters arg, CreativeModeTab.Output arg2) {
            arg2.acceptAll(
                CreatePlusPlus.REGISTRATE.getAll(Registries.ITEM)
                    .stream()
                    .map(i -> i.get().getDefaultInstance())
                    .collect(Collectors.toSet())
            );
        }
        
    }
    
    public record TabInfo(ResourceKey<CreativeModeTab> key, CreativeModeTab tab) {
    }
}
