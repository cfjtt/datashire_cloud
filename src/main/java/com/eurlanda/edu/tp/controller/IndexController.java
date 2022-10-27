package com.eurlanda.edu.tp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhudebin on 2018/3/29.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "redirect:/index.html";
    }

    @RequestMapping("/startEx")
    public String startEx(String host, String path, String port, HttpServletResponse response){
        //清除缓存
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        return "redirect:/ex.html?host="+host+"&path="+path+"&port="+port+"&password=11111111";
    }

    public static void main(String[] args){
//        String access_key = "";
//        String aws_secret_key = "";
//        String endpoint = "";
//        String bucketName = "";
//        /**
//         * AWS用户
//         */
//        AWSCredentials credentials = new BasicAWSCredentials(access_key,aws_secret_key);
//        AmazonS3 s3client = new AmazonS3Client(credentials);
//        s3client.setEndpoint(endpoint);
//        //列出所有的桶
//        s3client.listBuckets();
//        //列出所有的bucket(支持筛选条件)
//        ObjectListing objectListing = s3client.listObjects(new ListObjectsRequest().withBucketName("aaa").withPrefix("sss").withDelimiter("/"));
//        List<S3ObjectSummary> s3ObjecySummary = objectListing.getObjectSummaries();
//        for(S3ObjectSummary objectSummary : s3ObjecySummary){
//            String key = objectSummary.getKey();
//            bucketName = objectSummary.getBucketName();
//            S3Object object = s3client.getObject(new GetObjectRequest(bucketName,key));
//            //读取数据
//            InputStream in = object.getObjectContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//        }
//
//        /**
//         * IAM用户  如果使用的是AWS用户，那么IAM是不能设置回话有效期的，默认为一个小时
//         * 如果使用的是IAM用户来获取临时凭证，可以设置回话有效期
//         */
//        AWSSecurityTokenServiceClient stsClient = new AWSSecurityTokenServiceClient(credentials);
//        GetSessionTokenRequest getSessionTokenRequest = new GetSessionTokenRequest();
//        getSessionTokenRequest.setDurationSeconds(7200);
//        GetSessionTokenResult sessionTokenResult = stsClient.getSessionToken(getSessionTokenRequest);
//        Credentials sessionCredentials = sessionTokenResult.getCredentials();
//        BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(sessionCredentials.getAccessKeyId(),sessionCredentials.getSecretAccessKey(),sessionCredentials.getSessionToken());
//        s3client = new AmazonS3Client(basicSessionCredentials);
//        //正常操作
//
//        /**
//         * 联合用户  (默认为一个小时，但是如果使用IAM用户凭证来请求临时凭证，可以显式地设置其他持续时间值)
//         */
//        GetFederationTokenRequest getFederationTokenRequest = new GetFederationTokenRequest();
//        getFederationTokenRequest.setDurationSeconds(7200);
//        getFederationTokenRequest.setName("User1");
//        com.amazonaws.auth.policy.Policy policy = new com.amazonaws.auth.policy.Policy();
//        com.amazonaws.auth.policy.Condition condition = new StringCondition(StringCondition.StringComparisonType.StringLike, ConditionFactory.SOURCE_IP_CONDITION_KEY,"192.168.137.*");
//        policy.withStatements(new Statement(Statement.Effect.Allow)
//                .withActions(S3Actions.ListObjects)
//                .withConditions(condition)
//                .withResources(new Resource("arn:aws:s3:::"+bucketName)));
//        getFederationTokenRequest.setPolicy(policy.toJson());
//        GetFederationTokenResult federationTokenResult = stsClient.getFederationToken(getFederationTokenRequest);
//        sessionCredentials = federationTokenResult.getCredentials();
//        BasicSessionCredentials basicSessionCredentials1 = new BasicSessionCredentials(access_key,aws_secret_key,sessionCredentials.getSessionToken());
//        s3client = new AmazonS3Client(basicSessionCredentials);
        //正常操作

        //s3client.listBuckets()


        //使用双终端节点实现restful请求

    }
}
