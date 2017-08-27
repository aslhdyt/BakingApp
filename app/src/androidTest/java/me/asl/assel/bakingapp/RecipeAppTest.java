package me.asl.assel.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import me.asl.assel.bakingapp.model.Recipe;
import me.asl.assel.bakingapp.ui.MainActivity;
import me.asl.assel.bakingapp.ui.SplashActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class RecipeAppTest {

    private Context mockContext;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setup() {
        mockContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void potraitOrientationTest () {
        ArrayList<Recipe> mockList = (ArrayList<Recipe>) SplashActivity.localJson(mockContext);
        Intent i = new Intent();
        i.putParcelableArrayListExtra(SplashActivity.DATA, mockList);
        mActivityRule.launchActivity(i);


        onView(withId(R.id.recyclerView_main))
                .check(matches(isDisplayed()))
                .perform(actionOnItemAtPosition(0, click()));;

        String string = mockContext.getString(R.string.ingredients);
        onView(withId(R.id.listView))
                .check(matches(isDisplayed()));
        onData(allOf(instanceOf(String.class), is(string)))
                .perform(click());
        try {
            //idk how to loop with Espresso Test, what i want to do is:
            // while (isEnabled) {click()}, but Matcher is not boolean
            while (true) {
                onView(withId(R.id.button_next))
                        .check(matches(isEnabled()))
                        .perform(click());
            }
        } catch (Throwable e) {
            // so i try-catch it on while loop;
            e.printStackTrace();
            onView(isRoot())
                    .perform(pressBack());
        }
    }


}
