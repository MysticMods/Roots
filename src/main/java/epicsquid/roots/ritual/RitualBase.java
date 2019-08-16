package epicsquid.roots.ritual;

import epicsquid.roots.block.BlockBonfire;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.recipe.conditions.Condition;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class RitualBase {
  protected static int OFFERTORY_RADIUS = 6;
  protected static Random random = new Random();

  private List<Condition> conditions = new ArrayList<>();

  private Item icon;
  private String name;
  private int duration;
  private TextFormatting color;
  private boolean bold;

  public RitualBase(String name, int duration) {
    this.name = name;
    this.duration = duration;
  }

  public String getFormat () {
    String format = this.color + "";
    if (this.bold) {
      format += TextFormatting.BOLD;
    }
    return format;
  }

  public void setColor(TextFormatting color) {
    this.color = color;
  }

  public void setBold(boolean bold) {
    this.bold = bold;
  }

  public Item getIcon() {
    return icon;
  }

  public void setIcon(Item icon) {
    this.icon = icon;
  }

  public List<Condition> getConditions () {
    return this.conditions;
  }

  public void addCondition(Condition condition){
    this.conditions.add(condition);
  }

  public boolean isRitualRecipe(TileEntityBonfire tileEntityBonfire, @Nullable EntityPlayer player){
    for(Condition condition : this.conditions){
      if(condition instanceof ConditionItems){
        ConditionItems conditionItems = (ConditionItems) condition;
        return conditionItems.checkCondition(tileEntityBonfire, player);
      }
    }
    return false;
  }

  public boolean canFire(TileEntityBonfire tileEntityBonfire, EntityPlayer player) {
    IBlockState state = tileEntityBonfire.getWorld().getBlockState(tileEntityBonfire.getPos());
    if (state.getValue(BlockBonfire.BURNING)) {
      return false;
    }

    for(Condition condition : this.conditions){
      if(!condition.checkCondition(tileEntityBonfire, player)){
        return false;
      }
    }
    return true;
  }

  public abstract EntityRitualBase doEffect(World world, BlockPos pos);

  protected EntityRitualBase spawnEntity(World world, BlockPos pos, Class<? extends EntityRitualBase> entity) {
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
        return null;
      }
      ritual.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
      world.spawnEntity(ritual);
      return ritual;
    } else if (pastRituals.size() > 0) {
      for (EntityRitualBase ritual : pastRituals) {
        ritual.getDataManager().set(ritual.getLifetime(), duration + 20);
        ritual.getDataManager().setDirty(ritual.getLifetime());
      }
    }
    return null;
  }

  public int getDuration() {
    return duration;
  }

  public String getName() {
    return name;
  }

  @SuppressWarnings("unchecked")
  public List<ItemStack> getRecipe(){
    for(Condition condition : this.conditions){
      if(condition instanceof ConditionItems){
        ConditionItems conditionItems = (ConditionItems) condition;
        ItemStack[] stacks = conditionItems.getIngredients().stream()
            .map(ingredient -> ingredient.getMatchingStacks()[0])
            .toArray(ItemStack[]::new);
        return Arrays.asList(stacks);
      }
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  public List<Ingredient> getIngredients() {
    for (Condition condition : this.conditions) {
      if (condition instanceof ConditionItems) {
        return ((ConditionItems) condition).getIngredients();
      }
    }
    return Collections.EMPTY_LIST;
  }
}