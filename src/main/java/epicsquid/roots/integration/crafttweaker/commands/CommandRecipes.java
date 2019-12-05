package epicsquid.roots.integration.crafttweaker.commands;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Map;

public class CommandRecipes extends CraftTweakerCommand {
  public CommandRecipes() {
    super("roots_recipes");
  }

  private enum SubCommand {
    all, animal_harvest, bark, fey_crafting, flower_growth, mortar, pacifist, pyre_crafting, runic_shears;
  }

  @Override
  protected void init() {
    setDescription(new TranslationTextComponent("roots.commands.dump_recipes.desc"));
  }

  @Override
  public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
    if (args.length < 1) {
      sender.sendMessage(new TranslationTextComponent("roots.commands.dump_recipes.usage"));
      return;
    }
    Optional<SubCommand> command = Enums.getIfPresent(SubCommand.class, args[0]);
    if (command.isPresent()) {
      switch (command.get()) {
        case all:
        case animal_harvest:
          CraftTweakerAPI.getLogger().logInfo("Animal Harvest recipes:");
          for (Map.Entry<ResourceLocation, AnimalHarvestRecipe> entry : ModRecipes.getAnimalHarvestRecipes().entrySet()) {
            CraftTweakerAPI.getLogger().logInfo("  Key: " + entry.getKey().toString() + " for class: " + entry.getValue().getHarvestClass().getSimpleName());
          }
          CraftTweakerAPI.getLogger().logInfo("Animal Harvest Fish recipes:");
          for (Map.Entry<ResourceLocation, AnimalHarvestFishRecipe> entry : ModRecipes.getAnimalHarvestFishRecipes().entrySet()) {
            CraftTweakerAPI.getLogger().logInfo("  Key: " + entry.getKey().toString() + " for item: " + entry.getValue().getItemStack().toString() + " with weight: " + entry.getValue().getWeight());
          }
          if (command.get() == SubCommand.animal_harvest) {
            break;
          }
        case bark:
          CraftTweakerAPI.getLogger().logInfo("Bark recipes:");
          for (Map.Entry<ResourceLocation, BarkRecipe> entry : ModRecipes.getBarkRecipeMap().entrySet()) {
            BarkRecipe recipe = entry.getValue();
            if (recipe.getType() == null) {
              CraftTweakerAPI.getLogger().logInfo("  Modded Bark Recipe: " + entry.getKey().toString() + " converting " + recipe.getBlockStack() + " into " + recipe.getBarkStack(1));
            } else {
              CraftTweakerAPI.getLogger().logInfo("  Vanilla Bark Recipe: " + entry.getKey().toString() + " converting " + recipe.getType().getName() + " into " + recipe.getBarkStack(1));
            }
          }
          if (command.get() == SubCommand.bark) {
            break;
          }
        case fey_crafting:
          CraftTweakerAPI.getLogger().logInfo("Fey Crafting recipes:");
          for (Map.Entry<ResourceLocation, FeyCraftingRecipe> entry : ModRecipes.getFeyCraftingRecipes().entrySet()) {
            FeyCraftingRecipe recipe = entry.getValue();
            CraftTweakerAPI.getLogger().logInfo("  Key: " + entry.getKey().toString() + " produces " + recipe.getResult().toString());
          }
          if (command.get() == SubCommand.fey_crafting) {
            break;
          }
        case flower_growth:
          CraftTweakerAPI.getLogger().logInfo("Flower Growth recipes:");
          for (Map.Entry<ResourceLocation, FlowerRecipe> entry : ModRecipes.getFlowerRecipes().entrySet()) {
            FlowerRecipe recipe = entry.getValue();
            CraftTweakerAPI.getLogger().logInfo("  Key: " + entry.getKey().toString() + " linked to: " + recipe.getFlower().toString());
          }
          if (command.get() == SubCommand.flower_growth) {
            break;
          }
        case mortar:
          CraftTweakerAPI.getLogger().logInfo("Mortar recipes: ");
          for (MortarRecipe recipe : ModRecipes.getMortarRecipes()) {
            CraftTweakerAPI.getLogger().logInfo("  Result: " + recipe.getResult().toString());
          }
          if (command.get() == SubCommand.mortar) {
            break;
          }
        case pacifist:
          CraftTweakerAPI.getLogger().logInfo("Pacifist recipes:");
          for (Map.Entry<ResourceLocation, PacifistEntry> entry : ModRecipes.getPacifistEntities().entrySet()) {
            CraftTweakerAPI.getLogger().logInfo("  Key: " + entry.getKey().toString() + " for class: " + entry.getValue().getEntityClass().getSimpleName());
          }
          if (command.get() == SubCommand.pacifist) {
            break;
          }
        case pyre_crafting:
          CraftTweakerAPI.getLogger().logInfo("Pyre Crafting recipes:");
          for (Map.Entry<String, PyreCraftingRecipe> entry : ModRecipes.getPyreCraftingRecipes().entrySet()) {
            CraftTweakerAPI.getLogger().logInfo("  Key: roots:" + entry.getKey() + " with output: " + entry.getValue().getResult().toString());
          }
          if (command.get() == SubCommand.pyre_crafting) {
            break;
          }
        case runic_shears:
          CraftTweakerAPI.getLogger().logInfo("Runic Shears recipes:");
          for (Map.Entry<ResourceLocation, RunicShearRecipe> entry : ModRecipes.getRunicShearRecipes().entrySet()) {
            CraftTweakerAPI.getLogger().logInfo("  Key: " + entry.getKey().toString() + " with the result of " + entry.getValue().getDrop().toString());
          }
          CraftTweakerAPI.getLogger().logInfo("Runic Shears Entity recipes:");
          for (Map.Entry<ResourceLocation, RunicShearEntityRecipe> entry : ModRecipes.getRunicShearEntityRecipes().entrySet()) {
            CraftTweakerAPI.getLogger().logInfo("  Key: " + entry.getKey().toString() + " with the result of " + entry.getValue().getDrop().toString() + " for entity with class: " + entry.getValue().getClazz().getSimpleName());
          }
          if (command.get() == SubCommand.runic_shears) {
            break;
          }
      }
    }
  }
}
