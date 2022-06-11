package mysticmods.roots.block.entity;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.ServerTickBlockEntity;
import mysticmods.roots.block.entity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.recipe.grove.GroveCrafting;
import mysticmods.roots.recipe.grove.GroveInventoryWrapper;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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
    return InteractionResult.SUCCESS;
  }

  protected void revalidateRecipe() {
    GroveRecipe original = cachedRecipe;
    List<PedestalBlockEntity> pedestals = new ArrayList<>();
    for (BlockPos pedestal : pedestalPositions()) {
      if (getLevel().getBlockEntity(pedestal) instanceof PedestalBlockEntity pedestalBlockEntity) {
        pedestals.add(pedestalBlockEntity);
      }
    }
    if (pedestals.isEmpty()) {
      return;
    }
    GroveCrafting playerlessCrafting = new GroveCrafting(GroveInventoryWrapper.of(pedestals), this, null);
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
          pedestalPositions.add(pos.immutable());
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
    } else {
      pTag.putString("cached_recipe", "NO_RECIPE");
    }
    if (lastRecipe != null) {
      pTag.putString("last_recipe", lastRecipe.getId().toString());
    }
  }

  @Override
  public void load(CompoundTag pTag) {
    super.load(pTag);
    if (pTag.contains("cached_recipe", Tag.TAG_STRING)) {
      String value = pTag.getString("cached_recipe");
      if (value.equals("NO_RECIPE")) {
        cachedRecipe = null;
      } else {
        ResourceLocation cachedId = new ResourceLocation(value);
        cachedRecipe = ResolvedRecipes.GROVE.getRecipe(cachedId);
      }
    }
    if (pTag.contains("last_recipe", Tag.TAG_STRING)) {
      ResourceLocation lastId = new ResourceLocation(pTag.getString("last_recipe"));
      lastRecipe = ResolvedRecipes.GROVE.getRecipe(lastId);
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
    }
  }
}
