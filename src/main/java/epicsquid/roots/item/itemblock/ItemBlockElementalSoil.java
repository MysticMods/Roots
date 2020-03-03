package epicsquid.roots.item.itemblock;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockElementalSoil;
import epicsquid.roots.config.ElementalSoilConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.network.fx.ElementalSoilTransformFX;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockElementalSoil extends ItemBlock {

  public ItemBlockElementalSoil(Block block) {
    super(block);
  }

/*  @Override
  public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {

    Block oldblock = world.getBlockState(pos.offset(side.getOpposite())).getBlock();

    if (oldblock instanceof BlockFarmland && player.inventory.addItemStackToInventory(new ItemStack(Blocks.DIRT))) {
      pos = pos.offset(side.getOpposite());
    }
    return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
  }*/

  @Override
  public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
    BlockPos opposite = pos; // .offset(facing);
    IBlockState state = world.getBlockState(opposite);
    IBlockState stateDown = world.getBlockState(opposite.down());
    Roots.logger.info("BlockPos is " + pos.toString() + " while facing is " + facing.toString() + ". Pos state is " + state.toString() + ". State down is " + stateDown.toString());
    if (state.getBlock() == Blocks.FARMLAND || state.getBlock() instanceof BlockElementalSoil || stateDown.getBlock() == Blocks.FARMLAND || stateDown.getBlock() instanceof BlockElementalSoil) {
      if (stateDown.getBlock() == Blocks.FARMLAND || stateDown.getBlock() instanceof BlockElementalSoil) {
        opposite = opposite.down();
        state = stateDown;
      }
      IBlockState stateUp = world.getBlockState(opposite.up());
      world.setBlockToAir(opposite.up());
      ItemStack stack = player.getHeldItem(hand);
      world.setBlockToAir(opposite);
      boolean placed = false;
      if (player.canPlayerEdit(opposite, facing, stack) && world.mayPlace(this.block, opposite, true, facing, player)) {
        if (!world.isRemote) {
          placed = world.setBlockState(opposite, this.block.getDefaultState(), 11);
        }
      }
      if (!placed) {
        if (!world.isRemote) {
          world.setBlockState(opposite, state);
          world.setBlockState(opposite.up(), stateUp);
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
      } else {
        Block block = state.getBlock();
        if (block == Blocks.FARMLAND) {
          block = Blocks.DIRT;
        }
        ItemStack drop = new ItemStack(Item.getItemFromBlock(block), 1, stateDown.getBlock().damageDropped(stateDown));
        if (!player.addItemStackToInventory(drop)) {
          ItemUtil.spawnItem(world, player.getPosition(), drop);
        }
        world.setBlockState(opposite.up(), stateUp);
        if (!player.isCreative()) {
          stack.shrink(1);
          player.setHeldItem(hand, stack);
        }
        if (world.getBlockState(opposite).getBoundingBox(world, opposite).offset(opposite).intersects(player.getEntityBoundingBox())) {
          player.setPositionAndUpdate(player.posX, player.posY + 0.1, player.posZ);
        }
        return EnumActionResult.SUCCESS;
      }
    }

    return super.onItemUseFirst(player, world, pos, facing, hitX, hitY, hitZ, hand);
  }

  @Override
  public boolean onEntityItemUpdate(EntityItem entityItem) {

    if (block == ModBlocks.elemental_soil) {
      World world = entityItem.world;

      if (!world.isRemote) {
        int count = entityItem.getItem().getCount();

        //Magmatic Soil Crafting
        if (entityItem.isInLava()) {
          world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY + 0.5, entityItem.posZ,
              new ItemStack(ModBlocks.elemental_soil_fire, count)));
          PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 0), entityItem);
          entityItem.setDead();
          return true;
        }

        //Aqueous Soil Crafting
        if (entityItem.isInWater() && entityItem.ticksExisted >= ElementalSoilConfig.WaterSoilDelay) {
          world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
              new ItemStack(ModBlocks.elemental_soil_water, count)));
          PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 1), entityItem);
          entityItem.setDead();
          return true;
        }

        //Terran Soil Crafting
        if (entityItem.posY <= ElementalSoilConfig.EarthSoilMaxY && entityItem.ticksExisted >= ElementalSoilConfig.EarthSoilDelay) {
          BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(entityItem.getPosition());
          boolean found_roof = false;
          for (int i = pos.getY(); i < Math.min(ElementalSoilConfig.EarthSoilMaxY + 20, world.getChunk(pos).getHeight(pos)); i++) {
            pos.setY(i);
            if (world.isAirBlock(pos)) continue;
            found_roof = true;
          }
          if (!found_roof) return super.onEntityItemUpdate(entityItem);

          world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
              new ItemStack(ModBlocks.elemental_soil_earth, count)));
          PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 3), entityItem);
          entityItem.setDead();
          return true;
        }

        //Aeros Soil Crafting
        if (entityItem.posY >= ElementalSoilConfig.AirSoilMinY && entityItem.ticksExisted >= ElementalSoilConfig.AirSoilDelay) {
          BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(entityItem.getPosition());
          int height = world.getChunk(pos).getHeight(pos);
          if (pos.getY() < height) return super.onEntityItemUpdate(entityItem);

          world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ,
              new ItemStack(ModBlocks.elemental_soil_air, count)));
          PacketHandler.sendToAllTracking(new ElementalSoilTransformFX(entityItem.posX, entityItem.posY, entityItem.posZ, 2), entityItem);
          entityItem.setDead();
          return true;
        }
      }
    }
    return super.onEntityItemUpdate(entityItem);
  }
}
