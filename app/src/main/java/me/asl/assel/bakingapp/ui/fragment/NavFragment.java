package me.asl.assel.bakingapp.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.asl.assel.bakingapp.R;
import me.asl.assel.bakingapp.model.Recipe;
import me.asl.assel.bakingapp.model.Step;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInterface} interface
 * to handle interaction events.
 * Use the {@link NavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavFragment extends Fragment {
    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_POSITION = "position";


    private Recipe recipe;
    private int position;

    private FragmentInterface mListener;

    public NavFragment() {
    }

    public static NavFragment newInstance(Recipe recipe, int pos) {
        NavFragment fragment = new NavFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        args.putInt(ARG_POSITION, pos);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(ARG_RECIPE);
            position = getArguments().getInt(ARG_POSITION, 0);
        }
    }

    @BindView(R.id.listView)
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_menu, container, false);
        ButterKnife.bind(this, view);

        List<String> stringArray = new ArrayList<>();
        stringArray.add(0, "Ingredients");
        int i = 0;
        for(Step step : recipe.getSteps()) {
            i++;
            stringArray.add(i+". "+step.getShortDescription());
        }

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_activated_1, stringArray);
        listView.setDivider(getContext().getDrawable(R.color.colorAccent));
        listView.setDividerHeight(16);
        listView.setOnItemClickListener(itemClickListener);
        listView.setAdapter(adapter);


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setItemChecked(position, true);
        }
        listView.smoothScrollToPosition(position);

        getActivity().getActionBar().setTitle(recipe.getName());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mListener.FragmentChange(i);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterface) {
            mListener = (FragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }


}
