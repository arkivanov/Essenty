package com.arkivanov.essenty.backhandler

/**
 * Represents an event of the predictive back gesture.
 *
 * @param progress progress factor of the back gesture, must be between 0 and 1.
 * @param swipeEdge Indicates which edge the gesture is being performed from.
 * @param touchX absolute X location of the touch point of this event.
 * @param touchY absolute Y location of the touch point of this event.
 */
class BackEvent(
    val progress: Float = 0F,
    val swipeEdge: SwipeEdge = SwipeEdge.UNKNOWN,
    val touchX: Float = 0F,
    val touchY: Float = 0F,
) {

    init {
        require(progress in 0F..1F) { "The 'progress' argument must be between 0 and 1 (both inclusive)" }
    }

    enum class SwipeEdge {
        UNKNOWN,
        LEFT,
        RIGHT,
        BOTTOM,
    }
}
