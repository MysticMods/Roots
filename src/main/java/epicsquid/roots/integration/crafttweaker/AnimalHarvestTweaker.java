package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.entity.Entity;
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
          @ZenDocArg(arg="entity", info="the entity to generate drops for")
      }
  )
  @ZenMethod
  public static void addEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Add((EntityEntry) entity.getInternal()));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg="entity", info="the entity to stop generating drops for")
      }
  )
  @ZenMethod
  public static void removeEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Remove((EntityEntry) entity.getInternal()));
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
    public String describe () {
      return String.format("Recipe to remove %s from AnimalHarvest", entry.getName());
    }
  }

  private static class Add extends Action {
    private final EntityEntry entry;

    public Add(EntityEntry entry) {
      super("add_animal_harvest");
      this.entry = entry;
    }

    @Override
    public void apply() {
      ModRecipes.addAnimalHarvestRecipe(entry.getName(), entry.getEntityClass());
    }

    @Override
    public String describe () {
      return String.format("Recipe to add %s to AnimalHarvest", entry.getName());
    }
  }
}
