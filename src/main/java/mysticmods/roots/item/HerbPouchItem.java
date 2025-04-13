package mysticmods.roots.item;

import mysticmods.roots.inventory.pouch.herb.HerbPouchMenu;

public class HerbPouchItem extends BasePouchItem {
  public HerbPouchItem(Properties properties) {
    super(properties);
  }

  @Override
  public PouchMenuProvider getMenuProvider() {
    return HerbPouchMenu::new;
  }
}
