//package com.miaoshaproject.miaosha;
//
//import net.sf.json.JSONObject;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class AppAddTest {
//    public static final String ADD_URL = "http://localhost:8090/user/getotp";
//    public static void appadd() {
//        try {
//            URL url = new URL(ADD_URL);
//            HttpURLConnection connection = (HttpURLConnection) url
//                    .openConnection();
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.setRequestMethod("GET");
//            connection.setUseCaches(false);
//            connection.setInstanceFollowRedirects(true);
//            connection.setRequestProperty("connection", "Keep-Alive");
//            //connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
//            connection.connect();
//            //POST请求
//            DataOutputStream out = new DataOutputStream(
//                    connection.getOutputStream());
//            JSONObject obj = new JSONObject();
//            String message = java.net.URLEncoder.encode("哈哈哈","utf-8");
//            obj.element("tel", "12345678123");
////            obj.element("TEXT1", "asd");
////            obj.element("TEXT2", message);
//
//            out.writeBytes("data="+obj.toString());
//            System.out.println("data="+obj.toString());
//            out.flush();
//            out.close();
//            //读取响应
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String lines;
//            StringBuffer sb = new StringBuffer("");
//            while ((lines = reader.readLine()) != null) {
//                lines = new String(lines.getBytes(), "utf-8");
//                sb.append(lines);
//            }
//            System.out.println(sb);
//            reader.close();
//            connection.disconnect();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void main(String[] args) {
//        appadd();
//    }
//
//}
