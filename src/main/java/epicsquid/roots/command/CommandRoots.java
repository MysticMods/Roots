package epicsquid.roots.command;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.RayCastUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.network.MessageClearToasts;
import epicsquid.roots.properties.Property;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.util.SlaveUtil;
import epicsquid.roots.world.data.SpellLibraryData;
import epicsquid.roots.world.data.SpellLibraryRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRoots extends CommandBase {
  public CommandRoots() {
  }

  @Override
  public String getName() {
    return "roots";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/roots book | /roots activate | /roots rituals | /roots growables | /roots spells | /roots library | /roots modifiers";
  }

  @Override
  public List<String> getAliases() {
    return Collections.singletonList("roots");
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    if (sender instanceof EntityPlayerMP && args.length != 0) {
      EntityPlayerMP player = (EntityPlayerMP) sender;
      WorldServer world = player.getServerWorld();
      if (args[0].equalsIgnoreCase("slave")) {
        Entity target = RayCastUtil.mouseOverEntity(player);
        if (SlaveUtil.canBecomeSlave(target) || SlaveUtil.isSlave(target)) {
          EntityLivingBase parent = (EntityLivingBase) target;
          EntityLivingBase slave = SlaveUtil.canBecomeSlave(target) ? SlaveUtil.enslave(parent) : SlaveUtil.revert(parent);
          world.spawnEntity(slave);
          parent.setDropItemsWhenDead(false);
          parent.setDead();
          slave.setPositionAndUpdate(slave.posX, slave.posY, slave.posZ);
        }
      } else if (args[0].equalsIgnoreCase("modifiers")) {
        List<String> modifierList = new ArrayList<>();
        for (SpellBase spell : SpellRegistry.spellRegistry.values()) {
          for (IModifier modifier : spell.getModifiers()) {
            modifierList.add(modifier.getTranslationKey());
            modifierList.add(modifier.getTranslationKey() + ".desc");
          }
        }
        Path path = Paths.get("roots.log");
        try {
          Files.write(path, modifierList, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
          player.sendMessage(new TextComponentString("Unable to write roots.log"));
          return;
        }
        player.sendMessage(new TextComponentString("Modifiers written to roots.log"));
      } else if (args[0].equalsIgnoreCase("library")) {
        if (args.length == 2 && args[1].equals("fill") || args[1].equals("all")) {
          SpellLibraryData library = SpellLibraryRegistry.getData(player);
          for (SpellBase spell : SpellRegistry.getSpells()) {
            library.addSpell(spell);
          }
        } else if (args.length == 2 && args[1].equals("kill")) {
          SpellLibraryRegistry.clearData(player);
        } else {
          SpellLibraryData data = SpellLibraryRegistry.getData(player);
          for (LibrarySpellInfo info : data) {
            if (info.isObtained()) {
              player.sendMessage(new TextComponentString("Obtained: " + info.getSpell().getRegistryName()));
            }
          }
          for (LibrarySpellInfo info : data) {
            if (!info.isObtained()) {
              player.sendMessage(new TextComponentString("Unobtained: " + info.getSpell().getRegistryName()));
            }
          }
        }
      } else if (args[0].equalsIgnoreCase("activate")) {
        List<BlockPos> positions = Util.getBlocksWithinRadius(world, sender.getPosition(), 50, 50, 50, ModBlocks.grove_stone);
        for (BlockPos pos : positions) {
          IBlockState state = world.getBlockState(pos);
          if (state.getBlock() != ModBlocks.grove_stone) {
            continue;
          }

          if (state.getValue(BlockGroveStone.VALID)) {
            continue;
          }

          world.setBlockState(pos, state.withProperty(BlockGroveStone.VALID, true));
        }
      } else if (args[0].equalsIgnoreCase("book")) {
        AdvancementManager manager = world.getAdvancementManager();
        PlayerAdvancements advancements = player.getAdvancements();
        for (Advancement adv : manager.getAdvancements()) {
          ResourceLocation id = adv.getId();
          if (id.getNamespace().equals(Roots.MODID)) {
            AdvancementProgress progress = advancements.getProgress(adv);
            for (String criterion : progress.getRemaningCriteria()) {
              progress.grantCriterion(criterion);
            }
            adv.getRewards().apply(player);
          }
        }
        advancements.save();
        advancements.flushDirty(player);
        MessageClearToasts message = new MessageClearToasts();
        PacketHandler.INSTANCE.sendTo(message, player);
        Item guide = Item.REGISTRY.getObject(new ResourceLocation("patchouli", "guide_book"));
        ItemStack book = new ItemStack(guide);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("patchouli:book", "roots:roots_guide");
        book.setTagCompound(tag);
        if (!player.addItemStackToInventory(book)) {
          ItemUtil.spawnItem(world, player.getPosition(), book);
        }
      } else if (args[0].equalsIgnoreCase("growables")) {
        List<String> growablesList = new ArrayList<>();
        for (Block block : Block.REGISTRY) {
          if (block instanceof IGrowable) {
            growablesList.add(block.getRegistryName().toString());
          }
        }
        Path path = Paths.get("roots.log");
        try {
          Files.write(path, growablesList, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
          player.sendMessage(new TextComponentString("Unable to write roots.log"));
          return;
        }
        player.sendMessage(new TextComponentString("Growables written to roots.log"));
      } else if (args[0].equalsIgnoreCase("rituals")) {
        List<String> rituals = new ArrayList<>();

        for (RitualBase ritual : RitualRegistry.getRituals()) {
          PropertyTable table = ritual.getProperties();
          rituals.add("Ritual: " + ritual.getName() + (ritual.isDisabled() ? " (disabled)" : ""));
          for (Property<?> prop : table.getProperties()) {
            rituals.add("    Property: " + prop.getName());
            if (prop.hasDefaultValue()) {
              rituals.add("        Default: " + prop.getDefaultValue());
            }
            rituals.add("        Description: " + prop.getDescription());
            rituals.add("        Type: " + prop.getType().getSimpleName());
            rituals.add("        Value: " + table.get(prop));
            rituals.add("");
          }
        }

        Path path = Paths.get("roots.log");
        try {
          Files.write(path, rituals, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
          player.sendMessage(new TextComponentString("Unable to write roots.log"));
          Roots.logger.error("Unable to write to roots.log: ", e);
          return;
        }
        player.sendMessage(new TextComponentString("Rituals written to roots.log"));
      } else if (args[0].equalsIgnoreCase("spells")) {
        List<String> spells = new ArrayList<>();

        for (SpellBase spell : SpellRegistry.getSpells()) {
          PropertyTable table = spell.getProperties();
          spells.add("Spell: " + spell.getName() + (spell.isDisabled() ? " (disabled)" : ""));
          for (Property<?> prop : table.getProperties()) {
            if (prop.getType().equals(SpellBase.EnumCastType.class)) {
              continue;
            }
            spells.add("    Property: " + prop.getName());
            if (prop.hasDefaultValue()) {
              spells.add("        Default: " + prop.getDefaultValue());
            }
            spells.add("        Description: " + prop.getDescription());
            spells.add("        Type: " + prop.getType().getSimpleName());
            spells.add("        Value: " + table.get(prop));
            spells.add("");
          }
        }

        Path path = Paths.get("roots.log");
        try {
          Files.write(path, spells, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
          player.sendMessage(new TextComponentString("Unable to write roots.log"));
          return;
        }
        player.sendMessage(new TextComponentString("Spells written to roots.log"));
      }
    }
  }
}
