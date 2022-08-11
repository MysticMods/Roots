package epicsquid.roots.init;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HerbRegistry {
	public static final Map<String, Herb> registry = new HashMap<>();
	public static final Set<Item> HERB_ITEMS = new HashSet<>();
	public static final Herb spirit_herb = register("spirit_herb", () -> ModItems.spirit_herb);
	public static final Herb baffle_cap = register("baffle_cap", () -> ModItems.baffle_cap);
	public static final Herb moonglow_leaf = register("moonglow_leaf", () -> ModItems.moonglow_leaf);
	public static final Herb pereskia = register("pereskia", () -> ModItems.pereskia);
	public static final Herb terra_moss = register("terra_moss", () -> ModItems.terra_moss);
	public static final Herb wildroot = register("wildroot", () -> ModItems.wildroot);
	public static final Herb wildewheet = register("wildewheet", () -> ModItems.wildewheet);
	public static final Herb infernal_bulb = register("infernal_bulb", () -> ModItems.infernal_bulb);
	public static final Herb dewgonia = register("dewgonia", () -> ModItems.dewgonia);
	public static final Herb stalicripe = register("stalicripe", () -> ModItems.stalicripe);
	public static final Herb cloud_berry = register("cloud_berry", () -> ModItems.cloud_berry);
	
	public static Herb register(String name, Supplier<Item> item) {
		Herb result = new Herb(item);
		result.setRegistryName(new ResourceLocation(Roots.MODID, name));
		registry.put(name, result);
		return result;
	}
	
	@Nullable
	public static Herb getHerb(ResourceLocation res) {
		Herb result = registry.get(res.getPath());
		if (result == null) {
			for (Herb herb : registry.values()) {
				if (herb.getRegistryName().equals(res)) {
					return herb;
				}
			}
		}
		return result;
	}
	
	@Nullable
	public static Herb getHerbByName(@Nonnull String name) {
		Herb result = registry.get(name);
		if (result == null) {
			Roots.logger.warn("Herb \"" + name + "\" not found in HerbRegistry");
		}
		return result;
	}
	
	@Nullable
	public static Herb getHerbByItem(@Nonnull Item item) {
		if (!isHerb(item)) {
			return null;
		}
		
		for (Herb herb : registry.values()) {
			if (herb.getItem() == item) {
				return herb;
			}
		}
		Roots.logger.warn("Herb \"" + item.getRegistryName() + "\" not found in HerbRegistry");
		return null;
	}
	
	public static boolean isHerb(ItemStack stack) {
		return isHerb(stack.getItem());
	}
	
	public static boolean isHerb(@Nonnull Item item) {
		if (HERB_ITEMS.isEmpty()) {
			HERB_ITEMS.addAll(registry.values().stream().map(Herb::getItem).collect(Collectors.toSet()));
		}
		return HERB_ITEMS.contains(item);
	}
}
