package mysticmods.roots.api.grove;

import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.registry.StyledRegistryEntry;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.function.Predicate;

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

  public Holder.Reference<Grove> builtInRegistryHolder() {
    return RootsRegistries.GROVES.getHolderOrThrow(RootsRegistries.GROVES.getResourceKey(this).orElseThrow());
  }

  public boolean aligned(Herb herb) {
    return herb.is(herbs);
  }

  public boolean aligned(Spell spell) {
    return spell.is(alignedSpells);
  }

  public boolean aligned(Grove grove) {
    return grove.is(alignedGroves);
  }

  public boolean aligned(Ritual ritual) {
    return ritual.is(alignedRituals);
  }

  public boolean opposed(Spell spell) {
    return spell.is(opposedSpells);
  }

  public boolean opposed(Grove grove) {
    return grove.is(opposedGroves);
  }

  public boolean opposed(Ritual ritual) {
    return ritual.is(opposedRituals);
  }

  public boolean is(ResourceLocation location) {
    return builtInRegistryHolder().is(location);
  }

  public boolean is(ResourceKey<Grove> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<Grove>> predicate) {
    return builtInRegistryHolder().is(predicate);
  }

  public boolean is(TagKey<Grove> tag) {
    return builtInRegistryHolder().is(tag);
  }

  @Override
  protected String getDescriptor() {
    return "grove";
  }
}
