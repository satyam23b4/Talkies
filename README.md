# Talkies - Chat Application

**Talkies** is a simple, yet powerful chat application designed for Android. It provides two main features: a "Chat with Friends" feature, using TCP protocol for communication, and a "Report to Server" feature, utilizing UDP protocol. This application leverages Firebase Firestore as the database to manage user data and interactions.

## Features

- **Chat with Friends**: Users can chat with their friends in real time using the TCP protocol. The app allows users to send messages to each other, with real-time updates and easy-to-use interfaces.
- **Report to Server**: Users can report any issues directly to the server using the UDP protocol. This allows for quick, efficient communication between users and the server for feedback, bug reports, or any other kind of support.
- **Firebase Integration**: Firebase Firestore is used as the backend database to store user data, chat history, and other application-related information. Firebase provides a real-time, scalable solution to manage the data for all users.

## Technical Details

### 1. **Communication Protocols**
   
   - **TCP Protocol (Chat with Friends)**:
     - TCP (Transmission Control Protocol) is used for the "Chat with Friends" feature. It ensures reliable communication by establishing a connection between the client and the server.
     - Messages are sent and received in a sequential manner, ensuring that every message arrives correctly and in order.
   
   - **UDP Protocol (Report to Server)**:
     - UDP (User Datagram Protocol) is used for the "Report to Server" feature. UDP is a connectionless protocol that is faster than TCP, making it ideal for sending brief messages like user reports without the overhead of connection management.
     - Users can send a problem report to the server, and the server receives the report without the need for a response.

### 2. **Database Integration**
   - **Firebase Firestore**:
     - Firebase Firestore is used to store and manage user data, messages, and any other information needed by the application.
     - It offers real-time data synchronization across devices, allowing messages to be instantly updated across all users' devices.

### 3. **Features in Detail**
   
   - **Chat with Friends (TCP-based Communication)**:
     - When a user selects the "Chat with Friends" option, they can select a friend and start a conversation.
     - The app uses a TCP client-server model to handle real-time messaging, ensuring that the communication between the client and server is consistent and secure.

   - **Report to Server (UDP-based Communication)**:
     - When a user wants to report a problem or send feedback, they can click the "Report to Server" button. This feature uses the UDP protocol to send messages to the server.
     - Since UDP is connectionless and does not require acknowledgment from the server, it's ideal for lightweight, quick communication.

## Technologies Used

- **Android Development**: The application is built using Android Studio and Kotlin, making it lightweight and responsive for mobile users.
- **TCP & UDP Protocols**: The core messaging features of the app utilize TCP and UDP for communication between the client and the server.
- **Firebase Firestore**: Firebase is used for database management, offering real-time synchronization, security, and easy scalability.


## Installation Instructions

To run the Talkies application locally, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/satyam23b4/Talkies.git
