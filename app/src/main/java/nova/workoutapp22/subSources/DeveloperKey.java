package nova.workoutapp22.subSources;

/**
 * Created by jamsy on 2017-09-22.
 */

// Copyright 2012 Google Inc. All Rights Reserved.

/**
 * Static container class for holding a reference to your YouTube Developer Key.
 */
public class DeveloperKey {

    /**
     * Please replace this with a valid API key which is enabled for the
     * YouTube Data API v3 service. Go to the
     * <a href="https://code.google.com/apis/console/">Google APIs Console</a> to
     * register a new developer key.
     */

    /**
     * // API키 제한하기 : keytool 찾기 -> debug.keystore 경로 찾기

     *  제한이필요없다면 그냥 키발급받아서 넣어도 OK
     *  키 제한은 https://morgentau1032.blogspot.kr/2017/03/sha-1.html 참고
     *
     *  cmd 열어서
     *  keytool -list -keystore [debug.keystore의 경로 + 이름]  -alias androiddebugkey  -storepass android -keypass android
     */





    public static final String DEVELOPER_KEY = "AIzaSyCz07UIo_YXAGmpkqaSQY2MLR8yQq6Ykc8";

}