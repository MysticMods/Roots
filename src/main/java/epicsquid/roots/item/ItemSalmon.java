package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.network.MessageClearToasts;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;
import vazkii.patchouli.common.handler.AdvancementSyncHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings("deprecation")
public class ItemSalmon extends ItemBase {
  public ItemSalmon(@Nonnull String name) {
    super(name);
    setMaxStackSize(1);
  }

  @SuppressWarnings("Duplicates")
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity) {
      CompoundNBT tag = stack.getTagCompound();
      if (tag == null || (!tag.contains("crafter", Constants.NBT.TAG_STRING) || !tag.contains("advancements", Constants.NBT.TAG_LIST))) {
        return stack;
      }

      if (worldIn.isRemote) {
        return ItemStack.EMPTY;
      }

      List<ResourceLocation> advancements = new ArrayList<>();

      ListNBT advancementsList = tag.getList("advancements", Constants.NBT.TAG_STRING);
      for (int i = 0; i < advancementsList.size(); i++) {
        String adv = advancementsList.getStringTagAt(i);
        if (adv.equals("pacifist")) {
          continue;
        }
        advancements.add(new ResourceLocation(Roots.MODID, adv));
      }

      ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
      ServerWorld world = (ServerWorld) player.world;
      AdvancementManager manager = world.getAdvancementManager();
      PlayerAdvancements playerAdvancements = player.getAdvancements();

      if (advancements.size() == 1 && advancements.get(0).getPath().equals("everything")) {
        advancements.clear();

        for (Advancement adv : manager.getAdvancements()) {
          ResourceLocation id = adv.getId();
          if (id.getNamespace().equals(Roots.MODID) && !id.getPath().equals("pacifist")) {
            advancements.add(id);
          }
        }
      }

      for (ResourceLocation adv : advancements) {
        Advancement adv2 = manager.getAdvancement(adv);
        if (adv2 != null) {
          AdvancementProgress progress = playerAdvancements.getProgress(adv2);
          for (String criterion : progress.getRemaningCriteria()) {
            progress.grantCriterion(criterion);
          }
          adv2.getRewards().apply(player);
        }
      }
      playerAdvancements.save();
      playerAdvancements.flushDirty(player);
      MessageClearToasts message = new MessageClearToasts();
      PacketHandler.INSTANCE.sendTo(message, player);
      AdvancementSyncHandler.syncPlayer(player, false);
      worldIn.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 0.8f);
      player.sendMessage(new TranslationTextComponent("roots.message.salmon_eaten").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE).setBold(true)));
    }

    return ItemStack.EMPTY;
  }

  @Override
  public void getSubItems(ItemGroup tab, NonNullList<ItemStack> items) {
    if (isInCreativeTab(tab)) {
      ItemStack inTab = new ItemStack(this);
      CompoundNBT tag = inTab.getTagCompound();
      if (tag == null) {
        tag = new CompoundNBT();
        inTab.setTagCompound(tag);
      }
      tag.setString("crafter", "Nature");
      ListNBT advancements = new ListNBT();
      advancements.add(new StringNBT("everything"));
      tag.put("advancements", advancements);
      items.add(inTab);
    }
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 32;
  }

  @Override
  public UseAction getItemUseAction(ItemStack stack) {
    return UseAction.EAT;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    playerIn.setActiveHand(handIn);
    return ActionResult.newResult(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.EPIC;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    tooltip.add("");

    CompoundNBT tag = stack.getTagCompound();
    if (tag == null || !tag.contains("crafter", Constants.NBT.TAG_STRING) && !tag.contains("advancements", Constants.NBT.TAG_LIST)) {
      tooltip.add(TextFormatting.BOLD + "" + TextFormatting.RED + I18n.format("roots.tooltip.salmon.invalid"));
    } else {
      String crafter = tag.getString("crafter");
      ListNBT advancements = tag.getList("advancements", Constants.NBT.TAG_STRING);
      tooltip.add(I18n.format("roots.tooltip.salmon.crafter", crafter));
      StringJoiner joiner = new StringJoiner(", ", "[", "]");
      for (int i = 0; i < advancements.size(); i++) {
        joiner.add(advancements.getStringTagAt(i));
      }
      tooltip.add(I18n.format("roots.tooltip.salmon.advancements", joiner.toString()));
    }
  }
}
