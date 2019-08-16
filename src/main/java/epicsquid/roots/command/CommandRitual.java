package epicsquid.roots.command;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.recipe.conditions.Condition;
import epicsquid.roots.recipe.conditions.ConditionStandingStones;
import epicsquid.roots.recipe.conditions.ConditionTrees;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.util.ItemUtil;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

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

  private ItemStack resolveStack (Ingredient ing) {
    return ing.getMatchingStacks()[0].copy();
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    if (sender instanceof EntityPlayerMP && args.length != 0) {
      EntityPlayerMP player = (EntityPlayerMP) sender;
      String ritualName = args[0];
      if (!ritualName.startsWith("ritual")) {
        ritualName = "ritual_" + ritualName;
      }

      RitualBase ritual = RitualRegistry.getRitual(ritualName);

      if (ritual == null) {
        player.sendMessage(new TextComponentString("Invalid ritual: " + args[0]));
      }

      WorldServer world = player.getServerWorld();
      BlockPos pos = player.getPosition();

      world.setBlockState(pos.down(), Blocks.CHEST.getDefaultState());
      world.setBlockState(pos, ModBlocks.bonfire.getDefaultState());

      TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos.down());
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

      for (Condition condition : ritual.getConditions()) {
        if (condition instanceof ConditionStandingStones) {
          ConditionStandingStones stones = (ConditionStandingStones) condition;
          for (i = 0; i < stones.getAmount(); i++) {
            BlockPos base = pos.add(i + 1, 0, 0);
            for (int j = 0; j < stones.getHeight(); j++) {
              world.setBlockState(base.add(0, j, 0), j == stones.getHeight() - 1 ? ModBlocks.chiseled_runestone.getDefaultState() : ModBlocks.runestone.getDefaultState());
            }
          }
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
}
