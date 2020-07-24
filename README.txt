Group 73
Omer Cavdar e2171478
Bora Bengu e2171346

README:

Description:
This program performs UDP based reliable data transfer (RDT) with multi homing and pipelining. Unfortunately it cannot detect broken links.

These programs are written in java, source files are in src folder.

Fundamental logic:

There are three kinds of nodes: transmitters, receivers, routers.

S is the source node that forms packets from 'input.txt' file and sends the packets to D node via R1, R2 and R3.

R1, R2 and R3 nodes seve as a router from S to D for data messages and from D to S for acknowledge messages.

D is the destination node that gets the packets from S node via R1, R2 and R3 and forms 'output.txt' file from packets.

Problem with 'output.txt' file:

Only ASCII characters are copied to 'Output.txt' file and question mark '?' is copied for non-ASCII characters. The main reason is getting strings from byte arrays are performed with 'UTF-8' and we could not find a way to show the non-ASCII characters although we can successfully transfer them.

How to run:
1. Send the src folder (also contains 'input.txt' file) to ALL NODES, they will all have all codes but they will execute their own main class.
2. Compile them using javac, IN ALL NODES
3. Prepare to be quick, do following things in THAT ORDER
4. Run 'java DMain' from D
5. Run 'java R1Main' from R1
6. Run 'java R2Main' from R2
7. Run 'java R3Main' from R3
8. Run 'java SMain' from S
9. The prints are explanatory, wait till 'java SMain' from S is executed.
10. The 'output.txt' file is created in the same folder with 'input.txt', so you can compare them.
11. The main methods of routers (R1, R2 and R3) since it is logical to make them awake after the transmission. They should be terminated manually.
