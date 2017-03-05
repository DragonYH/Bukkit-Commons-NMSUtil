package cc.bukkitPlugin.commons.nmsutil.nbt;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import cc.commons.util.ClassUtil;

public class NBTCompressedTools{

    /**
     * 将NBTTagCompound序列化为byte[]
     * 
     * @param pNBTTagCompound
     *            NBTTagCompound实例
     * @return 序列化后的字节数据
     * @throws IOException
     *             序列化过程中发生错误
     */
    public static byte[] compressNBTCompound(Object pNBTTagCompound) throws IOException{
        if(!NBTUtil.clazz_NBTTagCompound.isInstance(pNBTTagCompound))
            throw new IOException("参数类型必须为NBTTagCompound");

        ByteArrayOutputStream bytearrayoutputstream=new ByteArrayOutputStream();
        DataOutputStream dataoutputstream=new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));

        try{
            NBTCompressedTools.compressNBTBase(pNBTTagCompound,dataoutputstream);
        }finally{
            dataoutputstream.close();
        }

        return bytearrayoutputstream.toByteArray();
    }

    /**
     * 将NBTBase序列化后写入流中
     * 
     * @param pNBTBase
     *            NBTBase的子类
     * @param pStream
     *            输出流
     * @throws IOException
     *             写入过程中发生错误
     */
    public static void compressNBTBase(Object pNBTBase,DataOutputStream pStream) throws IOException{
        if(!NBTUtil.clazz_NBTBase.isInstance(pNBTBase))
            throw new IllegalArgumentException("参数类型必须为NBTBase及其子类");

        byte typeId=(byte)ClassUtil.invokeMethod(NBTUtil.method_NBTBase_getTypeId,pNBTBase);
        pStream.writeByte(typeId);
        switch(typeId){
            case 0: //NBTTagEnd
                return;
            case 1: // NBTTagByte
                pStream.writeByte((byte)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagByte_value));
                return;
            case 2: // NBTTagShort
                pStream.writeShort((short)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagShort_value));
                return;
            case 3: // NBTTagInt
                pStream.writeInt((int)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagInt_value));
                return;
            case 4: // NBTTagLong
                pStream.writeLong((long)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagLong_value));
                return;
            case 5: // NBTTagFloat
                pStream.writeFloat((float)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagFloat_value));
                return;
            case 6: // NBTTagDouble
                pStream.writeDouble((double)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagDouble_value));
                return;
            case 7: // NBTTagByteArray
                byte[] tByteArrValue=(byte[])ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagByteArray_value);
                pStream.writeInt(tByteArrValue.length);
                pStream.write(tByteArrValue);
                return;
            case 8: // NBTTagString
                pStream.writeUTF((String)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagString_value));
                return;
            case 9: // NBTTagList
                List<Object> tNBTBaseArrValue=(List<Object>)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagList_value);
                pStream.writeInt(tNBTBaseArrValue.size());
                for(Object sNBTBase : tNBTBaseArrValue){
                    NBTCompressedTools.compressNBTBase(sNBTBase,pStream);
                }
                return;
            case 10: // NBTTagCompound
                Map<Object,Object> tNBTBaseMapValue=(Map<Object,Object>)ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagCompound_map);
                pStream.writeInt(tNBTBaseMapValue.size());
                for(Map.Entry<Object,Object> sEntry : tNBTBaseMapValue.entrySet()){
                    pStream.writeUTF(String.valueOf(sEntry.getKey()));
                    NBTCompressedTools.compressNBTBase(sEntry.getValue(),pStream);
                }
                return;
            case 11: // NBTTagIntArray
                int[] tIntArrValue=(int[])ClassUtil.getFieldValue(pNBTBase,NBTUtil.field_NBTTagIntArray_value);
                pStream.writeInt(tIntArrValue.length);
                for(int i=0;i<tIntArrValue.length;i++){
                    pStream.writeInt(tIntArrValue[i]);
                }
                return;
            default:
                //do nothing
        }
    }

    public static Object readCompressed(byte[] pCompressedData) throws IOException{
        if(pCompressedData==null||pCompressedData.length==0)
            return ClassUtil.getInstance(NBTUtil.clazz_NBTTagCompound);

        DataInputStream datainputstream=new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(pCompressedData))));

        try{
            Object tNBTTag=NBTCompressedTools.readCompressed(datainputstream);
            if(!NBTUtil.clazz_NBTTagCompound.isInstance(tNBTTag)){
                throw new IOException("根NBT节点不是NBTTagCompound");
            }
            return tNBTTag;
        }finally{
            datainputstream.close();
        }
    }

    public static Object readCompressed(DataInputStream pStream) throws IOException{
        byte typeId=pStream.readByte();
        int length;
        switch(typeId){
            case 1: // NBTTagByte
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagByte,byte.class,pStream.readByte());
            case 2: // NBTTagShort
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagShort,short.class,pStream.readShort());
            case 3: // NBTTagInt
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagInt,int.class,pStream.readInt());
            case 4: // NBTTagLong
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagLong,long.class,pStream.readLong());
            case 5: // NBTTagFloat
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagFloat,float.class,pStream.readFloat());
            case 6: // NBTTagDouble
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagDouble,double.class,pStream.readDouble());
            case 7: // NBTTagByteArray
                length=pStream.readInt();
                byte[] tByteArrValue=new byte[length];
                pStream.read(tByteArrValue);
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagByteArray,byte[].class,tByteArrValue);
            case 8: // NBTTagString
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagString,String.class,pStream.readUTF());
            case 9: // NBTTagList
                Object tNBTTagList=ClassUtil.getInstance(NBTUtil.clazz_NBTTagList);
                length=pStream.readInt();
                for(int i=0;i<length;i++){
                    ClassUtil.invokeMethod(NBTUtil.method_NBTTagList_add,tNBTTagList,NBTCompressedTools.readCompressed(pStream));
                }
                return tNBTTagList;
            case 10: // NBTTagCompound
                Object tNBTTagCompound=ClassUtil.getInstance(NBTUtil.clazz_NBTTagCompound);
                length=pStream.readInt();
                for(int i=0;i<length;i++){
                    ClassUtil.invokeMethod(NBTUtil.method_NBTTagCompound_set,tNBTTagCompound,new Object[]{pStream.readUTF(),NBTCompressedTools.readCompressed(pStream)});
                }
                return tNBTTagCompound;
            case 11: // NBTTagIntArray
                length=pStream.readInt();
                int[] tIntArrValue=new int[length];
                for(int i=0;i<tIntArrValue.length;i++){
                    tIntArrValue[i]=pStream.readInt();
                }
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagIntArray,int[].class,tIntArrValue);
            default: //NBTTagEnd
                return ClassUtil.getInstance(NBTUtil.clazz_NBTTagEnd);
        }
    }

}
