package com.uohe;


import com.alibaba.fastjson.JSONObject;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.javafx.DefaultNetworkDelegate;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import java.awt.*;
import javax.swing.*;
import java.util.Map;

public class Robot {

    public static void main(String[] args) {

        final String url = "";
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);
        browser.loadURL(url);
        getURL(browser);

    }




    /**
     * 轮询获取滑块子页面url.  直到获取之后执行下一步
     * @param browser
     */
    public static void getURL(Browser browser){

        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                    if (event.getValidatedURL().contains("https://t.captcha.qq.com/cap_union_new_show?aid=16")) {
                        event.getBrowser().loadURL(event.getValidatedURL());
                        monitoringNetwork();//获取url，cookie
                    }
                if (event.isMainFrame()) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    Browser browser = event.getBrowser();

                    browser.executeJavaScript(" var f=document.getElementById(\"tcaptcha_drag_thumb\");\n" +
                            "    var e=document.createEvent(\"MouseEvents\");\n" +
                            "    e.initMouseEvent('mousedown',true,true,window,1,1,1,1,1,false,false,true,false,0,null);\n" +
                            "\n" +
                            "    f.dispatchEvent(e);");


                    browser.executeJavaScript("var f1=document.getElementById(\"tcaptcha_drag_thumb\");\n" +
                            "    var e1=document.createEvent(\"MouseEvents\");\n" +
                            "    e1.initMouseEvent('mousemove',true,true,window,1,1,1,190,1,false,false,true,false,0,null);\n" +
                            "\n" +
                            "    f1.dispatchEvent(e1);");


                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    browser.executeJavaScript("    var f1=document.getElementById(\"tcaptcha_drag_thumb\");\n" +
                            "    var e1=document.createEvent(\"MouseEvents\");\n" +
                            "    e1.initMouseEvent('mouseup',true,true,window,1,1,1,190,1,false,false,true,false,0,null);\n" +
                            "\n" +
                            "    f1.dispatchEvent(e1);\n");



                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }



                }

            }
        });

    }




    /**
     *  常用函数setNetworkDelegate，包含对网络传输数据状态的多种监控回调。
     *
     */
    public static void monitoringNetwork(){
        BrowserContext browserContext = BrowserContext.defaultContext();
        NetworkService networkService = browserContext.getNetworkService();

        networkService.setNetworkDelegate(new DefaultNetworkDelegate() {

            @Override
            public void onDataReceived(DataReceivedParams params) {
//                String url = params.getURL(); //针对某些特定url进行处理
                //可以在这里对某些感兴起的数据进行收集
                ByteToAll byteToAll=new ByteToAll();
                if (params.getMimeType().equals("application/json") || params.getMimeType().equals("text/html")) {
                    String data=(byteToAll.byteToText(params.getData()));
                    if(data.contains("errorCode")){
                        if(data.contains("\"errorCode\":\"0\"")){
                            Map map= (Map) JSONObject.parse(params.getData());
                            System.out.println(map);
                        }else {
                            System.out.println("{\"errorCode\":\"-1\",\"errMessage\":\"hkerrer\"}");
                        }
                    }

                }
            }


        });
    }



}
