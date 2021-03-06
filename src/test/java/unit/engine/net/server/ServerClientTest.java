package unit.engine.net.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.junit.Before;
import org.junit.Test;
import net.whg.we.net.IDataHandler;
import net.whg.we.net.IPacket;
import net.whg.we.net.ISocket;
import net.whg.we.net.server.ServerClient;

public class ServerClientTest
{
    private final String IP_VALUE = "192.168.1.5";
    private final int PORT = 3445;
    private ISocket socket;
    private IDataHandler dataHandler;

    @Before
    public void init() throws IOException
    {
        var inputStream = mock(InputStream.class);
        var outputStream = mock(OutputStream.class);

        socket = mock(ISocket.class);
        when(socket.getIP()).thenReturn(IP_VALUE);
        when(socket.getPort()).thenReturn(PORT);
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);

        dataHandler = mock(IDataHandler.class);
    }

    @Test
    public void getConnectionTest() throws IOException
    {
        var client = new ServerClient(socket, dataHandler);
        var connection = client.getConnection();

        assertEquals(IP_VALUE, connection.getIP());
        assertEquals(PORT, connection.getPort());
    }

    @Test
    public void kickClient() throws IOException
    {
        var client = new ServerClient(socket, dataHandler);

        client.kick();

        verify(socket).close();
    }

    @Test
    public void readPacket() throws IOException
    {
        var client = new ServerClient(socket, dataHandler);

        var packet = mock(IPacket.class);
        when(dataHandler.readPacket(any(), any())).thenReturn(packet);

        assertEquals(packet, client.readPacket());
    }

    @Test
    public void readPacket_socketClosed() throws IOException
    {
        var client = new ServerClient(socket, dataHandler);

        when(dataHandler.readPacket(any(), any())).thenThrow(new SocketException());

        assertThrows(SocketException.class, () -> client.readPacket());
    }

    @Test
    public void writePacket() throws IOException
    {
        var client = new ServerClient(socket, dataHandler);

        var packet = mock(IPacket.class);
        client.sendPacket(packet);

        verify(dataHandler).writePacket(any(), eq(packet));
    }

    @Test
    public void writePacket_socketClosed() throws IOException
    {
        var client = new ServerClient(socket, dataHandler);

        doThrow(SocketException.class).when(dataHandler)
                                      .writePacket(any(), any());

        var packet = mock(IPacket.class);
        assertThrows(SocketException.class, () -> client.sendPacket(packet));
    }
}
