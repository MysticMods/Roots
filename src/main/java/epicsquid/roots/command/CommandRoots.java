package epicsquid.roots.command;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.handler.SpellHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemStaff;
import epicsquid.roots.network.MessageClearToasts;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.ItemUtil;
import net.minecraft.advancements.*;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.patchouli.common.handler.AdvancementSyncHandler;

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
    return "/roots book";
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
      if (args[0].equalsIgnoreCase("book")) {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        WorldServer world = player.getServerWorld();
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
      }
    }
  }
}
