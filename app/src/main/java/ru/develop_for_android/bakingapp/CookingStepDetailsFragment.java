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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * A placeholder fragment containing a simple view.
 */
public class CookingStepDetailsFragment extends Fragment {

    private static final String KEY_PLAYER_POSITION = "player_position";
    private static final String KEY_PLAYING_STATE = "player_state";

    private long oldPoisition;
    private boolean playingState;

    TextView stepDescriptionView;
    PlayerView exoPlayer;
    ImageView stepImage;

    LinearLayout.LayoutParams portraitParams;

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

                String videoAddress = stepEntry.getVideoUrl();
                if (videoAddress == null || videoAddress.equals("")) {
                    exoPlayer.setVisibility(View.GONE);
                } else {
                    exoPlayer.setVisibility(View.VISIBLE);
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
                    player.seekTo(oldPoisition);
                    player.setPlayWhenReady(playingState);

                    exoPlayer.setPlayer(player);
                }
            }
        });
    }

    public void showVideoOnFullScreen() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) exoPlayer.getLayoutParams();
        params.width=MATCH_PARENT;
        params.height= MATCH_PARENT;
        exoPlayer.setLayoutParams(params);
    }

    public void exitVideoFullScreen() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) exoPlayer.getLayoutParams();
        params.width=MATCH_PARENT;
        params.height= 0;
        params.weight = 3;
        exoPlayer.setLayoutParams(params);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_cooking_step_details,
                container, false);
        stepDescriptionView = fragmentView.findViewById(R.id.step_description);
        exoPlayer = fragmentView.findViewById(R.id.step_video);
        stepImage = fragmentView.findViewById(R.id.step_image);
        portraitParams = (LinearLayout.LayoutParams) exoPlayer.getLayoutParams();
        if (savedInstanceState != null) {
            oldPoisition = savedInstanceState.getLong(KEY_PLAYER_POSITION);
            playingState = savedInstanceState.getBoolean(KEY_PLAYING_STATE);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        exoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void releasePlayer() {
        Player player = exoPlayer.getPlayer();
        if (player != null) {
            player.stop();
            player.release();
            exoPlayer.setPlayer(null);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(KEY_PLAYER_POSITION, exoPlayer.getPlayer().getContentPosition());
        outState.putBoolean(KEY_PLAYING_STATE, exoPlayer.getPlayer().getPlayWhenReady());
        super.onSaveInstanceState(outState);
    }
}
