package mobcamo.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ArmorMob extends ItemArmor
{
	public ArmorMob(ArmorMaterial material, int renderIndex, int type)
	{
		super(material, renderIndex, type);
	}
	
	public int getTier(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		
		if(tags == null)
		{
			tags = new NBTTagCompound();
			stack.setTagCompound(tags);
		}
		
		return tags.getInteger("MOB_ARMOR_TIER");
	}
	
	public void setTier(ItemStack stack, int num)
	{
		NBTTagCompound tags = stack.getTagCompound();
		
		if(tags == null)
		{
			tags = new NBTTagCompound();
			stack.setTagCompound(tags);
		}
		
		tags.setInteger("MOB_ARMOR_TIER", num);
	}

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
		NBTTagCompound tags = stack.getTagCompound();
		
		if(tags == null)
		{
			tags = new NBTTagCompound();
			stack.setTagCompound(tags);
		}
		
		if(tags.getInteger("MOB_ARMOR_TIER") == 2)
		{
			return true;
		} else
		{
			return stack.isItemEnchanted();
		}
    }
}
