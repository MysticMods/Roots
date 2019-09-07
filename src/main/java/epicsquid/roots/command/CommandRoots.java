package epicsquid.roots.command;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.network.MessageClearToasts;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import vazkii.patchouli.common.handler.AdvancementSyncHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRoots extends CommandBase {
  public CommandRoots() {
  }

  @Override
  public String getName() {
    return "roots";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/roots book | /roots growables";
  }

  @Override
  public List<String> getAliases() {
    return Collections.singletonList("roots");
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    if (sender instanceof EntityPlayerMP && args.length != 0) {
      EntityPlayerMP player = (EntityPlayerMP) sender;
      WorldServer world = player.getServerWorld();
      if (args[0].equalsIgnoreCase("book")) {
        AdvancementManager manager = world.getAdvancementManager();
        PlayerAdvancements advancements = player.getAdvancements();
        for (Advancement adv : manager.getAdvancements()) {
          ResourceLocation id = adv.getId();
          if (id.getNamespace().equals(Roots.MODID)) {
            AdvancementProgress progress = advancements.getProgress(adv);
            for (String criterion : progress.getRemaningCriteria()) {
              progress.grantCriterion(criterion);
            }
            adv.getRewards().apply(player);
          }
        }
        advancements.save();
        advancements.flushDirty(player);
        MessageClearToasts message = new MessageClearToasts();
        PacketHandler.INSTANCE.sendTo(message, player);
        Item guide = Item.REGISTRY.getObject(new ResourceLocation("patchouli", "guide_book"));
        ItemStack book = new ItemStack(guide);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("patchouli:book", "roots:roots_guide");
        book.setTagCompound(tag);
        if (!player.addItemStackToInventory(book)) {
          ItemUtil.spawnItem(world, player.getPosition(), book);
        }
        AdvancementSyncHandler.syncPlayer(player, false);
      } else if (args[0].equalsIgnoreCase("growables")) {
        List<String> growablesList = new ArrayList<>();
        for (Block block : Block.REGISTRY) {
          if (block instanceof IGrowable) {
            growablesList.add(block.getRegistryName().toString());
          }
        }
        Path path = Paths.get("roots.log");
        try {
          Files.write(path, growablesList, StandardCharsets.UTF_8);
        } catch (IOException e) {
          player.sendMessage(new TextComponentString("Unable to write roots.log"));
          return;
        }
        player.sendMessage(new TextComponentString("Growables written to roots.log"));
      }
    }
  }
}
