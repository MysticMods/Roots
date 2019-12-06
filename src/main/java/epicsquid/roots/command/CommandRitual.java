/*package epicsquid.roots.command;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.ritual.conditions.Condition;
import epicsquid.roots.ritual.conditions.ConditionStandingStones;
import epicsquid.roots.ritual.conditions.ConditionTrees;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.ServerWorld;

import java.util.Collections;
import java.util.List;

public class CommandRitual extends CommandBase {
  public CommandRitual() {
  }

  @Override
  public String getName() {
    return "ritual";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/ritual <ritual name>";
  }

  @Override
  public List<String> getAliases() {
    return Collections.singletonList("ritual");
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  private ItemStack resolveStack(Ingredient ing) {
    return ing.getMatchingStacks()[0].copy();
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    if (sender instanceof ServerPlayerEntity && args.length != 0) {
      ServerPlayerEntity player = (ServerPlayerEntity) sender;
      String ritualName = args[0];
      if (!ritualName.startsWith("ritual")) {
        ritualName = "ritual_" + ritualName;
      }

      RitualBase ritual = RitualRegistry.getRitual(ritualName);

      if (ritual == null || ritual.isDisabled()) {
        player.sendMessage(new StringTextComponent("Invalid or disabled ritual: " + args[0]));
        return;
      }

      ServerWorld world = player.getServerWorld();
      BlockPos pos = player.getPosition();

      world.setBlockState(pos.down(), Blocks.CHEST.getDefaultState());
      world.setBlockState(pos, ModBlocks.bonfire.getDefaultState());

      ChestTileEntity chest = (ChestTileEntity) world.getTileEntity(pos.down());
      TileEntityBonfire bonfire = (TileEntityBonfire) world.getTileEntity(pos);
      bonfire.setLastRitualUsed(ritual);

      int i = 0;
      for (Ingredient ing : ritual.getIngredients()) {
        bonfire.inventory.setStackInSlot(i, resolveStack(ing));
        for (int j = i * 5; j < i * 5 + 5; j++) {
          ItemStack stack = resolveStack(ing);
          stack.setCount(stack.getMaxStackSize());
          chest.setInventorySlotContents(j, stack);
        }
        i++;
      }

      ItemStack flint = new ItemStack(Items.FLINT_AND_STEEL);
      if (!player.addItemStackToInventory(flint)) {
        ItemUtil.spawnItem(world, pos, flint);
      }

      int runePos = 0;
      for (Condition condition : ritual.getConditions()) {
        if (condition instanceof ConditionStandingStones) {
          ConditionStandingStones stones = (ConditionStandingStones) condition;
          for (i = 0; i < stones.getAmount(); i++) {
            BlockPos base = pos.add(runePos + i + 1, 0, 0);
            for (int j = 0; j < stones.getHeight(); j++) {
              world.setBlockState(base.add(0, j, 0), j == stones.getHeight() - 1 ? ModBlocks.chiseled_runestone.getDefaultState() : ModBlocks.runestone.getDefaultState());
            }
          }
          runePos += stones.getAmount();
        } else if (condition instanceof ConditionTrees) {
          ConditionTrees trees = (ConditionTrees) condition;
          BlockPlanks.EnumType type = trees.getTreeType();
          for (i = 0; i < trees.getAmount(); i++) {
            BlockPos base = pos.add(0, 1, i + 1);
            if (type.getMetadata() >= 4) {
              world.setBlockState(base, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, type));
            } else {
              world.setBlockState(base, Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, type));
            }
          }
        }
      }
    }
  }
}*/
