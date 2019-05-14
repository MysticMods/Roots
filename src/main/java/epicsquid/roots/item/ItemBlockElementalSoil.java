package epicsquid.roots.item;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
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

    System.out.println(entityItem.ticksExisted);

    World world = entityItem.world;
    int count = entityItem.getItem().getCount();

    if (!world.isRemote && entityItem.isInLava())
    {
      world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY + 0.5, entityItem.posZ,
              new ItemStack(ModBlocks.elemental_soil_fire, count)));

      entityItem.setDead();
    }

    if (!world.isRemote && entityItem.ticksExisted >= 100)
    {
      entityItem.setDead();

      if (entityItem.isInWater())
      {
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                new ItemStack(ModBlocks.elemental_soil_water, count)));
      }
      else if (entityItem.posY <= 20)
      {
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                new ItemStack(ModBlocks.elemental_soil_earth, count)));
      }
      else if (entityItem.posY >= 120)
      {
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                new ItemStack(ModBlocks.elemental_soil_air, count)));
      }
    }
    else
      return super.onEntityItemUpdate(entityItem);

      return false;
  }
}
