package cc.bukkitPlugin.commons.nmsutil.nbt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import cc.bukkitPlugin.commons.nmsutil.NMSUtil;
import cc.commons.util.ClassUtil;

public class NBTUtil{

    public static final Class<?> clazz_NMSItemStack;
    public static final Class<?> clazz_NBTBase;
    public static final Class<?> clazz_NBTTagByte;
    public static final Class<?> clazz_NBTTagShort;
    public static final Class<?> clazz_NBTTagInt;
    public static final Class<?> clazz_NBTTagLong;
    public static final Class<?> clazz_NBTTagFloat;
    public static final Class<?> clazz_NBTTagDouble;
    public static final Class<?> clazz_NBTTagEnd;
    public static final Class<?> clazz_NBTTagByteArray;
    public static final Class<?> clazz_NBTTagString;
    public static final Class<?> clazz_NBTTagIntArray;
    public static final Class<?> clazz_NBTTagList;
    public static final Class<?> clazz_NBTTagCompound;

    public static final Method method_NMSItemStack_getTag;
    public static final Method method_NMSItemStack_setTag;
    public static final Method method_NMSItemStack_saveToNBT;
    public static final Method method_NMSItemStack_loadFromNBT;

    public static final Method method_CraftItemStack_asNMSCopy;
    public static final Method method_NBTBase_getTypeId;
    public static final Method method_NBTTagCompound_isEmpty;
    public static final Method method_NBTTagCompound_clone;
    public static final Method method_NBTTagCompound_hasKeyOfType;
    public static final Method method_NBTTagCompound_get;
    public static final Method method_NBTTagCompound_getInt;
    public static final Method method_NBTTagCompound_getString;
    public static final Method method_NBTTagCompound_getList;
    public static final Method method_NBTTagCompound_set;
    public static final Method method_NBTTagList_add;

    public static final Field field_NBTTagString_value;
    public static final Field field_NBTTagByte_value;
    public static final Field field_NBTTagShort_value;
    public static final Field field_NBTTagInt_value;
    public static final Field field_NBTTagFloat_value;
    public static final Field field_NBTTagDouble_value;
    public static final Field field_NBTTagLong_value;
    public static final Field field_NBTTagList_value;
    public static final Field field_NBTTagByteArray_value;
    public static final Field field_NBTTagIntArray_value;
    public static final Field field_NBTTagCompound_map;
    public static final Field field_NMSItemStack_tag;

    static{
        String className=Bukkit.getServer().getClass().getPackage().getName();
        String tMCVersion=className.substring(className.lastIndexOf('.')+1);
        Class<?> tClassCraftItemStack=ClassUtil.getClass("org.bukkit.craftbukkit."+tMCVersion+".inventory.CraftItemStack");
        ItemStack tItem=new ItemStack(Material.STONE);
        method_CraftItemStack_asNMSCopy=ClassUtil.getMethod(tClassCraftItemStack,"asNMSCopy",ItemStack.class);
        Object tNMSItemStack_mcitem=ClassUtil.invokeMethod(method_CraftItemStack_asNMSCopy,null,tItem);
        clazz_NMSItemStack=tNMSItemStack_mcitem.getClass();

        method_NMSItemStack_getTag=ClassUtil.getUnknowMethod(NBTUtil.clazz_NMSItemStack,"NBTTagCompound").get(0);
        clazz_NBTTagCompound=method_NMSItemStack_getTag.getReturnType();
        String tPacketPath=ClassUtil.getClassPacket(clazz_NBTTagCompound.getName());
        clazz_NBTBase=ClassUtil.getClass(tPacketPath+"NBTBase");
        clazz_NBTTagByte=ClassUtil.getClass(tPacketPath+"NBTTagByte");
        clazz_NBTTagShort=ClassUtil.getClass(tPacketPath+"NBTTagShort");
        clazz_NBTTagInt=ClassUtil.getClass(tPacketPath+"NBTTagInt");
        clazz_NBTTagLong=ClassUtil.getClass(tPacketPath+"NBTTagLong");
        clazz_NBTTagFloat=ClassUtil.getClass(tPacketPath+"NBTTagFloat");
        clazz_NBTTagDouble=ClassUtil.getClass(tPacketPath+"NBTTagDouble");
        clazz_NBTTagEnd=ClassUtil.getClass(tPacketPath+"NBTTagEnd");
        clazz_NBTTagByteArray=ClassUtil.getClass(tPacketPath+"NBTTagByteArray");
        clazz_NBTTagString=ClassUtil.getClass(tPacketPath+"NBTTagString");
        clazz_NBTTagIntArray=ClassUtil.getClass(tPacketPath+"NBTTagIntArray");
        clazz_NBTTagList=ClassUtil.getClass(tPacketPath+"NBTTagList");

        method_NBTBase_getTypeId=ClassUtil.getUnknowMethod(clazz_NBTBase,byte.class).get(0);
        method_NBTTagCompound_isEmpty=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,boolean.class).get(0);
        method_NBTTagCompound_clone=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,clazz_NBTBase).get(0);
        method_NBTTagCompound_hasKeyOfType=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,boolean.class,new Class<?>[]{String.class,int.class}).get(0);
        method_NBTTagCompound_get=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,clazz_NBTBase,String.class).get(0);
        method_NBTTagCompound_getInt=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,int.class,String.class).get(0);
        method_NBTTagCompound_getString=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,String.class,String.class).get(0);
        method_NBTTagCompound_getList=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,clazz_NBTTagList,new Class<?>[]{String.class,int.class}).get(0);
        method_NBTTagCompound_set=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,void.class,new Class<?>[]{String.class,clazz_NBTBase}).get(0);
        method_NBTTagList_add=ClassUtil.getUnknowMethod(clazz_NBTTagList,void.class,clazz_NBTBase).get(0);

        field_NBTTagByte_value=ClassUtil.getField(clazz_NBTTagByte,byte.class,-1).get(0);
        field_NBTTagShort_value=ClassUtil.getField(clazz_NBTTagShort,short.class,-1).get(0);
        field_NBTTagInt_value=ClassUtil.getField(clazz_NBTTagInt,int.class,-1).get(0);
        field_NBTTagFloat_value=ClassUtil.getField(clazz_NBTTagFloat,float.class,-1).get(0);
        field_NBTTagDouble_value=ClassUtil.getField(clazz_NBTTagDouble,double.class,-1).get(0);
        field_NBTTagLong_value=ClassUtil.getField(clazz_NBTTagLong,long.class,-1).get(0);
        field_NBTTagString_value=ClassUtil.getField(clazz_NBTTagString,String.class,Modifier.PRIVATE).get(0);
        field_NBTTagList_value=ClassUtil.getField(clazz_NBTTagList,List.class,-1).get(0);
        field_NBTTagByteArray_value=ClassUtil.getField(clazz_NBTTagByteArray,byte[].class,-1).get(0);
        field_NBTTagIntArray_value=ClassUtil.getField(clazz_NBTTagIntArray,int[].class,-1).get(0);
        field_NBTTagCompound_map=ClassUtil.getField(clazz_NBTTagCompound,Map.class,-1).get(0);
        field_NMSItemStack_tag=ClassUtil.getField(NBTUtil.clazz_NMSItemStack,clazz_NBTTagCompound,-1).get(0);
        // ItemStack
        method_NMSItemStack_saveToNBT=ClassUtil.getUnknowMethod(NBTUtil.clazz_NMSItemStack,clazz_NBTTagCompound,clazz_NBTTagCompound).get(0);
        ArrayList<Method> tMethods=ClassUtil.getUnknowMethod(NBTUtil.clazz_NMSItemStack,void.class,clazz_NBTTagCompound);
        int setTagMethodIndex=0;
        Object tTag=ClassUtil.getInstance(clazz_NBTTagCompound);
        Object tNMSItem=NBTUtil.getNMSItem(tItem);
        ClassUtil.invokeMethod(tMethods.get(setTagMethodIndex),tNMSItem,tTag);
        if(ClassUtil.getFieldValue(tNMSItem,field_NMSItemStack_tag)!=tTag){
            setTagMethodIndex=1;
        }
        method_NMSItemStack_setTag=tMethods.get(setTagMethodIndex);
        method_NMSItemStack_loadFromNBT=tMethods.get(1-setTagMethodIndex);
    }

    /**
     * 获取Bukkit物品对应的MC物品
     * 
     * @param pItem
     *            Bukkit物品实例
     * @return NMS ItemStack实例或者null
     * @see {@link NMSUtil#getNMSItem(ItemStack)}
     */
    public static Object getNMSItem(ItemStack pItem){
        if(pItem==null||pItem.getType()==Material.AIR)
            return null;
        return ClassUtil.invokeMethod(method_CraftItemStack_asNMSCopy,null,pItem);
    }

    /**
     * 获取物品的NBT
     * 
     * @param pItem
     *            Bukkit物品
     * @return 物品NBT,非null
     */
    public static Object getItemNBT(ItemStack pItem){
        return NBTUtil.getItemNBT_NMS(NBTUtil.getNMSItem(pItem));
    }

    /**
     * 获取物品的NBT
     * 
     * @param pNMSItem
     *            NMS物品
     * @return 物品NBT,非null
     */
    public static Object getItemNBT_NMS(Object pNMSItem){
        if(pNMSItem==null)
            return NBTUtil.newNBTTagCompound();
        Object tTag=ClassUtil.getFieldValue(pNMSItem,NBTUtil.field_NMSItemStack_tag);
        if(tTag==null){
            tTag=NBTUtil.newNBTTagCompound();
            NBTUtil.setItemNBT_NMS(pNMSItem,tTag);
        }
        return tTag;
    }

    /**
     * 设置物品的NBT
     * 
     * @param pItem
     *            Bukkit物品
     * @param pNBTTag
     *            NBT
     */
    public static void setItemNBT(ItemStack pItem,Object pNBTTag){
        NBTUtil.setItemNBT_NMS(NBTUtil.getNMSItem(pItem),pNBTTag);
    }

    /**
     * 设置物品的NBT
     * 
     * @param pNMSItem
     *            NMS物品
     * @param pNBTTag
     *            NBT
     */
    public static void setItemNBT_NMS(Object pNMSItem,Object pNBTTag){
        if(pNMSItem==null)
            return;
        ClassUtil.setFieldValue(NBTUtil.field_NMSItemStack_tag,pNMSItem,pNBTTag);
    }

    /**
     * 获取NBTTagCompound中的元素
     * 
     * @param pNBTTag
     *            NBTTagCompound实例
     * @param pKey
     *            键
     * @return 值或null
     */
    public static Object getFromNBTTagCompound(Object pNBTTag,String pKey){
        return ClassUtil.invokeMethod(method_NBTTagCompound_get,pNBTTag,pKey);
    }

    /**
     * 设置NBTTagCompound的元素
     * 
     * @param pNBTTag
     *            NBTTagCompound实例
     * @param pKey
     *            键
     * @param pNBTBaseValue
     *            NBTBase实例
     */
    public static void setToNBTTagCompound(Object pNBTTag,String pKey,Object pNBTBaseValue){
        ClassUtil.invokeMethod(NBTUtil.method_NBTTagCompound_set,pNBTTag,new Object[]{pKey,pNBTBaseValue});
    }

    /**
     * 保存物品数据到NBT
     * 
     * @param pItem
     *            Bukkit物品
     * @return NBT数据
     */
    public static Object saveItemToNBT(ItemStack pItem){
        return NBTUtil.saveItemToNBTNMS(NBTUtil.getNMSItem(pItem));
    }

    /**
     * 保存物品数据到NBT
     * 
     * @param pNMSItem
     *            NMS物品
     * @return NBT数据
     */
    public static Object saveItemToNBTNMS(Object pNMSItem){
        Object tTag=NBTUtil.newNBTTagCompound();
        if(pNMSItem==null){
            return tTag;
        }
        ClassUtil.invokeMethod(NBTUtil.method_NMSItemStack_saveToNBT,pNMSItem,tTag);
        return tTag;
    }

    /**
     * 克隆NBTTagCompound
     * 
     * @param pNBTTag
     *            NBTTagCompound实例
     * @return 克隆的NBTTagCompound
     */
    public static Object cloneTagCompound(Object pNBTTag){
        return ClassUtil.invokeMethod(method_NBTTagCompound_clone,pNBTTag);
    }

    /**
     * 获取NBT的值
     * <p>
     * 值中不包含类型后缀<br>
     * 例如原NBTTagShort会表示为2s,但是此函数中只会表示为2
     * </p>
     * 
     * @param pNBTObject
     * @return 类Json字符串
     */
    public static String getNBTBaseValueWithoTypeSuffix(Object pNBTObject){
        if(pNBTObject==null)
            return null;

        if(NBTUtil.clazz_NBTTagEnd.isInstance(pNBTObject)){
            return "";
        }else if(NBTUtil.clazz_NBTTagByte.isInstance(pNBTObject)){
            return String.valueOf(ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagByte_value));
        }else if(NBTUtil.clazz_NBTTagShort.isInstance(pNBTObject)){
            return String.valueOf(ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagShort_value));
        }else if(NBTUtil.clazz_NBTTagInt.isInstance(pNBTObject)){
            return String.valueOf(ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagInt_value));
        }else if(NBTUtil.clazz_NBTTagLong.isInstance(pNBTObject)){
            return String.valueOf(ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagLong_value));
        }else if(NBTUtil.clazz_NBTTagFloat.isInstance(pNBTObject)){
            return String.valueOf(ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagFloat_value));
        }else if(NBTUtil.clazz_NBTTagDouble.isInstance(pNBTObject)){
            return String.valueOf(ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagDouble_value));
        }else if(NBTUtil.clazz_NBTTagByteArray.isInstance(pNBTObject)){
            byte[] bvals=(byte[])ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagByteArray_value);
            StringBuilder tSB=new StringBuilder("[");
            for(byte sValue : bvals){
                tSB.append(sValue).append(',');
            }
            if(bvals.length>0)
                tSB.delete(tSB.length()-1,tSB.length()-1);
            return tSB.append(']').toString();
        }else if(NBTUtil.clazz_NBTTagString.isInstance(pNBTObject)){
            return String.valueOf(ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagString_value));
        }else if(NBTUtil.clazz_NBTTagIntArray.isInstance(pNBTObject)){
            int[] ivals=(int[])ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagIntArray_value);
            StringBuilder tSB=new StringBuilder("[");
            for(int sValue : ivals){
                tSB.append(sValue).append(',');
            }
            if(ivals.length>0)
                tSB.delete(tSB.length()-1,tSB.length()-1);
            return tSB.append(']').toString();
        }else if(NBTUtil.clazz_NBTTagList.isInstance(pNBTObject)){
            StringBuilder tSB=new StringBuilder("[");
            List<Object> tNBTBaseArrValue=(List<Object>)ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagList_value);
            for(Object sNBTBase : tNBTBaseArrValue){
                tSB.append(NBTUtil.getNBTBaseValueWithoTypeSuffix(sNBTBase)).append(',');
            }
            if(tNBTBaseArrValue.size()>0)
                tSB.delete(tSB.length()-1,tSB.length());
            return tSB.append(']').toString();
        }else if(NBTUtil.clazz_NBTTagCompound.isInstance(pNBTObject)){
            StringBuilder tSB=new StringBuilder("{");
            Map<Object,Object> tNBTBaseMapValue=(Map<Object,Object>)ClassUtil.getFieldValue(pNBTObject,NBTUtil.field_NBTTagCompound_map);
            for(Map.Entry<Object,Object> sEntry : tNBTBaseMapValue.entrySet()){
                tSB.append(String.valueOf(sEntry.getKey())).append(':').append(NBTUtil.getNBTBaseValueWithoTypeSuffix(sEntry.getValue())).append(',');
            }
            if(tNBTBaseMapValue.size()>0)
                tSB.delete(tSB.length()-1,tSB.length());
            return tSB.append('}').toString();
        }else{
            return String.valueOf(pNBTObject);
        }
    }

    /**
     * 获取Bukkit物品的NBT tag中第一层的标签名列表
     *
     * @param pItem
     *            Bukkit物品
     * @return 名字Set集合或null
     */
    public static Set<String> getNBTFirstDeepNameList(ItemStack pItem){
        Object nbtTag=NBTUtil.getItemNBT(pItem);
        if(nbtTag==null)
            return null;
        Map<String,Object> tMap=getNBTTagMapFromTag(nbtTag);
        Set<String> rSet=new HashSet<>();
        for(String skey : tMap.keySet())
            rSet.add(skey);
        return rSet;
    }

    /**
     * 获取物品的NBT的的值域Map
     *
     * @param pItem
     *            物品,可以为null
     * @return 可能为null
     */
    public static Map<String,Object> getNBTTagMapFromItem(ItemStack pItem){
        return getNBTTagMapFromTag(NBTUtil.getItemNBT(pItem));
    }

    /**
     * 获取类类型为NBTTagCompound的值域Map
     *
     * @param pNBTTagCompound
     *            实例,类型必须为NBTTagCompound,可以为null
     * @return Map,非null
     */
    @SuppressWarnings("unchecked")
    public static Map<String,Object> getNBTTagMapFromTag(Object pNBTTagCompound){
        if(pNBTTagCompound==null){
            return new HashMap<>(0);
        }
        return (Map<String,Object>)ClassUtil.getFieldValue(pNBTTagCompound,NBTUtil.field_NBTTagCompound_map);
    }

    /**
     * 获取类型为NBTTagList的内部List值
     * 
     * @param pNBTTagList
     *            实例,类型必须为NBTTagList,可以为null
     * @return List,非null
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getTagListValue(Object pNBTTagList){
        if(pNBTTagList==null){
            return new ArrayList<>(0);
        }
        return (List<Object>)ClassUtil.getFieldValue(pNBTTagList,NBTUtil.field_NBTTagList_value);
    }

    /**
     * 从NBTTagCompound实例中移除指定的标签
     *
     * @param pNBTTagCompound
     *            NBTTagCompound实例
     * @param pNBTLabel
     *            NBT标签
     * @return 移除的值或null
     */
    public static Object removeFromNBTTagCompound(Object pNBTTagCompound,String pNBTLabel){
        Map<String,Object> tMap=NBTUtil.getNBTTagMapFromTag(pNBTTagCompound);
        if(tMap==null)
            return null;
        return tMap.remove(pNBTLabel);
    }

    /**
     * 获取一个新的NBTTagCompound实例
     * 
     * @return 实例
     */
    public static Object newNBTTagCompound(){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagCompound);
    }

}
