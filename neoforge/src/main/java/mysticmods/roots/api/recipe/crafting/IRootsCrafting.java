package mysticmods.roots.api.recipe.crafting;

import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.inventory.IIInvWrapper;

public interface IRootsCrafting<H extends IItemHandler> extends IRootsCraftingBase, IIInvWrapper<H> {
  @Override
  H getHandler();
}
