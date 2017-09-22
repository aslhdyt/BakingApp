package me.asl.assel.bakingapp.ui.media;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by assel on 8/22/17.
 */

//source https://medium.com/tall-programmer/fullscreen-functionality-with-android-exoplayer-5fddad45509f
public class ExoPlayerVideoHandler {
    private static final String TAG_VIDEO_HANDLER = "VIDEO_HANDLER";
    private static ExoPlayerVideoHandler instance;


    public static ExoPlayerVideoHandler getInstance(){
        if(instance == null){
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }

    private SimpleExoPlayer player;
    private Uri playerUri;
    private boolean isPlayerPlaying;
    private long currentPos;

    private ExoPlayerVideoHandler(){}

    //i was attempt to create ExoPlayer go full screen. but keeps failing with blank PLayerView (but audio is intact)
    //i ask my mentor for help but then he said is not required in the rubrics. so i pass for creating fullscreen for now..

    public void prepareExoPlayerForUri(Context context, Uri uri,
                                       SimpleExoPlayerView exoPlayerView){
        if(context != null && uri != null && exoPlayerView != null){
            Log.d(TAG_VIDEO_HANDLER, "uri/playerUri: "+uri+" / "+playerUri+
                "\nis not equal? = "+!uri.equals(playerUri));
            if(!uri.equals(playerUri) || player == null){
                Log.d(TAG_VIDEO_HANDLER, "CREATE PLAYER");
                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);

                // Prepare the MediaSource.
                if (!uri.equals(playerUri)) {
                    currentPos = 0;
                }
                playerUri = uri;
                String userAgent = Util.getUserAgent(context, "BakingApp");
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                MediaSource mediaSource = new ExtractorMediaSource(
                        playerUri,
                        dataSourceFactory,
                        extractorsFactory,
                        null,
                        null
                );
                player.prepare(mediaSource);
            }
            player.clearVideoSurface();
            player.setVideoSurfaceView(
                    (SurfaceView)exoPlayerView.getVideoSurfaceView());
            player.seekTo(currentPos + 1);
            exoPlayerView.setControllerShowTimeoutMs(3000);
            exoPlayerView.setPlayer(player);
        }
    }
    public void releaseVideoPlayer(){
        if(player != null) {
            Log.e(TAG_VIDEO_HANDLER, "releaseVideoPlayer" +
                    "\ncurrentPos "+player.getCurrentPosition()+"/"+player.getDuration());
            currentPos = player.getCurrentPosition();
            player.release();
        }
        player = null;
    }

    public void goToBackground(){
        if(player != null){
            Log.e(TAG_VIDEO_HANDLER, "goToBackground" +
                    "\ncurrentPos "+player.getCurrentPosition()+"/"+player.getDuration());
            isPlayerPlaying = player.getPlayWhenReady();
            player.setPlayWhenReady(false);
        }
    }

    public void goToForeground(){
        if(player != null){
            player.setPlayWhenReady(isPlayerPlaying);
            Log.e(TAG_VIDEO_HANDLER, "goToForeground" +
                    "\ncurrentPos "+player.getCurrentPosition()+"/"+player.getDuration());
        }
    }
}
