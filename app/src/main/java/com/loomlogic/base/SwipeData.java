package com.loomlogic.base;

/**
 * Created by alex on 3/10/17.
 */

public class SwipeData {
    private boolean mPinned;
    private boolean mPinnedToLeft;

    public int getPinnedPosition() {
        return mPinnedPosition;
    }

    public void setPinnedPosition(int mPinnedPosition) {
        this.mPinnedPosition = mPinnedPosition;
    }

    private int mPinnedPosition;

    public boolean isPinnedToLeft() {
        return mPinnedToLeft;
    }

    public void setPinnedToLeft(boolean mPinnedToLeft) {
        this.mPinnedToLeft = mPinnedToLeft;
    }

    public boolean isPinned() {
        return mPinned;
    }

    public void setPinned(boolean pinned) {
        mPinned = pinned;
    }
}
