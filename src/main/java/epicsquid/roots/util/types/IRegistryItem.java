package epicsquid.roots.util.types;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IRegistryItem {
	void setRegistryName(ResourceLocation name);
	
	@Nonnull
	ResourceLocation getRegistryName();
	
	@Nonnull
	String getCachedName();
}
