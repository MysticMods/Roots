package epicsquid.roots.api;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import java.util.List;

public class SpellModifierProcessor implements IComponentProcessor {

  private SpellBase recipe = null;
  private List<ItemStack> stacks = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    recipe = SpellRegistry.getSpell(iVariableProvider.get("spell"));
    stacks = recipe.getModuleStacks();
  }

  @Override
  public String process(String s) {
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;

      if (index >= stacks.size()) {
        return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      }

      return ItemStackUtil.serializeStack(stacks.get(index));
    }

    return null;
  }
}
