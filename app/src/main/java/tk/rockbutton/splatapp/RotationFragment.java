package tk.rockbutton.splatapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RotationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RotationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RotationFragment extends Fragment {
    private View fragmentView;

    SwipeRefreshLayout refreshLayout;

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    RotationAdapter adapter;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static RotationFragment newInstance() {
        RotationFragment fragment = new RotationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RotationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_rotation, container, false);

        refreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.rotation_swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.rotation_recyclerView);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(MainActivity.context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // specify an adapter (see also next example)
        adapter = new RotationAdapter(null);
        recyclerView.setAdapter(adapter);

        refreshItems();

        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    void refreshItems() {
        refreshLayout.setRefreshing(true);

        // Load items
        new Thread(new Runnable() {
            @Override
            public void run() {
                MapRotation mapRotation = JsonHelper.getMapRotations();
                adapter.updateDataset(mapRotation.schedule.toArray(new MapRotation.ScheduleItem[mapRotation.schedule.size()]));
            }
        }).start();

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        refreshLayout.setRefreshing(false);
    }
}
