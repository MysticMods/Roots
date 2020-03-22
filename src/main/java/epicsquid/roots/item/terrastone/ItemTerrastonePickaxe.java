package epicsquid.roots.item.terrastone;

import epicsquid.mysticallib.item.ItemPickaxeBase;
import epicsquid.roots.Roots;
import epicsquid.roots.config.ToolConfig;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemTerrastonePickaxe extends ItemPickaxeBase implements ILivingRepair {
  public ItemTerrastonePickaxe(ToolMaterial material, String name) {
    super(material, name, 3, 565);
    // TODO: ???
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected, 20);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && RootsIngredients.MOSSY_COBBLESTONE.test(repair);
  }

}
