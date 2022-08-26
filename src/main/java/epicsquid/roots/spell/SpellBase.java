package epicsquid.roots.spell;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.api.Herb;
import epicsquid.roots.handler.SpellHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.PowderInventoryUtil;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SpellBase {
  private float red1, green1, blue1;
  private float red2, green2, blue2;
  private String name;
  protected int cooldown = 20;

  private TextFormatting textColor;
  protected EnumCastType castType = EnumCastType.INSTANTANEOUS;
  private Object2DoubleOpenHashMap<Herb> costs = new Object2DoubleOpenHashMap<>();
  private List<Ingredient> ingredients = new ArrayList<>();
  private List<SpellModule> acceptedModules = new ArrayList<>();

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
  }

  public boolean hasModules () {
    return !acceptedModules.isEmpty();
  }

  public SpellBase acceptModules(SpellModule ... modules) {
    assert modules.length < 5;
    acceptedModules.addAll(Arrays.asList(modules));
    return this;
  }

  public List<SpellModule> getModules() {
    return acceptedModules;
  }

  public SpellBase addIngredients(Object... stacks) {
    for (Object stack : stacks) {
      if (stack instanceof ItemStack) {
        ingredients.add(Ingredient.fromStacks((ItemStack) stack));
      } else if (stack instanceof Ingredient) {
        ingredients.add((Ingredient) stack);
      }
    }
    return this;
  }

  public boolean costsMet(EntityPlayer player) {
    boolean matches = true;
    for(Map.Entry<Herb, Double> entry : this.costs.entrySet()){
      Herb herb = entry.getKey();
      double d = entry.getValue();
      if (matches) {
        double r = PowderInventoryUtil.getPowderTotal(player, herb);
        matches = r >= d;
        if (!matches && !player.isCreative()) {
          player.sendStatusMessage(new TextComponentTranslation("roots.info.pouch.no_herbs", new TextComponentTranslation(String.format("item.%s.name", herb.getName()))), true);
        }
      }
    }
    return matches && costs.size() > 0 || player.capabilities.isCreativeMode;
  }

  public void enactCosts(EntityPlayer player) {
    for(Map.Entry<Herb, Double> entry : this.costs.entrySet()){
      Herb herb = entry.getKey();
      double d = entry.getValue();
      PowderInventoryUtil.removePowder(player, herb, d);
    }
  }

  public void enactTickCosts(EntityPlayer player) {
    for(Map.Entry<Herb, Double> entry : this.costs.entrySet()){
      Herb herb = entry.getKey();
      double d = entry.getValue();
      PowderInventoryUtil.removePowder(player, herb, d / 20.0);
    }
  }

  @SideOnly(Side.CLIENT)
  public void addToolTip(List<String> tooltip) {
    String prefix = "roots.spell." + name;
    tooltip.add("" + textColor + TextFormatting.BOLD + I18n.format(prefix + ".name") + TextFormatting.RESET);
    for(Map.Entry<Herb, Double> entry : this.costs.entrySet()) {
      Herb herb = entry.getKey();
      String d = String.format("%.4f", entry.getValue());
      tooltip.add(I18n.format(herb.getItem().getTranslationKey() + ".name") + I18n.format("roots.tooltip.pouch_divider") + d);
    }
  }

  private List<ItemStack> moduleItems = null;

  @SideOnly(Side.CLIENT)
  public List<ItemStack> getModuleStacks () {
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

  public SpellBase addCost(Herb herb, double amount) {
    if (herb == null) {
      System.out.println("Spell - " + this.getClass().getName() + " - added a null herb ingredient. This is a bug.");
      return this;
    }
    costs.put(herb, amount);
    return this;
  }

  public boolean matchesIngredients(List<ItemStack> ingredients) {
    return ListUtil.matchesIngredients(ingredients, this.ingredients);
  }

  public abstract boolean cast(EntityPlayer caster, List<SpellModule> modules);

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
    return ingredients;
  }

  public void setIngredients (List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public ItemStack getResult () {
    ItemStack stack = new ItemStack(ModItems.spell_dust);
    SpellHandler.fromStack(stack).setSpellToSlot(this);
    return stack;
  }

  public List<ItemStack> getCostItems () {
    return costs.keySet().stream().map((herb) -> new ItemStack(herb.getItem())).collect(Collectors.toList());
  }
}