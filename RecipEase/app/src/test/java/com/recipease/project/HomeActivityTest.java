package com.recipease.project;


import android.os.Bundle;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HomeActivityTest {

    private HomeActivity homeActivity;

    @Before
    public void setUp(){
        this.homeActivity = Mockito.spy(new HomeActivity());
    }

    @Test
    public void goToFindRecipesPageTest() throws Exception{

        View mockView = Mockito.mock(View.class);
        homeActivity.goToFindRecipesPage(mockView);
        Mockito.verify(homeActivity).goToFindRecipesPage(mockView);

    }

    @Test
    public void navigationBarTest() throws Exception{

        Bundle mockBundle = Mockito.mock(Bundle.class);
        homeActivity.onCreate(mockBundle);
        Mockito.verify(homeActivity).onCreate(mockBundle);

        //Need to figure out how to test functions inside of onCreate constructor

    }

}