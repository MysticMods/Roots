package epicsquid.roots.ritual;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RitualBase {
  protected static int OFFERTORY_RADIUS = 6;

  private List<ItemStack> ingredients = new ArrayList<>();
  private String name;
  private int duration;

  public RitualBase(String name, int duration) {
    this.name = name;
    this.duration = duration;
  }

  public RitualBase addIngredients(ItemStack... stack) {
    Collections.addAll(ingredients, stack);
    return this;
  }

  public boolean canFire(World world, BlockPos pos, @Nullable EntityPlayer player) {
    return true;
  }

  public abstract void doEffect(World world, BlockPos pos);

  protected void spawnEntity(World world, BlockPos pos, Class<? extends EntityRitualBase> entity) {
    List<EntityRitualBase> pastRituals = world
        .getEntitiesWithinAABB(entity, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1));
    if (pastRituals.size() == 0 && !world.isRemote) {
      EntityRitualBase ritual = null;
      try {
        Constructor<? extends EntityRitualBase> cons = entity.getDeclaredConstructor(World.class);
        ritual = cons.newInstance(world);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
        e.printStackTrace();
      }
      if (ritual == null) {
        return;
      }
      ritual.setPosition(pos.getX() + 0.5, pos.getY() + 6.5, pos.getZ() + 0.5);
      world.spawnEntity(ritual);
    } else if (pastRituals.size() > 0) {
      for (EntityRitualBase ritual : pastRituals) {
        ritual.getDataManager().set(ritual.getLifetime(), duration + 20);
        ritual.getDataManager().setDirty(ritual.getLifetime());
      }
    }
  }

  protected int getStandingStones(World world, BlockPos pos, int height, @Nullable Block standingMaterial) {
    Block material = standingMaterial;
    if(material == null){
      material = ModBlocks.runestone;
    }
    int threeHighCount = 0;
    for (int i = -6; i < 7 + 1; i++) {
      for (int j = -6; j < 7 + 1; j++) {
        IBlockState state = world.getBlockState(pos.add(i, height-1, j));
        if (state.getBlock() == ModBlocks.chiseled_runestone) {
          boolean stoneFound = true;
          for(int y = height-1; y > 0; y--){
            if (world.getBlockState(pos.add(i, 1, j)).getBlock() != material){
              stoneFound = false;
            }
          }
          if(stoneFound){
            threeHighCount++;
          }
        }
      }
    }
    return threeHighCount;
  }

  public int getDuration() {
    return duration;
  }

  public List<ItemStack> getIngredients() {
    return ingredients;
  }

  public String getName() {
    return name;
  }
}