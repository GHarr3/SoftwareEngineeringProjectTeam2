import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class UdpManager {
    private final DatagramSocket sendSocket;
    private final DatagramSocket recvSocket;

    private InetAddress networkAddress;

    private final int broadcastPort = 7500;
    private final int receivePort = 7501;


    public UdpManager(String initialNetworkAddress) throws SocketException, UnknownHostException {
        // set initial network address
        this.networkAddress = InetAddress.getByName(initialNetworkAddress);

        // sender (to 7500)
        // to enable broadcast
        this.sendSocket = new DatagramSocket();
        this.sendSocket.setBroadcast(true);

        // receiver (from any IP on 7501)
        // to enable recieving 
        this.recvSocket = new DatagramSocket(null);
        this.recvSocket.setReuseAddress(true);

        // 0.0.0.0 ip binds to all interfaces
        this.recvSocket.bind(new InetSocketAddress("0.0.0.0", receivePort));
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

    // start a loop to receive packets on port 7501
    // to use this, (msg -> { ... }) is passed as onMessage parameter
    public void startReceiverLoop(Consumer<String> onMessage) {
        // start a new thread for receiving packets
        Thread t = new Thread(() -> {
            // buffer for receiving packets
            byte[] buf = new byte[1024];
            while (!recvSocket.isClosed()) 
            {
                try {
                    DatagramPacket p = new DatagramPacket(buf, buf.length);
                    recvSocket.receive(p);

                    //get message 
                    String msg = new String(p.getData(), p.getOffset(), p.getLength(), StandardCharsets.UTF_8).trim();

                    // this uses java.util.function.Consumer 
                    onMessage.accept(msg);
                } catch (IOException e) {
                    // ignore exceptions if socket is closed
                    if (!recvSocket.isClosed()) e.printStackTrace();
                }
            }
        }, "udp-receiver-7501");

        // make daemon so it doesn't block JVM exit
        t.setDaemon(true);

        // start the loop
        t.start();
    }

    // close sockets 
    public void close() {
        recvSocket.close();
        sendSocket.close();
    }
}
