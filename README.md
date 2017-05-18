<h1 align="center">MVP with Firebase</h1>

<p align="center">
  <a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg?style=flat-square"></a>
  <a href="https://android-arsenal.com/api?level=14"><img src="https://img.shields.io/badge/API-7%2B-brightgreen.svg?style=flat-square"></a>
  <a href="https://github.com/allsoft777/MVP-with-Firebase/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-APACHE-blue.svg?style=flat-square"></a>
</p>


## 현 저장소에서는 아래의 주요 기능을 제공하고 관리하고 있습니다.

### ✰ MVP Architecture를 Library화 시켜서 배포하고 있습니다.<br>
#### Build Settings

##### Gradle
```groovy
dependencies {
    compile 'com.seongil:mvplife:1.0.2'
}
```
##### Maven
```xml
<dependency>
  <groupId>com.seongil</groupId>
  <artifactId>mvplife</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```

   <a href="http://softwaree.tistory.com/10" target="_blank">블로그 페이지 바로가기</a>

### ✰ Firebase 서비스를 어떻게 사용하는지에 대한 샘플 코드들을 볼 수 있습니다.<br>
   블로그 작성중

### ✰ MVP Architecture를 적용한 Firebase 서비스 앱으로서, Clip Diary를 관리하고 있습니다.<br>
   <a href="https://play.google.com/store/apps/details?id=com.seongil.mvplife.sample" target="_blank">안드로이드 마켓에서 APK 다운로드</a>

## ✰ 클립 다이어리는?

✔ 클립 보드 매니저입니다.<br>
클립 보드로 복사가 발생하면 이를 감지해서 클립 다이어리 앱으로 저장하게 됩니다. 재부팅을 하여도 계속해서 모니터링하면서 변화를 감지합니다.<br>

✔원격 저장<br>
클립 다이어리에는 Firebase 서버로 데이터를 저장합니다. 이로 인해 사용자는 앱을 삭제하거나 다른 기기에서 같은 계정으로 로그인하여도 동일한 데이터를 싱크하여 볼수 있습니다.<br>

✔ 작은 메모 앱 기능<br>
클립 다이어리는 클립보드에서 감지된 문자만 저장하는게 아닌, 사용자가 직접 추가하거나 수정 할 수 있습니다. 작은 메모 앱으로도 사용 가능합니다.<br>

✔ 공유<br>
클립 다이어리에 있는 항목을 다른 앱으로 공유 할 수 있습니다.<br><br>


✰ 그리고,<br>
✔무료<br>
✔광고 없음<br>
✔오픈 소스<br>

✰ 안드로이드 4.0(아이스크림 샌드위치)부터 지원합니다.<br>


<br>

## Screenshot
<img src="./materials/cliplistview.png" width="288">___
<img src="./materials/clipdetailview.png" width="288">
<br>

License
-------

    Copyright (C) 2017 Seongil Kim

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
