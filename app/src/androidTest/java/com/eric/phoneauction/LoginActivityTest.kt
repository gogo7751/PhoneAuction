package com.eric.phoneauction


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.eric.phoneauction.data.UserManager
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {


    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatButton = onView(
            allOf(
                withId(R.id.facebook_login_button), withText("使用 Facebook 登入"),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_login_down),
                        childAtPosition(
                            withId(R.id.constraint_login_up),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        Thread.sleep(7000)
        appCompatButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val recyclerView = onView(
            allOf(
                withId(R.id.recyclerview_home)

            )
         )

        Thread.sleep(2000)
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.button_auction_post), withText("我要出價"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.myNavHostFragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())
        Thread.sleep(3000)

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.button_auction_minimal), withText("最低出價"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())
        Thread.sleep(3000)

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.button_auction_post), withText("我要出價"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        0
                    ),
                    14
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())
        Thread.sleep(3000)

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.button_checkout_success_shopping), withText("再去逛逛"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_checkout_success_bottom),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())
        Thread.sleep(3000)
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
