package ujr.websocket.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/xplane/", decoders = MessageDecoderString.class, encoders = MessageEncoderString.class)
public class XPlaneEndpoint {

	private Session session;
	private static Set<XPlaneEndpoint> mappersEndpoints = new CopyOnWriteArraySet<>();

	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
		mappersEndpoints.add(this);
		try {
			broadcast("Connected Id:" + session.getId());
		} catch (EncodeException e) {
			throw new RuntimeException(e);
		}
	}

	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		System.out.println(session.getId() + " sent message " + message);
		try {
			broadcast(message);
		} catch (EncodeException e) {
			throw new RuntimeException(e);
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		mappersEndpoints.remove(this);
		System.out.println(session.getId() + " disconnets");
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		throw new RuntimeException(throwable);
	}

	private static void broadcast(String message) throws IOException, EncodeException {
		mappersEndpoints.forEach(endpoint -> {
			synchronized (endpoint) {
				try {
					endpoint.session.getBasicRemote().sendObject(message);
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*private static void broadcast(String message) throws IOException, EncodeException {
		chatEndpoints.forEach(endpoint -> {
			synchronized (endpoint) {
				try {
					endpoint.session.getBasicRemote().sendObject(message);
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
			}
		});
	}*/

}
