package mysticmods.roots.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import noobanidus.libs.noobutil.crafting.ICrafting;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.List;

// TODO: Move to NoobUtil
public interface IProcessorRecipe<C extends ICrafting<?, ?>> extends IRecipe<C> {
  List<Processor<C>> getProcessors();

  void addProcessor (Processor<C> processor);
}
