package mobcamo.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mobcamo.handlers.RegistryHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ArmorSpider extends ArmorMob
{
	IIcon acIcon;
	
	public ArmorSpider(ArmorMaterial material, int renderIndex, int type)
	{
		super(material, renderIndex, type);
		this.setTextureName("spider_eye");
		this.setUnlocalizedName("spiderCamo");
		this.setCreativeTab(RegistryHandler.camoTab);
	}
	
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		int camoTier = this.getTier(stack);
		
		if(camoTier == 0)
		{
			return "mobcamo:textures/models/armor/t1_creeperarmor_layer_" + (slot == 2 ? 2 : 1) + ".png";
		} else if(camoTier == 1)
		{
			return "mobcamo:textures/models/armor/t2_creeperarmor_layer_" + (slot == 2 ? 2 : 1) + ".png";
		} else if(camoTier == 2)
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
		this.acIcon = par1IconRegister.registerIcon("spider_eye");
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
}
