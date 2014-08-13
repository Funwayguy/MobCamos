package mobcamo.ai;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mobcamo.handlers.RegistryHandler;
import mobcamo.items.ArmorMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class CamoAIUtils
{
	public static HashMap<Class<? extends EntityLivingBase>, ArrayList<ItemArmor>> creatureToCamo = new HashMap<Class<? extends EntityLivingBase>, ArrayList<ItemArmor>>();
	public static HashMap<ItemArmor, ArrayList<Class<? extends EntityLivingBase>>> camoToCreature = new HashMap<ItemArmor, ArrayList<Class<? extends EntityLivingBase>>>();
	
	public static void SetupCamoMap()
	{
		AddMapping(RegistryHandler.creeperHat, EntityCreeper.class);
		AddMapping(RegistryHandler.creeperShirt, EntityCreeper.class);
		AddMapping(RegistryHandler.creeperPants, EntityCreeper.class);
		AddMapping(RegistryHandler.creeperShoes, EntityCreeper.class);

		AddMapping(RegistryHandler.skeletonHat, EntitySkeleton.class);
		AddMapping(RegistryHandler.skeletonShirt, EntitySkeleton.class);
		AddMapping(RegistryHandler.skeletonPants, EntitySkeleton.class);
		AddMapping(RegistryHandler.skeletonShoes, EntitySkeleton.class);

		AddMapping(RegistryHandler.spiderHat, EntitySpider.class);
		AddMapping(RegistryHandler.spiderShirt, EntitySpider.class);
		AddMapping(RegistryHandler.spiderPants, EntitySpider.class);
		AddMapping(RegistryHandler.spiderShoes, EntitySpider.class);

		AddMapping(RegistryHandler.ghastHat, EntityGhast.class);
		AddMapping(RegistryHandler.ghastShirt, EntityGhast.class);
		AddMapping(RegistryHandler.ghastPants, EntityGhast.class);
		AddMapping(RegistryHandler.ghastShoes, EntityGhast.class);
	}
	
	public static void AddMapping(ItemArmor armor, Class<? extends EntityLivingBase> creature)
	{
		if(creatureToCamo.containsKey(creature))
		{
			ArrayList<ItemArmor> armorList = creatureToCamo.get(creature);
			armorList.add(armor);
			creatureToCamo.put(creature, armorList);
		} else
		{
			ArrayList<ItemArmor> armorList = new ArrayList<ItemArmor>();
			armorList.add(armor);
			creatureToCamo.put(creature, armorList);
		}
		
		if(camoToCreature.containsKey(armor))
		{
			ArrayList<Class<? extends EntityLivingBase>> armorList = camoToCreature.get(armor);
			armorList.add(creature);
			camoToCreature.put(armor, armorList);
		} else
		{
			ArrayList<Class<? extends EntityLivingBase>> armorList = new ArrayList<Class<? extends EntityLivingBase>>();
			armorList.add(creature);
			camoToCreature.put(armor, armorList);
		}
	}
	
	public static void ReplaceAI(EntityCreature creature)
	{
		boolean replaced = false;
		int priority = 0;
		
		@SuppressWarnings("unchecked")
		List<EntityAITaskEntry> taskList = creature.targetTasks.taskEntries;
		
		for(int i = taskList.size() - 1; i >= 0; i--)
		{
			EntityAITaskEntry task = taskList.get(i);
			
			if(task.action instanceof EntityAINearestAttackableTarget)
			{
				priority = task.priority;
				taskList.remove(i);
				replaced = true;
				break;
			}
		}
		
		if(replaced)
		{
			creature.targetTasks.addTask(priority, new CamoNearestAttackableTarget(creature, EntityPlayer.class, 0, true));
		}
	}
	
	public static void requestBackup(EntityLivingBase entityLiving, EntityLivingBase target, Class<? extends EntityLivingBase> backupEntity)
	{
		if(target.isDead)
		{
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<EntityLiving> recruits = entityLiving.worldObj.getEntitiesWithinAABB(backupEntity, entityLiving.boundingBox.expand(16F, 16F, 16F));
		
		for(int i = 0; i < recruits.size(); i++)
		{
			EntityLiving subject = recruits.get(i);
			subject.setAttackTarget(target);
			
			if(subject instanceof EntityCreature && subject != target)
			{
				((EntityCreature)subject).setTarget(target);
			}
		}
	}
	
	public static List<Class<? extends EntityLivingBase>> getBackupTypes(EntityLivingBase entityLiving)
	{
		ArrayList<Class<? extends EntityLivingBase>> typeList = new ArrayList<Class<? extends EntityLivingBase>>();
		Class<? extends ItemArmor> clazz = null;
		
		for(int i = 1; i <= 4; i++)
		{
			ItemStack stack = entityLiving.getEquipmentInSlot(i);
			
			if(stack != null && stack.getItem() instanceof ArmorMob)
			{
				ArmorMob armor = (ArmorMob)stack.getItem();
				
				if(clazz == null)
				{
					clazz = armor.getClass();
				} else
				{
					if(!clazz.isInstance(armor))
					{
						return new ArrayList<Class<? extends EntityLivingBase>>();
					}
				}
				
				if(camoToCreature.containsKey(stack.getItem()))
				{
					typeList.addAll(camoToCreature.get(stack.getItem()));
				}
			}
		}
		
		return typeList;
	}
	
	/**
	 * Get the maximum tier this set represents. All pieces must be part of the same set.
	 * @param entityLiving
	 * @return
	 */
	public static int getMaxTier(EntityLivingBase entityLiving)
	{
		int tier = -1;
		Class<? extends ItemArmor> clazz = null;
		
		for(int i = 1; i <= 4; i++)
		{
			ItemStack stack = entityLiving.getEquipmentInSlot(i);
			
			if(stack != null && stack.getItem() instanceof ArmorMob)
			{
				ArmorMob armor = (ArmorMob)stack.getItem();
				
				if(clazz == null)
				{
					clazz = armor.getClass();
				} else
				{
					if(!clazz.isInstance(armor))
					{
						return 0;
					}
				}
				
				if(tier == -1)
				{
					tier = armor.getTier(stack);
				} else
				{
					if(armor.getTier(stack) <= tier)
					{
						tier = armor.getTier(stack);
					}
				}
			} else
			{
				return 0;
			}
		}
		
		return tier;
	}
	
	public static boolean HasCamo(EntityLivingBase entityLiving)
	{
		String name = (String)EntityList.classToStringMapping.get(entityLiving.getClass());
		
		if(name != null && creatureToCamo.containsKey(name))
		{
			return true;
		} else
		{
			return false;
		}
	}
	
	public static double getReducedVisibility(EntityLivingBase attacker, EntityLivingBase target, double origDist)
	{
		double mult = 1.0D;
		boolean dropped = false;
		
		for(int i = 1; i <= 4; i++)
		{
			ItemStack stack = target.getEquipmentInSlot(i);
			
			if(stack != null && stack.getItem() instanceof ArmorMob)
			{
				ArmorMob camo = (ArmorMob)stack.getItem();
				double camoTier = camo.getTier(stack) + 1D;
				
				if(creatureToCamo.get(EntityList.classToStringMapping.get(attacker.getClass())).contains(camo))
				{
					mult -= (0.084D * camoTier);
					dropped = true;
				}
			}
		}
		
		if(dropped)
		{
			return origDist * mult;
		} else
		{
			return origDist;
		}
	}
	
	public static void refreshTarget(Entity entity)
	{
		if(entity instanceof EntityCreature)
		{
			refreshTargetCreature((EntityCreature)entity);
		} else if(entity instanceof EntityGhast)
		{
			refreshTargetGhast((EntityGhast)entity);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void refreshTargetCreature(EntityCreature entity)
	{
		Entity target = entity.getEntityToAttack();
		
		if(entity.targetTasks.taskEntries.size() >= 1 || !(entity instanceof IMob))
		{
			return;
		}
		
		if(target != null && target instanceof EntityLivingBase)
		{
			if(getMaxTier((EntityLivingBase)target) == 2 && getBackupTypes((EntityLivingBase)target).contains(entity.getClass()) && target != entity.getLastAttacker())
			{
				entity.setTarget(null);
				if(entity.hasPath())
				{
					entity.setPathToEntity(null);
				}
			} else
			{
				return;
			}
		} else if(target != null)
		{
			return;
		}
		
		EntityLivingBase closestTarget = null;
		ArrayList<EntityPlayer> targets = new ArrayList<EntityPlayer>();
		
		targets.addAll(entity.worldObj.getEntitiesWithinAABB(EntityPlayer.class, entity.boundingBox.expand(16D, 4.0D, 16D)));
		
		double dist = 17D;
		
		for(int i = 0; i < targets.size(); i++)
		{
			EntityLivingBase subject = targets.get(i);
			
			if(subject.isDead)
			{
				continue;
			}
			
			if(subject instanceof EntityPlayer)
			{
				EntityPlayer tmpPlayer = (EntityPlayer)subject;
				
				if(tmpPlayer.capabilities.isCreativeMode)
				{
					continue;
				}
			}
			
			if(entity.getDistanceToEntity(subject) < dist && (entity instanceof EntitySpider || entity.canEntityBeSeen(subject)))
			{
				if(!(getMaxTier(subject) == 2 && getBackupTypes((EntityLivingBase)subject).contains(entity.getClass())))
				{
					closestTarget = subject;
					dist = entity.getDistanceToEntity(subject);
				}
			}
		}
		
		entity.setTarget(closestTarget);
		
		if(closestTarget != null)
		{
			entity.setPathToEntity(entity.worldObj.getPathEntityToEntity(entity, closestTarget, 16, true, false, false, true));
		} else
		{
			entity.setPathToEntity(null);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void refreshTargetGhast(EntityGhast ghast)
	{
		Entity target = getGhastTarget(ghast);
		
		if(target != null && target instanceof EntityLivingBase)
		{
			if(getMaxTier((EntityLivingBase)target) == 2 && getBackupTypes((EntityLivingBase)target).contains(ghast.getClass()) && target != ghast.getLastAttacker())
			{
				setGhastTarget(ghast, null);
				ghast.attackCounter = 0;
			} else
			{
				return;
			}
		} else if(target != null)
		{
			return;
		}
		
		EntityLivingBase closestTarget = null;
		ArrayList<EntityPlayer> targets = new ArrayList<EntityPlayer>();
		
		targets.addAll(ghast.worldObj.getEntitiesWithinAABB(EntityPlayer.class, ghast.boundingBox.expand(16D, 4.0D, 16D)));
		
		double dist = 101D;
		
		for(int i = 0; i < targets.size(); i++)
		{
			EntityLivingBase subject = targets.get(i);
			
			if(subject.isDead)
			{
				continue;
			}
			
			if(subject instanceof EntityPlayer)
			{
				EntityPlayer tmpPlayer = (EntityPlayer)subject;
				
				if(tmpPlayer.capabilities.isCreativeMode)
				{
					continue;
				}
			}
			
			if(ghast.getDistanceToEntity(subject) < dist && ghast.canEntityBeSeen(subject))
			{
				if(!(getMaxTier(subject) == 2 && getBackupTypes((EntityLivingBase)subject).contains(ghast.getClass())))
				{
					closestTarget = subject;
					dist = ghast.getDistanceToEntity(subject);
				}
			}
		}
		
		setGhastTarget(ghast, closestTarget);
		
		if(closestTarget == null)
		{
			ghast.attackCounter = 0;
		}
	}
	
	private static void setGhastTarget(EntityGhast ghast, Entity target)
	{
		Field field = null;
		try
		{
			field = EntityGhast.class.getDeclaredField("targetedEntity");
		} catch(NoSuchFieldException e)
		{
			try
			{
				field = EntityGhast.class.getDeclaredField("field_70792_g");
			} catch(NoSuchFieldException e1)
			{
				e.printStackTrace();
				e1.printStackTrace();
				return;
			} catch(SecurityException e1)
			{
				e.printStackTrace();
				e1.printStackTrace();
				return;
			}
		} catch(SecurityException e)
		{
			try
			{
				field = EntityGhast.class.getDeclaredField("field_70792_g");
			} catch(NoSuchFieldException e1)
			{
				e.printStackTrace();
				e1.printStackTrace();
				return;
			} catch(SecurityException e1)
			{
				e.printStackTrace();
				e1.printStackTrace();
				return;
			}
		}
		
		field.setAccessible(true);
		
		try
		{
			field.set(ghast, target);
		} catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			return;
		} catch(IllegalAccessException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	private static Entity getGhastTarget(EntityGhast ghast)
	{
		Entity flag = null;
		
		Field field = null;
		try
		{
			field = EntityGhast.class.getDeclaredField("targetedEntity");
		} catch(NoSuchFieldException e)
		{
			try
			{
				field = EntityGhast.class.getDeclaredField("field_70792_g");
			} catch(NoSuchFieldException e1)
			{
				e.printStackTrace();
				e1.printStackTrace();
				return flag;
			} catch(SecurityException e1)
			{
				e.printStackTrace();
				e1.printStackTrace();
				return flag;
			}
		} catch(SecurityException e)
		{
			try
			{
				field = EntityGhast.class.getDeclaredField("field_70792_g");
			} catch(NoSuchFieldException e1)
			{
				e.printStackTrace();
				e1.printStackTrace();
				return flag;
			} catch(SecurityException e1)
			{
				e.printStackTrace();
				e1.printStackTrace();
				return flag;
			}
		}
		
		field.setAccessible(true);
		
		try
		{
			flag = (Entity)field.get(ghast);
		} catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			return flag;
		} catch(IllegalAccessException e)
		{
			e.printStackTrace();
			return flag;
		}
		
		return flag;
	}
}
