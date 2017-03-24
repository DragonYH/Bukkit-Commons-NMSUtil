package cc.bukkitPlugin.commons.nmsutil.nbt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import cc.bukkitPlugin.commons.nmsutil.NMSUtil;
import cc.commons.util.ClassUtil;

public class NBTUtil{

    // NBTTag Type id
    public static final int NBT_End=0;
    public static final int NBT_Byte=1;
    public static final int NBT_Short=2;
    public static final int NBT_Int=3;
    public static final int NBT_Long=4;
    public static final int NBT_Float=5;
    public static final int NBT_Double=6;
    public static final int NBT_Number=99;
    public static final int NBT_ByteArray=7;
    public static final int NBT_String=8;
    public static final int NBT_List=9;
    public static final int NBT_Compound=10;
    public static final int NBT_IntArray=11;

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
    public static final Method method_NBTBase_copy;
    public static final Method method_NBTTagCompound_isEmpty;
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
        method_NBTBase_copy=ClassUtil.getUnknowMethod(clazz_NBTBase,clazz_NBTBase).get(0);
        method_NBTTagCompound_isEmpty=ClassUtil.getUnknowMethod(clazz_NBTTagCompound,boolean.class).get(0);
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
     * <p>
     * 此函数设置的目的为分离NBT模块
     * </p>
     * 
     * @param pItem
     *            Bukkit物品实例
     * @return NMS ItemStack实例或者null
     * @see {@link NMSUtil#getNMSItem(ItemStack)}
     */
    public static Object getNMSItem(ItemStack pItem){
        if(pItem==null||pItem.getType()==Material.AIR)
            return null;
        return ClassUtil.invokeMethod(NBTUtil.method_CraftItemStack_asNMSCopy,null,pItem);
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
    public static ItemStack setItemNBT(ItemStack pItem,Object pNBTTag){
        Object tNMSItem=NBTUtil.getNMSItem(pItem);
        NBTUtil.setItemNBT_NMS(tNMSItem,pNBTTag);
        return NMSUtil.getCBTItem(tNMSItem);
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
     * 保存物品数据到NBT
     * 
     * @param pItem
     *            Bukkit物品
     * @return NBT数据
     */
    public static Object saveItemToNBT(ItemStack pItem){
        return NBTUtil.saveItemToNBT_NMS(NBTUtil.getNMSItem(pItem));
    }

    /**
     * 保存物品数据到NBT
     * 
     * @param pNMSItem
     *            NMS物品
     * @return NBT数据
     */
    public static Object saveItemToNBT_NMS(Object pNMSItem){
        Object tTag=NBTUtil.newNBTTagCompound();
        if(pNMSItem==null){
            return tTag;
        }
        ClassUtil.invokeMethod(NBTUtil.method_NMSItemStack_saveToNBT,pNMSItem,tTag);
        return tTag;
    }

    /**
     * 获取NBT的值
     * <p>
     * 值中不包含类型后缀<br>
     * 例如原NBTTagShort会表示为2s,但是此函数中只会表示为2
     * </p>
     * 
     * @param pNBTBase
     * @return 类Json字符串
     */
    public static String getNBTBaseValueWithoTypeSuffix(Object pNBTBase){
        if(pNBTBase==null)
            return "";

        if(NBTUtil.clazz_NBTTagEnd.isInstance(pNBTBase)){
            return "";
        }else if(NBTUtil.clazz_NBTTagByte.isInstance(pNBTBase)){
            return String.valueOf(NBTUtil.getNBTTagByteValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagShort.isInstance(pNBTBase)){
            return String.valueOf(NBTUtil.getNBTTagShortValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagInt.isInstance(pNBTBase)){
            return String.valueOf(NBTUtil.getNBTTagIntValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagLong.isInstance(pNBTBase)){
            return String.valueOf(NBTUtil.getNBTTagLongValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagFloat.isInstance(pNBTBase)){
            return String.valueOf(NBTUtil.getNBTTagFloatValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagDouble.isInstance(pNBTBase)){
            return String.valueOf(NBTUtil.getNBTTagDoubleValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagByteArray.isInstance(pNBTBase)){
            byte[] bvals=NBTUtil.getNBTTagByteArrayValue(pNBTBase);
            StringBuilder tSB=new StringBuilder("[");
            for(byte sValue : bvals){
                tSB.append(sValue).append(',');
            }
            if(bvals.length>0)
                tSB.delete(tSB.length()-1,tSB.length()-1);
            return tSB.append(']').toString();
        }else if(NBTUtil.clazz_NBTTagString.isInstance(pNBTBase)){
            return NBTUtil.getNBTTagStringValue(pNBTBase);
        }else if(NBTUtil.clazz_NBTTagIntArray.isInstance(pNBTBase)){
            int[] ivals=NBTUtil.getNBTTagIntArrayValue(pNBTBase);
            StringBuilder tSB=new StringBuilder("[");
            for(int sValue : ivals){
                tSB.append(sValue).append(',');
            }
            if(ivals.length>0)
                tSB.delete(tSB.length()-1,tSB.length()-1);
            return tSB.append(']').toString();
        }else if(NBTUtil.clazz_NBTTagList.isInstance(pNBTBase)){
            StringBuilder tSB=new StringBuilder("[");
            List<Object> tContent=NBTUtil.getNBTTagListValue(pNBTBase);
            for(Object sNBTBase : tContent){
                tSB.append(NBTUtil.getNBTBaseValueWithoTypeSuffix(sNBTBase)).append(',');
            }
            if(tContent.size()>0)
                tSB.delete(tSB.length()-1,tSB.length());
            return tSB.append(']').toString();
        }else if(NBTUtil.clazz_NBTTagCompound.isInstance(pNBTBase)){
            StringBuilder tSB=new StringBuilder("{");
            Map<String,Object> tContent=NBTUtil.getNBTTagCompoundValue(pNBTBase);
            for(Map.Entry<String,Object> sEntry : tContent.entrySet()){
                tSB.append(sEntry.getKey()).append(':').append(NBTUtil.getNBTBaseValueWithoTypeSuffix(sEntry.getValue())).append(',');
            }
            if(tContent.size()>0)
                tSB.delete(tSB.length()-1,tSB.length());
            return tSB.append('}').toString();
        }else{
            return String.valueOf(pNBTBase);
        }
    }

    /**
     * 获取物品的NBT的的值域Map
     *
     * @param pItem
     *            物品,可以为null
     * @return 可能,非null
     */
    public static Map<String,Object> getNBTTagCompoundValueFromItem(ItemStack pItem){
        return NBTUtil.getNBTTagCompoundValue(NBTUtil.getItemNBT(pItem));
    }

    /**
     * 获取NBTTag的NBT类型
     * 
     * @param pNBTTag
     *            NBTBase实例
     * @return 类型
     */
    @SuppressWarnings("unchecked")
    public static byte getNBTTagTypeId(Object pNBTTag){
        return (byte)ClassUtil.invokeMethod(NBTUtil.method_NBTBase_getTypeId,pNBTTag);
    }

    public static Object invokeNBTTagCopy(Object pNBTTag){
        return ClassUtil.invokeMethod(NBTUtil.method_NBTBase_copy,pNBTTag);
    }

    public static Object newNBTTagEnd(){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagEnd);
    }

    public static boolean isNBTTagEnd(Object pObj){
        return NBTUtil.clazz_NBTTagEnd.isInstance(pObj);
    }

    public static Object newNBTTagByte(byte pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagByte,byte.class,pValue);
    }

    public static boolean isNBTTagByte(Object pObj){
        return NBTUtil.clazz_NBTTagByte.isInstance(pObj);
    }

    @SuppressWarnings("unchecked")
    public static byte getNBTTagByteValue(Object pNBTTagByte){
        return (byte)ClassUtil.getFieldValue(pNBTTagByte,NBTUtil.field_NBTTagByte_value);
    }

    public static Object newNBTTagShort(short pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagShort,short.class,pValue);
    }

    public static boolean isNBTTagShort(Object pObj){
        return NBTUtil.clazz_NBTTagShort.isInstance(pObj);
    }

    @SuppressWarnings("unchecked")
    public static short getNBTTagShortValue(Object pNBTTagShort){
        return (short)ClassUtil.getFieldValue(pNBTTagShort,NBTUtil.field_NBTTagShort_value);
    }

    public static Object newNBTTagInt(int pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagInt,int.class,pValue);
    }

    public static boolean isNBTTagInt(Object pObj){
        return NBTUtil.clazz_NBTTagInt.isInstance(pObj);
    }

    @SuppressWarnings("unchecked")
    public static int getNBTTagIntValue(Object pNBTTagInt){
        return (int)ClassUtil.getFieldValue(pNBTTagInt,NBTUtil.field_NBTTagInt_value);
    }

    public static Object newNBTTagLong(long pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagLong,long.class,pValue);
    }

    public static boolean isNBTTagLong(Object pObj){
        return NBTUtil.clazz_NBTTagLong.isInstance(pObj);
    }

    @SuppressWarnings("unchecked")
    public static long getNBTTagLongValue(Object pNBTTagLong){
        return (long)ClassUtil.getFieldValue(pNBTTagLong,NBTUtil.field_NBTTagLong_value);
    }

    public static Object newNBTTagFloat(float pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagFloat,float.class,pValue);
    }

    public static boolean isNBTTagFloat(Object pObj){
        return NBTUtil.clazz_NBTTagFloat.isInstance(pObj);
    }

    @SuppressWarnings("unchecked")
    public static float getNBTTagFloatValue(Object pNBTTagFloat){
        return (float)ClassUtil.getFieldValue(pNBTTagFloat,NBTUtil.field_NBTTagFloat_value);
    }

    public static Object newNBTTagDouble(double pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagDouble,double.class,pValue);
    }

    public static boolean isNBTTagDouble(Object pObj){
        return NBTUtil.clazz_NBTTagDouble.isInstance(pObj);
    }

    @SuppressWarnings("unchecked")
    public static double getNBTTagDoubleValue(Object pNBTTagDouble){
        return (double)ClassUtil.getFieldValue(pNBTTagDouble,NBTUtil.field_NBTTagDouble_value);
    }

    public static Object newNBTTagByteArray(byte[] pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagByteArray,byte[].class,pValue);
    }

    public static boolean isNBTTagByteArray(Object pObj){
        return NBTUtil.clazz_NBTTagByteArray.isInstance(pObj);
    }

    @SuppressWarnings("unchecked")
    public static byte[] getNBTTagByteArrayValue(Object pNBTTagByteArray){
        return (byte[])ClassUtil.getFieldValue(pNBTTagByteArray,NBTUtil.field_NBTTagByteArray_value);
    }

    public static Object newNBTTagString(String pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagString,String.class,pValue);
    }

    public static boolean isNBTTagString(Object pObj){
        return NBTUtil.clazz_NBTTagString.isInstance(pObj);
    }

    @SuppressWarnings("unchecked")
    public static String getNBTTagStringValue(Object pNBTTagString){
        return (String)ClassUtil.getFieldValue(pNBTTagString,NBTUtil.field_NBTTagString_value);
    }

    public static Object newNBTTagList(){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagList);
    }

    public static boolean isNBTTagList(Object pObj){
        return NBTUtil.clazz_NBTTagList.isInstance(pObj);
    }

    /**
     * 获取类型为NBTTagList的内部List值
     * 
     * @param pNBTTagList
     *            实例,类型必须为NBTTagList,可以为null
     * @return List,非null
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getNBTTagListValue(Object pNBTTagList){
        if(pNBTTagList==null){
            return new ArrayList<>(0);
        }
        return (List<Object>)ClassUtil.getFieldValue(pNBTTagList,NBTUtil.field_NBTTagList_value);
    }

    /**
     * 添加一个NBT值到NBTTagList实例中
     * 
     * @param pNBTTagList
     *            添加到的TagList
     * @param pNBTBase
     *            要添加的内容,必须是NBTBase的实例
     */
    public static void invokeNBTTagList_add(Object pNBTTagList,Object pNBTBase){
        ClassUtil.invokeMethod(NBTUtil.method_NBTTagList_add,pNBTTagList,pNBTBase);
    }

    /**
     * 获取一个新的NBTTagCompound实例
     * 
     * @return 实例
     */
    public static Object newNBTTagCompound(){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagCompound);
    }

    public static boolean isNBTTagCompound(Object pObj){
        return NBTUtil.clazz_NBTTagCompound.isInstance(pObj);
    }

    /**
     * 获取类类型为NBTTagCompound的值域Map
     *
     * @param pNBTTagCompound
     *            实例,类型必须为NBTTagCompound,可以为null
     * @return Map,非null
     */
    @SuppressWarnings("unchecked")
    public static Map<String,Object> getNBTTagCompoundValue(Object pNBTTagCompound){
        if(pNBTTagCompound==null){
            return new HashMap<>(0);
        }
        return (Map<String,Object>)ClassUtil.getFieldValue(pNBTTagCompound,NBTUtil.field_NBTTagCompound_map);
    }

    /**
     * 获取类类型为NBTTagCompound的值域Map
     *
     * @param pNBTTagCompound
     *            实例,不是NBTTagCompound类型时,将返回一个空的Map
     * @return Map,非null
     */
    @SuppressWarnings("unchecked")
    public static Map<String,Object> getNBTTagCompoundValueSafe(Object pNBTTagCompound){
        if(!NBTUtil.isNBTTagCompound(pNBTTagCompound)){
            return new HashMap<>(0);
        }
        return (Map<String,Object>)ClassUtil.getFieldValue(pNBTTagCompound,NBTUtil.field_NBTTagCompound_map);
    }

    /**
     * 获取NBTTagCompound中的元素
     * 
     * @param pNBTTagCompound
     *            NBTTagCompound实例,允许为null
     * @param pKey
     *            键
     * @return 值或null
     */
    public static Object invokeNBTTagCompound_get(Object pNBTTagCompound,String pKey){
        return pNBTTagCompound==null?null:ClassUtil.invokeMethod(NBTUtil.method_NBTTagCompound_get,pNBTTagCompound,pKey);
    }

    @SuppressWarnings("unchecked")
    public static String invokeNBTTagCompound_getString(Object pNBTTagCompound,String pKey){
        return pNBTTagCompound==null?null:(String)ClassUtil.invokeMethod(NBTUtil.method_NBTTagCompound_getString,pNBTTagCompound,pKey);
    }

    @SuppressWarnings("unchecked")
    public static int invokeNBTTagCompound_getInt(Object pNBTTagCompound,String pKey){
        return pNBTTagCompound==null?0:(int)ClassUtil.invokeMethod(NBTUtil.method_NBTTagCompound_getInt,pNBTTagCompound,pKey);
    }

    /**
     * 获取NBTTagCompound中的NBTTagList元素
     * 
     * @param pNBTTagCompound
     *            NBTTagCompound实例,允许为null
     * @param pKey
     *            键
     * @param pNBTType
     *            NBTTagList 所包含的NBT类型
     * @return NBTTagList实例或null
     */
    public static Object invokeNBTTagCompound_getList(Object pNBTTagCompound,String pKey,int pNBTType){
        return pNBTTagCompound==null?null:ClassUtil.invokeMethod(NBTUtil.method_NBTTagCompound_getList,pNBTTagCompound,new Object[]{pKey,pNBTType});
    }

    /**
     * 设置NBTTagCompound的元素
     * 
     * @param pNBTTagCompound
     *            NBTTagCompound实例,不能null
     * @param pKey
     *            键
     * @param pNBTBase
     *            NBTBase实例
     */
    public static void invokeNBTTagCompound_set(Object pNBTTagCompound,String pKey,Object pNBTBase){
        ClassUtil.invokeMethod(NBTUtil.method_NBTTagCompound_set,pNBTTagCompound,new Object[]{pKey,pNBTBase});
    }

    /**
     * 从NBTTagCompound实例中移除指定的标签
     *
     * @param pNBTTagCompound
     *            NBTTagCompound实例,允许为null
     * @param pKey
     *            NBT标签
     * @return 移除的值或null
     */
    public static Object invokeNBTTagCompound_remove(Object pNBTTagCompound,String pKey){
        Map<String,Object> tValue=NBTUtil.getNBTTagCompoundValue(pNBTTagCompound);
        return tValue==null?null:tValue.remove(pKey);
    }

    /**
     * 克隆NBTTagCompound
     * 
     * @param pNBTTagCompound
     *            NBTTagCompound实例,允许为null
     * @return 克隆的NBTTagCompound,或新的空NBTTagCompound
     */
    public static Object invokeNBTTagCompound_clone(Object pNBTTagCompound){
        return pNBTTagCompound!=null?NBTUtil.newNBTTagCompound():ClassUtil.invokeMethod(NBTUtil.method_NBTBase_copy,pNBTTagCompound);
    }

    public static Object newNBTTagIntArray(int[] pValue){
        return ClassUtil.getInstance(NBTUtil.clazz_NBTTagIntArray,int[].class,pValue);
    }

    @SuppressWarnings("unchecked")
    public static int[] getNBTTagIntArrayValue(Object pNBTTagIntArray){
        return (int[])ClassUtil.getFieldValue(pNBTTagIntArray,NBTUtil.field_NBTTagIntArray_value);
    }

    /**
     * 混合两个NBTTagCompound,并将混合结果放置打新的NBTTagCompound中
     * <p>
     * 注意,混合过程中不会更改两个参数中的数据,新数据会另外写入新的NBTTagCompound,因此,请务必接收函数的返回结果<br>
     * NBT同名Key替换策略,如果类型相同且是List或者Compound类型,将会进行递归替换,其他情况依据参数pRelaceDes保留前者还是后者
     * </p>
     * 
     * @param pNBTTagDes
     *            目标NBT数据,NBTTagCompound实例
     * @param pNBTTagSrc
     *            来源NBT数据,NBTTagCompound实例
     * @param pRelaceDes
     *            如果存在同名NBT数据时,是否使用Src中的数据替换Des
     * @return
     */
    public static Object mixNBT(Object pNBTTagDes,Object pNBTTagSrc,boolean pRelaceDes){
        Object tNewTag=NBTUtil.newNBTTagCompound();
        Map<String,Object> tMixMapValue=NBTUtil.getNBTTagCompoundValue(tNewTag);
        Map<String,Object> tDesMapValue=NBTUtil.getNBTTagCompoundValueSafe(pNBTTagDes);
        Map<String,Object> tSrcMapValue=NBTUtil.getNBTTagCompoundValueSafe(pNBTTagSrc);
        for(Map.Entry<String,Object> sEntry : tDesMapValue.entrySet()){
            String tKey=sEntry.getKey();
            Object tSrcEle=tSrcMapValue.get(sEntry.getKey());
            if(tSrcEle==null){
                tMixMapValue.put(tKey,NBTUtil.invokeNBTTagCopy(sEntry.getValue()));
            }else{
                if(tSrcEle.getClass()==sEntry.getValue().getClass()){
                    if(NBTUtil.isNBTTagCompound(tSrcEle)){
                        tMixMapValue.put(tKey,NBTUtil.mixNBT(sEntry.getValue(),tSrcEle,pRelaceDes));
                        continue;
                    }else if(NBTUtil.isNBTTagList(tSrcEle)){
                        Object tStoreTarget;
                        if(NBTUtil.getNBTTagListValue(sEntry.getValue()).isEmpty()){
                            tStoreTarget=tSrcEle;
                        }else if(NBTUtil.getNBTTagListValue(tSrcEle).isEmpty()){
                            tStoreTarget=sEntry.getValue();
                        }else{
                            tStoreTarget=pRelaceDes?tSrcEle:sEntry.getValue();
                        }
                        tMixMapValue.put(tKey,NBTUtil.invokeNBTTagCopy(tStoreTarget));
                        continue;
                    }
                }
                Object tStoreTarget=pRelaceDes?tSrcEle:sEntry.getValue();
                tMixMapValue.put(tKey,NBTUtil.invokeNBTTagCopy(tStoreTarget));
            }
        }
        return tNewTag;
    }
}
