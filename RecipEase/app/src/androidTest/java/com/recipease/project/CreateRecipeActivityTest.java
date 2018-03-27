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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringContains.containsString;


@RunWith(AndroidJUnit4.class)
public class CreateRecipeActivityTest{

    private String instructionString1;
    private String instructionString2;
    private String instructionStringBad;
    private String cookTimeString;
    private String ingredientString1;
    private String ingredientString2;
    private String recipeName;
    private String recipeNameBad;

    @Rule
    public ActivityTestRule<CreateRecipeActivity> activityRule = new ActivityTestRule<>(CreateRecipeActivity.class);


    @Before
    public void initRecipeName(){
        recipeName = "Lemon Meringue Pie";
        recipeNameBad = "Bitchin' Meringue Pie";
    }

    @Test
    public void addRecipeTitleTest(){
        onView(withId(R.id.etName)).perform(typeText(recipeName));
        onView(withId(R.id.etName)).perform(ViewActions.clearText());
        //onView(withId(R.id.etName)).perform(typeText(recipeNameBad));
    }

    @Before
    public void initCookTime(){
        cookTimeString = "45 minutes";
    }

    @Test
    public void addCookTimeTest(){
        onView(withId(R.id.cookTime)).perform(typeText(cookTimeString));
    }

    @Test
    public void difficultyButtonTest(){
        onView(withId(R.id.diff_3)).perform(ViewActions.scrollTo());
        onView(withId(R.id.diff_1)).perform(click());
        onView(withId(R.id.diff_2)).perform(click());
        onView(withId(R.id.diff_3)).perform(click());
    }

    @Before
    public void initValidInstructionString(){
        instructionString1 = "Mix until you have a glossy meringue with stiff peaks.";
        instructionString2 = "Sit the lemon curd in the fridge for 10 minutes to set.";
        instructionStringBad = "Mix that shit.";
    }

    @Test
    public void addInstructionToRecipe() throws Exception {

        onView(withId(R.id.button3)).perform(ViewActions.scrollTo());
        onView(withId(R.id.button3)).perform(click());
        onView(withText("I'm On It")).perform(click());

        onView(withId(R.id.etInstruction)).perform(ViewActions.scrollTo());
        onView(withId(R.id.etInstruction)).perform(click(), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etInstruction)).perform(typeText(instructionString1), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button3)).perform(click());
        onView(withId(R.id.tvInstructions)).check(matches(withText(containsString(instructionString1))));

        onView(withId(R.id.etInstruction)).perform(ViewActions.clearText());
        onView(withId(R.id.etInstruction)).perform(typeText(instructionStringBad), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etInstruction)).perform(click());
        onView(withId(R.id.tvInstructions)).check(matches(not(withText(containsString(instructionStringBad)))));

        onView(withId(R.id.etInstruction)).perform(ViewActions.clearText());
        onView(withId(R.id.etInstruction)).perform(click());
        onView(withId(R.id.etInstruction)).perform(typeText(instructionString2), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button3)).perform(click());
        onView(withId(R.id.tvInstructions)).check(matches(withText(containsString(instructionString2))));

    }

    @Before
    public void initIngredientStrings(){
        ingredientString1 = "Lemons";
        ingredientString2 = "Egg whites";
    }

    /*
    @Test
    public void addIngredientToRecipe() throws Exception {

        onView(withId(R.id.button6)).perform(ViewActions.scrollTo());
        onView(withId(R.id.button6)).perform(click()); //Still prints empty string

        onView(withId(R.id.etIngredient)).perform(click());
        onView(withId(R.id.etIngredient)).perform(typeText(ingredientString1), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button6)).perform(click());

        onView(withId(R.id.etIngredient)).perform(ViewActions.clearText());
        onView(withId(R.id.etIngredient)).perform(typeText(ingredientString2), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button6)).perform(click());

    }

    @Test
    public void removeIngredient() throws Exception {
        onView(withId(R.id.button6)).perform(ViewActions.scrollTo());
        onView(withId(R.id.etIngredient)).perform(click());
        onView(withId(R.id.etIngredient)).perform(typeText(ingredientString1), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button6)).perform(click());

        //onView(withId(R.id.button7)).perform(ViewActions.scrollTo());
        //onView(withId(R.id.button7)).perform(click());

        //onView(withId(R.id.button7)).perform(click());
    }
    */

    @Test
    public void removeInstruction() throws Exception {

        onView(withId(R.id.button3)).perform(ViewActions.scrollTo());
        onView(withId(R.id.etInstruction)).perform(click());
        onView(withId(R.id.etInstruction)).perform(typeText(instructionString1), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.button3)).perform(click());

        onView(withId(R.id.button5)).perform(ViewActions.scrollTo());
        onView(withId(R.id.button5)).perform(click());

        onView(withId(R.id.button5)).perform(click());
    }
}