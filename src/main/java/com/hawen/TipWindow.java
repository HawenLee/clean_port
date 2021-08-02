package com.hawen;

import com.sun.awt.AWTUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;


public class TipWindow {
    private static Logger log = LogManager.getLogger(TipWindow.class);

    JFrame frame;
    JLabel label;
    JEditorPane editorPane;

    private int width;//宽
    private int height;//高
    private int stayTime;//停留时间
    private String title;//标题
    private String message;//显示内容
    private int style;//窗体样式

    static{
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Throwable throwable){
            log.error(throwable.getMessage(),throwable);
        }
    }

    public TipWindow(int width, int height, int stayTime, int style, String title, String message) {
        this.width = width;
        this.height = height;
        this.stayTime = stayTime;
        this.title = title;
        this.message = message;
        this.style = style;
    }

    public TipWindow(int stayTime, int style, String title, String message) {
        this.width = 300;
        this.height = 100;
        this.stayTime = stayTime;
        this.title = title;
        this.message = message;
        this.style = style;
    }

    //窗体逐渐清晰
    private void show(){
        for(int i=1;i<20;i++){
            try{
                Thread.sleep(50);
            }catch (Throwable throwable){
                AWTUtilities.setWindowOpacity(frame,i * 0.05F);
            }
        }
    }

    private void hide(){
        float opacity = 100;
        while (true){
            if(opacity < 2){
                break;
            }
            opacity -= 2;
            AWTUtilities.setWindowOpacity(frame,opacity / 100);
            try{
                Thread.sleep(150);
            } catch (Throwable throwable){
                log.error(throwable.getMessage(),throwable);
            }
        }
        frame.dispose();
    }

    private void initializa(){
        frame = new JFrame();
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText(message);
        frame.add(editorPane);
        frame.setTitle(title);
        Point location = MouseInfo.getPointerInfo().getLocation();
        frame.setBounds((int)location.getX(),(int)location.getY(),width,height);
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(style);
        AWTUtilities.setWindowOpacity(frame,0);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);//窗体置顶
        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hide();
            }
        });
    }

    public void run(){
        initializa();
        show();
        try{
            Thread.sleep(stayTime * 100);
        } catch (Throwable throwable){
            log.error(throwable.getMessage(),throwable);
        }
        hide();
    }

    public static void showMessageInDesktop(String title,String message) {
        log.info("start show tip window......");
        TipWindow tipWindow = new TipWindow(2,JRootPane.QUESTION_DIALOG, title,message);
        tipWindow.run();
        log.info("stop show tip window");
    }
}
