package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModTypes {
  private static final BlockSetType RUNESTONE = new BlockSetType(
      RootsAPI.rl("runestone").toString(),
      true,
      true,
      false,
      BlockSetType.PressurePlateSensitivity.MOBS,
      SoundType.STONE,
      SoundEvents.IRON_DOOR_CLOSE,
      SoundEvents.IRON_DOOR_OPEN,
      SoundEvents.IRON_TRAPDOOR_CLOSE,
      SoundEvents.IRON_TRAPDOOR_OPEN,
      SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
      SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
      SoundEvents.STONE_BUTTON_CLICK_OFF,
      SoundEvents.STONE_BUTTON_CLICK_ON
  );
  private static final BlockSetType RUNED_OBSIDIAN = new BlockSetType(
      RootsAPI.rl("runed_obsidian").toString(),
      true,
      true,
      false,
      BlockSetType.PressurePlateSensitivity.MOBS,
      SoundType.STONE,
      SoundEvents.IRON_DOOR_CLOSE,
      SoundEvents.IRON_DOOR_OPEN,
      SoundEvents.IRON_TRAPDOOR_CLOSE,
      SoundEvents.IRON_TRAPDOOR_OPEN,
      SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF,
      SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
      SoundEvents.STONE_BUTTON_CLICK_OFF,
      SoundEvents.STONE_BUTTON_CLICK_ON
  );

  public static final BlockSetType RUNESTONE_SET = BlockSetType.register(RUNESTONE);
  public static final BlockSetType RUNED_OBSIDIAN_SET = BlockSetType.register(RUNED_OBSIDIAN);
  public static final BlockSetType WILDWOOD_SET = BlockSetType.register(new BlockSetType(RootsAPI.rl("wildwood")
      .toString()));
  public static final WoodType WILDWOOD_WOOD_TYPE = WoodType.register(new WoodType(RootsAPI.rl("wildwood")
      .toString(), WILDWOOD_SET));
}
