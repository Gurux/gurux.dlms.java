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
package gurux.dlms.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import gurux.common.GXCommon;
import gurux.common.IGXMedia;
import gurux.common.ReceiveParameters;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXReplyData;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.manufacturersettings.GXManufacturer;
import gurux.dlms.manufacturersettings.GXServerAddress;
import gurux.dlms.manufacturersettings.HDLCAddressType;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSDemandRegister;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXDLMSRegister;
import gurux.dlms.objects.IGXDLMSBase;
import gurux.dlms.secure.GXDLMSSecureClient;
import gurux.io.BaudRate;
import gurux.io.Parity;
import gurux.io.StopBits;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class GXCommunicate {
    IGXMedia Media;
    public boolean Trace = false;
    GXManufacturer manufacturer;
    public GXDLMSSecureClient dlms;
    boolean iec;
    java.nio.ByteBuffer replyBuff;
    int WaitTime = 60000;

    static void trace(PrintWriter logFile, String text) {
        logFile.write(text);
        System.out.print(text);
    }

    static void traceLn(PrintWriter logFile, String text) {
        logFile.write(text + "\r\n");
        System.out.print(text + "\r\n");
    }

    public GXCommunicate(int waitTime, GXDLMSSecureClient dlms,
            GXManufacturer manufacturer, boolean iec, Authentication auth,
            String pw, IGXMedia media) throws Exception {
        Files.deleteIfExists(Paths.get("trace.txt"));
        Media = media;
        WaitTime = waitTime;
        this.dlms = dlms;
        this.manufacturer = manufacturer;
        this.iec = iec;
        boolean useIec47 =
                manufacturer.getUseIEC47() && media instanceof gurux.net.GXNet;
        dlms.setUseLogicalNameReferencing(
                manufacturer.getUseLogicalNameReferencing());
        int value = manufacturer.getAuthentication(auth).getClientAddress();
        dlms.setClientAddress(value);
        GXServerAddress serv = manufacturer.getServer(HDLCAddressType.DEFAULT);
        if (useIec47) {
            dlms.setInterfaceType(InterfaceType.WRAPPER);
            value = serv.getPhysicalAddress();
        } else {
            dlms.setInterfaceType(InterfaceType.HDLC);
            value = GXDLMSClient.getServerAddress(serv.getLogicalAddress(),
                    serv.getPhysicalAddress());
        }
        dlms.setServerAddress(value);
        dlms.setAuthentication(auth);
        dlms.setPassword(pw.getBytes("ASCII"));
        System.out.println("Authentication: " + auth);
        System.out.println("ClientAddress: 0x"
                + Integer.toHexString(dlms.getClientAddress()));
        System.out.println("ServerAddress: 0x"
                + Integer.toHexString(dlms.getServerAddress()));
        if (dlms.getInterfaceType() == InterfaceType.WRAPPER) {
            replyBuff = java.nio.ByteBuffer.allocate(8 + 1024);
        } else {
            replyBuff = java.nio.ByteBuffer.allocate(100);
        }
    }

    void close() throws Exception {
        if (Media != null) {
            System.out.println("DisconnectRequest");
            GXReplyData reply = new GXReplyData();
            readDLMSPacket(dlms.disconnectRequest(), reply);
            Media.close();
        }
    }

    String now() {
        return new SimpleDateFormat("HH:mm:ss.SSS")
                .format(java.util.Calendar.getInstance().getTime());
    }

    void writeTrace(String line) {
        if (Trace) {
            System.out.println(line);
        }
        PrintWriter logFile = null;
        try {
            logFile = new PrintWriter(
                    new BufferedWriter(new FileWriter("trace.txt", true)));
            logFile.println(line);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (logFile != null) {
                logFile.close();
            }
        }
    }

    public void readDLMSPacket(byte[][] data) throws Exception {
        GXReplyData reply = new GXReplyData();
        for (byte[] it : data) {
            reply.clear();
            readDLMSPacket(it, reply);
        }
    }

    /**
     * Read DLMS Data from the device. If access is denied return null.
     */
    public void readDLMSPacket(byte[] data, GXReplyData reply)
            throws Exception {
        if (data == null || data.length == 0) {
            return;
        }
        reply.setError((short) 0);
        Object eop = (byte) 0x7E;
        // In network connection terminator is not used.
        if (dlms.getInterfaceType() == InterfaceType.WRAPPER
                && Media instanceof GXNet) {
            eop = null;
        }
        Integer pos = 0;
        boolean succeeded = false;
        ReceiveParameters<byte[]> p =
                new ReceiveParameters<byte[]>(byte[].class);
        p.setEop(eop);
        p.setCount(5);
        p.setWaitTime(WaitTime);
        synchronized (Media.getSynchronous()) {
            while (!succeeded) {
                writeTrace("<- " + now() + "\t" + GXCommon.bytesToHex(data));
                Media.send(data, null);
                if (p.getEop() == null) {
                    p.setCount(1);
                }
                succeeded = Media.receive(p);
                if (!succeeded) {
                    // Try to read again...
                    if (pos++ == 3) {
                        throw new RuntimeException(
                                "Failed to receive reply from the device in given time.");
                    }
                    System.out.println("Data send failed. Try to resend "
                            + pos.toString() + "/3");
                }
            }
            // Loop until whole DLMS packet is received.
            try {
                while (!dlms.getData(p.getReply(), reply)) {
                    if (p.getEop() == null) {
                        p.setCount(1);
                    }
                    if (!Media.receive(p)) {
                        // If echo.
                        if (reply.isEcho()) {
                            Media.send(data, null);
                        }
                        // Try to read again...
                        if (++pos == 3) {
                            throw new Exception(
                                    "Failed to receive reply from the device in given time.");
                        }
                        System.out.println("Data send failed. Try to resend "
                                + pos.toString() + "/3");
                    }
                }
            } catch (Exception e) {
                writeTrace("-> " + now() + "\t"
                        + GXCommon.bytesToHex(p.getReply()));
                throw e;
            }
        }
        writeTrace("-> " + now() + "\t" + GXCommon.bytesToHex(p.getReply()));
        if (reply.getError() != 0) {
            if (reply.getError() == ErrorCode.REJECTED.getValue()) {
                Thread.sleep(1000);
                readDLMSPacket(data, reply);
            } else {
                throw new GXDLMSException(reply.getError());
            }
        }
    }

    void readDataBlock(byte[][] data, GXReplyData reply) throws Exception {
        for (byte[] it : data) {
            reply.clear();
            readDataBlock(it, reply);
        }
    }

    /**
     * Reads next data block.
     * 
     * @param data
     * @return
     * @throws Exception
     */
    void readDataBlock(byte[] data, GXReplyData reply) throws Exception {
        RequestTypes rt;
        if (data.length != 0) {
            readDLMSPacket(data, reply);
            while (reply.isMoreData()) {
                rt = reply.getMoreData();
                data = dlms.receiverReady(rt);
                readDLMSPacket(data, reply);
            }
        }
    }

    /**
     * Initializes connection.
     * 
     * @param port
     * @throws InterruptedException
     * @throws Exception
     */
    void initializeConnection() throws Exception, InterruptedException {
        Media.open();
        if (Media instanceof GXSerial) {
            GXSerial serial = (GXSerial) Media;
            serial.setDtrEnable(true);
            serial.setRtsEnable(true);
            if (iec) {
                ReceiveParameters<byte[]> p =
                        new ReceiveParameters<byte[]>(byte[].class);
                p.setAllData(false);
                p.setEop((byte) '\n');
                p.setWaitTime(WaitTime);
                String data;
                String replyStr;
                synchronized (Media.getSynchronous()) {
                    data = "/?!\r\n";
                    writeTrace("<- " + now() + "\t"
                            + GXCommon.bytesToHex(data.getBytes("ASCII")));
                    Media.send(data, null);
                    if (!Media.receive(p)) {
                        throw new Exception("Invalid meter type.");
                    }
                    writeTrace("->" + now() + "\t"
                            + GXCommon.bytesToHex(p.getReply()));
                    // If echo is used.
                    replyStr = new String(p.getReply());
                    if (data.equals(replyStr)) {
                        p.setReply(null);
                        if (!Media.receive(p)) {
                            throw new Exception("Invalid meter type.");
                        }
                        writeTrace("-> " + now() + "\t"
                                + GXCommon.bytesToHex(p.getReply()));
                        replyStr = new String(p.getReply());
                    }
                }
                if (replyStr.length() == 0 || replyStr.charAt(0) != '/') {
                    throw new Exception("Invalid responce : " + replyStr);
                }
                String manufactureID = replyStr.substring(1, 4);
                if (manufacturer.getIdentification()
                        .compareToIgnoreCase(manufactureID) != 0) {
                    throw new Exception("Manufacturer "
                            + manufacturer.getIdentification()
                            + " expected but " + manufactureID + " found.");
                }
                int bitrate = 0;
                char baudrate = replyStr.charAt(4);
                switch (baudrate) {
                case '0':
                    bitrate = 300;
                    break;
                case '1':
                    bitrate = 600;
                    break;
                case '2':
                    bitrate = 1200;
                    break;
                case '3':
                    bitrate = 2400;
                    break;
                case '4':
                    bitrate = 4800;
                    break;
                case '5':
                    bitrate = 9600;
                    break;
                case '6':
                    bitrate = 19200;
                    break;
                default:
                    throw new Exception("Unknown baud rate.");
                }
                System.out.println("Bitrate is : " + bitrate);
                // Send ACK
                // Send Protocol control character
                byte controlCharacter = (byte) '2';// "2" HDLC protocol
                                                   // procedure (Mode E)
                // Send Baudrate character
                // Mode control character
                byte ModeControlCharacter = (byte) '2';// "2" //(HDLC protocol
                                                       // procedure) (Binary
                                                       // mode)
                // Set mode E.
                byte[] tmp = new byte[] { 0x06, controlCharacter,
                        (byte) baudrate, ModeControlCharacter, 13, 10 };
                p.setReply(null);
                synchronized (Media.getSynchronous()) {
                    Media.send(tmp, null);
                    writeTrace("<- " + now() + "\t" + GXCommon.bytesToHex(tmp));
                    p.setWaitTime(100);
                    if (Media.receive(p)) {
                        writeTrace("-> " + now() + "\t"
                                + GXCommon.bytesToHex(p.getReply()));
                    }
                    Media.close();
                    // This sleep make sure that all meters can be read.
                    Thread.sleep(400);
                    serial.setDataBits(8);
                    serial.setParity(Parity.NONE);
                    serial.setStopBits(StopBits.ONE);
                    serial.setBaudRate(BaudRate.forValue(bitrate));
                    Media.open();
                    serial.setDtrEnable(true);
                    serial.setRtsEnable(true);
                    Thread.sleep(400);
                }
            }
        }
        GXReplyData reply = new GXReplyData();
        byte[] data = dlms.snrmRequest();
        if (data.length != 0) {
            readDLMSPacket(data, reply);
            // Has server accepted client.
            dlms.parseUAResponse(reply.getData());

            // Allocate buffer to same size as transmit buffer of the meter.
            // Size of replyBuff is payload and frame (Bop, EOP, crc).
            int size = (int) ((((Number) dlms.getLimits().getMaxInfoTX())
                    .intValue() & 0xFFFFFFFFL) + 40);
            replyBuff = java.nio.ByteBuffer.allocate(size);
        }
        reply.clear();
        readDataBlock(dlms.aarqRequest(), reply);
        // Parse reply.
        dlms.parseAareResponse(reply.getData());
        reply.clear();

        // Get challenge Is HLS authentication is used.
        if (dlms.getIsAuthenticationRequired()) {
            for (byte[] it : dlms.getApplicationAssociationRequest()) {
                readDLMSPacket(it, reply);
            }
            dlms.parseApplicationAssociationResponse(reply.getData());
        }
    }

    /**
     * Reads selected DLMS object with selected attribute index.
     * 
     * @param item
     * @param attributeIndex
     * @return
     * @throws Exception
     */
    public Object readObject(GXDLMSObject item, int attributeIndex)
            throws Exception {
        byte[] data = dlms.read(item.getName(), item.getObjectType(),
                attributeIndex)[0];
        GXReplyData reply = new GXReplyData();

        readDataBlock(data, reply);
        // Update data type on read.
        if (item.getDataType(attributeIndex) == DataType.NONE) {
            item.setDataType(attributeIndex, reply.getValueType());
        }
        return dlms.updateValue(item, attributeIndex, reply.getValue());
    }

    /*
     * /// Read list of attributes.
     */
    public void readList(List<Entry<GXDLMSObject, Integer>> list)
            throws Exception {
        byte[][] data = dlms.readList(list);
        GXReplyData reply = new GXReplyData();
        readDataBlock(data, reply);
        dlms.updateValues(list, Arrays.asList(reply.getValue()));
    }

    /**
     * Writes value to DLMS object with selected attribute index.
     * 
     * @param item
     * @param attributeIndex
     * @throws Exception
     */
    public void writeObject(GXDLMSObject item, int attributeIndex)
            throws Exception {
        byte[][] data = dlms.write(item, attributeIndex);
        readDLMSPacket(data);
    }

    /*
     * Returns columns of profile Generic.
     */
    public List<Entry<GXDLMSObject, GXDLMSCaptureObject>>
            GetColumns(GXDLMSProfileGeneric pg) throws Exception {
        Object entries = readObject(pg, 7);
        System.out.println("Reading Profile Generic: " + pg.getLogicalName()
                + " " + pg.getDescription() + " entries:" + entries.toString());
        GXReplyData reply = new GXReplyData();
        byte[] data = dlms.read(pg.getName(), pg.getObjectType(), 3)[0];
        readDataBlock(data, reply);
        dlms.updateValue((GXDLMSObject) pg, 3, reply.getValue());
        return pg.getCaptureObjects();
    }

    /**
     * Read Profile Generic's data by entry start and count.
     * 
     * @param pg
     * @param index
     * @param count
     * @return
     * @throws Exception
     */
    public Object[] readRowsByEntry(GXDLMSProfileGeneric pg, int index,
            int count) throws Exception {
        byte[][] data = dlms.readRowsByEntry(pg, index, count);
        GXReplyData reply = new GXReplyData();
        readDataBlock(data, reply);
        return (Object[]) dlms.updateValue(pg, 2, reply.getValue());
    }

    /**
     * Read Profile Generic's data by range (start and end time).
     * 
     * @param pg
     * @param sortedItem
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public Object[] readRowsByRange(final GXDLMSProfileGeneric pg,
            final Date start, final Date end) throws Exception {
        GXReplyData reply = new GXReplyData();
        byte[][] data = dlms.readRowsByRange(pg, start, end);
        readDataBlock(data, reply);
        return (Object[]) dlms.updateValue(pg, 2, reply.getValue());
    }

    /*
     * Read Scalers and units from the register objects.
     */
    void readScalerAndUnits(final GXDLMSObjectCollection objects,
            final PrintWriter logFile) throws Exception {
        GXDLMSObjectCollection objs = objects.getObjects(new ObjectType[] {
                ObjectType.REGISTER, ObjectType.DEMAND_REGISTER,
                ObjectType.EXTENDED_REGISTER });
        try {
            if (dlms.getNegotiatedConformance()
                    .contains(Conformance.MULTIPLE_REFERENCES)) {
                List<Entry<GXDLMSObject, Integer>> list =
                        new ArrayList<Entry<GXDLMSObject, Integer>>();
                for (GXDLMSObject it : objs) {
                    if (it instanceof GXDLMSRegister) {
                        list.add(new GXSimpleEntry<GXDLMSObject, Integer>(it,
                                3));
                    }
                    if (it instanceof GXDLMSDemandRegister) {
                        list.add(new GXSimpleEntry<GXDLMSObject, Integer>(it,
                                4));
                    }
                }
                readList(list);
            }
        } catch (Exception e) {
            // Some meters are set multiple references, but don't support it.
            dlms.getNegotiatedConformance()
                    .remove(Conformance.MULTIPLE_REFERENCES);
        }
        if (!dlms.getNegotiatedConformance()
                .contains(Conformance.MULTIPLE_REFERENCES)) {
            for (GXDLMSObject it : objs) {
                try {
                    if (it instanceof GXDLMSRegister) {
                        readObject(it, 3);
                    } else if (it instanceof GXDLMSDemandRegister) {
                        readObject(it, 4);
                    }
                } catch (Exception e) {
                    traceLn(logFile,
                            "Err! Failed to read scaler and unit value: "
                                    + e.getMessage());
                    // Continue reading.
                }
            }
        }
    }

    /*
     * Read profile generic columns from the meter.
     */
    void readProfileGenericColumns(final GXDLMSObjectCollection objects,
            final PrintWriter logFile) {
        GXDLMSObjectCollection profileGenerics =
                objects.getObjects(ObjectType.PROFILE_GENERIC);
        for (GXDLMSObject it : profileGenerics) {
            traceLn(logFile, "Profile Generic " + it.getName() + " Columns:");
            GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) it;
            // Read columns.
            try {
                readObject(pg, 3);
                boolean first = true;
                StringBuilder sb = new StringBuilder();
                for (Entry<GXDLMSObject, GXDLMSCaptureObject> col : pg
                        .getCaptureObjects()) {
                    if (!first) {
                        sb.append(" | ");
                    }
                    sb.append(col.getKey().getName());
                    sb.append(" ");
                    String desc = col.getKey().getDescription();
                    if (desc != null) {
                        sb.append(desc);
                    }
                    first = false;
                }
                traceLn(logFile, sb.toString());
            } catch (Exception ex) {
                traceLn(logFile,
                        "Err! Failed to read columns:" + ex.getMessage());
                // Continue reading.
            }
        }
    }

    /**
     * Read all data from the meter except profile generic (Historical) data.
     */
    void readValues(final GXDLMSObjectCollection objects,
            final PrintWriter logFile) {
        for (GXDLMSObject it : objects) {
            if (!(it instanceof IGXDLMSBase)) {
                // If interface is not implemented.
                System.out.println(
                        "Unknown Interface: " + it.getObjectType().toString());
                continue;
            }

            if (it instanceof GXDLMSProfileGeneric) {
                // Profile generic are read later
                // because it might take so long time
                // and this is only a example.
                continue;
            }

            traceLn(logFile,
                    "-------- Reading " + it.getClass().getSimpleName() + " "
                            + it.getName().toString() + " "
                            + it.getDescription());
            for (int pos : ((IGXDLMSBase) it).getAttributeIndexToRead()) {
                try {

                    Object val = readObject(it, pos);
                    if (val instanceof byte[]) {
                        val = GXCommon.bytesToHex((byte[]) val);
                    } else if (val instanceof Double) {
                        NumberFormat formatter =
                                NumberFormat.getNumberInstance();
                        val = formatter.format(val);
                    } else if (val != null && val.getClass().isArray()) {
                        String str = "";
                        for (int pos2 = 0; pos2 != Array
                                .getLength(val); ++pos2) {
                            if (!str.equals("")) {
                                str += ", ";
                            }
                            Object tmp = Array.get(val, pos2);
                            if (tmp instanceof byte[]) {
                                str += GXCommon.bytesToHex((byte[]) tmp);
                            } else {
                                str += String.valueOf(tmp);
                            }
                        }
                        val = str;
                    }
                    traceLn(logFile,
                            "Index: " + pos + " Value: " + String.valueOf(val));
                } catch (Exception ex) {
                    traceLn(logFile,
                            "Error! Index: " + pos + " " + ex.getMessage());
                    // Continue reading.
                }
            }
        }
    }

    /**
     * Read profile generic (Historical) data.
     */
    void readProfileGenerics(final GXDLMSObjectCollection objects,
            final PrintWriter logFile) throws Exception {
        Object[] cells;
        GXDLMSObjectCollection profileGenerics =
                objects.getObjects(ObjectType.PROFILE_GENERIC);
        for (GXDLMSObject it : profileGenerics) {
            traceLn(logFile,
                    "-------- Reading " + it.getClass().getSimpleName() + " "
                            + it.getName().toString() + " "
                            + it.getDescription());

            long entriesInUse = ((Number) readObject(it, 7)).longValue();
            long entries = ((Number) readObject(it, 8)).longValue();
            traceLn(logFile, "Entries: " + String.valueOf(entriesInUse) + "/"
                    + String.valueOf(entries));
            GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) it;
            // If there are no columns.
            if (entriesInUse == 0 || pg.getCaptureObjects().size() == 0) {
                continue;
            }
            ///////////////////////////////////////////////////////////////////
            // Read first item.
            try {
                cells = readRowsByEntry(pg, 1, 1);
                for (Object rows : cells) {
                    for (Object cell : (Object[]) rows) {
                        if (cell instanceof byte[]) {
                            trace(logFile,
                                    GXCommon.bytesToHex((byte[]) cell) + " | ");
                        } else {
                            trace(logFile, cell + " | ");
                        }
                    }
                    traceLn(logFile, "");
                }
            } catch (Exception ex) {
                traceLn(logFile,
                        "Error! Failed to read first row: " + ex.getMessage());
                // Continue reading if device returns access denied error.
            }
            ///////////////////////////////////////////////////////////////////
            // Read last day.
            try {
                java.util.Calendar start = java.util.Calendar
                        .getInstance(java.util.TimeZone.getTimeZone("UTC"));
                start.set(java.util.Calendar.HOUR_OF_DAY, 0); // set hour to
                                                              // midnight
                start.set(java.util.Calendar.MINUTE, 0); // set minute in
                                                         // hour
                start.set(java.util.Calendar.SECOND, 0); // set second in
                                                         // minute
                start.set(java.util.Calendar.MILLISECOND, 0);
                start.add(java.util.Calendar.DATE, -1);

                java.util.Calendar end = java.util.Calendar.getInstance();
                end.set(java.util.Calendar.MINUTE, 0); // set minute in hour
                end.set(java.util.Calendar.SECOND, 0); // set second in
                                                       // minute
                end.set(java.util.Calendar.MILLISECOND, 0);
                cells = readRowsByRange((GXDLMSProfileGeneric) it,
                        start.getTime(), end.getTime());
                for (Object rows : cells) {
                    for (Object cell : (Object[]) rows) {
                        if (cell instanceof byte[]) {
                            System.out.print(
                                    GXCommon.bytesToHex((byte[]) cell) + " | ");
                        } else {
                            trace(logFile, cell + " | ");
                        }
                    }
                    traceLn(logFile, "");
                }
            } catch (Exception ex) {
                traceLn(logFile,
                        "Error! Failed to read last day: " + ex.getMessage());
                // Continue reading if device returns access denied error.
            }
        }
    }

    /*
     * Read all objects from the meter. This is only example. Usually there is
     * no need to read all data from the meter.
     */
    void readAllObjects(PrintWriter logFile) throws Exception {
        GXReplyData reply = new GXReplyData();
        // Get Association view from the meter.
        readDataBlock(dlms.getObjectsRequest(), reply);
        GXDLMSObjectCollection objects =
                dlms.parseObjects(reply.getData(), true);
        // Get description of the objects.
        GXDLMSConverter converter = new GXDLMSConverter();
        converter.updateOBISCodeInformation(objects);

        // Read Scalers and units from the register objects.
        readScalerAndUnits(objects, logFile);
        // Read Profile Generic columns.
        readProfileGenericColumns(objects, logFile);
        // Read all attributes from all objects.
        readValues(objects, logFile);
        // Read historical data.
        readProfileGenerics(objects, logFile);
    }
}