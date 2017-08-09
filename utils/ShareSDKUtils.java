package com.chaychan.blogfileresource.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.HashMap;


/**
 * @author chaychan
 * @description: 分享的工具类
 * @date 2016/11/10  20:24
 */
public class ShareSDKUtils {

    private static Handler mHandler = new Handler();
    private static ShareSDKUtils mInstance;
    private static Object mLock = new Object();
    private static Context mContext;

    public static ShareSDKUtils getInstance(Context context){
        if (mInstance == null){
            synchronized (mLock){
                if (mInstance == null){
                    mInstance = new ShareSDKUtils();
                    mContext = context;
                }
            }
        }
        return mInstance;
    }

    /**
     * 使用默认的分享GUI
     *
     * @param title         标题
     * @param content       内容
     * @param imgUrl        图片的Url
     * @param siteUrl       网站的Url
     * @param shareListener 分享的回调
     */
    private static void useDefaultGUI(String title, String content, String imgUrl, String siteUrl, PlatformActionListener shareListener) {
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        //oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(siteUrl);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(imgUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(siteUrl);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(siteUrl);

        // text是分享文本，所有平台都需要这个字段
        oks.setText(content + "\n链接: " + siteUrl);

        //设置回调
        oks.setCallback(shareListener);

        // 启动分享GUI
        oks.show(mContext);
    }

    /**
     * 分享到微信好友或朋友圈,图片可以是ur或者是本地的，默认本地图片优先
     *
     * @param wechatType    微信好友或朋友圈 Wechat.NAME 微信好友 WechatMoments.NAME 朋友圈
     * @param shareType    分享的类型 Platform.SHARE_WEBPAGE 网页 Platform.SHARE_IMAGE 图片
     * @param title         标题
     * @param content       内容
     * @param siteUrl       网站的url
     * @param imgUrl        网络图片的url
     * @param bm            bitmap
     * @param imgPath       本地图片的绝对路径
     * @param shareListener 分享的回调
     */
    private static void shareToWechat(String wechatType, int shareType, String title, String content, String siteUrl, String imgUrl, Bitmap bm, String imgPath, PlatformActionListener shareListener) {
        ShareSDK.initSDK(mContext);

        Wechat.ShareParams shareParams = new Wechat.ShareParams();
        shareParams.setShareType(shareType);
        shareParams.setTitle(title);//设置标题
        shareParams.setText(content);//设置内容
        shareParams.setUrl(siteUrl);//设置网站

        if (!TextUtils.isEmpty(imgUrl)) {
            //如果有网络图片，则设置图片的url
            shareParams.setImageUrl(imgUrl);
        }

        if (bm != null) {
            //如果是bitmap类型，则设置bm
            shareParams.setImageData(bm);
        }

        if (!TextUtils.isEmpty(imgPath)) {
            //如果是本地图片路径，则设置图片路径
            shareParams.setImagePath(imgPath);
        }

        Platform platform = ShareSDK.getPlatform(wechatType);
        platform.share(shareParams);//分享
        if (shareListener != null) {
            platform.setPlatformActionListener(shareListener);//设置回调
        } else {
            //如果为空，设置默认回调回调
            platform.setPlatformActionListener(defaultShareListner);
        }
    }

    /**
     * 分享到QQ，QQ空间
     *
     * @param title         标题
     * @param content       内容
     * @param siteUrl       网站的url
     * @param imgUrl        网络图片的url
     * @param bm            bitmap
     * @param imgPath       本地图片的绝对路径
     * @param shareListener 分享的回调
     */
    private static void shareToQQ(String title, String content, String siteUrl, String imgUrl, Bitmap bm, String imgPath, PlatformActionListener shareListener) {
        ShareSDK.initSDK(mContext);
        QQ.ShareParams shareParams = new QQ.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);//标题
        shareParams.setText(content);//内容
        shareParams.setTitleUrl(siteUrl);//网址

        if (!TextUtils.isEmpty(imgUrl)) {
            //如果有网络图片，则设置图片的url
            shareParams.setImageUrl(imgUrl);
        }

        if (bm != null) {
            //如果是bitmap类型，则设置bm
            shareParams.setImageData(bm);
        }

        if (!TextUtils.isEmpty(imgPath)) {
            //如果是本地图片路径，则设置图片路径
            shareParams.setImagePath(imgPath);
        }

        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.share(shareParams);//分享
        if (shareListener != null) {
            platform.setPlatformActionListener(shareListener);//设置回调
        } else {
            //如果为空，设置默认回调回调
            platform.setPlatformActionListener(defaultShareListner);
        }
    }

    /**
     * 分享到新浪微博
     *
     * @param content       内容
     * @param siteUrl       网站的url
     * @param imgUrl        网络图片的url
     * @param bm            bitmap
     * @param imgPath       本地图片的绝对路径
     * @param shareListener 分享的回调
     */
    private static void shareToSina(String content, String siteUrl, String imgUrl, Bitmap bm, String imgPath, PlatformActionListener shareListener) {
        ShareSDK.initSDK(mContext);
        SinaWeibo.ShareParams shareParams = new SinaWeibo.ShareParams();
        shareParams.setContentType(Platform.SHARE_WEBPAGE);
        shareParams.setText(content + "\n" + siteUrl);//设置内容
        if (!TextUtils.isEmpty(imgUrl)) {
            //如果有网络图片，则设置图片的url
            shareParams.setImageUrl(imgUrl);
        }

        if (bm != null) {
            //如果是bitmap类型，则设置bm
            shareParams.setImageData(bm);
        }

        if (!TextUtils.isEmpty(imgPath)) {
            //如果是本地图片路径，则设置图片路径
            shareParams.setImagePath(imgPath);
        }

        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        platform.share(shareParams);//分享
        if (shareListener != null) {
            platform.setPlatformActionListener(shareListener);//设置回调
        } else {
            //如果为空，设置默认回调回调
            platform.setPlatformActionListener(defaultShareListner);
        }
    }

    /**
     * 默认的分享回调
     */
    public static PlatformActionListener defaultShareListner = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onCancel(Platform platform, int i) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "分享已取消", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
