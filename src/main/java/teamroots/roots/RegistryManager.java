package teamroots.roots;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.roots.block.*;
import teamroots.roots.entity.*;
import teamroots.roots.item.*;
import teamroots.roots.tileentity.*;
import teamroots.roots.util.Misc;
import teamroots.roots.world.WorldGenBarrow;
import teamroots.roots.world.WorldGenFairyPool;
import teamroots.roots.world.WorldGenGarden;
import teamroots.roots.world.WorldGenHut;
import teamroots.roots.world.WorldGenLeyMarker;
import teamroots.roots.world.WorldGenStandingStones;

public class RegistryManager {
	public static ArrayList<Block> blocks = new ArrayList<Block>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	public static IWorldGenerator worldGenGarden, worldGenFairyPool, worldGenBarrow, worldGenStandingStones, worldGenHut, worldGenLeyMarker;
	
	public static Block offertory_plate, fairy_dust, chiseled_runestone, runestone_brick, runestone, structure_marker, bonfire, spirit_herb, imbuer, moonglow, terra_moss, wildroot, pereskia, aubergine, mortar, thatch;
	
	public static Item rift_wand, spritely_brew, fairy_charm, book_base, ritual_book, dwindle_dust, totem_fragment, spirit_herb_item, firestarter, gold_hammer, diamond_hammer, iron_hammer, stone_hammer, wood_hammer, gold_knife, diamond_knife, iron_knife, stone_knife, wood_knife, bark_oak, bark_birch, bark_spruce, bark_jungle, bark_dark_oak, bark_acacia, wood_shears, spellcraft_book, herblore_book, pouch, straw, staff, moontinged_seed, moonglow_leaf, terra_moss_spore, terra_moss_ball, wildroot_item, pestle, petal_dust, aubergine_item, aubergine_seeds, pereskia_blossom, pereskia_bulb;
	
	public static void registerAll(){
		blocks.add(moonglow = new BlockMoonglow("moonglow",true));
		blocks.add(terra_moss = new BlockTerraMoss("terra_moss",true));
		blocks.add(wildroot = new BlockWildroot("wildroot",true));
		blocks.add(aubergine = new BlockAubergine("aubergine",true));
		blocks.add(pereskia = new BlockPereskiaFlower("pereskia",true));
		blocks.add(mortar = new BlockMortar(Material.GROUND,SoundType.STONE,"mortar",true).setIsFullCube(false).setIsOpaqueCube(false).setBoundingBox(new AxisAlignedBB(0.3125,0,0.3125,0.6875,0.4375,0.6875)).setHardness(1.4f).setLightOpacity(0));
		blocks.add(imbuer = new BlockImbuer(Material.GROUND,SoundType.STONE,"imbuer",true).setIsFullCube(false).setIsOpaqueCube(false).setBoundingBox(new AxisAlignedBB(0.3125,0,0.3125,0.6875,0.125,0.6875)).setHardness(1.4f));
		blocks.add(thatch = new BlockThatch(Material.LEAVES,SoundType.PLANT,"thatch",true).setIsFullCube(true).setIsOpaqueCube(true).setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED).setHardness(0.8f).setLightOpacity(0));
		blocks.add(bonfire = new BlockBonfire(Material.GROUND,SoundType.WOOD,"bonfire",true).setIsFullCube(false).setIsOpaqueCube(false).setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED).setBoundingBox(new AxisAlignedBB(0.25,0,0.25,0.75,0.1875,0.75)).setHardness(1.4f));
		blocks.add(spirit_herb = new BlockSpiritHerb("spirit_herb",true));
		blocks.add(structure_marker = new BlockStructureMarker());
		blocks.add(runestone = new BlockBase(Material.ROCK,SoundType.STONE,"runestone",true).setIsFullCube(true).setIsOpaqueCube(true).setHardness(1.2f).setLightOpacity(15));
		blocks.add(runestone_brick = new BlockBase(Material.ROCK,SoundType.STONE,"runestone_brick",true).setIsFullCube(true).setIsOpaqueCube(true).setHardness(1.2f).setLightOpacity(15));
		blocks.add(chiseled_runestone = new BlockBase(Material.ROCK,SoundType.STONE,"chiseled_runestone",true).setIsFullCube(true).setIsOpaqueCube(true).setHardness(1.2f).setLightOpacity(15));
		blocks.add(fairy_dust = new BlockFairyDust(Material.CIRCUITS,SoundType.SNOW,"fairy_dust",true).setIsFullCube(false).setIsOpaqueCube(false).setBoundingBox(new AxisAlignedBB(0.3125,0.3125,0.3125,0.6875,0.6875,0.6875)).setHardness(0.1f).setLightOpacity(0).setLightLevel(1.0f));
		blocks.add(offertory_plate = new BlockOffertoryPlate(Material.ROCK,SoundType.STONE,"offertory_plate",true).setIsFullCube(false).setIsOpaqueCube(false).setBoundingBox(new AxisAlignedBB(0.125,0.0,0.125,0.875,0.875,0.875)).setHardness(1.6f).setLightOpacity(0));
		
		items.add(moontinged_seed = new ItemMoonglowSeed("moontinged_seed",true));
		items.add(moonglow_leaf = new ItemHerb("moonglow_leaf",true));
		items.add(terra_moss_spore = new ItemTerraMossSeed("terra_moss_spore",true));
		items.add(terra_moss_ball = new ItemHerb("terra_moss_ball",true));
		items.add(staff = new ItemStaff("staff",true));
		items.add(wildroot_item = new ItemWildroot("wildroot_item",true));
		items.add(pereskia_bulb = new ItemPereskiaBulb("pereskia_bulb",true));
		items.add(pereskia_blossom = new ItemHerb("pereskia_blossom",true));
		items.add(aubergine_seeds = new ItemAubergineSeed("aubergine_seeds",true));
		items.add(aubergine_item = new ItemFoodBase("aubergine_item",true,2,1.0f));
		items.add(pestle = new ItemPestle("pestle",true));
		items.add(petal_dust = new ItemPetalDust("petal_dust",true));
		items.add(pouch = new ItemPouch("pouch",true));
		items.add(straw = new ItemBase("straw",true));
		items.add(herblore_book = new ItemBook("herblore_book",true, 0));
		items.add(spellcraft_book = new ItemBook("spellcraft_book",true, 2));
		items.add(wood_shears = new ItemShearsBase("wood_shears",true, 70));
		items.add(bark_oak = new ItemBase("bark_oak",true));
		items.add(bark_spruce = new ItemBase("bark_spruce",true));
		items.add(bark_birch = new ItemBase("bark_birch",true));
		items.add(bark_jungle = new ItemBase("bark_jungle",true));
		items.add(bark_dark_oak = new ItemBase("bark_dark_oak",true));
		items.add(bark_acacia = new ItemBase("bark_acacia",true));
		items.add(wood_knife = new ItemKnife("wood_knife",true,ToolMaterial.WOOD));
		items.add(wood_hammer = new ItemHammer("wood_hammer",true,ToolMaterial.WOOD));
		items.add(stone_knife = new ItemKnife("stone_knife",true,ToolMaterial.STONE));
		items.add(stone_hammer = new ItemHammer("stone_hammer",true,ToolMaterial.STONE));
		items.add(iron_knife = new ItemKnife("iron_knife",true,ToolMaterial.IRON));
		items.add(iron_hammer = new ItemHammer("iron_hammer",true,ToolMaterial.IRON));
		items.add(diamond_knife = new ItemKnife("diamond_knife",true,ToolMaterial.DIAMOND));
		items.add(diamond_hammer = new ItemHammer("diamond_hammer",true,ToolMaterial.DIAMOND));
		items.add(gold_knife = new ItemKnife("gold_knife",true,ToolMaterial.GOLD));
		items.add(gold_hammer = new ItemHammer("gold_hammer",true,ToolMaterial.GOLD));
		items.add(firestarter = new ItemLighterBase("firestarter",true, 11));
		items.add(spirit_herb_item = new ItemSpiritHerb("spirit_herb_item",true));
		items.add(dwindle_dust = new ItemSpiritHerb("dwindle_dust",true));
		items.add(totem_fragment = new ItemSpiritHerb("totem_fragment",true));
		items.add(book_base = new ItemBase("book_base",true));
		items.add(ritual_book = new ItemBook("ritual_book",true, 4));
		items.add(fairy_charm = new ItemFairyCharm("fairy_charm",true));
		items.add(spritely_brew = new ItemSpritelyBrew("spritely_brew",true));
		
		GameRegistry.registerWorldGenerator(worldGenStandingStones = new WorldGenStandingStones(), 100);
		GameRegistry.registerWorldGenerator(worldGenHut = new WorldGenHut(), 101);
		GameRegistry.registerWorldGenerator(worldGenLeyMarker = new WorldGenLeyMarker(), 102);
		GameRegistry.registerWorldGenerator(worldGenBarrow = new WorldGenBarrow(), 103);
		GameRegistry.registerWorldGenerator(worldGenFairyPool = new WorldGenFairyPool(), 104);
		GameRegistry.registerWorldGenerator(worldGenGarden = new WorldGenGarden(), 105);
		
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":auspicious_point"), EntityAuspiciousPoint.class, "auspicious_point", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":fire_jet"), EntityFireJet.class, "fire_jet", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":thorn_trap"), EntityThornTrap.class, "thorn_trap", id ++, Roots.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":petal_shell"), EntityPetalShell.class, "petal_shell", id ++, Roots.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":time_stop"), EntityTimeStop.class, "time_stop", id ++, Roots.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":boost"), EntityBoost.class, "boost", id ++, Roots.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":deer"), EntityDeer.class, "deer", id ++, Roots.instance, 64, 1, true);
		EntityRegistry.registerEgg(new ResourceLocation(Roots.MODID+":deer"), Misc.intColor(161, 132, 88), Misc.intColor(94, 77, 51));
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":ritual_life"), EntityRitualLife.class, "ritual_life", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":ritual_storm"), EntityRitualStorm.class, "ritual_storm", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":ritual_light"), EntityRitualLight.class, "ritual_light", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":ritual_fire_storm"), EntityRitualFireStorm.class, "ritual_fire_storm", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":flare"), EntityFlare.class, "flare", id ++, Roots.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":ritual_regrowth"), EntityRitualRegrowth.class, "ritual_regrowth", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":ritual_windwall"), EntityRitualWindwall.class, "ritual_windwall", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":sprout"), EntitySprout.class, "sprout", id ++, Roots.instance, 64, 1, true);
		EntityRegistry.registerEgg(new ResourceLocation(Roots.MODID+":sprout"), Misc.intColor(136, 191, 33), Misc.intColor(165, 232, 42));
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":barrow"), EntityBarrow.class, "barrow", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":ritual_warden"), EntityRitualWarden.class, "ritual_warden", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":fairy"), EntityFairy.class, "fairy", id ++, Roots.instance, 64, 1, true);
		EntityRegistry.registerEgg(new ResourceLocation(Roots.MODID+":fairy"), Misc.intColor(255, 214, 255), Misc.intColor(209, 255, 173));
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":fairy_circle"), EntityFairyCircle.class, "fairy_circle", id ++, Roots.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Roots.MODID+":blink_projectile"), EntityBlinkProjectile.class, "blink_projectile", id ++, Roots.instance, 64, 1, true);
		
		List<BiomeEntry> biomeEntries = new ArrayList<BiomeEntry>();
		biomeEntries.addAll(BiomeManager.getBiomes(BiomeType.COOL));
		biomeEntries.addAll(BiomeManager.getBiomes(BiomeType.DESERT));
		biomeEntries.addAll(BiomeManager.getBiomes(BiomeType.ICY));
		biomeEntries.addAll(BiomeManager.getBiomes(BiomeType.WARM));
		List<Biome> biomes = new ArrayList<Biome>();
		for (BiomeEntry b : biomeEntries){
			biomes.add(b.biome);
		}
		biomes.addAll(BiomeManager.oceanBiomes);
		
		if (ConfigManager.deerSpawnWeight > 0){
			EntityRegistry.addSpawn(EntityDeer.class, ConfigManager.deerSpawnWeight, 3, 7, EnumCreatureType.CREATURE, biomes.toArray(new Biome[biomes.size()]));
		}
		if (ConfigManager.sproutSpawnWeight > 0){
			EntityRegistry.addSpawn(EntitySprout.class, ConfigManager.sproutSpawnWeight, 3, 7, EnumCreatureType.CREATURE, biomes.toArray(new Biome[biomes.size()]));
		}
		
		GameRegistry.registerTileEntity(TileEntityMortar.class,Roots.MODID+":tile_entity_mortar");
		GameRegistry.registerTileEntity(TileEntityImbuer.class,Roots.MODID+":tile_entity_imbuer");
		GameRegistry.registerTileEntity(TileEntityBonfire.class,Roots.MODID+":tile_entity_bonfire");
		GameRegistry.registerTileEntity(TileEntityOffertoryPlate.class,Roots.MODID+":tile_entity_offertory_plate");
	}
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){
    	for (Block b : blocks){
    		event.getRegistry().register(b);
    	}
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
    	for (Item i : items){
    		event.getRegistry().register(i);
    	}
    	for (Block b : blocks){
    		if (b instanceof IBlock){
    			event.getRegistry().register(((IBlock)b).getItemBlock());
    		}
    	}
    }
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
    public void registerRendering(ModelRegistryEvent event){
		for (int i = 0; i < blocks.size(); i ++){
			if (blocks.get(i) instanceof IModeledBlock){
				((IModeledBlock)blocks.get(i)).initModel();
			}
		}
		for (int i = 0; i < items.size(); i ++){
			if (items.get(i) instanceof IModeledItem){
				((IModeledItem)items.get(i)).initModel();
			}
		}
		RenderingRegistry.registerEntityRenderingHandler(EntityAuspiciousPoint.class, new RenderAuspiciousPoint.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFireJet.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityThornTrap.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityPetalShell.class, new RenderPetalShell.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityTimeStop.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityBoost.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityDeer.class, new RenderDeer.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityRitualLife.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityRitualStorm.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityRitualLight.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityRitualFireStorm.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFlare.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityRitualRegrowth.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityRitualWindwall.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntitySprout.class, new RenderSprout.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityBarrow.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityRitualWarden.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFairy.class, new RenderFairy.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFairyCircle.class, new RenderNull.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityBlinkProjectile.class, new RenderNull.Factory());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMortar.class, new TileEntityMortarRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityImbuer.class, new TileEntityImbuerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBonfire.class, new TileEntityBonfireRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOffertoryPlate.class, new TileEntityOffertoryPlateRenderer());
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerColorHandlers(){
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemStaff.StaffColorHandler(), staff);
	}
}
