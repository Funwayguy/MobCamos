package mobcamo.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public abstract class CamoTarget extends EntityAIBase
{
	/** The entity that this task belongs to */
	protected EntityCreature taskOwner;
	/**
	 * If true, EntityAI targets must be able to be seen (cannot be blocked by walls) to be suitable targets.
	 */
	protected boolean shouldCheckSight;
	/**
	 * When true, only entities that can be reached with minimal effort will be targetted.
	 */
	private boolean nearbyOnly;
	/**
	 * When nearbyOnly is true: 0 -> No target, but OK to search; 1 -> Nearby target found; 2 -> Target too far.
	 */
	private int targetSearchStatus;
	/**
	 * When nearbyOnly is true, this throttles target searching to avoid excessive pathfinding.
	 */
	private int targetSearchDelay;
	private int field_75298_g;
	private static final String __OBFID = "CL_00001626";
	
	public CamoTarget(EntityCreature creature, boolean sight)
	{
		this(creature, sight, false);
	}
	
	public CamoTarget(EntityCreature creature, boolean sight, boolean near)
	{
		this.taskOwner = creature;
		this.shouldCheckSight = sight;
		this.nearbyOnly = near;
	}
	
	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();
		
		if(entitylivingbase == null)
		{
			return false;
		} else if(!entitylivingbase.isEntityAlive())
		{
			return false;
		} else
		{
			double d0 = this.getTargetDistance();
			
			if(this.taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0)
			{
				return false;
			} else
			{
				if(this.shouldCheckSight)
				{
					if(this.taskOwner.getEntitySenses().canSee(entitylivingbase))
					{
						this.field_75298_g = 0;
					} else if(++this.field_75298_g > 60)
					{
						return false;
					}
				}
				
				return !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).theItemInWorldManager.isCreative();
			}
		}
	}
	
	protected double getTargetDistance()
	{
		IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.targetSearchStatus = 0;
		this.targetSearchDelay = 0;
		this.field_75298_g = 0;
	}
	
	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.taskOwner.setAttackTarget((EntityLivingBase)null);
	}
	
	/**
	 * A method used to see if an entity is a suitable target through a number of checks.
	 */
	protected boolean isSuitableTarget(EntityLivingBase target, boolean creative)
	{
		if(target == null)
		{
			return false;
		} else if(target == this.taskOwner)
		{
			return false;
		} else if(!target.isEntityAlive())
		{
			return false;
		} else if(!this.taskOwner.canAttackClass(target.getClass()))
		{
			return false;
		} else
		{
			if(this.taskOwner instanceof IEntityOwnable && StringUtils.isNotEmpty(((IEntityOwnable)this.taskOwner).func_152113_b()))
			{
				if(target instanceof IEntityOwnable && ((IEntityOwnable)this.taskOwner).func_152113_b().equals(((IEntityOwnable)target).func_152113_b()))
				{
					return false;
				}
				
				if(target == ((IEntityOwnable)this.taskOwner).getOwner())
				{
					return false;
				}
			} else if(target instanceof EntityPlayer && !creative && ((EntityPlayer)target).capabilities.disableDamage)
			{
				return false;
			}
			
			if(!this.taskOwner.isWithinHomeDistance(MathHelper.floor_double(target.posX), MathHelper.floor_double(target.posY), MathHelper.floor_double(target.posZ)))
			{
				return false;
			} else if(this.shouldCheckSight && !this.taskOwner.getEntitySenses().canSee(target))
			{
				return false;
			} else
			{
				if(this.nearbyOnly)
				{
					if(--this.targetSearchDelay <= 0)
					{
						this.targetSearchStatus = 0;
					}
					
					if(this.targetSearchStatus == 0)
					{
						this.targetSearchStatus = this.canEasilyReach(target) ? 1 : 2;
					}
					
					if(this.targetSearchStatus == 2)
					{
						return false;
					}
				}
				
				if(taskOwner.getDistanceToEntity(target) < CamoAIUtils.getReducedVisibility(taskOwner, target, getTargetDistance()))
				{
					return true;
				} else
				{
					return false;
				}
			}
		}
	}
	
	/**
	 * Checks to see if this entity can find a short path to the given target.
	 */
	private boolean canEasilyReach(EntityLivingBase target)
	{
		this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
		PathEntity pathentity = this.taskOwner.getNavigator().getPathToEntityLiving(target);
		
		if(pathentity == null)
		{
			return false;
		} else
		{
			PathPoint pathpoint = pathentity.getFinalPathPoint();
			
			if(pathpoint == null)
			{
				return false;
			} else
			{
				int i = pathpoint.xCoord - MathHelper.floor_double(target.posX);
				int j = pathpoint.zCoord - MathHelper.floor_double(target.posZ);
				return (double)(i * i + j * j) <= 2.25D;
			}
		}
	}
}
