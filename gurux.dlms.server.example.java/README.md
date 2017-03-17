See An [Gurux](http://www.gurux.org/ "Gurux") for an overview.

Join the Gurux Community or follow [@Gurux](http://twitter.com/guruxorg "@Gurux") for project updates.

Gurux.DLMS library for Java is a high-performance Java component that helps you to read DLMS/COSEM compatible electricity, gas or water meters. You can also use Gurux DLMS component to create own DLMS meter or broxy.

We have try to make component so easy to use that you do not need understand protocol at all.

For more info check out [Gurux.DLMS](http://www.gurux.fi/index.php?q=Gurux.DLMS "Gurux.DLMS").

We are updating documentation on Gurux web page. 

You should read [DLMS/COSEM FAQ](http://www.gurux.org/index.php?q=DLMSCOSEMFAQ) first to get started. Read Instructions for making your own [meter reading application](http://www.gurux.org/index.php?q=DLMSIntro) or build own 
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
  <version>2.0.1</version>
</dependency>
```

Simple example
=========================== 
First you must create server class and derive it from GXDLMSServerBase to add support for DLMS/COSEM protocol.
Then you must implement IGXMediaListener and yourself interfaces to add TCP/IP support.


```Java

public class GXDLMSExampleServer extends GXDLMSServerBase implements IGXMediaListener,
        gurux.net.yourself

```

First you must tell what objects meter offers.
You can also set default or static values here.

```Java
public void Initialize(int port) throws IOException
{
    ///////////////////////////////////////////////////////////////////////
    //Add Logical Device Name. 123456 is meter serial number.
    GXDLMSData d = new GXDLMSData("0.0.42.0.0.255");
    d.setValue("Gurux123456");
    //Set access right. Client can't change Device name.
    d.setAccess(2, AccessMode.READ);
    getItems().add(d);
}

```

Read method is called when client wants to read some data from the meter.
If you want that framework returns current value just set Handled = false. 
Otherwice you can just set value that you want to return. 
In this example we will return current time for the clock.
Otherwise we will return attribute value of the object.

```Java

@Override
public void read(ValueEventArgs e)
{
    if(e.getTarget() instanceof GXDLMSClock)
    {
        //Implement spesific clock handling here.    
        //Otherwice initial values are used.      
        if (e.getIndex() == 2)
        {
            e.setValue(java.util.Calendar.getInstance().getTime());                
            e.setHandled(true);
            return;
        }
    }
    e.setHandled(false);
}

```

Write method is called when client wants to write some data to the meter.
You can handle write by you self or let the framework handle it.

```Java

@Override
public void write(ValueEventArgs e)
{
    System.out.println(String.format("Client Write new value %1$s to object: %2$s.", 
        e.getValue(), e.getTarget().getName()));
}

```

Action method is called when client performs action like reset.
You can handle actions by you self or let the framework handle it.

```Java

@Override
public void action(ValueEventArgs e)
{
        
}

```

Main functionality is happening here. When client sends DLMS byte packet to the server
media component receives it and sends it to the onReceived method.
You should only send received data to the handleRequest. This method parses the data and 
handles all nesessary actions. In result method returns data that is send to the client.

Note! If clientID and server ID (Server and Client Address) do not match, we do not return anything.
This is for the security. Client can't try to find meters just polling differents IP addresses.

```

/*
*  Client has send data.
*/
@Override
public void onReceived(Object sender, ReceiveEventArgs e) 
{
    try
    {
        synchronized (this)
        {
            // System.out.println(toHex((byte[])e.getData()));
            byte[] reply = handleRequest((byte[])e.getData());
            //Reply is null if we do not want to send any data to the client.
            //This is done if client try to make connection with wrong server or client address.
            if (reply != null)
            {
                Media.send(reply, e.getSenderInfo());
            }
        }
    }
    catch (IOException ex)
    {
        System.out.println(ex.getMessage());
    }    
}

```

On error is called if media causes error.

```Java

@Override
public void onError(Object sender, RuntimeException ex) 
{
    System.out.println("Error has occurred:" + ex.getMessage());
}

```

When client is making connection onClientConnected method is called. 
You can example write log here.

```Java

/*
* Client has made connection.
*/
@Override
public void onClientConnected(Object sender, gurux.net.ConnectionEventArgs e) 
{
    System.out.println("Client Connected.");
}

```

When client is closing connection onClientDisconnected method is called. 
It is important that yo call reset method here to reset all connection settings.


```Java

/*
* Client has close connection.
*/
@Override
public void onClientDisconnected(Object sender, gurux.net.ConnectionEventArgs e) 
{
    //Reset server settings when connection closed.
    this.reset();
    System.out.println("Client Disconnected.");
}
```