package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualFireStorm;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import epicsquid.roots.util.types.Property;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class RitualFireStorm extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(600);
  public static Property<Integer> PROP_PROJECTILE_COUNT = new Property<>("projectile_count", 40);
  public static Property<Float> PROP_PROJECTILE_DAMAGE = new Property<>("projectile_damage", 4f);
  public static Property<Float> PROP_PROJECTILE_KNOCKBACK = new Property<>("projectile_knockback", 0.5f);

  public float projectile_damage, projectile_knockback;
  public int projectile_count;

  public RitualFireStorm(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_PROJECTILE_COUNT, PROP_PROJECTILE_DAMAGE, PROP_PROJECTILE_KNOCKBACK);
  }

  @Override
  public void init () {
    addCondition(new ConditionItems(
        new ItemStack(ModItems.infernal_bulb),
        new ItemStack(ModItems.bark_acacia),
        new ItemStack(Items.COAL),
        new ItemStack(ModItems.bark_acacia),
        new ItemStack(Items.BLAZE_POWDER)
    ));
    addCondition(new ConditionStandingStones(3, 3));
    setIcon(ModItems.ritual_fire_storm);
    setColor(TextFormatting.RED);
    setBold(true);
  }

  @Override
  public void finalise() {
    projectile_damage = properties.getProperty(PROP_PROJECTILE_DAMAGE);
    projectile_count = properties.getProperty(PROP_PROJECTILE_COUNT);
    projectile_knockback = properties.getProperty(PROP_PROJECTILE_KNOCKBACK);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos) {
    return this.spawnEntity(world, pos, EntityRitualFireStorm.class);
  }
}