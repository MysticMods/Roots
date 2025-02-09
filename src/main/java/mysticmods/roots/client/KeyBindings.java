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
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class KeyBindings {
  public static final String CATEGORY = "key.category.roots.general";

  public static final CastingTaggedSpell ADJUSTABLE = CastingTaggedSpell.spell(RootsTags.Spells.ADJUSTABLE_SPELL);

  public static final KeyMapping OPEN_SPELL_LIBRARY = new KeyMapping("key.roots.open_spell_library", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);
  public static final KeyMapping INCREASE_SPELL = new KeyMapping("key.roots.increase_spell", ADJUSTABLE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, CATEGORY);
  public static final KeyMapping DECREASE_SPELL = new KeyMapping("key.roots.decrease_spell", ADJUSTABLE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, CATEGORY);
  public static final KeyMapping CYCLE_SPELL = new KeyMapping("key.roots.cycle_spell", ADJUSTABLE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_BACKSLASH, CATEGORY);

  public static final List<KeyMapping> MAPPINGS = new ArrayList<>();

  @SubscribeEvent
  public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
    MAPPINGS.add(OPEN_SPELL_LIBRARY);
    MAPPINGS.add(INCREASE_SPELL);
    MAPPINGS.add(DECREASE_SPELL);
    MAPPINGS.add(CYCLE_SPELL);
    event.register(OPEN_SPELL_LIBRARY);
    event.register(INCREASE_SPELL);
    event.register(DECREASE_SPELL);
    event.register(CYCLE_SPELL);
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
