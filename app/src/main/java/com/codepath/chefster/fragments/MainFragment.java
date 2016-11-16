package com.codepath.chefster.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.activities.DishListActivity;
import com.codepath.chefster.adapters.CategoryAdapter;
import com.codepath.chefster.models.Categories;
import com.codepath.chefster.utils.ItemClickSupport;
import com.codepath.chefster.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnMainFragmentInteractionListener mListener;
    private List<Categories> categoriesList;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @BindView(R.id.rvCategoryGrid) RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);
        initializeRecyclerView();

        ItemClickSupport.addTo(rvCategories).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Categories categories = categoriesList.get(position);
                Intent intent = new Intent(getActivity(), DishListActivity.class);
                intent.putExtra("category_name", categories.getCategoryName());
                startActivity(intent);
            }
        });
        return view;
    }

    private void initializeRecyclerView(){
        StaggeredGridLayoutManager manager  = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rvCategories.setLayoutManager(manager);
        rvCategories.setItemAnimator(new SlideInUpAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        rvCategories.addItemDecoration(decoration);
        categoryAdapter = new CategoryAdapter(getActivity(), getCategoryList());
        rvCategories.setAdapter(categoryAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMainFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            mListener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMainFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMainFragmentInteraction(Uri uri);
    }

    //Will give the list of categories.
    //for now its hard coded, later can get from database
    private List<Categories> getCategoryList(){
        try {
            categoriesList = new ArrayList<>();
            categoriesList.add(new Categories("American",R.drawable.american_category));
//            categoriesList.add(new Categories("Italian",""));
//            categoriesList.add(new Categories("Vietnamese",""));
//            categoriesList.add(new Categories("Chinese",""));
            categoriesList.add(new Categories("Israeli",R.drawable.israeli_dish));
            categoriesList.add(new Categories("Side Dish",R.drawable.side_dish));
//            categoriesList.add(new Categories("Thai",""));
//            categoriesList.add(new Categories("Spanish",""));
//            categoriesList.add(new Categories("French",""));
//            categoriesList.add(new Categories("Japanese",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoriesList;
    }
}
