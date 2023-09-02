package mysticmods.roots.api.grove;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.registry.StyledRegistryEntry;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Grove extends StyledRegistryEntry<Grove> {
  // This is really just a huge collection of tags.
  private final TagKey<Herb> herbs;
  private final TagKey<Grove> alignedGroves;
  private final TagKey<Grove> opposedGroves;

  private final TagKey<Spell> alignedSpells;
  private final TagKey<Spell> opposedSpells;

  private final TagKey<Ritual> alignedRituals;
  private final TagKey<Ritual> opposedRituals;

  public Grove(ChatFormatting color, TagKey<Herb> herbs, TagKey<Grove> alignedGroves, TagKey<Grove> opposedGroves, TagKey<Spell> alignedSpells, TagKey<Spell> opposedSpells, TagKey<Ritual> alignedRituals, TagKey<Ritual> opposedRituals) {
    this.color = color;
    this.herbs = herbs;
    this.alignedGroves = alignedGroves;
    this.opposedGroves = opposedGroves;
    this.alignedSpells = alignedSpells;
    this.opposedSpells = opposedSpells;
    this.alignedRituals = alignedRituals;
    this.opposedRituals = opposedRituals;
  }

  public boolean aligned (Herb herb) {
    return herb.is(herbs);
  }

  public boolean aligned (Spell spell) {
    return spell.is(alignedSpells);
  }

  public boolean aligned (Grove grove) {
    return grove.is(alignedGroves);
  }

  public boolean aligned (Ritual ritual) {
    return ritual.is(alignedRituals);
  }

  public boolean opposed (Spell spell) {
    return spell.is(opposedSpells);
  }

  public boolean opposed (Grove grove) {
    return grove.is(opposedGroves);
  }

  public boolean opposed (Ritual ritual) {
    return ritual.is(opposedRituals);
  }

  public boolean is(ResourceLocation location) {
    return Registries.GROVE_REGISTRY.get().getHolder(this).map(o -> o.is(location)).orElse(false);
  }

  public boolean is(ResourceKey<Grove> key) {
    return Registries.GROVE_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(Predicate<ResourceKey<Grove>> predicate) {
    return Registries.GROVE_REGISTRY.get().getHolder(this).map(o -> o.is(predicate)).orElse(false);
  }

  public boolean is(TagKey<Grove> tag) {
    return Registries.GROVE_REGISTRY.get().getHolder(this).map(o -> o.is(tag)).orElse(false);
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.GROVE_REGISTRY.get().getKey(this);
  }

  @Override
  protected String getDescriptor() {
    return "grove";
  }
}
