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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

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
import gurux.dlms.GXTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.enums.Unit;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSCaptureObject;
import gurux.dlms.objects.GXDLMSClock;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.GXDLMSProfileGeneric;
import gurux.dlms.objects.GXDLMSRegister;
import gurux.dlms.objects.GXXmlWriterSettings;
import gurux.dlms.secure.GXDLMSSecureServer2;
import gurux.net.GXNet;
import gurux.net.enums.NetworkType;

/**
 * All example servers are using same objects.
 */
public class GXDLMSBase extends GXDLMSSecureServer2
    implements IGXMediaListener, gurux.net.IGXNetListener {
  Object settingsLock = new Object();
  GXDLMSRegister temperature;
  GXBatteryUseTimeCounter batteryUseTimeCounter;
  boolean Trace = false;
  private GXNet media;

  /**
   * Constructor.
   * 
   * @param ln
   *          Association logical name.
   * @param type
   *          Interface type.
   */
  public GXDLMSBase(final GXDLMSAssociationLogicalName ln,
      final InterfaceType interfaceType) {
    super(ln, interfaceType);
    this.setMaxReceivePDUSize(1024);
    byte[] secret = "Gurux".getBytes();
    ln.setSecret(secret);
  }

  /**
   * Constructor.
   * 
   * @param sn
   *          Association short name.
   * @param type
   *          Interface type.
   */
  public GXDLMSBase(final GXDLMSAssociationShortName sn,
      final InterfaceType interfaceType) {
    super(sn, interfaceType);
    this.setMaxReceivePDUSize(1024);
    byte[] secret = "Gurux".getBytes();
    sn.setSecret(secret);
  }

  /**
   * Add available objects.
   * 
   * @param server
   */
  public final void initialize(final int port) throws Exception {
    media = new gurux.net.GXNet(NetworkType.TCP, port);
    media.setTrace(TraceLevel.VERBOSE);
    media.addListener(this);
    media.open();
    ///////////////////////////////////////////////////////////////////////
    // Add objects of the meter.
    temperature = (GXDLMSRegister) getItems().findByLN(ObjectType.REGISTER,
        "0.0.96.9.0.255");
    if (temperature == null) {
      // CPU temperature.
      temperature = new GXDLMSRegister("0.0.96.9.0.255");
      temperature.setScaler(1);
      temperature.setUnit(Unit.TEMPERATURE);
      temperature.setValue(100);
      // Temperature is send using signed byte.
      temperature.setDataType(2, DataType.INT8);
      getItems().add(temperature);
    }
    // Battery use time counter
    GXDLMSRegister r = (GXDLMSRegister) getItems().findByLN(ObjectType.REGISTER,
        "0.0.96.6.0.255");
    if (r == null) {
      r = new GXDLMSRegister("0.0.96.6.0.255");
      // Battery use time counter is send using UInt16.
      r.setDataType(2, DataType.UINT16);
      getItems().add(r);
    }
    batteryUseTimeCounter = new GXBatteryUseTimeCounter(r);
    batteryUseTimeCounter.start();
    ///////////////////////////////////////////////////////////////////////
    // Server must initialize after all objects are added.
    super.initialize();
  }

  @Override
  public final void close() throws Exception {
    super.close();
    if (media != null) {
      media.close();
      media = null;
    }
    if (batteryUseTimeCounter != null) {
      batteryUseTimeCounter.setClosing(true);
      batteryUseTimeCounter.join();
      batteryUseTimeCounter = null;
    }
  }

  /**
   * Each server has own history file.
   * 
   * @param target
   *          Target Profile generic.
   * @return Name of the file.
   */
  private String getProfileGenericName(final GXDLMSObject target) {
    String name =
        ((GXNet) media).getPort() + "_" + target.getLogicalName() + ".csv";
    return name;
  }

  /**
   * Return data using start and end indexes.
   * 
   * @param p
   *          ProfileGeneric
   * @param index
   *          Row index.
   * @param count
   *          Row count.
   */
  private void getProfileGenericDataByEntry(final GXDLMSProfileGeneric p,
      final long index, final long count) {
    String name = getProfileGenericName(p);
    if (!new File(name).exists()) {
      return;
    }
    long index2 = index;
    // Clear old data. It's already serialized.
    p.clearBuffer();
    BufferedReader reader = null;
    synchronized (p) {
      try {
        reader = new BufferedReader(new FileReader(name));
        String line;
        while ((line = reader.readLine()) != null) {
          // Skip row
          if (index2 > 0) {
            --index2;
          } else if (line.length() != 0) {
            String[] values = line.split("[;]", -1);
            Object[] list = new Object[values.length];
            for (int pos = 0; pos != values.length; ++pos) {
              DataType t = p.getCaptureObjects().get(pos).getKey()
                  .getUIDataType(p.getCaptureObjects().get(pos).getValue()
                      .getAttributeIndex());
              if (t == DataType.DATETIME) {
                list[pos] = new GXDateTime(values[pos]);
              } else if (t == DataType.DATE) {
                list[pos] = new GXDate(values[pos]);
              } else if (t == DataType.TIME) {
                list[pos] = new GXTime(values[pos]);
              } else {
                list[pos] = values[pos];
              }
            }
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
   * @param p
   *          Profile generic.
   * @param e
   *          Value arguments.
   */
  private void getProfileGenericDataByRange(final GXDLMSProfileGeneric p,
      final ValueEventArgs e) {
    String name = getProfileGenericName(p);
    if (!new File(name).exists()) {
      return;
    }
    GXDateTime start = (GXDateTime) GXDLMSClient.changeType(
        (byte[]) ((Object[]) e.getParameters())[1], DataType.DATETIME);
    GXDateTime end = (GXDateTime) GXDLMSClient.changeType(
        (byte[]) ((Object[]) e.getParameters())[2], DataType.DATETIME);
    synchronized (p) {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new FileReader(name));
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
   * @param p
   *          Profile generic.
   * @return Row count.
   */
  private int getProfileGenericDataCount(final GXDLMSProfileGeneric p) {
    String name = getProfileGenericName(p);
    if (!new File(name).exists()) {
      return 0;
    }
    int rows = 0;
    BufferedReader reader = null;
    synchronized (p) {
      try {
        reader = new BufferedReader(new FileReader(name));
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
  public final void onPreRead(final ValueEventArgs[] args) {
    for (ValueEventArgs e : args) {
      // If user wants to know CPU temperature.
      if (e.getTarget() == temperature && e.getIndex() == 2) {
        updateTemperature();
      }
      // Framework will handle profile generic automatically.
      else if (e.getTarget() instanceof GXDLMSProfileGeneric) {
        // If buffer is read and we want to save memory.
        if (e.getIndex() == 6) {
          // If client wants to know EntriesInUse.
          GXDLMSProfileGeneric p = (GXDLMSProfileGeneric) e.getTarget();
          p.setEntriesInUse(getProfileGenericDataCount(p));
        }
        if (e.getIndex() == 2) {
          // Client reads buffer.
          GXDLMSProfileGeneric p = (GXDLMSProfileGeneric) e.getTarget();
          // Read rows from file.
          // If reading first time.
          if (e.getRowEndIndex() == 0) {
            if (e.getSelector() == 0) {
              e.setRowEndIndex(getProfileGenericDataCount(p));
            } else if (e.getSelector() == 1) {
              // Read by entry.
              getProfileGenericDataByRange(p, e);
            } else if (e.getSelector() == 2) {
              // Read by range.
              e.setRowBeginIndex(((long) ((Object[]) e.getParameters())[0]));
              e.setRowEndIndex(e.getRowBeginIndex()
                  + (long) ((Object[]) e.getParameters())[1]);
              // If client wants to read more data what we have.
              int cnt = getProfileGenericDataCount(p);
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
          if (e.getRowEndIndex() - e.getRowBeginIndex() > e.getRowToPdu()) {
            count = e.getRowToPdu();
          }
          getProfileGenericDataByEntry(p, e.getRowBeginIndex(), count);
        }
        continue;
      }
      System.out
          .println(String.format("Client Read value from %s attribute: %d.",
              e.getTarget().getName(), e.getIndex()));
    }
  }

  @Override
  public final void onPostRead(final ValueEventArgs[] args) {

  }

  @Override
  public final void onPreWrite(final ValueEventArgs[] args) {
    for (ValueEventArgs e : args) {
      System.out
          .println(String.format("Client Write new value %1$s to object: %2$s.",
              e.getValue(), e.getTarget().getName()));
    }
  }

  /**
   * Save COSEM objects to XML.
   * 
   * @param path
   *          File path.
   * @throws IOException
   * @throws XMLStreamException
   */
  private void saveObjects(final String path)
      throws XMLStreamException, IOException {
    synchronized (settingsLock) {
      GXXmlWriterSettings settings = new GXXmlWriterSettings();
      getItems().save(path, settings);
    }
  }

  /**
   * Load saved COSEM objects from XML.
   * 
   * @param path
   *          File path.
   * @throws IOException
   *           IOException
   * @throws XMLStreamException
   *           XMLStreamException
   */
  private void loadObjects(final String path)
      throws XMLStreamException, IOException {
    synchronized (settingsLock) {
      if (new File(path).exists()) {
        GXDLMSObjectCollection objects = GXDLMSObjectCollection.load(path);
        getItems().clear();
        getItems().addAll(objects);
      }
    }
  }

  /**
   * Load meter settings.
   * 
   * @throws XMLStreamException
   *           XMLStreamException
   * @throws IOException
   *           IOException
   */
  private void loadSettings() throws XMLStreamException, IOException {
    if (getUseLogicalNameReferencing()) {
      loadObjects("AssociationLogicalName.xml");
    } else {
      loadObjects("AssociationShortName.xml");
    }
  }

  /**
   * Save meter settings.
   * 
   * @throws IOException
   *           IO exception.
   * @throws XMLStreamException
   *           XML exception.
   */
  private void saveSettings() throws XMLStreamException, IOException {
    if (getUseLogicalNameReferencing()) {
      saveObjects("AssociationLogicalName.xml");
    } else {
      saveObjects("AssociationShortName.xml");
    }
  }

  /**
   * Update association view.
   * 
   * @param collection
   *          COSEM objects.
   * @throws IOException
   * @throws XMLStreamException
   */
  private void updateAssociationView(final GXDLMSObjectCollection collection)
      throws XMLStreamException, IOException {
    // Add new items.
    boolean newItems = false;
    for (GXDLMSObject it : collection) {
      if (!getItems().contains(it)) {
        getItems().add(it);
        newItems = true;
      }
    }
    // Remove old items.
    List<GXDLMSObject> removed = new ArrayList<GXDLMSObject>();
    for (GXDLMSObject it : getItems()) {
      if (!collection.contains(it)) {
        removed.add(it);
      }
    }
    for (GXDLMSObject it : removed) {
      getItems().remove(it);
    }
    // Update short names if new items are added.
    if (newItems && !getUseLogicalNameReferencing()) {
      updateShortNames();
    }

    // Save added objects.
    saveSettings();
  }

  @Override
  public final void onPostWrite(final ValueEventArgs[] args) throws Exception {
    for (ValueEventArgs e : args) {
      // If association is updated.
      if (e.getTarget() instanceof GXDLMSAssociationLogicalName
          && e.getIndex() == 2) {
        // Update new items to object list.
        GXDLMSAssociationLogicalName a =
            (GXDLMSAssociationLogicalName) e.getTarget();
        updateAssociationView(a.getObjectList());
      } else if (e.getTarget() instanceof GXDLMSAssociationShortName
          && e.getIndex() == 2) {
        // If association is updated.
        // Update new items to object list.
        GXDLMSAssociationShortName a =
            (GXDLMSAssociationShortName) e.getTarget();
        updateAssociationView(a.getObjectList());
      } else {
        saveSettings();
      }
    }
  }

  @Override
  public final void onPreAction(final ValueEventArgs[] args) throws Exception {

  }

  private void handleProfileGenericActions(final ValueEventArgs it)
      throws IOException {
    GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) it.getTarget();
    FileWriter writer = null;
    synchronized (pg) {
      if (it.getIndex() == 1) {
        // Profile generic clear is called. Clear data.
        String name = getProfileGenericName(pg);
        try {
          writer = new FileWriter(name, false);
        } finally {
          writer.close();
        }
      } else if (it.getIndex() == 2) {
      }
    }
  }

  @Override
  public final void onPostAction(final ValueEventArgs[] args) throws Exception {
    for (ValueEventArgs it : args) {
      if (it.getTarget() instanceof GXDLMSProfileGeneric) {
        handleProfileGenericActions(it);
      }
    }
  }

  @Override
  public final void onError(final Object sender, final Exception ex) {
    System.out.println("Error has occurred:" + ex.getMessage());
  }

  /*
   * Client has send data.
   */
  @Override
  public final void onReceived(final Object sender, final ReceiveEventArgs e) {
    try {
      synchronized (this) {
        if (Trace) {
          System.out.println(
              "<- " + gurux.common.GXCommon.bytesToHex((byte[]) e.getData()));
        }
        byte[] reply = handleRequest((byte[]) e.getData());
        // Reply is null if we do not want to send any data to the
        // client.
        // This is done if client try to make connection with wrong
        // server or client address.
        if (reply != null) {
          if (Trace) {
            System.out.println("-> " + gurux.common.GXCommon.bytesToHex(reply));
          }
          media.send(reply, e.getSenderInfo());
        }
      }
    } catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }

  @Override
  public final void onMediaStateChange(final Object sender,
      final MediaStateEventArgs e) {

  }

  /*
   * Client has made connection.
   */
  @Override
  public final void onClientConnected(final Object sender,
      final gurux.net.ConnectionEventArgs e) {
    System.out.println("Client Connected.");
  }

  /*
   * Client has close connection.
   */
  @Override
  public final void onClientDisconnected(final Object sender,
      final gurux.net.ConnectionEventArgs e) {
    // Reset server settings when connection closed.
    this.reset();
    System.out.println("Client Disconnected.");
  }

  @Override
  public final void onTrace(final Object sender, final TraceEventArgs e) {
    // System.out.println(e.toString());
  }

  @Override
  public final void onPropertyChanged(final Object sender,
      final PropertyChangedEventArgs e) {

  }

  @Override
  public final GXDLMSObject onFindObject(final ObjectType objectType,
      final int sn, final String ln) {
    return null;
  }

  /**
   * Example server accepts all connections.
   * 
   * @param serverAddress
   *          Server address.
   * @param clientAddress
   *          Client address.
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

  /**
   * Check password.
   * 
   * @param authentication
   *          Used authentication level.
   * @param password
   *          received password.
   * @return Is password correct.
   */
  private SourceDiagnostic checkPassword(final Authentication authentication,
      final byte[] password) {
    if (authentication == Authentication.LOW) {

      byte[] expected;
      if (getUseLogicalNameReferencing()) {
        GXDLMSAssociationLogicalName ln =
            (GXDLMSAssociationLogicalName) getItems().findByLN(
                ObjectType.ASSOCIATION_LOGICAL_NAME, "0.0.40.0.0.255");
        expected = ln.getSecret();
      } else {
        GXDLMSAssociationShortName sn = (GXDLMSAssociationShortName) getItems()
            .findByLN(ObjectType.ASSOCIATION_SHORT_NAME, "0.0.40.0.0.255");
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
  protected final AccessMode onGetAttributeAccess(final ValueEventArgs arg) {
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
  protected final MethodAccessMode onGetMethodAccess(final ValueEventArgs arg) {
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
  protected final void
      onConnected(final GXDLMSConnectionEventArgs connectionInfo) {
  }

  /**
   * DLMS client connection failed.
   */
  @Override
  protected final void
      onInvalidConnection(final GXDLMSConnectionEventArgs connectionInfo) {

  }

  /**
   * DLMS client connection closed.
   */

  @Override
  protected final void
      onDisconnected(final GXDLMSConnectionEventArgs connectionInfo) {

  }

  /**
   * Get temperature. This value is retrieve only when needed.
   */
  private void updateTemperature() {
    // Random value is used here because Windows is not supporting this very
    // well.
    temperature.setValue(new java.util.Random().nextInt(45 + 25) - 40);
  }

  /**
   * Remove rows from the file.
   * 
   * @param name
   *          File name.
   * @param count
   *          Amount of removed rows.
   * @throws IOException
   *           IOException
   */
  private void removeRows(final String name, final int count)
      throws IOException {
    List<String> rows = null;
    rows = Files.readAllLines(Paths.get(name));
    int pos = 0;
    while (pos < count) {
      rows.remove(0);
      ++pos;
    }
    try (FileWriter fw = new FileWriter(name)) {
      for (String it : rows) {
        fw.write(it);
        fw.write("\r");
      }
      fw.close();
    }
  }

  /**
   * Schedule or profile generic asks current value.
   */
  @Override
  public final void onPreGet(final ValueEventArgs[] args) {
    for (ValueEventArgs e : args) {
      if (e.getTarget() instanceof GXDLMSProfileGeneric) {
        // We want to save values to the file right a way.
        GXDLMSProfileGeneric pg = (GXDLMSProfileGeneric) e.getTarget();
        // Get entries in use if not know yet.
        if (pg.getEntriesInUse() == 0) {
          pg.setEntriesInUse(getProfileGenericDataCount(pg));
        }
        String name = getProfileGenericName(pg);
        Object[] values = new Object[pg.getCaptureObjects().size()];
        int pos = 0;
        synchronized (pg) {
          for (Entry<GXDLMSObject, GXDLMSCaptureObject> it : pg
              .getCaptureObjects()) {
            if (it.getKey() instanceof GXDLMSClock
                && it.getValue().getAttributeIndex() == 2) {
              GXDLMSClock c = (GXDLMSClock) it.getKey();
              c.setTime(c.now());
            } else if (it.getKey() == temperature
                && it.getValue().getAttributeIndex() == 2) {
              // Get CPU temperature.
              updateTemperature();
            }
            values[pos] =
                it.getKey().getValues()[it.getValue().getAttributeIndex() - 1];
            ++pos;
          }
          pg.setEntriesInUse(pg.getEntriesInUse() + 1);
          // Remove first row if maximum row count is received.
          if (pg.getProfileEntries() != 0
              && pg.getEntriesInUse() >= pg.getProfileEntries()) {
            try {
              removeRows(name, pg.getEntriesInUse() - pg.getProfileEntries());
            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
            pg.setEntriesInUse(pg.getProfileEntries());
          }
          SimpleDateFormat df = new SimpleDateFormat();
          try (FileWriter writer = new FileWriter(name, true)) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c != values.length; ++c) {
              if (c != 0) {
                sb.append(';');
              }
              Object col = values[c];
              if (col instanceof Date) {
                sb.append(df.format((Date) col));
              } else {
                sb.append(String.valueOf(col));
              }
            }
            sb.append("\n");
            writer.write(sb.toString());
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
        e.setHandled(true);
      }
    }
  }

  /**
   * Schedule or profile generic asks current value.
   */
  @Override
  public final void onPostGet(final ValueEventArgs[] e) {

  }

}