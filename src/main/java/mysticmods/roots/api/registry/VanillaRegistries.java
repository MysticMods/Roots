package mysticmods.roots.api.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.function.Supplier;

public class VanillaRegistries {
  public static Supplier<ForgeRegistry<EntityType<?>>> ENTITIES;
}
