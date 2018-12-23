package com.example.demo;

import okhttp3.*;
import okhttp3.internal.ws.RealWebSocket;
import okio.ByteString;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args){
        new Client().run();
    }
    WebSocket mSocket;
    public void run(){
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()

                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间

                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间

                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .pingInterval(3,TimeUnit.SECONDS)
                .build();



        Request request = new Request.Builder().url("ws://127.0.0.1:7397/websocket?request=1,2").build();

        EchoWebSocketListener socketListener = new EchoWebSocketListener();

        mOkHttpClient.newWebSocket(request, socketListener);

        mOkHttpClient.dispatcher().executorService().shutdown();
    }

    private final class EchoWebSocketListener extends WebSocketListener {



        @Override

        public void onOpen(WebSocket webSocket, Response response) {

            super.onOpen(webSocket, response);

            mSocket = webSocket;


            String openid = "1";

            //连接成功后，发送登录信息

            String message = "{\"type\":\"login\",\"user_id\":\""+openid+"\"}";

            mSocket.send(message);

            output("连接成功！");



        }



        @Override

        public void onMessage(WebSocket webSocket, ByteString bytes) {

            super.onMessage(webSocket, bytes);

            output("receive bytes:" + bytes.hex());
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {

                @Override

                public void run() {



                }

            },25000);
        }



        @Override

        public void onMessage(WebSocket webSocket, String text) {

            super.onMessage(webSocket, text);

            output("receive text:" + text);
        }



        @Override

        public void onClosed(WebSocket webSocket, int code, String reason) {

            super.onClosed(webSocket, code, reason);

            output("closed:" + reason);

        }



        @Override

        public void onClosing(WebSocket webSocket, int code, String reason) {

            super.onClosing(webSocket, code, reason);

            output("closing:" + reason);

        }



        @Override

        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

            super.onFailure(webSocket, t, response);

            output("failure:" + t.getMessage());

        }

    }

    private void output(String s) {
        System.out.println(s);
    }
}
