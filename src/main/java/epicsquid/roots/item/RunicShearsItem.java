package epicsquid.roots.item;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RunicShearsItem extends Item {
  public static AxisAlignedBB bounding = new AxisAlignedBB(-2, -2, -2, 2, 2, 2);

  // TODO: Doesn't this already exist in the item by default?
  private Random random;

  public RunicShearsItem(Properties properties) {
    super(properties);
  }

/*  public RunicShearsItem(@Nonnull String name) {
    super(name);
    setMaxDamage(357);
    setMaxStackSize(1);
    setHasSubtypes(false);
    random = new Random();
  }*/

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    Item item = repair.getItem();
    if (item instanceof BlockItem) {
      Block block = ((BlockItem) item).getBlock();
      return ModBlocks.runestoneBlocks.contains(block);
    }

    return false;
  }

  @Override
  @Nonnull
  public ActionResultType onItemUse(ItemUseContext context) {
/*    PlayerEntity player, World
  } world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {*/
    World world = context.getWorld();
    BlockPos pos = context.getPos();
    BlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    if (block == ModBlocks.imbuer) {
      return ActionResultType.PASS;
    }

    // TODO: Once moss configuration is reinstated as a JSON recipe

    BlockState moss = Blocks.AIR.getDefaultState(); // MossConfig.scrapeResult(state);
    BlockState moss2 = moss; // MossConfig.mossConversion(state);

    if (moss != null || moss2 != null) {
      if (!world.isRemote) {
        AxisAlignedBB bounds = bounding.offset(pos);
        BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
        List<BlockPos> affectedBlocks = new ArrayList<>();
        for (BlockPos p : BlockPos.getAllInBoxMutable(start, stop)) {
          BlockState pState = world.getBlockState(p);
          BlockState m = Blocks.AIR.getDefaultState(); // MossConfig.scrapeResult(pState);
          if (m != null) {
            affectedBlocks.add(p.toImmutable());
            world.setBlockState(p, m);
            world.getPendingBlockTicks().scheduleTick(p, m.getBlock(), 1, TickPriority.NORMAL);
            ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), new ItemStack(ModItems.terra_moss));
          }
        }
        if (!affectedBlocks.isEmpty()) {
          if (!player.isCreative()) {
            player.getHeldItem(hand).damageItem(1 + Math.min(6, random.nextInt(affectedBlocks.size())), player, (p_220282_1_) -> {
              p_220282_1_.sendBreakAnimation(hand);
            });
          }
          // TODO: Packets
/*          MessageRunicShearsAOEFX message = new MessageRunicShearsAOEFX(affectedBlocks);
          PacketHandler.sendToAllTracking(message, world.provider.getDimension(), pos);*/
        }
      }
      player.swingArm(hand);
      world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
      return ActionResultType.SUCCESS;
    }

    RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(block);

    if (recipe != null) {
      if (!world.isRemote) {

        if (block instanceof CropsBlock) {
          if (((CropsBlock) block).isMaxAge(world.getBlockState(pos))) {
            world.setBlockState(pos, ((CropsBlock) block).withAge(0));
          } else {
            return ActionResultType.SUCCESS;
          }
        } else {
          world.setBlockState(pos, recipe.getReplacementBlock().getDefaultState());
        }
        ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), recipe.getDrop().copy());
        if (!player.isCreative()) {
          player.getHeldItem(hand).damageItem(1, player, (p_220282_1_) -> {
            p_220282_1_.sendBreakAnimation(hand);
          });
        }
        player.swingArm(hand);
        world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
      } else {
        for (int i = 0; i < 50; i++) {
          // TODO: Something about this
          /*          ClientProxy.particleRenderer.spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.855 + random.nextDouble() * 0.05, 0.710, 0.943 - random.nextDouble() * 0.05, 1, random.nextDouble() + 0.5, random.nextDouble() * 2);*/
        }
      }
    }
    return ActionResultType.SUCCESS;
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack itemstack, PlayerEntity player, LivingEntity entity, Hand hand) {
    World world = player.world;
    Random rand = random;

    if (entity instanceof IShearable) {
      int count = 0;
      if (Items.SHEARS.itemInteractionForEntity(itemstack, player, entity, hand)) count++;

      float radius = 20; // TODO: update with config GeneralConfig.RunicShearsRadius;
      List<LivingEntity> entities = Util.getEntitiesWithinRadius(entity.world, (Entity e) -> e instanceof IShearable, entity.getPosition(), radius, radius / 2, radius);
      for (LivingEntity e : entities) {
        e.captureDrops(new ArrayList<>());
        if (Items.SHEARS.itemInteractionForEntity(itemstack, player, e, hand)) count++;
        Collection<ItemEntity> items = e.captureDrops();
        if (!world.isRemote) {
          for (ItemEntity ent : items) {
            ent.setPosition(entity.posX, entity.posY, entity.posZ);
            ent.setMotion(0, 0, 0);
            ent.world.addEntity(ent);
          }
        }
      }
      if (count > 0) {
        player.swingArm(hand);
        return true;
      }
    }

    if (entity.isChild()) return true;

    RunicShearEntityRecipe recipe = ModRecipes.getRunicShearRecipe(entity);
    if (recipe != null) {
      player.swingArm(hand);
      if (!world.isRemote) {
        entity.getCapability(RunicShearsCapabilityProvider.RUNIC_SHEARS_CAPABILITY).ifPresent((cap) -> {
          if (cap.canHarvest()) {
            cap.setCooldown(recipe.getCooldown());
            ItemEntity ent = entity.entityDropItem(recipe.getDrop().copy(), 1.0F);
            Vec3d motion = ent.getMotion();
            ent.setMotion(motion.y + (rand.nextFloat() - rand.nextFloat()) * 0.1f, motion.x + rand.nextFloat() * 0.05f, motion.y + (rand.nextFloat() - rand.nextFloat()) * 0.1f);
            if (!player.isCreative()) {
              itemstack.damageItem(1, entity, (p_220282_1_) -> {
                p_220282_1_.sendBreakAnimation(hand);
              });
            }
            world.playSound(player, entity.getPosition(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
            // TODO: Packets
/*            IMessage packet = new MessageRunicShearsFX(entity);
            PacketHandler.sendToAllTracking(packet, entity);*/
          } else {
            // TODO: play particles (failure)?
            player.sendStatusMessage(new TranslationTextComponent("roots.runic_shears.cooldown").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), true);
          }
        });
        if (entity.getCapability(RunicShearsCapabilityProvider.RUNIC_SHEARS_CAPABILITY).isPresent()) {
          return true;
        }
      }
    }

    return false;
  }
}
