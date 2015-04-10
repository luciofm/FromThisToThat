package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.transition.CircularPropagation;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.activity.MovieDetailsActivity;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.model.Movie;
import com.luciofm.presentation.droiconit.model.Movies;
import com.luciofm.presentation.droiconit.transitions.CircleTransition;
import com.luciofm.presentation.droiconit.transitions.Pop;
import com.luciofm.presentation.droiconit.transitions.TransitionListenerAdapter;
import com.luciofm.presentation.droiconit.util.PaletteTransformation;
import com.luciofm.presentation.droiconit.util.Utils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;

public class MoviesFragment extends BaseFragment {

    @InjectView(R.id.grid)
    GridView grid;

    TransitionInflater inflater;
    Transition defaultTransition;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = TransitionInflater.from(getActivity());
        defaultTransition = inflater.inflateTransition(R.transition.fade);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_movies;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        grid.setAdapter(new MoviesAdapter(getActivity(), Movies.movies));

        return v;
    }

    public void onMovieClick(Movie movie, MoviesAdapter.ViewHolder holder, int position) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra("MOVIE", Parcels.wrap(movie));

        holder.thumb.setTransitionName("thumb_" + position);
        grid.setClipChildren(false);

        setupTransition(position, holder);
        intent.putExtra("CURRENT_POSITION", position);
        intent.putExtra("COLOR", holder.bgColor);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                holder.thumb, "thumb");

        startActivity(intent, options.toBundle());
    }

    public void setupTransition(int position, MoviesAdapter.ViewHolder holder) {
        TransitionSet set;
        CircularPropagation propagation;
        Explode explode;
        Slide slide;
        Fade fade;
        getActivity().getWindow().setReenterTransition(defaultTransition);
        getActivity().getWindow().setExitTransition(defaultTransition);
        getActivity().getWindow().setAllowEnterTransitionOverlap(true);
        getActivity().getWindow().setAllowReturnTransitionOverlap(true);
        getActivity().setExitSharedElementCallback(null);

        switch (position) {
            case 1:
                getActivity().getWindow().setExitTransition(inflater.inflateTransition(R.transition.slide_left));
                break;
            case 2:
                set = new TransitionSet();
                fade = new Fade();
                slide = new Slide(Gravity.BOTTOM);
                propagation = new CircularPropagation();
                propagation.setPropagationSpeed(1f);
                set.addTransition(fade).addTransition(slide);
                set.setPropagation(propagation);
                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
                //set.setDuration(900);
                getActivity().getWindow().setExitTransition(set);
                break;
            case 3:
                explode = new Explode();
                propagation = new CircularPropagation();
                propagation.setPropagationSpeed(1f);
                explode.setPropagation(propagation);
                //explode.setDuration(900);
                getActivity().getWindow().setReenterTransition(explode);
                getActivity().getWindow().setExitTransition(explode);
                break;
            case 4:
                slide = new Slide(Gravity.LEFT);
                propagation = new CircularPropagation();
                propagation.setPropagationSpeed(1f);
                slide.setPropagation(propagation);
                //slide.setDuration(900);
                getActivity().getWindow().setExitTransition(slide);
                getActivity().getWindow().setReenterTransition(slide);
                getActivity().getWindow().setAllowEnterTransitionOverlap(false);
                getActivity().getWindow().setAllowReturnTransitionOverlap(false);
                break;
            case 5:
                getActivity().getWindow().setExitTransition(inflater.inflateTransition(R.transition.slide_top));
                getActivity().getWindow().setReenterTransition(inflater.inflateTransition(R.transition.slide_bottom));
                break;
            case 6:
                Pop pop = new Pop(false);
                getActivity().getWindow().setReenterTransition(pop);
                break;
            case 7:
                getActivity().setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
                        int bitmapWidth = Math.round(screenBounds.width());
                        int bitmapHeight = Math.round(screenBounds.height());
                        Bitmap bitmap = null;
                        if (bitmapWidth > 0 && bitmapHeight > 0) {
                            Matrix matrix = new Matrix();
                            matrix.set(viewToGlobalMatrix);
                            matrix.postTranslate(-screenBounds.left, -screenBounds.top);
                            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            canvas.concat(matrix);
                            sharedElement.draw(canvas);
                        }
                        return bitmap;
                    }
                });
                break;
            case 8:
                break;
        }
    }

    @Override
    public void onNextPressed() {
        View view;
        switch (++currentStep) {
            case 2:
                view = grid.getChildAt(0);
                Utils.dispatchTouch(view, 300);
                break;
            case 3:
                view = grid.getChildAt(1);
                Utils.dispatchTouch(view, 300);
                break;
            case 4:
                view = grid.getChildAt(2);
                Utils.dispatchTouch(view, 300);
                break;
            case 5:
                view = grid.getChildAt(3);
                Utils.dispatchTouch(view, 300);
                break;
            case 6:
                view = grid.getChildAt(4);
                Utils.dispatchTouch(view, 300);
                break;
            case 7:
                view = grid.getChildAt(5);
                Utils.dispatchTouch(view, 300);
                break;
            case 8:
                view = grid.getChildAt(6);
                Utils.dispatchTouch(view, 300);
                break;
            case 9:
                view = grid.getChildAt(7);
                Utils.dispatchTouch(view, 300);
                break;
            case 10:
                view = grid.getChildAt(8);
                Utils.dispatchTouch(view, 300);
                break;
            case 11:
                view = grid.getChildAt(9);
                Utils.dispatchTouch(view, 300);
                break;
            default:
                ((BaseActivity) getActivity()).nextFragment();
        }
    }

    @Override
    public void onPrevPressed() {
        if (--currentStep < 1)
            super.onPrevPressed();
    }

    @Optional
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
        return enter ? ObjectAnimator.ofFloat(null, new XFractionProperty(), 1f, 0f) :
                ObjectAnimator.ofFloat(null, new XFractionProperty(), 0f, -1f);
    }

    public class MoviesAdapter extends ArrayAdapter<Movie> {

        LayoutInflater inflater;

        public MoviesAdapter(Context context, List<Movie> objects) {
            super(context, R.layout.movie_item, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;

            if (v == null) {
                v = inflater.inflate(R.layout.movie_item, parent, false);
                holder = new ViewHolder(v);
            } else
                holder = (ViewHolder) v.getTag();

            holder.setMovie(getItem(position), position);

            return v;
        }

        public class ViewHolder {
            @InjectView(R.id.movie)
            FrameLayout root;
            @InjectView(R.id.content)
            ViewGroup content;
            @InjectView(R.id.thumb)
            ImageView thumb;
            @InjectView(R.id.title)
            TextView title;
            @InjectView(R.id.year)
            TextView year;

            Movie movie;
            int bgColor;
            int position;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
                view.setTag(this);
                view.setOnClickListener(clickListener);
            }

            public void setMovie(Movie movie, int position) {
                this.movie = movie;
                this.position = position;
                title.setText(movie.getTitle());
                year.setText(movie.getYear());

                Picasso.with(getContext()).load(movie.resId)
                        .transform(PaletteTransformation.instance())
                        .into(thumb, new PaletteTransformation.PaletteCallback(thumb) {
                            @Override
                            protected void onSuccess(Palette palette) {
                                Palette.Swatch bgSwatch = Utils.getBackgroundSwatch(palette);
                                Palette.Swatch titleSwatch = Utils.getTitleSwatch(palette);

                                title.setTextColor(titleSwatch.getTitleTextColor());
                                year.setTextColor(titleSwatch.getBodyTextColor());
                                content.setBackgroundColor(titleSwatch.getRgb());

                                bgColor = titleSwatch.getRgb();
                                Utils.colorRipple(root, bgSwatch.getRgb(), titleSwatch.getRgb());
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMovieClick(movie, (ViewHolder) v.getTag(), position);
                }
            };
        }
    }
}