package gurux.dlms;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.AccessMode3;
import gurux.dlms.enums.AcseServiceProvider;
import gurux.dlms.enums.AssociationResult;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.Command;
import gurux.dlms.enums.Conformance;
import gurux.dlms.enums.DateTimeSkips;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ExceptionServiceError;
import gurux.dlms.enums.ExceptionStateError;
import gurux.dlms.enums.Initiate;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.MethodAccessMode3;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.enums.Priority;
import gurux.dlms.enums.RequestTypes;
import gurux.dlms.enums.ServiceClass;
import gurux.dlms.enums.SourceDiagnostic;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.manufacturersettings.GXAttributeCollection;
import gurux.dlms.manufacturersettings.GXDLMSAttributeSettings;
import gurux.dlms.objects.GXDLMSAssociationLogicalName;
import gurux.dlms.objects.GXDLMSAssociationShortName;
import gurux.dlms.objects.GXDLMSHdlcSetup;
import gurux.dlms.objects.GXDLMSObject;
import gurux.dlms.objects.GXDLMSObjectCollection;
import gurux.dlms.objects.IGXDLMSBase;
import gurux.dlms.objects.IGXDLMSSettings;
import gurux.dlms.objects.enums.ApplicationContextName;
import gurux.dlms.objects.enums.AssociationStatus;
import gurux.dlms.secure.GXSecure;

public class GXDLMSServerBase {
	private static final Logger LOGGER = Logger.getLogger(GXDLMSServer.class.getName());

	private final Object owner;
	private GXReplyData info = new GXReplyData();
	/*
	 * Received data.
	 */
	private GXByteBuffer receivedData = new GXByteBuffer();

	/*
	 * Reply data.
	 */
	private GXByteBuffer replyData = new GXByteBuffer();

	/*
	 * Long get or read transaction information.
	 */
	private GXDLMSLongTransaction transaction;

	/*
	 * Server settings.
	 */
	private final GXDLMSSettings settings;

	/*
	 * Is server initialized.
	 */
	private boolean initialized = false;

	/**
	 * When data was received last time.
	 */
	private long dataReceived = 0;

	/*
	 * @param value Cipher interface that is used to cipher PDU.
	 */
	protected final void setCipher(final GXICipher value) {
		settings.setCipher(value);
	}

	/*
	 * This is reserved for internal use only.
	 * 
	 * @param value Transaction.
	 */
	final void setTransaction(final GXDLMSLongTransaction value) {
		transaction = value;
	}

	/*
	 * This is reserved for internal use only.
	 * 
	 * @return Transaction.
	 */
	final GXDLMSLongTransaction getTransaction() {
		return transaction;
	}

	/*
	 * @return Client to Server challenge.
	 */
	public final byte[] getCtoSChallenge() {
		return settings.getCtoSChallenge();
	}

	/*
	 * @return Server to Client challenge.
	 */
	public final byte[] getStoCChallenge() {
		return settings.getStoCChallenge();
	}

	/*
	 * @return Interface type.
	 */
	public final InterfaceType getInterfaceType() {
		return settings.getInterfaceType();
	}

	/*
	 * Server to Client custom challenge. This is for debugging purposes. Reset
	 * custom challenge settings StoCChallenge to null.
	 * 
	 * @param value Server to Client challenge.
	 */
	public final void setStoCChallenge(final byte[] value) {
		settings.setUseCustomChallenge(value != null);
		settings.setStoCChallenge(value);
	}

	/*
	 * Set starting packet index. Default is One based, but some meters use Zero
	 * based value. Usually this is not used.
	 * 
	 * @param value Zero based starting index.
	 */
	public final void setStartingPacketIndex(final int value) {
		settings.setBlockIndex(value);
	}

	/*
	 * @return Invoke ID.
	 */
	public final int getInvokeID() {
		return settings.getInvokeID();
	}

	/*
	 * @param value Invoke ID.
	 */
	public final void setInvokeID(final int value) {
		settings.setInvokeID(value);
	}

	/*
	 * @return Used service class.
	 */
	public final ServiceClass getServiceClass() {
		return settings.getServiceClass();
	}

	/*
	 * @param value Used service class.
	 */
	public final void setServiceClass(final ServiceClass value) {
		settings.setServiceClass(value);
	}

	/*
	 * @return Used priority.
	 */
	public final Priority getPriority() {
		return settings.getPriority();
	}

	/*
	 * @param value Used priority.
	 */
	public final void setPriority(final Priority value) {
		settings.setPriority(value);
	}

	/**
	 * @param value Current association of the server.
	 */
	public final void setAssignedAssociation(final GXDLMSAssociationLogicalName value) {
		settings.setAssignedAssociation(value);
	}

	/**
	 * 
	 * @return Current server association.
	 */
	public final GXDLMSAssociationLogicalName getAssignedAssociation() {
		return settings.getAssignedAssociation();
	}

	/*
	 * Constructor.
	 * 
	 * @param forOwner Owner.
	 * 
	 * @param logicalNameReferencing Is logical name referencing used.
	 * 
	 * @param type Interface type.
	 */
	public GXDLMSServerBase(final Object forOwner, final boolean logicalNameReferencing, final InterfaceType type) {
		settings = new GXDLMSSettings(true, this instanceof IGXCryptoNotifier ? (IGXCryptoNotifier) this : null);
		owner = forOwner;
		settings.setUseLogicalNameReferencing(logicalNameReferencing);
		settings.setInterfaceType(type);
		reset();
	}

	/*
	 * @return List of objects that meter supports.
	 */
	public final GXDLMSObjectCollection getItems() {
		return settings.getObjects();
	}

	/**
	 * @return HDLC connection settings.
	 * @deprecated use {@link getHdlcSettings} instead.
	 */
	public final GXDLMSLimits getLimits() {
		return (GXDLMSLimits) settings.getHdlcSettings();
	}

	/**
	 * @return HDLC connection settings.
	 */
	public final GXHdlcSettings getHdlcSettings() {
		return settings.getHdlcSettings();
	}

	/**
	 * Standard says that Time zone is from normal time to UTC in minutes. If meter
	 * is configured to use UTC time (UTC to normal time) set this to true.
	 * 
	 * @return True, if UTC time is used.
	 */
	public boolean getUseUtc2NormalTime() {
		return settings.getUseUtc2NormalTime();
	}

	/**
	 * Standard says that Time zone is from normal time to UTC in minutes. If meter
	 * is configured to use UTC time (UTC to normal time) set this to true.
	 * 
	 * @param value True, if UTC time is used.
	 */
	public void setUseUtc2NormalTime(final boolean value) {
		settings.setUseUtc2NormalTime(value);
	}

	/**
	 * @return Skipped date time fields. This value can be used if meter can't
	 *         handle deviation or status.
	 */
	public java.util.Set<DateTimeSkips> getDateTimeSkips() {
		return settings.getDateTimeSkips();
	}

	/**
	 * @param value Skipped date time fields. This value can be used if meter can't
	 *              handle deviation or status.
	 */
	public void setDateTimeSkips(final java.util.Set<DateTimeSkips> value) {
		settings.setDateTimeSkips(value);
	}

	/*
	 * Retrieves the maximum size of received PDU. PDU size tells maximum size of
	 * PDU packet. Value can be from 0 to 0xFFFF. By default the value is 0xFFFF.
	 * 
	 * @return Maximum size of received PDU.
	 */
	public final int getMaxReceivePDUSize() {
		return settings.getMaxServerPDUSize();
	}

	/*
	 * @param value Maximum size of received PDU.
	 */
	public final void setMaxReceivePDUSize(final int value) {
		settings.setMaxServerPDUSize(value);
	}

	/*
	 * Determines, whether Logical, or Short name, referencing is used. Referencing
	 * depends on the device to communicate with. Normally, a device supports only
	 * either Logical or Short name referencing. The referencing is defined by the
	 * device manufacturer. If the referencing is wrong, the SNMR message will fail.
	 * 
	 * @see #getMaxReceivePDUSize
	 * 
	 * @return Is logical name referencing used.
	 */
	public final boolean getUseLogicalNameReferencing() {
		return settings.getUseLogicalNameReferencing();
	}

	/*
	 * @param value Is Logical Name referencing used.
	 */
	public final void setUseLogicalNameReferencing(final boolean value) {
		settings.setUseLogicalNameReferencing(value);
	}

	/*
	 * @return Get settings.
	 */
	public final GXDLMSSettings getSettings() {
		return settings;
	}

	/**
	 * Close server.
	 * 
	 * @throws Exception Occurred exception.
	 */
	public void close() throws Exception {
		for (GXDLMSObject it : settings.getObjects()) {
			it.stop(this);
		}
	}

	/*
	 * Initialize server. This must call after server objects are set.
	 */
	public final void initialize() {
		GXDLMSObject associationObject = null;
		initialized = true;
		for (int pos = 0; pos != settings.getObjects().size(); ++pos) {
			GXDLMSObject it = settings.getObjects().get(pos);
			if (it.getLogicalName() == null) {
				throw new IllegalArgumentException("Invalid Logical Name.");
			}
			it.start(this);
			if (it instanceof GXDLMSAssociationShortName && !this.getUseLogicalNameReferencing()) {
				if (((GXDLMSAssociationShortName) it).getObjectList().isEmpty()) {
					((GXDLMSAssociationShortName) it).getObjectList().addAll(getItems());
				}
				associationObject = it;
			} else if (it instanceof GXDLMSAssociationLogicalName && this.getUseLogicalNameReferencing()) {
				GXDLMSAssociationLogicalName ln = (GXDLMSAssociationLogicalName) it;
				if (ln.getObjectList().isEmpty()) {
					ln.getObjectList().addAll(getItems());
				}
				associationObject = it;
				if (!settings.getProposedConformance().isEmpty()) {
					ln.getXDLMSContextInfo().setConformance(settings.getProposedConformance());
				}

			} else if (!(it instanceof IGXDLMSBase)) {
				// Remove unsupported items.
				LOGGER.log(Level.INFO, it.getLogicalName() + " not supported.");
				settings.getObjects().remove(pos);
				--pos;
			} else if (it instanceof IGXDLMSSettings) {
				((IGXDLMSSettings) it).setSettings(getSettings());
			}
		}
		if (associationObject == null) {
			if (getUseLogicalNameReferencing()) {
				GXDLMSAssociationLogicalName it = new GXDLMSAssociationLogicalName();
				it.getXDLMSContextInfo().setMaxReceivePduSize(settings.getMaxServerPDUSize());
				it.getXDLMSContextInfo().setMaxSendPduSize(settings.getMaxServerPDUSize());
				getItems().add(it);
				it.getObjectList().addAll(getItems());
			} else {
				GXDLMSAssociationShortName it = new GXDLMSAssociationShortName();
				getItems().add(it);
				it.getObjectList().addAll(getItems());
			}
		}
		// Arrange items by Short Name.
		if (!this.getUseLogicalNameReferencing()) {
			updateShortNames(false);
		}
	}

	/**
	 * Update short names.
	 * 
	 * @param force Force update.
	 */
	final void updateShortNames(final boolean force) {
		int sn = 0xA0;
		int[] offset = new int[1];
		int[] count = new int[1];
		for (GXDLMSObject it : settings.getObjects()) {
			if (!(it instanceof GXDLMSAssociationShortName || it instanceof GXDLMSAssociationLogicalName)) {
				// Generate Short Name if not given.
				if (force || it.getShortName() == 0) {
					it.setShortName(sn);
					// Add method index addresses.
					GXDLMS.getActionInfo(it.getObjectType(), offset, count);
					if (count[0] != 0) {
						sn += offset[0] + (8 * count[0]);
					} else {
						// If there are no methods.
						// Add attribute index addresses.
						sn += 8 * it.getAttributeCount();
					}
				} else {
					sn = it.getShortName();
				}
			}
		}
	}

	/*
	 * Parse AARQ request that client send and returns AARE request.
	 * 
	 * @return Reply to the client.
	 */
	private void handleAarqRequest(final GXByteBuffer data, final GXDLMSConnectionEventArgs connectionInfo)
			throws Exception {
		AssociationResult result = AssociationResult.ACCEPTED;
		GXByteBuffer error = null;
		settings.setCtoSChallenge(null);
		if (settings.getCipher() != null) {
			settings.getCipher().setDedicatedKey(null);
		}
		// Reset settings for wrapper.
		if (settings.getInterfaceType() == InterfaceType.WRAPPER || settings.getInterfaceType() == InterfaceType.PDU) {
			reset(true);
		}
		Object ret;
		int name = 0;
		try {
			ret = GXAPDU.parsePDU(settings, settings.getCipher(), data, null);
			if (ret instanceof ExceptionServiceError) {
				ExceptionServiceError e = (ExceptionServiceError) ret;
				if (GXDLMS.useHdlc(settings.getInterfaceType())) {
					replyData.set(GXCommon.LLC_REPLY_BYTES);
				}
				replyData.setUInt8(Command.EXCEPTION_RESPONSE);
				replyData.setUInt8(ExceptionStateError.SERVICE_UNKNOWN.getValue());
				replyData.setUInt8(e.getValue());
				if (e == ExceptionServiceError.INVOCATION_COUNTER_ERROR) {
					replyData.setUInt32(((Number) settings.getInvocationCounter().getValue()).longValue());
				}
				return;
			}
			if (!(ret instanceof AcseServiceProvider)) {
				if (ret instanceof ApplicationContextName) {
					name = ((ApplicationContextName) ret).ordinal();
					result = AssociationResult.PERMANENT_REJECTED;
					ret = SourceDiagnostic.NOT_SUPPORTED;

				} else if (settings.getNegotiatedConformance().isEmpty()) {
					result = AssociationResult.PERMANENT_REJECTED;
					ret = SourceDiagnostic.NO_REASON_GIVEN;
					error = new GXByteBuffer();
					error.setUInt8(0xE);
					error.setUInt8(ConfirmedServiceError.INITIATE_ERROR.getValue());
					error.setUInt8(ServiceError.INITIATE.getValue());
					error.setUInt8(Initiate.INCOMPATIBLE_CONFORMANCE.getValue());
				} else if (settings.getMaxPduSize() < 64) {
					// If PDU is too low.
					result = AssociationResult.PERMANENT_REJECTED;
					ret = SourceDiagnostic.NO_REASON_GIVEN;
					error = new GXByteBuffer();
					error.setUInt8(0xE);
					error.setUInt8(ConfirmedServiceError.INITIATE_ERROR.getValue());
					error.setUInt8(ServiceError.INITIATE.getValue());
					error.setUInt8(Initiate.PDU_SIZE_TOO_SHORT.getValue());
				} else if (settings.getDLMSVersion() != 6) {
					settings.setDLMSVersion(6);
					result = AssociationResult.PERMANENT_REJECTED;
					ret = SourceDiagnostic.NO_REASON_GIVEN;
					error = new GXByteBuffer();
					error.setUInt8(0xE);
					error.setUInt8(ConfirmedServiceError.INITIATE_ERROR.getValue());
					error.setUInt8(ServiceError.INITIATE.getValue());
					error.setUInt8(Initiate.DLMS_VERSION_TOO_LOW.getValue());
				} else if (SourceDiagnostic.forValue((int) ret) != SourceDiagnostic.NONE) {
					result = AssociationResult.PERMANENT_REJECTED;
					ret = SourceDiagnostic.NOT_SUPPORTED;
					notifyInvalidConnection(connectionInfo);
				} else {
					if (owner instanceof GXDLMSServer) {
						GXDLMSServer b = (GXDLMSServer) owner;
						ret = b.validateAuthentication(settings.getAuthentication(), settings.getPassword());
					} else {
						GXDLMSServer2 b = (GXDLMSServer2) owner;
						ret = b.onValidateAuthentication(settings.getAuthentication(), settings.getPassword());
					}

					if ((SourceDiagnostic) ret != SourceDiagnostic.NONE) {
						result = AssociationResult.PERMANENT_REJECTED;
					} else if (settings.getAuthentication().getValue() > Authentication.LOW.getValue()) {
						result = AssociationResult.ACCEPTED;
						ret = SourceDiagnostic.AUTHENTICATION_REQUIRED;
						if (getUseLogicalNameReferencing()) {
							GXDLMSAssociationLogicalName ln = getAssignedAssociation();
							if (ln == null) {
								ln = (GXDLMSAssociationLogicalName) getItems()
										.findByLN(ObjectType.ASSOCIATION_LOGICAL_NAME, "0.0.40.0.0.255");
								if (ln == null) {
									ln = (GXDLMSAssociationLogicalName) notifyFindObject(
											ObjectType.ASSOCIATION_LOGICAL_NAME, 0, "0.0.40.0.0.255");
								}
							}
							if (ln != null) {
								ln.setAssociationStatus(AssociationStatus.ASSOCIATION_PENDING);
							}
						}
					} else {
						if (getUseLogicalNameReferencing()) {
							GXDLMSAssociationLogicalName ln = getAssignedAssociation();
							if (ln == null) {
								ln = (GXDLMSAssociationLogicalName) getItems()
										.findByLN(ObjectType.ASSOCIATION_LOGICAL_NAME, "0.0.40.0.0.255");
								if (ln == null) {
									ln = (GXDLMSAssociationLogicalName) notifyFindObject(
											ObjectType.ASSOCIATION_LOGICAL_NAME, 0, "0.0.40.0.0.255");
								}
							}
							if (ln != null) {
								ln.setAssociationStatus(AssociationStatus.ASSOCIATED);
							}
						}
						settings.setConnected(settings.getConnected() | ConnectionState.DLMS);
					}
				}
			} else if (result == AssociationResult.ACCEPTED && ((AcseServiceProvider) ret).getValue() != 0) {
				result = AssociationResult.PERMANENT_REJECTED;
			}
		} catch (GXDLMSConfirmedServiceError e) {
			result = AssociationResult.PERMANENT_REJECTED;
			ret = SourceDiagnostic.NO_REASON_GIVEN;
			error = new GXByteBuffer();
			error.setUInt8(0xE);
			error.setUInt8(e.getConfirmedServiceError().getValue());
			error.setUInt8(e.getServiceError().getValue());
			error.setUInt8(e.getServiceErrorValue());
		} catch (GXDLMSException e) {
			result = e.getResult();
			ret = e.getDiagnostic();
		}
		if (GXDLMS.useHdlc(settings.getInterfaceType())) {
			replyData.set(GXCommon.LLC_REPLY_BYTES);
		}
		// Generate challenge if High authentication is used.
		if (settings.getAuthentication().getValue() > Authentication.LOW.getValue()) {
			settings.setStoCChallenge(GXSecure.generateChallenge(settings.getAuthentication()));
		}
		// Generate AARE packet.
		GXAPDU.generateAARE(name, settings, replyData, result, ret, settings.getCipher(), error, null);
	}

	/**
	 * Handles release request.
	 * 
	 * @param data           Received data.
	 * @param connectionInfo Connection info.
	 */
	@SuppressWarnings("squid:S1172")
	private void handleReleaseRequest(final GXByteBuffer data, final GXDLMSConnectionEventArgs connectionInfo)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// Return error if connection is not established.
		if ((settings.getConnected() & ConnectionState.DLMS) == 0) {
			replyData.set(GXDLMSServerBase.generateConfirmedServiceError(ConfirmedServiceError.INITIATE_ERROR,
					ServiceError.SERVICE, Service.UNSUPPORTED.getValue()));
			return;
		}
		if (GXDLMS.useHdlc(settings.getInterfaceType())) {
			replyData.set(0, GXCommon.LLC_REPLY_BYTES);
		}
		byte[] tmp = GXAPDU.getUserInformation(settings, settings.getCipher());
		replyData.setUInt8(0x63);
		// Len.
		replyData.setUInt8((byte) (tmp.length + 3));
		replyData.setUInt8(0x80);
		replyData.setUInt8(0x01);
		replyData.setUInt8(0x00);
		replyData.setUInt8(0xBE);
		replyData.setUInt8((byte) (tmp.length + 1));
		replyData.setUInt8(4);
		replyData.setUInt8((byte) (tmp.length));
		replyData.set(tmp);
	}

	/*
	 * Parse SNRM Request. If server do not accept client empty byte array is
	 * returned.
	 * 
	 * @return Returns returned UA packet.
	 */
	private void handleSnrmRequest(final GXByteBuffer data) {
		GXDLMS.parseSnrmUaResponse(data, settings);
		reset(true);
		replyData.setUInt8(0x81); // FromatID
		replyData.setUInt8(0x80); // GroupID
		replyData.setUInt8(0); // Length
		if (getHdlc() != null) {
			// If client wants send larger HDLC frames what meter accepts.
			if (settings.getHdlcSettings().getMaxInfoTX() > getHdlc().getMaximumInfoLengthReceive()) {
				settings.getHdlcSettings().setMaxInfoTX(getHdlc().getMaximumInfoLengthReceive());
			}
			// If client wants receive larger HDLC frames what meter accepts.
			if (settings.getHdlcSettings().getMaxInfoRX() > getHdlc().getMaximumInfoLengthTransmit()) {
				settings.getHdlcSettings().setMaxInfoRX(getHdlc().getMaximumInfoLengthTransmit());
			}
			// If client asks higher window size what meter accepts.
			if (settings.getHdlcSettings().getMaxInfoRX() > getHdlc().getMaximumInfoLengthTransmit()) {
				settings.getHdlcSettings().setWindowSizeTX(getHdlc().getWindowSizeReceive());
			}
			// If client asks higher window size what meter accepts.
			if (settings.getHdlcSettings().getMaxInfoRX() > getHdlc().getMaximumInfoLengthTransmit()) {
				settings.getHdlcSettings().setWindowSizeRX(getHdlc().getWindowSizeTransmit());
			}
		}
		replyData.setUInt8(HDLCInfo.MAX_INFO_TX);
		GXDLMS.appendHdlcParameter(replyData, getHdlcSettings().getMaxInfoTX());

		replyData.setUInt8(HDLCInfo.MAX_INFO_RX);
		GXDLMS.appendHdlcParameter(replyData, getHdlcSettings().getMaxInfoRX());

		replyData.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
		replyData.setUInt8(4);
		replyData.setUInt32(getHdlcSettings().getWindowSizeTX());
		replyData.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
		replyData.setUInt8(4);
		replyData.setUInt32(getHdlcSettings().getWindowSizeRX());
		int len = replyData.size() - 3;
		replyData.setUInt8(2, len); // Length
		settings.setConnected(ConnectionState.HDLC);
	}

	/*
	 * Generates disconnect request.
	 * 
	 * @return Disconnect request.
	 */
	private void generateDisconnectRequest() {
		replyData.setUInt8(0x81); // FromatID
		replyData.setUInt8(0x80); // GroupID
		replyData.setUInt8(0); // Length

		replyData.setUInt8(HDLCInfo.MAX_INFO_TX);
		replyData.setUInt8(1);
		replyData.setUInt8(getHdlcSettings().getMaxInfoTX());

		replyData.setUInt8(HDLCInfo.MAX_INFO_RX);
		replyData.setUInt8(1);
		replyData.setUInt8(getHdlcSettings().getMaxInfoRX());

		replyData.setUInt8(HDLCInfo.WINDOW_SIZE_TX);
		replyData.setUInt8(4);
		replyData.setUInt32(getHdlcSettings().getWindowSizeTX());

		replyData.setUInt8(HDLCInfo.WINDOW_SIZE_RX);
		replyData.setUInt8(4);
		replyData.setUInt32(getHdlcSettings().getWindowSizeRX());

		int len = replyData.size() - 3;
		replyData.setUInt8(2, len); // Length.
	}

	/*
	 * Reset after connection is closed.
	 * 
	 * @param connect Is new connection.
	 */
	final void reset(final boolean connect) {
		if (!connect) {
			info.clear();
			settings.setServerAddress(0);
			settings.setClientAddress(0);
		}
		// Reset Ephemeral keys.
		settings.setEphemeralBlockCipherKey(null);
		settings.setEphemeralBroadcastBlockCipherKey(null);
		settings.setEphemeralAuthenticationKey(null);
		settings.setProtocolVersion(null);
		settings.setCtoSChallenge(null);
		settings.setStoCChallenge(null);
		receivedData.clear();
		transaction = null;
		settings.setCount(0);
		settings.setIndex(0);
		settings.setConnected(ConnectionState.NONE);
		replyData.clear();
		settings.setAuthentication(Authentication.NONE);
		if (settings.getCipher() != null) {
			settings.getCipher().reset();
		}
	}

	/*
	 * Reset after connection is closed.
	 */
	public final void reset() {
		reset(false);
	}

	/**
	 * Handles client request.
	 * 
	 * @param sr Server reply.
	 * @throws NoSuchPaddingException             No such padding exception.
	 * @throws NoSuchAlgorithmException           No such algorithm exception.
	 * @throws InvalidAlgorithmParameterException Invalid algorithm parameter
	 *                                            exception.
	 * @throws InvalidKeyException                Invalid key exception.
	 * @throws BadPaddingException                Bad padding exception.
	 * @throws IllegalBlockSizeException          Illegal block size exception.
	 */
	@SuppressWarnings({ "squid:S00112", "squid:S1193", "squid:S1066", "squid:S1141" })
	public final void handleRequest(GXServerReply sr) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (!sr.isStreaming() && (sr.getData() == null || sr.getData().length == 0)) {
			return;
		}
		if (!initialized) {
			throw new RuntimeException("Server not Initialized.");
		}
		try {
			if (!sr.isStreaming()) {
				receivedData.set(sr.getData());
				boolean first = settings.getServerAddress() == 0 && settings.getClientAddress() == 0;
				try {
					GXDLMS.getData(settings, receivedData, info, null);
				} catch (GXDLMSExceptionResponse ex) {
					transaction = null;
					settings.setCount(0);
					settings.setIndex(0);
//					info.clear();
					receivedData.clear();
					dataReceived = Calendar.getInstance().getTimeInMillis();
					sr.setReply(reportError(info.getCommand(), ErrorCode.INCONSISTENT_CLASS));
					return;
				} catch (Exception ex) {
					transaction = null;
					settings.setCount(0);
					settings.setIndex(0);
					receivedData.clear();
					dataReceived = Calendar.getInstance().getTimeInMillis();
					if ((getSettings().getConnected() & ConnectionState.DLMS) != 0) {
						sr.setReply(reportError(info.getCommand(), ErrorCode.INCONSISTENT_CLASS));
					}
					return;
				}
				// If all data is not received yet.
				if (!info.isComplete()) {
					return;
				}
				receivedData.clear();
				if (info.getCommand() == Command.DISCONNECT_REQUEST
						&& (settings.getConnected() == ConnectionState.NONE)) {
					if (owner instanceof GXDLMSServer) {
						GXDLMSServer b = (GXDLMSServer) owner;
						// Check is data send to this server.
						if (!b.isTarget(settings.getServerAddress(), settings.getClientAddress())) {
							info.clear();
							return;
						}
					} else {
						GXDLMSServer2 b = (GXDLMSServer2) owner;
						// Check is data send to this server.
						if (!b.isTarget(settings.getServerAddress(), settings.getClientAddress())) {
							info.clear();
							return;
						}
					}
					sr.setReply(GXDLMS.getHdlcFrame(settings, Command.DISCONNECT_MODE, replyData));
					info.clear();
					return;
				}

				if (first || info.getCommand() == Command.SNRM || (settings.getInterfaceType() == InterfaceType.WRAPPER
						&& info.getCommand() == Command.AARQ)) {
					if (owner instanceof GXDLMSServer) {
						GXDLMSServer b = (GXDLMSServer) owner;
						// Check is data send to this server.
						if (!b.isTarget(settings.getServerAddress(), settings.getClientAddress())) {
							info.clear();
							settings.setClientAddress(0);
							settings.setServerAddress(0);
							return;
						}
					} else {
						GXDLMSServer2 b = (GXDLMSServer2) owner;
						// Check is data send to this server.
						if (!b.isTarget(settings.getServerAddress(), settings.getClientAddress())) {
							info.clear();
							settings.setClientAddress(0);
							settings.setServerAddress(0);
							return;
						}
					}
				}

				// If client want next frame.
				if (info.getMoreData().contains(RequestTypes.FRAME)) {
					dataReceived = Calendar.getInstance().getTimeInMillis();
					sr.setReply(GXDLMS.getHdlcFrame(settings, settings.getReceiverReady(), replyData));
					return;
				}
				// Update command if transaction and next frame is asked.
				if (info.getCommand() == Command.NONE) {
					if (transaction != null) {
						info.setCommand(transaction.getCommand());
					} else if (replyData.size() == 0) {
						sr.setReply(GXDLMS.getHdlcFrame(settings, settings.getReceiverReady(), replyData));
						return;
					}
				}
				// Check inactivity time out.
				if (settings.getHdlc() != null && settings.getHdlc().getInactivityTimeout() != 0) {
					if (info.getCommand() != Command.SNRM) {
						int elapsed = (int) (Calendar.getInstance().getTimeInMillis() - dataReceived) / 1000;
						// If inactivity time out is elapsed.
						if (elapsed >= settings.getHdlc().getInactivityTimeout()) {
							reset();
							dataReceived = 0;
							return;
						}
					}
				} else if (settings.getWrapper() != null && settings.getWrapper().getInactivityTimeout() != 0) {
					if (info.getCommand() != Command.AARQ) {
						int elapsed = (int) (Calendar.getInstance().getTimeInMillis() - dataReceived) / 1000;
						// If inactivity time out is elapsed.
						if (elapsed >= settings.getWrapper().getInactivityTimeout()) {
							reset();
							dataReceived = 0;
							return;
						}
					}
				}
			} else {
				info.setCommand(Command.GENERAL_BLOCK_TRANSFER);
			}
			try {
				sr.setReply(handleCommand(info.getCommand(), info.getData(), sr, info.getCipheredCommand()));
			} catch (Exception ex) {
				receivedData.size(0);
				sr.setReply(reportError(info.getCommand(), ErrorCode.INCONSISTENT_CLASS));
				return;
			}
			dataReceived = Calendar.getInstance().getTimeInMillis();
			info.clear();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
			if (e instanceof GXDLMSConfirmedServiceError) {
				sr.setReply(reportConfirmedServiceError((GXDLMSConfirmedServiceError) e));
				transaction = null;
				settings.setCount(0);
				settings.setIndex(0);
				info.clear();
				receivedData.clear();
				return;
			}
			if (info.getCommand() != Command.NONE) {
				sr.setReply(reportError(info.getCommand(), ErrorCode.INCONSISTENT_CLASS));
				transaction = null;
				settings.setCount(0);
				settings.setIndex(0);
				info.clear();
				receivedData.clear();
			} else {
				reset();
				if ((settings.getConnected() & ConnectionState.DLMS) != 0) {
					settings.setConnected(settings.getConnected() & ~ConnectionState.DLMS);
					if (owner instanceof GXDLMSServer) {
						GXDLMSServer b = (GXDLMSServer) owner;
						b.disconnected(sr.getConnectionInfo());
					} else {
						GXDLMSServer2 b = (GXDLMSServer2) owner;
						try {
							b.onDisconnected(sr.getConnectionInfo());
						} catch (Exception ex) {
							// It's OK if this fails.
						}
					}
				}
			}
		}
	}

	// GXDLMSConfirmedServiceError
	private byte[] reportConfirmedServiceError(final GXDLMSConfirmedServiceError e) {
		replyData.clear();
		if (getSettings().getInterfaceType() == InterfaceType.HDLC) {
			GXDLMS.addLLCBytes(getSettings(), replyData);
		}
		replyData.setUInt8(Command.CONFIRMED_SERVICE_ERROR);
		replyData.setUInt8(e.getConfirmedServiceError().getValue());
		replyData.setUInt8(e.getServiceError().getValue());
		replyData.setUInt8(e.getServiceErrorValue());
		if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
			return GXDLMS.getWrapperFrame(settings, Command.CONFIRMED_SERVICE_ERROR, replyData);
		} else {
			return GXDLMS.getHdlcFrame(settings, (byte) 0, replyData);
		}
	}

	private byte[] reportError(final int command, final ErrorCode error)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		short cmd;
		switch (command) {
		case Command.READ_REQUEST:
			cmd = Command.READ_RESPONSE;
			break;
		case Command.WRITE_REQUEST:
			cmd = Command.WRITE_RESPONSE;
			break;
		case Command.GET_REQUEST:
			cmd = Command.GET_RESPONSE;
			break;
		case Command.SET_REQUEST:
			cmd = Command.SET_RESPONSE;
			break;
		case Command.METHOD_REQUEST:
			cmd = Command.METHOD_RESPONSE;
			break;
		case Command.GENERAL_CIPHERING:
			cmd = Command.GENERAL_CIPHERING;
			break;
		default:
			// Return HW error and close connection.
			cmd = Command.NONE;
			break;
		}
		if (settings.getUseLogicalNameReferencing()) {
			GXDLMSLNParameters p = new GXDLMSLNParameters(settings, 0, cmd, 1, null, null, error.getValue(),
					info.getCipheredCommand());
			GXDLMS.getLNPdu(p, replyData);
		} else {
			GXByteBuffer bb = new GXByteBuffer();
			bb.setUInt8(error.getValue());
			GXDLMSSNParameters p = new GXDLMSSNParameters(settings, cmd, 1, 1, null, bb);
			GXDLMS.getSNPdu(p, replyData);
		}
		if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
			return GXDLMS.getWrapperFrame(settings, cmd, replyData);
		} else {
			return GXDLMS.getHdlcFrame(settings, (byte) 0, replyData);
		}
	}

	/*
	 * Handle received command.
	 * 
	 * @param cmd Executed command.
	 * 
	 * @param data Received data from the client.
	 * 
	 * @param connectionInfo Connection info.
	 * 
	 * @return Response for the client.
	 */
	private byte[] handleCommand(final int cmd, final GXByteBuffer data, final GXServerReply sr,
			final int cipheredCommand) throws Exception {
		byte frame = 0;
		if (GXDLMS.useHdlc(settings.getInterfaceType()) && replyData.size() != 0) {
			// Get next frame.
			frame = settings.getNextSend(false);
		}
		Boolean invalidCommand = false;
		// Connection established is checked inside of the function because of HLS.
		// If connection is not established.
		if (cmd != Command.AARQ && cmd != Command.SNRM && cmd != Command.WRITE_REQUEST && cmd != Command.METHOD_REQUEST
				&& cmd != Command.DISCONNECT_REQUEST && (settings.getConnected() & ConnectionState.DLMS) == 0
				&& cipheredCommand == 0) {
			replyData.clear();
			replyData.setUInt8(Command.EXCEPTION_RESPONSE);
			replyData.setUInt8(ExceptionStateError.SERVICE_UNKNOWN.getValue());
			replyData.setUInt8(ExceptionServiceError.SERVICE_NOT_SUPPORTED.getValue());
		} else {
			switch (cmd) {
			case Command.ACCESS_REQUEST:
				if (!settings.getNegotiatedConformance().contains(Conformance.ACCESS)) {
					invalidCommand = true;
				} else {
					GXDLMSLNCommandHandler.handleAccessRequest(settings, this, data, replyData, null, cipheredCommand);
				}
				break;
			case Command.SET_REQUEST:
				if (!settings.getNegotiatedConformance().contains(Conformance.SET)) {
					invalidCommand = true;
				} else {
					GXDLMSLNCommandHandler.handleSetRequest(settings, this, data, replyData, null, cipheredCommand);
				}
				break;
			case Command.WRITE_REQUEST:
				if (!settings.getNegotiatedConformance().contains(Conformance.WRITE)) {
					invalidCommand = true;
				} else {
					GXDLMSSNCommandHandler.handleWriteRequest(settings, this, data, replyData, null, cipheredCommand);
				}
				break;
			case Command.GET_REQUEST:
				if (!settings.getNegotiatedConformance().contains(Conformance.GET)) {
					invalidCommand = true;
				} else {
					if (data.size() != 0) {
						GXDLMSLNCommandHandler.handleGetRequest(settings, this, data, replyData, null, cipheredCommand);
					}
				}
				break;
			case Command.READ_REQUEST:
				if (!settings.getNegotiatedConformance().contains(Conformance.READ)) {
					invalidCommand = true;
				} else {
					GXDLMSSNCommandHandler.handleReadRequest(settings, this, data, replyData, null, cipheredCommand);
				}
				break;
			case Command.METHOD_REQUEST:
				if (!settings.getNegotiatedConformance().contains(Conformance.ACTION)) {
					invalidCommand = true;
				} else {
					GXDLMSLNCommandHandler.handleMethodRequest(settings, this, data, sr.getConnectionInfo(), replyData,
							null, cipheredCommand);
				}
				break;
			case Command.SNRM:
				handleSnrmRequest(data);
				frame = (byte) Command.UA;
				break;
			case Command.AARQ:
				handleAarqRequest(data, sr.getConnectionInfo());
				settings.updateSecuritySettings(settings.getSourceSystemTitle());
				// Update public key to signing key pair.
//				if (settings.getAuthentication() == Authentication.HIGH_ECDSA
//						|| (settings.getCipher().getSecuritySuite() != SecuritySuite.GMAC
//								&& settings.getCipher().getSecurity() != Security.NONE)) {
//					KeyPair kp = settings.getCipher().getSigningKeyPair();
//					if (kp != null) {						
//						GXDLMSSecuritySetup ss = (GXDLMSSecuritySetup) assignedAssociation.getObjectList()
//						.findByLN(ObjectType.SECURITY_SETUP, assignedAssociation.getSecuritySetupReference());
//				if (ss != null) {
////						GXx509Certificate cert = settings.getCipher().getCertificates().findCertificateBySystemTitle(
////								settings.getSourceSystemTitle(), KeyUsage.DIGITAL_SIGNATURE);
////						if (cert != null) {
////							kp = new KeyPair(cert.getPublicKey(), kp.getPrivate());
////							settings.getCipher().setSigningKeyPair(kp);
////							Logger.getLogger(GXDLMSServer.class.getName()).log(Level.INFO,
////									"Signing key updated to " + cert.getSubject());
////						}
//					}
//					kp = settings.getCipher().getKeyAgreementKeyPair();
//					if (kp != null) {
////						GXx509Certificate cert = settings.getCipher().getCertificates()
////								.findCertificateBySystemTitle(settings.getSourceSystemTitle(), KeyUsage.KEY_AGREEMENT);
////						if (cert != null) {
////							kp = new KeyPair(cert.getPublicKey(), kp.getPrivate());
////							settings.getCipher().setKeyAgreementKeyPair(kp);
////							Logger.getLogger(GXDLMSServer.class.getName()).log(Level.INFO,
////									"Agreement key updated to " + cert.getSubject());
////						}
//					}
//				}

				if ((settings.getConnected() & ConnectionState.DLMS) != 0) {
					notifyConnected(sr.getConnectionInfo());
				}
				break;
			case Command.RELEASE_REQUEST:
				handleReleaseRequest(data, sr.getConnectionInfo());
				if ((settings.getConnected() & ConnectionState.DLMS) != 0) {
					settings.setConnected(settings.getConnected() & ~ConnectionState.DLMS);
					if (owner instanceof GXDLMSServer) {
						((GXDLMSServer) owner).disconnected(sr.getConnectionInfo());
					} else {
						((GXDLMSServer2) owner).onDisconnected(sr.getConnectionInfo());
					}
				}
				break;
			case Command.DISCONNECT_REQUEST:
				replyData.clear();
				generateDisconnectRequest();
				if ((settings.getConnected() & ConnectionState.DLMS) != 0) {
					if (owner instanceof GXDLMSServer) {
						((GXDLMSServer) owner).disconnected(sr.getConnectionInfo());
					} else {
						((GXDLMSServer2) owner).onDisconnected(sr.getConnectionInfo());
					}
				}
				settings.setConnected(settings.getConnected() & ~ConnectionState.DLMS);
				frame = Command.UA;
				break;
			case Command.GENERAL_BLOCK_TRANSFER:
				if (!handleGeneralBlockTransfer(data, sr, info.getCipheredCommand())) {
					return null;
				}
				break;
			case Command.DISCOVER_REQUEST:
				settings.getPlc().parseDiscoverRequest(data);
				boolean newMeter = settings.getPlc().getMacSourceAddress() == 0xFFE
						&& settings.getPlc().getMacDestinationAddress() == 0xFFF;
				return settings.getPlc().discoverReport(settings.getPlc().getSystemTitle(), newMeter);
			case Command.REGISTER_REQUEST:
				settings.getPlc().parseRegisterRequest(data);
				return settings.getPlc().discoverReport(settings.getPlc().getSystemTitle(), false);
			case Command.PING_REQUEST:
				break;
			case Command.NONE:
				// Client wants to get next block.
				break;
			default:
				throw new IllegalArgumentException("Invalid command: " + String.valueOf(cmd));
			}
			if (invalidCommand) {
				replyData.clear();
				replyData.setUInt8(Command.EXCEPTION_RESPONSE);
				replyData.setUInt8(ExceptionStateError.SERVICE_UNKNOWN.getValue());
				replyData.setUInt8(ExceptionServiceError.SERVICE_NOT_SUPPORTED.getValue());
			}
		}
		byte[] reply;
		if (settings.getInterfaceType() == InterfaceType.WRAPPER) {
			reply = GXDLMS.getWrapperFrame(settings, cmd, replyData);
		} else {
			reply = GXDLMS.getHdlcFrame(settings, frame, replyData);
		}
		if (cmd == Command.DISCONNECT_REQUEST
				|| (settings.getInterfaceType() == InterfaceType.WRAPPER && cmd == Command.RELEASE_REQUEST)) {
			reset();
		}
		return reply;
	}

	private boolean handleGeneralBlockTransfer(final GXByteBuffer data, final GXServerReply sr,
			final int cipheredCommand) throws Exception {
		if (transaction != null) {
			if (transaction.getCommand() == Command.GET_REQUEST) {
				// Get request for next data block
				if (sr.getCount() == 0) {
					settings.setBlockNumberAck(settings.getBlockNumberAck() + 1);
					sr.setCount(settings.getWindowSize());
				}
				GXDLMSLNCommandHandler.getRequestNextDataBlock(settings, 0, this, data, replyData, null, true,
						cipheredCommand);
				if (sr.getCount() != 0) {
					sr.setCount(sr.getCount() - 1);
				}
				if (this.transaction == null) {
					sr.setCount(0);
				}
			} else {
				// BlockControl
				short bc = data.getUInt8();
				// Block number.
				int blockNumber = data.getUInt16();
				// Block number acknowledged.
				int blockNumberAck = data.getUInt16();
				int len = GXCommon.getObjectCount(data);
				if (len > data.size() - data.position()) {
					replyData.set(generateConfirmedServiceError(ConfirmedServiceError.INITIATE_ERROR,
							ServiceError.SERVICE, Service.UNSUPPORTED.getValue()));
				} else {
					transaction.getData().set(data);
					// Send ACK.
					boolean igonoreAck = (bc & 0x40) != 0
							&& (blockNumberAck * settings.getWindowSize()) + 1 > blockNumber;
					int windowSize = settings.getWindowSize();
					int bn = settings.getBlockIndex();
					if ((bc & 0x80) != 0) {
						handleCommand(transaction.getCommand(), transaction.getData(), sr, cipheredCommand);
						transaction = null;
						igonoreAck = false;
						windowSize = 1;
					}
					if (igonoreAck) {
						return false;
					}
					replyData.setUInt8(Command.GENERAL_BLOCK_TRANSFER);
					replyData.setUInt8((byte) (0x80 | windowSize));
					settings.setBlockIndex(settings.getBlockIndex() + 1);
					replyData.setUInt16(bn);
					replyData.setUInt16(blockNumber);
					replyData.setUInt8(0);
				}
			}
		} else {
			// BlockControl
			// short bc =
			data.getUInt8();
			// Block number.
			int blockNumber = data.getUInt16();
			// Block number acknowledged.
			int blockNumberAck = data.getUInt16();
			int len = GXCommon.getObjectCount(data);
			if (len > data.size() - data.position()) {
				replyData.set(generateConfirmedServiceError(ConfirmedServiceError.INITIATE_ERROR, ServiceError.SERVICE,
						Service.UNSUPPORTED.getValue()));
			} else {
				transaction = new GXDLMSLongTransaction(null, data.getUInt8(), data);
				replyData.setUInt8(Command.GENERAL_BLOCK_TRANSFER);
				replyData.setUInt8((0x80 | settings.getWindowSize()));
				replyData.setUInt16(blockNumber);
				++blockNumberAck;
				replyData.setUInt16(blockNumberAck);
				replyData.setUInt8(0);
			}
		}
		return true;
	}

	/*
	 * Generate confirmed service error.
	 * 
	 * @param service Confirmed service error.
	 * 
	 * @param type Service error.
	 * 
	 * @param code code
	 * 
	 * @return
	 */
	static byte[] generateConfirmedServiceError(final ConfirmedServiceError service, final ServiceError type,
			final int code) {
		return new byte[] { (byte) Command.CONFIRMED_SERVICE_ERROR, (byte) service.getValue(), (byte) type.getValue(),
				(byte) code };
	}

	final GXDLMSObject notifyFindObject(final ObjectType objectType, final int sn, final String ln) throws Exception {
		if (owner instanceof GXDLMSServer) {
			return ((GXDLMSServer) owner).onFindObject(objectType, sn, ln);
		}
		return ((GXDLMSServer2) owner).onFindObject(objectType, sn, ln);
	}

	/*
	 * Read selected item(s).
	 * 
	 * @param args Handled read requests.
	 */
	final void notifyRead(final ValueEventArgs[] args) throws Exception {
		if (owner instanceof GXDLMSServer) {
			((GXDLMSServer) owner).read(args);
		} else {
			((GXDLMSServer2) owner).onPreRead(args);
		}
	}

	/*
	 * Write selected item(s).
	 * 
	 * @param args Handled write requests.
	 */
	final void notifyWrite(final ValueEventArgs[] args) throws Exception {
		if (owner instanceof GXDLMSServer) {
			((GXDLMSServer) owner).write(args);
		} else {
			((GXDLMSServer2) owner).onPreWrite(args);
		}
	}

	/*
	 * Action is occurred.
	 * 
	 * @param args Handled action requests.
	 */
	public final void notifyAction(final ValueEventArgs[] args) throws Exception {
		if (owner instanceof GXDLMSServer) {
			((GXDLMSServer) owner).action(args);
		} else {
			((GXDLMSServer2) owner).onPreAction(args);
		}
	}

	/*
	 * Client has try to made invalid connection. Password is incorrect.
	 * 
	 * @param connectionInfo Connection info.
	 */
	final void notifyInvalidConnection(final GXDLMSConnectionEventArgs connectionInfo) throws Exception {
		if (owner instanceof GXDLMSServer) {
			((GXDLMSServer) owner).invalidConnection(connectionInfo);
		} else {
			((GXDLMSServer2) owner).onInvalidConnection(connectionInfo);
		}
	}

	/*
	 * Client has connected.
	 * 
	 * @param connectionInfo Connection info.
	 */
	final void notifyConnected(final GXDLMSConnectionEventArgs connectionInfo) throws Exception {
		if ((settings.getConnected() & ConnectionState.DLMS) != 0) {
			if (owner instanceof GXDLMSServer) {
				((GXDLMSServer) owner).connected(connectionInfo);
			} else {
				((GXDLMSServer2) owner).onConnected(connectionInfo);
			}
		}
	}

	public final int notifyGetAttributeAccess(final ValueEventArgs arg) throws Exception {
		if (owner instanceof GXDLMSServer) {
			GXAttributeCollection attributes = arg.getTarget().getAttributes();
			GXDLMSAttributeSettings att = attributes.find(arg.getIndex());
			/// If attribute is not set return read only.
			if (att == null) {
				return AccessMode.READ.getValue();
			}
			return att.getAccess().getValue();
		} else if (owner instanceof GXDLMSServer3) {
			if (getSettings().getAssignedAssociation().getVersion() < 3) {
				return ((GXDLMSServer3) owner).onGetAttributeAccess(arg).getValue();
			} else {
				return AccessMode3.toInteger(((GXDLMSServer3) owner).onGetAttributeAccess3(arg));
			}
		}
		if (arg.getIndex() == 1) {
			return AccessMode.READ.getValue();
		}
		return ((GXDLMSServer2) owner).onGetAttributeAccess(arg).getValue();
	}

	public final int notifyGetMethodAccess(final ValueEventArgs arg) throws Exception {
		if (owner instanceof GXDLMSServer) {
			GXAttributeCollection attributes = arg.getTarget().getMethodAttributes();
			GXDLMSAttributeSettings att = attributes.find(arg.getIndex());
			/// If attribute is not set return read only.
			if (att == null) {
				return MethodAccessMode.NO_ACCESS.getValue();
			}
			return att.getMethodAccess().getValue();
		} else if (owner instanceof GXDLMSServer3) {
			if (getSettings().getAssignedAssociation().getVersion() < 3) {
				return ((GXDLMSServer3) owner).onGetMethodAccess(arg).getValue();
			} else {
				return MethodAccessMode3.toInteger(((GXDLMSServer3) owner).onGetMethodAccess3(arg));
			}
		}
		return ((GXDLMSServer2) owner).onGetMethodAccess(arg).getValue();
	}

	/*
	 * Read selected item(s).
	 * 
	 * @param args Handled read requests.
	 */
	final void notifyPostRead(final ValueEventArgs[] args) throws Exception {
		if (owner instanceof GXDLMSServer2) {
			((GXDLMSServer2) owner).onPostRead(args);
		}
	}

	/*
	 * Write selected item(s).
	 * 
	 * @param args Handled write requests.
	 */
	final void notifyPostWrite(final ValueEventArgs[] args) throws Exception {
		if (owner instanceof GXDLMSServer2) {
			((GXDLMSServer2) owner).onPostWrite(args);
		}
	}

	/*
	 * Action is occurred.
	 * 
	 * @param args Handled action requests.
	 */
	public final void notifyPostAction(final ValueEventArgs[] args) throws Exception {
		if (owner instanceof GXDLMSServer2) {
			((GXDLMSServer2) owner).onPostAction(args);
		}
	}

	public final void notifyPreGet(final ValueEventArgs[] args) throws Exception {
		if (owner instanceof GXDLMSServer2) {
			((GXDLMSServer2) owner).onPreGet(args);
		} else if (owner instanceof GXDLMSServer) {
			((GXDLMSServer) owner).read(args);
		}
	}

	public final void notifyPostGet(final ValueEventArgs[] args) throws Exception {
		if (owner instanceof GXDLMSServer2) {
			((GXDLMSServer2) owner).onPostGet(args);
		}
	}

	/**
	 * @return HDLC settings.
	 */
	public GXDLMSHdlcSetup getHdlc() {
		return settings.getHdlc();
	}

	/**
	 * @param value HDLC settings.
	 */
	public void setHdlc(final GXDLMSHdlcSetup value) {
		settings.setHdlc(value);
	}
}
