package com.sasaadamovic.brzevijesti; // Your package name

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // If you are using a TextView

// You might need to import your R file if it's not automatically resolved
// import com.sasaadamovic.brzevijesti.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Make sure you have a layout file named 'fragment_home.xml' in your res/layout folder
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // --- Example: Find a TextView and set its text ---
        // TextView homeTextView = view.findViewById(R.id.textViewHomeFragment); // Assume you have this ID in fragment_home.xml
        // if (homeTextView != null) {
        //     homeTextView.setText("This is the Home Fragment");
        // }
        // --- End Example ---

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // You can do more view setup here after the view has been created
        // For example, setting up listeners, observing LiveData, etc.
    }
}