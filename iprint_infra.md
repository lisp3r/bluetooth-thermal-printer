# iPring infra

## Code

    IPRINT_BASEURL = "https://121.196.58.129:5600/";
    IPRINT_BASEURL_HTTP = "https://121.196.58.129:5600/";

    public static String XUEKEWANG_SAME = "http://118.178.86.32:10089/open/question/getSimilarQuestions";
    public static String clientId = "l2kuLkHo3DzkHqy2Om1tASDu";
    public static String clientSecret = "oYN8Oj1tiukRegqewxQFeTGcGtleYvIr";

### Classes

- AppUri.java

```java
public String getCurrentProcessName()
    {
        Object obj;
        try
        {
            obj = JVM INSTR new #456 <Class File>;
            Object obj1 = JVM INSTR new #458 <Class StringBuilder>;
            ((StringBuilder) (obj1)).StringBuilder();
            ((StringBuilder) (obj1)).append("/proc/");
            ((StringBuilder) (obj1)).append(Process.myPid());
            ((StringBuilder) (obj1)).append("/cmdline");
            ((File) (obj)).File(((StringBuilder) (obj1)).toString());
            obj1 = JVM INSTR new #481 <Class BufferedReader>;
            FileReader filereader = JVM INSTR new #483 <Class FileReader>;
            filereader.FileReader(((File) (obj)));
            ((BufferedReader) (obj1)).BufferedReader(filereader);
            obj = ((BufferedReader) (obj1)).readLine().trim();
            ((BufferedReader) (obj1)).close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
        return ((String) (obj));
    }

    public Realm getRealm()
    {
        return Realm.getInstance(configuration);
    }

    public BLESPPUtils getmBLESPPUtils()
    {
        return mBLESPPUtils;
    }

    public void onCreate()
    {
        StatService.setDebugOn(false);
        StatService.setAppKey("418bbd7aad");
        StatService.setStartType(true);
        StatService.autoTrace(this);
        StatService.setDebugOn(false);
        StatService.setAppKey("418bbd7aad");
        StatService.setStartType(true);
        StatService.autoTrace(this);
        super.onCreate();
        ViewTarget.setTagId(0x7f08034e);
        context = getApplicationContext();
        if(AppUtils.isAppDebug())
        {
            IPRINT_BASEURL = "https://121.196.58.129:5600/";
            IPRINT_BASEURL_HTTP = "https://121.196.58.129:5600/";
        } else
        {
            IPRINT_BASEURL = "https://121.196.58.129:5600/";
            IPRINT_BASEURL_HTTP = "https://121.196.58.129:5600/";
        }
        ble = BluetoothUtils.getInstance(this);
        mBLESPPUtils = new BLESPPUtils(this);
        mBLESPPUtils.setStopString("\r\n");
        mBLESPPUtils.onCreate();
        app = this;
        initOkGo();
        initOpenCV(this);
        initRealm();
        getAuth();
        Bugly.init(getApplicationContext(), "ca475af5f9", true);
        StatService.setAuthorizedState(context, false);
        StatService.setAppChannel(context, getResources().getString(0x7f0e0030), true);
    }

    public static String IPRINT_BASEURL = "";
    public static String IPRINT_BASEURL_HTTP = "";
    public static int PRINT_SIZE = 384;
    public static final int REQUEST_PERMISSION_LOCATION = 2;
    public static final int REQUEST_PERMISSION_WRITE = 3;
    public static String XUEKEWANG_SAME = "http://118.178.86.32:10089/open/question/getSimilarQuestions";
    public static String clientId = "l2kuLkHo3DzkHqy2Om1tASDu";
    public static String clientSecret = "oYN8Oj1tiukRegqewxQFeTGcGtleYvIr";
}
```


## Scan

    ➜  ~ sudo nmap -Pn -p- 121.196.58.129
    PORT     STATE SERVICE
    22/tcp   open  ssh
    3306/tcp open  mysql
    5600/tcp open  esmmanager
    6000/tcp open  X11
    6111/tcp open  spc
    6600/tcp open  mshvlm
    8000/tcp open  http-alt
    8400/tcp open  cvd

    ➜  ~ sudo nmap -Pn -sC -sV 121.196.58.129 -p 22,3306,5600,6000,6111,6600,8000,8400
    PORT     STATE SERVICE  VERSION
    22/tcp   open  ssh      OpenSSH 7.4 (protocol 2.0)
    3306/tcp open  mysql    MySQL 5.7.29
    | mysql-info:
    |   Protocol: 10
    |   Version: 5.7.29
    |   Thread ID: 1090616
    |   Capabilities flags: 65535
    |   Some Capabilities: Support41Auth, SupportsCompression, InteractiveClient, SupportsTransactions, SupportsLoadDataLocal, Speaks41ProtocolOld, IgnoreSpaceBeforeParenthesis, IgnoreSigpipes, FoundRows, Speaks41ProtocolNew, DontAllowDatabaseTableColumn, LongPassword, SwitchToSSLAfterHandshake, LongColumnFlag, ODBCClient, ConnectWithDatabase, SupportsMultipleStatments, SupportsAuthPlugins, SupportsMultipleResults
    |   Status: Autocommit
    |   Salt: 6jS\x1EXj=S;/8XC]`|@Z>+
    |_  Auth Plugin Name: mysql_native_password
    | ssl-cert: Subject: commonName=MySQL_Server_5.7.29_Auto_Generated_Server_Certificate
    | Not valid before: 2020-04-20T01:18:22
    |_Not valid after:  2030-04-18T01:18:22
    |_ssl-date: TLS randomness does not represent time
    5600/tcp open  ssl/http Tornado httpd 4.0
    |_http-title: Site doesn't have a title (text/plain).
    | ssl-cert: Subject: commonName=cm/organizationName=cm/stateOrProvinceName=xwch/countryName=xw
    | Not valid before: 2019-11-07T09:16:36
    |_Not valid after:  2029-11-04T09:16:36
    |_ssl-date: TLS randomness does not represent time
    6000/tcp open  http     Tornado httpd 4.0
    6111/tcp open  http     Tornado httpd 4.0
    6600/tcp open  http     Tornado httpd 4.0
    8000/tcp open  ssl/http Tornado httpd 4.0
    8400/tcp open  http     Tornado httpd 4.0

    ➜  ~ sudo nmap -Pn -p- 118.178.86.32
    PORT     STATE SERVICE
    80/tcp   open  http
    443/tcp  open  https
    8002/tcp open  teradataordbms
    8006/tcp open  wpl-analytics
    8007/tcp open  ajp12
    8008/tcp open  http
    8009/tcp open  ajp13
    8016/tcp open  ads-s
    8086/tcp open  d-s-n

    ➜  ~ sudo nmap -Pn -sC -sV 118.178.86.32 -p 80,443,8002,8006,8007,8008,8009,8016,8086
    PORT     STATE SERVICE  VERSION
    80/tcp   open  http     Microsoft HTTPAPI httpd 2.0 (SSDP/UPnP)
    |_http-server-header: Microsoft-HTTPAPI/2.0
    |_http-title: Not Found
    443/tcp  open  ssl/http Microsoft HTTPAPI httpd 2.0 (SSDP/UPnP)
    |_http-server-header: Microsoft-HTTPAPI/2.0
    |_http-title: Not Found
    | ssl-cert: Subject: commonName=*.zxxk.com/organizationName=\xE5\x8C\x97\xE4\xBA\xAC\xE5\x87\xA4\xE5\x87\xB0\xE5\xAD\xA6\xE6\x98\x93\xE7\xA7\x91\xE6\x8A\x80\xE6\x9C\x89\xE9\x99\x90\xE5\x85\xAC\xE5\x8F\xB8/stateOrProvinceName=\xE5\x8C\x97\xE4\xBA\xAC\xE5\xB8\x82/countryName=CN
    | Subject Alternative Name: DNS:*.zxxk.com, DNS:xkw.com, DNS:xkw.cn, DNS:*.xkw.com, DNS:*.xkw.cn, DNS:zxxk.com
    | Not valid before: 2020-05-11T01:36:32
    |_Not valid after:  2022-06-24T23:59:59
    |_ssl-date: 2021-08-30T17:54:28+00:00; 0s from scanner time.
    | sslv2:
    |   SSLv2 supported
    |   ciphers:
    |     SSL2_RC4_128_WITH_MD5
    |_    SSL2_DES_192_EDE3_CBC_WITH_MD5
    8002/tcp open  http     Microsoft HTTPAPI httpd 2.0 (SSDP/UPnP)
    |_http-server-header: Microsoft-HTTPAPI/2.0
    |_http-title: Bad Request
    8006/tcp open  http     Microsoft HTTPAPI httpd 2.0 (SSDP/UPnP)
    |_http-server-header: Microsoft-HTTPAPI/2.0
    |_http-title: Bad Request
    8007/tcp open  http     Microsoft HTTPAPI httpd 2.0 (SSDP/UPnP)
    |_http-server-header: Microsoft-HTTPAPI/2.0
    |_http-title: Bad Request
    8008/tcp open  http     Microsoft HTTPAPI httpd 2.0 (SSDP/UPnP)
    |_http-server-header: Microsoft-HTTPAPI/2.0
    |_http-title: Bad Request
    8009/tcp open  http     Microsoft HTTPAPI httpd 2.0 (SSDP/UPnP)
    |_ajp-methods: Failed to get a valid response for the OPTION request
    |_http-server-header: Microsoft-HTTPAPI/2.0
    |_http-title: Bad Request
    8016/tcp open  http     Microsoft IIS httpd 7.5
    | http-methods:
    |_  Potentially risky methods: TRACE
    |_http-server-header: Microsoft-IIS/7.5
    |_http-title: 403 - \xBD\xFB\xD6\xB9\xB7\xC3\xCE\xCA: \xB7\xC3\xCE\xCA\xB1\xBB\xBE\xDC\xBE\xF8\xA1\xA3
    8086/tcp open  http     Microsoft HTTPAPI httpd 2.0 (SSDP/UPnP)
    |_http-server-header: Microsoft-HTTPAPI/2.0
    |_http-title: Bad Request
    Service Info: OS: Windows; CPE: cpe:/o:microsoft:windows
