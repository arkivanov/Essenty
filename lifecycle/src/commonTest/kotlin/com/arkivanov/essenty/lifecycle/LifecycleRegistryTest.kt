package com.arkivanov.essenty.lifecycle

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class LifecycleRegistryTest {

    private val registry = LifecycleRegistryImpl(initialState = Lifecycle.State.INITIALIZED)

    @Test
    fun WHEN_called_THEN_calls_subscribers_in_correct_order() {
        val events = ArrayList<String>()

        fun callbacks(name: String): Lifecycle.Callbacks =
            object : Lifecycle.Callbacks {
                override fun onCreate() {
                    events += "onCreate $name"
                }

                override fun onStart() {
                    events += "onStart $name"
                }

                override fun onResume() {
                    events += "onResume $name"
                }

                override fun onPause() {
                    events += "onPause $name"
                }

                override fun onStop() {
                    events += "onStop $name"
                }

                override fun onDestroy() {
                    events += "onDestroy $name"
                }
            }

        registry.subscribe(callbacks(name = "1"))
        registry.subscribe(callbacks(name = "2"))

        registry.onCreate()
        registry.onStart()
        registry.onResume()
        registry.onPause()
        registry.onStop()
        registry.onDestroy()

        assertEquals(
            listOf(
                "onCreate 1",
                "onCreate 2",
                "onStart 1",
                "onStart 2",
                "onResume 1",
                "onResume 2",
                "onPause 2",
                "onPause 1",
                "onStop 2",
                "onStop 1",
                "onDestroy 2",
                "onDestroy 1"
            ),
            events
        )
    }

    @Test
    fun WHEN_unsubscribed_and_called_THEN_callbacks_not_called() {
        val events = ArrayList<String>()

        val callbacks =
            object : Lifecycle.Callbacks {
                override fun onCreate() {
                    events += "onCreate"
                }
            }

        registry.subscribe(callbacks)
        registry.unsubscribe(callbacks)
        registry.onCreate()

        assertEquals(emptyList(), events)
    }

    @Test
    fun WHEN_unsubscribed_from_callback_and_called_THEN_callbacks_not_called() {
        val events = ArrayList<String>()

        val callbacks =
            object : Lifecycle.Callbacks {
                override fun onCreate() {
                    registry.unsubscribe(this)
                }

                override fun onStart() {
                    events += "onStart"
                }
            }

        registry.subscribe(callbacks)
        registry.onCreate()
        registry.onStart()

        assertEquals(emptyList(), events)
    }

    @Test
    fun WHEN_created_with_initial_state_THEN_state_returns_that_state() {
        val registry = LifecycleRegistryImpl(initialState = Lifecycle.State.RESUMED)

        assertEquals(Lifecycle.State.RESUMED, registry.state)
    }

    @Test
    fun GIVEN_created_with_initial_state_WHEN_subscribed_THEN_callbacks_called() {
        val registry = LifecycleRegistryImpl(initialState = Lifecycle.State.RESUMED)
        val events = ArrayList<String>()

        val callbacks =
            object : Lifecycle.Callbacks {
                override fun onCreate() {
                    events += "onCreate"
                }

                override fun onStart() {
                    events += "onStart"
                }

                override fun onResume() {
                    events += "onResume"
                }
            }

        registry.subscribe(callbacks)

        assertEquals(listOf("onCreate", "onStart", "onResume"), events)
    }
}
