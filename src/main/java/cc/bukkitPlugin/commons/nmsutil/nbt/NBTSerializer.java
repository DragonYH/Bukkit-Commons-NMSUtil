package cc.bukkitPlugin.commons.nmsutil.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import cc.bukkitPlugin.commons.nmsutil.nbt.exception.NBTDeserializeException;
import cc.bukkitPlugin.commons.nmsutil.nbt.exception.NBTSerializeException;
import cc.commons.commentedyaml.CommentedSection;
import cc.commons.util.ByteUtil;
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
        NBTSerializer.serializeNBTToYaml_Tag(NBTUtil.getItemNBT(pItem),pSection);
    }

    /**
     * 序列化NBT数据到配置节点
     * 
     * @param pNBTTag
     *            NBT
     * @param pSection
     *            配置节点
     * @throws NBTSerializeException
     */
    public static void serializeNBTToYaml_Tag(Object pNBTTag,CommentedSection pSection) throws NBTSerializeException{
        if(pNBTTag!=null){
            try{
                Map<String,Object> tMapValue=NBTUtil.getNBTTagCompoundValue(pNBTTag);
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
        int tTypeId=NBTUtil.getNBTTagTypeId(pNBTBase);
        String tSaveKey=String.format("%02d|",tTypeId)+pKey.replace('.',NBTSerializer.RE_CHAR);
        if(NBTUtil.clazz_NBTTagEnd.isInstance(pNBTBase)){
            pSection.set(tSaveKey,"null");
        }else if(NBTUtil.clazz_NBTTagByte.isInstance(pNBTBase)){
            pSection.set(tSaveKey,NBTUtil.getNBTTagByteValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagShort.isInstance(pNBTBase)){
            pSection.set(tSaveKey,NBTUtil.getNBTTagShortValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagInt.isInstance(pNBTBase)){
            pSection.set(tSaveKey,NBTUtil.getNBTTagIntValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagLong.isInstance(pNBTBase)){
            pSection.set(tSaveKey,NBTUtil.getNBTTagLongValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagFloat.isInstance(pNBTBase)){
            pSection.set(tSaveKey,NBTUtil.getNBTTagFloatValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagDouble.isInstance(pNBTBase)){
            pSection.set(tSaveKey,NBTUtil.getNBTTagDoubleValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagByteArray.isInstance(pNBTBase)){
            byte[] tByteArray=NBTUtil.getNBTTagByteArrayValue(pNBTBase);
            String tValueStr="";
            for(byte sByte : tByteArray){
                tValueStr+=sByte+",";
            }
            if(tValueStr.endsWith(",")){
                tValueStr=tValueStr.substring(0,tValueStr.length()-1);
            }
            pSection.set(tSaveKey,tValueStr);
        }else if(NBTUtil.clazz_NBTTagString.isInstance(pNBTBase)){ // 8
            pSection.set(tSaveKey,NBTUtil.getNBTTagStringValue(pNBTBase));
        }else if(NBTUtil.clazz_NBTTagList.isInstance(pNBTBase)){ //9
            List<Object> tListValue=NBTUtil.getNBTTagListValue(pNBTBase);
            CommentedSection tChildSec=pSection.createSection(tSaveKey);
            int i=0;
            for(Object sChildNBTBase : tListValue){
                NBTSerializer.saveNBTBaseToYaml(tChildSec,String.format("index%02d",i),pNBTBase);
                i++;
            }
        }else if(NBTUtil.clazz_NBTTagCompound.isInstance(pNBTBase)){//10
            Map<String,Object> tMapValue=NBTUtil.getNBTTagCompoundValue(pNBTBase);
            CommentedSection tChildSec=pSection.createSection(tSaveKey);
            for(Map.Entry<String,Object> sEntry : tMapValue.entrySet()){
                NBTSerializer.saveNBTBaseToYaml(tChildSec,sEntry.getKey(),sEntry.getValue());
            }
        }else if(NBTUtil.clazz_NBTTagIntArray.isInstance(pNBTBase)){// 11
            int[] tIntArray=NBTUtil.getNBTTagIntArrayValue(pNBTBase);
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
            if(pSection==null)
                return tTag;

            for(String sKey : pSection.getKeys(false)){
                Object tChildNBT=NBTSerializer.loadNBTBaseFromSection(pSection,sKey);
                if(tChildNBT!=null){
                    sKey=sKey.split("[|]",2)[1].replace(NBTSerializer.RE_CHAR,'.');
                    NBTUtil.invokeNBTTagCompound_set(tTag,sKey,tChildNBT);
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
        String[] tParts=pKey.split("[|]",2);
        if(tParts.length!=2)
            return null;

        int tTypeId=0;
        try{
            tTypeId=Integer.parseInt(tParts[0]);
        }catch(NumberFormatException exp){
            return null;
        }
        
        if(tTypeId==0){
            return NBTUtil.newNBTTagEnd();
        }else if(tTypeId==1){
            return NBTUtil.newNBTTagByte(pSection.getByte(pKey));
        }else if(tTypeId==2){
            return NBTUtil.newNBTTagShort(pSection.getShort(pKey));
        }else if(tTypeId==3){
            return NBTUtil.newNBTTagInt(pSection.getInt(pKey));
        }else if(tTypeId==4){
            return NBTUtil.newNBTTagLong(pSection.getLong(pKey));
        }else if(tTypeId==5){
            return NBTUtil.newNBTTagFloat(pSection.getFloat(pKey));
        }else if(tTypeId==6){
            return NBTUtil.newNBTTagDouble(pSection.getDouble(pKey));
        }else if(tTypeId==7){
            List<Byte> tBList=new ArrayList<>();
            String[] tBStrVals=pSection.getString(pKey,"").trim().split(",");
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
            return NBTUtil.newNBTTagByteArray(tBArray);
        }else if(tTypeId==8){
            return NBTUtil.newNBTTagString(pSection.getString(pKey,""));
        }else if(tTypeId==9){
            Object tRestoreNBT=NBTUtil.newNBTTagList();
            CommentedSection tChildSec=pSection.getSection(pKey);
            if(tChildSec!=null){
                for(String sChildKey : tChildSec.getKeys(false)){
                    Object tChildNBT=NBTSerializer.loadNBTBaseFromSection(tChildSec,sChildKey);
                    if(tChildNBT!=null){
                        NBTUtil.invokeNBTTagList_add(tRestoreNBT,tChildNBT);
                    }
                }
            }
            return tRestoreNBT;
        }else if(tTypeId==10){
            return NBTSerializer.deserializeNBTFromYaml(pSection.getSection(pKey));
        }else if(tTypeId==11){
            List<Integer> tIList=new ArrayList<>();
            String[] tIStrValues=pSection.getString(pKey,"").trim().split(",");
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
            return NBTUtil.newNBTTagIntArray(tIArray);
        }
        return null;
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
            Map<String,Object> tNBTContents=NBTUtil.getNBTTagCompoundValue(pNBTBase);
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
            List<Object> tNBTContents=NBTUtil.getNBTTagListValue(pNBTBase);
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
            String tValue=NBTUtil.getNBTTagStringValue(pNBTBase);
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
        return NBTSerializer.serializeNBTToByte_Tag(NBTUtil.getItemNBT(pItem));
    }

    /**
     * 序列化物品的NBT
     * 
     * @param pNBTTag
     *            要序列化的NBT
     * @return 序列化的NBT Base64数据,如果物品不存在NBT,将返回null
     * @throws NBTSerializeException
     */
    public static String serializeNBTToByte_Tag(Object pNBTTag) throws NBTSerializeException{
        if(pNBTTag!=null){
            try{
                byte[] tData=NBTCompressedTools.compressNBTCompound(pNBTTag);
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
