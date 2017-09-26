//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL:  $
//
// Version:         $Revision: $,
//                  $Date:  $
//                  $Author: $
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
// More information of Gurux DLMS/COSEM Director: http://www.gurux.org/GXDLMSDirector
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.server.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;

import gurux.common.IGXMediaListener;
import gurux.common.MediaStateEventArgs;
import gurux.common.PropertyChangedEventArgs;
import gurux.common.ReceiveEventArgs;
import gurux.common.TraceEventArgs;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSConnectionEventArgs;
import gurux.dlms.GXDate;
import gurux.dlms.GXDateTime;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.GXTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.objects.GXDLMSActionSchedule;
import gurux.dlms.objects.GXDLMSActivityCalendar;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSAutoAnswer;
import gurux.dlms.objects.GXDLMSAutoConnect;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSClock;
import gurux.dlms.objects.GXDLMSData;
import gurux.dlms.objects.GXDLMSDayProfile;
import gurux.dlms.objects.GXDLMSDayProfileAction;
import gurux.dlms.objects.GXDLMSDemandRegister;
import gurux.dlms.objects.GXDLMSHdlcSetup;
import gurux.dlms.objects.GXDLMSIECOpticalPortSetup;
import gurux.dlms.objects.GXDLMSImageTransfer;
import gurux.dlms.objects.GXDLMSIp4Setup;
import gurux.dlms.objects.GXDLMSMacAddressSetup;
import gurux.dlms.objects.GXDLMSModemConfiguration;
import gurux.dlms.objects.GXDLMSModemInitialisation;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXDLMSPushSetup;
import gurux.dlms.objects.GXDLMSRegister;
import gurux.dlms.objects.GXDLMSRegisterMonitor;
import gurux.dlms.objects.GXDLMSSapAssignment;
import gurux.dlms.objects.GXDLMSSeasonProfile;
import gurux.dlms.objects.GXDLMSTcpUdpSetup;
import gurux.dlms.objects.GXDLMSWeekProfile;
import gurux.dlms.objects.enums.AutoAnswerMode;
import gurux.dlms.objects.enums.AutoAnswerStatus;
import gurux.dlms.objects.enums.AutoConnectMode;
import gurux.dlms.objects.enums.BaudRate;
import gurux.dlms.objects.enums.LocalPortResponseTime;
import gurux.dlms.objects.enums.OpticalProtocolMode;
import gurux.dlms.objects.enums.SingleActionScheduleType;
import gurux.dlms.objects.enums.SortMethod;
import gurux.dlms.secure.GXDLMSSecureServer2;
import gurux.net.GXNet;
import gurux.net.enums.NetworkType;

/**
 * All example servers are using same objects.
 */
public class GXDLMSBase extends GXDLMSSecureServer2
        implements IGXMediaListener, gurux.net.IGXNetListener {

    TraceLevel Trace = TraceLevel.INFO;
    private GXNet media;

    static final Object fileLock = new Object();

    // Date file is saved to same directory where app is.
    static final String getDataFile() {
        final String dir = Paths
                .get(GXDLMSBase.class.getProtectionDomain().getCodeSource()
                        .getLocation().getPath().substring(1))
                .getParent().toString();
        return dir + "/data.csv";
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Association logical name.
     * @param hdlc
     *            HDLC settings.
     */
    public GXDLMSBase(final GXDLMSAssociationLogicalName ln,
            final GXDLMSHdlcSetup hdlc) {
        super(ln, hdlc);
        this.setMaxReceivePDUSize(1024);
        byte[] secret = "Gurux".getBytes();
        ln.setSecret(secret);
    }

    /**
     * Constructor.
     * 
     * @param sn
     *            Association short name.
     * @param type
     *            HDLC settings.
     */
    public GXDLMSBase(GXDLMSAssociationShortName sn,
            final GXDLMSHdlcSetup hdlc) {
        super(sn, hdlc);
        this.setMaxReceivePDUSize(1024);
        byte[] secret = "Gurux".getBytes();
        sn.setSecret(secret);
    }

    /**
     * Constructor.
     * 
     * @param ln
     *            Association logical name.
     * @param wrapper
     *            Wrapper settings.
     */
    public GXDLMSBase(final GXDLMSAssociationLogicalName ln,
            final GXDLMSTcpUdpSetup wrapper) {
        super(ln, wrapper);
        this.setMaxReceivePDUSize(1024);
        byte[] secret = "Gurux".getBytes();
        ln.setSecret(secret);
    }

    /**
     * Constructor.
     * 
     * @param sn
     *            Association short name.
     * @param wrapper
     *            Wrapper settings.
     */
    public GXDLMSBase(GXDLMSAssociationShortName sn,
            final GXDLMSTcpUdpSetup wrapper) {
        super(sn, wrapper);
        this.setMaxReceivePDUSize(1024);
        byte[] secret = "Gurux".getBytes();
        sn.setSecret(secret);
    }

    /*
     * Add Logical Device Name. 123456 is meter serial number.
     */
    void addLogicalDeviceName() {
        GXDLMSData d = new GXDLMSData("0.0.42.0.0.255");
        d.setValue("Gurux123456");
        // Set access right. Client can't change Device name.
        d.setAccess(2, AccessMode.READ);
        d.setDataType(2, DataType.OCTET_STRING);
        d.setUIDataType(2, DataType.STRING);
        getItems().add(d);
    }

    /*
     * Add firmware version.
     */
    void addFirmwareVersion() {
        GXDLMSData d = new GXDLMSData("1.0.0.2.0.255");
        d.setValue("Gurux FW 0.0.1");
        getItems().add(d);
    }

    GXDLMSRegister addRegister() {
        // Add Last average.
        GXDLMSRegister r = new GXDLMSRegister("1.1.21.25.0.255");
        // Set access right. Client can't change Device name.
        r.setAccess(2, AccessMode.READ);
        getItems().add(r);
        return r;
    }

    /**
     * Add default clock. Clock's Logical Name is 0.0.1.0.0.255.
     */
    GXDLMSClock addClock() {
        GXDLMSClock clock = new GXDLMSClock();
        clock.setBegin(new GXDateTime(-1, 9, 1, -1, -1, -1, -1));
        clock.setEnd(new GXDateTime(-1, 3, 1, -1, -1, -1, -1));
        clock.setDeviation(0);
        getItems().add(clock);
        return clock;
    }

    /**
     * Add TCP/IP UDP setup object.
     */
    void addTcpUdpSetup() {
        GXDLMSTcpUdpSetup tcp = new GXDLMSTcpUdpSetup();
        getItems().add(tcp);
    }

    /*
     * Add profile generic (historical) object.
     */
    GXDLMSProfileGeneric addProfileGeneric(final GXDLMSClock clock,
            final GXDLMSRegister register) {
        GXDLMSProfileGeneric pg = new GXDLMSProfileGeneric("1.0.99.1.0.255");
        // Set capture period to 60 second.
        pg.setCapturePeriod(60);
        // Maximum row count.
        pg.setProfileEntries(100);
        pg.setSortMethod(SortMethod.FIFO);
        pg.setSortObject(clock);
        // Add columns.
        pg.addCaptureObject(clock, 2, 0);
        pg.addCaptureObject(register, 2, 0);
        getItems().add(pg);
        // Create 10 000 rows for profile generic file.
        // In example profile generic we have two columns.
        // Date time and integer value.
        int rowCount = 10000;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -(rowCount - 1));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat();
        for (int pos = 0; pos != rowCount; ++pos) {
            sb.append(df.format(cal.getTime()));
            sb.append(';');
            sb.append(String.valueOf(pos + 1));
            sb.append(System.lineSeparator());
            cal.add(Calendar.HOUR, 1);
        }
        synchronized (fileLock) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(getDataFile(), false);
                writer.write(sb.toString());
                writer.close();
            } catch (IOException e) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e1) {
                    }
                }
                throw new RuntimeException(e.getMessage());
            }
        }
        return pg;
    }

    /*
     * Add Auto connect object.
     */
    void addAutoConnect() {
        GXDLMSAutoConnect ac = new GXDLMSAutoConnect();
        ac.setMode(AutoConnectMode.AUTO_DIALLING_ALLOWED_ANYTIME);
        ac.setRepetitions(10);
        ac.setRepetitionDelay(60);
        // Calling is allowed between 1am to 6am.
        ac.getCallingWindow()
                .add(new AbstractMap.SimpleEntry<GXDateTime, GXDateTime>(
                        new GXTime(1, 0, 0, -1), new GXTime(6, 0, 0, -1)));
        ac.setDestinations(new String[] { "www.gurux.org" });
        getItems().add(ac);
    }

    /*
     * Add Activity Calendar object.
     */
    void addActivityCalendar() {
        java.util.Calendar tm = Calendar.getInstance();
        java.util.Date now = tm.getTime();
        GXDLMSActivityCalendar activity = new GXDLMSActivityCalendar();
        activity.setCalendarNameActive("Active");
        activity.setSeasonProfileActive(
                new GXDLMSSeasonProfile[] { new GXDLMSSeasonProfile(
                        "Summer time", new GXDate(-1, 3, 31), "") });
        GXDLMSWeekProfile wp = new GXDLMSWeekProfile();
        wp.setName("Monday");
        wp.setMonday(1);
        wp.setTuesday(1);
        wp.setWednesday(1);
        wp.setThursday(1);
        wp.setFriday(1);
        wp.setSaturday(1);
        wp.setSunday(1);
        activity.setWeekProfileTableActive(new GXDLMSWeekProfile[] { wp });
        activity.setDayProfileTableActive(
                new GXDLMSDayProfile[] { new GXDLMSDayProfile(1,
                        new GXDLMSDayProfileAction[] {
                                new GXDLMSDayProfileAction(new GXTime(now),
                                        "0.1.10.1.101.255", 1) }) });
        activity.setCalendarNamePassive("Passive");
        activity.setSeasonProfilePassive(
                new GXDLMSSeasonProfile[] { new GXDLMSSeasonProfile(
                        "Winter time", new GXDate(-1, 10, 30), "") });
        wp = new GXDLMSWeekProfile();
        wp.setName("Tuesday");
        wp.setMonday(1);
        wp.setTuesday(1);
        wp.setWednesday(1);
        wp.setThursday(1);
        wp.setFriday(1);
        wp.setSaturday(1);
        wp.setSunday(1);
        activity.setWeekProfileTablePassive(new GXDLMSWeekProfile[] { wp });
        activity.setDayProfileTablePassive(
                new GXDLMSDayProfile[] { new GXDLMSDayProfile(1,
                        new GXDLMSDayProfileAction[] {
                                new GXDLMSDayProfileAction(new GXTime(now),
                                        "0.0.1.0.0.255", 1) }) });
        activity.setTime(new GXDateTime(now));
        getItems().add(activity);
    }

    /*
     * Add Optical Port Setup object.
     */
    void addOpticalPortSetup() {
        GXDLMSIECOpticalPortSetup optical = new GXDLMSIECOpticalPortSetup();
        optical.setDefaultMode(OpticalProtocolMode.DEFAULT);
        optical.setProposedBaudrate(BaudRate.BAUDRATE_9600);
        optical.setDefaultBaudrate(BaudRate.BAUDRATE_300);
        optical.setResponseTime(LocalPortResponseTime.ms200);
        optical.setDeviceAddress("Gurux");
        optical.setPassword1("Gurux1");
        optical.setPassword2("Gurux2");
        optical.setPassword5("Gurux5");
        getItems().add(optical);
    }

    /*
     * Add Demand Register object.
     */
    void addDemandRegister() {
        java.util.Calendar tm = Calendar.getInstance();
        java.util.Date now = tm.getTime();
        GXDLMSDemandRegister dr = new GXDLMSDemandRegister();
        dr.setLogicalName("0.0.1.0.0.255");
        dr.setCurrentAverageValue(10);
        dr.setLastAverageValue(20);
        dr.setStatus((int) 1);
        dr.setStartTimeCurrent(new GXDateTime(now));
        dr.setCaptureTime(new GXDateTime(now));
        dr.setPeriod(10);
        dr.setNumberOfPeriods(1);
        getItems().add(dr);
    }

    /*
     * Add Register Monitor object.
     */
    void addRegisterMonitor(GXDLMSRegister register) {
        GXDLMSRegisterMonitor rm = new GXDLMSRegisterMonitor();
        rm.setLogicalName("0.0.1.0.0.255");
        rm.setThresholds(null);
        rm.getMonitoredValue().update(register, 2);
        getItems().add(rm);
    }

    /*
     * Add action schedule object.
     */
    void addActionSchedule() {
        GXDLMSActionSchedule actionS = new GXDLMSActionSchedule();
        actionS.setLogicalName("0.0.1.0.0.255");
        actionS.setExecutedScriptLogicalName("1.2.3.4.5.6");
        actionS.setExecutedScriptSelector(1);
        actionS.setType(SingleActionScheduleType.SingleActionScheduleType1);
        actionS.setExecutionTime(new GXDateTime[] {
                new GXDateTime(Calendar.getInstance().getTime()) });
        getItems().add(actionS);
    }

    /*
     * Add SAP Assignment object.
     */
    void addSapAssignment() {
        GXDLMSSapAssignment sap = new GXDLMSSapAssignment();
        sap.getSapAssignmentList()
                .add(new AbstractMap.SimpleEntry<Integer, String>(1, "Gurux"));
        sap.getSapAssignmentList().add(
                new AbstractMap.SimpleEntry<Integer, String>(16, "Gurux-2"));
        getItems().add(sap);
    }

    /**
     * Add Auto Answer object.
     */
    void addAutoAnswer() {
        GXDLMSAutoAnswer aa = new GXDLMSAutoAnswer();
        aa.setMode(AutoAnswerMode.CALL);
        aa.getListeningWindow()
                .add(new AbstractMap.SimpleEntry<GXDateTime, GXDateTime>(
                        new GXDateTime(-1, -1, -1, 6, -1, -1, -1),
                        new GXDateTime(-1, -1, -1, 8, -1, -1, -1)));
        aa.setStatus(AutoAnswerStatus.INACTIVE);
        aa.setNumberOfCalls(0);
        aa.setNumberOfRingsInListeningWindow(1);
        aa.setNumberOfRingsOutListeningWindow(2);
        getItems().add(aa);
    }

    /*
     * Add Modem Configuration object.
     */
    void addModemConfiguration() {
        GXDLMSModemConfiguration mc = new GXDLMSModemConfiguration();
        mc.setCommunicationSpeed(BaudRate.BAUDRATE_600);
        GXDLMSModemInitialisation init = new GXDLMSModemInitialisation();
        init.setRequest("AT");
        init.setResponse("OK");
        init.setDelay(0);
        mc.setInitialisationStrings(new GXDLMSModemInitialisation[] { init });
        getItems().add(mc);
    }

    /**
     * Add MAC Address Setup object.
     */
    void addMacAddressSetup() {
        GXDLMSMacAddressSetup mac = new GXDLMSMacAddressSetup();
        mac.setMacAddress("11:22:33:44:55:66");
        getItems().add(mac);
    }

    /**
     * Add Image transfer object.
     */
    void addImageTransfer() {
        GXDLMSImageTransfer i = new GXDLMSImageTransfer();
        getItems().add(i);
    }

    /**
     * Add IP4 setup object.
     */
    void addIp4Setup() {
        GXDLMSIp4Setup ip4 = new GXDLMSIp4Setup();
        // Get FIRST local IP address.
        try {
            ip4.setIPAddress(
                    InetAddress.getLocalHost().getHostAddress().toString());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getMessage());
        }
        getItems().add(ip4);
    }

    /**
     * Add Push Setup. (On Connectivity object).
     */
    void addPushSetup() {
        GXDLMSPushSetup push = new GXDLMSPushSetup("0.0.25.9.0.255");
        getItems().add(push);
        // Add push object itself. This is needed to tell structure of data to
        // the Push listener.
        push.getPushObjectList()
                .add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(push,
                        new GXDLMSCaptureObject(2, 0)));
        // Add logical device name.
        GXDLMSObject ldn =
                getItems().findByLN(ObjectType.DATA, "0.0.42.0.0.255");
        push.getPushObjectList()
                .add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(ldn,
                        new GXDLMSCaptureObject(2, 0)));
        // Add .0.0.25.1.0.255 Ch. 0 IPv4 setup IP address.
        GXDLMSObject ip4 =
                getItems().findByLN(ObjectType.IP4_SETUP, "0.0.25.1.0.255");
        push.getPushObjectList()
                .add(new GXSimpleEntry<GXDLMSObject, GXDLMSCaptureObject>(ip4,
                        new GXDLMSCaptureObject(3, 0)));
    }

    /**
     * Add available objects.
     * 
     * @param server
     */
    public void initialize(int port, TraceLevel trace) throws Exception {
        media = new gurux.net.GXNet(NetworkType.TCP, port);
        media.setTrace(TraceLevel.VERBOSE);
        Trace = trace;
        media.addListener(this);
        media.open();
        ///////////////////////////////////////////////////////////////////////
        // Add objects of the meter.
        addLogicalDeviceName();
        // Add firmware version.
        addFirmwareVersion();
        // Add example register object.
        GXDLMSRegister register = addRegister();
        // Add default clock object.
        GXDLMSClock clock = addClock();
        // Add TCP/IP UDP setup object.
        addTcpUdpSetup();
        // Add profile generic object.
        addProfileGeneric(clock, register);
        // Add Auto connect object.
        addAutoConnect();
        // Add Activity Calendar object.
        addActivityCalendar();

        // Add Optical Port Setup object.
        addOpticalPortSetup();

        // Add Demand Register object.
        addDemandRegister();

        // Add Register Monitor object.
        addRegisterMonitor(register);

        // Add action schedule object.
        addActionSchedule();

        // Add SAP Assignment object.
        addSapAssignment();
        // Add Auto Answer object.
        addAutoAnswer();

        // Add Modem Configuration object.
        addModemConfiguration();
        // Add MAC Address Setup object.
        addMacAddressSetup();
        // Add Image transfer object.
        addImageTransfer();

        // Add IP4 Setup object.
        addIp4Setup();

        // Add Push Setup. (On Connectivity object)
        addPushSetup();
        ///////////////////////////////////////////////////////////////////////
        // Server must initialize after all objects are added.
        super.initialize();
    }

    @Override
    public void close() throws Exception {
        super.close();
        media.close();
    }

    /**
     * Return data using start and end indexes.
     * 
     * @param p
     *            ProfileGeneric
     * @param index
     * @param count
     * @return Add data Rows
     */
    private void getProfileGenericDataByEntry(final GXDLMSProfileGeneric p,
            long index, final long count) {
        // Clear old data. It's already serialized.
        p.clearBuffer();
        BufferedReader reader = null;
        SimpleDateFormat df = new SimpleDateFormat();
        synchronized (fileLock) {
            try {
                reader = new BufferedReader(new FileReader(getDataFile()));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip row
                    if (index > 0) {
                        --index;
                    } else if (line.length() != 0) {
                        String[] values = line.split("[;]", -1);
                        p.addRow(new Object[] { df.parse(values[0]),
                                Integer.parseInt(values[1]) });
                    }
                    if (p.getBuffer().length == count) {
                        break;
                    }
                }
                reader.close();
            } catch (Exception e) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e1) {
                    }
                }
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * Find start index and row count using start and end date time.
     * 
     * @param start
     *            Start time.
     * @param end
     *            End time
     * @param index
     *            Start index.
     * @param count
     *            Item count.
     */
    private void getProfileGenericDataByRange(ValueEventArgs e) {
        GXDateTime start = (GXDateTime) GXDLMSClient.changeType(
                (byte[]) ((Object[]) e.getParameters())[1], DataType.DATETIME);
        GXDateTime end = (GXDateTime) GXDLMSClient.changeType(
                (byte[]) ((Object[]) e.getParameters())[2], DataType.DATETIME);

        synchronized (fileLock) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(getDataFile()));
                String line;
                SimpleDateFormat df = new SimpleDateFormat();
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split("[;]", -1);
                    Date tm = df.parse(values[0]);
                    if (tm.compareTo(end.getCalendar().getTime()) > 0) {
                        // If all data is read.
                        break;
                    }
                    if (tm.compareTo(start.getCalendar().getTime()) < 0) {
                        // If we have not find first item.
                        e.setRowBeginIndex(e.getRowBeginIndex() + 1);
                    }
                    e.setRowEndIndex(e.getRowEndIndex() + 1);
                }
                reader.close();
            } catch (Exception ex) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e1) {
                    }
                }
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    /**
     * Get row count.
     * 
     * @return
     */
    private int getProfileGenericDataCount() {
        int rows = 0;
        BufferedReader reader = null;
        synchronized (fileLock) {
            try {
                reader = new BufferedReader(new FileReader(getDataFile()));
                while (reader.readLine() != null) {
                    ++rows;
                }
                reader.close();
            } catch (Exception e) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e1) {
                    }
                }
                throw new RuntimeException(e.getMessage());
            }
        }
        return rows;
    }

    @Override
    public void onPreRead(ValueEventArgs[] args) {
        for (ValueEventArgs e : args) {
            // Framework will handle Association objects automatically.
            if (e.getTarget() instanceof GXDLMSAssociationLogicalName
                    || e.getTarget() instanceof GXDLMSAssociationShortName) {
                continue;
            }
            if (e.getTarget() instanceof GXDLMSClock && e.getIndex() == 2) {
                GXDLMSClock c = (GXDLMSClock) e.getTarget();
                c.setTime(c.now());
            }
            // Framework will handle profile generic automatically.
            if (e.getTarget() instanceof GXDLMSProfileGeneric) {
                // If buffer is read and we want to save memory.
                if (e.getIndex() == 7) {
                    // If client wants to know EntriesInUse.
                    GXDLMSProfileGeneric p =
                            (GXDLMSProfileGeneric) e.getTarget();
                    p.setEntriesInUse(getProfileGenericDataCount());
                }
                if (e.getIndex() == 2) {
                    // Client reads buffer.
                    GXDLMSProfileGeneric p =
                            (GXDLMSProfileGeneric) e.getTarget();
                    // Read rows from file.
                    // If reading first time.
                    if (e.getRowEndIndex() == 0) {
                        if (e.getSelector() == 0) {
                            e.setRowEndIndex(getProfileGenericDataCount());
                        } else if (e.getSelector() == 1) {
                            // Read by entry.
                            getProfileGenericDataByRange(e);
                        } else if (e.getSelector() == 2) {
                            // Read by range.
                            e.setRowBeginIndex(
                                    ((long) ((Object[]) e.getParameters())[0]));
                            e.setRowEndIndex(e.getRowBeginIndex()
                                    + (long) ((Object[]) e.getParameters())[1]);
                            // If client wants to read more data what we have.
                            int cnt = getProfileGenericDataCount();
                            if (e.getRowEndIndex() - e.getRowBeginIndex() > cnt
                                    - e.getRowBeginIndex()) {
                                e.setRowEndIndex(cnt - e.getRowBeginIndex());
                                if (e.getRowEndIndex() < 0) {
                                    e.setRowEndIndex(0);
                                }
                            }
                        }
                    }
                    long count = e.getRowEndIndex() - e.getRowBeginIndex();
                    // Read only rows that can fit to one PDU.
                    if (e.getRowEndIndex() - e.getRowBeginIndex() > e
                            .getRowToPdu()) {
                        count = e.getRowToPdu();
                    }
                    getProfileGenericDataByEntry(p, e.getRowBeginIndex(),
                            count);
                }
                continue;
            }

            System.out.println(
                    String.format("Client Read value from %s attribute: %d.",
                            e.getTarget().getName(), e.getIndex()));
            if ((e.getTarget().getUIDataType(e.getIndex()) == DataType.DATETIME
                    || e.getTarget()
                            .getDataType(e.getIndex()) == DataType.DATETIME)
                    && !(e.getTarget() instanceof GXDLMSClock)) {
                e.setValue(Calendar.getInstance().getTime());
                e.setHandled(true);
            } else if (e.getTarget() instanceof GXDLMSClock) {
                continue;
            } else if (e.getTarget() instanceof GXDLMSRegisterMonitor
                    && e.getIndex() == 2) {
                // Update Register Monitor Thresholds values.
                e.setValue(
                        new Object[] { new java.util.Random().nextInt(1000) });
                e.setHandled(true);
            } else {
                // If data is not assigned and value type is unknown return
                // number.
                Object[] values = e.getTarget().getValues();
                if (e.getIndex() <= values.length) {
                    if (values[e.getIndex() - 1] == null) {
                        DataType tp = e.getTarget().getDataType(e.getIndex());
                        if (tp == DataType.NONE || tp == DataType.INT8
                                || tp == DataType.INT16 || tp == DataType.INT32
                                || tp == DataType.INT64 || tp == DataType.UINT8
                                || tp == DataType.UINT16
                                || tp == DataType.UINT32
                                || tp == DataType.UINT64) {
                            e.setValue(new java.util.Random().nextInt(1000));
                            e.setHandled(true);
                        }
                        if (tp == DataType.STRING) {
                            e.setValue("Gurux");
                            e.setHandled(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPostRead(ValueEventArgs[] args) {

    }

    @Override
    public void onPreWrite(ValueEventArgs[] args) {
        for (ValueEventArgs e : args) {
            System.out.println(String.format(
                    "Client Write new value %1$s to object: %2$s.",
                    e.getValue(), e.getTarget().getName()));
        }
    }

    @Override
    public void onPostWrite(ValueEventArgs[] args) {
    }

    @Override
    public void onPreAction(ValueEventArgs[] args) {

    }

    private void capture(GXDLMSProfileGeneric pg) throws IOException {
        // Profile generic Capture is called.
        SimpleDateFormat df = new SimpleDateFormat();
        synchronized (fileLock) {
            FileWriter writer = new FileWriter(getDataFile(), true);
            try {
                StringBuilder sb = new StringBuilder();
                for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pg
                        .getCaptureObjects()) {
                    if (sb.length() != 0) {
                        sb.append(';');
                    }
                    // TODO: Read value here example from the meter if it's not
                    // updated automatically.
                    Object value = it.getKey()
                            .getValues()[it.getValue().getAttributeIndex() - 1];
                    if (value == null) {
                        // Generate random value here.
                        value = getProfileGenericDataCount() + 1;
                    }
                    if (value instanceof Date) {
                        sb.append(df.format((Date) value));
                    } else if (value instanceof GXDateTime) {
                        sb.append(df.format(((GXDateTime) value)
                                .getLocalCalendar().getTime()));
                    } else {
                        sb.append(value);
                    }
                }
                sb.append(System.lineSeparator());
                writer.write(sb.toString());
            } finally {
                writer.close();
            }
        }
    }

    private void handleProfileGenericActions(ValueEventArgs it)
            throws IOException {
        GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) it.getTarget();
        FileWriter writer = null;
        synchronized (fileLock) {
            try {
                if (it.getIndex() == 1) {
                    // Profile generic clear is called. Clear data.
                    writer = new FileWriter(getDataFile(), false);
                } else if (it.getIndex() == 2) {
                    capture(pg);
                }
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    @Override
    public void onPostAction(ValueEventArgs[] args) throws Exception {
        for (ValueEventArgs it : args) {
            if (it.getTarget() instanceof GXDLMSProfileGeneric) {
                handleProfileGenericActions(it);
            }
        }
    }

    @Override
    public void onError(Object sender, Exception ex) {
        if (Trace.ordinal() > TraceLevel.OFF.ordinal()) {
            System.out.println("Error has occurred:" + ex.getMessage());
        }
    }

    /*
     * Client has send data.
     */
    @Override
    public void onReceived(Object sender, ReceiveEventArgs e) {
        try {
            synchronized (this) {
                if (Trace == TraceLevel.VERBOSE) {
                    System.out.println("<- " + gurux.common.GXCommon
                            .bytesToHex((byte[]) e.getData()));
                }
                byte[] reply = handleRequest((byte[]) e.getData());
                // Reply is null if we do not want to send any data to the
                // client.
                // This is done if client try to make connection with wrong
                // server or client address.
                if (reply != null) {
                    if (Trace == TraceLevel.VERBOSE) {
                        System.out.println("-> "
                                + gurux.common.GXCommon.bytesToHex(reply));
                    }
                    media.send(reply, e.getSenderInfo());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void onMediaStateChange(Object sender, MediaStateEventArgs e) {

    }

    /*
     * Client has made connection.
     */
    @Override
    public void onClientConnected(Object sender,
            gurux.net.ConnectionEventArgs e) {
        // Reset server settings when connection is established.
        this.reset();
        System.out.println("Client Connected.");
    }

    /*
     * Client has close connection.
     */
    @Override
    public void onClientDisconnected(Object sender,
            gurux.net.ConnectionEventArgs e) {
        System.out.println("Client Disconnected.");
    }

    @Override
    public void onTrace(Object sender, TraceEventArgs e) {
        // System.out.println(e.toString());
    }

    @Override
    public void onPropertyChanged(Object sender, PropertyChangedEventArgs e) {

    }

    @Override
    public GXDLMSObject onFindObject(ObjectType objectType, int sn, String ln) {
        return null;
    }

    /**
     * Example server accepts all connections.
     * 
     * @param serverAddress
     *            Server address.
     * @param clientAddress
     *            Client address.
     * @return True.
     */
    @Override
    public final boolean isTarget(final int serverAddress,
            final int clientAddress) {
        return true;
    }

    @Override
    public final SourceDiagnostic onValidateAuthentication(
            final Authentication authentication, final byte[] password) {
        // Accept all passwords.
        // return SourceDiagnostic.NONE;
        // Uncomment checkPassword if you want to check password.
        return checkPassword(authentication, password);
    }

    SourceDiagnostic checkPassword(final Authentication authentication,
            final byte[] password) {
        if (authentication == Authentication.LOW) {

            byte[] expected;
            if (getUseLogicalNameReferencing()) {
                GXDLMSAssociationLogicalName ln =
                        (GXDLMSAssociationLogicalName) getItems().findByLN(
                                ObjectType.ASSOCIATION_LOGICAL_NAME,
                                "0.0.40.0.0.255");
                expected = ln.getSecret();
            } else {
                GXDLMSAssociationShortName sn =
                        (GXDLMSAssociationShortName) getItems().findByLN(
                                ObjectType.ASSOCIATION_SHORT_NAME,
                                "0.0.40.0.0.255");
                expected = sn.getSecret();
            }
            if (java.util.Arrays.equals(expected, password)) {
                return SourceDiagnostic.NONE;
            }
            String actual = "";
            if (password != null) {
                actual = new String(password);
            }
            System.out.println("Password does not match. Actual: '" + actual
                    + "' Expected: '" + new String(expected) + "'");
            return SourceDiagnostic.AUTHENTICATION_FAILURE;
        }
        // Other authentication levels are check on phase two.
        return SourceDiagnostic.NONE;

    }

    @Override
    protected AccessMode onGetAttributeAccess(final ValueEventArgs arg) {
        // Only read is allowed
        if (arg.getSettings().getAuthentication() == Authentication.NONE) {
            return AccessMode.READ;
        }
        // Only clock write is allowed.
        if (arg.getSettings().getAuthentication() == Authentication.LOW) {
            if (arg.getTarget() instanceof GXDLMSClock) {
                return AccessMode.READ_WRITE;
            }
            return AccessMode.READ;
        }
        // All writes are allowed.
        return AccessMode.READ_WRITE;
    }

    @Override
    protected MethodAccessMode onGetMethodAccess(final ValueEventArgs arg) {
        // Methods are not allowed.
        if (arg.getSettings().getAuthentication() == Authentication.NONE) {
            return MethodAccessMode.NO_ACCESS;
        }
        // Only clock methods are allowed.
        if (arg.getSettings().getAuthentication() == Authentication.LOW) {
            if (arg.getTarget() instanceof GXDLMSClock) {
                return MethodAccessMode.ACCESS;
            }
            return MethodAccessMode.NO_ACCESS;
        }
        return MethodAccessMode.ACCESS;
    }

    /**
     * DLMS client connection succeeded.
     */
    @Override
    protected void onConnected(GXDLMSConnectionEventArgs connectionInfo) {
    }

    /**
     * DLMS client connection failed.
     */
    @Override
    protected void
            onInvalidConnection(GXDLMSConnectionEventArgs connectionInfo) {

    }

    /**
     * DLMS client connection closed.
     */

    @Override
    protected void onDisconnected(GXDLMSConnectionEventArgs connectionInfo) {

    }

    /**
     * Schedule or profile generic asks current value.
     * 
     * @throws IOException
     */
    @Override
    public void onPreGet(ValueEventArgs[] args) throws IOException {
        for (ValueEventArgs it : args) {
            if (it.getTarget() instanceof GXDLMSProfileGeneric) {
                GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) it.getTarget();
                capture(pg);
            }
        }
    }

    /**
     * Schedule or profile generic asks current value.
     */
    @Override
    public void onPostGet(ValueEventArgs[] e) {

    }

}