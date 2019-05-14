package epicsquid.roots.item;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.network.fx.ElementalSoilTransformFX;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemBlockElementalSoil extends ItemBlock {

  public ItemBlockElementalSoil(Block block) {
    super(block);
    setRegistryName(new ResourceLocation(Roots.MODID, "runic_soil"));
  }

  @Override
  public boolean onEntityItemUpdate(EntityItem entityItem) {

    World world = entityItem.world;
    int count = entityItem.getItem().getCount();

    if (!world.isRemote && entityItem.isInLava())
    {
      world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY + 0.5, entityItem.posZ,
              new ItemStack(ModBlocks.elemental_soil_fire, count)));
      PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 0), entityItem);
      entityItem.setDead();
    }

    if (!world.isRemote && entityItem.ticksExisted >= 40)
    {
      entityItem.setDead();

      if (entityItem.isInWater())
      {
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                new ItemStack(ModBlocks.elemental_soil_water, count)));
        PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 1), entityItem);
      }
      else if (entityItem.posY <= 20)
      {
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                new ItemStack(ModBlocks.elemental_soil_earth, count)));
        PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 3), entityItem);
      }
      else if (entityItem.posY >= 120)
      {
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                new ItemStack(ModBlocks.elemental_soil_air, count)));
        PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 2), entityItem);
      }
    }
    else
      return super.onEntityItemUpdate(entityItem);

      return false;
  }
}
