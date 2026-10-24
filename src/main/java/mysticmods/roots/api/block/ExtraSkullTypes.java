package mysticmods.roots.api.block;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.level.block.SkullBlock;

public enum ExtraSkullTypes implements SkullBlock.Type {
  // Extra zombies
  HUSK("husk"),
  // TODO: 2nd layer
  DROWNED("drowned"),
  // TODO: profession layer
  ZOMBIE_VILLAGER("zombie_villager"),
  ZOMBIFIED_PIGLIN("zombified_piglin"),
  // Skeletons
  // TODO: 2nd layer
  BOGGED("bogged"),
  // TODO: 2nd layer
  STRAY("stray"),
  // Enderman, TODO: special eyes
  ENDERMAN("enderman"),
  // Pillager-esque
  WITCH("witch"),
  PILLAGER("pillager"),
  ILLUSIONER("illusioner"),
  // Extra peegleens
  PIGLIN_BRUTE("piglin_brute"),
  // Villagers
  // TODO: extra lay4ers
  VILLAGER("villager"),
  WANDERING_TRADER("wandering_trader"),
  // Goleems
  // TODO: special overlay
  IRON_GOLEM("iron_golem");

  private final String name;

  ExtraSkullTypes(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  // Called via enqueueWork
  public static void register() {
    for (ExtraSkullTypes type : values()) {
      SkullBlock.Type.TYPES.put(type.name, type);
    }
  }
}
