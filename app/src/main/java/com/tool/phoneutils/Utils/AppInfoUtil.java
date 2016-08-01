package com.tool.phoneutils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by wlhuang on 19/07/2016.
 */
public class AppInfoUtil {

    public static boolean exeCmdWithRoot(String cmdStr){
        if(null == cmdStr || "".equals(cmdStr)){
            L.d("bad cmdStr" );
            return false;
        }
        Process process = null;
        String[] cmds = new String[] { cmdStr };
        try {
            process = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            for (String cmd : cmds) {
                os.write(new String(cmd + "\n").getBytes());
            }
            os.flush();
            os.close();
        } catch (Exception e) {
            String message = "executeCmd: " + cmdStr + " error: " + e.toString();
            L.d("errorMessage:"+message);
        }

        if (process != null) {
            L.d("process wait execed");
            try {
                int status = process.waitFor();
                process.getOutputStream().close();
                process.getErrorStream().close();
                process.getInputStream().close();
                L.d("status:"+status);
                //这里是关键代码，其实只有status为1的时候是没有权限，这里个人直接把所有运行shell命令的异常都归为失败
                if(0 == status){
                    return true;
                }else{
                    return false;
                }
            } catch (InterruptedException e) {
                L.d("InterruptedException");
                e.printStackTrace();
            } catch (IOException e) {
                L.d("IOException");
                e.printStackTrace();
            }
        }
        return false;
    }
}
