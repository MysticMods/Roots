// @HEADER@
package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.SpellType;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.reference.SpellCosts;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModModifiers {
  private static final DeferredRegister<SpellModifier> REGISTER = DeferredRegister.create(RootsRegistries.Keys.SPELL_MODIFIERS, RootsAPI.MODID);
  private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RootsAPI.MODID);

  public static final List<GroupId> GROUP_IDS = new ArrayList<>();

  static {
    var x = Cost.class;
    var y = CostInstance.class;
    var z = SpellType.class;
    var a = SpellCosts.class;
  }

  // @GROUPS@

  // @MODIFIERS@

  static {
    // @ALIASES@
    // @TOKEN_ITEMS@
  }

  private static TokenItem.SpellModifierTokenItem modifier(Holder<SpellModifier> modifier) {
    return new TokenItem.SpellModifierTokenItem(modifier.getKey(), new Item.Properties().stacksTo(1));
  }

  private static DeferredHolder<Item, TokenItem.SpellModifierTokenItem> modifier(DeferredRegister.Items reg, Holder<SpellModifier> modifier) {
    return reg.register(modifier.getKey().location().getPath(), () -> modifier(modifier));
  }

  public static GroupId group(String name) {
    return group(name, false);
  }

  public static GroupId group(String name, boolean useGroupDescription) {
    var id = new GroupId(name, useGroupDescription);
    GROUP_IDS.add(id);
    return id;
  }

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
    ITEMS.register(bus);
  }

  public static void noop() {
  }
}