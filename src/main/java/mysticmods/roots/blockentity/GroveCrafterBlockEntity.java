package mysticmods.roots.blockentity;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.grove.GroveCrafting;
import mysticmods.roots.recipe.grove.GroveInventoryWrapper;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import noobanidus.libs.noobutil.util.ItemUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GroveCrafterBlockEntity extends UseDelegatedBlockEntity implements ServerTickBlockEntity {
  private GroveRecipe lastRecipe = null;
  private GroveRecipe cachedRecipe = null;

  public GroveCrafterBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    ItemStack inHand = player.getItemInHand(hand);
    if (inHand.is(RootsTags.Items.GROVE_CRAFTER_ACTIVATION)) {
      GroveCrafting playerCrafting = new GroveCrafting(new GroveInventoryWrapper(pedestals()), this, player);
      if (cachedRecipe == null) {
        cachedRecipe = ResolvedRecipes.GROVE.findRecipe(playerCrafting, getLevel());
      }
      if (cachedRecipe == null) {
        return InteractionResult.FAIL;
      }
      // TODO: Provider better feedback to the player
      RootsRecipe.ConditionResult conditionResult = cachedRecipe.checkConditions(level, player, PyreBlockEntity.PYRE_BOUNDS, pos);
      if (conditionResult.anyFailed()) {
        RootsAPI.LOG.info("Conditions failed.");
        conditionResult.failedLevelConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
        conditionResult.failedPlayerConditions().forEach(o -> RootsAPI.LOG.info("Failed: " + o.getDescriptionId()));
        return InteractionResult.FAIL;
      }
      lastRecipe = cachedRecipe;
      ItemStack result = cachedRecipe.assemble(playerCrafting);
      NonNullList<ItemStack> processed = cachedRecipe.process(playerCrafting.popItems());
      ItemUtil.Spawn.spawnItem(level, player.blockPosition(), result);
      for (ItemStack stack : processed) {
        ItemUtil.Spawn.spawnItem(level, player.blockPosition(), stack);
      }
      cachedRecipe = null;
      setChanged();
      updateViaState();

      return InteractionResult.SUCCESS;
    }

    return InteractionResult.FAIL;
  }

  protected List<PedestalBlockEntity> pedestals() {
    List<PedestalBlockEntity> pedestals = new ArrayList<>();
    for (BlockPos pedestal : pedestalPositions()) {
      if (getLevel().getBlockEntity(pedestal) instanceof PedestalBlockEntity pedestalBlockEntity) {
        pedestals.add(pedestalBlockEntity);
      }
    }
    return pedestals;
  }

  protected void revalidateRecipe() {
    List<PedestalBlockEntity> pedestals = pedestals();
    if (pedestals.isEmpty()) {
      return;
    }
    GroveCrafting playerlessCrafting = new GroveCrafting(new GroveInventoryWrapper(pedestals), this, null);
    boolean changed = false;
    if (cachedRecipe == null) {
      cachedRecipe = ResolvedRecipes.GROVE.findRecipe(playerlessCrafting, getLevel());
      if (cachedRecipe != null) {
        changed = true;
      }
    } else {
      if (!cachedRecipe.matches(playerlessCrafting, getLevel())) {
        cachedRecipe = null;
        changed = true;
      }
    }

    if (changed) {
      setChanged();
      updateViaState();
    }
  }

  @Override
  public int getRadiusX() {
    return 3;
  }

  @Override
  public int getRadiusY() {
    return 2;
  }

  @Override
  public int getRadiusZ() {
    return 3;
  }

  public List<BlockPos> pedestalPositions() {
    List<BlockPos> pedestalPositions = new ArrayList<>();
    if (getBoundingBox() != null) {
      BlockPos.betweenClosedStream(getBoundingBox()).forEach(pos -> {
        BlockState state = getLevel().getBlockState(pos);
        if (state.is(RootsTags.Blocks.GROVE_PEDESTALS)) {
          if (getLevel().getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
            // Already checks for empty
            if (!pedestal.getHeldItem().isEmpty()) {
              pedestalPositions.add(pos.immutable());
            }
          }
        }
      });
    }
    return pedestalPositions;
  }

/*  @Override
  public void notify(ServerLevel pLevel, BlockPos pPos) {
    if (pedestalPositions == null) {
      pedestalPositions = new ArrayList<>();
    } else {
      pedestalPositions.clear();
    }
    BlockPos.betweenClosedStream(getBoundingBox()).forEach(pos -> {
      BlockState state = pLevel.getBlockState(pos);
      if (state.is(RootsTags.Blocks.GROVE_PEDESTALS)) {
        if (!state.getValue(PedestalBlock.VALID)) {
*//*          if (pLevel.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {*//*
   *//*            if (!pedestal.getHeldItem().isEmpty()) {*//*
              pLevel.setBlock(pos, state.setValue(PedestalBlock.VALID, true), 1 | 2 | 8);
              pedestalPositions.add(pos.immutable());
*//*            }*//*
   *//*          }*//*
        } else {
          pedestalPositions.add(pos.immutable());
        }
      }
    });
    revalidateRecipe();
  }*/

  @Override
  protected void saveAdditional(CompoundTag pTag) {
    super.saveAdditional(pTag);
    if (cachedRecipe != null) {
      pTag.putString("cached_recipe", cachedRecipe.getId().toString());
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.getId().toString());
    }
  }

  @Override
  public void load(CompoundTag pTag) {
    super.load(pTag);
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      ResourceLocation cachedId = new ResourceLocation(pTag.getString("cached_recipe"));
      cachedRecipe = ResolvedRecipes.GROVE.getRecipe(cachedId);
    } else {
      cachedRecipe = null;
    }
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      ResourceLocation lastId = new ResourceLocation(pTag.getString("last_recipe"));
      lastRecipe = ResolvedRecipes.GROVE.getRecipe(lastId);
    } else {
      lastRecipe = null;
    }
  }

  @Nullable
  public GroveRecipe getRecipe() {
    return cachedRecipe;
  }

  @Override
  public void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
    MinecraftServer server = pLevel.getServer();
    if (server != null && server.getTickCount() % 20 == 0) {
      revalidateRecipe();
    }
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    super.onDataPacket(net, pkt);
    CompoundTag tag = pkt.getTag();
    if (tag != null) {
      load(tag);
    } else {
      lastRecipe = null;
      cachedRecipe = null;
    }
  }
}
