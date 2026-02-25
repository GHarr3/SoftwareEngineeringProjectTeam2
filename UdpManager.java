
package com.gamemaster.udp;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class UdpManager {
    private final DatagramSocket sendSocket;
    private final DatagramSocket recvSocket;
    private final DatagramSocket recvSocket7500;

    private InetAddress networkAddress;

    private final int broadcastPort = 7500;
    private final int receivePort = 7501;
    

        //Changed Constructor have no parameters and automatically initialize to default network
    public UdpManager() throws SocketException, UnknownHostException {
        // set initial network address
        String initialNetworkAddress = "127.0.0.1";
        this.networkAddress = InetAddress.getByName(initialNetworkAddress);

        this.sendSocket = new DatagramSocket();
        this.sendSocket.setBroadcast(true);

        // 7500 for broadcasting messages to the network
        this.recvSocket7500 = new DatagramSocket(null);
        this.recvSocket7500.setReuseAddress(true);
        this.recvSocket7500.bind(new InetSocketAddress("0.0.0.0", broadcastPort)); 

        // 7501 for receiving player IDs from the network
        this.recvSocket = new DatagramSocket(null);
        this.recvSocket.setReuseAddress(true);
        this.recvSocket.bind(new InetSocketAddress("0.0.0.0", receivePort));

    }

    public InetAddress getNetworkAddress()
    {

        return this.networkAddress;

    }



    // set the network address to send broadcasts to
    public synchronized void setNetworkAddress(String addr) throws UnknownHostException {
        this.networkAddress = InetAddress.getByName(addr);
        
    }

    // broadcast an integer code to the network address on port 7500
    public void broadcastInt(int code) throws IOException 
    {
        final InetAddress dest;
        // capture under lock
        synchronized (this) {
            // use current network address
            dest = this.networkAddress; 
        }
        // prepare and send packet
        byte[] buf = Integer.toString(code).getBytes(StandardCharsets.UTF_8);

        DatagramPacket p = new DatagramPacket(buf, buf.length, dest, broadcastPort);
        sendSocket.send(p);
    }

    public void startReceiverLoop(Consumer<String> onMessage) {
        startLoop(recvSocket, "udp-receiver-7501", onMessage);
        startLoop(recvSocket7500, "udp-receiver-7500", onMessage);
    }

    private void startLoop(DatagramSocket socket, String name, Consumer<String> onMessage) {
        Thread t = new Thread(() -> {
            byte[] buf = new byte[1024];
            while (!socket.isClosed()) {
                try {
                    DatagramPacket p = new DatagramPacket(buf, buf.length);
                    socket.receive(p);
                    String msg = new String(p.getData(), p.getOffset(), p.getLength(), StandardCharsets.UTF_8).trim();
                    onMessage.accept(msg);
                } catch (IOException e) {
                    if (!socket.isClosed()) e.printStackTrace();
                }
            }
        }, name);
        t.setDaemon(true);
        t.start();
    }

    // close sockets 
    public void close() {
        recvSocket.close();
        recvSocket7500.close();
        sendSocket.close();
    }
}
