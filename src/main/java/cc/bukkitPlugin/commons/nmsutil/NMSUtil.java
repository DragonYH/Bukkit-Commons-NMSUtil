package cc.bukkitPlugin.commons.nmsutil;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.bukkitPlugin.commons.nmsutil.nbt.NBTSerializer;
import cc.bukkitPlugin.commons.nmsutil.nbt.NBTUtil;
import cc.commons.util.reflect.ClassUtil;
import cc.commons.util.reflect.MethodUtil;

/**
 * 一个用于获取NMS类的类
 * 
 * @author 聪聪
 *
 */
public class NMSUtil{

    public static String mTestVersion="v1_7_R4";
    /**
     * MC版本,请勿直接获取,请使用{@link ClassUtil#getServerVersion()}来获取
     */
    @Deprecated
    private static String mMCVersion;

    /**
     * NMS类
     */
    public static final Class<?> clazz_EntityPlayerMP;
    public static final Class<?> clazz_EntityPlayer;
    public static final Class<?> clazz_NMSEntity;
    public static final Class<?> clazz_NMSItemStack;
    public static final Class<?> clazz_NMSWorld;
    public static final Class<?> clazz_IInventory;

    public static final Method method_CraftItemStack_asNMSCopy;
    public static final Method method_CraftItemStack_asCraftMirror;
    public static final Method method_CraftPlayer_getHandle;
    public static final Method method_CraftEntity_getHandle;

    /**
     * CraftBukkit类
     */
    public static final Class<?> clazz_CraftItemStack;
    public static final Class<?> clazz_CraftInventory;
    public static final Class<?> clazz_CraftPlayer;

    static{
        clazz_CraftItemStack=NMSUtil.getCBTClass("inventory.CraftItemStack");
        clazz_CraftInventory=NMSUtil.getCBTClass("inventory.CraftInventory");
        Method method_CraftInventory_getInventory=MethodUtil.getMethod(clazz_CraftInventory,"getInventory",true);
        clazz_IInventory=method_CraftInventory_getInventory.getReturnType();

        Class<?> tEntityClazz=NMSUtil.getCBTClass("entity.CraftEntity");
        method_CraftEntity_getHandle=MethodUtil.getMethod(tEntityClazz,"getHandle",true);
        clazz_NMSEntity=method_CraftEntity_getHandle.getReturnType();
        
        clazz_CraftPlayer=NMSUtil.getCBTClass("entity.CraftPlayer");
        method_CraftPlayer_getHandle=MethodUtil.getMethod(clazz_CraftPlayer,"getHandle",true);
        clazz_EntityPlayerMP=method_CraftPlayer_getHandle.getReturnType();
        clazz_EntityPlayer=clazz_EntityPlayerMP.getSuperclass();
        
        clazz_NMSWorld=clazz_EntityPlayer.getDeclaredConstructors()[0].getParameterTypes()[0];

        // NMS ItemStck
        ItemStack tItem=new ItemStack(Material.STONE);
        method_CraftItemStack_asNMSCopy=MethodUtil.getMethod(clazz_CraftItemStack,"asNMSCopy",ItemStack.class,true);
        Object tNMSItemStack_mcitem=MethodUtil.invokeStaticMethod(method_CraftItemStack_asNMSCopy,tItem);
        clazz_NMSItemStack=tNMSItemStack_mcitem.getClass();
        method_CraftItemStack_asCraftMirror=MethodUtil.getMethod(clazz_CraftItemStack,"asCraftMirror",clazz_NMSItemStack,true);
    }

    /**
     * 获取服务的Bukkit版本
     */
    public static String getServerVersion(){
        if(mMCVersion==null){
            if(Bukkit.getServer()!=null){
                String className=Bukkit.getServer().getClass().getPackage().getName();
                mMCVersion=className.substring(className.lastIndexOf('.')+1);
            }else mMCVersion=mTestVersion;
        }
        return mMCVersion;
    }

    /**
     * 获取org.bukkit.craftbukkit类的全名
     * 
     * @param pName
     *            短名字
     * @return 完整名字
     */
    public static String getCBTName(String pName){
        return "org.bukkit.craftbukkit."+NMSUtil.getServerVersion()+"."+pName;
    }

    /**
     * 获取craftbukkit类的{@link Class}对象
     * 
     * @param pClazz
     *            craftbukkit类短名字,(org.bukkit.craftbukkit.version后的名字)
     */
    public static Class<?> getCBTClass(String pClazz){
        return ClassUtil.getClass(getCBTName(pClazz));
    }

    /**
     * 获取NMS类的全名
     * 
     * @param pName
     *            短名字
     * @return 完整名字
     */
    public static String getNMSName(String pName){
        return "net.minecraft.server."+NMSUtil.getServerVersion()+"."+pName;
    }

    /**
     * 获取NMS类的{@link Class}对象
     * 
     * @param pClazz
     *            NMS类短名字
     */
    public static Class<?> getNMSClass(String pClazz){
        return ClassUtil.getClass(getNMSName(pClazz));
    }

    /**
     * 获取Bukkit物品对应的MC物品
     * 
     * @param pItem
     *            Bukkit物品实例
     * @return NMS ItemStack实例或者null
     */
    public static Object getNMSItem(ItemStack pItem){
        if(pItem==null||pItem.getType()==Material.AIR)
            return null;
        return MethodUtil.invokeStaticMethod(method_CraftItemStack_asNMSCopy,pItem);
    }

    /**
     * 获取NMS物品对应的Bukkit物品
     * 
     * @param pItem
     *            NMS物品实例
     * @return Bukkit物品实例或者null
     */
    public static ItemStack getCBTItem(Object pNMSItem){
        if(!NMSUtil.clazz_NMSItemStack.isInstance(pNMSItem))
            return null;

        Object tItem=MethodUtil.invokeStaticMethod(NMSUtil.method_CraftItemStack_asCraftMirror,pNMSItem);
        if(!ItemStack.class.isInstance(tItem))
            return null;
        return (ItemStack)tItem;
    }

    /**
     * 转换一个纯bukkit物品为由NMS物品镜像后的物品
     * 
     * @param pItem
     *            bukkit物品
     * @return 转换失败则返回源物品
     */
    public static ItemStack convertItemToNMSFormat(ItemStack pItem){
        if(pItem==null)
            return null;

        ItemStack tItem=pItem.clone();
        int tAmount=tItem.getAmount();
        tItem.setAmount(1);

        Object nmsItem=NMSUtil.getNMSItem(tItem);
        if(nmsItem==null||(tItem=NMSUtil.getCBTItem(nmsItem))==null)
            return pItem;

        tItem.setAmount(tAmount);
        return tItem;
    }

    /**
     * 获取物品用于发送Tellraw的Json字符串
     * <p>
     * 由于Tellraw的Json中,部分值可能被修改,因此该Json不适合用作存储与序列化
     * </p>
     */
    public static String getItemTellrawJson(ItemStack pItem){
        if(pItem==null||pItem.getType()==Material.AIR)
            return "{}";

        StringBuilder tItemJson=new StringBuilder("{id:");
        Object tNMSItem=NMSUtil.getNMSItem(pItem);
        if(tNMSItem!=null){
            Object tTag=NBTUtil.saveItemToNBT_NMS(tNMSItem);
            tItemJson.append(NBTUtil.invokeNBTTagCompound_get(tTag,"id"));
        }else{
            tItemJson.append(pItem.getTypeId()).append('s');
        }
        tItemJson.append(",Damage:").append(pItem.getDurability());
        Object tagNBTTagCompound=NBTUtil.getItemNBT(pItem);
        if(tagNBTTagCompound!=null)
            tItemJson.append(",tag:").append(NBTSerializer.serializeNBTToTellrawJson(tagNBTTagCompound));
        tItemJson.append('}');
        return tItemJson.toString();
    }

    /**
     * 获取NMS玩家
     * 
     * @param pPlayer
     *            Bukkit玩家
     * @return NMS玩家或null
     */
    public static Object getNMSPlayer(Player pPlayer){
        if(pPlayer!=null){
            if(NMSUtil.clazz_CraftPlayer.isInstance(pPlayer)){
                return MethodUtil.invokeMethod(NMSUtil.method_CraftPlayer_getHandle,pPlayer);
            }else{
                return NMSUtil.getNMSEntity(pPlayer);
            }
        }
        return null;
    }

    public static Object getNMSEntity(Entity pEntity){
        if(pEntity!=null&&NMSUtil.method_CraftEntity_getHandle.getDeclaringClass().isInstance(pEntity)){
            return MethodUtil.invokeMethod(NMSUtil.method_CraftEntity_getHandle,pEntity);
        }
        return null;
    }

}
