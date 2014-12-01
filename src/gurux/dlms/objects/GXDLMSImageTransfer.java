//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

import gurux.dlms.Command;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSServerBase;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ImageTransferStatus;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class GXDLMSImageTransfer extends GXDLMSObject implements IGXDLMSBase
{
    long ImageSize;
    Dictionary<Long, Object> ImageData = new Hashtable<Long, Object>();
    long ImageBlockSize;
    String ImageTransferredBlocksStatus;
    long ImageFirstNotTransferredBlockNumber;
    boolean ImageTransferEnabled;
    ImageTransferStatus ImageTransferStatus;
    GXDLMSImageActivateInfo[] ImageActivateInfo;

    /**  
     Constructor.
     @param ln Logical Name of the object.
    */
    public GXDLMSImageTransfer()
    {
        super(ObjectType.IMAGE_TRANSFER, "0.0.44.0.0.255", 0);            
        ImageBlockSize = 200;
        ImageFirstNotTransferredBlockNumber = 0;
        ImageTransferEnabled = true;
        GXDLMSImageActivateInfo info = new GXDLMSImageActivateInfo();
        info.Size = 0;
        info.Signature = "";
        info.Identification = "";
        ImageActivateInfo = new GXDLMSImageActivateInfo[] { info };
        ImageTransferStatus = ImageTransferStatus.IMAGE_TRANSFER_NOT_INITIATED;
    }
    
    /**  
     Constructor.
     @param ln Logical Name of the object.
    */
    public GXDLMSImageTransfer(String ln)
    {
        super(ObjectType.IMAGE_TRANSFER, ln, 0);        
        ImageBlockSize = 200;
        ImageFirstNotTransferredBlockNumber = 0;
        ImageTransferEnabled = true;
        GXDLMSImageActivateInfo info = new GXDLMSImageActivateInfo();
        info.Size = 0;
        info.Signature = "";
        info.Identification = "";
        ImageActivateInfo = new GXDLMSImageActivateInfo[] { info };
        ImageTransferStatus = ImageTransferStatus.IMAGE_TRANSFER_NOT_INITIATED;
    }

    /**  
     Constructor.
     @param ln Logical Name of the object.
     @param sn Short Name of the object.
    */
    public GXDLMSImageTransfer(String ln, int sn)
    {
        super(ObjectType.IMAGE_TRANSFER, ln, sn);        
        ImageBlockSize = 200;
        ImageFirstNotTransferredBlockNumber = 0;
        ImageTransferEnabled = true;
        GXDLMSImageActivateInfo info = new GXDLMSImageActivateInfo();
        info.Size = 0;
        info.Signature = "";
        info.Identification = "";
        ImageActivateInfo = new GXDLMSImageActivateInfo[] { info };
        ImageTransferStatus = ImageTransferStatus.IMAGE_TRANSFER_NOT_INITIATED;
    }       

    /** 
     Holds the ImageBlockSize, expressed in octets, 
     * which can be handled by the server
    */
    public final long getImageBlockSize()
    {
        return ImageBlockSize;
    }
    public final void setImageBlockSize(long value)
    {
        ImageBlockSize = value;
    }

    /** 
     * Provides information about the transfer status of each
     * ImageBlock. Each bit in the bit-string provides information about
     * one individual ImageBlock.
    */
    public final String getImageTransferredBlocksStatus()
    {
        return ImageTransferredBlocksStatus;
    }
    public final void setImageTransferredBlocksStatus(String value)
    {
    	ImageTransferredBlocksStatus = value;
    }

    /** 
     Provides the ImageBlockNumber of the first ImageBlock not transferred.
     * NOTE If the Image is complete, the value returned should be above the
     * number of blocks calculated from the Image size and the ImageBlockSize
    */
    public final long getImageFirstNotTransferredBlockNumber()
    {
        return ImageFirstNotTransferredBlockNumber;
    }
    public final void setImageFirstNotTransferredBlockNumber(long value)
    {
        ImageFirstNotTransferredBlockNumber = value;
    }

    /**      
     * Controls enabling the Image transfer process. The method can
     * be invoked successfully only if the value of this attribute is true.
     */
    public final boolean getImageTransferEnabled()
    {
        return ImageTransferEnabled;
    }
    public final void setImageTransferEnabled(boolean value)
    {
        ImageTransferEnabled = value;
    }

    /**
     * Holds the status of the Image transfer process.     
     */
    public final ImageTransferStatus getImageTransferStatus()
    {
        return ImageTransferStatus;
    }
    public final void setImageTransferStatus(ImageTransferStatus value)
    {
    	ImageTransferStatus = value;
    }

    public final GXDLMSImageActivateInfo[] getImageActivateInfo()
    {
        return ImageActivateInfo;
    }

    public final void setImageActivateInfo(GXDLMSImageActivateInfo[] value)
    {
        ImageActivateInfo = value;
    }

    @Override
    public Object[] getValues()
    {
        return new Object[] {getLogicalName(), getImageBlockSize(), 
            getImageTransferredBlocksStatus(), 
            getImageFirstNotTransferredBlockNumber(), 
            getImageTransferEnabled(), getImageTransferStatus(), 
            getImageActivateInfo()};
    }    
    
    /*
     * Returns collection of attributes to read.
     * 
     * If attribute is static and already read or device is returned HW error it is not returned.
     */
    @Override
    public int[] getAttributeIndexToRead()
    {
        java.util.ArrayList<Integer> attributes = new java.util.ArrayList<Integer>();
        //LN is static and read only once.
        if (LogicalName == null || LogicalName.compareTo("") == 0)
        {
            attributes.add(1);
        } 
        //ImageBlockSize
        if (!isRead(2))
        {
            attributes.add(2);
        }        
        //ImageTransferredBlocksStatus
        if (!isRead(3))
        {
            attributes.add(3);
        }
        //ImageFirstNotTransferredBlockNumber
        if (!isRead(4))
        {
            attributes.add(4);
        }
        //ImageTransferEnabled
        if (!isRead(5))
        {
            attributes.add(5);
        }
        //ImageTransferStatus
        if (!isRead(6))
        {
            attributes.add(6);
        }
        //ImageActivateInfo
        if (!isRead(7))
        {
            attributes.add(7);
        }
        return toIntArray(attributes);
    }
    
    /*
     * Returns amount of attributes.
     */  
    @Override
    public int getAttributeCount()
    {
        return 7;
    }
    
     /*
     * Returns amount of methods.
     */ 
    @Override
    public int getMethodCount()
    {
        return 4;
    }     
    
    /*
     * Data interface do not have any methods.
     */ 
    @Override
    public byte[][] invoke(Object sender, int index, Object parameters)
    {
        ImageTransferStatus = ImageTransferStatus.IMAGE_TRANSFER_NOT_INITIATED;
        GXDLMSServerBase s = (GXDLMSServerBase) sender;
        //Image transfer initiate
        if (index == 1)
        {
            ImageFirstNotTransferredBlockNumber = 0;
            ImageTransferredBlocksStatus = "";
            Object[] value = (Object[]) parameters;
            String ImageIdentifier = new String((byte[]) value[0]);
            ImageSize = ((Number)value[1]).longValue();
            ImageTransferStatus = ImageTransferStatus.IMAGE_TRANSFER_INITIATED;
            List<GXDLMSImageActivateInfo> list = new ArrayList<GXDLMSImageActivateInfo>();
            list.addAll(Arrays.asList(ImageActivateInfo));
            GXDLMSImageActivateInfo item = new GXDLMSImageActivateInfo();
            item.Size = ImageSize;
            item.Identification = ImageIdentifier;
            list.add(item);
            ImageActivateInfo = list.toArray(new GXDLMSImageActivateInfo[list.size()]);
            StringBuilder sb = new StringBuilder();
            for (long pos = 0; pos < ImageSize; ++pos)
            {
                sb.append('0');                    
            }
            ImageTransferredBlocksStatus = sb.toString();
            return s.acknowledge(Command.MethodResponse, 0);
        }
        //Image block transfer
        else if (index == 2)
        {                
            Object[] value = (Object[])parameters;
            long imageIndex = ((Number)value[0]).longValue();
            byte[] tmp = ImageTransferredBlocksStatus.getBytes();
            tmp[(int)imageIndex] = '1';
            ImageTransferredBlocksStatus = new String(tmp);
            ImageFirstNotTransferredBlockNumber = imageIndex + 1;
            ImageData.put(imageIndex, (byte[]) value[1]);
            ImageTransferStatus = ImageTransferStatus.IMAGE_TRANSFER_INITIATED;
            return s.acknowledge(Command.MethodResponse, 0);
        }
        //Image verify
        else if (index == 3)
        {
            ImageTransferStatus = ImageTransferStatus.IMAGE_VERIFICATION_INITIATED;
            //Check that size match.
            int size = 0;
            Enumeration<Object> keys = ImageData.elements();
            byte[] value;
            while(keys.hasMoreElements())
            {   
                value = (byte[]) keys.nextElement();
                size += value.length;
            }
            if (size != ImageSize)
            {
                //Return HW error.
                ImageTransferStatus = ImageTransferStatus.IMAGE_VERIFICATION_FAILED;
                throw new RuntimeException("Invalid image size.");
            }
            ImageTransferStatus = ImageTransferStatus.IMAGE_VERIFICATION_SUCCESSFUL;
            return s.acknowledge(Command.MethodResponse, 0);
        }
        //Image activate.
        else if (index == 4)
        {
            ImageTransferStatus = ImageTransferStatus.IMAGE_ACTIVATION_SUCCESSFUL;
            return s.acknowledge(Command.MethodResponse, 0);
        }
        else
        {
            throw new RuntimeException("Invoke failed. Invalid attribute index.");
        }            
    }
    
    @Override
    public DataType getDataType(int index)
    {
        if (index == 1)
        {
            return DataType.OCTET_STRING;
        }
        if (index == 2)
        {
            return DataType.UINT32;
        }
        if (index == 3)
        {
            return DataType.BITSTRING;
        }
        if (index == 4)
        {
            return DataType.UINT32;
        }
        if (index == 5)
        {
            return DataType.BOOLEAN;
        }
        if (index == 6)
        {
            return DataType.ENUM;
        }
        if (index == 7)
        {
            return DataType.ARRAY;
        }    
        throw new IllegalArgumentException("getDataType failed. Invalid attribute index.");
    }
     
    /*
     * Returns value of given attribute.
     */    
    @Override
    public Object getValue(int index, int selector, Object parameters)
    {
        if (index == 1)
        {
            return getLogicalName();
        }
        if (index == 2)
        {   
            return getImageBlockSize();
        }
        if (index == 3)
        {
            return ImageTransferredBlocksStatus;
        }
        if (index == 4)
        {
            return getImageFirstNotTransferredBlockNumber();
        }
        if (index == 5)
        {
            return getImageTransferEnabled();

        }
        if (index == 6)
        {
            return getImageTransferStatus().ordinal();            
        }
        if (index == 7)
        {
            ByteArrayOutputStream data = new ByteArrayOutputStream();            
            data.write((byte)DataType.ARRAY.getValue());
            data.write((byte) ImageActivateInfo.length); //Count  
            try
            {
                for(GXDLMSImageActivateInfo it : ImageActivateInfo)
                {
                    data.write((byte)DataType.STRUCTURE.getValue());                    
                    data.write((byte)3);//Item count.
                    GXCommon.setData(data, DataType.UINT32, it.getSize());
                    GXCommon.setData(data, DataType.OCTET_STRING, GXCommon.getBytes(it.getIdentification()));
                    String tmp = it.getSignature();
                    if (tmp != null)
                    {
                        GXCommon.setData(data, DataType.OCTET_STRING, GXCommon.getBytes(it.getSignature()));
                    }
                    else
                    {
                        GXCommon.setData(data, DataType.OCTET_STRING, null);
                    }
                }
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
            return data.toByteArray();
        }       
        throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
    }
        
    /*
     * Set value of given attribute.
     */
    @Override
    public void setValue(int index, Object value)
    {
        if (index == 1)
        {
            super.setValue(index, value);            
        }        
        else if (index == 2)
        {
            if (value == null)
            {
                setImageBlockSize(0);
            }
            else
            {
                setImageBlockSize(((Number) value).intValue());
            }
        }
        else if (index == 3)
        {
            if (value == null)
            {
                ImageTransferredBlocksStatus = "";
            }
            else
            {
                ImageTransferredBlocksStatus = value.toString();
            }            
        }
        else if (index == 4)
        {
            if (value == null)
            {
                setImageFirstNotTransferredBlockNumber(0);
            }
            else
            {
                setImageFirstNotTransferredBlockNumber(((Number) value).intValue());
            }            
        }
        else if (index == 5)
        {
            if (value == null)
            {
                setImageTransferEnabled(false);
            }
            else
            {
                setImageTransferEnabled((Boolean) value);
            }                  
        }
        else if (index == 6)
        {
            if (value == null)
            {
                setImageTransferStatus(ImageTransferStatus.IMAGE_TRANSFER_NOT_INITIATED);
            }
            else
            {
                setImageTransferStatus(ImageTransferStatus.values()[((Number) value).intValue()]);
            }              
        }
        else if (index == 7)
        {
            ImageActivateInfo = new GXDLMSImageActivateInfo[0];
            if (value != null)
            {        
                List<GXDLMSImageActivateInfo> list = new ArrayList<GXDLMSImageActivateInfo>();
                for(Object it : (Object[]) value)
                {                    
                    GXDLMSImageActivateInfo item = new GXDLMSImageActivateInfo();
                    item.setSize(((Number)Array.get(it, 0)).longValue());
                    item.setIdentification(new String((byte[]) Array.get(it, 1)));
                    item.setSignature(new String((byte[]) Array.get(it, 2)));
                    list.add(item);
                }
                ImageActivateInfo = list.toArray(new GXDLMSImageActivateInfo[list.size()]);
            }
        }         
        else
        {
            throw new IllegalArgumentException("GetValue failed. Invalid attribute index.");
        }
    }
    
     public byte[][] imageTransferInitiate(GXDLMSClient client, String imageIdentifier, long imageSize)
    {
        if (ImageBlockSize == 0)
        {
            throw new RuntimeException("Invalid image block size");
        }
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write((byte)DataType.STRUCTURE.getValue());
        data.write(2);
        try
        {            
            GXCommon.setData(data, DataType.OCTET_STRING, GXCommon.getBytes(imageIdentifier));
            GXCommon.setData(data, DataType.UINT32, imageSize);
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
        return client.method(this, 1, data.toByteArray(), DataType.ARRAY);
    }

    public byte[][] imageBlockTransfer(GXDLMSClient client, byte[] imageBlockValue, int[] imageBlockCount)
    {
        int cnt = (int)(imageBlockValue.length / ImageBlockSize);
        if (imageBlockValue.length % ImageBlockSize != 0)
        {
            ++cnt;
        }
        if (imageBlockCount != null)
        {
            imageBlockCount[0] = cnt;
        }
        List<byte[]> packets = new ArrayList<byte[]>();
        try
        {
            for (int pos = 0; pos != cnt; ++pos)
            {
                ByteArrayOutputStream data = new ByteArrayOutputStream();
                data.write(DataType.STRUCTURE.getValue());
                data.write(2);
                GXCommon.setData(data, DataType.UINT32, pos);
                byte[] tmp;
                int bytes = (int)(imageBlockValue.length - ((pos + 1) * ImageBlockSize));
                //If last packet
                if (bytes < 0)
                {
                    bytes = (int)(imageBlockValue.length - (pos * ImageBlockSize));
                    tmp = new byte[bytes];
                    System.arraycopy(imageBlockValue, (int) (pos * ImageBlockSize), tmp, 0, bytes);
                }
                else
                {
                    tmp = new byte[(int)ImageBlockSize];
                    System.arraycopy(imageBlockValue, (int)(pos * ImageBlockSize), tmp, 0, (int)ImageBlockSize);
                }
                GXCommon.setData(data, DataType.OCTET_STRING, tmp);
                packets.addAll(Arrays.asList(client.method(this, 2, data.toByteArray(), DataType.ARRAY)));                
            }
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
        byte[][] tmp = new byte[packets.size()][];
        int pos = -1;
        for(byte[] it : packets)
        {
            ++pos;
            tmp[pos] = new byte[it.length];
            System.arraycopy(it, 0, tmp[pos], 0, it.length);
        }
        return tmp;
    }

    public byte[][] imageVerify(GXDLMSClient client)
    {
        return client.method(this, 3, 0, DataType.INT8);
    }

    public byte[][] imageActivate(GXDLMSClient client)
    {
        return client.method(this, 4, 0, DataType.INT8);
    }  
}
