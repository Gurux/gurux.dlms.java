See An [Gurux](http://www.gurux.org/ "Gurux") for an overview.

Join the Gurux Community or follow [@Gurux](https://twitter.com/guruxorg "@Gurux") for project updates.

Gurux.DLMS library for Java is a high-performance Java component that helps you to read DLMS/COSEM compatible electricity, gas or water meters. We have try to make component so easy to use that you do not need understand protocol at all.

For more info check out [Gurux.DLMS](http://www.gurux.fi/index.php?q=Gurux.DLMS "Gurux.DLMS").

We are updating documentation on [Gurux web page](http://www.gurux.fi/index.php?q=Gurux.DLMS.Simulator "Gurux web page").

Read should read [DLMS/COSEM FAQ](https://www.gurux.org/index.php?q=DLMSCOSEMFAQ) first to get started. 

If you have problems you can ask your questions in Gurux [Forum](https://www.gurux.org/forum).

Purpose of Gurux.DLMS.Simulator is help you make testing environment without huge number of real meter.
You need only one meter to get started. There is also one simulator template in this project (crystal.xml).
You can use it to get started. 

Usage
=========================== 
First you need to read values from real meter. You use meter values to make simulator template.
You create simulator template running Gurux.DLMS.Simulator. In this example meter is connected to serial port 8.

```java

Gurux.DLMS.Simulator.Net.exe -S COM8 -o simulator_template.xml -t Verbose

```

This might take some time because all the values are read from the meter. After you have create the simulator file you can start simulator.

```java

Gurux.DLMS.Simulator.Net.exe -p 1000 -x simulator_template.xml -t Verbose -N 10

```
If you want to that all the meters are using the same TCP/IP port you can use -X parameter.
You need to use serial number to read the meter if you are using the same TCP/IP port.

```java

Gurux.DLMS.Simulator.Net.exe -p 1000 -x simulator_template.xml -t Verbose -N 10000 -X

```

Modifying values.
=========================== 
Because needs are so different when simulating meters, there is only a few basic values are updated. Everything else are static. You can see them in UpdateValues function.
If you need only meters to read that should be enough for you. 
Default functionality updates COSEM Logical Device Name and meter serial number. Serial number is a one-based number the is increased by one.

You can expand the functionality of your needs.
You can read [what each method does](https://gurux.fi/Gurux.DLMS.Server).
