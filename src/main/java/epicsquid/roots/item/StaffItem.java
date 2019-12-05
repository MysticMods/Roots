package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.EventManager;
import epicsquid.roots.Roots;
import epicsquid.roots.handler.SpellHandler;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class StaffItem extends ItemBase {
  public StaffItem(String name) {
    super(name);
    setMaxStackSize(1);
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
    SpellHandler oldCapability = SpellHandler.fromStack(oldStack);
    SpellHandler newCapability = SpellHandler.fromStack(newStack);

    if (oldCapability != null && newCapability != null) {
      return slotChanged || oldCapability.getSelectedSlot() != newCapability.getSelectedSlot();
    }

    return slotChanged;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    SpellHandler capability = SpellHandler.fromStack(stack);
    if (player.isSneaking()) {
      capability.nextSlot();
      if (world.isRemote) {
        SpellBase spell = capability.getSelectedSpell();
        player.sendStatusMessage(new TranslationTextComponent("roots.info.staff.slot_and_spell", capability.getSelectedSlot() + 1, spell == null ? "none" : new TranslationTextComponent("roots.spell." + spell.getName() + ".name").setStyle(new Style().setColor(spell.getTextColor()).setBold(true))).setStyle(new Style().setColor(TextFormatting.GOLD)), true);
      }
      return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
    } else {
      if (capability.getCooldown() <= 0) {
        SpellBase spell = capability.getSelectedSpell();
        if (spell != null) {
          SpellEvent event = new SpellEvent(player, spell);
          MinecraftForge.EVENT_BUS.post(event);
          spell = event.getSpell();
          if (spell.getCastType() == SpellBase.EnumCastType.INSTANTANEOUS) {
            if (spell.costsMet(player)) {
              boolean result = spell.cast(player, capability.getSelectedModules());
              if (result) {
                if (!player.capabilities.isCreativeMode) {
                  spell.enactCosts(player);
                  capability.setCooldown(event.getCooldown());
                  capability.setLastCooldown(event.getCooldown());
                }
              }
              return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
            }
          } else if (spell.getCastType() == SpellBase.EnumCastType.CONTINUOUS) {
            player.setActiveHand(hand);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
          }
        }
      }
    }
    return new ActionResult<>(ActionResultType.FAIL, player.getHeldItem(hand));
  }

  @Override
  public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    if (player instanceof PlayerEntity) {
      if (capability.getCooldown() <= 0) {
        SpellBase spell = capability.getSelectedSpell();
        if (spell != null) {
          if (spell.getCastType() == SpellBase.EnumCastType.CONTINUOUS) {
            if (spell.costsMet((PlayerEntity) player)) {
              boolean result = spell.cast((PlayerEntity) player, capability.getSelectedModules());
              if (result) {
                spell.enactTickCosts((PlayerEntity) player);
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    SpellBase spell = capability.getSelectedSpell();
    if (spell != null) {
      SpellEvent event = new SpellEvent((PlayerEntity) entity, spell);
      MinecraftForge.EVENT_BUS.post(event);
      if (spell.getCastType() == SpellBase.EnumCastType.CONTINUOUS) {
        if (!((PlayerEntity) entity).capabilities.isCreativeMode) {
          capability.setCooldown(event.getCooldown());
          capability.setLastCooldown(event.getCooldown());
        }
      }
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    if (capability.getCooldown() > 0) {
      capability.setCooldown(capability.getCooldown() - 1);
      if (capability.getCooldown() <= 0) {
        capability.setCooldown(0);
        capability.setLastCooldown(0);
      }
    }
  }

  public static void createData(ItemStack stack, SpellHandler dustCapability) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    capability.setSpellToSlot(dustCapability.getSelectedSpell());
    for (SpellModule module : dustCapability.getSelectedModules()) {
      capability.addModule(module);
    }
  }

  public static void clearData(ItemStack stack) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    capability.clearSelectedSlot();
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    tooltip.add(I18n.format("roots.tooltip.staff.selected") + (capability.getSelectedSlot() + 1));
    SpellBase spell = capability.getSelectedSpell();
    if (spell != null) {
      tooltip.add("");
      spell.addToolTip(tooltip);
      List<SpellModule> spellModules = capability.getSelectedModules();
      if (!spellModules.isEmpty()) {
        tooltip.add(I18n.format("roots.spell.module.description"));
        String prefix = "roots.spell." + spell.getName();
        for (SpellModule module : spellModules) {
          tooltip.add(module.getFormat() + I18n.format("roots.spell.module." + module.getName() + ".name") + ": " + I18n.format(prefix + "." + module.getName() + ".description"));
        }
      }
    } else {
      tooltip.add("");
      tooltip.add("No spell.");
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
      tooltip.add("");
      for (int i = 0; i < 5; i++) {
        SpellBase other = capability.getSpellInSlot(i);
        if (other == null) {
          tooltip.add("" + (i + 1) + ": No spell.");
        } else {
          tooltip.add("" + (i + 1) + ": " + other.getTextColor() + TextFormatting.BOLD + I18n.format("roots.spell." + other.getName() + ".name"));
        }
      }
    }
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    SpellBase spell = capability.getSelectedSpell();
    if (capability.getCooldown() > 0 && spell != null) {
      return true;
    }
    return false;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    SpellBase spell = capability.getSelectedSpell();
    if (spell != null) {
      double factor = 0.5f * (Math.sin(6.0f * Math.toRadians(EventManager.ticks + Minecraft.getMinecraft().getRenderPartialTicks())) + 1.0f);
      return Util
          .intColor((int) (255 * (spell.getRed1() * factor + spell.getRed2() * (1.0 - factor))), (int) (255 * (spell.getGreen1() * factor + spell.getGreen2() * (1.0 - factor))),
              (int) (255 * (spell.getBlue1() * factor + spell.getBlue2() * (1.0 - factor))));
    }
    return Util.intColor(255, 255, 255);
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    return (double) capability.getCooldown() / capability.getLastCooldown();
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 72000;
  }

  @Nonnull
  @Override
  public UseAction getItemUseAction(ItemStack stack) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    SpellBase spell = capability.getSelectedSpell();
    if (spell != null) {
      if (spell.getCastType() == SpellBase.EnumCastType.CONTINUOUS) {
        return UseAction.BOW;
      } else {
        return UseAction.NONE;
      }
    }
    return UseAction.NONE;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void initModel() {
    ModelBakery
        .registerItemVariants(this, new ModelResourceLocation(getRegistryName().toString()), new ModelResourceLocation(getRegistryName().toString() + "_1"));
    ModelLoader.setCustomMeshDefinition(this, stack -> {
      ResourceLocation baseName = getRegistryName();
      if (stack.getDisplayName().compareToIgnoreCase("Shiny Rod") == 0) {
        baseName = new ResourceLocation(Roots.MODID + ":shiny_rod");
      }
      if (stack.getDisplayName().compareToIgnoreCase("Cutie Moon Rod") == 0) {
        baseName = new ResourceLocation(Roots.MODID + ":moon_rod");
      }

      SpellHandler capability = SpellHandler.fromStack(stack);
      if (capability.hasSpellInSlot()) {
        String s = capability.getSelectedSpell().getName();
        if (SpellRegistry.spellRegistry.containsKey(s)) {
          return new ModelResourceLocation(baseName.toString() + "_1");
        } else {
          return new ModelResourceLocation(baseName.toString());
        }
      }
      return new ModelResourceLocation(baseName.toString());
    });
  }

  public static class StaffColorHandler implements IItemColor {

    @Override
    public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
      SpellHandler capability = SpellHandler.fromStack(stack);
      if (capability.hasSpellInSlot() && stack.getItem() instanceof StaffItem) {
        if (stack.getDisplayName().compareToIgnoreCase("Shiny Rod") == 0 || stack.getDisplayName().compareToIgnoreCase("Cutie Moon Rod") == 0) {
          SpellBase spell = capability.getSelectedSpell();
          if (spell != null) {
            if (tintIndex == 0) {
              int r = (int) (255 * spell.getRed1());
              int g = (int) (255 * spell.getGreen1());
              int b = (int) (255 * spell.getBlue1());
              return (r << 16) + (g << 8) + b;
            }
            if (tintIndex == 1) {
              int r = (int) (255 * spell.getRed2());
              int g = (int) (255 * spell.getGreen2());
              int b = (int) (255 * spell.getBlue2());
              return (r << 16) + (g << 8) + b;
            }
          }
          return Util.intColor(255, 255, 255);
        } else {
          SpellBase spell = capability.getSelectedSpell();
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
      }
      return Util.intColor(255, 255, 255);
    }
  }

  @Override
  public String getHighlightTip(ItemStack stack, String displayName) {
    SpellHandler capability = SpellHandler.fromStack(stack);
    String additional = capability.formatSelectedSpell();
    if (additional != null) {
      return displayName + " " + additional;
    }

    return displayName;
  }
}