package epicsquid.roots.ritual;

import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockBonfire;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ICondition;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.util.types.PropertyTable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
  public Class<? extends EntityRitualBase> entityClass;
  private List<ICondition> conditions = new ArrayList<>();
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

  public Class<? extends EntityRitualBase> getEntityClass() {
    return this.entityClass;
  }

  public void setEntityClass(Class<? extends EntityRitualBase> entityClass) {
    this.entityClass = entityClass;
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

  public List<ICondition> getConditions() {
    return this.conditions;
  }

  public void addCondition(ICondition condition) {
    this.conditions.add(condition);
  }

  public boolean isRitualRecipe(TileEntityBonfire tileEntityBonfire, @Nullable EntityPlayer player) {
    if (isDisabled()) return false;
    for (ICondition condition : this.conditions) {
      if (condition instanceof ConditionItems) {
        ConditionItems conditionItems = (ConditionItems) condition;
        return conditionItems.checkCondition(tileEntityBonfire, player);
      }
    }
    return false;
  }

  public boolean canFire(TileEntityBonfire tileEntityBonfire, @Nullable EntityPlayer player) {
    IBlockState state = tileEntityBonfire.getWorld().getBlockState(tileEntityBonfire.getPos());
    if (state.getValue(BlockBonfire.BURNING)) {
      return false;
    }

    return checkTileConditions(tileEntityBonfire, player);
  }

  public boolean checkTileConditions(TileEntityBonfire tileEntityBonfire, @Nullable EntityPlayer player) {
    boolean success = true;
    for (ICondition condition : this.conditions) {
      if (!condition.check(tileEntityBonfire, player)) {
        success = false;
      }
    }
    return success;
  }

  @Nullable
  public EntityRitualBase doEffect(World world, BlockPos pos, @Nullable EntityPlayer player) {
    return this.spawnEntity(world, pos, getEntityClass(), player);
  }

  @Nullable
  protected EntityRitualBase spawnEntity(World world, BlockPos pos, Class<? extends EntityRitualBase> entity, @Nullable EntityPlayer player) {
    List<EntityRitualBase> pastRituals = world
        .getEntitiesWithinAABB(entity, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1));
    pastRituals.removeIf(o -> !o.getClass().equals(entity));
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
      if (player != null) {
        ritual.setPlayer(player.getUniqueID());
      }
      world.spawnEntity(ritual);
      return ritual;
    } else if (pastRituals.size() > 0) {

      for (EntityRitualBase ritual : pastRituals) {
        if (ritual.getClass().equals(entity)) {
          ritual.getDataManager().set(EntityRitualBase.lifetime, getDuration() + 20);
          ritual.getDataManager().setDirty(EntityRitualBase.lifetime);
        }
      }
      return pastRituals.get(0);
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
  public List<Ingredient> getIngredients() {
    for (ICondition condition : this.conditions) {
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