package epicsquid.roots.ritual;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualSummonCreatures;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.properties.Property;
import epicsquid.roots.recipe.SummonCreatureRecipe;
import epicsquid.roots.ritual.conditions.ConditionRunedPillars;
import epicsquid.roots.ritual.conditions.ConditionValidSummon;
import epicsquid.roots.tileentity.TileEntityCatalystPlate;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RitualSummonCreatures extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(200);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 3).setDescription("Radius on the X Axis of the square area in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 5).setDescription("Radius on the Y Axis of the square area in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 3).setDescription("Radius on the Z Axis of the square area in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(150).setDescription("interval in ticks between each summoned creature");
  public static Property<Integer> PROP_TRIES = new Property<>("tries", 10).setDescription("number of attempts at finding a random good (satisfying the conditions) position to spawn the creature");
  public static Property<Integer> PROP_GLOW_DURATION = new Property<>("glow_duration", 30).setDescription("the duration at which newly spawned and summoned thaumcraft.entities will glow at (in ticks)");

  public int radius_x, radius_y, radius_z, tries, interval, glow_duration;

  public RitualSummonCreatures(String name, boolean disabled) {
    super(name, disabled);
    properties.add(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Y, PROP_RADIUS_Z, PROP_INTERVAL, PROP_TRIES, PROP_GLOW_DURATION);
    setEntityClass(EntityRitualSummonCreatures.class);
  }

  @Override
  public void init() {
    recipe = new RitualRecipe(this,
        new ItemStack(Items.WHEAT_SEEDS),
        new OreIngredient("cropWheat"),
        new OreIngredient("egg"),
        new ItemStack(Items.ROTTEN_FLESH),
        new ItemStack(Items.WHEAT_SEEDS)
    );
    addCondition(new ConditionValidSummon());
    setIcon(ModItems.ritual_summon_creatures);
    setColor(TextFormatting.DARK_PURPLE);
    setBold(true);
  }

  @Override
  public void doFinalise() {
    duration = properties.get(PROP_DURATION);
    radius_x = properties.get(PROP_RADIUS_X);
    radius_z = properties.get(PROP_RADIUS_Z);
    radius_y = properties.get(PROP_RADIUS_Y);
    interval = properties.get(PROP_INTERVAL);
    tries = properties.get(PROP_TRIES);
    glow_duration = properties.get(PROP_GLOW_DURATION);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos, @Nullable EntityPlayer player) {
    EntityRitualSummonCreatures entity = (EntityRitualSummonCreatures) this.spawnEntity(world, pos, EntityRitualSummonCreatures.class, player);
    if (!world.isRemote) {
      List<TileEntityCatalystPlate> plates = RitualUtil.getNearbyCatalystPlates(world, pos);
      List<ItemStack> plateItems = RitualUtil.getItemsFromNearbyPlates(plates);

      SummonCreatureRecipe recipe = ModRecipes.findSummonCreatureEntry(plateItems);
      List<ItemStack> ingredients = new ArrayList<>();
      if (recipe != null) {
        for (TileEntityCatalystPlate plate : plates) {
          ingredients.add(plate.removeItem());
        }
      }
      ItemStack essence = ItemStack.EMPTY;
      if (recipe == null) {
        for (TileEntityCatalystPlate plate : plates) {
          ItemStack stack = plate.getHeldItem();
          if (stack.getItem() == ModItems.life_essence) {
            essence = stack;
            plate.removeItem();
            break;
          }
        }
      }
      if (!ingredients.isEmpty()) {
        for (ItemStack stack : recipe.transformIngredients(ingredients, null)) {
          ItemUtil.spawnItem(world, pos.add(random.nextBoolean() ? -1 : 1, 1, random.nextBoolean() ? -1 : 1), stack);
        }
      }

      if (entity != null) {
        entity.setEssence(essence);
        entity.setSummonRecipe(recipe);
      }
    }

    return entity;
  }
}
