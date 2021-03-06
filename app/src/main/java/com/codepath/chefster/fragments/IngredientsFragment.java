package com.codepath.chefster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.adapters.IngredientsAdapter;
import com.codepath.chefster.models.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends BaseFragment {
    @BindView(R.id.rvIngredients)
    RecyclerView rvIngredients;

    final static private String ARGUMENT = "ingredients";

    private List<Ingredient> ingredientList;
    private IngredientsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ingredientList = (List) getArguments().getParcelableArrayList(ARGUMENT);
        getArguments().remove(ARGUMENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        final View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this, view);

        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        adapter = new IngredientsAdapter(getActivity(), ingredientList);
        rvIngredients.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvIngredients.setLayoutManager(manager);
    }
}
