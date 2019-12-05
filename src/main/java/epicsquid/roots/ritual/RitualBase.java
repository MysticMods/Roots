package epicsquid.roots.ritual;

import epicsquid.roots.Roots;
import epicsquid.roots.block.BonfireBlock;
import epicsquid.roots.entity.ritual.BaseRitualEntity;
import epicsquid.roots.ritual.conditions.Condition;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.util.types.PropertyTable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class RitualBase {
  protected static int OFFERTORY_RADIUS = 6;
  protected static Random random = new Random();

  protected PropertyTable properties = new PropertyTable();

  private List<Condition> conditions = new ArrayList<>();
  private Item icon;
  private String name;
  private TextFormatting color;
  private boolean bold;

  protected int duration;

  protected boolean disabled;
  protected boolean finalised;

  public RitualBase(String name, boolean disabled) {
    this.name = name;
    this.disabled = disabled;
    this.duration = 0;
  }

  public String getFormat() {
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

  public boolean isDisabled() {
    return disabled;
  }

  public List<Condition> getConditions() {
    return this.conditions;
  }

  public void addCondition(Condition condition) {
    this.conditions.add(condition);
  }

  public boolean isRitualRecipe(TileEntityBonfire tileEntityBonfire, @Nullable PlayerEntity player) {
    if (isDisabled()) return false;
    for (Condition condition : this.conditions) {
      if (condition instanceof ConditionItems) {
        ConditionItems conditionItems = (ConditionItems) condition;
        return conditionItems.checkCondition(tileEntityBonfire, player);
      }
    }
    return false;
  }

  public boolean canFire(TileEntityBonfire tileEntityBonfire, @Nullable PlayerEntity player) {
    BlockState state = tileEntityBonfire.getWorld().getBlockState(tileEntityBonfire.getPos());
    if (state.get(BonfireBlock.BURNING)) {
      return false;
    }

    boolean success = true;
    for (Condition condition : this.conditions) {
      if (!condition.check(tileEntityBonfire, player)) {
        success = false;
      }
    }
    return success;
  }

  public abstract BaseRitualEntity doEffect(World world, BlockPos pos);

  protected BaseRitualEntity spawnEntity(World world, BlockPos pos, Class<? extends BaseRitualEntity> entity) {
    List<BaseRitualEntity> pastRituals = world
        .getEntitiesWithinAABB(entity, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1));
    if (pastRituals.size() == 0 && !world.isRemote) {
      BaseRitualEntity ritual = null;
      try {
        Constructor<? extends BaseRitualEntity> cons = entity.getDeclaredConstructor(World.class);
        ritual = cons.newInstance(world);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
        e.printStackTrace();
      }
      if (ritual == null) {
        return null;
      }
      ritual.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
      world.addEntity(ritual);
      return ritual;
    } else if (pastRituals.size() > 0) {
      for (BaseRitualEntity ritual : pastRituals) {
        ritual.getDataManager().set(BaseRitualEntity.lifetime, getDuration() + 20);
        // TODO:
        // return ritual; ???
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

  public abstract void init();

  @SuppressWarnings("unchecked")
  public List<ItemStack> getRecipe() {
    for (Condition condition : this.conditions) {
      if (condition instanceof ConditionItems) {
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

  public void finalise() {
    doFinalise();
    validateProperties();
  }

  public abstract void doFinalise();

  public void validateProperties() {
    List<String> values = properties.finalise();
    if (!values.isEmpty()) {
      StringJoiner join = new StringJoiner(",");
      values.forEach(join::add);
      Roots.logger.error("Ritual '" + name + "' property table has the following keys inserted but not fetched: |" + join.toString() + "|");
    }
  }

  public boolean finalised() {
    return finalised;
  }

  public PropertyTable getProperties() {
    return properties;
  }
}