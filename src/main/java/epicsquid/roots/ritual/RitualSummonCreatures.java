package epicsquid.roots.ritual;

import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.entity.ritual.EntityRitualSummonCreatures;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.SummonCreatureRecipe;
import epicsquid.roots.ritual.conditions.ConditionItems;
import epicsquid.roots.ritual.conditions.ConditionValidSummon;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import epicsquid.roots.util.RitualUtil;
import epicsquid.roots.util.types.Property;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.List;

public class RitualSummonCreatures extends RitualBase {
  public static Property.PropertyDuration PROP_DURATION = new Property.PropertyDuration(200);
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 3).setDescription("Radius on the X Axis of the square area in which the ritual takes place");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 3).setDescription("Radius on the Z Axis of the square area in which the ritual takes place");
  public static Property.PropertyInterval PROP_INTERVAL = new Property.PropertyInterval(150);
  public static Property<Integer> PROP_TRIES = new Property<>("tries", 10).setDescription("number of attempts at finding a random good (satisfying the conditions) position to spawn the creature");

  public int radius_x, radius_z, tries, interval;

  public RitualSummonCreatures(String name, boolean disabled) {
    super(name, disabled);
    properties.addProperties(PROP_DURATION, PROP_RADIUS_X, PROP_RADIUS_Z, PROP_INTERVAL, PROP_TRIES);
    setEntityClass(EntityRitualSummonCreatures.class);
  }

  @Override
  public void init() {
    addCondition(new ConditionItems(
        new ItemStack(Items.WHEAT_SEEDS),
        new OreIngredient("cropWheat"),
        new OreIngredient("egg"),
        new ItemStack(Items.ROTTEN_FLESH),
        new ItemStack(Items.WHEAT_SEEDS)
    ));
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
    interval = properties.get(PROP_INTERVAL);
    tries = properties.get(PROP_TRIES);
  }

  @Override
  public EntityRitualBase doEffect(World world, BlockPos pos, @Nullable EntityPlayer player) {
    EntityRitualSummonCreatures entity = (EntityRitualSummonCreatures) this.spawnEntity(world, pos, EntityRitualSummonCreatures.class, player);
    if (!world.isRemote) {
      List<TileEntityOffertoryPlate> plates = RitualUtil.getNearbyOfferingPlates(world, pos);
      List<ItemStack> plateItems = RitualUtil.getItemsFromNearbyPlates(plates);

      SummonCreatureRecipe recipe = ModRecipes.findSummonCreatureEntry(plateItems);
      if (recipe != null) {
        for (TileEntityOffertoryPlate plate : plates) {
          plate.removeItem();
        }
      }
      ItemStack essence = ItemStack.EMPTY;
      if (recipe == null) {
        for (TileEntityOffertoryPlate plate : plates) {
          ItemStack stack = plate.getHeldItem();
          if (stack.getItem() == ModItems.life_essence) {
            essence = stack;
            plate.removeItem();
            break;
          }
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
