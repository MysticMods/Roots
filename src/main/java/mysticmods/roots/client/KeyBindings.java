package mysticmods.roots.client;

import com.mojang.blaze3d.platform.InputConstants;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.gui.layer.HudOverlay;
import mysticmods.roots.client.gui.screen.fake.SpellModifierScreen;
import mysticmods.roots.client.gui.screen.fake.StaffScreen;
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

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class KeyBindings {
  public static final String CATEGORY = "key.category.roots.general";

  public static final CastingTaggedSpell ADJUSTABLE = CastingTaggedSpell.spell(RootsTags.Spells.ADJUSTABLE_SPELL);
  public static final HasTomeSlotAdjustable HAS_ADJUSTABLE_TOME = new HasTomeSlotAdjustable();
  public static final HoldingTaggedItem HOLDING_STAFF = HoldingTaggedItem.item(RootsTags.Items.CASTING_TOOLS);

  public static final LibraryKeyConflictContext IN_LIBRARY = new LibraryKeyConflictContext();

  public static final NearRelevantBlockEntity NEAR_RELEVANT_BLOCK_ENTITY = new NearRelevantBlockEntity();

  public static final IKeyConflictContext HAS_ANY_ADJUSTABLE = new MultiKeyConflictContext(ADJUSTABLE, HAS_ADJUSTABLE_TOME);

  public static final KeyMapping CANCEL_EFFECT = new KeyMapping("key.roots.cancel_effect", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_TAB, CATEGORY);
  public static final KeyMapping OPEN_SPELL_LIBRARY = new KeyMapping("key.roots.open_spell_library", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, InputConstants.KEY_K, CATEGORY);
  public static final KeyMapping OPEN_POUCH = new KeyMapping("key.roots.open_pouch", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CATEGORY);
  public static final KeyMapping OPEN_REPUTATION = new KeyMapping("key.roots.open_reputation", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), CATEGORY);
  public static final KeyMapping CYCLE_SPELL_MODE = new KeyMapping("key.roots.cycle_spell_mode", HAS_ANY_ADJUSTABLE, InputConstants.Type.KEYSYM, InputConstants.KEY_BACKSLASH, CATEGORY);
  public static final KeyMapping CYCLE_STAFF_SPELL = new KeyMapping("key.roots.cycle_staff_spell", HOLDING_STAFF, InputConstants.Type.KEYSYM, InputConstants.KEY_PAGEDOWN, CATEGORY);
  public static final KeyMapping OPEN_FAKE_MENU = new KeyMapping("key.roots.open_fake_menu", NEAR_RELEVANT_BLOCK_ENTITY, InputConstants.Type.KEYSYM, InputConstants.KEY_INSERT, CATEGORY);
  public static final KeyMapping CLEAR_CONTAINER = new KeyMapping("key.roots.clear_container", NEAR_RELEVANT_BLOCK_ENTITY, InputConstants.Type.KEYSYM, InputConstants.KEY_DELETE, CATEGORY);
  public static final KeyMapping DELETE_SPELL = new KeyMapping("key.roots.delete_spell", IN_LIBRARY, InputConstants.Type.KEYSYM, InputConstants.KEY_DELETE, CATEGORY);
  public static final KeyMapping MODIFY_SPELL = new KeyMapping("key.roots.modify_spell", IN_LIBRARY, InputConstants.Type.KEYSYM, InputConstants.KEY_INSERT, CATEGORY);

  public static final List<KeyMapping> MAPPINGS = Arrays.asList(
      OPEN_SPELL_LIBRARY,
      CYCLE_SPELL_MODE,
      CYCLE_STAFF_SPELL,
      OPEN_POUCH,
      OPEN_REPUTATION,
      CLEAR_CONTAINER,
      OPEN_FAKE_MENU,
      DELETE_SPELL,
      MODIFY_SPELL);


  @SubscribeEvent
  public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
    event.register(OPEN_SPELL_LIBRARY);
    event.register(CYCLE_SPELL_MODE);
    event.register(CYCLE_STAFF_SPELL);
    event.register(OPEN_POUCH);
    event.register(OPEN_REPUTATION);
    event.register(CANCEL_EFFECT);
    event.register(CLEAR_CONTAINER);
    event.register(OPEN_FAKE_MENU);
    event.register(DELETE_SPELL);
    event.register(MODIFY_SPELL);
  }

  public static class LibraryKeyConflictContext implements IKeyConflictContext {
    @Override
    public boolean isActive() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.screen == null) {
        return false;
      }

      if (minecraft.player == null) {
        return false;
      }

      return (minecraft.screen instanceof StaffScreen || minecraft.screen instanceof SpellModifierScreen);
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
      return this == other;
    }
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
      ItemStack item2 = minecraft.player.getOffhandItem();
      if (item.isEmpty() && item2.isEmpty()) {
        return false;
      }

      return item.is(tag) || item2.is(tag);
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
      return this == other;
    }
  }

  public static class NearRelevantBlockEntity implements IKeyConflictContext {
    @Override
    public boolean isActive() {
      return HudOverlay.getStoredBlockPos() != null;
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
