package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModTypes {
  public static final BlockSetType RUNESTONE_SET = BlockSetType.register(new BlockSetType(RootsAPI.rl("runestone").toString()));
  public static final BlockSetType RUNED_OBSIDIAN_SET = BlockSetType.register(new BlockSetType(RootsAPI.rl("runed_obsidian").toString()));
  public static final BlockSetType WILDWOOD_SET = BlockSetType.register(new BlockSetType(RootsAPI.rl("wildwood").toString()));

  public static final WoodType WILDWOOD_WOOD_TYPE  = WoodType.register(new WoodType(RootsAPI.rl("wildwood").toString(), WILDWOOD_SET));

  // TODO: Do we need to load it?
  public static void load () {
  }
}
