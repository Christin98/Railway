package com.kha.train.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ScrollerCompat;
import java.util.Arrays;

public class ViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float f) {
            f -= 1.0f;
            return ((((f * f) * f) * f) * f) + 1.0f;
        }
    };
    private int mActivePointerId = -1;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private ScrollerCompat mScroller;
    private final Runnable mSetIdleRunnable = new Runnable() {
        public void run() {
            ViewDragHelper.this.setDragState(0);
        }
    };
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

    public static abstract class Callback {
        public int clampViewPositionHorizontal(View view, int i, int i2) {
            return 0;
        }

        public int clampViewPositionVertical(View view, int i, int i2) {
            return 0;
        }

        public int getOrderedChildIndex(int i) {
            return i;
        }

        public int getViewHorizontalDragRange(View view) {
            return 0;
        }

        public int getViewVerticalDragRange(View view) {
            return 0;
        }

        public void onEdgeDragStarted(int i, int i2) {
        }

        public boolean onEdgeLock(int i) {
            return false;
        }

        public void onEdgeTouched(int i, int i2) {
        }

        public void onViewCaptured(View view, int i) {
        }

        public void onViewDragStateChanged(int i) {
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
        }

        public void onViewReleased(View view, float f, float f2) {
        }

        public abstract boolean tryCaptureView(View view, int i);
    }

    public static ViewDragHelper create(ViewGroup viewGroup, Callback callback) {
        return new ViewDragHelper(viewGroup.getContext(), viewGroup, null, callback);
    }

    public static ViewDragHelper create(ViewGroup viewGroup, Interpolator interpolator, Callback callback) {
        return new ViewDragHelper(viewGroup.getContext(), viewGroup, interpolator, callback);
    }

    public static ViewDragHelper create(ViewGroup viewGroup, float f, Callback callback) {
        ViewDragHelper create = create(viewGroup, callback);
        create.mTouchSlop = (int) (((float) create.mTouchSlop) * (1.0f / f));
        return create;
    }

    public static ViewDragHelper create(ViewGroup viewGroup, float f, Interpolator interpolator, Callback callback) {
        ViewDragHelper create = create(viewGroup, interpolator, callback);
        create.mTouchSlop = (int) (((float) create.mTouchSlop) * (1.0f / f));
        return create;
    }

    private ViewDragHelper(Context context, ViewGroup viewGroup, Interpolator interpolator, Callback callback) {
        if (viewGroup == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        } else if (callback != null) {
            this.mParentView = viewGroup;
            this.mCallback = callback;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            this.mEdgeSize = (int) ((context.getResources().getDisplayMetrics().density * 20.0f) + 0.5f);
            this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
            this.mMaxVelocity = (float) viewConfiguration.getScaledMaximumFlingVelocity();
            this.mMinVelocity = (float) viewConfiguration.getScaledMinimumFlingVelocity();
            if (interpolator == null) {
                interpolator = sInterpolator;
            }
            this.mScroller = ScrollerCompat.create(context, interpolator);
        } else {
            throw new IllegalArgumentException("Callback may not be null");
        }
    }

    public void setMinVelocity(float f) {
        this.mMinVelocity = f;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public void setEdgeTrackingEnabled(int i) {
        this.mTrackingEdges = i;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public void captureChildView(View view, int i) {
        if (view.getParent() == this.mParentView) {
            this.mCapturedView = view;
            this.mActivePointerId = i;
            this.mCallback.onViewCaptured(view, i);
            setDragState(1);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
        stringBuilder.append(this.mParentView);
        stringBuilder.append(")");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public void cancel() {
        this.mActivePointerId = -1;
        clearMotionHistory();
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void abort() {
        cancel();
        if (this.mDragState == 2) {
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            int currX2 = this.mScroller.getCurrX();
            int currY2 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        setDragState(0);
    }

    public boolean smoothSlideViewTo(View view, int i, int i2) {
        this.mCapturedView = view;
        this.mActivePointerId = -1;
        return forceSettleCapturedViewAt(i, i2, 0, 0);
    }

    public boolean settleCapturedViewAt(int i, int i2) {
        if (this.mReleaseInProgress) {
            return forceSettleCapturedViewAt(i, i2, (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    private boolean forceSettleCapturedViewAt(int i, int i2, int i3, int i4) {
        int left = this.mCapturedView.getLeft();
        int top = this.mCapturedView.getTop();
        i -= left;
        i2 -= top;
        if (i == 0 && i2 == 0) {
            this.mScroller.abortAnimation();
            setDragState(0);
            return false;
        }
        this.mScroller.startScroll(left, top, i, i2, computeSettleDuration(this.mCapturedView, i, i2, i3, i4));
        setDragState(2);
        return true;
    }

    private int computeSettleDuration(View view, int i, int i2, int i3, int i4) {
        float f;
        float f2;
        float f3;
        i3 = clampMag(i3, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        i4 = clampMag(i4, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int abs = Math.abs(i);
        int abs2 = Math.abs(i2);
        int abs3 = Math.abs(i3);
        int abs4 = Math.abs(i4);
        int i5 = abs3 + abs4;
        int i6 = abs + abs2;
        if (i3 != 0) {
            f = (float) abs3;
            f2 = (float) i5;
        } else {
            f = (float) abs;
            f2 = (float) i6;
        }
        f /= f2;
        if (i4 != 0) {
            f3 = (float) abs4;
            f2 = (float) i5;
        } else {
            f3 = (float) abs2;
            f2 = (float) i6;
        }
        f3 /= f2;
        return (int) ((((float) computeAxisDuration(i, i3, this.mCallback.getViewHorizontalDragRange(view))) * f) + (((float) computeAxisDuration(i2, i4, this.mCallback.getViewVerticalDragRange(view))) * f3));
    }

    private int computeAxisDuration(int i, int i2, int i3) {
        if (i == 0) {
            return 0;
        }
        int width = this.mParentView.getWidth();
        float f = (float) (width / 2);
        f += distanceInfluenceForSnapDuration(Math.min(1.0f, ((float) Math.abs(i)) / ((float) width))) * f;
        i2 = Math.abs(i2);
        if (i2 > 0) {
            i = Math.round(Math.abs(f / ((float) i2)) * 1000.0f) * 4;
        } else {
            i = (int) (((((float) Math.abs(i)) / ((float) i3)) + 1.0f) * 256.0f);
        }
        return Math.min(i, MAX_SETTLE_DURATION);
    }

    private int clampMag(int i, int i2, int i3) {
        int abs = Math.abs(i);
        if (abs < i2) {
            return 0;
        }
        if (abs <= i3) {
            return i;
        }
        if (i <= 0) {
            i3 = -i3;
        }
        return i3;
    }

    private float clampMag(float f, float f2, float f3) {
        float abs = Math.abs(f);
        if (abs < f2) {
            return 0.0f;
        }
        if (abs <= f3) {
            return f;
        }
        if (f <= 0.0f) {
            f3 = -f3;
        }
        return f3;
    }

    private float distanceInfluenceForSnapDuration(float f) {
        double d = (double) (f - 0.5f);
        Double.isNaN(d);
        return (float) Math.sin((double) ((float) (d * 0.4712389167638204d)));
    }

    public void flingCapturedView(int i, int i2, int i3, int i4) {
        if (this.mReleaseInProgress) {
            this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), i, i3, i2, i4);
            setDragState(2);
            return;
        }
        throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
    }

    public boolean continueSettling(boolean z) {
        boolean z2 = false;
        if (this.mCapturedView == null) {
            return false;
        }
        if (this.mDragState == 2) {
            boolean computeScrollOffset = this.mScroller.computeScrollOffset();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            int left = currX - this.mCapturedView.getLeft();
            int top = currY - this.mCapturedView.getTop();
            if (computeScrollOffset || top == 0) {
                if (left != 0) {
                    this.mCapturedView.offsetLeftAndRight(left);
                }
                if (top != 0) {
                    this.mCapturedView.offsetTopAndBottom(top);
                }
                if (!(left == 0 && top == 0)) {
                    this.mCallback.onViewPositionChanged(this.mCapturedView, currX, currY, left, top);
                }
                if (computeScrollOffset && currX == this.mScroller.getFinalX() && currY == this.mScroller.getFinalY()) {
                    this.mScroller.abortAnimation();
                    computeScrollOffset = this.mScroller.isFinished();
                }
                if (!computeScrollOffset) {
                    if (z) {
                        this.mParentView.post(this.mSetIdleRunnable);
                    } else {
                        setDragState(0);
                    }
                }
            } else {
                this.mCapturedView.setTop(0);
                return true;
            }
        }
        if (this.mDragState == 2) {
            z2 = true;
        }
        return z2;
    }

    private void dispatchViewReleased(float f, float f2) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, f, f2);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            setDragState(0);
        }
    }

    private void clearMotionHistory() {
        float[] fArr = this.mInitialMotionX;
        if (fArr != null) {
            Arrays.fill(fArr, 0.0f);
            Arrays.fill(this.mInitialMotionY, 0.0f);
            Arrays.fill(this.mLastMotionX, 0.0f);
            Arrays.fill(this.mLastMotionY, 0.0f);
            Arrays.fill(this.mInitialEdgesTouched, 0);
            Arrays.fill(this.mEdgeDragsInProgress, 0);
            Arrays.fill(this.mEdgeDragsLocked, 0);
            this.mPointersDown = 0;
        }
    }

    private void clearMotionHistory(int i) {
        float[] fArr = this.mInitialMotionX;
        if (fArr != null && fArr.length > i) {
            fArr[i] = 0.0f;
            this.mInitialMotionY[i] = 0.0f;
            this.mLastMotionX[i] = 0.0f;
            this.mLastMotionY[i] = 0.0f;
            this.mInitialEdgesTouched[i] = 0;
            this.mEdgeDragsInProgress[i] = 0;
            this.mEdgeDragsLocked[i] = 0;
            this.mPointersDown = ((1 << i) ^ -1) & this.mPointersDown;
        }
    }

    private void ensureMotionHistorySizeForId(int i) {
        float[] fArr = this.mInitialMotionX;
        if (fArr == null || fArr.length <= i) {
            i++;
            fArr = new float[i];
            float[] fArr2 = new float[i];
            float[] fArr3 = new float[i];
            float[] fArr4 = new float[i];
            int[] iArr = new int[i];
            int[] iArr2 = new int[i];
            int[] iArr3 = new int[i];
            float[] fArr5 = this.mInitialMotionX;
            if (fArr5 != null) {
                System.arraycopy(fArr5, 0, fArr, 0, fArr5.length);
                fArr5 = this.mInitialMotionY;
                System.arraycopy(fArr5, 0, fArr2, 0, fArr5.length);
                fArr5 = this.mLastMotionX;
                System.arraycopy(fArr5, 0, fArr3, 0, fArr5.length);
                fArr5 = this.mLastMotionY;
                System.arraycopy(fArr5, 0, fArr4, 0, fArr5.length);
                int[] iArr4 = this.mInitialEdgesTouched;
                System.arraycopy(iArr4, 0, iArr, 0, iArr4.length);
                iArr4 = this.mEdgeDragsInProgress;
                System.arraycopy(iArr4, 0, iArr2, 0, iArr4.length);
                iArr4 = this.mEdgeDragsLocked;
                System.arraycopy(iArr4, 0, iArr3, 0, iArr4.length);
            }
            this.mInitialMotionX = fArr;
            this.mInitialMotionY = fArr2;
            this.mLastMotionX = fArr3;
            this.mLastMotionY = fArr4;
            this.mInitialEdgesTouched = iArr;
            this.mEdgeDragsInProgress = iArr2;
            this.mEdgeDragsLocked = iArr3;
        }
    }

    private void saveInitialMotion(float f, float f2, int i) {
        ensureMotionHistorySizeForId(i);
        float[] fArr = this.mInitialMotionX;
        this.mLastMotionX[i] = f;
        fArr[i] = f;
        fArr = this.mInitialMotionY;
        this.mLastMotionY[i] = f2;
        fArr[i] = f2;
        this.mInitialEdgesTouched[i] = getEdgesTouched((int) f, (int) f2);
        this.mPointersDown |= 1 << i;
    }

    private void saveLastMotion(MotionEvent motionEvent) {
        int pointerCount = MotionEventCompat.getPointerCount(motionEvent);
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = MotionEventCompat.getPointerId(motionEvent, i);
            float x = MotionEventCompat.getX(motionEvent, i);
            float y = MotionEventCompat.getY(motionEvent, i);
            float[] fArr = this.mLastMotionX;
            if (fArr != null) {
                float[] fArr2 = this.mLastMotionY;
                if (fArr2 != null && fArr.length > pointerId && fArr2.length > pointerId) {
                    fArr[pointerId] = x;
                    fArr2[pointerId] = y;
                }
            }
        }
    }

    public boolean isPointerDown(int i) {
        return ((1 << i) & this.mPointersDown) != 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void setDragState(int i) {
        if (this.mDragState != i) {
            this.mDragState = i;
            this.mCallback.onViewDragStateChanged(i);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean tryCaptureViewForDrag(View view, int i) {
        if (view == this.mCapturedView && this.mActivePointerId == i) {
            return true;
        }
        if (view == null || !this.mCallback.tryCaptureView(view, i)) {
            return false;
        }
        this.mActivePointerId = i;
        captureChildView(view, i);
        return true;
    }


    public boolean canScroll(View view, boolean z, int i, int i2, int i3, int i4) {
        View view2 = view;
        boolean z2 = true;
        if (view2 instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view2;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                int i5 = i3 + scrollX;
                if (i5 >= childAt.getLeft() && i5 < childAt.getRight()) {
                    int i6 = i4 + scrollY;
                    if (i6 >= childAt.getTop() && i6 < childAt.getBottom()) {
                        if (canScroll(childAt, true, i, i2, i5 - childAt.getLeft(), i6 - childAt.getTop())) {
                            return true;
                        }
                    }
                }
            }
        }
        if (!(z && (ViewCompat.canScrollHorizontally(view, -i) || ViewCompat.canScrollVertically(view, -i2)))) {
            z2 = false;
        }
        return z2;
    }

    public boolean shouldInterceptTouchEvent(MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int i;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                float x;
                if (actionMasked == 2) {
                    actionMasked = MotionEventCompat.getPointerCount(motionEvent);
                    for (actionIndex = 0; actionIndex < actionMasked && this.mInitialMotionX != null && this.mInitialMotionY != null; actionIndex++) {
                        int pointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                        if (pointerId < this.mInitialMotionX.length && pointerId < this.mInitialMotionY.length) {
                            x = MotionEventCompat.getX(motionEvent, actionIndex);
                            x -= this.mInitialMotionX[pointerId];
                            float y = MotionEventCompat.getY(motionEvent, actionIndex) - this.mInitialMotionY[pointerId];
                            reportNewEdgeDrags(x, y, pointerId);
                            if (this.mDragState != 1) {
                                View findTopChildUnder = findTopChildUnder((int) this.mInitialMotionX[pointerId], (int) this.mInitialMotionY[pointerId]);
                                if (findTopChildUnder != null && checkTouchSlop(findTopChildUnder, x, y) && tryCaptureViewForDrag(findTopChildUnder, pointerId)) {
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    saveLastMotion(motionEvent);
                } else if (actionMasked != 3) {
                    if (actionMasked == 5) {
                        actionMasked = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                        x = MotionEventCompat.getX(motionEvent, actionIndex);
                        float y2 = MotionEventCompat.getY(motionEvent, actionIndex);
                        saveInitialMotion(x, y2, actionMasked);
                        actionIndex = this.mDragState;
                        if (actionIndex == 0) {
                            i = this.mInitialEdgesTouched[actionMasked];
                            actionIndex = this.mTrackingEdges;
                            if ((i & actionIndex) != 0) {
                                this.mCallback.onEdgeTouched(i & actionIndex, actionMasked);
                            }
                        } else if (actionIndex == 2) {
                            View findTopChildUnder2 = findTopChildUnder((int) x, (int) y2);
                            if (findTopChildUnder2 == this.mCapturedView) {
                                tryCaptureViewForDrag(findTopChildUnder2, actionMasked);
                            }
                        }
                    } else if (actionMasked == 6) {
                        clearMotionHistory(MotionEventCompat.getPointerId(motionEvent, actionIndex));
                    }
                }
            }
            cancel();
        } else {
            float x2 = motionEvent.getX();
            float y3 = motionEvent.getY();
            i = MotionEventCompat.getPointerId(motionEvent, 0);
            saveInitialMotion(x2, y3, i);
            View findTopChildUnder3 = findTopChildUnder((int) x2, (int) y3);
            if (findTopChildUnder3 == this.mCapturedView && this.mDragState == 2) {
                tryCaptureViewForDrag(findTopChildUnder3, i);
            }
            actionMasked = this.mInitialEdgesTouched[i];
            actionIndex = this.mTrackingEdges;
            if ((actionMasked & actionIndex) != 0) {
                this.mCallback.onEdgeTouched(actionMasked & actionIndex, i);
            }
        }
        if (this.mDragState == 1) {
            return true;
        }
        return false;
    }

    public void processTouchEvent(MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int i = 0;
        float x;
        float y;
        int pointerId;
        View view;
        if (actionMasked == 0) {
            x = motionEvent.getX();
            y = motionEvent.getY();
            pointerId = MotionEventCompat.getPointerId(motionEvent, 0);
            View findTopChildUnder = findTopChildUnder((int) x, (int) y);
            saveInitialMotion(x, y, pointerId);
            tryCaptureViewForDrag(findTopChildUnder, pointerId);
            actionMasked = this.mInitialEdgesTouched[pointerId];
            actionIndex = this.mTrackingEdges;
            if ((actionMasked & actionIndex) != 0) {
                this.mCallback.onEdgeTouched(actionMasked & actionIndex, pointerId);
            }
        } else if (actionMasked == 1) {
            if (this.mDragState == 1) {
                releaseViewForPointerUp();
            }
            cancel();
        } else if (actionMasked != 2) {
            if (actionMasked == 3) {
                if (this.mDragState == 1) {
                    dispatchViewReleased(0.0f, 0.0f);
                }
                cancel();
            } else if (actionMasked == 5) {
                actionMasked = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                float x2 = MotionEventCompat.getX(motionEvent, actionIndex);
                float y2 = MotionEventCompat.getY(motionEvent, actionIndex);
                saveInitialMotion(x2, y2, actionMasked);
                if (this.mDragState == 0) {
                    tryCaptureViewForDrag(findTopChildUnder((int) x2, (int) y2), actionMasked);
                    pointerId = this.mInitialEdgesTouched[actionMasked];
                    actionIndex = this.mTrackingEdges;
                    if ((pointerId & actionIndex) != 0) {
                        this.mCallback.onEdgeTouched(pointerId & actionIndex, actionMasked);
                    }
                } else if (isCapturedViewUnder((int) x2, (int) y2)) {
                    tryCaptureViewForDrag(this.mCapturedView, actionMasked);
                }
            } else if (actionMasked == 6) {
                actionMasked = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                if (this.mDragState == 1 && actionMasked == this.mActivePointerId) {
                    actionIndex = MotionEventCompat.getPointerCount(motionEvent);
                    while (i < actionIndex) {
                        int pointerId2 = MotionEventCompat.getPointerId(motionEvent, i);
                        if (pointerId2 != this.mActivePointerId) {
                            View findTopChildUnder2 = findTopChildUnder((int) MotionEventCompat.getX(motionEvent, i), (int) MotionEventCompat.getY(motionEvent, i));
                            view = this.mCapturedView;
                            if (findTopChildUnder2 == view && tryCaptureViewForDrag(view, pointerId2)) {
                                pointerId = this.mActivePointerId;
                                break;
                            }
                        }
                        i++;
                    }
                    pointerId = -1;
                    if (pointerId == -1) {
                        releaseViewForPointerUp();
                    }
                }
                clearMotionHistory(actionMasked);
            }
        } else if (this.mDragState == 1) {
            actionMasked = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
            y = MotionEventCompat.getX(motionEvent, actionMasked);
            x = MotionEventCompat.getY(motionEvent, actionMasked);
            float[] fArr = this.mLastMotionX;
            int i2 = this.mActivePointerId;
            actionIndex = (int) (y - fArr[i2]);
            actionMasked = (int) (x - this.mLastMotionY[i2]);
            dragTo(this.mCapturedView.getLeft() + actionIndex, this.mCapturedView.getTop() + actionMasked, actionIndex, actionMasked);
            saveLastMotion(motionEvent);
        } else {
            actionMasked = MotionEventCompat.getPointerCount(motionEvent);
            while (i < actionMasked) {
                actionIndex = MotionEventCompat.getPointerId(motionEvent, i);
                float x3 = MotionEventCompat.getX(motionEvent, i);
                x3 -= this.mInitialMotionX[actionIndex];
                float y3 = MotionEventCompat.getY(motionEvent, i) - this.mInitialMotionY[actionIndex];
                reportNewEdgeDrags(x3, y3, actionIndex);
                if (this.mDragState != 1) {
                    view = findTopChildUnder((int) this.mInitialMotionX[actionIndex], (int) this.mInitialMotionY[actionIndex]);
                    if (checkTouchSlop(view, x3, y3) && tryCaptureViewForDrag(view, actionIndex)) {
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            saveLastMotion(motionEvent);
        }
    }

    private void reportNewEdgeDrags(float f, float f2, int i) {
        int i2 = 1;
        if (!checkNewEdgeDrag(f, f2, i, 1)) {
            i2 = 0;
        }
        if (checkNewEdgeDrag(f2, f, i, 4)) {
            i2 |= 4;
        }
        if (checkNewEdgeDrag(f, f2, i, 2)) {
            i2 |= 2;
        }
        if (checkNewEdgeDrag(f2, f, i, 8)) {
            i2 |= 8;
        }
        if (i2 != 0) {
            int[] iArr = this.mEdgeDragsInProgress;
            iArr[i] = iArr[i] | i2;
            this.mCallback.onEdgeDragStarted(i2, i);
        }
    }

    private boolean checkNewEdgeDrag(float f, float f2, int i, int i2) {
        f = Math.abs(f);
        f2 = Math.abs(f2);
        boolean z = false;
        if (!((this.mInitialEdgesTouched[i] & i2) != i2 || (this.mTrackingEdges & i2) == 0 || (this.mEdgeDragsLocked[i] & i2) == i2 || (this.mEdgeDragsInProgress[i] & i2) == i2)) {
            int i3 = this.mTouchSlop;
            if (f > ((float) i3) || f2 > ((float) i3)) {
                if (f < f2 * 0.5f && this.mCallback.onEdgeLock(i2)) {
                    int[] iArr = this.mEdgeDragsLocked;
                    iArr[i] = iArr[i] | i2;
                    return false;
                } else if ((this.mEdgeDragsInProgress[i] & i2) == 0 && f > ((float) this.mTouchSlop)) {
                    z = true;
                }
            }
        }
        return z;
    }

    private boolean checkTouchSlop(View view, float f, float f2) {
        boolean z = false;
        if (view == null) {
            return false;
        }
        Object obj = this.mCallback.getViewHorizontalDragRange(view) > 0 ? 1 : null;
        Object obj2 = this.mCallback.getViewVerticalDragRange(view) > 0 ? 1 : null;
        if (obj != null && obj2 != null) {
            f = (f * f) + (f2 * f2);
            int i = this.mTouchSlop;
            if (f > ((float) (i * i))) {
                z = true;
            }
            return z;
        } else if (obj != null) {
            if (Math.abs(f) > ((float) this.mTouchSlop)) {
                z = true;
            }
            return z;
        } else {
            if (obj2 != null && Math.abs(f2) > ((float) this.mTouchSlop)) {
                z = true;
            }
            return z;
        }
    }

    public boolean checkTouchSlop(int i) {
        int length = this.mInitialMotionX.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (checkTouchSlop(i, i2)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTouchSlop(int i, int i2) {
        boolean z = false;
        if (!isPointerDown(i2)) {
            return false;
        }
        Object obj = (i & 1) == 1 ? 1 : null;
        Object obj2 = (i & 2) == 2 ? 1 : null;
        float f = this.mLastMotionX[i2] - this.mInitialMotionX[i2];
        float f2 = this.mLastMotionY[i2] - this.mInitialMotionY[i2];
        if (obj != null && obj2 != null) {
            f = (f * f) + (f2 * f2);
            i = this.mTouchSlop;
            if (f > ((float) (i * i))) {
                z = true;
            }
            return z;
        } else if (obj != null) {
            if (Math.abs(f) > ((float) this.mTouchSlop)) {
                z = true;
            }
            return z;
        } else {
            if (obj2 != null && Math.abs(f2) > ((float) this.mTouchSlop)) {
                z = true;
            }
            return z;
        }
    }

    public boolean isEdgeTouched(int i) {
        int length = this.mInitialEdgesTouched.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (isEdgeTouched(i, i2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEdgeTouched(int i, int i2) {
        return isPointerDown(i2) && (i & this.mInitialEdgesTouched[i2]) != 0;
    }

    public boolean isDragging() {
        return this.mDragState == 1;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        dispatchViewReleased(clampMag(VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    private void dragTo(int i, int i2, int i3, int i4) {
        int left = this.mCapturedView.getLeft();
        int top = this.mCapturedView.getTop();
        if (i3 != 0) {
            i = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, i, i3);
            this.mCapturedView.offsetLeftAndRight(i - left);
        }
        int i5 = i;
        if (i4 != 0) {
            i2 = this.mCallback.clampViewPositionVertical(this.mCapturedView, i2, i4);
            this.mCapturedView.offsetTopAndBottom(i2 - top);
        }
        int i6 = i2;
        if (i3 != 0 || i4 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, i5, i6, i5 - left, i6 - top);
        }
    }

    public boolean isCapturedViewUnder(int i, int i2) {
        return isViewUnder(this.mCapturedView, i, i2);
    }

    public boolean isViewUnder(View view, int i, int i2) {
        boolean z = false;
        if (view == null) {
            return false;
        }
        if (i >= view.getLeft() && i < view.getRight() && i2 >= view.getTop() && i2 < view.getBottom()) {
            z = true;
        }
        return z;
    }

    public View findTopChildUnder(int i, int i2) {
        for (int childCount = this.mParentView.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(childCount));
            if (i >= childAt.getLeft() && i < childAt.getRight() && i2 >= childAt.getTop() && i2 < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    private int getEdgesTouched(int i, int i2) {
        int i3 = i < this.mParentView.getLeft() + this.mEdgeSize ? 1 : 0;
        if (i2 < this.mParentView.getTop() + this.mEdgeSize) {
            i3 |= 4;
        }
        if (i > this.mParentView.getRight() - this.mEdgeSize) {
            i3 |= 2;
        }
        return i2 > this.mParentView.getBottom() - this.mEdgeSize ? i3 | 8 : i3;
    }
}
