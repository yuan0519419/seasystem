package com.itheima.utils;
import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AliOssUtil {
    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";
    private static final String BUCKET_NAME = "big-event-s1eeep";
    public static String uploadFile(String objectName, InputStream in) throws Exception {

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String ENDPOINT = "https://oss-cn-beijing.aliyuncs.com";
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        String url ="";
        try {
            // 填写字符串。
            String content = "Hello OSS，你好世界";

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, in);


            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            url="https://"+BUCKET_NAME+"."+ENDPOINT.substring(ENDPOINT.lastIndexOf("/")+1)+"/"+objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}
