package me.asl.assel.bakingapp.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.asl.assel.bakingapp.R;
import me.asl.assel.bakingapp.model.Ingredient;

public class IngredientFragment extends Fragment {

    private static final String ARG_INGREDIENT = "ingredient";
    private ArrayList<Ingredient> mIngredient = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientFragment() {
        //require empty constructor
    }

    FragmentInterface mListener;

    public static IngredientFragment newInstance(ArrayList<Ingredient> listIngredient) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_INGREDIENT, listIngredient);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIngredient = getArguments().getParcelableArrayList(ARG_INGREDIENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CustomRecyclerViewAdapter(mIngredient));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));



        View nav = view.findViewById(R.id.include);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            nav.setVisibility(View.GONE);
        } else {
            getActivity().getActionBar().setTitle("Ingredients");
            mListener.CreateNavButton(nav, 0);//index 0 always ingredients
        }
        return view;
    }

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

    class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

        private final List<Ingredient> mValues;

        CustomRecyclerViewAdapter(List<Ingredient> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mDesc.setText(holder.mItem.getIngredient());
            holder.mQty.setText(holder.mItem.getQuantity()+" "+holder.mItem.getMeasure());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mDesc;
            final TextView mQty;
            Ingredient mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mDesc = (TextView) view.findViewById(R.id.content);
                mQty = (TextView) view.findViewById(R.id.qty);
            }

        }


    }




}
