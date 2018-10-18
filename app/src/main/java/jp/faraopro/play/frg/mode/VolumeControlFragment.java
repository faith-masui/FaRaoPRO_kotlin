package jp.faraopro.play.frg.mode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;

public class VolumeControlFragment extends Fragment implements IModeFragment {
    private static final int MINIMUM = 20;
    private static final int MAXIMUM = 100;
    private static final int INFLATER = MAXIMUM - MINIMUM;
    private SeekBar channelVolumeController;
    private SeekBar interruptVolumeController;
    private SeekBar audioFocusVolumeController;
    // private TextView channelVolumeValue;
    // private TextView interruptVolumeValue;

    public static VolumeControlFragment newInstance() {
        return new VolumeControlFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_volume_control, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button reset = (Button) view.findViewById(R.id.button_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FROForm.getInstance().changeInterruptVolume("100");
                FROForm.getInstance().changeChannelVolume("100");
                FROForm.getInstance().changeAudioFocusedVolume(10);
                updateViews();
            }
        });

        // 1st line for interrupt volume control
        // interruptVolumeValue = (TextView)
        // view.findViewById(R.id.text_volume_value_second);
        interruptVolumeController = (SeekBar) view.findViewById(R.id.seek_first);
        interruptVolumeController.setMax(INFLATER);
        interruptVolumeController.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String value = Integer.toString(progress + MINIMUM);
                // interruptVolumeValue.setText(value + "%");
                FROForm.getInstance().changeInterruptVolume(value);
            }
        });
        // 2nd line for channel volume control
        // channelVolumeValue = (TextView)
        // view.findViewById(R.id.text_volume_value_first);
        channelVolumeController = (SeekBar) view.findViewById(R.id.seek_second);
        channelVolumeController.setMax(INFLATER);
        channelVolumeController.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String value = Integer.toString(progress + MINIMUM);
                // channelVolumeValue.setText(value + "%");
                FROForm.getInstance().changeChannelVolume(value);
            }
        });
        // 3rd line for channel volume control
        //audioFocusVolumeValue = (TextView) view.findViewById(R.id.text_volume_value_third);
        audioFocusVolumeController = (SeekBar) view.findViewById(R.id.seek_third);
        audioFocusVolumeController.setMax(MAXIMUM);
        audioFocusVolumeController.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //if (Flavor.DEVELOPER_MODE) {
                //    FROUtils.showToast(getActivity(), seekBar.getProgress() + "%");
                //}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                audioFocusVolumeValue.setText(progress + "%");
                FROForm.getInstance().changeAudioFocusedVolume(progress);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            updateViews();
        }
    }

    @Override
    public void getData() {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void show() {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void updateViews() {
        if (interruptVolumeController != null) {
            int percent = (int) (FROForm.getInstance().getInterruptVolume() * 100);
            // interruptVolumeValue.setText(Integer.toString(percent) + "%");
            percent = percent - MINIMUM;
            interruptVolumeController.setProgress(percent);
        }
        if (channelVolumeController != null) {
            int percent = (int) (FROForm.getInstance().getChannelVolume() * 100);
            // channelVolumeValue.setText(Integer.toString(percent) + "%");
            percent = percent - MINIMUM;
            channelVolumeController.setProgress(percent);
        }
        //if (audioFocusVolumeController != null) {
        //    int percent = (int) (FROForm.getInstance().getAudioFocusedVolume() * 100);
        //    audioFocusVolumeController.setProgress(percent);
        //}
    }

    @Override
    public void onBackPress() {
        ((MainActivity) getActivity()).showMode(MainActivity.SETTING);
    }

    @Override
    public void onError(int when, int code) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
