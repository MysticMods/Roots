package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.roots.Pacifist")
@ZenDocAppend({"docs/include/pacifist.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Pacifist")
public class PacifistTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "entity", info = "the entity to add to the pacifist list")
      },
      description = "Allows the addition of new entities to the list of 'Pacifist' creatures, i.e., those that, when killed, will grant the Untrue Pacifist advancement."
  )
  @ZenMethod
  public static void addEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Add((EntityEntry) entity.getInternal()));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "entity", info = "the entity to remove from the pacifist list")
      },
      description = "Removes an entity from the list of 'Pacifist Creatures', i.e., those that, when killed, will grant the Untrue Pacfist advancement."
  )
  @ZenMethod
  public static void removeEntity(IEntityDefinition entity) {
    CraftTweaker.LATE_ACTIONS.add(new Remove((EntityEntry) entity.getInternal()));
  }

  private static class Remove extends Action {
    private final EntityEntry entry;

    public Remove(EntityEntry entry) {
      super("remove_pacifist");
      this.entry = entry;
    }

    @Override
    public void apply() {
      ModRecipes.removePacifistEntry(entry.getName());
    }

    @Override
    public String describe() {
      return String.format("Recipe to remove %s from Pacifist", entry.getName());
    }
  }

  private static class Add extends Action {
    private final EntityEntry entry;

    public Add(EntityEntry entry) {
      super("add_pacifist");
      this.entry = entry;
    }

    @Override
    public void apply() {
      ModRecipes.addPacifistEntry(entry.getName(), entry.getEntityClass());
    }

    @Override
    public String describe() {
      return String.format("Recipe to add %s to Pacifism", entry.getName());
    }
  }
}
