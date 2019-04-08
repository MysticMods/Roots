package epicsquid.roots.spell;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class SpellThaw extends SpellBase{

  public static String spellName = "spell_thaw";
  public static SpellThaw instance = new SpellThaw(spellName);

  public SpellThaw(String name) {
    super(name, TextFormatting.AQUA, 25F/255F, 1F, 235F/255F, 252F/255F, 166F/255F, 37F/255F);

    this.castType = EnumCastType.CONTINUOUS;
    this.cooldown = 20;

    addCost(HerbRegistry.getHerbByName("wildewheet"), 0.25F);

    addIngredients(
            new ItemStack(ModItems.bark_acacia),
            new ItemStack(Blocks.TORCH),
            new ItemStack(Blocks.TORCH),
            new ItemStack(ModItems.bark_acacia)
            //MAYBE sunflowers (?)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    BlockPos pos = RitualUtil.getRandomPosRadial(caster.getPosition(), 5, 5);

    //FIXME the boolean system to avoid useless herb cost doesn't work
    boolean applied = false;

    //FIXME this for idea doesn't work yet
    for (int i = 0; i <= 1; i++) {
      pos = pos.down(i);
      if (caster.world.getBlockState(pos) == Blocks.SNOW_LAYER.getDefaultState()) {
        caster.world.setBlockState(pos, Blocks.AIR.getBlockState().getBaseState());
        applied = true;
      }
      //had .down() originally
      if (caster.world.getBlockState(pos) == Blocks.SNOW.getDefaultState() || caster.world.getBlockState(pos) == Blocks.ICE.getDefaultState()) {
        caster.world.setBlockState(pos, Blocks.WATER.getBlockState().getBaseState());
        applied = true;
      }
      //had .down() originally
      if (caster.world.getBlockState(pos) == Blocks.PACKED_ICE.getDefaultState()) {
        caster.world.setBlockState(pos, Blocks.ICE.getBlockState().getBaseState());
        applied = true;
      }
      //had .down() originally
      //FIXME (probably broken condition)
      if (caster.world.getBlockState(pos).getBlock().isFertile(caster.world, pos)) {
        caster.world.setBlockState(pos, Blocks.FARMLAND.getBlockState().getBaseState().withProperty(BlockFarmland.MOISTURE, 4));
        applied = true;
      }
    }
      return applied;
  }
}
