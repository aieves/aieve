![](http://www.aieve.cn/aieve.png)
##What
Android framework for development. Please click:http://www.aieve.cn
```
compile 'ai.eve:app:1.0.0'
```

##Can

* `Http Request`
    *  get、post、put、delete and so on.
    *  cache
    *  security
* `AsyncTask —— ETask`
* `Utils`
* `Modularization development`

##How

* Customize your Application extends EApplication.(自定义Application继承自EApplicatio)
* To specify Application name in mainfest.(在manifest文件中，指定Application名字)
* Add permission to mainfest.(在mainfest文件中添加权限)
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_SMS" />
<uses-permission android:name="android.permission.READ_SMS" />
<uses-permission android:name="android.permission.SEND_SMS" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.GET_TASKS" />
```
* You are now ready to start developing with eve.<br>
  Do not forget make your activity extends EActivity or EToolBarActicity.(所有Activity均需要继承自EActivity或EToolBarActicity)

##Issues
If u have some questions, Please contacts us with: 

* email(484349734@qq.com)
* QQ: 484349734
* Website：http://www.aieve.cn

##Thanks
Thanks to the following authors:

* [cui]
* [shuai]
* [w.yong]

##Licence

Copyright 2015-2016 aieve

Licensed under the Apache License, Version 2.0 (the "License");<br>
you may not use this file except in compliance with the License.<br>
You may obtain a copy of the License at
```
    http://www.apache.org/licenses/LICENSE-2.0
```
Unless required by applicable law or agreed to in writing, software<br>
distributed under the License is distributed on an "AS IS" BASIS,<br>
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either <br>express or implied.
See the License for the specific language <br>
governing permissions and
limitations under the License.<br>
