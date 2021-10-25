package epicsquid.roots.item.dispenser;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.capability.runic_shears.RunicShearsCapability;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.ItemRunicShears;
import epicsquid.roots.network.fx.MessageRunicShearsAOEFX;
import epicsquid.roots.network.fx.MessageRunicShearsBlockFX;
import epicsquid.roots.network.fx.MessageRunicShearsFX;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.*;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class DispenseRunicShears implements IDispenseItemBehavior {
  private static final DispenseRunicShears INSTANCE = new DispenseRunicShears();

  public static DispenseRunicShears getInstance() {
    return INSTANCE;
  }

  private DispenseRunicShears() {
  }

  @Override
  public ItemStack dispense(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    if (MossConfig.getBlacklistDimensions().contains(world.provider.getDimension())) {
      return stack;
    }

    boolean successful = false;
    if (!world.isRemote) {
      Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
      BlockPos pos = source.getBlockPos().offset(facing);
      BlockState targetState = world.getBlockState(pos);
      BlockState moss = MossConfig.scrapeResult(targetState);
      BlockState moss2 = MossConfig.mossConversion(targetState);
      Block block = targetState.getBlock();

      // AoE moss
      if (moss != null || moss2 != null) {
        AxisAlignedBB bounds = ItemRunicShears.bounding.offset(pos);
        BlockPos start = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos stop = new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ);
        List<BlockPos> affectedBlocks = new ArrayList<>();
        for (BlockPos.MutableBlockPos p : BlockPos.getAllInBoxMutable(start, stop)) {
          BlockState pState = world.getBlockState(p);
          BlockState m = MossConfig.scrapeResult(pState);
          if (m != null) {
            affectedBlocks.add(p.toImmutable());
            world.setBlockState(p, m);
            world.scheduleBlockUpdate(p, m.getBlock(), 1, m.getBlock().tickRate(world));
            ItemUtil.spawnItem(world, pos.up(), new ItemStack(ModItems.terra_moss));
          }
        }
        if (!affectedBlocks.isEmpty()) {
          if (stack.attemptDamageItem(1 + Math.min(6, world.rand.nextInt(affectedBlocks.size())), world.rand, FakePlayerFactory.getMinecraft((ServerWorld) world))) {
            stack.setCount(0);
          }
          MessageRunicShearsAOEFX message = new MessageRunicShearsAOEFX(affectedBlocks);
          PacketHandler.sendToAllTracking(message, world.provider.getDimension(), pos);
          successful = true;
        }
      }

      // Runic Shears block recipe
      if (!successful) {
        RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(targetState);

        if (recipe != null) {
          if (block instanceof CropsBlock) {
            CropsBlock crop = (CropsBlock) block;
            if (crop.isMaxAge(world.getBlockState(pos))) {
              world.setBlockState(pos, crop.withAge(0));
              successful = true;
            }
          } else {
            world.setBlockState(pos, recipe.getReplacementState());
            successful = true;
          }

          if (successful) {
            ItemUtil.spawnItem(world, pos.up(), recipe.getDrop().copy());
            if (stack.attemptDamageItem(1, world.rand, FakePlayerFactory.getMinecraft((ServerWorld) world))) {
              stack.setCount(0);
            }
            MessageRunicShearsBlockFX message = new MessageRunicShearsBlockFX(pos);
            PacketHandler.sendToAllTracking(message, world.provider.getDimension(), pos);
          }
        }
      }

      // Runic Shears individual recipe
      if (!successful) {
        List<Entity> entities = world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(pos), e -> !(e instanceof PlayerEntity) && e instanceof LivingEntity);
        if (!entities.isEmpty()) {
          Entity entity = null;
          RunicShearEntityRecipe recipe = null;
          RunicShearsCapability cap = null;
          while (!entities.isEmpty()) {
            entity = entities.remove(world.rand.nextInt(entities.size()));
            recipe = ModRecipes.getRunicShearRecipe((LivingEntity) entity);
            if (recipe != null) {
              cap = entity.getCapability(RunicShearsCapabilityProvider.RUNIC_SHEARS_CAPABILITY, null);
              if (cap != null) {
                if (cap.canHarvest()) {
                  break;
                } else {
                  recipe = null;
                  cap = null;
                }
              }
            }
          }

          if (entity != null && recipe != null && cap != null) {
            cap.setCooldown(recipe.getCooldown());
            ItemEntity ent = entity.entityDropItem(recipe.getDrop((LivingEntity) entity).copy(), 1.0F);
            ent.motionY += Util.rand.nextFloat() * 0.05F;
            ent.motionX += (Util.rand.nextFloat() - Util.rand.nextFloat()) * 0.1F;
            ent.motionZ += (Util.rand.nextFloat() - Util.rand.nextFloat()) * 0.1F;
            if (stack.attemptDamageItem(1, world.rand, FakePlayerFactory.getMinecraft((ServerWorld) world))) {
              stack.setCount(0);
            }
            IMessage packet = new MessageRunicShearsFX(entity);
            PacketHandler.sendToAllTracking(packet, entity);
            successful = true;
          }
        }
      }

      if (!successful) {
        List<Entity> closeEntities = world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(pos), e -> e instanceof IShearable);
        if (!closeEntities.isEmpty()) {
          Entity entity = closeEntities.get(world.rand.nextInt(closeEntities.size()));
          successful = true;
          Items.SHEARS.itemInteractionForEntity(stack, FakePlayerFactory.getMinecraft((ServerWorld) world), (LivingEntity) entity, Hand.MAIN_HAND);

          List<BlockPos> affectedSpots = new ArrayList<>();
          affectedSpots.add(entity.getPosition());

          float radius = GeneralConfig.RunicShearsRadius;
          List<MobEntity> entities = Util.getEntitiesWithinRadius(entity.world, (Entity e) -> e instanceof IShearable, entity.getPosition(), radius, radius / 2, radius);
          for (MobEntity e : entities) {
            e.captureDrops = true;
            if (Items.SHEARS.itemInteractionForEntity(stack, FakePlayerFactory.getMinecraft((ServerWorld) world), e, Hand.MAIN_HAND)) {
              affectedSpots.add(e.getPosition());
            }
            e.captureDrops = false;
            for (ItemEntity ent : e.capturedDrops) {
              ent.setPosition(entity.posX, entity.posY, entity.posZ);
              ent.motionY = 0;
              ent.motionX = 0;
              ent.motionZ = 0;
              ent.world.spawnEntity(ent);
            }
          }

          MessageRunicShearsAOEFX message = new MessageRunicShearsAOEFX(affectedSpots);
          PacketHandler.sendToAllTracking(message, world.provider.getDimension(), pos);
        }
      }

      if (successful) {
        source.getWorld().playSound(null, source.getBlockPos(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
      }
    }

    return stack;
  }
}