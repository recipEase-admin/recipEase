package com.recipease.project;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    private String emailString;
    private String passwordString;
    private String nameString;
    private String bioString;

    @Rule
    public ActivityTestRule<RegisterActivity> activityRule = new ActivityTestRule<>(RegisterActivity.class);

    @Before
    public void initRegistrationFields(){

        emailString = "email@email.com";
        passwordString = "password";
        nameString = "display";
        bioString = "bio";
    }

    @Test
    public void registerTest(){

        onView(withId(R.id.etEmail)).perform(typeText(emailString), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create)).perform(click());
        onView(withText("Try again")).perform(click());

        onView(withId(R.id.etPassword)).perform(typeText(passwordString), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create)).perform(click());
        onView(withText("Try again")).perform(click());

        onView(withId(R.id.etDisplayName)).perform(typeText(nameString), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create)).perform(click());
        onView(withText("Try again")).perform(click());

        onView(withId(R.id.etBio)).perform(typeText(bioString), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create)).perform(click());
        onView(withText("Try again")).perform(click());

    }

}