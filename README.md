See An [Gurux](http://www.gurux.org/ "Gurux") for an overview.

Join the Gurux Community or follow [@Gurux](https://twitter.com/guruxorg "@Gurux") for project updates.

Gurux.DLMS library for Java is a high-performance Java component that helps you to read you DLMS/COSEM compatible electricity, gas or water meters. We have try to make component so easy to use that you do not need understand protocol at all.

For more info check out [Gurux.DLMS](http://www.gurux.fi/index.php?q=Gurux.DLMS "Gurux.DLMS").

We are updating documentation on Gurux web page. 

Read should read [DLMS/COSEM FAQ](http://www.gurux.org/index.php?q=DLMSCOSEMFAQ) first to get started. Read Instructions for making your own [meter reading application](http://www.gurux.org/index.php?q=DLMSIntro) or build own 
DLMS/COSEM [meter/simulator/proxy](http://www.gurux.org/index.php?q=OwnDLMSMeter).

If you have problems you can ask your questions in Gurux [Forum](http://www.gurux.org/forum).

You do not nesessary need to use Gurux media component like Gurux.Net. 
You can use any connection library you want to.
Gurux.DLMS classes only parse the data.


Simple example
=========================== 
Before use you must set following device parameters. 
Parameters are manufacturer spesific.


```Java

GXDLMSClient client = new GXDLMSClient();

// Is used Logican Name or Short Name referencing.
client.setUseLogicalNameReferencing(true);

// Is used HDLC or COSEM transport layers for IPv4 networks
client.setInterfaceType(InterfaceType.GENERAL);

// Read http://www.gurux.fi/index.php?q=node/336 
// to find out how Client and Server addresses are counted.
// Some manufacturers might use own Server and Client addresses.

client.setClientID((byte) 0x21);
client.setServerID((byte) 0x3);

```


After you have set parameters you can try to connect to the meter.
First you should send SNRM request and handle UA response.
After that you will send AARQ request and handle AARE response.


```Java

byte[] data, reply = null;
data = client.SNRMRequest();
if (data != null)
{
    reply = readDLMSPacket(data);
    //Has server accepted client.
    client.parseUAResponse(reply);
}

//Generate AARQ request.
//Split requests to multiple packets if needed. 
//If password is used all data might not fit to one packet.
for (byte[] it : client.AARQRequest(null))
{
    reply = readDLMSPacket(it);
}
//Parse reply.
client.parseAAREResponse(reply);

```

If parameters are right connection is made.
Next you can read Association view and show all objects that meter can offer.

```Java
/// Read Association View from the meter.
byte[] reply = readDataBlock(client.getObjects());
GXDLMSObjectCollection objects = client.parseObjects(reply, true);

```
Now you can read wanted objects. After read you must close the connection by sending
disconnecting request.

```Java
readDLMSPacket(m_Parser.disconnectRequest());
Media.close();

```

```Java

/*
 * Read DLMS Data from the device.
 * If access is denied return null.
 */
public byte[] readDLMSPacket(byte[] data) throws Exception
{
    if (data == null || data.length == 0)
    {
        return null;
    }
    Object eop = (byte) 0x7E;
    //In network connection terminator is not used.
    if (dlms.getInterfaceType() == InterfaceType.NET && Media instanceof GXNet)
    {
        eop = null;
    }
    int pos = 0;
    boolean succeeded = false;
    ReceiveParameters<byte[]> p = new ReceiveParameters<>(byte[].class);
    p.setAllData(true);
    p.setEop(eop);
    p.setCount(5);
    p.setWaitTime(WaitTime);        
    synchronized (Media.getSynchronous())
    {
        while (!succeeded && pos != 3)
        {
            if (Trace)
            {   
                System.out.println("<- " + GXDLMSClient.toHex(data));
            }
            Media.send(data, null);
            succeeded = Media.receive(p);
            if (!succeeded)
            {
                throw new Exception("Failed to receive reply from the device in given time.");
            }
        }
        //Loop until whole Cosem packet is received.                
        while (!dlms.isDLMSPacketComplete(p.getReply()))
        {
            if (p.getEop() == null)
            {
                p.setCount(1);
            }
            if (!Media.receive(p))
            {
                throw new Exception("Failed to receive reply from the device in given time.");
            }
        }
    }    
    Object[][] errors = dlms.checkReplyErrors(data, p.getReply());
    if (errors != null)
    {
        throw new GXDLMSException((int) errors[0][0]);
    }
    return p.getReply();       
}

```

Using authentication


When authentication (Access security) is used server(meter) can allow different rights to  the client.
Example without authentication (None) only read is allowed.
Gurux DLMS component supports five different authentication level:

* None
* Low
* High
* HighMD5
* HighSHA1

In default Authentication level None is used. If other level is used password must also give.
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
client.parseAAREResponse(reply);
//Get challenge Is HSL authentication is used.
if (client.IsAuthenticationRequired)
{
    reply = readDLMSPacket(client.getApplicationAssociationRequest());
    client.parseApplicationAssociationResponse(reply);
}
``` 

Writing values

Writing values to the meter is very simple. There are two ways to do this. 
First is using Write -method of GXDLMSClient.

```Java
readDLMSPacket(client.write("0.0.1.0.0.255", dateTime, 2, DataType.OCTET_STRING, ObjectType.CLOCK, 2));
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
readDLMSPacket(client.write(item, 3));
``` 