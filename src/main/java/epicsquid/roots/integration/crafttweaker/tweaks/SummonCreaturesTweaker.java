package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.IIngredient;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ZenDocClass("mods.roots.SummonCreatures")
@ZenDocAppend({"docs/include/summon_creatures.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".SummonCreatures")
public class SummonCreaturesTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "entity", info = "the entity to be summoned"),
          @ZenDocArg(arg = "ingredients", info = "a list of ingredients used for the summoning")
      }
  )
  @ZenMethod
  public static void addEntity(IEntityDefinition entity, IIngredient[] ingredients) {
    CraftTweaker.LATE_ACTIONS.add(new Add((EntityEntry) entity.getInternal(), Stream.of(ingredients).map(CraftTweakerMC::getIngredient).collect(Collectors.toList())));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "entity", info = "the entity to remove from summoning via recipe")
      }
  )
  @ZenMethod
  public static void removeEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Remove((EntityEntry) entity.getInternal()));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "entity", info = "the entity to disable life essence for")
      }
  )
  public static void blacklistEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Blacklist((EntityEntry) entity.getInternal()));
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
      ModRecipes.removeSummonCreatureEntry(clz);
    }

    @Override
    public String describe() {
      return String.format("Recipe to remove %s from SummonCreatures", entry.getName());
    }
  }

  private static class Blacklist extends Action {
    private final EntityEntry entry;

    public Blacklist(EntityEntry entry) {
      super("blacklist_animal_harvest");
      this.entry = entry;
    }

    @Override
    public void apply() {
      Class<? extends Entity> clz = entry.getEntityClass();
      ModRecipes.blacklistLifeEssenceClass(clz);
    }

    @Override
    public String describe() {
      return String.format("Recipe to blacklist %s from SummonCreatures and Life Essence", entry.getName());
    }
  }

  private static class Add extends Action {
    private final EntityEntry entry;

    public Add(EntityEntry entry, List<Ingredient> ingredients) {
      super("add_summon_creature");
      this.entry = entry;
    }

    @Override
    public void apply() {
      ModRecipes.addAnimalHarvestRecipe(entry.getName(), entry.getEntityClass());
    }

    @Override
    public String describe() {
      return String.format("Recipe to add %s to AnimalHarvest", entry.getName());
    }
  }
}
