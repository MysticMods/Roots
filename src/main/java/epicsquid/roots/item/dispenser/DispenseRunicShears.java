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
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class DispenseRunicShears implements IBehaviorDispenseItem {
  private static final DispenseRunicShears INSTANCE = new DispenseRunicShears();

  public static DispenseRunicShears getInstance() {
    return INSTANCE;
  }

  private DispenseRunicShears() {
  }

  @Override
  public ItemStack dispense(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    boolean successful = false;
    if (!world.isRemote) {
      EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
      BlockPos pos = source.getBlockPos().offset(facing);
      IBlockState targetState = world.getBlockState(pos);
      IBlockState moss = MossConfig.scrapeResult(targetState);
      IBlockState moss2 = MossConfig.mossConversion(targetState);
      Block block = targetState.getBlock();

      // AoE moss
      if (moss != null || moss2 != null) {
        AxisAlignedBB bounds = ItemRunicShears.bounding.offset(pos);
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
            ItemUtil.spawnItem(world, pos.up(), new ItemStack(ModItems.terra_moss));
          }
        }
        if (!affectedBlocks.isEmpty()) {
          if (stack.attemptDamageItem(1 + Math.min(6, world.rand.nextInt(affectedBlocks.size())), world.rand, FakePlayerFactory.getMinecraft((WorldServer) world))) {
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
          if (block instanceof BlockCrops) {
            BlockCrops crop = (BlockCrops) block;
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
            if (stack.attemptDamageItem(1, world.rand, FakePlayerFactory.getMinecraft((WorldServer) world))) {
              stack.setCount(0);
            }
            MessageRunicShearsBlockFX message = new MessageRunicShearsBlockFX(pos);
            PacketHandler.sendToAllTracking(message, world.provider.getDimension(), pos);
          }
        }
      }

      // Runic Shears individual recipe
      if (!successful) {
        List<Entity> entities = world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(pos), e -> !(e instanceof EntityPlayer) && e instanceof EntityLivingBase);
        if (!entities.isEmpty()) {
          Entity entity = null;
          RunicShearEntityRecipe recipe = null;
          RunicShearsCapability cap = null;
          while (!entities.isEmpty()) {
            entity = entities.remove(world.rand.nextInt(entities.size()));
            recipe = ModRecipes.getRunicShearRecipe((EntityLivingBase) entity);
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
            EntityItem ent = entity.entityDropItem(recipe.getDrop().copy(), 1.0F);
            ent.motionY += Util.rand.nextFloat() * 0.05F;
            ent.motionX += (Util.rand.nextFloat() - Util.rand.nextFloat()) * 0.1F;
            ent.motionZ += (Util.rand.nextFloat() - Util.rand.nextFloat()) * 0.1F;
            if (stack.attemptDamageItem(1, world.rand, FakePlayerFactory.getMinecraft((WorldServer) world))) {
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
          Items.SHEARS.itemInteractionForEntity(stack, FakePlayerFactory.getMinecraft((WorldServer) world), (EntityLivingBase) entity, EnumHand.MAIN_HAND);

          List<BlockPos> affectedSpots = new ArrayList<>();
          affectedSpots.add(entity.getPosition());

          float radius = GeneralConfig.RunicShearsRadius;
          List<EntityLiving> entities = Util.getEntitiesWithinRadius(entity.world, (Entity e) -> e instanceof IShearable, entity.getPosition(), radius, radius / 2, radius);
          for (EntityLiving e : entities) {
            e.captureDrops = true;
            if (Items.SHEARS.itemInteractionForEntity(stack, FakePlayerFactory.getMinecraft((WorldServer) world), e, EnumHand.MAIN_HAND)) {
              affectedSpots.add(e.getPosition());
            }
            e.captureDrops = false;
            for (EntityItem ent : e.capturedDrops) {
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