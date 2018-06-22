package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.develop_for_android.bakingapp.database.RecipeEntry;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecipeAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);
        int spanCount;
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) >
                    Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                spanCount = 3;
            } else {
                spanCount = 2;
            }
        } else {
            spanCount = 1;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        adapter = new RecipeAdapter(getContext(), new ItemClickListener() {
            @Override
            public void onItemClick(int itemId, String title) {
                Intent openDetails = new Intent(requireContext(), RecipeDetailActivity.class);
                openDetails.putExtra(RecipeDetailFragment.RECIPE_ID_KEY, itemId);
                openDetails.putExtra(RecipeDetailActivity.RECIPE_TITLE_KEY, title);
                startActivity(openDetails);
            }
        });
        recyclerView.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);

        setupViewModel();

        return fragmentView;
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                adapter.setRecipes(recipeEntries);
            }
        });
    }
}
