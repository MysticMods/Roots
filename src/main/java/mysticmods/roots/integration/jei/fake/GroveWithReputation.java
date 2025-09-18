package mysticmods.roots.integration.jei.fake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.registry.RootsRegistries;

public record GroveWithReputation (GroveAction groveAction, GroveReputationEntry entry) {
  public static final Codec<GroveWithReputation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      RootsRegistries.GROVE_ACTIONS.byNameCodec().fieldOf("grove_action").forGetter(GroveWithReputation::groveAction),
      GroveReputationEntry.CODEC.fieldOf("entry").forGetter(GroveWithReputation::entry)
  ).apply(instance, GroveWithReputation::new));
}
