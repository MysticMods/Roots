package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.IModifierCost;
import epicsquid.roots.modifiers.Modifier;
import epicsquid.roots.modifiers.ModifierRegistry;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings("deprecation")
public class ItemSpellModifier extends ItemBase {
  public ItemSpellModifier(String name) {
    super(name);
    this.hasSubtypes = true;
    this.setHasSubtypes(true);
  }

  @Override
  public void getSubItems(ItemGroup tab, NonNullList<ItemStack> subItems) {
    if (tab == this.getCreativeTab()) {
      for (Modifier modifier : ModifierRegistry.getModifiers()) {
        if (ModifierRegistry.isDisabled(modifier)) {
          continue;
        }
        ItemStack mod = new ItemStack(this);
        CompoundNBT tag = ItemUtil.getOrCreateTag(mod);
        tag.setString("modifier", modifier.getRegistryName().toString());
        subItems.add(mod);
      }
    }
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {
    CompoundNBT tag = ItemUtil.getOrCreateTag(stack);
    if (tag.contains("modifier")) {
      ResourceLocation mod = new ResourceLocation(tag.getString("modifier"));
      Modifier modifier = ModifierRegistry.get(mod);
      if (modifier == null) {
        return "Unknown";
      }

      return I18n.translateToLocal(modifier.getTranslationKey());
    }
    return "Unknown";
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    CompoundNBT tag = ItemUtil.getOrCreateTag(stack);
    if (tag.contains("modifier")) {
      ResourceLocation mod = new ResourceLocation(tag.getString("modifier"));
      Modifier modifier = ModifierRegistry.get(mod);
      if (modifier == null) {
        return;
      }

      SpellBase spell = ModifierRegistry.getSpellFromModifier(modifier);
      if (spell == null) {
        return;
      }
      tooltip.add("");
      tooltip.add(modifier.getFormatting() + net.minecraft.client.resources.I18n.format(modifier.getTranslationKey()) + TextFormatting.RESET);
      tooltip.add(net.minecraft.client.resources.I18n.format("roots.tooltip.modifier.spell_name", net.minecraft.client.resources.I18n.format(spell.getTranslationKey()+".name")));
      tooltip.add(net.minecraft.client.resources.I18n.format("roots.tooltip.modifier.herb_name", net.minecraft.client.resources.I18n.format(modifier.getCore().getStack().getTranslationKey()+".name")));
      tooltip.add(net.minecraft.client.resources.I18n.format(modifier.getTranslationKey()+".desc"));
      if (!modifier.getConflicts().isEmpty()) {
        StringJoiner conflict = new StringJoiner(", ");
        for (IModifier m : modifier.getConflicts()) {
          conflict.add(m.getFormatting() + net.minecraft.client.resources.I18n.format(m.getTranslationKey()) + TextFormatting.RESET);
        }
        tooltip.add(net.minecraft.client.resources.I18n.format("roots.tooltip.modifier.conflicts", conflict.toString()));
      }
      tooltip.add(TextFormatting.BOLD + net.minecraft.client.resources.I18n.format("roots.tooltip.modifier.costs") + TextFormatting.RESET);

      for (IModifierCost cost : modifier.getCosts().values()) {
        switch (cost.getCost()) {
          case NO_COST:
            break;
          case ADDITIONAL_COST:
            if (cost.getHerb() == null) {
              throw new NullPointerException("Additional herb modifier cost type but no herb specified.");
            }
            tooltip.add(net.minecraft.client.resources.I18n.format("roots.guide.modifier.additional_cost", net.minecraft.client.resources.I18n.format(cost.getHerb().getStack().getTranslationKey() + ".name"), String.format("%.4f", cost.getValue())));
            break;
          case ALL_COST_MULTIPLIER:
            if (cost.getValue() < 0) {
              tooltip.add(net.minecraft.client.resources.I18n.format("roots.guide.modifier.decreased_cost", Math.floor(cost.getValue() * 100) + "%"));
            } else {
              tooltip.add(net.minecraft.client.resources.I18n.format("roots.guide.modifier.increased_cost", Math.floor(cost.getValue() * 100) + "%"));
            }
            break;
          case SPECIFIC_COST_ADJUSTMENT:
            if (cost.getValue() < 0) {
              tooltip.add(net.minecraft.client.resources.I18n.format("roots.guide.modifier.specific_decreased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
            } else {
              tooltip.add(net.minecraft.client.resources.I18n.format("roots.guide.modifier.specific_increased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
            }
            break;
          case SPECIFIC_COST_MULTIPLIER:
            if (cost.getValue() < 0) {
              tooltip.add(net.minecraft.client.resources.I18n.format("roots.guide.modifier.specific_decreased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
            } else {
              tooltip.add(net.minecraft.client.resources.I18n.format("roots.guide.modifier.specific_increased_cost", cost.getHerb().getName(), Math.floor(cost.getValue() * 100) + "%"));
            }
            break;
          default:
        }
      }
    }
  }
}