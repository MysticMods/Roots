package mysticmods.roots.client;

import com.mojang.blaze3d.platform.InputConstants;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import java.util.Arrays;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class KeyBindings {
  public static final String CATEGORY = "key.category.roots.general";

  public static final CastingTaggedSpell ADJUSTABLE = CastingTaggedSpell.spell(RootsTags.Spells.ADJUSTABLE_SPELL);
  public static final HasTomeSlotAdjustable HAS_ADJUSTABLE_TOME = new HasTomeSlotAdjustable();

  public static final IKeyConflictContext HAS_ANY_ADJUSTABLE = new MultiKeyConflictContext(ADJUSTABLE, HAS_ADJUSTABLE_TOME);

  public static final KeyMapping OPEN_SPELL_LIBRARY = new KeyMapping("key.roots.open_spell_library", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_K, CATEGORY);
  public static final KeyMapping OPEN_POUCH = new KeyMapping("key.roots.open_pouch", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CATEGORY);
  public static final KeyMapping OPEN_REPUTATION = new KeyMapping("key.roots.open_reputation", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CATEGORY);
  public static final KeyMapping INCREASE_SPELL = new KeyMapping("key.roots.increase_spell", ADJUSTABLE, InputConstants.Type.KEYSYM, InputConstants.KEY_RBRACKET, CATEGORY);
  public static final KeyMapping DECREASE_SPELL = new KeyMapping("key.roots.decrease_spell", ADJUSTABLE, InputConstants.Type.KEYSYM, InputConstants.KEY_LBRACKET, CATEGORY);
  public static final KeyMapping CYCLE_ADJUSTABLE = new KeyMapping("key.roots.cycle_adjustable", HAS_ANY_ADJUSTABLE, InputConstants.Type.KEYSYM, InputConstants.KEY_BACKSLASH, CATEGORY);

  public static final List<KeyMapping> MAPPINGS = Arrays.asList(
      OPEN_SPELL_LIBRARY,
      INCREASE_SPELL,
      DECREASE_SPELL,
      CYCLE_ADJUSTABLE,
      OPEN_POUCH,
      OPEN_REPUTATION);


  @SubscribeEvent
  public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
    event.register(OPEN_SPELL_LIBRARY);
    event.register(INCREASE_SPELL);
    event.register(DECREASE_SPELL);
    event.register(CYCLE_ADJUSTABLE);
    event.register(OPEN_POUCH);
    event.register(OPEN_REPUTATION);
  }

  public static class MultiKeyConflictContext implements IKeyConflictContext {
    private final List<IKeyConflictContext> contexts;

    public MultiKeyConflictContext(IKeyConflictContext... contexts) {
      this.contexts = Arrays.asList(contexts);
    }

    public MultiKeyConflictContext(List<IKeyConflictContext> contexts) {
      this.contexts = contexts;
    }

    @Override
    public boolean isActive() {
      for (IKeyConflictContext context : contexts) {
        if (context.isActive()) {
          return true;
        }
      }

      return false;
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
      for (IKeyConflictContext context : contexts) {
        if (context.conflicts(other)) {
          return true;
        }
      }

      return this == other;
    }
  }

  public static class HasTomeSlotAdjustable implements IKeyConflictContext {
    @Override
    public boolean isActive() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.screen != null) {
        return false;
      }

      if (minecraft.player == null) {
        return false;
      }

      ItemStack tome = RootsAPI.getInstance().getTome(minecraft.player);
      if (tome.isEmpty()) {
        return false;
      }

      return tome.is(RootsTags.Items.ADJUSTABLE_ITEM);
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
      return this == other;
    }
  }


  public static class HoldingTaggedItem implements IKeyConflictContext {
    private final TagKey<Item> tag;

    public static HoldingTaggedItem item(TagKey<Item> tag) {
      return new HoldingTaggedItem(tag);
    }

    protected HoldingTaggedItem(TagKey<Item> tag) {
      this.tag = tag;
    }

    @Override
    public boolean isActive() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.screen != null) {
        return false;
      }

      if (minecraft.player == null) {
        return false;
      }

      ItemStack item = minecraft.player.getMainHandItem();
      if (item.isEmpty()) {
        return false;
      }

      return item.is(tag);
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
      return this == other;
    }
  }

  public static class CastingTaggedSpell extends HoldingTaggedItem {
    private final TagKey<Spell> tag;

    public static CastingTaggedSpell spell(TagKey<Spell> tag) {
      return new CastingTaggedSpell(tag);
    }

    public static CastingTaggedSpell spell(TagKey<Item> item, TagKey<Spell> spell) {
      return new CastingTaggedSpell(item, spell);
    }

    protected CastingTaggedSpell(TagKey<Item> item, TagKey<Spell> spell) {
      super(item);
      this.tag = spell;
    }

    protected CastingTaggedSpell(TagKey<Spell> tag) {
      super(RootsTags.Items.CASTING_TOOLS);
      this.tag = tag;
    }

    @Override
    public boolean isActive() {
      if (!super.isActive()) {
        return false;
      }

      ItemStack item = Minecraft.getInstance().player.getMainHandItem();

      if (!item.has(ModAttachments.SPELL_STORAGE)) {
        return false;
      }

      SpellStorage storage = item.get(ModAttachments.SPELL_STORAGE);
      if (storage == null) {
        return false;
      }

      ISpellInstance spell = storage.getCurrentSpell();

      if (spell == null) {
        return false;
      }

      return spell.asSpell().is(tag);
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
      return this == other;
    }
  }
}
