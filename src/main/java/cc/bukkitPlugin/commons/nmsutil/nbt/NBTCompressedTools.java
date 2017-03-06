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

        byte typeId=NBTUtil.getNBTTagTypeId(pNBTBase);
        pStream.writeByte(typeId);
        switch(typeId){
            case 0: //NBTTagEnd
                return;
            case 1: // NBTTagByte 
                pStream.writeByte(NBTUtil.getNBTTagByteValue(pNBTBase));
                return;
            case 2: // NBTTagShort
                pStream.writeShort(NBTUtil.getNBTTagShortValue(pNBTBase));
                return;
            case 3: // NBTTagInt
                pStream.writeInt(NBTUtil.getNBTTagIntValue(pNBTBase));
                return;
            case 4: // NBTTagLong
                pStream.writeLong(NBTUtil.getNBTTagLongValue(pNBTBase));
                return;
            case 5: // NBTTagFloat
                pStream.writeFloat(NBTUtil.getNBTTagFloatValue(pNBTBase));
                return;
            case 6: // NBTTagDouble
                pStream.writeDouble(NBTUtil.getNBTTagDoubleValue(pNBTBase));
                return;
            case 7: // NBTTagByteArray
                byte[] tByteArrValue=NBTUtil.getNBTTagByteArrayValue(pNBTBase);
                pStream.writeInt(tByteArrValue.length);
                pStream.write(tByteArrValue);
                return;
            case 8: // NBTTagString
                pStream.writeUTF(NBTUtil.getNBTTagStringValue(pNBTBase));
                return;
            case 9: // NBTTagList
                List<Object> tNBTBaseArrValue=NBTUtil.getNBTTagListValue(pNBTBase);
                pStream.writeInt(tNBTBaseArrValue.size());
                for(Object sNBTBase : tNBTBaseArrValue){
                    NBTCompressedTools.compressNBTBase(sNBTBase,pStream);
                }
                return;
            case 10: // NBTTagCompound
                Map<String,Object> tNBTBaseMapValue=NBTUtil.getNBTTagCompoundValue(pNBTBase);
                pStream.writeInt(tNBTBaseMapValue.size());
                for(Map.Entry<String,Object> sEntry : tNBTBaseMapValue.entrySet()){
                    pStream.writeUTF(String.valueOf(sEntry.getKey()));
                    NBTCompressedTools.compressNBTBase(sEntry.getValue(),pStream);
                }
                return;
            case 11: // NBTTagIntArray
                int[] tIntArrValue=NBTUtil.getNBTTagIntArrayValue(pNBTBase);
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
            return NBTUtil.newNBTTagCompound();

        DataInputStream tDIStream=new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(pCompressedData))));

        try{
            Object tNBTTag=NBTCompressedTools.readCompressed(tDIStream);
            if(!NBTUtil.clazz_NBTTagCompound.isInstance(tNBTTag)){
                throw new IOException("根NBT节点不是NBTTagCompound");
            }
            return tNBTTag;
        }finally{
            tDIStream.close();
        }
    }

    public static Object readCompressed(DataInputStream pStream) throws IOException{
        byte typeId=pStream.readByte();
        int length;
        switch(typeId){
            case 1: // NBTTagByte
                return NBTUtil.newNBTTagByte(pStream.readByte());
            case 2: // NBTTagShort
                return NBTUtil.newNBTTagShort(pStream.readShort());
            case 3: // NBTTagInt
                return NBTUtil.newNBTTagInt(pStream.readInt());
            case 4: // NBTTagLong
                return NBTUtil.newNBTTagLong(pStream.readLong());
            case 5: // NBTTagFloat
                return NBTUtil.newNBTTagFloat(pStream.readFloat());
            case 6: // NBTTagDouble
                return NBTUtil.newNBTTagDouble(pStream.readDouble());
            case 7: // NBTTagByteArray
                length=pStream.readInt();
                byte[] tByteArrValue=new byte[length];
                pStream.read(tByteArrValue);
                return NBTUtil.newNBTTagByteArray(tByteArrValue);
            case 8: // NBTTagString
                return NBTUtil.newNBTTagString(pStream.readUTF());
            case 9: // NBTTagList
                Object tNBTTagList=NBTUtil.newNBTTagList();
                length=pStream.readInt();
                for(int i=0;i<length;i++){
                    ClassUtil.invokeMethod(NBTUtil.method_NBTTagList_add,tNBTTagList,NBTCompressedTools.readCompressed(pStream));
                }
                return tNBTTagList;
            case 10: // NBTTagCompound
                Object tNBTTagCompound=NBTUtil.newNBTTagCompound();
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
                return NBTUtil.newNBTTagIntArray(tIntArrValue);
            default: //NBTTagEnd
                return NBTUtil.newNBTTagEnd();
        }
    }

}
