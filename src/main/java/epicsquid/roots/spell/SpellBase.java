package epicsquid.roots.spell;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.entity.spell.EntitySpellBase;
import epicsquid.roots.modifiers.BaseModifiers;
import epicsquid.roots.modifiers.instance.ModifierInstance;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.modifier.Modifier;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.modifiers.modifier.ModifierList;
import epicsquid.roots.recipe.IRootsRecipe;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.tileentity.TileEntityMortar;
import epicsquid.roots.util.ClientHerbUtil;
import epicsquid.roots.util.ServerHerbUtil;
import epicsquid.roots.util.types.Property;
import epicsquid.roots.util.types.PropertyTable;
import epicsquid.roots.util.types.RegistryItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
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
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public abstract class SpellBase extends RegistryItem {
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
  private List<Modifier> acceptedModifiers = new ArrayList<>();
  private ModifierList modifierList = null;
  private float[] firstColours;
  private float[] secondColours;

  public SpellRecipe recipe = SpellRecipe.EMPTY;

  public enum EnumCastType {
    INSTANTANEOUS, CONTINUOUS
  }

  public SpellBase(ResourceLocation name, TextFormatting textColor, float r1, float g1, float b1, float r2, float g2, float b2) {
    setRegistryName(name);
    defaultModifiers();
    this.name = name.getPath();
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

  public abstract void init();

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public boolean hasModules() {
    return !acceptedModifiers.isEmpty();
  }

  public PropertyTable getProperties() {
    return properties;
  }

  public SpellBase acceptsModifiers(Modifier... modules) {
    acceptedModifiers.addAll(Arrays.asList(modules));
    return this;
  }

  public ModifierList getModifierList() {
    if (modifierList == null) {
      modifierList = new ModifierList(this);
    }
    return modifierList;
  }

  public List<Modifier> getModifiers () {
    return acceptedModifiers;
  }

  public void setRecipe(SpellRecipe recipe) {
    this.recipe = recipe;
  }

  public SpellBase addIngredients(Object... stacks) {
    this.recipe = new SpellRecipe(stacks);
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

  public SpellBase addCost(SpellCost cost) {
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

  public enum CastResult {
    FAIL,
    SUCCESS,
    SUCCESS_SPEEDY,
    SUCCESS_GREATER_SPEEDY;

    public boolean isSuccess () {
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

  public CastResult cast (EntityPlayer caster, StaffSpellInfo info, int ticks) {
    ModifierInstanceList modifiers = info.getModifiers();
    double amplifier = 0;
    ModifierInstance mod = modifiers.get(BaseModifiers.EMPOWER);
    if (mod != null && mod.isEnabled()) {
      amplifier = 0.1;
    }
    mod = modifiers.get(BaseModifiers.GREATER_EMPOWER);
    if (mod != null && mod.isEnabled()) {
      amplifier = 0.3;
    }
    double speedy = 0;
    mod = modifiers.get(BaseModifiers.SPEEDY);
    if (mod != null && mod.isEnabled()) {
      speedy = 0.1;
    }
    mod = modifiers.get(BaseModifiers.GREATER_SPEEDY);
    if (mod != null && mod.isEnabled()) {
      speedy = 0.3;
    }

    if (cast(caster, info.getModifiers(), ticks, amplifier, speedy)) {
      if (speedy == 0d) {
        return CastResult.SUCCESS;
      } else if (speedy == 0.1d) {
        return CastResult.SUCCESS_SPEEDY;
      } else {
        return CastResult.SUCCESS_GREATER_SPEEDY;
      }
    } else {
      return CastResult.FAIL;
    }
  }

  protected abstract boolean cast(EntityPlayer caster, ModifierInstanceList modifiers, int ticks, double amplifier, double speedy);

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
    DustSpellStorage.fromStack(stack).setSpellToSlot(this);
    return stack;
  }

  public List<ItemStack> getCostItems() {
    return costs.keySet().stream().map((herb) -> new ItemStack(herb.getItem())).collect(Collectors.toList());
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
    this.finalised = true;
  }

  public void defaultModifiers () {
    acceptsModifiers(BaseModifiers.EMPOWER, BaseModifiers.GREATER_EMPOWER, BaseModifiers.SPEEDY, BaseModifiers.GREATER_SPEEDY, BaseModifiers.REDUCTION, BaseModifiers.GREATER_REDUCTION);
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
    List<EntitySpellBase> pastRituals = world.getEntitiesWithinAABB(entity, new AxisAlignedBB(pos.x, pos.y, pos.z - 100, pos.x + 1, pos.y + 100, pos.z + 1), o -> o != null && o.getClass().equals(entity));
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
      spell.setAmplifier(amplifier);
      spell.setSpeedy(speedy);
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

    public Herb getHerb() {
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