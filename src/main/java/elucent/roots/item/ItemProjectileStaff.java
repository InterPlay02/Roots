package elucent.roots.item;

import java.util.List;
import java.util.Random;

import net.minecraft.util.text.TextFormatting;

import elucent.roots.Roots;
import elucent.roots.RootsCapabilityManager;
import elucent.roots.Util;
import elucent.roots.capability.mana.ManaProvider;
import elucent.roots.component.ComponentBase;
import elucent.roots.component.ComponentManager;
import elucent.roots.component.EnumCastType;
import elucent.roots.entity.EntitySpellProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProjectileStaff extends ItemCastingBase implements IManaRelatedItem {
	Random random = new Random();
	
	public ItemProjectileStaff(){
		super("projectileStaff",4);
	}
	
	@Override
	public double getCost(ComponentBase comp, double potency, double efficiency){
		return super.getCost(comp, potency+1, efficiency);
	}
	
	@Override
	public int getUseCount(double efficiency){
		return 257+(int)(128*efficiency);
	}
	
	@Override
	public void doEffect(World world, EntityPlayer player, ComponentBase component, double potency, double efficiency, double size){
		if (!world.isRemote){
			EntitySpellProjectile proj = new EntitySpellProjectile(world);
			proj.setPositionAndRotation(player.posX+player.getLookVec().xCoord*1.5, player.posY+player.getEyeHeight()+player.getLookVec().yCoord*1.5, player.posZ+player.getLookVec().zCoord*1.5,(float)Math.toRadians(-player.getRotationYawHead()),(float)Math.toRadians(player.rotationPitch));
			proj.onInitialSpawn(world.getDifficultyForLocation(player.getPosition()), null);
			proj.initSpecial(component, potency, efficiency, size, player.getUniqueID(),Math.toRadians(-player.getRotationYawHead()),Math.toRadians(player.rotationPitch));
			world.spawnEntityInWorld(proj);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(),"inventory"));
	}
	
	public static class ColorHandler implements IItemColor {
		public ColorHandler(){
			//
		}
		@Override
		public int getColorFromItemstack(ItemStack stack, int layer) {
			if (stack.hasTagCompound()){
				if (layer == 2){
					ComponentBase comp = ComponentManager.getComponentFromName(getEffect(stack));
					if (comp != null){
						return Util.intColor((int)comp.primaryColor.xCoord,(int)comp.primaryColor.yCoord,(int)comp.primaryColor.zCoord);
					}
				}
				if (layer == 1){
					ComponentBase comp = ComponentManager.getComponentFromName(getEffect(stack));
					if (comp != null){
						return Util.intColor((int)comp.secondaryColor.xCoord,(int)comp.secondaryColor.yCoord,(int)comp.secondaryColor.zCoord);
					}
				}
			}
			return Util.intColor(255, 255, 255);
		}
	}
}
