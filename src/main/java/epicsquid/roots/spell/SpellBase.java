package epicsquid.roots.spell;

import com.google.common.collect.ImmutableList;
import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.config.SpellConfig;
import epicsquid.roots.entity.spell.EntitySpellBase;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstance;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstanceList;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.recipe.IRootsRecipe;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.tileentity.TileEntityMortar;
import epicsquid.roots.util.ClientHerbUtil;
import epicsquid.roots.util.ServerHerbUtil;
import epicsquid.roots.util.types.RegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("Duplicates")
public abstract class SpellBase extends RegistryItem {
  protected PropertyTable properties = new PropertyTable();

  private boolean finalised = false;

  private float red1, green1, blue1;
  private float red2, green2, blue2;
  private String name;
  protected int cooldown = 20;
  protected boolean disabled = false;
  protected SpellConfig.SpellSoundsCategory.SpellSound sound = null;

  private TextFormatting textColor;
  protected EnumCastType castType = EnumCastType.INSTANTANEOUS;
  private Object2DoubleOpenHashMap<Herb> costs = new Object2DoubleOpenHashMap<>();
  private Set<Modifier> acceptedModifiers = new HashSet<>();

  public SpellRecipe recipe = SpellRecipe.EMPTY;

  protected SoundEvent cast_sound;

  public enum EnumCastType {
    INSTANTANEOUS, CONTINUOUS
  }

  public SpellBase(ResourceLocation name, TextFormatting textColor, float r1, float g1, float b1, float r2, float g2, float b2) {
    setRegistryName(name);
    this.name = name.getPath();
    this.red1 = r1;
    this.green1 = g1;
    this.blue1 = b1;
    this.red2 = r2;
    this.green2 = g2;
    this.blue2 = b2;
    this.textColor = textColor;
  }

  public float[] getFirstColours() {
    return getFirstColours(1.0f);
  }

  public float[] getFirstColours(float alpha) {
    return new float[]{red1, green1, blue1, alpha};
  }

  public float[] modifyFirstColours(float value) {
    return modifyFirstColours(value, 1.0f);
  }

  public float[] modifyFirstColours(float value, float alpha) {
    return new float[]{red1 * value, green1 * value, blue1 * value, alpha};
  }

  public float[] modifySecondColours(float value) {
    return modifySecondColours(value, 1.0f);
  }

  public float[] modifySecondColours(float value, float alpha) {
    return new float[]{red2 * value, green2 * value, blue2 * value, alpha};
  }

  public float[] getSecondColours(float alpha) {
    return new float[]{red2, green2, blue2, alpha};
  }

  public abstract void init();

  public SpellConfig.SpellSoundsCategory.SpellSound getSound() {
    return sound;
  }

  public void setSound(SpellConfig.SpellSoundsCategory.SpellSound sound) {
    this.sound = sound;
  }

  public boolean shouldPlaySound() {
    if (sound == null) {
      return true;
    } else {
      return sound.enabled;
    }
  }

  public float getSoundVolume() {
    if (sound == null) {
      return 1;
    } else {
      return (float) sound.volume;
    }
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public PropertyTable getProperties() {
    return properties;
  }

  public SpellBase acceptModifiers(Modifier... modules) {
    acceptedModifiers.addAll(Arrays.asList(modules));
    for (Modifier mod : modules) {
      for (Property<ModifierCost> prop : mod.asProperties()) {
        properties.add(prop);
      }
    }
    return this;
  }

  public Set<Modifier> getModifiers() {
    return acceptedModifiers;
  }

  public boolean acceptsModifiers (Modifier ... modules) {
    for (Modifier mod : modules) {
      if (!acceptedModifiers.contains(mod)) {
        return false;
      }
    }

    return true;
  }

  public void setCastSound(@Nullable SoundEvent event) {
    this.cast_sound = event;
  }

  @Nullable
  public SoundEvent getCastSound() {
    return this.cast_sound;
  }

  public void setRecipe(SpellRecipe recipe) {
    this.recipe = recipe;
  }

  public SpellBase addIngredients(Object... stacks) {
    this.recipe = new SpellRecipe(stacks);
    return this;
  }

  public boolean costsMet(EntityPlayer player, StaffModifierInstanceList modifiers) {
    boolean matches = true;
    for (Map.Entry<Herb, Double> entry : modifiers.apply(this.costs).entrySet()) {
      Herb herb = entry.getKey();
      double d = entry.getValue();
      if (matches) {
        double r;
        if (!player.world.isRemote) {
          r = ServerHerbUtil.getHerbAmount(player, herb);
        } else {
          r = ClientHerbUtil.getHerbAmount(herb);
        }
        matches = r >= d;
        if (!matches && !player.isCreative()) {
          if (r == -1.0) {
            if (!player.world.isRemote) {
              player.sendStatusMessage(new TextComponentTranslation("roots.info.pouch.no_pouch").setStyle(new Style().setColor(TextFormatting.RED)), true);
            }
          } else {
            if (!player.world.isRemote) {
              player.sendStatusMessage(new TextComponentTranslation("roots.info.pouch.no_herbs", new TextComponentTranslation(String.format("item.%s.name", herb.getName()))), true);
            }
          }
        }
      }
    }
    return matches && costs.size() > 0 || player.capabilities.isCreativeMode;
  }

  public void enactCosts(EntityPlayer player, StaffModifierInstanceList modifiers) {
    for (Map.Entry<Herb, Double> entry : modifiers.apply(this.costs).entrySet()) {
      Herb herb = entry.getKey();
      double d = entry.getValue();
      ServerHerbUtil.removePowder(player, herb, d);
    }
  }

  public void enactTickCosts(EntityPlayer player, StaffModifierInstanceList modifiers) {
    for (Map.Entry<Herb, Double> entry : modifiers.apply(this.costs).entrySet()) {
      Herb herb = entry.getKey();
      double d = entry.getValue();
      ServerHerbUtil.removePowder(player, herb, d / 20.0);
    }
  }

  @SideOnly(Side.CLIENT)
  public void addToolTipBase(List<String> tooltip, @Nullable BaseModifierInstanceList<?> list) {
    Object2DoubleOpenHashMap<Herb> costs = this.costs;
    if (list != null) {
      costs = list.apply(costs);
    }
    String prefix = getTranslationKey();
    tooltip.add("" + textColor + TextFormatting.BOLD + I18n.format(prefix + ".name") + TextFormatting.RESET);
    if (finalised()) {
      for (Map.Entry<Herb, Double> entry : costs.entrySet()) {
        Herb herb = entry.getKey();
        String d = String.format("%.4f", entry.getValue());
        tooltip.add(I18n.format(herb.getItem().getTranslationKey() + ".name") + I18n.format("roots.tooltip.pouch_divider") + d);
      }
    }
  }

  public String getTranslationKey() {
    return "roots.spell." + name;
  }

  @SideOnly(Side.CLIENT)
  public void addToolTip(List<String> tooltip, @Nullable LibraryModifierInstanceList list) {
    addToolTipBase(tooltip, list);
    if (list != null) {
      if (!list.isEmpty()) {
        tooltip.add("");
      }
      for (LibraryModifierInstance modifier : list) {
        if (modifier.isApplied()) {
          tooltip.add(modifier.describeName());
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  public void addToolTip(List<String> tooltip, @Nullable StaffModifierInstanceList list) {
    addToolTipBase(tooltip, list);
    addToolTipInfo(tooltip, list);
  }

  @SideOnly(Side.CLIENT)
  public void addToolTipInfo(List<String> tooltip, @Nullable StaffModifierInstanceList list) {
    if (list != null) {
      double addition = 0;
      double subtraction = 0;

      if (!GuiScreen.isShiftKeyDown()) {
        StringJoiner joiner = new StringJoiner(", ");
        for (StaffModifierInstance m : list) {
          if (m.getModifier() == null || !m.isApplied() || !m.isEnabled()) {
            continue;
          }

          for (IModifierCost c : m.getCosts().values()) {
            if (c.getCost() == CostType.ALL_COST_MULTIPLIER) {
              if (c.getValue() < 0) {
                subtraction += Math.abs(c.getValue());
              } else {
                addition += Math.abs(c.getValue());
              }
            }
          }
          joiner.add(m.describe());
        }

        String result = joiner.toString();
        if (!result.isEmpty()) {
          tooltip.add(result);
        }
        if (GuiScreen.isShiftKeyDown()) {
          if (!result.isEmpty()) {
            tooltip.add(result);
          }
        }
      } else {
        for (StaffModifierInstance m : list) {
          if (m.getModifier() == null || !m.isApplied() || !m.isEnabled()) {
            continue;
          }

          for (IModifierCost c : m.getCosts().values()) {
            if (c.getCost() == CostType.ALL_COST_MULTIPLIER) {
              if (c.getValue() < 0) {
                subtraction += Math.abs(c.getValue());
              } else {
                addition += Math.abs(c.getValue());
              }
            }
          }
          tooltip.add(m.describe());
        }
      }
      double actualSub = subtraction - addition;
      double actualAdd = addition - subtraction;
      if (actualSub > 0) {
        tooltip.add(I18n.format("roots.tooltip.reduced_by", Math.floor(actualSub * 100) + "%"));
      }
      if (actualAdd > 0) {
        tooltip.add(I18n.format("roots.tooltip.increased_by", Math.floor(actualAdd * 100) + "%"));
      }
    }
  }

  private SpellBase addCost(SpellCost cost) {
    return addCost(cost.getHerb(), cost.getCost());
  }

  private SpellBase addCost(Herb herb, double amount) {
    if (herb == null) {
      System.out.println("Spell - " + this.getClass().getName() + " - added a null herb ingredient. This is a bug.");
      return this;
    }
    costs.put(herb, amount);
    return this;
  }

  public boolean matchesIngredients(List<ItemStack> ingredients) {
    return ListUtil.matchesIngredients(ingredients, this.getIngredients());
  }

  public enum CastResult {
    FAIL,
    SUCCESS,
    SUCCESS_SPEEDY,
    SUCCESS_GREATER_SPEEDY;

    public boolean isSuccess() {
      return this != FAIL;
    }

    public long modifyCooldown(long cooldown) {
      switch (this) {
        default:
        case SUCCESS:
          return 0;
        case SUCCESS_SPEEDY:
          return Math.round(cooldown * 0.1);
        case SUCCESS_GREATER_SPEEDY:
          return Math.round(cooldown * 0.3);
      }
    }
  }

  public CastResult cast(EntityPlayer caster, StaffSpellInfo info, int ticks) {
    StaffModifierInstanceList mods = info.getModifiers();

    CastResult result = CastResult.FAIL;

    if (cast(caster, info.getModifiers(), ticks)) {
      result = CastResult.SUCCESS;
    }

    if (result != CastResult.FAIL && !caster.world.isRemote && (ticks == 0 || ticks == 72000)) {
      SoundEvent event = this.getCastSound();
      if (event != null && shouldPlaySound()) {
        caster.world.playSound(null, caster.getPosition(), event, SoundCategory.PLAYERS, getSoundVolume(), 1);
      }
    }
    return result;
  }

  protected abstract boolean cast(EntityPlayer caster, StaffModifierInstanceList info, int ticks);

  public float getRed1() {
    return red1;
  }

  public float getGreen1() {
    return green1;
  }

  public float getBlue1() {
    return blue1;
  }

  public float getRed2() {
    return red2;
  }

  public float getGreen2() {
    return green2;
  }

  public float getBlue2() {
    return blue2;
  }

  public String getName() {
    return name;
  }

  public int getCooldown() {
    return cooldown;
  }

  public TextFormatting getTextColor() {
    return textColor;
  }

  public EnumCastType getCastType() {
    return castType;
  }

  public Object2DoubleOpenHashMap<Herb> getCosts() {
    return costs;
  }

  public List<Ingredient> getIngredients() {
    return recipe.getIngredients();
  }

  private ItemStack result = null;
  private ItemStack icon = null;

  public ItemStack getResult() {
    if (result == null) {
      result = new ItemStack(ModItems.spell_dust);
      DustSpellStorage.fromStack(result).setSpellToSlot(this);
    }
    return result;
  }

  public ItemStack getIcon() {
    if (icon == null) {
      icon = new ItemStack(ModItems.spell_icon);
      DustSpellStorage.fromStack(icon).setSpellToSlot(this);
    }
    return icon;
  }

  private List<ItemStack> costItemCache = null;

  public List<ItemStack> getCostItems() {
    if (costItemCache == null) {
      List<ItemStack> costList = new ArrayList<>();
      for (Herb cost : this.costs.keySet()) {
        costList.add(new ItemStack(cost.getItem()));
      }
      costItemCache = ImmutableList.copyOf(costList);
    }
    return costItemCache;
  }

  public abstract void doFinalise();

  @SuppressWarnings("unchecked")
  public void finaliseCosts() {
    for (Map.Entry<String, Property<?>> entry : getProperties()) {
      if (!entry.getKey().startsWith("cost_")) {
        continue;
      }

      Property<SpellCost> prop = (Property<SpellCost>) entry.getValue();
      SpellCost cost = properties.get(prop);

      if (cost != null) {
        addCost(cost);
      }
    }
    for (Modifier mod : getModifiers()) {
      List<ModifierCost> costs = new ArrayList<>();
      for (Property<ModifierCost> prop : mod.asProperties()) {
        costs.add(properties.get(prop));
      }
      mod.replaceCosts(costs);
    }
    this.finalised = true;
  }

  public void finalise() {
    doFinalise();
    finaliseCosts();
    validateProperties();
  }

  public void validateProperties() {
    List<String> values = properties.finalise();
    if (!values.isEmpty()) {
      StringJoiner join = new StringJoiner(",");
      values.forEach(join::add);
      Roots.logger.error("Spell '" + name + "' property table has the following keys inserted but not fetched: |" + join.toString() + "|");
    }
  }

  public boolean finalised() {
    return finalised;
  }

  @Nullable
  protected EntitySpellBase spawnEntity(World world, BlockPos pos, Class<? extends EntitySpellBase> entity, @Nullable EntityPlayer player, double amplifier, double speedy) {
    return spawnEntity(world, new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), entity, player, amplifier, speedy);
  }

  @Nullable
  protected EntitySpellBase spawnEntity(World world, Vec3d pos, Class<? extends EntitySpellBase> entity, @Nullable EntityPlayer player, double amplifier, double speedy) {
    List<EntitySpellBase> pastRituals = world.getEntitiesWithinAABB(entity, new AxisAlignedBB(pos.x, pos.y, pos.z - 100, pos.x + 2, pos.y + 100, pos.z + 1), o -> o != null && o.getClass().equals(entity));
    if (pastRituals.isEmpty() && !world.isRemote) {
      EntitySpellBase spell = null;
      try {
        Constructor<? extends EntitySpellBase> cons = entity.getDeclaredConstructor(World.class);
        spell = cons.newInstance(world);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
        e.printStackTrace();
      }
      if (spell == null) {
        return null;
      }
      spell.setPosition(pos.x, pos.y, pos.z);
      if (player != null) {
        spell.setPlayer(player.getUniqueID());
      }
      world.spawnEntity(spell);
      return spell;
    }
    return null;
  }

  public static class ModifierCost {
    private Herb herb;
    private CostType type;
    private double cost;

    public ModifierCost(IModifierCost cost) {
      this(cost.getHerb(), cost.getCost(), cost.getValue());
    }

    public ModifierCost(Herb herb, CostType type, double cost) {
      this.herb = herb;
      this.type = type;
      this.cost = cost;
    }

    public Herb getHerb() {
      return herb;
    }

    public CostType getType() {
      return type;
    }

    public double getCost() {
      return cost;
    }

    public IModifierCost asCost() {
      return new Cost(getType(), getCost(), ModifierCores.fromHerb(getHerb()));
    }

    @Override
    public String toString() {
      return "ModifierCost{" +
          "herb=" + (herb == null ? "none" : herb.getName()) +
          ", type=" + type +
          ", cost=" + cost +
          '}';
    }
  }

  public static class SpellCost {
    public static SpellCost EMPTY = new SpellCost(null, 0);

    private String herb;
    private double cost;

    public SpellCost(String herb, double cost) {
      this.herb = herb;
      this.cost = cost;
    }

    public Herb getHerb() {
      return HerbRegistry.getHerbByName(herb);
    }

    public double getCost() {
      return cost;
    }

    public String getHerbName() {
      return this.herb;
    }

    @Override
    public String toString() {
      return "SpellCost{" +
          "herb='" + herb + '\'' +
          ", cost=" + cost +
          '}';
    }
  }


  public static class SpellRecipe implements IRootsRecipe<TileEntityMortar> {
    public static SpellRecipe EMPTY = new SpellRecipe();

    private List<Ingredient> ingredients = new ArrayList<>();

    public SpellRecipe(Object... stacks) {
      for (Object stack : stacks) {
        if (stack instanceof Ingredient) {
          ingredients.add((Ingredient) stack);
        } else if (stack instanceof ItemStack) {
          ingredients.add(Ingredient.fromStacks((ItemStack) stack));
        }
      }
    }

    @Override
    public List<Ingredient> getIngredients() {
      return ingredients;
    }
  }
}