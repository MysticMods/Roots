package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualFireStorm;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.properties.Property;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.TextFormatting;

public class RitualFireStorm extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(600);
  public static Property<Integer> PROP_PROJECTILE_COUNT = new Property<>("projectile_count", 40).setDescription("maximum number of projectiles flare thaumcraft.entities present during the ritual (checked every 2 ticks)");
  public static Property<Float> PROP_PROJECTILE_DAMAGE = new Property<>("projectile_damage", 4f).setDescription("damage dealt to hostile thaumcraft.entities by each ritual projectile");
  public static Property<Float> PROP_PROJECTILE_KNOCKBACK = new Property<>("projectile_knockback", 0.5f).setDescription("knockback dealt to hostile thaumcraft.entities by each ritual projectile");

  public float projectile_damage, projectile_knockback;
  public int projectile_count;

  public RitualFireStorm(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_PROJECTILE_COUNT, PROP_PROJECTILE_DAMAGE, PROP_PROJECTILE_KNOCKBACK);
    setEntityClass(EntityRitualFireStorm.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(ModItems.infernal_bulb),
        new ItemStack(ModItems.bark_acacia),
        Ingredient.fromItem(Items.COAL),
        new ItemStack(ModItems.bark_acacia),
        new ItemStack(Items.BLAZE_POWDER)
    );
    addCondition(new ConditionStandingStones(3, 1));
    addCondition(new ConditionStandingStones(4, 3));
    setIcon(ModItems.ritual_fire_storm);
    setColor(TextFormatting.RED);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    projectile_damage = properties.get(PROP_PROJECTILE_DAMAGE);
    projectile_count = properties.get(PROP_PROJECTILE_COUNT);
    projectile_knockback = properties.get(PROP_PROJECTILE_KNOCKBACK);
  }
}