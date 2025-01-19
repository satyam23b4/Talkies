import socket

# Server Configuration
UDP_IP = "0.0.0.0"  # Listen on all available interfaces
UDP_PORT = 8888     # Port number (must match the client-side port)

# Create a UDP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

print(f"UDP server is running and listening on port {UDP_PORT}...")

try:
    while True:
        # Receive message from the client
        data, addr = sock.recvfrom(1024)  # Buffer size of 1024 bytes
        message = data.decode("utf-8")
        print(f"Received message from {addr}: {message}")

        # Optional: Send acknowledgment back to the client
        ack_message = "Message received"
        sock.sendto(ack_message.encode("utf-8"), addr)
except KeyboardInterrupt:
    print("\nServer is shutting down...")
finally:
    sock.close()
