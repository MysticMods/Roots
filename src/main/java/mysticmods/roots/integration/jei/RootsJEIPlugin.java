package mysticmods.roots.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.IDescribedRegistryEntry;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.item.TokenItem;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class RootsJEIPlugin implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(RootsAPI.MODID, "jei");
  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistration registration) {
    registration.registerSubtypeInterpreter(ModItems.TOKEN.get(), (itemStack, uidContext) -> {
      TokenItem.Type type = TokenItem.getType(itemStack);
      if (type == null) {
        return "";
      }
      IDescribedRegistryEntry entry = switch (type) {
        case RITUAL -> TokenItem.getRitual(itemStack);
        case SPELL -> TokenItem.getSpell(itemStack);
        case MODIFIER -> TokenItem.getSingleModifier(itemStack);
        default -> null;
      };
      if (entry == null) {
        return "";
      }
      return entry.getDescriptionId();
    });
  }

}
