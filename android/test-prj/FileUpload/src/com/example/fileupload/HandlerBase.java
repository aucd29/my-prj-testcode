package com.example.fileupload;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * handler 기반에 이벤트를 처리 하기 위한 기본 클래스
 * 
 */
public abstract class HandlerBase extends Handler {
    protected static final int SUCCESS    = 0;
    protected static final int UPDATE     = 1;
    protected static final int ERROR      = 2;
    protected static final int TOTAL_SIZE = 3;
    protected static final int CANCELLED  = 4;

    //protected boolean isCancelled = false;

    /**
     * callback interface
     * 
     */
    public interface IHandlerBase {
        /**
         * 올바르게 완료
         */
        public void onSuccess();

        /**
         * 진행 상황 알림 (범위: 0~100%)
         * @param read
         */
        public void onUpdate(int read);

        /**
         * 오류 발생
         * @param errMessage
         */
        public void onError(String errMessage);

        /**
         * 파일 사이즈 전달
         * @param size
         */
        public void onTotalSize(long size);

        /**
         * 취소
         */
        public void onCancelled();

        /**
         * 취소 체크
         * @return
         */
        public boolean isCancelled();
    }

    @Override
    public void handleMessage(Message msg) {
        IHandlerBase l;
        Object[] obj = null;

        if (msg.what == ERROR || msg.what == TOTAL_SIZE) {
            obj = (Object[]) msg.obj;
            l = (IHandlerBase) obj[1];
        } else {
            l = (IHandlerBase) msg.obj;
        }

        switch (msg.what) {
        case SUCCESS:
            success(l);
            break;

        case UPDATE:
            update(l, msg.arg1);
            break;

        case ERROR:
            l.onError((String) obj[0]);
            break;

        case TOTAL_SIZE:
            l.onTotalSize((Long) obj[0]);
            break;

        case CANCELLED:
            l.onCancelled();
            break;
        }
    }

    /**
     * 기본적으로 thread 에서는 ui 가 제대로 작동하지 않기 때문에 핸들러를 이용해야
     * 하며 파일 다운로드시 thread 를 사용할 수 밖에 없기 때문에 다운로드 상황에
     * 대해서 알리기 위해서는 핸들러를 이용해야 한다.
     * 
     * 핸들러로 데이터를 전달한다.
     * 
     * @param type
     * @param val
     * @param obj
     */
    protected void sendMessage(int type, int val, Object obj) {
        Message msg = obtainMessage();
        msg.what = type;
        msg.arg1 = val;
        msg.obj  = obj;

        sendMessage(msg);
    }

    /**
     * 작업을 취소 한다.
     */
    //    public void cancel() {
    //        isCancelled = true;
    //    }

    /**
     * 오류 메시지를 전달 한다.
     * @param e
     * @param l
     */
    protected void sendErrorMessage(final Exception e, IHandlerBase l) {
        e.printStackTrace();

        Object[] obj = new Object[2];
        obj[0] = e.getMessage();
        obj[1] = l;

        if (l != null) {
            sendMessage(ERROR, 0, obj);
        }
    }

    /**
     * 리스너가 NULL 인지 확인 한다.
     * @param tag
     * @param l
     * @return
     */
    protected boolean checkListener(final String tag, IHandlerBase l) {
        if (l == null) {
            Log.d(tag, "===================================================================");
            Log.d(tag, "IHandlerBase l == null");
            Log.d(tag, "===================================================================");
            return false;
        }

        return true;
    }

    /**
     * 모든 진행이 올바르게 완료 되었을 때 발생하는 이벤트
     * @param l
     */
    protected abstract void success(IHandlerBase l);

    /**
     * 진행상황을 표현 한다 예) Progress 값을 증가 시킬때
     * @param l
     * @param value
     */
    protected abstract void update(IHandlerBase l, int value);

    /**
     * 파일 사이즈 이벤트를 전달 한다.
     * @param l
     * @param value
     */
    protected abstract void sendTotalSizeMessage(IHandlerBase l, long value);

    /**
     * 취소 이벤트를 전달 한다.
     * @param l
     * @param dest
     * @param fileName
     * @throws Exception
     */
    protected abstract void sendCancelledMessage(IHandlerBase l, String dest, String fileName) throws Exception;
}
