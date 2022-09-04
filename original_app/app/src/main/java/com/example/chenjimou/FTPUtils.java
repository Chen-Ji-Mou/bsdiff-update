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

public class FTPUtils
{
    private static final String TAG = "[FTP]";

    //超时时间（单位：s）
    public int timeOut = 2;

    private FTPClient ftpClient = null;

    private static FTPUtils mFTPUtils = null;


    private FTPUtils() { initFtpClient(); }

    public static FTPUtils getInstance() {
        if (mFTPUtils == null) {
            synchronized (FTPUtils.class) {
                if (mFTPUtils == null) {
                    mFTPUtils = new FTPUtils();
                }
            }
        }
        return mFTPUtils;
    }


    /**
     * 初始化配置
     */
    public void initFtpClient() {
        // 初始化FTPClient对象
        ftpClient = new FTPClient();
        // 设置超时时间
        ftpClient.setDefaultTimeout(timeOut * 1000);
        ftpClient.setConnectTimeout(timeOut * 1000);
        ftpClient.setDataTimeout(timeOut * 1000);
        ftpClient.setControlEncoding("utf-8");
    }

    /**
     * 连接并登陆FTP服务器
     */
    private void connectFTP() throws IOException {
        Log.d(TAG+" connectFTP","start");
        // 端口号
        int port = 21;
        // 服务器IP地址
        String hostname = "43.143.169.6";
        // 开始连接FTP服务器
        ftpClient.connect(hostname, port);
        // 开启被动模式
        ftpClient.setRemoteVerificationEnabled(false);
        ftpClient.enterLocalPassiveMode();
        // 登录账号
        String username = "ubuntu";
        // 登录密码
        String password = "Chenjimou1*";
        // 登录FTP服务器
        ftpClient.login(username, password);
        // 查看返回码判断是否成功登录FTP服务器
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            Log.e(TAG+" connectFTP","connect fail "+hostname+":"+port);
        } else {
            Log.e(TAG+" connectFTP","connect success "+hostname+":"+port);
        }
    }

    /**
     * 下载文件 *
     *
     * @param fileName  文件名称 *
     * @param localPath 下载后的文件路径 *
     */
    public Result downloadFile(String fileName, String localPath) {
        Log.d(TAG+" downloadFile","start fileName: "+fileName+"localPath: "+localPath);
        Result result = new Result();
        OutputStream os = null;
        try {
            connectFTP();
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
            result.status = ResultStatus.success;
            result.msg = "patch文件下载完成";
            Log.d(TAG+" downloadFile","download success");
        } catch (Exception e) {
            result.status = ResultStatus.error;
            result.msg = e.getMessage();
            Log.e(TAG+" downloadFile",e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    Log.e(TAG+" downloadFile",e.getMessage());
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG+" downloadFile",e.getMessage());
                }
            }
        }
        return result;
    }
}

enum ResultStatus { success, error }

class Result {
    ResultStatus status = ResultStatus.error;
    String msg;
}