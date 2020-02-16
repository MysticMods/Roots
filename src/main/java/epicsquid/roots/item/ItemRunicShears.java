package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.item.ItemShearsBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.capability.life_essence.LifeEssenceCapability;
import epicsquid.roots.capability.life_essence.LifeEssenceCapabilityProvider;
import epicsquid.roots.capability.runic_shears.RunicShearsCapability;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.dispenser.DispenseKnife;
import epicsquid.roots.item.dispenser.DispenseRunicShears;
import epicsquid.roots.network.fx.MessageRunicShearsAOEFX;
import epicsquid.roots.network.fx.MessageRunicShearsFX;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ItemRunicShears extends ItemShearsBase {
  public static AxisAlignedBB bounding = new AxisAlignedBB(-2, -2, -2, 2, 2, 2);
  private Random random;

  public ItemRunicShears(@Nonnull String name) {
    super(name);
    setMaxDamage(357);
    setMaxStackSize(1);
    setHasSubtypes(false);
    random = new Random();

    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseRunicShears.getInstance());
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    Item item = repair.getItem();
    if (item instanceof ItemBlock) {
      Block block = ((ItemBlock) item).getBlock();
      return ModBlocks.runestoneBlocks.contains(block);
    }

    return false;
  }

  @Override
  @Nonnull
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    if (block == ModBlocks.imbuer) {
      return EnumActionResult.PASS;
    }

    IBlockState moss = MossConfig.scrapeResult(state);
    IBlockState moss2 = MossConfig.mossConversion(state);

    if (moss != null || moss2 != null) {
      if (!world.isRemote) {
        AxisAlignedBB bounds = bounding.offset(pos);
        BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
        List<BlockPos> affectedBlocks = new ArrayList<>();
        for (BlockPos.MutableBlockPos p : BlockPos.getAllInBoxMutable(start, stop)) {
          IBlockState pState = world.getBlockState(p);
          IBlockState m = MossConfig.scrapeResult(pState);
          if (m != null) {
            affectedBlocks.add(p.toImmutable());
            world.setBlockState(p, m);
            world.scheduleBlockUpdate(p, m.getBlock(), 1, m.getBlock().tickRate(world));
            ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), new ItemStack(ModItems.terra_moss));
          }
        }
        if (!affectedBlocks.isEmpty()) {
          if (!player.capabilities.isCreativeMode) {
            player.getHeldItem(hand).damageItem(1 + Math.min(6, random.nextInt(affectedBlocks.size())), player);
          }
          MessageRunicShearsAOEFX message = new MessageRunicShearsAOEFX(affectedBlocks);
          PacketHandler.sendToAllTracking(message, world.provider.getDimension(), pos);
        }
      }
      player.swingArm(hand);
      world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
      return EnumActionResult.SUCCESS;
    }

    RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(block);

    if (recipe != null) {
      if (!world.isRemote) {

        if (block instanceof BlockCrops) {
          if (((BlockCrops) block).isMaxAge(world.getBlockState(pos))) {
            world.setBlockState(pos, ((BlockCrops) block).withAge(0));
          } else {
            return EnumActionResult.SUCCESS;
          }
        } else {
          world.setBlockState(pos, recipe.getReplacementBlock().getDefaultState());
        }
        ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), recipe.getDrop().copy());
        if (!player.capabilities.isCreativeMode) {
          player.getHeldItem(hand).damageItem(1, player);
        }
        player.swingArm(hand);
        world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
      } else {
        for (int i = 0; i < 50; i++) {
          ClientProxy.particleRenderer.spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
              random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.855 + random.nextDouble() * 0.05, 0.710, 0.943 - random.nextDouble() * 0.05, 1, random.nextDouble() + 0.5, random.nextDouble() * 2);
        }
      }
    }
    return EnumActionResult.SUCCESS;
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    World world = player.world;
    Random rand = itemRand;

    if (entity.isChild()) return true;

    if (!player.isSneaking()) {
      RunicShearEntityRecipe recipe = ModRecipes.getRunicShearRecipe(entity);
      if (recipe != null) {
        player.swingArm(hand);
        if (!world.isRemote) {
          RunicShearsCapability cap = entity.getCapability(RunicShearsCapabilityProvider.RUNIC_SHEARS_CAPABILITY, null);
          if (cap != null) {
            if (cap.canHarvest()) {
              cap.setCooldown(recipe.getCooldown());
              EntityItem ent = entity.entityDropItem(recipe.getDrop().copy(), 1.0F);
              ent.motionY += rand.nextFloat() * 0.05F;
              ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
              ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
              if (!player.capabilities.isCreativeMode) {
                itemstack.damageItem(1, entity);
              }
              world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
              IMessage packet = new MessageRunicShearsFX(entity);
              PacketHandler.sendToAllTracking(packet, entity);
              return true;
            } else {
              // TODO: play particles (failure)?
              player.sendStatusMessage(new TextComponentTranslation("roots.runic_shears.cooldown").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), true);
            }
          }
        }
      }
    } else {
      LifeEssenceCapability cap = entity.getCapability(LifeEssenceCapabilityProvider.LIFE_ESSENCE_CAPABILITY, null);
      if (cap != null) {
        if (cap.canHarvest()) {
          player.swingArm(hand);
          if (!world.isRemote) {
            cap.setCooldown(20 * 360);
            ItemStack stack = new ItemStack(ModItems.life_essence);
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) {
              tag = new NBTTagCompound();
              stack.setTagCompound(tag);
            }
            tag.setString("id", EntityList.getKey(entity).toString());
            EntityItem ent = entity.entityDropItem(stack, 1.0F);
            ent.motionY += rand.nextFloat() * 0.05F;
            ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            if (!player.capabilities.isCreativeMode) {
              itemstack.damageItem(1, entity);
            }
            world.playSound(player, entity.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
            IMessage packet = new MessageRunicShearsFX(entity);
            PacketHandler.sendToAllTracking(packet, entity);
            return true;
          }
        } else {
          player.sendStatusMessage(new TextComponentTranslation("roots.life_essence.cooldown").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), true);
        }
      } else {
        player.sendStatusMessage(new TextComponentTranslation("roots.life_essence.invalid").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), true);
      }
    }

    if (entity instanceof IShearable) {
      int count = 0;
      if (Items.SHEARS.itemInteractionForEntity(itemstack, player, entity, hand)) count++;

      float radius = GeneralConfig.RunicShearsRadius;
      List<EntityLiving> entities = Util.getEntitiesWithinRadius(entity.world, (Entity e) -> e instanceof IShearable, entity.getPosition(), radius, radius / 2, radius);
      for (EntityLiving e : entities) {
        e.captureDrops = true;
        if (Items.SHEARS.itemInteractionForEntity(itemstack, player, e, hand)) count++;
        e.captureDrops = false;
        if (!world.isRemote) {
          for (EntityItem ent : e.capturedDrops) {
            ent.setPosition(entity.posX, entity.posY, entity.posZ);
            ent.motionY = 0;
            ent.motionX = 0;
            ent.motionZ = 0;
            ent.world.spawnEntity(ent);
          }
        }
      }
      if (count > 0) {
        player.swingArm(hand);
        return true;
      }
    }

    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    tooltip.add("");
    tooltip.add(I18n.format("roots.runic_shears.tooltip1", TextFormatting.BOLD + "" + TextFormatting.AQUA + I18n.format("roots.runic_shears.right_click") + TextFormatting.RESET + TextFormatting.GREEN));
    tooltip.add(I18n.format("roots.runic_shears.tooltip2", TextFormatting.BOLD + "" + TextFormatting.AQUA + I18n.format("roots.runic_shears.sneak_right_click") + TextFormatting.RESET + TextFormatting.GREEN));
  }
}
