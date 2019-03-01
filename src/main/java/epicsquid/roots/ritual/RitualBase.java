package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.recipe.conditions.Condition;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class RitualBase {
  protected static int OFFERTORY_RADIUS = 6;
  protected static Random random = new Random();

  private List<Condition> conditions = new ArrayList<>();

  private String name;
  private int duration;

  public RitualBase(String name, int duration) {
    this.name = name;
    this.duration = duration;
  }

  protected void addCondition(Condition condition){
    this.conditions.add(condition);
  }

  public boolean isRitualRecipe(TileEntityBonfire tileEntityBonfire, EntityPlayer player){
    for(Condition condition : this.conditions){
      if(condition instanceof ConditionItems){
        ConditionItems conditionItems = (ConditionItems) condition;
        return conditionItems.checkCondition(tileEntityBonfire, player);
      }
    }
    return false;
  }

  public boolean canFire(TileEntityBonfire tileEntityBonfire, EntityPlayer player) {
    for(Condition condition : this.conditions){
      if(!condition.checkCondition(tileEntityBonfire, player)){
        return false;
      }
    }
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

  public int getDuration() {
    return duration;
  }

  public String getName() {
    return name;
  }

  public List<ItemStack> getRecipe(){
    for(Condition condition : this.conditions){
      if(condition instanceof ConditionItems){
        ConditionItems conditionItems = (ConditionItems) condition;
        return conditionItems.getItemList();
      }
    }
    return new ArrayList<>();
  }
}