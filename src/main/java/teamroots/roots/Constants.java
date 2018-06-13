package teamroots.roots;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.proxy.CommonProxy;

public class Constants {
	public static String TIME_STOP_TAG = Roots.MODID+":time_stop";
	public static String MIND_WARD_TAG = Roots.MODID+":mind_ward";
	public static String LIGHT_DRIFTER_TAG = Roots.MODID+":light_drifter";
	public static String LIGHT_DRIFTER_X = Roots.MODID+":light_drifter_x";
	public static String LIGHT_DRIFTER_Y = Roots.MODID+":light_drifter_y";
	public static String LIGHT_DRIFTER_Z = Roots.MODID+":light_drifter_z";
	public static String LIGHT_DRIFTER_MODE = Roots.MODID+":light_drifter_mode";
	public static String INVULNERABILITY_RITUAL_TAG = Roots.MODID+":invulnerability_ritual";
	public static String INVULNERABILITY_RITUAL_TAG_ID = Roots.MODID+":invulnerability_ritual_id";
	public static String TRAP_PAPER_ENTITY_TAG = Roots.MODID+":trapped_entity";
	public static String TRAP_PAPER_ENTITY_DATA_TAG = Roots.MODID+":trapped_entity_data";
	public static String EFFECT_TAG = Roots.MODID+":effects";
	public static String BOOK_OF_THE_DEAD_LIST = Roots.MODID+":book_dead_list";
	public static String FOLLOW_EFFECT_TARGET = Roots.MODID+":follow_target";
	public static String KNOWLEDGE = Roots.MODID+":knowledge";
}
