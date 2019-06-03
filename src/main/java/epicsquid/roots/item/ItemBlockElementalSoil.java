package epicsquid.roots.item;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.network.fx.ElementalSoilTransformFX;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBlockElementalSoil extends ItemBlock {

  private Block block;

  public ItemBlockElementalSoil(Block block, String name) {
    super(block);
    this.block = block;
    setRegistryName(new ResourceLocation(Roots.MODID, name));
  }

  @Override
  public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {

      Block oldblock = world.getBlockState(pos.offset(side.getOpposite())).getBlock();

      if (oldblock instanceof BlockFarmland && player.inventory.addItemStackToInventory(new ItemStack(oldblock))) {
        pos = pos.offset(side.getOpposite());
      }
    return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
  }

  @Override
  public boolean onEntityItemUpdate(EntityItem entityItem) {

    if (block == ModBlocks.elemental_soil)
    {
      World world = entityItem.world;
      int count = entityItem.getItem().getCount();

      if (!world.isRemote && entityItem.isInLava())
      {
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY + 0.5, entityItem.posZ,
                new ItemStack(ModBlocks.elemental_soil_fire, count)));
        PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 0), entityItem);
        entityItem.setDead();
        return true;
      }

      if (!world.isRemote && entityItem.ticksExisted >= 40)
      {
        entityItem.setDead();

        if (entityItem.isInWater()) {
          world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                  new ItemStack(ModBlocks.elemental_soil_water, count)));
          PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 1), entityItem);
          return true;
        }
        else if (entityItem.posY <= 20) {
          world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                  new ItemStack(ModBlocks.elemental_soil_earth, count)));
          PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 3), entityItem);
          return true;
        }
        else if (entityItem.posY >= 120) {
          world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                  new ItemStack(ModBlocks.elemental_soil_air, count)));
          PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 2), entityItem);
          return true;
        }
      }
    }
    return super.onEntityItemUpdate(entityItem);
  }
}
