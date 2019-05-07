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
    if (entityItem.isInWater())
    {
      World world = entityItem.world;

      if (!world.isRemote)
      {
        int count = entityItem.getItem().getCount();
        entityItem.setDead();
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                new ItemStack(ModBlocks.runic_soil_water, count)));
      }
    }
    else if (entityItem.isInLava())
    {
      World world = entityItem.world;

      if (!world.isRemote)
      {
        int count = entityItem.getItem().getCount();
        entityItem.setDead();
        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
                new ItemStack(ModBlocks.runic_soil_fire, count)));
      }
    }
    return false;
  }
}
