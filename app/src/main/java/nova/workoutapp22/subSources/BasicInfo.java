package nova.workoutapp22.subSources;

/**
 * Created by jamsy on 2017-08-31.
 */

public class BasicInfo {


    //========== 인텐트 부가정보 전달을 위한 키값 ==========//
    public static final String KEY_MEMO_MODE = "MEMO_MODE";
    public static final String KEY_MEMO_TEXT = "MEMO_TEXT";
    public static final String KEY_MEMO_ID = "MEMO_ID";
    public static final String KEY_MEMO_DATE = "MEMO_DATE";
    public static final String KEY_ID_PHOTO = "ID_PHOTO";
    public static final String KEY_URI_PHOTO = "URI_PHOTO";
    public static final String KEY_ID_VIDEO = "ID_VIDEO";
    public static final String KEY_URI_VIDEO = "URI_VIDEO";
    public static final String KEY_ID_VOICE = "ID_VOICE";
    public static final String KEY_URI_VOICE = "URI_VOICE";
    public static final String KEY_ID_HANDWRITING = "ID_HANDWRITING";
    public static final String KEY_URI_HANDWRITING = "URI_HANDWRITING";

    //========== 메모 모드 상수 ==========//
    public static final String MODE_ADD = "MODE_ADD";
    public static final String MODE_MODIFY = "MODE_MODIFY";
    public static final String MODE_VIEW = "MODE_VIEW";



    //========== 액티비티 요청 코드  ==========//
    public static final int REQ_VIEW_ACTIVITY = 1001;
    public static final int REQ_ADDMEMO_ACTIVITY = 1002;
    public static final int REQ_PHOTO_CAPTURE_ACTIVITY = 1501;
    public static final int REQ_PHOTO_SELECTION_ACTIVITY = 1502;
}
