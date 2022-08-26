package epicsquid.roots.command;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.world.data.SpellLibraryData;
import epicsquid.roots.world.data.SpellLibraryRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Collections;
import java.util.List;

public class CommandStaff extends CommandBase {
  public CommandStaff() {
  }

  @Override
  public String getName() {
    return "staff";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/staff <spell name>";
  }

  @Override
  public List<String> getAliases() {
    return Collections.singletonList("staff");
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    if (sender instanceof EntityPlayer && args.length != 0) {
      EntityPlayer player = (EntityPlayer) sender;
      String spellName = args[0];
      if (!spellName.startsWith("spell")) {
        spellName = "spell_" + spellName;
      }
      SpellBase spell = SpellRegistry.getSpell(spellName);
      if (spell == null) {
        player.sendMessage(new TextComponentString("Invalid spell: " + args[0]));
        return;
      }

      SpellLibraryData library = SpellLibraryRegistry.getData(player);
      library.addSpell(spell);

      ItemStack staff = player.getHeldItemMainhand();
      boolean newStaff = false;
      if (staff.getItem() != ModItems.staff) {
        staff = new ItemStack(ModItems.staff);
        newStaff = true;
      }
      StaffSpellStorage storage = StaffSpellStorage.fromStack(staff);
      StaffSpellInfo info = StaffSpellInfo.fromSpell(spell, true);
      if (storage != null && storage.hasFreeSlot()) {
        storage.setSpellToSlot(storage.getNextFreeSlot(), info);
        storage.saveToStack();
        if (!newStaff) {
          return;
        }
      }

      IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

      ItemStack result = ItemHandlerHelper.insertItemStacked(inv, staff, false);
      if (!result.isEmpty()) {
        ItemUtil.spawnItem(player.world, player.getPosition(), result);
      }
    }
  }
}
