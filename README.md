# Navigator


## 高德官网申请 Key

### 1. 获取应用包名
以 AndroidStudio 工程为例，在 **app** 目录的 **build.gradle** 文件里找到 **ApplicationId** 的值作为应用包名。如果没有设置 **ApplicationId**，那么以 **AndroidManifest.xml** 配置文件的 **package** 属性值作为应用包名。

### 2. 获取 SHA1 值
以 AndroidStudio 的默认 **keystore** 为例，在 **Terminal** 里输入命令 **`keytool -v -list -keystore ~/.android/debug.keystore`**，并根据提示 **输入密钥库口令**，然后在输出信息里找到 **SHA1** 的值。

### 3. 创建 Key
[创建应用](https://lbs.amap.com/dev/key/app) 后根据之前获得的 **应用包名** 和 **SHA1** 生成该应用的 Key。

### 4. 应用 Key
把生成的 **Key** 填到 **AndroidManifest.xml** 里。
```
        <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="你的 Key" />
```

## 阅读 [地图 SDK 参考手册](http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html)
