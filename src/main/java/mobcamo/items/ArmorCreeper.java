package mobcamo.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class ArmorCreeper extends ArmorMob
{
	IIcon acIcon;
	
	public ArmorCreeper(ArmorMaterial material, int renderIndex, int type)
	{
		super(material, renderIndex, type);
		this.setTextureName("skull_creeper");
		this.setUnlocalizedName("creeperCamo");
		this.setCreativeTab(CreativeTabs.tabCombat);
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		NBTTagCompound tags = stack.getTagCompound();
		
		if(tags == null)
		{
			//tags = new NBTTagCompound();
			//stack.setTagCompound(tags);
			return "mobcamo:textures/models/armor/t1_creeperarmor_layer_" + (slot == 2 ? 2 : 1) + ".png";
		}
		
		if(tags.getInteger("MOB_ARMOR_TIER") == 0)
		{
			return "mobcamo:textures/models/armor/t1_creeperarmor_layer_" + (slot == 2 ? 2 : 1) + ".png";
		} else if(tags.getInteger("MOB_ARMOR_TIER") == 1)
		{
			return "mobcamo:textures/models/armor/t2_creeperarmor_layer_" + (slot == 2 ? 2 : 1) + ".png";
		} else if(tags.getInteger("MOB_ARMOR_TIER") == 2)
		{
			return "mobcamo:textures/models/armor/t3_creeperarmor_layer_" + (slot == 2 ? 2 : 1) + ".png";
		} else
		{
			return "mobcamo:textures/models/armor/t1_creeperarmor_layer_" + (slot == 2 ? 2 : 1) + ".png";
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.acIcon = par1IconRegister.registerIcon("skull_creeper");
		super.registerIcons(par1IconRegister);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1)
	{
		if(acIcon != null)
		{
			return this.acIcon;
		} else
		{
			return super.getIconFromDamage(par1);
		}
	}
	
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return false;
	}
}
