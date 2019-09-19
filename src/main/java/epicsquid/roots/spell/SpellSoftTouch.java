package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageSoftTouchFX;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.Block;
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

  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("terra_moss", 0.125));

  public static String spellName = "spell_soft_touch";
  public static SpellSoftTouch instance = new SpellSoftTouch(spellName);

  public SpellSoftTouch(String name) {
    super(name, TextFormatting.GRAY, 64F/255F, 232F/255F, 159F/255F, 209F/255F, 209F/255F, 209F/255F);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1);
  }

  @Override
  public void init() {
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
    RayTraceResult result = caster.world.rayTraceBlocks(caster.getPositionVector().add(0, caster.getEyeHeight(), 0), caster.getLookVec().scale(8.0f).add(caster.getPositionVector().add(0, caster.getEyeHeight(), 0)));
    if (result != null && !caster.world.isRemote) {
      if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
        BlockPos pos = result.getBlockPos();
        IBlockState state = caster.world.getBlockState(pos);
        if (canApplySpell(state))
        {
          // FIXME: 07/09/2019 dropping sunflowers with tall grass
          caster.world.setBlockToAir(pos);
          caster.world.spawnEntity(new EntityItem(caster.world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(state.getBlock())));

          PacketHandler.sendToAllTracking(new MessageSoftTouchFX(pos.getX(), pos.getY(), pos.getZ()), caster);
          return true;
        }
      }
    }
    return false;
  }

  private boolean canApplySpell(IBlockState state)
  {
    Block block = state.getBlock();

    return (Item.getItemFromBlock(block) != (block.getItemDropped(state, Util.rand, 0))
            || block.equals(Blocks.ICE)
            || block.equals(Blocks.PACKED_ICE)
            || block.equals(Blocks.GLASS)
            || block.equals(Blocks.GLASS_PANE)
            || block.equals(Blocks.STAINED_GLASS)
            || block.equals(Blocks.STAINED_GLASS_PANE));

  }

  @Override
  public void finalise() {

    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);

    SpellCost cost1 = properties.getProperty(PROP_COST_1);
    this.addCost(cost1.getHerb(), cost1.getCost());
  }

}
