package epicsquid.roots.ritual;

import java.util.List;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualRegrowth;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualRegrowth extends RitualBase {
  public RitualRegrowth(String name, int duration, boolean doUpdateValidity){
    super(name,duration,doUpdateValidity);
    addIngredient(new ItemStack(ModItems.terra_moss,1));
    addIngredient(new ItemStack(ModItems.terra_moss_seed,1));
    addIngredient(new ItemStack(ModItems.bark_spruce,1));
    addIngredient(new ItemStack(Blocks.SAPLING,1,1));
    addIngredient(new ItemStack(Blocks.SAPLING,1));
  }

  @Override
  public void doEffect(World world, BlockPos pos){
    List<EntityRitualRegrowth> pastRituals = world.getEntitiesWithinAABB(EntityRitualRegrowth.class, new AxisAlignedBB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1,pos.getY()+100,pos.getZ()+1));
    if (pastRituals.size() == 0 && !world.isRemote){
      EntityRitualRegrowth ritual = new EntityRitualRegrowth(world);
      ritual.setPosition(pos.getX()+0.5, pos.getY()+6.5, pos.getZ()+0.5);
      world.spawnEntity(ritual);
    }
    else if (pastRituals.size() > 0){
      for (EntityRitualRegrowth ritual : pastRituals){
        ritual.getDataManager().set(EntityRitualRegrowth.lifetime, duration+20);
        ritual.getDataManager().setDirty(EntityRitualRegrowth.lifetime);
      }
    }
  }
}