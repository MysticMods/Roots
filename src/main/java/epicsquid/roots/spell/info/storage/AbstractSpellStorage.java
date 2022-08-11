package epicsquid.roots.spell.info.storage;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.AbstractSpellInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.Function;

public abstract class AbstractSpellStorage<V extends AbstractSpellInfo> implements INBTSerializable<NBTTagCompound> {
	protected ItemStack stack;
	protected int selectedSlot = 1;
	
	public AbstractSpellStorage() {
	}
	
	protected AbstractSpellStorage(ItemStack stack) {
		this.stack = stack;
	}
	
	public abstract boolean hasSpellInSlot();
	
	public abstract boolean isEmpty();
	
	public abstract boolean isValid();
	
	@Nullable
	public abstract V getSpellInSlot(int slot);
	
	public int getCooldownLeft() {
		return -1;
	}
	
	public int getCooldown() {
		return -1;
	}
	
	@Nullable
	public V getSelectedInfo() {
		return getSpellInSlot(selectedSlot);
	}
	
	@SideOnly(Side.CLIENT)
	public String formatSelectedSpell() {
		V info = getSelectedInfo();
		if (info == null) {
			return "";
		}
		
		SpellBase spell = info.getSpell();
		if (spell == null) {
			return "";
		}
		return "(" + spell.getTextColor() + TextFormatting.BOLD + I18n.format("roots.spell." + spell.getName() + ".name") + TextFormatting.RESET + ")";
	}
	
	public abstract void clearSelectedSlot();
	
	public abstract void clearSlot(int slot);
	
	public int getSelectedSlot() {
		return this.selectedSlot;
	}
	
	public void setSelectedSlot(int slot) {
		this.selectedSlot = slot;
	}
	
	public abstract void previousSlot();
	
	public abstract void nextSlot();
	
	public abstract void addSpell(V spell);
	
	public abstract void setSpellToSlot(int slot, V spell);
	
	public int getNextFreeSlot() {
		return -1;
	}
	
	public boolean hasFreeSlot() {
		return getNextFreeSlot() != -1;
	}
	
	@Override
	public abstract NBTTagCompound serializeNBT();
	
	@Override
	public abstract void deserializeNBT(NBTTagCompound tag);
	
	@Nullable
	protected static <V extends AbstractSpellInfo, T extends AbstractSpellStorage<V>> T fromStack(ItemStack stack, Function<ItemStack, T> factory) {
		T result = factory.apply(stack);
		if (!result.isValid()) {
			return null;
		}
		NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
		if (tag.hasKey("spell_storage")) {
			result.deserializeNBT(tag.getCompoundTag("spell_storage"));
		} else if (tag.hasKey("spell_holder")) {
			result.deserializeNBT(tag.getCompoundTag("spell_holder"));
			tag.removeTag("spell_holder");
			result.saveToStack();
		}
		return result;
	}
	
	public void saveToStack() {
		NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
		tag.setTag("spell_storage", this.serializeNBT());
	}
}
