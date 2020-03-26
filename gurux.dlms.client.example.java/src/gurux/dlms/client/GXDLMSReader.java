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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import gurux.common.GXCommon;
import gurux.common.IGXMedia;
import gurux.common.ReceiveParameters;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSConverter;
import gurux.dlms.GXDLMSException;
import gurux.dlms.GXDLMSTranslator;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXReplyData;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.TranslatorOutputType;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Security;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSData;
import gurux.dlms.objects.GXDLMSDemandRegister;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXDLMSRegister;
import gurux.dlms.objects.GXXmlWriterSettings;
import gurux.dlms.objects.IGXDLMSBase;
import gurux.dlms.secure.GXDLMSSecureClient;
import gurux.io.BaudRate;
import gurux.io.Parity;
import gurux.io.StopBits;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class GXDLMSReader {
    IGXMedia Media;
    TraceLevel Trace;
    GXDLMSSecureClient dlms;
    boolean iec;
    java.nio.ByteBuffer replyBuff;
    int waitTime = 60000;
    final PrintWriter logFile;
    // Invocation counter (frame counter).
    String invocationCounter = null;

    public GXDLMSReader(GXDLMSSecureClient client, IGXMedia media,
            TraceLevel trace, final boolean useIec, final String frameCounter)
            throws Exception {
        Files.deleteIfExists(Paths.get("trace.txt"));
        logFile = new PrintWriter(
                new BufferedWriter(new FileWriter("logFile.txt")));

        iec = useIec;
        Trace = trace;
        Media = media;
        dlms = client;
        invocationCounter = frameCounter;
        if (trace.ordinal() > TraceLevel.WARNING.ordinal()) {
            System.out.println("Authentication: " + dlms.getAuthentication());
            System.out.println("ClientAddress: 0x"
                    + Integer.toHexString(dlms.getClientAddress()));
            System.out.println("ServerAddress: 0x"
                    + Integer.toHexString(dlms.getServerAddress()));
        }
        if (dlms.getInterfaceType() == InterfaceType.WRAPPER) {
            replyBuff = java.nio.ByteBuffer.allocate(8 + 1024);
        } else {
            replyBuff = java.nio.ByteBuffer.allocate(100);
        }
    }

    void disconnect() throws Exception {
        if (Media != null && Media.isOpen()) {
            System.out.println("DisconnectRequest");
            GXReplyData reply = new GXReplyData();
            readDLMSPacket(dlms.disconnectRequest(), reply);
        }
    }

    void release() throws Exception {
        if (Media != null && Media.isOpen()) {
            GXReplyData reply = new GXReplyData();
            try {
                // Release is call only for secured connections.
                // All meters are not supporting Release and it's causing
                // problems.
                if (dlms.getInterfaceType() == InterfaceType.WRAPPER
                        || (dlms.getInterfaceType() == InterfaceType.HDLC
                                && dlms.getCiphering()
                                        .getSecurity() != Security.NONE)) {
                    System.out.println("release");
                    readDataBlock(dlms.releaseRequest(), reply);
                }
            } catch (Exception e) {
                // All meters don't support release.
            }
        }
    }

    void close() throws Exception {
        if (Media != null && Media.isOpen()) {
            System.out.println("DisconnectRequest");
            GXReplyData reply = new GXReplyData();
            try {
                // Release is call only for secured connections.
                // All meters are not supporting Release and it's causing
                // problems.
                if (dlms.getInterfaceType() == InterfaceType.WRAPPER
                        || (dlms.getInterfaceType() == InterfaceType.HDLC
                                && dlms.getCiphering()
                                        .getSecurity() != Security.NONE)) {
                    readDataBlock(dlms.releaseRequest(), reply);
                }
            } catch (Exception e) {
                // All meters don't support release.
            }
            reply.clear();
            readDLMSPacket(dlms.disconnectRequest(), reply);
            Media.close();
        }
    }

    String now() {
        return new SimpleDateFormat("HH:mm:ss.SSS")
                .format(java.util.Calendar.getInstance().getTime());
    }

    void writeTrace(String line, TraceLevel level) {
        if (Trace.ordinal() >= level.ordinal()) {
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
     * Handle received notify messages.
     *
     * @param reply
     *            Received data.
     * @throws Exception
     */
    private void handleNotifyMessages(final GXReplyData reply)
            throws Exception {
        List<Entry<GXDLMSObject, Integer>> items =
                new ArrayList<Entry<GXDLMSObject, Integer>>();
        Object value = dlms.parseReport(reply, items);
        // If Event notification or Information report.
        if (value == null) {
            for (Entry<GXDLMSObject, Integer> it : items) {
                System.out.println(it.getKey().toString() + " Value:"
                        + it.getKey().getValues()[it.getValue() - 1]);
            }
        } else // Show data notification.
        {
            if (value instanceof List<?>) {
                for (Object it : (List<?>) value) {
                    System.out.println("Value:" + String.valueOf(it));
                }
            } else {
                System.out.println("Value:" + String.valueOf(value));
            }

        }
        reply.clear();
    }

    /**
     * Read DLMS Data from the device. If access is denied return null.
     */
    public void readDLMSPacket(byte[] data, GXReplyData reply)
            throws Exception {
        if (!reply.getStreaming() && (data == null || data.length == 0)) {
            return;
        }
        GXReplyData notify = new GXReplyData();
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
        if (dlms.getInterfaceType() == InterfaceType.WRAPPER) {
            p.setCount(8);
        } else {
            p.setCount(5);
        }
        p.setWaitTime(waitTime);
        GXByteBuffer rd = new GXByteBuffer();
        synchronized (Media.getSynchronous()) {
            while (!succeeded) {
                if (!reply.isStreaming()) {
                    writeTrace(
                            "TX: " + now() + "\t" + GXCommon.bytesToHex(data),
                            TraceLevel.VERBOSE);
                    Media.send(data, null);
                }
                if (p.getEop() == null) {
                    p.setCount(1);
                }
                succeeded = Media.receive(p);
                if (!succeeded) {
                    if (p.getEop() == null) {
                        p.setCount(dlms.getFrameSize(rd));
                    }
                    // Try to read again...
                    if (pos++ == 3) {
                        throw new RuntimeException(
                                "Failed to receive reply from the device in given time.");
                    }
                    System.out.println("Data send failed. Try to resend "
                            + pos.toString() + "/3");
                }
            }
            rd = new GXByteBuffer(p.getReply());
            int msgPos = 0;
            // Loop until whole DLMS packet is received.
            try {
                while (!dlms.getData(rd, reply, notify)) {
                    p.setReply(null);
                    if (notify.getData().getData() != null) {
                        // Handle notify.
                        if (!notify.isMoreData()) {
                            // Show received push message as XML.
                            GXDLMSTranslator t = new GXDLMSTranslator(
                                    TranslatorOutputType.SIMPLE_XML);
                            String xml = t.dataToXml(notify.getData());
                            System.out.println(xml);
                            notify.clear();
                            msgPos = rd.position();
                        }
                        continue;
                    }

                    if (p.getEop() == null) {
                        p.setCount(dlms.getFrameSize(rd));
                    }
                    while (!Media.receive(p)) {
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
                    rd.position(msgPos);
                    rd.set(p.getReply());
                }
            } catch (Exception e) {
                writeTrace("RX: " + now() + "\t" + rd.toString(),
                        TraceLevel.ERROR);
                throw e;
            }
        }
        writeTrace("RX: " + now() + "\t" + rd.toString(), TraceLevel.VERBOSE);
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
        if (data != null) {
            for (byte[] it : data) {
                reply.clear();
                readDataBlock(it, reply);
            }
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
        if (data != null && data.length != 0) {
            readDLMSPacket(data, reply);
            while (reply.isMoreData()) {
                if (reply.isStreaming()) {
                    data = null;
                } else {
                    data = dlms.receiverReady(reply);
                }
                readDLMSPacket(data, reply);
            }
        }
    }

    /**
     * Read Invocation counter (frame counter) from the meter and update it.
     * 
     * @throws Exception
     */
    private void updateFrameCounter() throws Exception {
        // Read frame counter if GeneralProtection is used.
        if (invocationCounter != null && dlms.getCiphering() != null
                && dlms.getCiphering().getSecurity() != Security.NONE) {
            initializeOpticalHead();
            byte[] data;
            GXReplyData reply = new GXReplyData();
            reply.clear();
            dlms.getProposedConformance().add(Conformance.GENERAL_PROTECTION);
            int add = dlms.getClientAddress();
            Authentication auth = dlms.getAuthentication();
            Security security = dlms.getCiphering().getSecurity();
            byte[] challenge = dlms.getCtoSChallenge();
            try {
                dlms.setClientAddress(16);
                dlms.setAuthentication(Authentication.NONE);
                dlms.getCiphering().setSecurity(Security.NONE);
                data = dlms.snrmRequest();
                if (data != null) {
                    readDLMSPacket(data, reply);
                    // Has server accepted client.
                    dlms.parseUAResponse(reply.getData());

                    // Allocate buffer to same size as transmit buffer of the
                    // meter.
                    // Size of replyBuff is payload and frame (Bop, EOP, crc).
                    int size =
                            (int) ((((Number) dlms.getLimits().getMaxInfoTX())
                                    .intValue() & 0xFFFFFFFFL) + 40);
                    replyBuff = java.nio.ByteBuffer.allocate(size);
                }
                // Generate AARQ request.
                // Split requests to multiple packets if needed.
                // If password is used all data might not fit to one packet.
                reply.clear();
                readDataBlock(dlms.aarqRequest(), reply);
                try {
                    // Parse reply.
                    dlms.parseAareResponse(reply.getData());
                    reply.clear();
                    GXDLMSData d = new GXDLMSData(invocationCounter);
                    read(d, 2);
                    long iv = ((Number) d.getValue()).longValue();
                    dlms.getCiphering().setInvocationCounter(1 + iv);
                    writeTrace("Invocation counter: " + String.valueOf(iv),
                            TraceLevel.INFO);
                    reply.clear();
                    disconnect();
                } catch (Exception Ex) {
                    disconnect();
                    throw Ex;
                }
            } finally {
                dlms.setClientAddress(add);
                dlms.setAuthentication(auth);
                dlms.getCiphering().setSecurity(security);
                dlms.setCtoSChallenge(challenge);
            }
        }
    }

    void initializeOpticalHead() throws Exception {
        if (iec) {
            ReceiveParameters<byte[]> p =
                    new ReceiveParameters<byte[]>(byte[].class);
            p.setAllData(false);
            p.setEop((byte) '\n');
            p.setWaitTime(waitTime);
            String data;
            String replyStr;
            synchronized (Media.getSynchronous()) {
                data = "/?!\r\n";
                writeTrace(
                        "TX: " + now() + "\t"
                                + GXCommon.bytesToHex(data.getBytes("ASCII")),
                        TraceLevel.VERBOSE);
                Media.send(data, null);
                if (!Media.receive(p)) {
                    throw new Exception(
                            "Failed to received reply from the media.");
                }
                writeTrace(
                        "RX: " + now() + "\t"
                                + GXCommon.bytesToHex(p.getReply()),
                        TraceLevel.VERBOSE);
                // If echo is used.
                replyStr = new String(p.getReply());
                if (data.equals(replyStr)) {
                    p.setReply(null);
                    if (!Media.receive(p)) {
                        throw new Exception(
                                "Failed to received reply from the media.");
                    }
                    writeTrace(
                            "RX: " + now() + "\t"
                                    + GXCommon.bytesToHex(p.getReply()),
                            TraceLevel.VERBOSE);
                    replyStr = new String(p.getReply());
                }
            }
            if (replyStr.length() == 0 || replyStr.charAt(0) != '/') {
                throw new Exception("Invalid responce : " + replyStr);
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
            byte[] tmp = new byte[] { 0x06, controlCharacter, (byte) baudrate,
                    ModeControlCharacter, 13, 10 };
            p.setReply(null);
            synchronized (Media.getSynchronous()) {
                Media.send(tmp, null);
                writeTrace("RX: " + now() + "\t" + GXCommon.bytesToHex(tmp),
                        TraceLevel.VERBOSE);
                p.setWaitTime(100);
                if (Media.receive(p)) {
                    writeTrace(
                            "RX: " + now() + "\t"
                                    + GXCommon.bytesToHex(p.getReply()),
                            TraceLevel.VERBOSE);
                }
                Media.close();
                // This sleep make sure that all meters can be read.
                Thread.sleep(400);
                GXSerial serial = (GXSerial) Media;
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

    /**
     * Initializes connection.
     *
     * @param port
     * @throws InterruptedException
     * @throws Exception
     */
    void initializeConnection() throws Exception, InterruptedException {
        if (Media instanceof GXSerial) {
            GXSerial serial = (GXSerial) Media;
            serial.setDtrEnable(true);
            serial.setRtsEnable(true);
        }
        System.out.println("Standard: " + dlms.getStandard().toString());
        if (dlms.getCiphering().getSecurity() != Security.NONE) {
            System.out
                    .println("Security: " + dlms.getCiphering().getSecurity());
            System.out.println("System title: " + GXCommon
                    .bytesToHex(dlms.getCiphering().getSystemTitle()));
            System.out.println("Authentication key: " + GXCommon
                    .bytesToHex(dlms.getCiphering().getAuthenticationKey()));
            System.out.println("Block cipher key " + GXCommon
                    .bytesToHex(dlms.getCiphering().getBlockCipherKey()));
            if (dlms.getCiphering().getDedicatedKey() != null) {
                System.out.println("Dedicated key: " + GXCommon
                        .bytesToHex(dlms.getCiphering().getDedicatedKey()));
            }
        }
        updateFrameCounter();
        initializeOpticalHead();
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
        if (dlms.getAuthentication().getValue() > Authentication.LOW
                .getValue()) {
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
    public Object read(GXDLMSObject item, int attributeIndex) throws Exception {
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
     * Read list of attributes.
     */
    public void readList(List<Entry<GXDLMSObject, Integer>> list)
            throws Exception {
        if (!list.isEmpty()) {
            byte[][] data = dlms.readList(list);
            GXReplyData reply = new GXReplyData();
            List<Object> values = new ArrayList<Object>(list.size());
            for (byte[] it : data) {
                readDataBlock(it, reply);
                // Value is null if data is send in multiple frames.
                if (reply.getValue() != null) {
                    values.addAll((List<?>) reply.getValue());
                }
                reply.clear();
            }
            if (values.size() != list.size()) {
                throw new Exception(
                        "Invalid reply. Read items count do not match.");
            }
            dlms.updateValues(list, values);
        }
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
        Object entries = read(pg, 7);
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
            final GXDateTime start, final GXDateTime end) throws Exception {
        GXReplyData reply = new GXReplyData();
        byte[][] data = dlms.readRowsByRange(pg, start, end);
        readDataBlock(data, reply);
        return (Object[]) dlms.updateValue(pg, 2, reply.getValue());
    }

    /*
     * Read Scalers and units from the register objects.
     */
    void readScalerAndUnits() throws Exception {
        GXDLMSObjectCollection objs = dlms.getObjects()
                .getObjects(new ObjectType[] { ObjectType.REGISTER,
                        ObjectType.DEMAND_REGISTER,
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
                        read(it, 3);
                    } else if (it instanceof GXDLMSDemandRegister) {
                        read(it, 4);
                    }
                } catch (Exception e) {
                    // Actaric SL7000 can return error here. Continue reading.
                }
            }
        }
    }

    /*
     * Read profile generic columns from the meter.
     */
    void getProfileGenericColumns() {
        GXDLMSObjectCollection profileGenerics =
                dlms.getObjects().getObjects(ObjectType.PROFILE_GENERIC);
        for (GXDLMSObject it : profileGenerics) {
            writeTrace("Profile Generic " + it.getName() + "Columns:",
                    TraceLevel.INFO);
            GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) it;
            // Read columns.
            try {
                read(pg, 3);
                if (Trace.ordinal() > TraceLevel.WARNING.ordinal()) {
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
                    writeTrace(sb.toString(), TraceLevel.INFO);
                }
            } catch (Exception ex) {
                writeTrace("Err! Failed to read columns:" + ex.getMessage(),
                        TraceLevel.ERROR);
                // Continue reading.
            }
        }
    }

    /**
     * Read all data from the meter except profile generic (Historical) data.
     */
    void getReadOut() {
        for (GXDLMSObject it : dlms.getObjects()) {
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
            writeTrace("-------- Reading " + it.getClass().getSimpleName() + " "
                    + it.getName().toString() + " " + it.getDescription(),
                    TraceLevel.INFO);
            for (int pos : ((IGXDLMSBase) it).getAttributeIndexToRead(true)) {
                try {
                    if (pos == 1) {
                        continue;
                    }
                    Object val = read(it, pos);
                    showValue(pos, val);
                } catch (Exception ex) {
                    writeTrace("Error! Index: " + pos + " " + ex.getMessage(),
                            TraceLevel.ERROR);
                    writeTrace(ex.toString(), TraceLevel.ERROR);
                    // Continue reading.
                }
            }
        }
    }

    void showValue(final int pos, final Object value) {
        Object val = value;
        if (val instanceof byte[]) {
            val = GXCommon.bytesToHex((byte[]) val);
        } else if (val instanceof Double) {
            NumberFormat formatter = NumberFormat.getNumberInstance();
            val = formatter.format(val);
        } else if (val instanceof List) {
            StringBuilder sb = new StringBuilder();
            for (Object tmp : (List<?>) val) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                if (tmp instanceof byte[]) {
                    sb.append(GXCommon.bytesToHex((byte[]) tmp));
                } else {
                    sb.append(String.valueOf(tmp));
                }
            }
            val = sb.toString();
        } else if (val != null && val.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            for (int pos2 = 0; pos2 != Array.getLength(val); ++pos2) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                Object tmp = Array.get(val, pos2);
                if (tmp instanceof byte[]) {
                    sb.append(GXCommon.bytesToHex((byte[]) tmp));
                } else {
                    sb.append(String.valueOf(tmp));
                }
            }
            val = sb.toString();
        }
        writeTrace("Index: " + pos + " Value: " + String.valueOf(val),
                TraceLevel.INFO);
    }

    /**
     * Read profile generic (Historical) data.
     */
    void getProfileGenerics() throws Exception {
        Object[] cells;
        GXDLMSObjectCollection profileGenerics =
                dlms.getObjects().getObjects(ObjectType.PROFILE_GENERIC);
        for (GXDLMSObject it : profileGenerics) {
            writeTrace("-------- Reading " + it.getClass().getSimpleName() + " "
                    + it.getName().toString() + " " + it.getDescription(),
                    TraceLevel.INFO);
            long entriesInUse = ((Number) read(it, 7)).longValue();
            long entries = ((Number) read(it, 8)).longValue();
            writeTrace("Entries: " + String.valueOf(entriesInUse) + "/"
                    + String.valueOf(entries), TraceLevel.INFO);
            GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) it;
            // If there are no columns.
            if (entriesInUse == 0 || pg.getCaptureObjects().size() == 0) {
                continue;
            }
            ///////////////////////////////////////////////////////////////////
            // Read first item.
            try {
                cells = readRowsByEntry(pg, 1, 1);
                if (Trace.ordinal() > TraceLevel.WARNING.ordinal()) {
                    for (Object rows : cells) {
                        StringBuilder sb = new StringBuilder();
                        for (Object cell : (Object[]) rows) {
                            if (cell instanceof byte[]) {
                                sb.append(GXCommon.bytesToHex((byte[]) cell));
                                sb.append(" | ");
                            } else {
                                sb.append(String.valueOf(cell));
                                sb.append(" | ");
                            }
                        }
                        writeTrace(sb.toString(), TraceLevel.INFO);
                    }
                }
            } catch (Exception ex) {
                writeTrace(
                        "Error! Failed to read first row: " + ex.getMessage(),
                        TraceLevel.ERROR);
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
                GXDateTime s = new GXDateTime(start);
                GXDateTime e = new GXDateTime(end);
                cells = readRowsByRange((GXDLMSProfileGeneric) it, s, e);
                for (Object rows : cells) {
                    StringBuilder sb = new StringBuilder();
                    for (Object cell : (Object[]) rows) {
                        if (cell instanceof byte[]) {
                            sb.append(GXCommon.bytesToHex((byte[]) cell));
                            sb.append(" | ");
                        } else {
                            sb.append(String.valueOf(cell));
                            sb.append(" | ");
                        }
                    }
                    writeTrace(sb.toString(), TraceLevel.INFO);
                }
            } catch (Exception ex) {
                writeTrace("Error! Failed to read last day: " + ex.getMessage(),
                        TraceLevel.ERROR);
                // Continue reading if device returns access denied error.
            }
        }
    }

    /**
     * Read association view.
     *
     * @throws Exception
     */
    public void getAssociationView() throws Exception {
        GXReplyData reply = new GXReplyData();
        // Get Association view from the meter.
        readDataBlock(dlms.getObjectsRequest(), reply);
        dlms.parseObjects(reply.getData(), true);
        // Access rights must read differently when short Name referencing is
        // used.
        if (!dlms.getUseLogicalNameReferencing()) {
            GXDLMSAssociationShortName sn = (GXDLMSAssociationShortName) dlms
                    .getObjects().findBySN(0xFA00);
            if (sn != null && sn.getVersion() > 0) {
                read(sn, 3);
            }
        }
    }

    /*
     * Read all objects from the meter. This is only example. Usually there is
     * no need to read all data from the meter.
     */
    void readAll(String outputFile) throws Exception {
        try {
            initializeConnection();
            boolean read = false;
            if (outputFile != null && new File(outputFile).exists()) {
                try {
                    GXDLMSObjectCollection list =
                            GXDLMSObjectCollection.load(outputFile);
                    dlms.getObjects().addAll(list);
                    GXDLMSConverter c = new GXDLMSConverter(dlms.getStandard());
                    c.updateOBISCodeInformation(dlms.getObjects());
                    read = true;
                } catch (Exception ex) {
                    // It's OK if this fails.
                    System.out.print(ex.getMessage());
                }
            }
            if (!read) {
                getAssociationView();
                // Read Scalers and units from the register objects.
                readScalerAndUnits();
                // Read Profile Generic columns.
                getProfileGenericColumns();
            }
            // Read all attributes from all objects.
            getReadOut();
            // Read historical data.
            getProfileGenerics();
        } finally {
            close();
        }
        if (outputFile != null) {
            GXXmlWriterSettings s = new GXXmlWriterSettings();
            s.setIgnoreDefaultValues(false);
            dlms.getObjects().save(outputFile, s);
        }

    }
}