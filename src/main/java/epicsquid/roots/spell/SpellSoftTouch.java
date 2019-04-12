package epicsquid.roots.spell;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.Random;

public class SpellSoftTouch extends SpellBase {

  public static String spellName = "spell_soft_touch";
  public static SpellSoftTouch instance = new SpellSoftTouch(spellName);

  public SpellSoftTouch(String name) {
    super(name, TextFormatting.GRAY, 64F/255F, 232F/255F, 159F/255F, 209F/255F, 209F/255F, 209F/255F);

    this.cooldown = 40;
    this.castType = EnumCastType.INSTANTANEOUS;

    addCost(HerbRegistry.getHerbByName("terra_moss"), 0.125F);
    addIngredients(
            new ItemStack(ModItems.bark_birch),
            new ItemStack(ModItems.terra_moss),
            new ItemStack(ModItems.living_pickaxe),
            new ItemStack(Items.STRING),
            new ItemStack(Items.FEATHER)
    );
  }

  @Override
  public boolean cast(EntityPlayer caster, List<SpellModule> modules) {
    RayTraceResult result = caster.world.rayTraceBlocks(caster.getPositionVector().addVector(0, caster.getEyeHeight(), 0), caster.getLookVec().scale(8.0f).add(caster.getPositionVector().addVector(0, caster.getEyeHeight(), 0)));
    if (result != null && !caster.world.isRemote) {
      if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
        BlockPos pos = result.getBlockPos();
        IBlockState state = caster.world.getBlockState(pos);
        if (canApplySpell(state))
        {
          caster.world.setBlockState(pos, Blocks.AIR.getBlockState().getBaseState());
          caster.world.spawnEntity(new EntityItem(caster.world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(state.getBlock())));
          return true;
        }
      }
    }
    return false;
  }

  private boolean canApplySpell(IBlockState state)
  {
    return (Item.getItemFromBlock(state.getBlock()) != (state.getBlock().getItemDropped(state, new Random(), 0))
            || state.getBlock().equals(Blocks.ICE)
            || state.getBlock().equals(Blocks.PACKED_ICE)
            || state.getBlock().equals(Blocks.GLASS)
            || state.getBlock().equals(Blocks.GLASS_PANE)
            || state.getBlock().equals(Blocks.STAINED_GLASS)
            || state.getBlock().equals(Blocks.STAINED_GLASS_PANE));

  }
}
