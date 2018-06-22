package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.develop_for_android.bakingapp.database.RecipeEntry;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = fragmentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new RecipeAdapter(getContext());
        recyclerView.setAdapter(adapter);
        Log.i("MainActivityFragment", "fragment ready");

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
