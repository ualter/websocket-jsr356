package ujr.websocket.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import ujr.websocket.model.Message;

//@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoderJson.class, encoders = MessageEncoderJson.class)
//@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoderString.class, encoders = MessageEncoderString.class)
public class ChatEndpoint {

	private Session session;
	private static Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
	private static HashMap<String, String> users = new HashMap<>();

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException {
		this.session = session;
		chatEndpoints.add(this);
		users.put(session.getId(), username);

		Message message = new Message();
		message.setFrom(username);
		message.setContent("Connected!");
		
		try {
			broadcast(message);
		} catch (EncodeException e) {
			throw new RuntimeException(e);
		}
	}

	/*@OnMessage
	public void onMessage(Session session, String message) {
		try {
			broadcast(message);
		} catch (EncodeException | IOException e) {
			throw new RuntimeException(e);
		}
	}*/
	
	@OnMessage
	public void onMessage(Session session, Message message) throws IOException {
		message.setFrom(users.get(session.getId()));
		
		try {
			broadcast(message);
		} catch (EncodeException e) {
			throw new RuntimeException(e);
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		chatEndpoints.remove(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");
        
        try {
			broadcast(message);
		} catch (EncodeException e) {
			throw new RuntimeException(e);
		}
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		throw new RuntimeException(throwable);
	}

	private static void broadcast(Message message) throws IOException, EncodeException {
		chatEndpoints.forEach(endpoint -> {
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
