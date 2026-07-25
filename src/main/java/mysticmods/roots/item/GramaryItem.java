package mysticmods.roots.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.blockentity.BindableBlockEntity;
import mysticmods.roots.api.spell.Cycling;
import mysticmods.roots.init.ModAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

public class GramaryItem extends Item {
  public GramaryItem(Properties properties) {
    super(properties);
  }

  @Override
  public Component getName(ItemStack pStack) {
    GramaryMode mode = pStack.get(ModAttachments.GRAMARY_MODE);
    return Component.translatable("roots.item.gramary.with_mode", mode.getStyledName());
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    ItemStack stack = context.getItemInHand();
    if (getMode(stack) != GramaryMode.BIND_POSITION) {
      return super.useOn(context);
    }

    Level level = context.getLevel();
    if (level.isClientSide()) {
      return InteractionResult.CONSUME;
    }

    BlockPos pos = context.getClickedPos();

    boolean hasBound = stack.has(ModAttachments.BOUND_POSITION);
    BlockPos boundPos = null;
    if (hasBound) {
      boundPos = stack.get(ModAttachments.BOUND_POSITION);
    }

    BlockEntity blockEntity = level.getBlockEntity(pos);

    if (hasBound && blockEntity instanceof BindableBlockEntity bindable) {
      bindable.setBoundPosition(boundPos);
      // TODO: Send a message to say that it was bound
      if (context.getPlayer() != null) {
        context.getPlayer()
            .displayClientMessage(Component.translatable("roots.item.gramary.bound_block_entity", pos.getX(), pos.getY(), pos.getZ()), true);
      }
    } else {
      pos = pos.above();
      stack.set(ModAttachments.BOUND_POSITION, pos);
      if (context.getPlayer() != null) {
        context.getPlayer()
            .displayClientMessage(Component.translatable("roots.item.gramary.bound_block_position", pos.getX(), pos.getY(), pos.getZ()), true);
      }
    }

    return InteractionResult.SUCCESS;
  }

  public static GramaryMode getMode(ItemStack item) {
    if (item.has(ModAttachments.GRAMARY_MODE)) {
      return item.get(ModAttachments.GRAMARY_MODE);
    }

    return GramaryMode.NONE;
  }

  public enum GramaryMode implements Cycling<GramaryMode>, StringRepresentable {
    // TODO: Encode properly
    NONE(ChatFormatting.GRAY.getColor()),
    ENTITY_INFORMATION(ChatFormatting.DARK_PURPLE.getColor()),
    BLOCK_INFORMATION(ChatFormatting.AQUA.getColor()),
    BIND_POSITION(ChatFormatting.DARK_GREEN.getColor());

    public static final Codec<GramaryMode> CODEC = StringRepresentable.fromEnum(GramaryMode::values);
    public static final IntFunction<GramaryMode> BY_ID = ByIdMap.continuous(GramaryMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, GramaryMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, GramaryMode::ordinal);

    private final TextColor color;
    private Style style;
    private String descriptionId;

    GramaryMode (int color) {
      this.color = TextColor.fromRgb(color);
    }

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public TextColor getTextColor() {
      return color;
    }

    @Override
    public Style getOrCreateStyle() {
      if (this.style == null) {
        this.style = Style.EMPTY.withColor(getTextColor());
      }
      return style;
    }

    @Override
    public String getOrCreateDescriptionId() {
      if (this.descriptionId == null) {
        this.descriptionId = Util.makeDescriptionId("gramary_mode", RootsAPI.rl(this.getSerializedName()));
      }
      return descriptionId;
    }

    @Override
    public GramaryMode[] valuesInternal() {
      return values();
    }
  }
}
