# 开屏接入文档
##### 目前开屏广告方式为冷启动开屏
# 接入方式
## 1.1SDK包的导入
### 在项目根目录build.gradle中添加maven依赖
```
    allprojects {
        repositories {
            maven { url "http://47.103.41.218:8081/repository/maven-public/" }
            google()
            jcenter()

        }
    }
```
### 在app项目build.gradle中添加依赖

```
    implementation 'com.coolpush:ctbusinesslibcore:1.1'
```

## 1.2 AndroidManifest配置
### 1.2.1 添加权限

```
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <!-- GPS定位权限 -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

### 1.2.2 适配Anroid7.0及以上
如果您的应用需要在Anroid7.0及以上环境运行，请在AndroidManifest中添加如下代码：

```
     <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.coolpush.ctbusinesslibcore.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
    </provider>
    在res/xml目录下，新建一个xml文件file_paths，在该文件中添加如下代码：
    <?xml version="1.0" encoding="utf-8"?>
    <paths xmlns:android="http://schemas.android.com/apk/res/android">
         <external-files-path name="external_files_path" path="Download" />
        <!--为了适配所有路径可以设置 path = "." -->
    </paths>

```
### 1.2.4 运行环境配置
本SDK可运行于Android4.0 (API Level 14) 及以上版本。

```
<uses-sdk android:minSdkVersion="16" android:targetSdkVersion="28" />

```
如果开发者声明targetSdkVersion到API 23以上，请确保调用本SDK的任何接口前，已经申请到了SDK要求的所有权限。否则SDK可能无法正常工作。
## 1.3 代码混淆
如果您需要使用proguard混淆代码，需确保不要混淆SDK的代码。 请在proguard.cfg文件(或其他混淆文件)尾部添加如下配置:

```
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
```
## 1.4初始化sdk
注意：为获取更好的广告推荐效果，以及提高激励视频广告、下载类广告等填充率，建议在广告请求前，合适的时机调用SDK提供的方法，建议在Application初始化的地方调用

```
    AdInitParam adInitParam = new AdInitParam.Builder().setId("5034057").setName("妈妈帮").setBundle("com.example.ctbdemo").setDomain("example.ctbdemo").setVer("5.1.0").build();
            CTGlobalAdConfig.init(this, adInitParam);
```
## 权限申请和其他
需要宿主app调用请求真实ip接口(必须)
```
CTGlobalAdConfig.requestRealNetworkIp();
```
需要宿主app调用请求location的接口(非必须)

```
LocationUtils.getInstance().initLocation(CTGlobalAdConfig.getApplication());
```
整体参考如下

```
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CODE_PERMISSION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (needRequestPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        }
        findViewById(R.id.tv_entrance).setOnClickListener(this);
        CTGlobalAdConfig.requestRealNetworkIp();
    }

    private boolean needRequestPermission() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationUtils.getInstance().initLocation(CTGlobalAdConfig.getApplication());
        switch (v.getId()) {
            case R.id.tv_entrance:
                EntranceActivity.launch(this);
                break;
        }
    }

}
```

## 广告

```
public class EntranceActivity extends Activity {
    private static final String TAG = "EntranceActivity";

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, EntranceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        final CTAdBaseLayout ctAdBaseLayout = findViewById(R.id.ct_ad);
        ctAdBaseLayout.setCTAdRenderCallback(new CTAdRenderCallback() {
            /**
             * 广告渲染成功
             * @param isShowRedDot　是否需要个人中心显示未读红点
             */
            @Override
            public void onAdRenderSucceed(boolean isShowRedDot) {
                Log.d(TAG, "onAdRenderSucceed() called with: isShowRedDot = [" + isShowRedDot +
                        "]");
                ctAdBaseLayout.setVisibility(View.VISIBLE);

                if (isShowRedDot) {
                    // TODO: lzx 19-12-17 宿主个人中心显示红点
                }
            }

            @Override
            public void onAdRenderFailed() {

            }
        });
        // 8个广告位，目前必须是8个
        List<String> positionList = Arrays.asList("455442", "455443", "455444", "455445", "455446"
                , "455447", "455448", "455449");
        ctAdBaseLayout.refreshAd(positionList);
    }
}

```