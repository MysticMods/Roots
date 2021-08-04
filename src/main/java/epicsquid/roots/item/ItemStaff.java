package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.EventManager;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class ItemStaff extends ItemBase {
  public ItemStaff(String name) {
    super(name);
    setMaxStackSize(1);
    this.hasSubtypes = true;
    this.setHasSubtypes(true);
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
    StaffSpellStorage oldCapability = StaffSpellStorage.fromStack(oldStack);
    StaffSpellStorage newCapability = StaffSpellStorage.fromStack(newStack);

    return slotChanged || (oldCapability != null && newCapability != null && oldCapability.getSelectedSlot() != newCapability.getSelectedSlot());
  }

  public ItemStack nextSlot(World world, EntityPlayer player, ItemStack stack, StaffSpellStorage capability) {
    capability.nextSlot();
    if (world.isRemote) {
      StaffSpellInfo info = capability.getSelectedInfo();
      if (info != null) {
        SpellBase spell = info.getSpell();
        player.sendStatusMessage(new TextComponentTranslation("roots.info.staff.slot_and_spell", capability.getSelectedSlot(), spell == null ? "none" : new TextComponentTranslation("roots.spell." + spell.getName() + ".name").setStyle(new Style().setColor(spell.getTextColor()).setBold(true))).setStyle(new Style().setColor(TextFormatting.GOLD)), true);
      }
    }
    return stack;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (player.isSneaking() && capability != null) {
      return new ActionResult<>(EnumActionResult.SUCCESS, nextSlot(world, player, stack, capability));
    } else {
      if (capability != null && !capability.onCooldown()) {
        StaffSpellInfo info = capability.getSelectedInfo();
        if (info != null) {
          SpellBase spell = info.getSpell();
          if (spell != null && spell.getCastType() != SpellBase.EnumCastType.CONTINUOUS) {
            if (spell.getCastType() == SpellBase.EnumCastType.INSTANTANEOUS) {
              if (spell.costsMet(player, info.getModifiers())) {
                SpellBase.CastResult result = spell.cast(player, info, 0);
                if (result.isSuccess()) {
                  if (!player.capabilities.isCreativeMode && !world.isRemote) {
                    spell.enactCosts(player, info.getModifiers());
                    capability.setCooldown(result.modifyCooldown(info.cooldownTotal()) + player.world.getTotalWorldTime());
                  }
                }
              }
              return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
          } else if (spell != null && spell.getCastType() == SpellBase.EnumCastType.CONTINUOUS) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
          }
        }
      }
    }
    return new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
  }

  @Override
  public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (player instanceof EntityPlayer && capability != null) {
      StaffSpellInfo info = capability.getSelectedInfo();
      SpellBase spell = info == null ? null : info.getSpell();
      if (spell != null) {
        if (spell.getCastType() == SpellBase.EnumCastType.CONTINUOUS) {
          if (spell.costsMet((EntityPlayer) player, info.getModifiers())) {
            SpellBase.CastResult result = spell.cast((EntityPlayer) player, info, count);
            if (result.isSuccess() && !player.world.isRemote) {
              spell.enactTickCosts((EntityPlayer) player, info.getModifiers());
            }
          }
        }
      }
    }
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability == null) {
      return;
    }
    StaffSpellInfo info = capability.getSelectedInfo();
    SpellBase spell = info == null ? null : info.getSpell();
    EntityPlayer player = (EntityPlayer) entity;
    if (spell != null) {
      if (spell.getCastType() == SpellBase.EnumCastType.CONTINUOUS) {
        if (!player.isCreative()) {
          info.use(player.world.getTotalWorldTime());
        }
      }
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability != null) {
      capability.tick(entity.world.getTotalWorldTime());
    }
  }

  // TODO: This needs to happen to the library
  // TODO: BUT WHY DOES IT NEED TO HAPPEN IN THE LIBRARY???
  public static void createData(ItemStack stack, DustSpellStorage dustCapability) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability != null) {
      capability.addSpell(Objects.requireNonNull(dustCapability.getSelectedInfo()).toStaff());
      if (capability.getSelectedSlot() == 0) {
        capability.setSelectedSlot(1);
      }
    }
  }

  public static void clearData(ItemStack stack) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability != null) {
      capability.clearSelectedSlot();
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability == null) {
      return;
    }
    tooltip.add(I18n.format("roots.tooltip.staff.selected") + (capability.getSelectedSlot()));
    StaffSpellInfo info = capability.getSelectedInfo();
    if (info != null) {
      SpellBase spell = info.getSpell();
      if (spell != null) {
        tooltip.add("");
        spell.addToolTip(tooltip, info.getModifiers());
      }
    } else {
      tooltip.add("");
      tooltip.add("No spell.");
    }
    if (GuiScreen.isShiftKeyDown()) {
      tooltip.add("");
      for (int i = StaffSpellStorage.MIN_SPELL_SLOT; i <= StaffSpellStorage.MAX_SPELL_SLOT; i++) {
        info = capability.getSpellInSlot(i);
        if (info != null) {
          SpellBase other = info.getSpell();
          if (other == null) {
            tooltip.add("" + i + ": No spell.");
          } else {
            tooltip.add("" + i + ": " + other.getTextColor() + TextFormatting.BOLD + I18n.format("roots.spell." + other.getName() + ".name"));
          }
        }
      }
    }
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    return capability != null && capability.onCooldown();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability == null) {
      return Util.intColor(255, 255, 255);
    }
    StaffSpellInfo info = capability.getSelectedInfo();
    if (info == null) {
      return Util.intColor(255, 255, 255);
    }
    SpellBase spell = info.getSpell();
    if (spell != null) {
      double factor = 0.5f * (Math.sin(6.0f * Math.toRadians(EventManager.ticks + Minecraft.getMinecraft().getRenderPartialTicks())) + 1.0f);
      return Util.intColor((int) (255 * (spell.getRed1() * factor + spell.getRed2() * (1.0 - factor))), (int) (255 * (spell.getGreen1() * factor + spell.getGreen2() * (1.0 - factor))), (int) (255 * (spell.getBlue1() * factor + spell.getBlue2() * (1.0 - factor))));
    }
    return Util.intColor(255, 255, 255);
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability == null) {
      return 0;
    }
    return (double) capability.getCooldownLeft() / capability.getCooldown();
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 72000;
  }

  @Nonnull
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability == null) {
      return EnumAction.NONE;
    }
    StaffSpellInfo info = capability.getSelectedInfo();
    if (info != null) {
      SpellBase spell = info.getSpell();
      if (spell != null) {
        if (spell.getCastType() == SpellBase.EnumCastType.CONTINUOUS) {
          return EnumAction.BOW;
        } else {
          return EnumAction.NONE;
        }
      }
    }
    return EnumAction.NONE;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName().toString()), new ModelResourceLocation(getRegistryName().toString() + "_1"));
    ModelLoader.setCustomMeshDefinition(this, stack -> {
      ResourceLocation baseName = getRegistryName();

      StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
      if (capability != null && capability.hasSpellInSlot()) {
        return new ModelResourceLocation(baseName.toString() + "_1");
      } else {
        return new ModelResourceLocation(baseName.toString());
      }
    });
  }

  public static class StaffColorHandler implements IItemColor {

    @Override
    public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
      StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
      if (capability != null && capability.hasSpellInSlot() && stack.getItem() instanceof ItemStaff) {
        StaffSpellInfo info = capability.getSelectedInfo();
        if (info == null) {
          return Util.intColor(255, 255, 255);
        }
        SpellBase spell = info.getSpell();
        if (tintIndex == 1) {
          int r = (int) (255 * spell.getRed1());
          int g = (int) (255 * spell.getGreen1());
          int b = (int) (255 * spell.getBlue1());
          return (r << 16) + (g << 8) + b;
        }
        if (tintIndex == 2) {
          int r = (int) (255 * spell.getRed2());
          int g = (int) (255 * spell.getGreen2());
          int b = (int) (255 * spell.getBlue2());
          return (r << 16) + (g << 8) + b;
        }
        return Util.intColor(255, 255, 255);
      }
      return Util.intColor(255, 255, 255);
    }
  }

  @Override
  public String getHighlightTip(ItemStack stack, String displayName) {
    StaffSpellStorage capability = StaffSpellStorage.fromStack(stack);
    if (capability == null) {
      return displayName;
    }
    String additional = capability.formatSelectedSpell();
    if (additional != null) {
      return displayName + " " + additional;
    }

    return displayName;
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
    if (tab == this.getCreativeTab()) {
      subItems.add(new ItemStack(this));
      for (SpellBase entry : SpellRegistry.spellRegistry.values()) {
        if (entry.isDisabled()) {
          continue;
        }
        subItems.add(entry.getStaff());
      }
    }
  }
}