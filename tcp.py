import socket
import threading

# List to store connected clients
clients = []
clients_lock = threading.Lock()

def handle_client(client_socket, addr):
    """
    Handles communication with a single client.
    """
    print(f"New connection: {addr}")
    try:
        while True:
            message = client_socket.recv(1024).decode('utf-8')
            if message:
                print(f"Received from {addr}: {message}")
                broadcast_message(message, client_socket)
            else:
                # Handle empty message or client disconnection
                break
    except Exception as e:
        print(f"Error with client {addr}: {e}")
    finally:
        remove_client(client_socket, addr)

def broadcast_message(message, sender_socket):
    """
    Sends the received message to all connected clients except the sender.
    """
    with clients_lock:
        for client in clients:
            if client != sender_socket:
                try:
                    client.send(message.encode('utf-8'))
                except Exception as e:
                    print(f"Error sending message to a client: {e}")
                    remove_client(client, None)

def remove_client(client_socket, addr):
    """
    Removes a client from the list and closes the connection.
    """
    with clients_lock:
        if client_socket in clients:
            clients.remove(client_socket)
    client_socket.close()
    if addr:
        print(f"Connection closed: {addr}")

def start_server():
    """
    Starts the server and listens for incoming connections.
    """
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind(('0.0.0.0', 9999))
    server.listen(5)
    print("Server started on port 9999...")

    try:
        while True:
            client_socket, addr = server.accept()
            with clients_lock:
                clients.append(client_socket)
            threading.Thread(target=handle_client, args=(client_socket, addr), daemon=True).start()
    except KeyboardInterrupt:
        print("\nServer shutting down...")
    except Exception as e:
        print(f"Server error: {e}")
    finally:
        with clients_lock:
            for client in clients:
                client.close()
        server.close()

if __name__ == "__main__":
    start_server()
