package epicsquid.roots.integration.crafttweaker.commands;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.getLinkToCraftTweakerLog;

public class CommandRecipes extends CraftTweakerCommand {
  public CommandRecipes() {
    super("roots_recipes");
  }

  private enum SubCommand {
    all, animal_harvest, bark, fey_crafting, flower_growth, mortar, pacifist, pyre_crafting, runic_shears, life_essence
  }
  
  @Override
  public List<String> getSubSubCommand(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return Arrays.stream(SubCommand.values()).map(Enum::toString).collect(Collectors.toList());
  }
  
  @Override
  protected void init() {
    setDescription(new TextComponentTranslation("roots.commands.dump_recipes.desc"));
  }

  @Override
  public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
    if (args.length < 1) {
      sender.sendMessage(new TextComponentTranslation("roots.commands.dump_recipes.usage"));
      return;
    }
    Optional<SubCommand> command = Enums.getIfPresent(SubCommand.class, args[0]);
    if (command.isPresent()) {
      switch (command.get()) {
        case all:
        case animal_harvest:
          CraftTweakerAPI.logCommand("Animal Harvest recipes:");
          for (Map.Entry<ResourceLocation, AnimalHarvestRecipe> entry : ModRecipes.getAnimalHarvestRecipes().entrySet()) {
            CraftTweakerAPI.logCommand("  Key: " + entry.getKey().toString() + " for class: " + entry.getValue().getHarvestClass().getSimpleName());
          }
          CraftTweakerAPI.logCommand("Animal Harvest Fish recipes:");
          for (Map.Entry<ResourceLocation, AnimalHarvestFishRecipe> entry : ModRecipes.getAnimalHarvestFishRecipes().entrySet()) {
            CraftTweakerAPI.logCommand("  Key: " + entry.getKey().toString() + " for item: " + entry.getValue().getItemStack().toString() + " with weight: " + entry.getValue().getWeight());
          }
          if (command.get() == SubCommand.animal_harvest) {
            break;
          }
        case bark:
          CraftTweakerAPI.logCommand("Bark recipes:");
          for (Map.Entry<ResourceLocation, BarkRecipe> entry : ModRecipes.getBarkRecipeMap().entrySet()) {
            BarkRecipe recipe = entry.getValue();
            if (recipe.getType() == null) {
              CraftTweakerAPI.logCommand("  Modded Bark Recipe: " + entry.getKey().toString() + " converting " + recipe.getBlockStack() + " into " + recipe.getBarkStack(1));
            } else {
              CraftTweakerAPI.logCommand("  Vanilla Bark Recipe: " + entry.getKey().toString() + " converting " + recipe.getType().getName() + " into " + recipe.getBarkStack(1));
            }
          }
          if (command.get() == SubCommand.bark) {
            break;
          }
        case fey_crafting:
          CraftTweakerAPI.logCommand("Fey Crafting recipes:");
          for (Map.Entry<ResourceLocation, FeyCraftingRecipe> entry : ModRecipes.getFeyCraftingRecipes().entrySet()) {
            FeyCraftingRecipe recipe = entry.getValue();
            CraftTweakerAPI.logCommand("  Key: " + entry.getKey().toString() + " produces " + recipe.getResult().toString());
          }
          if (command.get() == SubCommand.fey_crafting) {
            break;
          }
        case flower_growth:
          CraftTweakerAPI.logCommand("Flower Growth recipes:");
          for (Map.Entry<ResourceLocation, FlowerRecipe> entry : ModRecipes.getFlowerRecipes().entrySet()) {
            FlowerRecipe recipe = entry.getValue();
            CraftTweakerAPI.logCommand("  Key: " + entry.getKey().toString() + " linked to: " + recipe.getFlower().toString());
          }
          if (command.get() == SubCommand.flower_growth) {
            break;
          }
        case mortar:
          CraftTweakerAPI.logCommand("Mortar recipes: ");
          for (MortarRecipe recipe : ModRecipes.getMortarRecipes()) {
            CraftTweakerAPI.logCommand("  Result: " + recipe.getResult().toString());
          }
          if (command.get() == SubCommand.mortar) {
            break;
          }
        case pacifist:
          CraftTweakerAPI.logCommand("Pacifist recipes:");
          for (Map.Entry<ResourceLocation, PacifistEntry> entry : ModRecipes.getPacifistEntities().entrySet()) {
            CraftTweakerAPI.logCommand("  Key: " + entry.getKey().toString() + " for class: " + entry.getValue().getEntityClass().getSimpleName());
          }
          if (command.get() == SubCommand.pacifist) {
            break;
          }
        case pyre_crafting:
          CraftTweakerAPI.logCommand("Pyre Crafting recipes:");
          for (Map.Entry<ResourceLocation, PyreCraftingRecipe> entry : ModRecipes.getPyreCraftingRecipes().entrySet()) {
            CraftTweakerAPI.logCommand("  Key: roots:" + entry.getKey() + " with output: " + entry.getValue().getResult().toString());
          }
          if (command.get() == SubCommand.pyre_crafting) {
            break;
          }
        case runic_shears:
          CraftTweakerAPI.logCommand("Runic Shears recipes:");
          for (Map.Entry<ResourceLocation, RunicShearRecipe> entry : ModRecipes.getRunicShearRecipes().entrySet()) {
            CraftTweakerAPI.logCommand("  Key: " + entry.getKey().toString() + " with the result of " + entry.getValue().getDrop().toString());
          }
          CraftTweakerAPI.logCommand("Runic Shears Entity recipes:");
          for (Map.Entry<ResourceLocation, RunicShearEntityRecipe> entry : ModRecipes.getRunicShearEntityRecipes().entrySet()) {
            CraftTweakerAPI.logCommand("  Key: " + entry.getKey().toString() + " with the result of " + entry.getValue().getDrop().toString() + " for entity with class: " + entry.getValue().getClazz().getSimpleName());
          }
          if (command.get() == SubCommand.runic_shears) {
            break;
          }
        case life_essence:
          CraftTweakerAPI.logCommand("Life Essence reipes:");
          for (Class<? extends EntityLivingBase> clazz : ModRecipes.getLifeEssenceList()) {
            CraftTweakerAPI.logCommand("  Entity: " + clazz.getSimpleName());
          }
          if (command.get() == SubCommand.life_essence) {
            break;
          }
      }
      sender.sendMessage(getLinkToCraftTweakerLog("Dumped to CraftTweaker log.", sender));
    }
  }
}
