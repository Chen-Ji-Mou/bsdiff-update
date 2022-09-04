package com.example.chenjimou;

import android.util.Log;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FtpUtils
{
    private static final String TAG = "FTP";

    //超时时间（单位：s）
    public int timeOut = 2;
    //被动模式开关 如果不开被动模式 有防火墙 可能会上传失败， 但被动模式需要ftp支持
    public boolean enterLocalPassiveMode = true;

    private FTPClient ftpClient = null;

    private static FtpUtils mFtpUtils = null;


    private FtpUtils() { initFtpClient(); }

    public static FtpUtils getInstance() {
        if (mFtpUtils == null) {
            synchronized (FtpUtils.class) {
                if (mFtpUtils == null) {
                    mFtpUtils = new FtpUtils();
                }
            }
        }
        return mFtpUtils;
    }


    /**
     * 初始化配置  全局只需初始化一次
     */
    public void initFtpClient() {
        //初始化ftpClient对象
        ftpClient = new FTPClient();
        //设置超时时间以毫秒为单位使用时，从数据连接读。
        ftpClient.setDefaultTimeout(timeOut * 1000);
        ftpClient.setConnectTimeout(timeOut * 1000);
        ftpClient.setDataTimeout(timeOut * 1000);
        ftpClient.setControlEncoding("utf-8");
    }

    /**
     * 连接并登陆ftp
     */
    private void connectFtp(){
        try {
            Log.e(TAG, "连接...FTP服务器...");
            //ftp服务器端口号默认为21
            int port = 21;
            //ftp服务器地址
            String hostname = "43.143.169.6";
            ftpClient.connect(hostname, port); //连接ftp服务器
            //是否开启被动模式
            if (enterLocalPassiveMode) {
                ftpClient.setRemoteVerificationEnabled(false);
                ftpClient.enterLocalPassiveMode();
            }
            //ftp登录密码
            String password = "Chenjimou1*";
            //ftp登录账号
            String username = "ubuntu";
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                Log.e(TAG,"连接...FTP服务器...失败: " + hostname + ":" + port + "");
            }
            Log.e(TAG,"连接...FTP服务器...成功:" + hostname + ":" + port);
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 下载文件 *
     *
     * @param fileName  文件名称 *
     * @param localPath 下载后的文件路径 *
     */
    public boolean downloadFile(String fileName, String localPath) {
        Log.d(TAG,"fileName: "+fileName+"localPath: "+localPath);
        boolean flag = false;
        OutputStream os = null;
        try {
            connectFtp();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (fileName.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localPath + "/" + file.getName());
                    os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    os.close();
                }
            }
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return flag;
    }
}