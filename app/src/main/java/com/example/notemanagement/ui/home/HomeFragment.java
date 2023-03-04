package com.example.notemanagement.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.FragmentHomeBinding;
import com.example.notemanagement.model.Dashboard;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private List<Dashboard> dashboardList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        viewModel.getDashboardLiveData().observe(getViewLifecycleOwner(), dashboards ->{
            dashboardList = dashboards;
            for (int i = 0; i < dashboardList.size(); i++) {
                String name = dashboardList.get(i).getDashboardName();
                int rate = Integer.parseInt(dashboardList.get(i).getDashboardRate());
                pieEntries.add(new PieEntry(rate, name));
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(16f);

            PieData pieData = new PieData(pieDataSet);

            binding.fragmentHomePieChart.setData(pieData);
            binding.fragmentHomePieChart.getDescription().setEnabled(false);
            binding.fragmentHomePieChart.setCenterText("My Note");
            binding.fragmentHomePieChart.animate();
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshData();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}