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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gurux.common.GXCmdParameter;
import gurux.common.GXCommon;
import gurux.common.IGXMedia;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.KeyAgreementScheme;
import gurux.dlms.enums.Security;
import gurux.dlms.enums.Standard;
import gurux.dlms.objects.enums.SecuritySuite;
import gurux.io.BaudRate;
import gurux.io.Parity;
import gurux.io.StopBits;
import gurux.net.GXNet;
import gurux.serial.GXSerial;

public class Settings {
	public IGXMedia media = null;
	public TraceLevel trace = TraceLevel.INFO;
	public GXDLMSSecureClient2 client = new GXDLMSSecureClient2(true);
	String invocationCounter;
	// Objects to read.
	public List<Map.Entry<String, Integer>> readObjects = new ArrayList<Map.Entry<String, Integer>>();

	// Cache file.
	String outputFile;
	// Client and server certificates are exported from the meter.
	public String exportSecuritySetupLN = null;

	// Generate new client and server certificates and import them to the server.
	public String generateSecuritySetupLN = null;

	/**
	 * Show help.
	 */
	static void showHelp() {
		System.out.println("GuruxDlmsSample reads data from the DLMS/COSEM device.");
		System.out.println("GuruxDlmsSample -h [Meter IP Address] -p [Meter Port No] -c 16 -s 1 -r sn");
		System.out.println(" -h \t host name or IP address.");
		System.out.println(" -p \t port number or name (Example: 1000).");
		System.out.println(" -S \t serial port.");
		System.out.println(" -a \t Authentication (None, Low, High, HighMd5, HighSha1, HighGMac, HighSha256).");
		System.out.println(" -P \t Password for authentication.");
		System.out.println(" -c \t Client address. (Default: 16)");
		System.out.println(" -s \t Server address. (Default: 1)");
		System.out.println(" -n \t Server address as serial number.");
		System.out.println(" -l \t Logical Server address.");
		System.out.println(" -r [sn, ln]\t Short name or Logical Name (default) referencing is used.");
		System.out.println(" -t [Error, Warning, Info, Verbose] Trace messages.");
		System.out
				.println(" -g \"0.0.1.0.0.255:1; 0.0.1.0.0.255:2\" Get selected object(s) with given attribute index.");
		System.out.println(
				" -C \t Security Level. (None, Authentication, Encrypted, AuthenticationEncryption or DigitallySigned)");
		System.out.println(" -V \t Security Suite version. (Default: Suite0). (Suite0, Suite1 or Suite2)");
		System.out.println(" -K \t Used Key agreement scheme (OnePassDiffieHellman or StaticUnifiedModel).");
		System.out.println(" -v \t Invocation counter data object Logical Name. Ex. 0.0.43.1.1.255");
		System.out.println(" -I \t Auto increase invoke ID");
		System.out.println(" -o \t Cache association view to make reading faster. Ex. -o C:\\device.xml");
		System.out.println(" -T \t System title that is used with chiphering. Ex -T 4775727578313233");
		System.out.println(
				" -A \t Authentication key that is used with chiphering. Ex -A D0D1D2D3D4D5D6D7D8D9DADBDCDDDEDF");
		System.out.println(
				" -B \t Block cipher key that is used with chiphering. Ex -B 000102030405060708090A0B0C0D0E0F");
		System.out.println(" -D \t Dedicated key that is used with chiphering. Ex -D 00112233445566778899AABBCCDDEEFF");
		System.out.println(" -d \t Used DLMS standard. Ex -d India (DLMS, India, Italy, SaudiArabia, IDIS)");
		System.out.println(" -E \t Export client and server certificates from the meter. Ex -E 0.0.43.0.0.255. ");
		System.out.println(
				" -R \t Generate new client and server certificates and import them to the server. Ex. -R 0.0.43.0.0.255.");
		System.out.println(" -i \t Used communication interface. Ex. -i WRAPPER.");
		System.out.println(" -m \t Used PLC MAC address. Ex. -m 1.");
		System.out.println("Example:");
		System.out.println("Read LG device using TCP/IP connection.");
		System.out.println("GuruxDlmsSample -r sn -c 16 -s 1 -h [Meter IP Address] -p [Meter Port No]");
		System.out.println("Read LG device using serial port connection.");
		System.out.println("GuruxDlmsSample -r sn -c 16 -s 1 -S COM1 -i");
		System.out.println("Read Indian device using serial port connection.");
		System.out.println("GuruxDlmsSample -S COM1 -c 16 -s 1 -a Low -P [password]");

	}

	static int getParameters(String[] args, Settings settings) throws IOException {
		List<GXCmdParameter> parameters = GXCommon.getParameters(args,
				"h:p:c:s:r:i:It:a:pP:g:S:n:C:v:o:T:A:B:D:d:l:E:V:M:K:N:");
		GXNet net = null;
		// Has user give the custom serial port settings or are the default
		// values used in mode E.
		boolean modeEDefaultValues = true;
		for (GXCmdParameter it : parameters) {
			switch (it.getTag()) {
			case 'r':
				if ("sn".compareTo(it.getValue()) == 0) {
					settings.client.setUseLogicalNameReferencing(false);
				} else if ("ln".compareTo(it.getValue()) == 0) {
					settings.client.setUseLogicalNameReferencing(true);
				} else {
					throw new IllegalArgumentException("Invalid reference option.");
				}
				break;
			case 'h':
				// Host address.
				if (settings.media == null) {
					settings.media = new GXNet();
				}
				net = (GXNet) settings.media;
				net.setHostName(it.getValue());
				break;
			case 't':
				// Trace.
				if ("Error".compareTo(it.getValue()) == 0)
					settings.trace = TraceLevel.ERROR;
				else if ("Warning".compareTo(it.getValue()) == 0)
					settings.trace = TraceLevel.WARNING;
				else if ("Info".compareTo(it.getValue()) == 0)
					settings.trace = TraceLevel.INFO;
				else if ("Verbose".compareTo(it.getValue()) == 0)
					settings.trace = TraceLevel.VERBOSE;
				else if ("Off".compareTo(it.getValue()) == 0)
					settings.trace = TraceLevel.OFF;
				else
					throw new IllegalArgumentException("Invalid Authentication option '" + it.getValue()
							+ "'. (Error, Warning, Info, Verbose, Off).");
				break;
			case 'p':
				// Port.
				if (settings.media == null) {
					settings.media = new GXNet();
				}
				net = (GXNet) settings.media;
				net.setPort(Integer.parseInt(it.getValue()));
				break;
			case 'P':// Password
				settings.client.setPassword(it.getValue().getBytes());
				break;
			case 'i':// Interface type.
				if ("HDLC".equalsIgnoreCase(it.getValue()))
					settings.client.setInterfaceType(InterfaceType.HDLC);
				else if ("WRAPPER".equalsIgnoreCase(it.getValue()))
					settings.client.setInterfaceType(InterfaceType.WRAPPER);
				else if ("HdlcWithModeE".equalsIgnoreCase(it.getValue()))
					settings.client.setInterfaceType(InterfaceType.HDLC_WITH_MODE_E);
				else if ("Plc".equalsIgnoreCase(it.getValue()))
					settings.client.setInterfaceType(InterfaceType.PLC);
				else if ("PlcHdlc".equalsIgnoreCase(it.getValue()))
					settings.client.setInterfaceType(InterfaceType.PLC_HDLC);
				else
					throw new IllegalArgumentException("Invalid interface type option." + it.getValue()
							+ " (HDLC, WRAPPER, HdlcWithModeE, Plc, PlcHdlc)");

				if (modeEDefaultValues && settings.client.getInterfaceType() == InterfaceType.HDLC_WITH_MODE_E) {
					GXSerial serial = (GXSerial) settings.media;
					serial.setBaudRate(BaudRate.BAUD_RATE_300);
					serial.setDataBits(7);
					serial.setParity(Parity.EVEN);
					serial.setStopBits(StopBits.ONE);
				}
				settings.client.getPlc().reset();
				break;
			case 'I':
				// AutoIncreaseInvokeID.
				settings.client.setAutoIncreaseInvokeID(true);
				break;
			case 'C':
				settings.client.getCiphering().getSecurityPolicy().clear();
				if ("None".compareTo(it.getValue()) == 0) {
				} else if ("Authentication".compareTo(it.getValue()) == 0) {
					settings.client.getCiphering().setSecurity(Security.AUTHENTICATION);
				} else if ("Encryption".compareTo(it.getValue()) == 0) {
					settings.client.getCiphering().setSecurity(Security.ENCRYPTION);
				} else if ("AuthenticationEncryption".compareTo(it.getValue()) == 0) {
					settings.client.getCiphering().setSecurity(Security.AUTHENTICATION_ENCRYPTION);
				} else if ("DigitallySigned".compareTo(it.getValue()) == 0) {
					settings.client.getCiphering().setSecurity(Security.DIGITALLY_SIGNED);
				} else {
					throw new IllegalArgumentException("Invalid Ciphering option '" + it.getValue()
							+ "'. (None, Authentication, Encryption, AuthenticationEncryption)");
				}
				break;
			case 'V':
				if ("None".compareTo(it.getValue()) == 0) {
				} else if ("Suite0".compareTo(it.getValue()) == 0) {
					settings.client.getCiphering().setSecuritySuite(SecuritySuite.SUITE_0);
				} else if ("Suite1".compareTo(it.getValue()) == 0) {
					settings.client.getCiphering().setSecuritySuite(SecuritySuite.SUITE_1);
				} else if ("Suite2".compareTo(it.getValue()) == 0) {
					// Java doesn't support ECDSA 384 at the moment.
					throw new IllegalArgumentException(
							"Invalid Ciphering option '" + it.getValue() + "'. (Suite0, Suite1)");
				} else {
					throw new IllegalArgumentException(
							"Invalid Ciphering option '" + it.getValue() + "'. (Suite0, Suite1)");
				}
				break;
			case 'K':
				if ("OnePassDiffieHellman".compareTo(it.getValue()) == 0) {
					settings.client.getCiphering().setKeyAgreementScheme(KeyAgreementScheme.ONE_PASS_DIFFIE_HELLMAN);
				} else if ("StaticUnifiedModel".compareTo(it.getValue()) == 0) {
					settings.client.getCiphering().setKeyAgreementScheme(KeyAgreementScheme.STATIC_UNIFIED_MODEL);
				} else {
					throw new IllegalArgumentException("Invalid Ciphering option '" + it.getValue()
							+ "'. (OnePassDiffieHellman, StaticUnifiedModel)");
				}
				break;
			case 'T':
				settings.client.getCiphering().setSystemTitle(GXCommon.hexToBytes(it.getValue()));
				break;
			case 'M':
				settings.client.setServerSystemTitle(GXCommon.hexToBytes(it.getValue()));
				break;
			case 'A':
				settings.client.getCiphering().setAuthenticationKey(GXCommon.hexToBytes(it.getValue()));
				break;
			case 'B':
				settings.client.getCiphering().setBlockCipherKey(GXCommon.hexToBytes(it.getValue()));
				break;
			case 'D':
				settings.client.getCiphering().setDedicatedKey(GXCommon.hexToBytes(it.getValue()));
				break;
			case 'N':
				settings.generateSecuritySetupLN = it.getValue().trim();
				break;
			case 'E':
				settings.exportSecuritySetupLN = it.getValue().trim();
				break;
			case 'o':
				settings.outputFile = it.getValue();
				break;
			case 'd':
				try {
					settings.client.setStandard(Standard.valueOfString(it.getValue()));
					if (settings.client.getStandard() == Standard.ITALY
							|| settings.client.getStandard() == Standard.INDIA
							|| settings.client.getStandard() == Standard.SAUDI_ARABIA) {
						settings.client.setUseUtc2NormalTime(true);
					}
				} catch (Exception e) {
					throw new IllegalArgumentException("Invalid DLMS standard option: '" + it.getValue()
							+ "'. (DLMS, India, Italy, SaudiArabia, IDIS)");
				}
				break;
			case 'v':
				settings.invocationCounter = it.getValue();
				// TODO:
				// GXDLMSObject.ValidateLogicalName(settings.invocationCounter);
				break;
			case 'g':
				// Get (read) selected objects.
				for (String o : it.getValue().split("[;,]")) {
					String[] tmp = o.split("[:]");
					if (tmp.length != 2) {
						throw new IllegalArgumentException("Invalid Logical name or attribute index.");
					}
					settings.readObjects
							.add(new GXSimpleEntry<String, Integer>(tmp[0].trim(), Integer.parseInt(tmp[1].trim())));
				}
				break;
			case 'S':// Serial Port
				settings.media = new GXSerial();
				GXSerial serial = (GXSerial) settings.media;
				String[] tmp = it.getValue().split("[:]");
				serial.setPortName(tmp[0]);
				if (tmp.length > 1) {
					modeEDefaultValues = false;
					serial.setBaudRate(BaudRate.forValue(Integer.parseInt(tmp[1])));
					serial.setDataBits(Integer.parseInt(tmp[2].substring(0, 1)));
					String parity = tmp[2].substring(1, tmp[2].length() - 1);
					if ("NONE".equalsIgnoreCase(parity)) {
						serial.setParity(Parity.NONE);
					} else if ("ODD".equalsIgnoreCase(parity)) {
						serial.setParity(Parity.ODD);
					} else if ("EVEN".equalsIgnoreCase(parity)) {
						serial.setParity(Parity.EVEN);
					} else if ("MARK".equalsIgnoreCase(parity)) {
						serial.setParity(Parity.MARK);
					} else if ("SPACE".equalsIgnoreCase(parity)) {
						serial.setParity(Parity.SPACE);
					}
					serial.setStopBits(StopBits.values()[Integer.parseInt(tmp[2].substring(tmp[2].length() - 1)) - 1]);

				} else {
					if (settings.client.getInterfaceType() == InterfaceType.HDLC_WITH_MODE_E) {
						serial.setBaudRate(BaudRate.BAUD_RATE_300);
						serial.setDataBits(7);
						serial.setParity(Parity.EVEN);
						serial.setStopBits(StopBits.ONE);
					} else {
						serial.setBaudRate(BaudRate.BAUD_RATE_9600);
						serial.setDataBits(8);
						serial.setParity(Parity.NONE);
						serial.setStopBits(StopBits.ONE);
					}
				}
				break;
			case 'a':
				try {
					settings.client.setAuthentication(Authentication.valueOfString(it.getValue()));
				} catch (Exception e) {
					throw new IllegalArgumentException("Invalid Authentication option: '" + it.getValue()
							+ "'. (None, Low, High, HighMd5, HighSha1, HighGmac, HighSha256, HighECDSA)");
				}
				break;
			case 'c':
				settings.client.setClientAddress(Integer.parseInt(it.getValue()));
				break;
			case 's':
				if (settings.client.getServerAddress() != 1) {
					settings.client.setServerAddress(GXDLMSClient.getServerAddress(settings.client.getServerAddress(),
							Integer.parseInt(it.getValue())));
				} else {
					settings.client.setServerAddress(Integer.parseInt(it.getValue()));
				}
				break;
			case 'l':
				settings.client.setServerAddress(GXDLMSClient.getServerAddress(Integer.parseInt(it.getValue()),
						settings.client.getServerAddress()));
				break;
			case 'n':
				settings.client.setServerAddress(GXDLMSClient.getServerAddress(Integer.parseInt(it.getValue())));
				break;
			case '?':
				switch (it.getTag()) {
				case 'c':
					throw new IllegalArgumentException("Missing mandatory client option.");
				case 's':
					throw new IllegalArgumentException("Missing mandatory server option.");
				case 'h':
					throw new IllegalArgumentException("Missing mandatory host name option.");
				case 'p':
					throw new IllegalArgumentException("Missing mandatory port option.");
				case 'r':
					throw new IllegalArgumentException("Missing mandatory reference option.");
				case 'a':
					throw new IllegalArgumentException("Missing mandatory authentication option.");
				case 'S':
					throw new IllegalArgumentException("Missing mandatory Serial port option.\n");
				case 't':
					throw new IllegalArgumentException("Missing mandatory trace option.\n");
				case 'g':
					throw new IllegalArgumentException("Missing mandatory OBIS code option.");
				case 'C':
					throw new IllegalArgumentException("Missing mandatory Ciphering option.");
				case 'v':
					throw new IllegalArgumentException("Missing mandatory invocation counter logical name option.");
				case 'T':
					throw new IllegalArgumentException("Missing mandatory system title option.");
				case 'A':
					throw new IllegalArgumentException("Missing mandatory authentication key option.");
				case 'B':
					throw new IllegalArgumentException("Missing mandatory block cipher key option.");
				case 'D':
					throw new IllegalArgumentException("Missing mandatory dedicated key option.");
				case 'd':
					throw new IllegalArgumentException("Missing mandatory DLMS standard option.");
				case 'K':
					throw new IllegalArgumentException("Missing mandatory key agreement scheme option.");
				case 'l':
					throw new IllegalArgumentException("Missing mandatory logical server address option.");
				case 'i':
					throw new IllegalArgumentException("Missing mandatory interface type option.");
				case 'R':
					throw new IllegalArgumentException("Missing mandatory logical name of security setup object.");
				default:
					showHelp();
					return 1;
				}
			default:
				showHelp();
				return 1;
			}
		}
		if (settings.media == null)

		{
			showHelp();
			return 1;
		}
		return 0;
	}
}