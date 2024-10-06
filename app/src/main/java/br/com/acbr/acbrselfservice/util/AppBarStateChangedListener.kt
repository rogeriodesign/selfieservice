package br.com.acbr.acbrselfservice.util

import com.google.android.material.appbar.AppBarLayout

abstract class AppBarStateChangedListener : AppBarLayout.OnOffsetChangedListener {
    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    private var mCurrentState = State.IDLE
    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        //toolbar_info.alpha = (appBarLayout.totalScrollRange + verticalOffset).toFloat() / appBarLayout.totalScrollRange
        if (verticalOffset == 0) {
            setCurrentStateAndNotify(appBarLayout, State.EXPANDED)
        } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            setCurrentStateAndNotify(appBarLayout, State.COLLAPSED)
        } else {
            setCurrentStateAndNotify(appBarLayout, State.IDLE)
        }
    }

    private fun setCurrentStateAndNotify(appBarLayout: AppBarLayout, state: State) {
        if (mCurrentState != state) {
            onStateChanged(appBarLayout, state)
        }
        mCurrentState = state
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State?)
}