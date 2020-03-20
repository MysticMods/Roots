package epicsquid.roots.spell;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.entity.spell.EntitySpellBase;
import epicsquid.roots.handler.SpellHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.IRootsRecipe;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.tileentity.TileEntityMortar;
import epicsquid.roots.util.ClientHerbUtil;
import epicsquid.roots.util.ServerHerbUtil;
import epicsquid.roots.util.types.Property;
import epicsquid.roots.util.types.PropertyTable;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public abstract class SpellBase {
  protected PropertyTable properties = new PropertyTable();

  private boolean finalised = false;

  private float red1, green1, blue1;
  private float red2, green2, blue2;
  private String name;
  protected int cooldown = 20;
  protected boolean disabled = false;

  private TextFormatting textColor;
  protected EnumCastType castType = EnumCastType.INSTANTANEOUS;
  private Object2DoubleOpenHashMap<Herb> costs = new Object2DoubleOpenHashMap<>();
  private List<SpellModule> acceptedModules = new ArrayList<>();
  private float[] firstColours;
  private float[] secondColours;

  public SpellRecipe recipe = SpellRecipe.EMPTY;

  public enum EnumCastType {
    INSTANTANEOUS, CONTINUOUS
  }

  public SpellBase(String name, TextFormatting textColor, float r1, float g1, float b1, float r2, float g2, float b2) {
    this.name = name;
    this.red1 = r1;
    this.green1 = g1;
    this.blue1 = b1;
    this.red2 = r2;
    this.green2 = g2;
    this.blue2 = b2;
    this.textColor = textColor;
    this.firstColours = new float[]{r1, g1, b1};
    this.secondColours = new float[]{r2, g2, b2};
  }

  public float[] getFirstColours() {
    return firstColours;
  }

  public float[] getSecondColours() {
    return secondColours;
  }

  public abstract void init ();

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public boolean hasModules() {
    return !acceptedModules.isEmpty();
  }

  public PropertyTable getProperties () {
    return properties;
  }

  public SpellBase acceptModules(SpellModule ... modules) {
    assert modules.length < 5;
    acceptedModules.addAll(Arrays.asList(modules));
    return this;
  }

  public List<SpellModule> getModules() {
    return acceptedModules;
  }

  public void setRecipe (SpellRecipe recipe) {
    this.recipe = recipe;
  }

  public SpellBase addIngredients(Object... stacks) {
    this.recipe = new SpellRecipe(this, stacks);
    return this;
  }

  public boolean costsMet(EntityPlayer player) {
    boolean matches = true;
    for (Map.Entry<Herb, Double> entry : this.costs.entrySet()) {
      Herb herb = entry.getKey();
      double d = entry.getValue();
      if (matches) {
        double r;
        if (!player.world.isRemote) {
          r = ServerHerbUtil.getPowderTotal(player, herb);
        } else {
          r = ClientHerbUtil.herbAmount(herb);
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

  public void enactCosts(EntityPlayer player) {
    for (Map.Entry<Herb, Double> entry : this.costs.entrySet()) {
      Herb herb = entry.getKey();
      double d = entry.getValue();
      ServerHerbUtil.removePowder(player, herb, d);
    }
  }

  public void enactTickCosts(EntityPlayer player) {
    for (Map.Entry<Herb, Double> entry : this.costs.entrySet()) {
      Herb herb = entry.getKey();
      double d = entry.getValue();
      ServerHerbUtil.removePowder(player, herb, d / 20.0);
    }
  }

  @SideOnly(Side.CLIENT)
  public void addToolTip(List<String> tooltip) {
    String prefix = "roots.spell." + name;
    tooltip.add("" + textColor + TextFormatting.BOLD + I18n.format(prefix + ".name") + TextFormatting.RESET);
    if (finalised()) {
      for (Map.Entry<Herb, Double> entry : this.costs.entrySet()) {
        Herb herb = entry.getKey();
        String d = String.format("%.4f", entry.getValue());
        tooltip.add(I18n.format(herb.getItem().getTranslationKey() + ".name") + I18n.format("roots.tooltip.pouch_divider") + d);
      }
    }
  }

  private List<ItemStack> moduleItems = null;

  @SideOnly(Side.CLIENT)
  public List<ItemStack> getModuleStacks() {
    if (moduleItems == null) {
      moduleItems = new ArrayList<>();
      String prefix = "roots.spell." + name + ".";
      String mod = I18n.format("roots.spell.module.description");

      for (SpellModule module : getModules()) {
        ItemStack stack = module.getIngredient().copy();
        String description = I18n.format(prefix + module.getName() + ".description");
        Util.appendLoreTag(stack, mod, description);
        moduleItems.add(stack);
      }
    }

    return moduleItems;
  }

  public SpellBase addCost (SpellCost cost) {
    return addCost(cost.getHerb(), cost.getCost());
  }

  public SpellBase addCost(Herb herb, double amount) {
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

  public abstract boolean cast(EntityPlayer caster, List<SpellModule> modules, int ticks);

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

  public ItemStack getResult() {
    ItemStack stack = new ItemStack(ModItems.spell_dust);
    SpellHandler.fromStack(stack).setSpellToSlot(this);
    return stack;
  }

  public List<ItemStack> getCostItems() {
    return costs.keySet().stream().map((herb) -> new ItemStack(herb.getItem())).collect(Collectors.toList());
  }

  public abstract void doFinalise();

  @SuppressWarnings("unchecked")
  public void finaliseCosts () {
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
    this.finalised = true;
  }

  public void finalise () {
    doFinalise();
    finaliseCosts();
    validateProperties();
  }

  public void validateProperties () {
    List<String> values = properties.finalise();
    if (!values.isEmpty()) {
      StringJoiner join = new StringJoiner(",");
      values.forEach(join::add);
      Roots.logger.error("Spell '" + name + "' property table has the following keys inserted but not fetched: |" + join.toString() + "|");
    }
  }

  public boolean finalised () {
    return finalised;
  }

  @Nullable
  protected EntitySpellBase spawnEntity(World world, BlockPos pos, Class<? extends EntitySpellBase> entity, @Nullable EntityPlayer player) {
    List<EntitySpellBase> pastRituals = world.getEntitiesWithinAABB(entity, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1), o -> o != null && o.getClass().equals(entity));
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
      spell.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
      if (player != null) {
        spell.setPlayer(player.getUniqueID());
      }
      world.spawnEntity(spell);
      return spell;
    }
    return null;
  }

  public static class SpellCost {
    public static SpellCost EMPTY = new SpellCost(null, 0);

    private String herb;
    private double cost;

    public SpellCost(String herb, double cost) {
      this.herb = herb;
      this.cost = cost;
    }

    public Herb getHerb () {
      return HerbRegistry.getHerbByName(herb);
    }

    public double getCost() {
      return cost;
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