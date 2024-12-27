package mysticmods.roots.api.recipe.crafting;




public interface IRootsCrafting<H extends IItemHandler> extends IRootsCraftingBase, IIInvWrapper<H> {
  @Override
  H getHandler();
}
