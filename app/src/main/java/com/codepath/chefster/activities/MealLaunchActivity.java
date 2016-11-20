package com.codepath.chefster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.adapters.FavoritesListAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealLaunchActivity extends BaseActivity {
    @BindView(R.id.recycler_view_chosen_dishes) RecyclerView dishesRecyclerView;
    @BindView(R.id.pans_spinner) Spinner pansSpinner;
    @BindView(R.id.pots_spinner) Spinner potsSpinner;
    @BindView(R.id.checkbox_oven) CheckBox ovenCheckbox;
    @BindView(R.id.text_view_person) TextView onePersonTextView;
    @BindView(R.id.text_view_persons) TextView morePeopleTextView;
    @BindView(R.id.text_view_regular_time_calc) TextView regularTimeTextView;
    @BindView(R.id.text_view_app_time_calc) TextView appTimeTextView;

    @BindColor(android.R.color.black) int chosenColor;
    @BindColor(android.R.color.white) int unchosenColor;

    List<Dish> chosenDishes;
    FavoritesListAdapter dishesAdapter;
    int numberOfPeople = 1;
    int numberOfPans = 1;
    int numberOfPots = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_launch);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupSpinners();

        onePersonTextView.setBackgroundColor(chosenColor);
        onePersonTextView.setTextColor(unchosenColor);

        calculateCookingTime();
    }

    private void setupRecyclerView() {
        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));
        dishesAdapter = new FavoritesListAdapter(this, chosenDishes);
        dishesRecyclerView.setAdapter(dishesAdapter);
        // Setup layout manager for items with orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Attach layout manager to the RecyclerView
        dishesRecyclerView.setLayoutManager(layoutManager);
    }

    private void setupSpinners() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.amount_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        pansSpinner.setAdapter(adapter);
        pansSpinner.setSelection(numberOfPans - 1);
        pansSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberOfPans = i + 1;
                calculateCookingTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        potsSpinner.setAdapter(adapter);
        potsSpinner.setSelection(numberOfPots - 1);
        potsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberOfPots = i + 1;
                calculateCookingTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * This method calculates the time it will take to cook this meal sequentially (without the app)
     * and how much it will take 1/2 people to cook it with the help of the app
     */
    private void calculateCookingTime() {
        // Prep time is for steps that demand preparation are defined as "Busy" steps, and Cooking time
        // is for steps that demands only a partial attention, like paste on stove or baking something
        // in the oven
        int totalPrepTime = 0, totalCookingTime = 0, totalOptimizedTime = 0, totalAggregatedTime = 0;
        for (Dish dish : chosenDishes) {
            List<Step> stepsList = dish.getSteps();
            for (Step step : stepsList) {
                if (step.getType().equals("Prep")) {
                    // This is a busy step and we aggregate the entire step time, divided by the amount
                    // of people cooking
                    totalPrepTime += (step.getDurationTime() / numberOfPeople);
                } else {
                    // This is not a busy step so we calculate the time it takes according to the
                    // step time. The bigger the step, the longer time a cook would have to dedicate
                    // to it
                    totalCookingTime += (step.getDurationTime() < 5 ? 2 :
                            (step.getDurationTime() < 10 ? 4 :
                            (step.getDurationTime() < 20 ? 6 : 10)));
                }
            }

            // If you cook sequentially, add 10 minutes between dishes to breathe a little :)
            totalAggregatedTime += (dish.getCooking_time() + dish.getPrep_time() + 10);
        }
        // Remove the last 10 minutes that were added because it's a wrong calculation
        totalAggregatedTime -= 10;
        totalAggregatedTime -= (numberOfPeople > 1 ? (totalAggregatedTime / 5) : 0);
        totalOptimizedTime = totalPrepTime + totalCookingTime;

        regularTimeTextView.setText(getBetterFormat(totalAggregatedTime));
        appTimeTextView.setText(getBetterFormat(totalOptimizedTime));
    }

    private CharSequence getBetterFormat(int timeInMinutes) {
        return "" + (timeInMinutes / 60) + "h" + (timeInMinutes % 60) + "m";
    }

    @OnClick(R.id.text_view_person)
    public void chooseOnePerson() {
        onePersonTextView.setBackgroundColor(chosenColor);
        onePersonTextView.setTextColor(unchosenColor);
        morePeopleTextView.setBackgroundColor(unchosenColor);
        morePeopleTextView.setTextColor(chosenColor);
        numberOfPeople = 1;
        calculateCookingTime();
    }

    @OnClick(R.id.text_view_persons)
    public void chooseMorePeople() {
        onePersonTextView.setBackgroundColor(unchosenColor);
        onePersonTextView.setTextColor(chosenColor);
        morePeopleTextView.setBackgroundColor(chosenColor);
        morePeopleTextView.setTextColor(unchosenColor);
        numberOfPeople = 2;
        calculateCookingTime();
    }

    @OnClick(R.id.button_start_cooking)
    public void startCooking() {
        Intent intent = new Intent(this, ProgressActivity.class);
        intent.putExtra(ChefsterApplication.SELECTED_DISHES_KEY, Parcels.wrap(chosenDishes));
        intent.putExtra("number_of_people", numberOfPeople);
        intent.putExtra("number_of_pans", numberOfPans);
        intent.putExtra("number_of_pots", numberOfPots);
        startActivity(intent);
    }
}
