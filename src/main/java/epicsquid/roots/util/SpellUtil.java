package epicsquid.roots.util;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstance;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstanceList;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.world.data.SpellLibraryData;
import epicsquid.roots.world.data.SpellLibraryRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SpellUtil {
  public static boolean isValidStaff(ItemStack stack) {
    return stack.getItem() == ModItems.staff || stack.getItem() == ModItems.spell_dust || stack.getItem() == ModItems.spell_icon;
  }

  public static boolean isStaff(ItemStack stack) {
    return stack.getItem() == ModItems.staff;
  }

  public static boolean isValidDust(ItemStack stack) {
    return stack.getItem() == ModItems.spell_dust || stack.getItem() == ModItems.spell_icon;
  }

  public static void updateModifiers (EntityPlayer player) {
    if (player.world.isRemote) {
      return;
    }

    IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
    if (handler == null) {
      return;
    }

    for (int i = 0; i < handler.getSlots(); i++) {
      ItemStack stack = handler.getStackInSlot(i);
      if (isStaff(stack)) {
        updateModifiers(stack, player);
      }
    }
  }

  public static boolean updateModifiers(ItemStack stack, EntityPlayer player) {
    if (player.world.isRemote) {
      return false;
    }

    if (!isStaff(stack)) {
      return false;
    }

    StaffSpellStorage storage = StaffSpellStorage.fromStack(stack);
    if (storage == null) {
      return false;
    }

    SpellLibraryData data = SpellLibraryRegistry.getData(player);

    for (StaffSpellInfo info : storage.getSpells()) {
      LibraryModifierInstanceList libraryModifiers = data.getModifiers(info.toLibrary());
      info.getModifiers().removeIf(o -> libraryModifiers.get(o.getModifier()) == null || ModifierRegistry.get(o.getModifier()) == null);

      data.updateSpell(info.toLibrary());
    }

    for (StaffSpellInfo info : storage.getSpells()) {
      SpellBase spell = info.getSpell();
      LibrarySpellInfo current = data.getData(spell);
      StaffModifierInstanceList modifiers = info.getModifiers();
      for (LibraryModifierInstance instance : current.getModifiers()) {
        if (instance.isApplied()) {
          StaffModifierInstance currentInstance = modifiers.get(instance.getModifier());
          if (currentInstance == null) {
            throw new NullPointerException("Trying to update " + spell.getRegistryName() + " item for player " + player + " but incoming modifier list contained " + instance.getModifier().getRegistryName() + " but our copy does not!");
          }
          currentInstance.setApplied();
        }
      }
    }

    storage.saveToStack();
    return true;
  }
}
