package cc.bukkitPlugin.commons.nmsutil.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import cc.bukkitPlugin.commons.nmsutil.nbt.exception.NBTDeserializeException;
import cc.bukkitPlugin.commons.nmsutil.nbt.exception.NBTSerializeException;
import cc.commons.commentedyaml.CommentedSection;
import cc.commons.util.ByteUtil;
import cc.commons.util.ClassUtil;
import cc.commons.util.StringUtil;

public class NBTSerializer{

    /**
     * 将NBT节点名中的替换点字符串替换成的字符
     */
    private static char RE_CHAR='$';

    // ----------------|| 序列化NBT到Yaml配置 ||----------------

    /**
     * 序列化NBT数据到配置节点
     * 
     * @param pItem
     *            物品
     * @param pSection
     *            配置节点
     * @throws NBTSerializeException
     */
    public static void serializeNBTToYaml(ItemStack pItem,CommentedSection pSection) throws NBTSerializeException{
        Object tTag=NBTUtil.getItemNBT(pItem);
        if(tTag!=null){
            try{
                Map<String,Object> tMapValue=NBTUtil.getNBTTagMapFromTag(tTag);
                for(Map.Entry<String,Object> sEntry : tMapValue.entrySet()){
                    NBTSerializer.saveNBTBaseToYaml(pSection,sEntry.getKey(),sEntry.getValue());
                }
            }catch(Throwable exp){
                throw new NBTSerializeException(exp);
            }
        }
    }

    /**
     * 序列化NBT到配置节点
     * 
     * @param pSection
     *            配置节点
     * @param pKey
     *            NBT的Key
     * @param pNBTBase
     *            NBT
     */
    private static void saveNBTBaseToYaml(CommentedSection pSection,String pKey,Object pNBTBase){
        int tTypeId=(byte)ClassUtil.invokeMethod(NBTUtil.method_NBTBase_getTypeId,pNBTBase);
        String tSaveKey=String.format("%02d|",tTypeId)+pKey.replace('.',NBTSerializer.RE_CHAR);
        if(NBTUtil.clazz_NBTTagEnd.isInstance(pNBTBase)){
            pSection.set(tSaveKey,"null");
        }else if(NBTUtil.clazz_NBTTagByte.isInstance(pNBTBase)){
            pSection.set(tSaveKey,ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagByte_value));
        }else if(NBTUtil.clazz_NBTTagShort.isInstance(pNBTBase)){
            pSection.set(tSaveKey,ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagShort_value));
        }else if(NBTUtil.clazz_NBTTagInt.isInstance(pNBTBase)){
            pSection.set(tSaveKey,ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagInt_value));
        }else if(NBTUtil.clazz_NBTTagLong.isInstance(pNBTBase)){
            pSection.set(tSaveKey,ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagLong_value));
        }else if(NBTUtil.clazz_NBTTagFloat.isInstance(pNBTBase)){
            pSection.set(tSaveKey,ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagFloat_value));
        }else if(NBTUtil.clazz_NBTTagDouble.isInstance(pNBTBase)){
            pSection.set(tSaveKey,ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagDouble_value));
        }else if(NBTUtil.clazz_NBTTagByteArray.isInstance(pNBTBase)){
            byte[] tByteArray=(byte[])ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagByteArray_value);
            String tValueStr="";
            for(byte sByte : tByteArray){
                tValueStr+=sByte+",";
            }
            if(tValueStr.endsWith(",")){
                tValueStr=tValueStr.substring(0,tValueStr.length()-1);
            }
            pSection.set(tSaveKey,tValueStr);
        }else if(NBTUtil.clazz_NBTTagString.isInstance(pNBTBase)){ // 8
            pSection.set(tSaveKey,ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagString_value));
        }else if(NBTUtil.clazz_NBTTagList.isInstance(pNBTBase)){ //9
            List<Object> tListValue=(List<Object>)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagList_value);
            CommentedSection tChildSec=pSection.createSection(tSaveKey);
            int i=0;
            for(Object sChildNBTBase : tListValue){
                NBTSerializer.saveNBTBaseToYaml(tChildSec,String.format("index%0d",i),pNBTBase);
                i++;
            }
        }else if(NBTUtil.clazz_NBTTagCompound.isInstance(pNBTBase)){//10
            Map<String,Object> tMapValue=NBTUtil.getNBTTagMapFromTag(pNBTBase);
            CommentedSection tChildSec=pSection.createSection(tSaveKey);
            for(Map.Entry<String,Object> sEntry : tMapValue.entrySet()){
                NBTSerializer.saveNBTBaseToYaml(tChildSec,sEntry.getKey(),sEntry.getValue());
            }
        }else if(NBTUtil.clazz_NBTTagIntArray.isInstance(pNBTBase)){// 11
            int[] tIntArray=(int[])ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagIntArray_value);
            String tValueStr="";
            for(int sInt : tIntArray){
                tValueStr+=sInt+",";
            }
            if(tValueStr.endsWith(",")){
                tValueStr=tValueStr.substring(0,tValueStr.length()-1);
            }
            pSection.set(tSaveKey,tValueStr);
        }
        // do nothing
    }

    /**
     * 从配置节点载入NBT序列化数据
     * 
     * @param pSection
     *            节点
     * @return NBTTagCompound实例
     * @throws NBTDeserializeException
     */
    public static Object deserializeNBTFromYaml(CommentedSection pSection) throws NBTDeserializeException{
        try{
            Object tTag=NBTUtil.newNBTTagCompound();
            for(String sKey : pSection.getKeys(false)){
                Object tChildNBT=NBTSerializer.loadNBTBaseFromSection(pSection,sKey);
                if(tChildNBT!=null){
                    NBTUtil.setToNBTTagCompound(tTag,sKey.split("|")[1],tChildNBT);
                }
            }
            return tTag;
        }catch(NBTDeserializeException exp){
            throw exp;
        }catch(Throwable exp){
            throw new NBTDeserializeException(exp);
        }
    }

    /**
     * 从配置节点反序列化NBT
     * <p>
     * 无视所有错误配置
     * </p>
     * 
     * @param pSection
     *            配置节点
     * @param pKey
     *            当前配置key
     * @return 反序列化的NBT,可能为null
     * @throws NBTDeserializeException
     */
    private static Object loadNBTBaseFromSection(CommentedSection pSection,String pKey) throws NBTDeserializeException{
        String[] tParts=pKey.split("|",2);
        if(tParts.length!=2)
            return null;
        tParts[1]=tParts[1].replace(NBTSerializer.RE_CHAR,'.');
        int tTypeId=0;
        try{
            tTypeId=Integer.parseInt(tParts[0]);
        }catch(NumberFormatException exp){
            return null;
        }
        Object tRestoreNBT=null;
        if(tTypeId==0){
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagEnd);
        }else if(tTypeId==1){
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagByte,byte.class,pSection.getByte(tParts[1]));
        }else if(tTypeId==2){
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagShort,short.class,pSection.getShort(tParts[1]));
        }else if(tTypeId==3){
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagInt,int.class,pSection.getInt(tParts[1]));
        }else if(tTypeId==4){
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagLong,long.class,pSection.getLong(tParts[1]));
        }else if(tTypeId==5){
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagFloat,float.class,pSection.getFloat(tParts[1]));
        }else if(tTypeId==6){
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagDouble,double.class,pSection.getDouble(tParts[1]));
        }else if(tTypeId==7){
            List<Byte> tBList=new ArrayList<>();
            String[] tBStrVals=pSection.getString(tParts[1],"").trim().split(",");
            for(String sValue : tBStrVals){
                if((sValue=sValue.trim()).isEmpty())
                    continue;
                try{
                    tBList.add(Byte.parseByte(sValue));
                }catch(NumberFormatException exp){
                    //ignore
                }
            }
            byte[] tBArray=new byte[tBList.size()];
            for(int i=0;i<tBList.size();i++)
                tBArray[i]=tBList.get(i).byteValue();
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagByteArray,byte[].class,tBArray);
        }else if(tTypeId==8){
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagString,String.class,pSection.getString(tParts[1],""));
        }else if(tTypeId==9){
            tRestoreNBT=ClassUtil.getInstance(NBTUtil.clazz_NBTTagList);
            CommentedSection tChildSec=pSection.getSection(tParts[1]);
            if(tChildSec!=null){
                for(String sChildKey : tChildSec.getKeys(false)){
                    Object tChildNBT=NBTSerializer.loadNBTBaseFromSection(tChildSec,pKey);
                    if(tChildNBT!=null){
                        ClassUtil.invokeMethod(NBTUtil.method_NBTTagList_add,tRestoreNBT,tChildNBT);
                    }
                }
            }
            return tRestoreNBT;
        }else if(tTypeId==10){
            tRestoreNBT=ClassUtil.getInstance(NBTUtil.clazz_NBTTagCompound);
            CommentedSection tChildSec=pSection.getSection(tParts[1]);
            if(tChildSec!=null){
                for(String sChildKey : tChildSec.getKeys(false)){
                    Object tChildNBT=NBTSerializer.loadNBTBaseFromSection(tChildSec,pKey);
                    if(tChildNBT!=null){
                        NBTUtil.setToNBTTagCompound(tRestoreNBT,sChildKey.split("|")[1],tChildNBT);
                    }
                }
            }
            return tRestoreNBT;
        }else if(tTypeId==11){
            List<Integer> tIList=new ArrayList<>();
            String[] tIStrValues=pSection.getString(tParts[1],"").trim().split(",");
            for(String sValue : tIStrValues){
                if((sValue=sValue.trim()).isEmpty())
                    continue;
                try{
                    tIList.add(Integer.valueOf(sValue));
                }catch(NumberFormatException exp){
                    //ignore
                }
            }
            int[] tIArray=new int[tIList.size()];
            for(int i=0;i<tIList.size();i++)
                tIArray[i]=tIList.get(i).intValue();
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagIntArray,int[].class,tIArray);
        }
        return tRestoreNBT;
    }

    // ----------------|| 序列化NBT到Json ||----------------

    /**
     * 获取NBT的Json串
     * <p>
     * 只对NBTTagString的toString方法重写<br />
     * 其他方法不变,不替换特殊字符
     * </p>
     *
     * @param pNBTBase
     *            NBT值
     * @return json字符串
     */
    public static String serializeNBTToJson(Object pNBTBase){
        return NBTSerializer.getNBTJson(pNBTBase,false);
    }

    /**
     * 获取NBT的Json串
     * <p>
     * 只对NBTTagString的toString方法重写<br />
     * 其他方法不变
     * </p>
     *
     * @param pNBTBase
     *            NBT值
     * @return json字符串
     */
    public static String serializeNBTToTellrawJson(Object pNBTBase){
        return NBTSerializer.getNBTJson(pNBTBase,true);
    }

    /**
     * 获取NBT的Json字符串
     * 
     * @param pNBTBase
     *            NBT
     * @param pTellraw
     *            此Json是否为发送Tellraw
     * @return Json字符串
     */
    private static String getNBTJson(Object pNBTBase,boolean pTellraw){
        if(!NBTUtil.clazz_NBTBase.isInstance(pNBTBase)){
            return "{}";
        }
        if(NBTUtil.clazz_NBTTagCompound.isInstance(pNBTBase)){
            Map<String,Object> tNBTContents=(Map<String,Object>)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagCompound_map);
            if(tNBTContents==null||tNBTContents.isEmpty())
                return "{}";
            String tContentJson="{";
            for(String sKey : tNBTContents.keySet()){
                Object tContentNode=tNBTContents.get(sKey);
                if(tContentNode!=null){
                    tContentJson+=sKey+':'+NBTSerializer.getNBTJson(tContentNode,pTellraw)+',';
                }
            }
            if(tContentJson.lastIndexOf(",")!=-1){
                tContentJson=tContentJson.substring(0,tContentJson.length()-1);
            }
            return tContentJson+"}";
        }else if(NBTUtil.clazz_NBTTagList.isInstance(pNBTBase)){
            List<Object> tNBTContents=(List<Object>)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagList_value);
            if(tNBTContents==null||tNBTContents.isEmpty())
                return "[]";
            String tContentJson="[";
            int i=0;
            for(Object tContentNode : tNBTContents){
                if(tContentNode!=null){
                    tContentJson+=i+":"+NBTSerializer.getNBTJson(tContentNode,pTellraw)+',';
                }
                i++;
            }
            if(tContentJson.lastIndexOf(",")!=-1){
                tContentJson=tContentJson.substring(0,tContentJson.length()-1);
            }
            return tContentJson+"]";
        }else if(NBTUtil.clazz_NBTTagString.isInstance(pNBTBase)){
            String tValue=(String)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagString_value);
            if(pTellraw){
                tValue=tValue.replace("\"","\\\"");
                if(StringUtil.isNotBlank(tValue)&&tValue.charAt(tValue.length()-1)=='\\'){
                    tValue=tValue+" ";
                }
            }
            return "\""+tValue+"\"";
        }else return pNBTBase.toString();
    }

    // ----------------|| 序列化NBT到GZip字节流 ||----------------

    /**
     * 序列化物品的NBT
     * 
     * @param pItem
     *            要序列化的NBT的物品来源
     * @return 序列化的NBT Base64数据,如果物品不存在NBT,将返回null
     * @throws NBTSerializeException
     */
    public static String serializeNBTToByte(ItemStack pItem) throws NBTSerializeException{
        Object tNBTObj=NBTUtil.getItemNBT(pItem);
        if(tNBTObj!=null){
            try{
                byte[] tData=NBTCompressedTools.compressNBTCompound(tNBTObj);
                if(tData!=null&&tData.length>0){
                    return new String(ByteUtil.byteToBase64(tData));
                }
            }catch(Throwable exp){
                throw new NBTSerializeException(exp);
            }
        }
        return null;
    }

    /**
     * 反序列化由{@link #serializeNBT(ItemStack)创建的序列化的数据}
     * 
     * @param pData
     *            序列化的Base64 NBT数据
     * @return 反序列化的NBTCompound实例,非null
     * @throws NBTDeserializeException
     */
    public static Object deserializeNBTFromByte(String pData) throws NBTDeserializeException{
        try{
            return NBTCompressedTools.readCompressed(ByteUtil.base64ToByte(pData));
        }catch(Throwable exp){
            throw new NBTDeserializeException(exp);
        }
    }

}
