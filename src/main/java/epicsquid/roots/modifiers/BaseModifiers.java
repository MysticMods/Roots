package epicsquid.roots.modifiers;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BaseModifiers {
  public static IModifierCore AIR = new IModifierCore() {
    private final ResourceLocation name = new ResourceLocation(Roots.MODID, "air");
    private String cachedName = null;

    @Override
    public void setRegistryName(ResourceLocation name) {
      throw new NotImplementedException("setRegistryName is not implemented for this");
    }

    @Nonnull
    @Override
    public ResourceLocation getRegistryName() {
      return name;
    }

    @Nonnull
    @Override
    public String getCachedName() {
      if (cachedName == null) {
        cachedName = getRegistryName().toString();
      }
      return cachedName;
    }

    @Override
    public Herb getHerb() {
      return null;
    }

    @Override
    public ItemStack getStack() {
      return ItemStack.EMPTY;
    }

    @Override
    public String getTranslationKey() {
      return "roots.modifiers.cores.air";
    }

    @Override
    public String getFormatting() {
      return "";
    }

    @Override
    public int getKey() {
      return -1;
    }
  };

  public static Modifier NULL = new Modifier(new ResourceLocation(Roots.MODID, "null"), AIR, Cost.noCost());
}
