See An [Gurux](http://www.gurux.org/ "Gurux") for an overview.

Join the Gurux Community or follow [@Gurux](https://twitter.com/guruxorg "@Gurux") for project updates.

Gurux.DLMS library for Java is a high-performance Java component that helps you to read you DLMS/COSEM compatible electricity, gas or water meters. We have try to make component so easy to use that you do not need understand protocol at all.

For more info check out [Gurux.DLMS](http://www.gurux.fi/index.php?q=Gurux.DLMS "Gurux.DLMS").

We are updating documentation on Gurux web page. 

Read should read [DLMS/COSEM FAQ](http://www.gurux.org/index.php?q=DLMSCOSEMFAQ) first to get started. Read Instructions for making your own [meter reading application](http://www.gurux.org/index.php?q=DLMSIntro) or build own 
DLMS/COSEM [meter/simulator/proxy](http://www.gurux.org/index.php?q=OwnDLMSMeter).

If you have problems you can ask your questions in Gurux [Forum](http://www.gurux.org/forum).

You do not necessary need to use Gurux media component like gurux.net.java or gurux.serial.java 
You can use any connection library you want to.
Gurux.DLMS classes only parse the data.

You can get source codes from http://www.github.com/gurux/gurux.dlms.java or if you use Maven add this to your POM-file:
```java
<dependency>
  <groupId>org.gurux</groupId>
  <artifactId>gurux.dlms</artifactId>
  <version>2.1.7</version>
</dependency>
```
Import new Project selecting "Existing Maven Project". Import examples separately. If you import only main Project, you can't debug or run examples. 

Simple example
=========================== 
Before use you must set following device parameters. 
Parameters are manufacturer specific.


```Java

GXDLMSClient client = new GXDLMSClient();

// Is used Logical Name or Short Name referencing.
client.setUseLogicalNameReferencing(true);

// Is used HDLC or COSEM transport layers for IPv4 networks (WRAPPER).
client.setInterfaceType(InterfaceType.HDLC);

// Read http://www.gurux.org/dlmsAddress
// to find out how Client and Server addresses are counted.
// Some manufacturers might use own Server and Client addresses.

client.setClientAddress(16);
client.setServerAddress(1);

```


After you have set parameters you can try to connect to the meter.
First you should send SNRM request and handle UA response.
After that you will send AARQ request and handle AARE response.


```Java

GXReplyData reply = new GXReplyData();
byte[] data;
data = client.SNRMRequest();
if (data != null)
{
    readDLMSPacket(data, reply);
    //Has server accepted client.
    client.parseUAResponse(reply.getData());
}

//Generate AARQ request.
//Split requests to multiple packets if needed. 
//If password is used all data might not fit to one packet.
for (byte[] it : client.AARQRequest())
{
    reply.clear();
    readDLMSPacket(it, reply);
}
//Parse reply.
client.parseAAREResponse(reply.getData());

```

If parameters are right connection is made.
Next you can read Association view and show all objects that meter can offer.

```Java
/// Read Association View from the meter.
GXReplyData reply = new GXReplyData();
readDataBlock(client.getObjects(), reply);
GXDLMSObjectCollection objects = client.parseObjects(reply.getData(), true);

```
Now you can read wanted objects. After read you must close the connection by sending
disconnecting request.

```Java
GXReplyData reply = new GXReplyData();
readDLMSPacket(client.disconnectRequest(), reply);
Media.close();

```

```Java

/**
* Read DLMS Data from the device.
* If access is denied return null.
*/
public void readDLMSPacket(byte[] data, GXReplyData reply)
        throws Exception {
    if (data == null || data.length == 0) {
        return;
    }
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
    p.setAllData(true);
    p.setEop(eop);
    p.setCount(5);
    p.setWaitTime(WaitTime);
    synchronized (Media.getSynchronous()) {
        while (!succeeded) {
            writeTrace("<- " + now() + "\t" + GXCommon.toHex(data));
            Media.send(data, null);
            if (p.getEop() == null) {
                p.setCount(1);
            }
            succeeded = Media.receive(p);
            if (!succeeded) {
                // Try to read again...
                if (pos++ != 3) {
                    System.out.println("Data send failed. Try to resend "
                            + pos.toString() + "/3");
                    continue;
                }
                throw new RuntimeException(
                        "Failed to receive reply from the device in given time.");
            }
        }
        // Loop until whole DLMS packet is received.
        while (!dlms.getData(p.getReply(), reply)) {
            if (p.getEop() == null) {
                p.setCount(1);
            }
            if (!Media.receive(p)) {
                throw new Exception(
                        "Failed to receive reply from the device in given time.");
            }
        }
    }
    writeTrace("-> " + now() + "\t" + GXCommon.toHex(p.getReply()));
    if (reply.getError() != 0) {
        throw new GXDLMSException(reply.getError());
    }
}

```

Using authentication


When authentication (Access security) is used server(meter) can allow different rights to  the client.
Example without authentication (None) only read is allowed.
Gurux DLMS component supports five different authentication level:

+ None
+ Low
+ High
+ HighMD5
+ HighSHA1

In default Authentication level None is used. If other level is used password or secure must also give.
Used password depends from the meter.

```Java
client.setAuthentication(Authentication.HIGH_MD5);
client.setPassword("12345678".GetBytes("ASCII"));
``` 

When authentication is High or above High Level security (HLS) is used.
After connection is made client must send challenge to the server and server must accept this challenge.
This is done checking is Is Authentication Required after AARE message is parsed.
If authentication is required client sends challenge to the server and if everything succeeded
server returns own challenge that client checks.

```Java
//Parse reply.
client.parseAAREResponse(reply.getData());
//Get challenge Is HSL authentication is used.
if (client.IsAuthenticationRequired)
{
    reply.clear();
    readDLMSPacket(client.getApplicationAssociationRequest(), reply);
    client.parseApplicationAssociationResponse(reply.getData());
}
``` 

Writing values

Writing values to the meter is very simple. There are two ways to do this. 
First is using Write -method of GXDLMSClient.

```Java
readDLMSPacket(client.write("0.0.1.0.0.255", dateTime, 2, DataType.OCTET_STRING, ObjectType.CLOCK, 2), reply);
``` 


Note!
Data type must be correct or meter returns usually error.
If you are reading byte value you can't write UIn16.

It is easy to write simple data types like this. If you want to write complex data types like arrays there
is also another way to do this. You can Update Object's propery and then write it.
In this example we want to update listening window of GXDLMSAutoAnswer object.

```Java
//Read Association view and find GXDLMSAutoAnswer object first.
GXDLMSAutoAnswer item = client.getObject().findByLN("0.0.2.2.0.255", ObjectType.AUTO_ANSWER);
//Window time is from 6am to 8am.
item.getListeningWindow().add(new AbstractMap.SimpleEntry<GXDateTime, 
                GXDateTime>(new GXDateTime(-1, -1, -1, 6, -1, -1, -1), 
                new GXDateTime(-1, -1, -1, 8, -1, -1, -1)));
readDLMSPacket(client.write(item, 3), reply);
``` 

Transport security

DLMS supports tree different transport security. When transport security is used each packet is secured using GMAC security. Security level are:
* Authentication
* Encryption
* AuthenticationEncryption

Using secured messages is easy. Before security can be used following properties must set:
* Security
* SystemTitle
* AuthenticationKey
* BlockCipherKey
* FrameCounter

If we want communicate with Gurux DLMS server you just need to set the following settings.
Note! You must use GXDLMSSecureClient not GXDLMSClient.


```Java
GXDLMSSecureClient sc = new GXDLMSSecureClient();
sc.getCiphering().setSecurity(Security.ENCRYPTION);
//Default security when using Gurux test server.
sc.getCiphering().setSystemTitle("GRX12345".GetBytes("ASCII");

```
