package epicsquid.roots.util.types;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class RegistryItem implements IRegistryItem {
	private ResourceLocation resourceLocation;
	private String cachedName = null;
	
	@Override
	public void setRegistryName(ResourceLocation name) {
		this.resourceLocation = name;
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return this.resourceLocation;
	}
	
	@Nonnull
	@Override
	public String getCachedName() {
		if (cachedName == null) {
			cachedName = this.resourceLocation.toString();
		}
		
		return cachedName;
	}
}
