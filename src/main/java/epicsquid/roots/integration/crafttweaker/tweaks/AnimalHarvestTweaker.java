package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.roots.AnimalHarvest")
@ZenDocAppend({"docs/include/animal_harvest.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".AnimalHarvest")
public class AnimalHarvestTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "entity", info = "the entity to generate drops for")
      },
      description = "Adds the specified entity to the Animal Harvest ritual list, making it eligible for drops. (Animal Harvest creatures form the default basis of Life Essence creation for Summon Creatures. See Summon Creatures for modifications of that derived list/emptying it.)"
  )
  @ZenMethod
  public static void addEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Add((EntityEntry) entity.getInternal()));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "entity", info = "the entity to stop generating drops for")
      },
      description = "Removes the specified entity from the Animal Harvest ritual list, preventing it from dropping anything."
  )
  @ZenMethod
  public static void removeEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Remove((EntityEntry) entity.getInternal()));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the type of fish"),
          @ZenDocArg(arg = "fish", info = "the type of fish as an item stack"),
          @ZenDocArg(arg = "weight", info = "the weight of the fish as an integer")
      },
      description = "Adds a specific item to the fish drop table, making it eligible to be dropped if there is a water source block in the radius of the ritual."
  )
  @ZenMethod
  public static void addFish(String name, IItemStack fish, int weight) {
    CraftTweaker.LATE_ACTIONS.add(new AddFish(name, CraftTweakerMC.getItemStack(fish), weight));
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "fish", info = "the type of fish to remove as an item stack")
      },
      description = "Removes a specific item from the fish drop table, preventing it from being dropped in water source thaumcraft.blocks."
  )
  @ZenMethod
  public static void removeFish(IItemStack fish) {
    CraftTweaker.LATE_ACTIONS.add(new RemoveFish(CraftTweakerMC.getItemStack(fish)));
  }

  private static class Remove extends Action {
    private final EntityEntry entry;

    public Remove(EntityEntry entry) {
      super("remove_animal_harvest");
      this.entry = entry;
    }

    @Override
    public void apply() {
      Class<? extends Entity> clz = entry.getEntityClass();
      ModRecipes.removeAnimalHarvestRecipe(clz);
    }

    @Override
    public String describe() {
      return String.format("Recipe to remove %s from AnimalHarvest", entry.getName());
    }
  }

  private static class Add extends Action {
    private final EntityEntry entry;

    public Add(EntityEntry entry) {
      super("add_animal_harvest");
      this.entry = entry;
      if (!EntityLivingBase.class.isAssignableFrom(this.entry.getEntityClass())) {
        CraftTweakerAPI.logError("Invalid Animal Harvest class to add: " + this.entry.getEntityClass().getSimpleName());
      }
    }

    @Override
    public void apply() {
      ModRecipes.addAnimalHarvestRecipe(entry.getName(), (Class<? extends EntityLivingBase>) entry.getEntityClass());
    }

    @Override
    public String describe() {
      return String.format("Recipe to add %s to AnimalHarvest", entry.getName());
    }
  }

  private static class AddFish extends Action {
    private final ItemStack stack;
    private final String name;
    private final int weight;

    public AddFish(String name, ItemStack stack, int weight) {
      super("add_animal_harvest_fish");
      this.name = name;
      this.stack = stack;
      this.weight = weight;
    }

    @Override
    public void apply() {
      ModRecipes.addAnimalHarvestFishRecipe(name, stack, weight);
    }

    @Override
    public String describe() {
      return String.format("Recipe to add %s to AnimalHarvest fish recipes", name);
    }
  }

  private static class RemoveFish extends Action {
    private final ItemStack stack;

    public RemoveFish(ItemStack stack) {
      super("remove_animal_harvest_fish");
      this.stack = stack;
    }

    @Override
    public void apply() {
      ModRecipes.removeAnimalHarvestFishRecipe(stack);
    }

    @Override
    public String describe() {
      return String.format("Recipe to remove %s from AnimalHarvest fish recipes", stack);
    }
  }
}
