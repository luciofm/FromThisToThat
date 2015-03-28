package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.CircularPropagation;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.anim.YFractionProperty;
import com.luciofm.presentation.droiconit.transitions.NoTransition;
import com.luciofm.presentation.droiconit.transitions.TransitionListenerAdapter;
import com.luciofm.presentation.droiconit.util.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SceneFragment extends BaseFragment {

    @InjectView(R.id.container)
    ViewGroup container;
    @InjectView(R.id.titleContainer)
    View titleContainer;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.checkMark)
    View checkMark;

    @InjectView(R.id.root)
    ViewGroup root;

    @InjectView(R.id.textSwitcher)
    TextSwitcher textSwitcher;

    Scene scene1;
    Scene scene2;

    public SceneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_scene;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        textSwitcher.setInAnimation(getActivity(), android.R.anim.fade_in);
        textSwitcher.setOutAnimation(getActivity(), android.R.anim.fade_out);

        scene1 = new Scene(root, (ViewGroup) root.findViewById(R.id.sceneContainer));
        scene2 = Scene.getSceneForLayout(root, R.layout.scene_scene2, getActivity());

        return v;
    }

    @Override
    public void onNextPressed() {
        TransitionSet image;
        TransitionSet set;
        TransitionSet set2;
        switch (++currentStep) {
            case 2:
                container.setLayoutTransition(new LayoutTransition());
                titleContainer.animate().scaleY(0.5f).scaleX(0.5f);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleContainer.getLayoutParams();
                params.topMargin = Utils.dpToPx(getActivity(), 40) * -1;
                titleContainer.setLayoutParams(params);

                root.setVisibility(View.VISIBLE);
                textSwitcher.setVisibility(View.VISIBLE);
                textSwitcher.setText("Layout 1");
                break;
            case 3:
                container.setLayoutTransition(null);
                TransitionManager.go(scene2, new NoTransition());
                textSwitcher.setText("Layout 2");
                break;
            case 4:
                TransitionManager.go(scene1, new NoTransition());
                textSwitcher.setText("Scene 1");
                break;
            case 5:
                image = new TransitionSet();
                image.setOrdering(TransitionSet.ORDERING_TOGETHER);
                image.addTransition(new ChangeImageTransform()).addTransition(new ChangeBounds()).addTransition(new Fade(Fade.OUT));
                set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
                set.addTransition(image).addTransition(new Fade(Fade.IN));
                TransitionManager.go(scene2, set);
                textSwitcher.setText("Scene 2");
                break;
            case 6:
                image = new TransitionSet();
                image.setOrdering(TransitionSet.ORDERING_TOGETHER);
                image.addTransition(new ChangeImageTransform()).addTransition(new ChangeBounds());
                set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
                set.addTransition(new Fade(Fade.IN)).addTransition(image);
                set.excludeTarget(R.id.checkMark, true);
                checkMark.setAlpha(0);
                set.addListener(new TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        checkMark.animate().alpha(1f);
                    }
                });
                TransitionManager.go(scene1, set);
                textSwitcher.setText("Scene 1");
                break;
            case 7:
                checkMark.animate().alpha(0f);
                image = new TransitionSet();
                image.setOrdering(TransitionSet.ORDERING_TOGETHER);
                image.addTransition(new ChangeImageTransform()).addTransition(new ChangeBounds());;
                set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
                set.addTransition(image).addTransition(new Slide(Gravity.BOTTOM));
                set.excludeTarget(R.id.checkMark, true);
                TransitionManager.go(scene2, set);
                textSwitcher.setText("Scene 2 - sliding");
                break;
            case 8:
                image = new TransitionSet();
                image.setOrdering(TransitionSet.ORDERING_TOGETHER);
                image.addTransition(new ChangeImageTransform()).addTransition(new ChangeBounds());;
                set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
                set.addTransition(new Slide(Gravity.BOTTOM)).addTransition(image);
                set.excludeTarget(R.id.checkMark, true);
                set.addListener(new TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        checkMark.animate().alpha(1f);
                    }
                });
                TransitionManager.go(scene1, set);
                textSwitcher.setText("Scene 1");
                break;
            case 9:
                checkMark.animate().alpha(0f);
                Slide slide = new Slide();
                slide.setSlideEdge(Gravity.BOTTOM);
                slide.setPropagation(new CircularPropagation() {
                    @Override
                    public long getStartDelay(ViewGroup sceneRoot, Transition transition, TransitionValues startValues, TransitionValues endValues) {
                        long delay = super.getStartDelay(sceneRoot, transition, startValues, endValues);
                        return delay * 8;
                    }
                });
                slide.setEpicenterCallback(new Transition.EpicenterCallback() {
                    @Override
                    public Rect onGetEpicenter(Transition transition) {
                        int[] loc = new int[2];
                        root.getLocationOnScreen(loc);

                        return new Rect((root.getWidth() / 2) - 40, loc[1], (root.getWidth() / 2) + 40, loc[1] + 40);
                    }
                });
                image = new TransitionSet();
                image.setOrdering(TransitionSet.ORDERING_TOGETHER);
                image.addTransition(new ChangeImageTransform()).addTransition(new ChangeBounds());;
                set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
                set.addTransition(image).addTransition(slide);
                set.excludeTarget(R.id.checkMark, true);
                TransitionManager.go(scene2, set);
                textSwitcher.setText("Scene 2 - sliding delayed");
            default:
                ((BaseActivity) getActivity()).nextFragment();
        }
    }

    @OnClick(R.id.container)
    public void onClick() {
        onNextPressed();
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (transit == 0 | enter) {
            return null;
        }

        //Target will be filled in by the framework
        return enter ? ObjectAnimator.ofFloat(null, new YFractionProperty(), 1f, 0f) :
                ObjectAnimator.ofFloat(null, new YFractionProperty(), 0f, -1f);
    }

}