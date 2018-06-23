package ru.develop_for_android.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import ru.develop_for_android.bakingapp.database.CookingStepEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class CookingStepDetailsFragment extends Fragment {

    TextView stepDescriptionView;
    PlayerView exoPlayer;
    ImageView stepImage;

    public CookingStepDetailsFragment() {
    }

    private void setupViewModel() {
        RecipeDetailsViewModel viewModel = ViewModelProviders.of(requireActivity())
                .get(RecipeDetailsViewModel.class);
        viewModel.getStepEntry().observe(this, new Observer<CookingStepEntry>() {
            @Override
            public void onChanged(@Nullable CookingStepEntry stepEntry) {
                if (stepEntry == null) {
                    return;
                }
                String description = stepEntry.getDescription();
                stepDescriptionView.setText(description);
                String imageAddress = stepEntry.getThumbnailUrl();
                if (imageAddress == null || imageAddress.equals("")) {
                    stepImage.setVisibility(View.GONE);
                } else {
                    stepImage.setVisibility(View.VISIBLE);
                    Glide.with(stepImage).load(imageAddress).into(stepImage);
                }

                //BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector =
                        new DefaultTrackSelector(videoTrackSelectionFactory);

                SimpleExoPlayer player =
                        ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireContext(),
                        Util.getUserAgent(requireContext(), "BakingApp"), bandwidthMeter);
                // This is the MediaSource representing the media to be played.
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(stepEntry.getVideoUrl()));
                // Prepare the player with the source.
                player.prepare(videoSource);

                exoPlayer.setPlayer(player);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_cooking_step_details,
                container, false);
        stepDescriptionView = fragmentView.findViewById(R.id.step_description);
        exoPlayer = fragmentView.findViewById(R.id.step_video);
        stepImage = fragmentView.findViewById(R.id.step_image);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }
}
