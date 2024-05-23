import 'package:flutter/material.dart';
import 'package:web_socket_channel/web_socket_channel.dart'; // Use the web implementation

import 'utils/websocket_channel_stub.dart'
  if (dart.library.io) 'utils/websocket_channel_io.dart'
  if (dart.library.html) 'utils/websocket_channel_html.dart';

// import 'package:web_socket_channel/io.dart';
// import 'package:web_socket_channel/html.dart';

import 'dart:html' as html;

void main() {
  runApp(MyApp());
  if (html.Notification.permission != 'granted') {
    html.Notification.requestPermission().then((permission) {
      print('Notification permission: $permission');
    });
  }
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'WebSocket Echo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final TextEditingController _controller = TextEditingController();
  final _channel = createWebSocketChannel('ws://localhost:8080/ws/message/Pitbull_named_Universe_Destroyer');
  // final _channel = WebSocketChannel.connect(
  //   Uri.parse('wss://localhost:8080/ws/message/earth'),
  // );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('WebSocket Echo'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            TextField(
              controller: _controller,
              decoration: InputDecoration(labelText: 'Send a message'),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _sendMessage,
              child: Text('Send'),
            ),
            SizedBox(height: 20),
            StreamBuilder(
              stream: _channel.stream,
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  _showNotification(snapshot.data);
                }
                return Text(snapshot.hasData ? '${snapshot.data}' : '');
              },
            ),
          ],
        ),
      ),
    );
  }

  void _sendMessage() {
    if (_controller.text.isNotEmpty) {
      _channel.sink.add(_controller.text);
    }
  }

  void _showNotification(String? message) {
    if (html.Notification.permission == 'granted' && message != null) {
      html.Notification("New Message", body: message);
    }
  }

  @override
  void dispose() {
    _channel.sink.close();
    super.dispose();
  }
}
