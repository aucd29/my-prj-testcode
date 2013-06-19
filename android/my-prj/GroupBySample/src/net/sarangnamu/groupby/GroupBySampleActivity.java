package net.sarangnamu.groupby;



import net.sarang.groupby.R;
import net.sarangnamu.groupby.Workspace.IWorkspaceParent;
import net.sarangnamu.util.drag.DragController;
import net.sarangnamu.util.drag.DragLayer;
import android.app.Activity;
import android.os.Bundle;

public class GroupBySampleActivity extends Activity implements IWorkspaceParent {
    private static final String TAG = "GroupBySampleActivity";
    private Workspace workspace;

    // drag
    private DragLayer mDragLayer;
    private DragController mDragController;

    static final int DEFAULT_SCREEN = 2;
    private static final Object sLock = new Object();
    private static int sScreen = DEFAULT_SCREEN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Workspace.urls.add("http://daum.net");
        Workspace.urls.add("http://nate.com");
        Workspace.urls.add("http://naver.com");

        mDragController = new DragController(this);
        setContentView(R.layout.main);

        mDragLayer = (DragLayer) findViewById(R.id.drag_layer);
        mDragLayer.setDragController(mDragController);

        workspace = (Workspace) mDragLayer.findViewById(R.id.workspace);
        workspace.setHapticFeedbackEnabled(false);
        workspace.setDragController(mDragController);
        workspace.setListener(this);

        mDragController.setDragScoller(workspace);
        mDragController.setScrollView(mDragLayer);
        mDragController.setMoveTarget(workspace);
        mDragController.addDropTarget(workspace);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDragController.cancelDrag();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDragController = null;
    }

    @Override
    public boolean isWorkspaceLocked() {
        return true;
    }

    static int getScreen() {
        synchronized (sLock) {
            return sScreen;
        }
    }

    static void setScreen(int screen) {
        synchronized (sLock) {
            sScreen = screen;
        }
    }
}